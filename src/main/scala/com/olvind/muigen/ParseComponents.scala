package com.olvind.muigen

import argonaut.Argonaut._

import scalaz.{-\/, \/-}

object StringUtils{
  def padTo(s: String)(n: Int) = s + (" " * (n - s.length))
}

case class OutFile(filename: String, content: String, secondaries: Seq[SecondaryOutFile])
case class SecondaryOutFile(filename: String, content: String)

object ParseComponents{
  def diagnose(compName: String, propTypes: Map[String, OutField], propsFromDoc: Seq[JsonField], eventsFromDoc: List[JsonField]) = {
    val fromDoc: Seq[JsonField] = propsFromDoc ++ eventsFromDoc
    fromDoc.foreach{
      case f =>
        val fname = f.name.replaceAll("Deprecated:", "").replaceAll("or children", "")
        if (!propTypes.contains(fname)){
        println(s"$compName: ${fname} (${f.`type`})")
      }
    }
//    propTypes foreach {
//      case (key, field) =>
//        if (!fromDoc.exists(_.name == key)){
//          println(s"$compName: $key (${field.typeName})")
//        }
//    }
  }
  def apply(comp: Component): OutFile = {

    comp.json.decodeEither[List[JsonSection]] match {
      case \/-(sections) =>
        PropTypeLib.results.toSeq.sortBy(_._1) foreach println
        val propTypes: Map[String, OutField] =
          PropTypeLib.results.getOrElse(comp.name,
            throw new RuntimeException(s"No Proptypes found for ${comp.name}")
          )

        val sMap: Map[String, JsonSection] = sections.map(s => s.name -> s).toMap
        val methodSectionOpt   = sMap.get(comp.overrideMethods getOrElse "Methods")
        val eventsFromDoc      = sMap.get(comp.overrideEvents getOrElse "Events").toList.flatMap(_.infoArray)
        val propsFromDoc       = comp.propsSections.map(sMap.apply).flatMap(_.infoArray)

        val out                = OutComponentClass(comp.name)
        val methodClassOpt     = methodSectionOpt.map(_ => comp.name + "M").map(OutMethodClass)

        diagnose(comp.name, propTypes, propsFromDoc, eventsFromDoc)

        out.addField(OptField("key", OutParam.mapType(comp.name, "key")("string"), None))
        out.addField(OptField("ref", OutParamClass(methodClassOpt.fold("String")(_.name + " => Unit")), None))

        propTypes.toSeq.sortBy(_._1).foreach{ f =>
//          println(f)
//          val fieldName = f.name.replaceAll("Deprecated:", "").replaceAll("or children", "").trim
//          if (f.isRequired) out.addField(ReqField(fieldName, OutParam.mapType(comp.name, fieldName)(f.`type` getOrElse f.header), Some(f)))
//          else                   out.addField(OptField(fieldName, OutParam.mapType(comp.name, fieldName)(f.`type` getOrElse f.header), Some(f)))
        }


        propsFromDoc.sortBy(_.name).foreach{ f =>
          val fieldName = f.name.replaceAll("Deprecated:", "").replaceAll("or children", "").trim
          if (f.isRequired) out.addField(ReqField(fieldName, OutParam.mapType(comp.name, fieldName)(f.`type` getOrElse f.header), Some(f)))
          else                   out.addField(OptField(fieldName, OutParam.mapType(comp.name, fieldName)(f.`type` getOrElse f.header), Some(f)))
        }
        eventsFromDoc.foreach { f =>
          val fieldName = f.name.replaceAll("Deprecated: ", "")
          if (f.isRequired) out.addField(ReqField(fieldName, OutParamClass(FunctionTypes(comp.name, fieldName)), Some(f)))
          else              out.addField(OptField(fieldName, OutParamClass(FunctionTypes(comp.name, fieldName)), Some(f)))
        }
        comp.shared.foreach(_.inheritProps.foreach(out.addField))
        comp.shared.foreach(_.inheritEvents.foreach(out.addField))

        propTypes.foreach{
          case (n, f) => out.addField(f)
        }

        methodSectionOpt zip methodClassOpt foreach {
          case (section: JsonSection, methodOut) =>
            section.infoArray.foreach { f =>
              val fieldName = f.name.replaceAll("Deprecated:", "").replaceAll("or children", "").trim
              methodOut.addField(ReqField(fieldName, OutParamClass("Unit"), Some(f)))
            }
        }
        outComponent(comp, out, methodClassOpt)
      case -\/(error) => throw new RuntimeException(comp.name + ": " + error)
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