package com.olvind
package muigen

import com.olvind.StringUtils.padTo

sealed trait OutField{
  val name:          PropName
  val baseType:      OutParam
  val typeName:      String
  val commentOpt:    Option[PropComment]
  val deprecatedMsg: Option[String]

  def toString(fs: FieldStats): String

  final def typeNameLength  = typeName.length
  final def fieldNameLength = name.value.length

  def comment: String =
    commentOpt.fold("")(c => s"  /* ${c.value}*/\n")

  final def intro(fs: FieldStats) = {
    val fixedName: String =
      if (name.value == "type") "`type`" else name.value
    val deprecated = deprecatedMsg.fold("")(msg => s"""  @deprecated("$msg")\n""")
    s"$comment$deprecated  ${padTo(fixedName + ": ")(fs.maxFieldNameLen + 2)}"
  }
}

final case class ReqField(name: PropName, baseType: OutParam, commentOpt: Option[PropComment], deprecatedMsg: Option[String]) extends OutField {
  override val typeName = baseType.typeName
  override def toString(fs: FieldStats): String =
    intro(fs) + typeName
}

final case class OptField(name: PropName, baseType: OutParam, commentOpt: Option[PropComment], deprecatedMsg: Option[String]) extends OutField {
  override val typeName = s"js.UndefOr[${baseType.typeName}]"
  override def toString(fs: FieldStats): String =
    intro(fs) + padTo(typeName)(fs.maxTypeNameLen + 2) + " = js.undefined"
}

sealed trait OutParam {
  def typeName: String
}
case class OutParamClass(override val typeName: String) extends OutParam

case class OutParamEnum(component: CompName, name: PropName, ss: Seq[String]) extends OutParam{
  override val typeName = component + name.value.capitalize
  def enumClass = OutEnumClass(typeName, ss)
}

object OutField {
  //  "Deprecated(string, 'Instead, use a custom `actions` property.')"
  val Pattern = "Deprecated\\(([^,]+), '(.+)'\\)".r

  def apply(compName:   CompName,
            propName:   PropName,
            propString: PropString,
            commentOpt: Option[PropComment]): OutField = {

    val _clean: String =
      propString.value
        .replace("React.", "")
        .replace("PropTypes.", "")
        .replace(".isRequired", "")
        .replace("_react2['default'].", "")
        .replace("_utilsPropTypes2['default'].", "Mui.")
        .replace("(0, _utilsDeprecatedPropType2['default'])", "Deprecated")

    val (typeStr: String, deprecatedOpt: Option[String]) = _clean match {
      case Pattern(tpe, depMsg) => (tpe, Some(depMsg))
      case tpe                  => (tpe, None)
    }

    val mappedType: OutParam =
      TypeMapper(compName, propName)(typeStr)

    val isRequired: Boolean =
      propString.value.contains(".isRequired")

    if (isRequired) ReqField(propName, mappedType, commentOpt, deprecatedOpt)
    else            OptField(propName, mappedType, commentOpt, deprecatedOpt)
  }
}
