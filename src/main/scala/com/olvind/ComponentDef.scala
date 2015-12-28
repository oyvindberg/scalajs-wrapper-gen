package com.olvind

trait ComponentDef {
  val name: CompName
  val json: String
  val propsSections: Seq[String] = Seq("Props")
  val overrideMethods: Option[String] = None
  val overrideEvents: Option[String] = None
  val shared: Option[CompName] = None
  val postlude: Option[String] = None
  val multipleChildren: Boolean = true
  val deprecated: Boolean = false
}

trait DocProvider {
  def apply(prefix: String, comp: ComponentDef): (Map[PropName, PropComment], Option[ParsedMethodClass])
}

trait TypeMapper {
  def apply(compName: CompName, fieldName: PropName, typeString: String): PropType
}

trait Library {
  def components: Seq[ComponentDef]
  def docProvider: DocProvider
  def typeMapper: TypeMapper
  def prefix: String
}