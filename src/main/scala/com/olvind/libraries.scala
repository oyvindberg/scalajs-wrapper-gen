package com.olvind

import ammonite.ops.Path

trait TypeMapper {
  def apply(compName: CompName, fieldName: PropName, typeString: String): Type
}

trait MemberMapper {
  def apply(compName: CompName)(memberMethod: MemberMethod): ParsedMethod
}

trait Library {
  def name: String
  def prefixOpt: Option[String]
  def location: Path
  def inheritance: Map[CompName, CompName]
  def typeMapper: TypeMapper
  def memberMapper: MemberMapper

  @deprecated
  final def prefix: String =
    prefixOpt getOrElse ""
}