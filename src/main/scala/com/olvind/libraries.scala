package com.olvind

trait ComponentDef {
  val name: CompName
  val json: String
  val shared: Option[CompName] = None
  val postlude: Option[String] = None
  val multipleChildren: Boolean = true
  val deprecated: Boolean = false
}

trait DocProvider[D <: ComponentDef] {
  def apply(prefix: String, comp: D): (Map[PropName, PropComment], Option[ParsedMethodClass])
}

trait TypeMapper {
  def apply(compName: CompName, fieldName: PropName, typeString: String): PropType
}

trait Library[D <: ComponentDef] {
  def components: Seq[D]
  def docProvider: DocProvider[D]
  def typeMapper: TypeMapper
  def prefix: String
}