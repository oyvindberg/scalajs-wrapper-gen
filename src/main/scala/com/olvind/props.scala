package com.olvind

final case class ParsedComponent(
  name:           CompName,
  fields:         Seq[ParsedProp],
  methodClassOpt: Option[ParsedMethodClass]) {

  val childrenOpt: Option[ParsedProp] =
    fields.find(_.name.value == "children")

  val genericParams: Seq[ParsedGeneric] =
    fields.foldLeft(Map.empty[String, Boolean]){
      case (m, ParsedProp(_, _, Normal(_, Some(Generic(name, jsObject))), _, _, _)) ⇒
        m.updated(name, m.getOrElse(name, jsObject) || jsObject)
      case (m, other) ⇒
        m
    }.map(ParsedGeneric.tupled)
     .toSeq

  val enumClases: Seq[ParsedEnumClass] =
    fields.map(_.baseType).collect{
      case o: Enum => o.enumClass
    }

  def nameDef(prefix: String, withBounds: Boolean = false): String = {
    val tpeParam =
      if (genericParams.isEmpty) ""
      else genericParams.map {
        p ⇒
          val bounds: String =
            (p.jsObject, withBounds) match {
              case (_, false)    ⇒ ""
              case (true, true)  ⇒ "" //todo: revisit
              case (false, true) ⇒ ""
            }
          s"${p.name}$bounds"
      }.mkString("[", ", ", "]"
      )

    s"$prefix$name$tpeParam"
  }
}

final case class ParsedMethodClass(
  className: String,
  methods: Seq[ParsedMethod]
)

final case class ParsedEnumClass(
  name: String,
  identifiers: Seq[(Identifier, String)]
)

sealed trait Annotation
case class Deprecated(reason: String) extends Annotation
case class Param(value: String) extends Annotation
case object Ignore extends Annotation

final case class ParsedProp(
  name:          PropName,
  isRequired:    Boolean,
  baseType:      Type,
  commentOpt:    Option[PropComment],
  deprecatedMsg: Option[String],
  inheritedFrom: Option[CompName]) {

  val typeName: String =
    if (isRequired) baseType.name
    else            s"js.UndefOr[${baseType.name}]"
}

final case class ParsedGeneric(name: String, jsObject: Boolean)

sealed trait Type {
  def name: String
}

case class Generic(name: String, jsObject: Boolean = false)

case class Normal(name: String, genericOpt: Option[Generic] = None) extends Type {
  def generic(name: String) =
    copy(genericOpt = Some(Generic(name)))

  def genericJs(name: String) =
    copy(genericOpt = Some(Generic(name, jsObject = true)))
}

case class Enum(component: CompName, ss: Seq[String]) extends Type {
  val fixedNames: Seq[(Identifier, String)] =
    ss.map { m => (Identifier.safe(m), m)}

  override val name: String =
    fixedNames.map(_._1.value.capitalize).mkString("")

  def enumClass: ParsedEnumClass =
    ParsedEnumClass(name, fixedNames)
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