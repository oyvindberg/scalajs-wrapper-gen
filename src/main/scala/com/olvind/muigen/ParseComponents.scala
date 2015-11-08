package com.olvind.muigen

import argonaut.Argonaut._

import scalaz.{-\/, \/-}

object StringUtils{
  def padTo(s: String)(n: Int) = s + (" " * (n - s.length))
}

case class OutFile(filename: String, content: String)

object ParseComponents{
  def apply(comp: Component): Seq[OutFile] = {

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

        methodSectionOpt zip methodClassOpt foreach {
          case (section: JsonSection, methodOut) =>
            section.infoArray.foreach { f =>
              methodOut.addField(ReqField(f.name, OutParamClass("Unit"), Some(f)))
            }
        }
        outComponent(comp, out) ++ (methodClassOpt map outMethodClass)
      case -\/(error) => throw new RuntimeException(error)
    }
  }

  def outComponent(comp: Component, c: OutComponentClass): Seq[OutFile] = {
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
    val content = Seq(p1, p2, body).mkString("\n")

    Seq(OutFile(comp.name, content)) ++ (c.enumClases map outEnumClass)
  }

  def outEnumClass(c: OutEnumClass): OutFile = {
    val content =
    s"""
       |class ${c.name}(val value: String) extends AnyVal
       |object ${c.name}{
       |${c.members.map {
        m =>
          val memberName = if (m.forall(_.isDigit)) "_" + m else m
          val memberName_ = memberName.toUpperCase.replace("-", "_")
          s"""\tval $memberName_ = new ${c.name}("$m")"""
        }.mkString("\n")}
       |}""".stripMargin
    OutFile(c.name, content)
  }

  def outMethodClass(c: OutMethodClass): OutFile = {
    val content = s"""
       |@js.native
       |class ${c.name} extends js.Object{
       |${c.fields.map(m => s"${m.comment}\tdef ${m.name}(): Unit = js.native").mkString("\n\n")}
       |}""".stripMargin
    OutFile(c.name, content)
  }
}