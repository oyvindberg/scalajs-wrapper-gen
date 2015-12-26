package com.olvind
package muigen

import scala.collection.mutable

sealed trait OutClass {
  val fields: mutable.ArrayBuffer[OutField] =
    mutable.ArrayBuffer.empty[OutField]

  def addField(f: OutField) =
    if (fields.exists(_.name == f.name)) println(s"$this: Duplicate field! $f")
    else fields += f

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

case class OutComponentClass(name: CompName) extends OutClass{
  def childrenOpt = fields.find(_.name.value == "children")
}
case class OutMethodClass(name: String) extends OutClass
case class OutEnumClass(name: String, members: Seq[String])

case class OutFile(filename: CompName, content: String, secondaries: Seq[SecondaryOutFile])
case class SecondaryOutFile(filename: String, content: String)

object ParseComponents {
  def apply(allComps: Map[CompName, gen.Component])
           (muiComp:  MuiComponent): OutFile = {

    val (commentMap, methodClassOpt) = MuiDocs(muiComp)

    val propTypes: Map[PropName, (PropString, Option[PropComment])] =
      allComps.get(muiComp.name.map(_.replaceAll("Mui", ""))).flatMap(_.propsOpt).getOrElse(
        throw new RuntimeException(s"No Proptypes found for ${muiComp.name}")
      )

    val out = OutComponentClass(muiComp.name)

    out.addField(OptField(PropName("key"), OutParamClass("String"), None, None))
    out.addField(OptField(PropName("ref"), OutParamClass(methodClassOpt.fold("String")(_.name + " => Unit")), None, None))

    propTypes.toSeq.sortBy(_._1.clean.value).foreach{
      case (name, (tpe, commentOpt)) =>
        val field = OutField(muiComp.name, name, tpe, commentOpt orElse (commentMap get name))
        out.addField(field)
    }

    muiComp.shared.foreach(_.inheritProps.foreach(out.addField))
    muiComp.shared.foreach(_.inheritEvents.foreach(out.addField))

    outComponent(muiComp, out, methodClassOpt)
  }

  def outComponent(comp: MuiComponent, c: OutComponentClass, methodClassOpt: Option[OutMethodClass]): OutFile = {
    val fs = c.fieldStats
    val p1 = s"\ncase class ${comp.name}("
    val p2 = c.fields.map(_.toString(fs)).mkString("", ",\n", ")")
    val body = s"""{
      |
      |  def apply() = {
      |    val props = JSMacro[${comp.name}](this)
      |    val f = React.asInstanceOf[js.Dynamic].createFactory(Mui.${comp.name.value.replace("Mui", "")})
      |    f(props).asInstanceOf[ReactComponentU_]
      |  }
      |}
    """.stripMargin
    def bodyChildren(c: OutField) =
      s"""{
         |
         |  def apply(children: ${c.typeName}) = {
         |    val props = JSMacro[${comp.name}](this)
         |    val f = React.asInstanceOf[js.Dynamic].createFactory(Mui.${comp.name.value.replace("Mui", "")})
         |    f(props, children).asInstanceOf[ReactComponentU_]
         |  }
         |}""".stripMargin

    val content = (Seq(p1, p2, c.childrenOpt.fold(body)((c: OutField) => bodyChildren(c))) ++ comp.postlude.toSeq).mkString("\n")

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
            s"""  val $fixed = new ${c.name}("$original")"""
        }.mkString("\n")}
       |  val values = ${fixedNames.map(_._2).toList}
       |}""".stripMargin
    SecondaryOutFile(c.name, content)
  }

  def outMethodClass(c: OutMethodClass): SecondaryOutFile = {
    val content = s"""
       |@js.native
       |class ${c.name} extends js.Object{
       |${c.fields.map{m =>
          val deprecated = if (m.asInstanceOf[Object].toString.toLowerCase.contains("deprecated")) "  @deprecated\n" else ""
          s"${m.comment}$deprecated  def ${m.name.value}(): Unit = js.native"
          }.mkString("\n\n")
         }
       |}""".stripMargin
    SecondaryOutFile(c.name, content)
  }
}