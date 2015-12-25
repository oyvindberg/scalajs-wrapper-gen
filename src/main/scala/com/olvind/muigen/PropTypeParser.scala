package com.olvind.muigen

import java.io.File

import ammonite.ops._

import scala.collection.mutable

case class Prop(tpe: String,
                deprecatedMsg: Option[String] = None,
                required: Boolean = false){
  def mapType(f: String => String) = copy(tpe = f(tpe))
}

sealed trait Ret
case class Folder(name: String, cs: Seq[Ret]) extends Ret
case class Comp(name: String, childrenOpt: Option[Prop], props: Map[String, Prop]) extends Ret

object PropTypeRunner extends App {
  def indented(n: Int)(s: String) =
    println((" " * n) + s)

  def print(indent: Int)(r: Ret): Unit = {
    r match {
      case Folder(name, cs) =>
        println("-" * 20)
        indented(indent)(name)
        cs foreach print(indent + 2)
        println("-" * 20)
      case Comp(compName, hasChildren, props) =>
        indented(indent)(compName + ":")
        props foreach {case (name, tpe) => indented(indent + 2)(name + " -> " + tpe)}
    }
  }
  val result: Ret = PropTypeParser(home / "pr" / "material-ui" / "lib")
  print(0)(result)
}

object PropTypeParser {
  val Pattern = "var\\s+(\\S+)\\s+=\\s*require\\('([^']+)'\\).*".r

  def add(p: Path, frag: String): Path =
    frag
      .split("/")
      .filterNot(_.isEmpty)
      .filterNot(_ == ".")
      .foldLeft(p)(_ / _)

  def exists(path: Path): Boolean =
    new File(path.toString).exists()

  def apply(p: Path): Ret = {
    val index      = p / "index.js"
    val comps      = read.lines.!!(index).toList.collect {
      case Pattern(name, file) =>
        val jsFile = add(p, file + ".js")
        if (exists(jsFile)) {
          JsParser(jsFile)
        }
        else Seq(apply(add(p, file)))
//      case other =>
//        println(other)
//        Seq.empty
//
    }
    Folder(p.toString, comps.flatten)
  }
}

object JsParser{
  import jdk.nashorn.internal.ir._
  import jdk.nashorn.internal.parser.Parser
  import jdk.nashorn.internal.runtime.options.Options
  import jdk.nashorn.internal.runtime.{Context, ErrorManager, Source}

  import scala.collection.JavaConverters._

  val options = new Options("nashorn")
  options.set("anon.functions", true)
  options.set("parse.only", true)
  options.set("scripting", true)

  case class CompFound(name: String, jsContent: String, propType: ObjectNode)

  def apply(jsFile: Path): Seq[Comp] = {
    val content = read.lines(jsFile).toList.mkString("\n")

    /* setup */
    val errors  = new ErrorManager()
    val context = new Context(options, errors, Thread.currentThread().getContextClassLoader)
    val source  = Source.sourceFor(jsFile.toString, content)
    val parser  = new Parser(context.getEnv, source, errors)

    val parsed     = parser.parse()
    val statements = parsed.getBody.getStatements.asScala

    val topLevelVars = statements collect {
      case v: VarNode => v
    }
    val comps = mutable.ArrayBuffer[CompFound]()

    topLevelVars foreach {
      v => Some(v.getInit) collect {
        case c: CallNode => c.getArgs.asScala.headOption collect {
          case o: ObjectNode =>
            o.getElements.asScala foreach {
              case p: PropertyNode =>
                Some(p.getKey) collect {
                  case i: IdentNode if i.getName == "propTypes" =>
                    p.getValue match {
                      case propTypes: ObjectNode =>
                        comps += CompFound(v.getName.getName, content.substring(v.getStart, v.getFinish), propTypes)
                    }
                }
            }
        }
      }
    }
//    println(s"PropTypeParser: ${jsFile.last}: Found ${comps.toList.map(_.name)}")
    val ret = comps.toList map parsePropTypes(jsFile)
//    println(ret)
    ret
  }

  def unmentionedProps(content: String): Set[String] = {
    val pattern = "this\\.props\\.(\\w+)".r
    val asd = pattern.findAllIn(content).toSet
    val asd2 = asd map (_.replace("this.props.", ""))
    asd2
  }

  def parsePropTypes(jsFile: Path)(c: CompFound): Comp = c match {
    case CompFound(name, jsContent, o) =>
      val props = o.getElements.asScala.map{
        case p: PropertyNode =>
          p.getKeyName -> parsePropType(jsFile)(p.getValue)
      }
      val allUmentioned = unmentionedProps(c.jsContent)
      val hasChildren = allUmentioned.contains("children")
      val unWanted = Set("children", "ref", "key", "valueLink", "hasOwnProperty")
      val unmentioned = allUmentioned
        .filterNot(unWanted)
        .filterNot(p => props.exists(_._1 == p))
        .map(u => u -> "UNKNOWN")

      if (unmentioned.nonEmpty)
        throw new RuntimeException(s"UMENTIONED for ${c.name}: $unmentioned")

      Comp(name, props.collectFirst{case ("children", t) => t}, props.filterNot(p => unWanted.contains(p._1)).toMap)

  }

  def parsePropType(jsFile: Path)(e: Expression): Prop = e match {
    case a: AccessNode =>
      parsePropType(jsFile)(a.getBase).mapType(_ + "." + a.getProperty)
    case i: IdentNode  =>
      Prop(i.getName)

    case c: CallNode =>
      c.getFunction match {
        case b: BinaryNode if b.toString().toLowerCase.contains("deprecated") =>
          val ret = parsePropType(jsFile)(c.getArgs.get(0))
          val re2 = ret.copy(deprecatedMsg = Some(c.getArgs.get(1).toString))
          re2
        case _ =>
          val params = c.getArgs.asScala.map(e => parsePropType(jsFile)(e)).map(_.tpe).mkString(", ")
          parsePropType(jsFile)(c.getFunction).mapType(_ + "(" + params + ")")
      }

    case l: LiteralNode.ArrayLiteralNode =>
      Prop((l.getValue map parsePropType(jsFile) map (_.tpe)).mkString(","))

    case l: LiteralNode[_] =>
      val s = l.getValue match {
        case s: String => s""""$s""""
        case other => other.toString
      }
      Prop(s)

    case l: IndexNode  =>
      println(s"$jsFile ignoring $l")
      Prop("React")
  }
}