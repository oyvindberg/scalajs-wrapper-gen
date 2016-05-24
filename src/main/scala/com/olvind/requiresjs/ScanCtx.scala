package com.olvind.requiresjs

import ammonite.ops.Path
import com.olvind.CompName

import scala.collection.mutable

class ScanCtx {
  private val parsedFiles   = mutable.Map.empty[Path, ParsedFile]
  private val requiredFiles = mutable.Map.empty[Path, Required]
  private val comps         = mutable.Map.empty[CompName, FoundComponent]

  def parsedFile(p: Path): ParsedFile =
    parsedFiles.getOrElseUpdate(p, JsParser(p))

  def required(p: Path, require: ⇒ Required): Required =
    requiredFiles.getOrElseUpdate(p, require)

}
