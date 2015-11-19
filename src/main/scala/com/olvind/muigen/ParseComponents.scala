package com.olvind.muigen

import argonaut.Argonaut._

import scalaz.{-\/, \/-}

object StringUtils{
  def padTo(s: String)(n: Int) = s + (" " * (n - s.length))
}

case class OutFile(filename: String, content: String, secondaries: Seq[SecondaryOutFile])
case class SecondaryOutFile(filename: String, content: String)

object ParseComponents{
  def apply(comp: Component): OutFile = {

    comp.json.decodeEither[List[JsonSection]] match {
      case \/-(sections) =>
        val sMap: Map[String, JsonSection] = sections.map(s => s.name -> s).toMap
        val methodSectionOpt   = sMap.get(comp.overrideMethods getOrElse "Methods")
        val eventsSectionOpt   = sMap.get(comp.overrideEvents getOrElse "Events")
        val propsFields        = comp.propsSections.map(sMap.apply).flatMap(_.infoArray)
        val out                = OutComponentClass(comp.name)
        val methodClassOpt     = methodSectionOpt.map(_ => comp.name + "M").map(OutMethodClass)

        out.addField(OptField("key", OutParam.mapType(comp.name, "key")("string"), None))
        out.addField(OptField("ref", OutParamClass(methodClassOpt.fold("String")(_.name + " => Unit")), None))

        propsFields.sortBy(_.name).foreach{ f =>
          if (f.name == "label or children") out.addField(OptField("label", OutParamClass("String"), Some(f)))
          else if (f.isRequired) out.addField(ReqField(f.name, OutParam.mapType(comp.name, f.name)(f.`type` getOrElse f.header), Some(f)))
          else              out.addField(OptField(f.name, OutParam.mapType(comp.name, f.name)(f.`type` getOrElse f.header), Some(f)))
        }
        eventsSectionOpt.toList.flatMap(_.infoArray).foreach { f =>
          if (f.isRequired) out.addField(ReqField(f.name, OutParam.mapFunction(comp.name, f.name, f.`type` getOrElse f.header), Some(f)))
          else              out.addField(OptField(f.name, OutParam.mapFunction(comp.name, f.name, f.`type` getOrElse f.header), Some(f)))
        }
        comp.shared.foreach(_.inheritProps.foreach(out.addField))
        comp.shared.foreach(_.inheritEvents.foreach(out.addField))

        PropTypeLib.results.get(comp.name).foreach{
          m => m.foreach{
            case (n, f) => out.addField(f)
          }
        }

        methodSectionOpt zip methodClassOpt foreach {
          case (section: JsonSection, methodOut) =>
            section.infoArray.foreach { f =>
              methodOut.addField(ReqField(f.name, OutParamClass("Unit"), Some(f)))
            }
        }
        outComponent(comp, out, methodClassOpt)
      case -\/(error) => throw new RuntimeException(comp.toString + error)
    }
  }

  def outComponent(comp: Component, c: OutComponentClass, methodClassOpt: Option[OutMethodClass]): OutFile = {
    val fs = c.fieldStats
    val p1 = s"\ncase class ${comp.name}("
    val p2 = c.fields.map(_.toString(fs)).mkString("", ",\n", ")")
    val body = s"""{
      |
      |  def apply() = {
      |    val props = JSMacro[${comp.name}](this)
      |    val f = React.asInstanceOf[js.Dynamic].createFactory(Mui.${comp.name.replace("Mui", "")})
      |    f(props).asInstanceOf[ReactComponentU_]
      |  }
      |}
    """.stripMargin
    val bodyChildren =
      s"""{
         |
         |  def apply(children: ReactNode*) = {
         |    val props = JSMacro[${comp.name}](this)
         |    val f = React.asInstanceOf[js.Dynamic].createFactory(Mui.${comp.name.replace("Mui", "")})
         |    f(props, children.toJsArray).asInstanceOf[ReactComponentU_]
         |  }
         |}""".stripMargin

    val content = (Seq(p1, p2, if (comp.children) bodyChildren else body) ++ comp.postlude.toSeq).mkString("\n")

    OutFile(comp.name, content, (c.enumClases map outEnumClass) ++ (methodClassOpt map outMethodClass))
  }

  def outEnumClass(c: OutEnumClass): SecondaryOutFile = {
    val fixedNames: Seq[(String, String)] = c.members.map { m =>
      val memberName = if (m.head.isDigit) "_" + m else m
      (m, memberName.toUpperCase.replace("-", "_"))
    }
    val content =
    s"""
       |class ${c.name}(val value: String) extends AnyVal
       |object ${c.name}{
       |${fixedNames.map {
          case (original, fixed) =>
            s"""\tval $fixed = new ${c.name}("$original")"""
        }.mkString("\n")}
       |\tval values = ${fixedNames.map(_._2).toList}
       |}""".stripMargin
    SecondaryOutFile(c.name, content)
  }

  def outMethodClass(c: OutMethodClass): SecondaryOutFile = {
    val content = s"""
       |@js.native
       |class ${c.name} extends js.Object{
       |${c.fields.map{m =>
          val deprecated = if (m.asInstanceOf[Object].toString.toLowerCase.contains("deprecated")) "\t@deprecated\n" else ""
          s"${m.comment}$deprecated\tdef ${m.name}(): Unit = js.native"
          }.mkString("\n\n")
         }
       |}""".stripMargin
    SecondaryOutFile(c.name, content)
  }
}