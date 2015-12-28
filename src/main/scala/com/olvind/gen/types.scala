package com.olvind
package gen

import ammonite.ops.Path
import jdk.nashorn.internal.ir.FunctionNode

import scala.collection.mutable

case class ParsedFile(path: Path, content: String, result: FunctionNode)

class ScanCtx {
  private val parsedFiles = mutable.Map.empty[Path, ParsedFile]
  private val comps       = mutable.Map.empty[CompName, FoundComponent]

  def parsedFile(p: Path): ParsedFile =
    parsedFiles.getOrElseUpdate(p, JsParser(p))
}

sealed trait ScanResult
case class Module(name: VarName, path: Path, rs: Iterable[ScanResult]) extends ScanResult
case class Single(compName: CompName, c: FoundComponent) extends ScanResult

case class FoundComponent(
  name:      CompName,
  file:      Path,
  imports:   Seq[Import],
  jsContent: String,
  propsOpt:  Option[Map[PropName, PropUnparsed]]
)
