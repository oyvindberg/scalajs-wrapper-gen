package com.olvind
package react_select

import ammonite.ops._

object SelectLibrary extends Library[ComponentDef] {
  object SelectComponent extends ComponentDef{
    override val name = CompName("ReactSelect")
  }

  object SelectTypeMapper extends TypeMapper {
    override def apply(compName: CompName, fieldName: PropName, typeString: String): PropType =
      (compName.value, fieldName.value, typeString) match {
        case (s1, s2, s3) => PropType.Type("js.Any")
      }
  }
  override val nameOpt     = None
  override val importName  = VarName("ReactSelect")
  override val prefixOpt   = None
  override val location    = home / "pr" / "scalajs-react-components" / "demo" / "node_modules" / "react-select" / "dist" / "react-select.js"
  override val components  = Seq(
    SelectComponent
  )
  override val typeMapper  = SelectTypeMapper

  override def memberMapper: MemberMapper = ???
}

object SelectRunner extends Runner(SelectLibrary) with App
