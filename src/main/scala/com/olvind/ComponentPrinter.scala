package com.olvind

case class FieldStats(maxFieldNameLen: Int, maxTypeNameLen: Int)

sealed trait OutFile
case class PrimaryOutFile(filename: CompName, content: String, secondaries: Seq[SecondaryOutFile]) extends OutFile
case class SecondaryOutFile(filename: String, content: String) extends OutFile

object ComponentPrinter {
  def apply(prefix: String, comp: ParsedComponent): (PrimaryOutFile, Seq[SecondaryOutFile]) = {
    val fs: FieldStats =
      FieldStats(
        maxFieldNameLen = comp.fields.map(_.fieldNameLength).max,
        maxTypeNameLen  = comp.fields.map(_.typeNameLength).max
      )

    val d1 = if (comp.definition.deprecated) "@deprecated\n" else ""
    val p1 = s"\n${d1}case class $prefix${comp.name}("
    val p2 = comp.fields.filterNot(_.name == PropName("children")).map(
      p => outProp(p, fs)
    ).mkString("", ",\n", ")")

    val body = s"""{
      |
      |${indent(1)}def apply() = {
      |${indent(2)}val props = JSMacro[$prefix${comp.name}](this)
      |${indent(2)}val f = React.asInstanceOf[js.Dynamic].createFactory($prefix.${comp.name.value})
      |${indent(2)}f(props).asInstanceOf[ReactComponentU_]
      |${indent(1)}}
      |}
    """.stripMargin

    def bodyChildren(c: ParsedProp): String = {
      val cd = c.commentOpt.fold("")(d =>
        s"""${indent(1)}/**
           |${indent(1)} * @param children $d
           |${indent(1)} */""".stripMargin
      )

      if (comp.definition.multipleChildren)
        s"""{
           |$cd
           |${indent(1)}def apply(children: ${c.baseType.typeName}*) = {
           |${indent(2)}val props = JSMacro[$prefix${comp.name}](this)
           |${indent(2)}val f = React.asInstanceOf[js.Dynamic].createFactory($prefix.${comp.name.value})
           |${indent(2)}if (children.isEmpty)
           |${indent(3)}f(props).asInstanceOf[ReactComponentU_]
           |${indent(2)}else
           |${indent(3)}f(props, children.toJsArray).asInstanceOf[ReactComponentU_]
           |${indent(1)}}
           |}""".stripMargin
      else
        s"""{
           |$cd
           |${indent(1)}def apply(children: ${c.typeName} = js.undefined) = {
           |${indent(2)}val props = JSMacro[$prefix${comp.name}](this)
           |${indent(2)}val f = React.asInstanceOf[js.Dynamic].createFactory($prefix.${comp.name.value})
           |${indent(2)}f(props, children).asInstanceOf[ReactComponentU_]
           |${indent(1)}}
           |}""".stripMargin
      }

    val content      = (Seq(p1, p2, comp.childrenOpt.fold(body)((c: ParsedProp) => bodyChildren(c))) ++ comp.definition.postlude.toSeq).mkString("\n")
    val outFile      = PrimaryOutFile(comp.name, content, comp.methodClassOpt.toSeq map outMethodClass)
    val outEnumFiles = comp.enumClases map outEnumClass

    (outFile, outEnumFiles)
  }

  def outComment(commentOpt: Option[PropComment], inheritedFrom: Option[CompName]): String = {
    val lines = commentOpt.toSeq ++ inheritedFrom.map(i => s"(Passed on to $i)")
    if (lines.isEmpty) ""
    else lines.mkString(s"${indent(1)}/* ", s"\n${indent(1)}", "*/\n")
  }

  def outProp(p: ParsedProp, fs: FieldStats): String = {
    val comment = outComment(p.commentOpt, p.inheritedFrom)

    val intro: String = {
      val fixedName: String =
        if (p.name.value == "type") "`type`" else p.name.value
      val deprecated = p.deprecatedMsg.fold("")(msg => s"""${indent(1)}@deprecated("$msg")\n""")
      s"$comment$deprecated${indent(1)}${padTo(fixedName + ": ")(fs.maxFieldNameLen + 2)}"
    }

    p.isRequired match {
      case true  => intro + p.typeName
      case false => intro + padTo(p.typeName)(fs.maxTypeNameLen) + " = js.undefined"
    }
  }

  def outEnumClass(c: ParsedEnumClass): SecondaryOutFile =
    SecondaryOutFile(
      c.name,
      s"""
         |class ${c.name}(val value: String) extends AnyVal
         |object ${c.name} {
         |${c.identifiers.map {
            case (ident, original) =>
              s"""${indent(1)}val ${ident.value} = new ${c.name}("$original")"""
          }.mkString("\n")}
         |${indent(1)}val values = ${c.identifiers.map(_._1.value).toList}
         |}""".stripMargin
    )

  def outMethodClass(c: ParsedMethodClass): SecondaryOutFile =
    SecondaryOutFile(
      c.className,
      s"""
         |@js.native
         |class ${c.className} extends js.Object {
         |${c.ms.map{m =>
            val deprecated = if (m.asInstanceOf[Object].toString.toLowerCase.contains("deprecated")) s"${indent(1)}@deprecated\n" else ""
            val comment = outComment(m.commentOpt, None)
            s"$comment$deprecated${indent(1)}def ${m.definition} = js.native"
            }.mkString("\n\n")
           }
         |}""".stripMargin
    )
}