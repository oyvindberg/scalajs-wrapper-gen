package com.olvind
package requiresjs

import ammonite.ops.Path
import jdk.nashorn.internal.ir.FunctionNode

case class ParsedFile(path: Path, content: String, result: FunctionNode)

sealed trait Required
case class Multiple(name: VarName, path: Path, rs: Seq[Required]) extends Required
case class Single(compName: CompName, c: FoundComponent) extends Required
case object NotFound extends Required

case class FoundComponent(
  name:      CompName,
  file:      Path,
  imports:   Seq[Import],
  jsContent: String,
  propsOpt:  Option[Map[PropName, PropUnparsed]],
  methods:   Option[Set[MemberMethod]]
)
