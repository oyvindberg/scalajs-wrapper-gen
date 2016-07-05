package com.olvind
package requiresjs

import ammonite.ops.{Path, exists}

case class ResolvedPath(file: Path, folder: Path)

object ResolvePath {
  def apply(p: Path): ResolvedPath =
    exists(p) match {
      case true ⇒
        p.isDir match {
          case true ⇒
            ResolvedPath(p / "index.js", p)
          case false ⇒
            panic("handle this when it happens")
        }

      case false ⇒
        val withExtension: Path =
          p.copy(segments = p.segments.dropRight(1) :+ p.segments.last + ".js")

        exists(withExtension) match {
          case true ⇒
            ResolvedPath(withExtension, p.copy(segments = p.segments.dropRight(1)))
          case false ⇒
            panic(s"Could not resolve path: $p")
        }
    }
}
