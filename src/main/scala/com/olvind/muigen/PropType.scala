package com.olvind
package muigen

sealed trait PropType {
  def typeName: String
}
case class PropTypeClass(override val typeName: String) extends PropType

case class PropTypeEnum(component: CompName, name: PropName, ss: Seq[String]) extends PropType{
  val fixedNames: Seq[(Identifier, String)] =
    ss.map { m => (Identifier.safe(m), m)}

  override val typeName: String =
    fixedNames.map(_._1.value.capitalize).mkString("") //name.value.capitalize

  def enumClass = OutEnumClass(typeName, fixedNames)
}