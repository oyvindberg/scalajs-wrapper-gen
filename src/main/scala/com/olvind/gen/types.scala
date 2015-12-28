package com.olvind
package gen

import ammonite.ops.Path
import jdk.nashorn.internal.ir.FunctionNode

import scala.collection.mutable

case class ParsedFile(path: Path, content: String, result: FunctionNode)

class Ctx {
  private val parsedFiles = mutable.Map.empty[Path, ParsedFile]
  private val comps       = mutable.Map.empty[CompName, Component]

  def parsedFile(p: Path): ParsedFile =
    parsedFiles.getOrElseUpdate(p, JsParser(p))
}

case class Component(
  name:      CompName,
  file:      Path,
  imports:   Seq[Import],
  jsContent: String,
  propsOpt:  Option[Map[PropName, PropUnparsed]]
)
