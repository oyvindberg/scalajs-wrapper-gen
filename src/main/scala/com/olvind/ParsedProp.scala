package com.olvind

import com.olvind.muigen.MuiTypeMapper

sealed trait PropType {
  def typeName: String
}

case class PropTypeClass(override val typeName: String) extends PropType

case class PropTypeEnum(component: CompName, name: PropName, ss: Seq[String]) extends PropType{
  val fixedNames: Seq[(Identifier, String)] =
    ss.map { m => (Identifier.safe(m), m)}

  override val typeName: String =
    fixedNames.map(_._1.value.capitalize).mkString("") //name.value.capitalize

  def enumClass = ParsedEnumClass(typeName, fixedNames)
}

final case class ParsedProp(
  name:          PropName,
  isRequired:    Boolean,
  baseType:      PropType,
  commentOpt:    Option[PropComment],
  deprecatedMsg: Option[String],
  inheritedFrom: Option[CompName]) {

  def typeNameLength  = typeName.length
  def fieldNameLength = name.value.length

  val typeName =
    if (isRequired) baseType.typeName
    else            s"js.UndefOr[${baseType.typeName}]"
}

object ParsedProp {
  //  "Deprecated(string, 'Instead, use a custom `actions` property.')"
  val Pattern = "Deprecated\\(([^,]+), '(.+)'\\)".r

  def apply(compName:     CompName,
            origCompName: CompName,
            propName:     PropName,
            propString:   PropTypeUnparsed,
            commentOpt:   Option[PropComment]): ParsedProp = {

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
      MuiTypeMapper(origCompName, propName, typeStr)

    val isRequired: Boolean =
      propString.value.contains(".isRequired")

    val inheritedFrom: Option[CompName] =
      if (compName == origCompName) None else Some(origCompName)

    ParsedProp(propName, isRequired && inheritedFrom.isEmpty, mappedType, commentOpt, deprecatedOpt, inheritedFrom)
  }
}
