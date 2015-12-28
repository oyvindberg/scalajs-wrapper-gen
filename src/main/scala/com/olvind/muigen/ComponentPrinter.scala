package com.olvind
package muigen

import scala.collection.mutable

case class OutComponentClass(name: CompName) {
  def childrenOpt = fields.find(_.name.value == "children")

  val fields: mutable.ArrayBuffer[ParsedProp] =
    mutable.ArrayBuffer.empty[ParsedProp]

  def addField(f: ParsedProp) =
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

case class OutMethodClass(name: CompName, ms: Seq[OutMethod]){
  val className = prefix + name.value + "M"
}

case class OutMethod(definition: String, commentOpt: Option[PropComment]) {
  require(!definition.contains("="))
  require(!definition.startsWith("def "))
}

case class OutEnumClass(name: String, identifiers: Seq[(Identifier, String)])

sealed trait OutFile
case class PrimaryOutFile(filename: CompName, content: String, secondaries: Seq[SecondaryOutFile]) extends OutFile
case class SecondaryOutFile(filename: String, content: String) extends OutFile

object ComponentPrinter {
  def apply(allComps: Map[CompName, gen.Component])
           (muiComp:  ComponentDef): Seq[OutFile] = {

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

    out.addField(ParsedProp(PropName("key"), isRequired = false, PropTypeClass("String"), None, None, None))
    out.addField(ParsedProp(PropName("ref"), isRequired = false, PropTypeClass(methodClassOpt.fold("String")(c => c.className + " => Unit")), None, None, None))
//    out.addField(ReqField(PropName("untyped"), PropTypeClass("Map[String, js.Any]"), None, None, None))

    (inheritedProps ++ propTypes).toSeq.sortBy(p => (p._2.origComp != muiComp.name, p._1.clean.value)).foreach{
      case (name, OriginalProp(origComp, tpe, commentOpt)) =>
        val field = ParsedProp(muiComp.name, origComp, name, tpe, commentOpt orElse (commentMap get name))
        out.addField(field)
    }

    outComponent(muiComp, out, methodClassOpt)
  }

  def outComponent(comp: ComponentDef, c: OutComponentClass, methodClassOpt: Option[OutMethodClass]): Seq[OutFile] = {
    val fs = c.fieldStats
    val d1 = if (comp.deprecated) "@deprecated\n" else ""
    val p1 = s"\n${d1}case class $prefix${comp.name}("
    val p2 = c.fields.filterNot(_.name == PropName("children")).map(
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

      if (comp.multipleChildren)
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

    val content      = (Seq(p1, p2, c.childrenOpt.fold(body)((c: ParsedProp) => bodyChildren(c))) ++ comp.postlude.toSeq).mkString("\n")
    val outFile      = PrimaryOutFile(comp.name, content, methodClassOpt.toSeq map outMethodClass)
    val outEnumFiles = c.enumClases map outEnumClass

    outFile +: outEnumFiles
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

  def outEnumClass(c: OutEnumClass): SecondaryOutFile =
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

  def outMethodClass(c: OutMethodClass): SecondaryOutFile =
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