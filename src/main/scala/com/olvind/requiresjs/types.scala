package com.olvind
package requiresjs

import ammonite.ops.Path
import jdk.nashorn.internal.ir.FunctionNode

case class ParsedFile(path: Path, content: String, result: FunctionNode)

object Required {
  def apply(path: Path, rs: Seq[Required]): Required =
    rs.size match {
      case 0 ⇒ NotFound(path)
      case 1 ⇒ rs.head
      case n ⇒ Multiple(path, rs)
    }
}
sealed trait Required
case class Multiple(path: Path, rs: Seq[Required]) extends Required
case class Single(compName: CompName, c: FoundComponent) extends Required
case class NotFound(path: Path) extends Required

case class FoundComponent(
  name:      CompName,
  file:      Path,
  jsContent: String,
  propsOpt:  Map[PropName, PropUnparsed],
  methods:   Option[Set[MemberMethod]]
)
