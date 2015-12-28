package com.olvind

final case class ParsedComponent(
  definition:     ComponentDef,
  fields:         Seq[ParsedProp],
  methodClassOpt: Option[ParsedMethodClass]) {

  def name = definition.name

  val childrenOpt = fields.find(_.name.value == "children")

  val enumClases: Seq[ParsedEnumClass] =
    fields.map(_.baseType).collect{
      case o: PropType.Enum => o.enumClass
    }
}

final case class ParsedMethodClass(
  className: String,
  ms: Seq[ParsedMethod]
)

final case class ParsedEnumClass(
  name: String,
  identifiers: Seq[(Identifier, String)]
)

final case class ParsedProp(
  name:          PropName,
  isRequired:    Boolean,
  baseType:      PropType,
  commentOpt:    Option[PropComment],
  deprecatedMsg: Option[String],
  inheritedFrom: Option[CompName]) {

  val typeName =
    if (isRequired) baseType.typeName
    else            s"js.UndefOr[${baseType.typeName}]"
}

sealed trait PropType {
  def typeName: String
}

object PropType {
  case class Type(override val typeName: String) extends PropType

  case class Enum(component: CompName, name: PropName, ss: Seq[String]) extends PropType{
    val fixedNames: Seq[(Identifier, String)] =
      ss.map { m => (Identifier.safe(m), m)}

    override val typeName: String =
      fixedNames.map(_._1.value.capitalize).mkString("")

    def enumClass: ParsedEnumClass =
      ParsedEnumClass(typeName, fixedNames)
  }
}

final case class ParsedMethod(definition: String, commentOpt: Option[PropComment]) {
  require(!definition.contains("="))
  require(!definition.startsWith("def "))
}

final case class PropUnparsed(
  fromComp:   CompName,
  unparsed:   PropTypeUnparsed,
  commentOpt: Option[PropComment]
)

final case class PropTypeUnparsed(value: String) extends Wrapper[String]