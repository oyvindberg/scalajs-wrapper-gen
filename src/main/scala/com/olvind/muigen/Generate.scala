package com.olvind.muigen

import argonaut.Argonaut._

import scala.collection.mutable
import scalaz.{-\/, \/-}

object StringUtils{
  def padTo(s: String)(n: Int) = s + (" " * (n - s.length))
}

/* in */

case class JsonField(name: String, `type`: Option[String], header: String, desc: String){
  def isRequired: Boolean =
    header.toLowerCase.contains("required")
}
case class JsonSection(name: String, infoArray: List[JsonField])

object JsonSection{
  implicit def FieldCodecJson =
    casecodec4(JsonField.apply, JsonField.unapply)("name", "type", "header", "desc")
  implicit def SectionCodecJson =
    casecodec2(JsonSection.apply, JsonSection.unapply)("name", "infoArray")
}

/* out */


sealed trait OutClass {
  val fields: mutable.ArrayBuffer[OutField] =
    mutable.ArrayBuffer.empty[OutField]

  def addField(f: OutField) =
    fields += f

  def fieldStats =
    FieldStats(
      maxFieldNameLen = fields.map(_.fieldNameLength).max,
      maxTypeNameLen  = fields.map(_.typeNameLength).max
    )

  def enumClases: Seq[OutEnumClass] =
    fields.map(_.baseType).collect{
      case o: OutParamEnum => o.enumClass
    }
}

case class FieldStats(maxFieldNameLen: Int, maxTypeNameLen: Int)

case class OutComponentClass(name: String) extends OutClass
case class OutMethodClass(name: String) extends OutClass
case class OutEnumClass(name: String, members: Seq[String])

case class Generate(comp: Component) {

  comp.json.decodeEither[List[JsonSection]] match {
    case \/-(sections) =>
      val sMap: Map[String, JsonSection] = sections.map(s => s.name -> s).toMap
      val methodSectionOpt   = sMap.get(comp.overrideMethods getOrElse "Methods")
      val eventsSectionOpt   = sMap.get(comp.overrideEvents getOrElse "Events")
      val propsSection       = sMap(comp.overrideProps getOrElse  "Props")
      val out                = OutComponentClass(comp.name)
      val methodClassOpt     = methodSectionOpt.map(_ => comp.name + "M").map(OutMethodClass)

      out.addField(OptField("ref", OutParam.mapType(comp.name, "ref")("string"), None))
      out.addField(OptField("key", OutParamClass(methodClassOpt.fold("String")(_.name + " => Unit")), None))

      propsSection.infoArray.sortBy(_.name).foreach{ f =>
        if (f.name == "label or children") out.addField(OptField(f.name, OutParamClass("String"), Some(f)))
        else if (f.isRequired) out.addField(ReqField(f.name, OutParam.mapType(comp.name, f.name)(f.`type` getOrElse f.header), Some(f)))
        else              out.addField(OptField(f.name, OutParam.mapType(comp.name, f.name)(f.`type` getOrElse f.header), Some(f)))
      }
      eventsSectionOpt.toList.flatMap(_.infoArray).foreach { f =>
        if (f.isRequired) out.addField(ReqField(f.name, OutParam.mapFunction(comp.name, f.name, f.`type` getOrElse f.header), Some(f)))
        else              out.addField(OptField(f.name, OutParam.mapFunction(comp.name, f.name, f.`type` getOrElse f.header), Some(f)))
      }
      comp.shared.foreach(_.inheritProps.foreach(out.addField))
      comp.shared.foreach(_.inheritEvents.foreach(out.addField))

      methodSectionOpt zip methodClassOpt foreach {
        case (section: JsonSection, methodOut) =>
          section.infoArray.foreach { f =>
            methodOut.addField(ReqField(f.name, OutParamClass("Unit"), Some(f)))
          }
      }
      printClass(out)
      methodClassOpt foreach printMethodClass
    case -\/(error) => println(error)
  }

  def printClass(c: OutComponentClass) = {
        val fs = c.fieldStats
        println(s"case class ${comp.name}(")
        print(c.fields.map(_.toString(fs)).mkString("", ",\n", ")"))
        val body = s"""{
          |
          |  def apply() = {
          |    val props = JSMacro[${comp.name}](this)
          |    val f = React.asInstanceOf[js.Dynamic].createFactory(Mui.${comp.name.replace("Mui", "")})
          |    f(props).asInstanceOf[ReactComponentU_]
          |  }
          |}
        """.stripMargin
        println(body)

        c.enumClases foreach printEnumClass
  }

  def printEnumClass(c: OutEnumClass) = {
    val base =
      s"""
         |class ${c.name}(val value: String) extends AnyVal
         |object ${c.name}{
         |${c.members.map(m => s"""\tval ${m.toUpperCase.replace("-", "_")} = new ${c.name}("$m")""").mkString("\n")}
         |}""".stripMargin
    println(base)
  }

  def printMethodClass(c: OutMethodClass) = {
    val base =
      s"""
         |@js.native
         |class ${c.name} extends js.Object{
         |${c.fields.map(m => s"${m.comment}\tdef ${m.name}(): Unit = js.native").mkString("\n\n")}
         |}""".stripMargin
    println(base)
  }
}

object RunGenerate extends App{
  Component.components foreach Generate
}