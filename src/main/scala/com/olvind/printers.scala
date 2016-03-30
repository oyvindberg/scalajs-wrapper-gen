package com.olvind

sealed trait OutFile
case class PrimaryOutFile(filename: CompName, content: String, secondaries: Seq[SecondaryOutFile]) extends OutFile
case class SecondaryOutFile(filename: String, content: String) extends OutFile

object Printer {
  case class FieldStats(maxFieldNameLen: Int, maxTypeNameLen: Int)

  def apply(prefix: String, comp: ParsedComponent): (PrimaryOutFile, Seq[SecondaryOutFile]) = {
    val fs: FieldStats =
      FieldStats(
        maxFieldNameLen = comp.fields.map(_.name.value.length).max,
        maxTypeNameLen  = comp.fields.map(_.typeName.length).max
      )

    val deprecated: String =
      if (comp.definition.deprecated) "@deprecated\n" else ""

    val p =
      PrimaryOutFile(
        comp.name,
        Seq(
          s"\n${deprecated}case class $prefix${comp.name}(",

          comp.fields.filterNot(_.name == PropName("children")).map(
            p => outProp(p, fs)
          ).mkString("", ",\n", ")") + bodyChildren(prefix, comp)

        ) ++ comp.definition.postlude.toSeq mkString "\n",
        comp.methodClassOpt.toSeq map outMethodClass
      )

    (p, comp.enumClases map outEnumClass)
  }

  def bodyChildren(prefix: String, comp: ParsedComponent): String =
    (comp.childrenOpt, comp.definition.multipleChildren) match {
      case (None, _) =>
        s"""{
          |
          |${indent(1)}def apply() = {
          |${indent(2)}val props = JSMacro[$prefix${comp.name}](this)
          |${indent(2)}val f = React.asInstanceOf[js.Dynamic].createFactory($prefix.${comp.name.value})
          |${indent(2)}f(props).asInstanceOf[ReactComponentU_]
          |${indent(1)}}
          |}
        """.stripMargin

      case (Some(childrenProp), true) =>
        s"""{
           |${outChildrenComment(childrenProp.commentOpt)}
           |${indent(1)}def apply(children: ${childrenProp.baseType.typeName}*) = {
           |${indent(2)}val props = JSMacro[$prefix${comp.name}](this)
           |${indent(2)}val f = React.asInstanceOf[js.Dynamic].createFactory($prefix.${comp.name.value})
           |${indent(2)}if (children.isEmpty)
           |${indent(3)}f(props).asInstanceOf[ReactComponentU_]
           |${indent(2)}else if (children.size == 1)
           |${indent(3)}f(props, children.head).asInstanceOf[ReactComponentU_]
           |${indent(2)}else
           |${indent(3)}f(props, children.toJsArray).asInstanceOf[ReactComponentU_]
           |${indent(1)}}
           |}""".stripMargin

      case (Some(childrenProp), false) =>
        s"""{
           |${outChildrenComment(childrenProp.commentOpt)}
           |${indent(1)}def apply(children: ${childrenProp.typeName} = js.undefined) = {
           |${indent(2)}val props = JSMacro[$prefix${comp.name}](this)
           |${indent(2)}val f = React.asInstanceOf[js.Dynamic].createFactory($prefix.${comp.name.value})
           |${indent(2)}f(props, children).asInstanceOf[ReactComponentU_]
           |${indent(1)}}
           |}""".stripMargin
    }

  def outChildrenComment(oc: Option[PropComment]) =
    oc.fold("")(d =>
      s"""${indent(1)}/**
         |${indent(1)} * @param children ${d.value}
         |${indent(1)} */""".stripMargin
    )

  def outComment(commentOpt: Option[PropComment], inheritedFrom: Option[CompName]): String = {
    val lines = commentOpt.map(_.value).toSeq ++ inheritedFrom.map(i => s"(Passed on to $i)")
    if (lines.isEmpty) ""
    else lines.flatMap(_.split("\n")).mkString(s"${indent(1)}/* ", s"\n${indent(1)}", "*/\n")
  }

  def outProp(p: ParsedProp, fs: FieldStats): String = {
    val comment = outComment(p.commentOpt, p.inheritedFrom)

    val intro: String = {
      val fixedName: String =
        if (p.name.value == "type") "`type`" else p.name.value
      val deps: String =
        (p.deprecatedMsg, p.commentOpt.exists(_.anns.contains(Ignore))) match {
          case (Some(msg), _  ) => s"""${indent(1)}@deprecated("$msg")\n"""
          case (None,     true) => s"""${indent(1)}@deprecated("Internal API")\n"""
          case _                => ""
        }
//      val deprecated = p.deprecatedMsg.fold("")(msg => s"""${indent(1)}@deprecated("$msg")\n""")
      s"$comment$deps${indent(1)}${padTo(fixedName + ": ")(fs.maxFieldNameLen + 2)}"
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
            val deprecated: String =
              if (m.toString().toLowerCase.contains("deprecated")) s"${indent(1)}@deprecated\n"
              else ""
            val comment = outComment(m.commentOpt, None)
            s"$comment$deprecated${indent(1)}def ${m.definition} = js.native"
            }.mkString("\n\n")
           }
         |}""".stripMargin
    )
}