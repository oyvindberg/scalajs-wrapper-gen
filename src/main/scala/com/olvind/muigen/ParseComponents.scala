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
      case o: PropTypeEnum => o.enumClass
    }
}

case class FieldStats(maxFieldNameLen: Int, maxTypeNameLen: Int)

case class OutComponentClass(name: CompName) extends OutClass{
  def childrenOpt = fields.find(_.name.value == "children")
}
case class OutMethodClass(name: String) extends OutClass
case class OutEnumClass(name: String, fixedNames: Seq[(Identifier, String)])

sealed trait OutFile
case class PrimaryOutFile(filename: CompName, content: String, secondaries: Seq[SecondaryOutFile]) extends OutFile
case class SecondaryOutFile(filename: String, content: String) extends OutFile

object ParseComponents {
  def apply(allComps: Map[CompName, gen.Component])
           (muiComp:  ManualComponent): Seq[OutFile] = {

    val (commentMap, methodClassOpt) = MuiDocs(muiComp)

    val propTypes: Map[PropName, OriginalProp] =
      allComps.get(muiComp.name).flatMap(_.propsOpt).getOrElse(
        throw new RuntimeException(s"No Proptypes found for ${muiComp.name}")
      )

    val inheritedProps: Map[PropName, OriginalProp] =
      muiComp.shared match {
        case None         => Map.empty
        case Some(shared) =>
          allComps.get(shared).flatMap(_.propsOpt).getOrElse(
            throw new RuntimeException(s"No Proptypes found for $shared")
          )
      }

    val out = OutComponentClass(muiComp.name)

    out.addField(OptField(PropName("key"), PropTypeClass("String"), None, None, None))
    out.addField(OptField(PropName("ref"), PropTypeClass(methodClassOpt.fold("String")(c => prefix + c.name + " => Unit")), None, None, None))

    (inheritedProps ++ propTypes).toSeq.sortBy(p => (p._2.origComp != muiComp.name, p._1.clean.value)).foreach{
      case (name, OriginalProp(origComp, tpe, commentOpt)) =>
        val field = OutField(muiComp.name, origComp, name, tpe, commentOpt orElse (commentMap get name))
        out.addField(field)
    }

    outComponent(muiComp, out, methodClassOpt)
  }

  def outComponent(comp: ManualComponent, c: OutComponentClass, methodClassOpt: Option[OutMethodClass]): Seq[OutFile] = {
    val fs = c.fieldStats
    val d1 = if (comp.deprecated) "@deprecated\n" else ""
    val p1 = s"\n${d1}case class $prefix${comp.name}("
    val p2 = c.fields.filterNot(_.name == PropName("children")).map(_.toString(fs)).mkString("", ",\n", ")")
    val body = s"""{
      |
      |  def apply() = {
      |    val props = JSMacro[$prefix${comp.name}](this)
      |    val f = React.asInstanceOf[js.Dynamic].createFactory($prefix.${comp.name.value})
      |    f(props).asInstanceOf[ReactComponentU_]
      |  }
      |}
    """.stripMargin

    def bodyChildren(c: OutField): String = {
      val cd = c.commentOpt.fold("")(d =>
        s"""  /**
           |   * @param children $d
           |   */""".stripMargin)

      if (comp.multipleChildren)
        s"""{
           |$cd
           |  def apply(children: ${c.baseType.typeName}*) = {
           |    val props = JSMacro[$prefix${comp.name}](this)
           |    val f = React.asInstanceOf[js.Dynamic].createFactory($prefix.${comp.name.value})
           |    if (children.isEmpty)
           |      f(props).asInstanceOf[ReactComponentU_]
           |    else
           |      f(props, children.toJsArray).asInstanceOf[ReactComponentU_]
           |  }
           |}""".stripMargin
      else
        s"""{
           |$cd
           |  def apply(children: ${c.typeName} = js.undefined) = {
           |    val props = JSMacro[$prefix${comp.name}](this)
           |    val f = React.asInstanceOf[js.Dynamic].createFactory($prefix.${comp.name.value})
           |    f(props, children).asInstanceOf[ReactComponentU_]
           |  }
           |}""".stripMargin
      }

    val content      = (Seq(p1, p2, c.childrenOpt.fold(body)((c: OutField) => bodyChildren(c))) ++ comp.postlude.toSeq).mkString("\n")
    val outFile      = PrimaryOutFile(comp.name, content, methodClassOpt.toSeq map outMethodClass)
    val outEnumFiles = c.enumClases map outEnumClass

    outFile +: outEnumFiles
  }

  def outEnumClass(c: OutEnumClass): SecondaryOutFile = {
    val content =
    s"""
       |class ${c.name}(val value: String) extends AnyVal
       |object ${c.name}{
       |${c.fixedNames.map {
          case (fixed, original) =>
            s"""  val ${fixed.value} = new ${c.name}("$original")"""
        }.mkString("\n")}
       |  val values = ${c.fixedNames.map(_._1.value).toList}
       |}""".stripMargin
    SecondaryOutFile(c.name, content)
  }

  def outMethodClass(c: OutMethodClass): SecondaryOutFile = {
    val content = s"""
       |@js.native
       |class $prefix${c.name} extends js.Object{
       |${c.fields.map{m =>
          val deprecated = if (m.asInstanceOf[Object].toString.toLowerCase.contains("deprecated")) "  @deprecated\n" else ""
          s"${m.comment}$deprecated  def ${m.name.value}(): Unit = js.native"
          }.mkString("\n\n")
         }
       |}""".stripMargin
    SecondaryOutFile(c.name, content)
  }
}