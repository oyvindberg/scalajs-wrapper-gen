package com.olvind

import java.io.File

import ammonite.ops.Path

trait ComponentDef {
  val name: CompName
  val shared: Option[ComponentDef] = None
  val postlude: Option[String] = None
  val multipleChildren: Boolean = true
  val deprecated: Boolean = false
}

trait TypeMapper {
  def apply(compName: CompName, fieldName: PropName, typeString: String): PropType
}

trait MemberMapper {
  def apply(compName: CompName)(memberMethod: MemberMethod): ParsedMethod
}

trait Library[D <: ComponentDef] {
  def nameOpt: Option[String]
  def prefixOpt: Option[String]
  def importName: VarName
  def location: Path
  def components: Seq[D]
  def typeMapper: TypeMapper
  def memberMapper: MemberMapper
  @deprecated
  final def prefix: String =
    prefixOpt getOrElse ""

  final def destinationFolder(baseDir: File): File =
    nameOpt.fold(baseDir)(name => new File(baseDir, name))

  final def destinationFile(baseDir: File, comp: CompName): File = {
    val baseFile = comp.value + ".scala"
    val filename = prefixOpt.fold(baseFile)(_ + baseFile)
    new File(destinationFolder(baseDir), filename)
  }
}