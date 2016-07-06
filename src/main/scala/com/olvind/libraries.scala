package com.olvind

import ammonite.ops.Path

final case class ComponentDef(
  name:             CompName,
  shared:           Option[ComponentDef] = None,
  multipleChildren: Boolean              = true,
  domeTypeOpt:      Option[DomType]      = Some(DomElement)
)

trait TypeMapper {
  def apply(compName: CompName, fieldName: PropName, typeString: String): Type
}

trait MemberMapper {
  def apply(compName: CompName)(memberMethod: MemberMethod): ParsedMethod
}

trait Library[D <: ComponentDef] {
  def name: String
  def prefixOpt: Option[String]
  def importName: VarName
  def location: Path
  def outputPath: Path
  def components: Seq[D]
  def typeMapper: TypeMapper
  def memberMapper: MemberMapper

  @deprecated
  final def prefix: String =
    prefixOpt getOrElse ""

  final def destinationPath(comp: CompName): Path = {
    val baseFile = comp.value + ".scala"
    val filename = prefixOpt.fold(baseFile)(_ + baseFile)
    outputPath / filename
  }
}