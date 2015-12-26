package com.olvind
package muigen

import com.olvind.StringUtils.padTo

sealed trait OutField {
  val name:          PropName
  val baseType:      PropType
  val typeName:      String
  val commentOpt:    Option[PropComment]
  val deprecatedMsg: Option[String]
  val inheritedOpt:  Option[CompName]
  def toString(fs: FieldStats): String

  final def typeNameLength  = typeName.length
  final def fieldNameLength = name.value.length

  def comment: String = {
    val comments = commentOpt.toSeq ++ inheritedOpt.map(i => s"(Passed on to $i)")
    if (comments.isEmpty) "" else comments.mkString("  /* ", "\n  ", "*/\n")
  }

  final def intro(fs: FieldStats) = {
    val fixedName: String =
      if (name.value == "type") "`type`" else name.value
    val deprecated = deprecatedMsg.fold("")(msg => s"""  @deprecated("$msg")\n""")
    s"$comment$deprecated  ${padTo(fixedName + ": ")(fs.maxFieldNameLen + 2)}"
  }
}

final case class ReqField(name: PropName, baseType: PropType, commentOpt: Option[PropComment], deprecatedMsg: Option[String], inheritedOpt: Option[CompName]) extends OutField {
  override val typeName = baseType.typeName
  override def toString(fs: FieldStats): String =
    intro(fs) + typeName
}

final case class OptField(name: PropName, baseType: PropType, commentOpt: Option[PropComment], deprecatedMsg: Option[String], inheritedOpt: Option[CompName]) extends OutField {
  override val typeName = s"js.UndefOr[${baseType.typeName}]"
  override def toString(fs: FieldStats): String =
    intro(fs) + padTo(typeName)(fs.maxTypeNameLen + 2) + " = js.undefined"
}

object OutField {
  //  "Deprecated(string, 'Instead, use a custom `actions` property.')"
  val Pattern = "Deprecated\\(([^,]+), '(.+)'\\)".r

  def apply(compName:     CompName,
            origCompName: CompName,
            propName:     PropName,
            propString:   PropString,
            commentOpt:   Option[PropComment]): OutField = {

    val _clean: String =
      propString.value
        .replace("React.", "")
        .replace("PropTypes.", "")
        .replace(".isRequired", "")
        /* old style */
        .replace("_react2['default'].", "")
        .replace("_utilsPropTypes2['default'].", "Mui.")
        .replace("(0, _utilsDeprecatedPropType2['default'])", "Deprecated")
        /* new style */
        .replace("_react2.default.", "")
        .replace("_propTypes2.default.", "Mui.")
        .replace("(0, _deprecatedPropType2.default)", "Deprecated")

    val (typeStr: String, deprecatedOpt: Option[String]) = _clean match {
      case Pattern(tpe, depMsg) => (tpe, Some(depMsg))
      case tpe                  => (tpe, None)
    }

    val mappedType: PropType =
      TypeMapper(origCompName, propName, typeStr)

    val isRequired: Boolean =
      propString.value.contains(".isRequired")

    val inheritedOpt: Option[CompName] =
      if (compName == origCompName) None else Some(origCompName)

    if (isRequired && inheritedOpt.isEmpty)
      ReqField(propName, mappedType, commentOpt, deprecatedOpt, inheritedOpt)
    else
      OptField(propName, mappedType, commentOpt, deprecatedOpt, inheritedOpt)
  }
}
