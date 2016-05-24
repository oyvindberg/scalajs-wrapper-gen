package com.olvind.requiresjs

import ammonite.ops.{Path, exists}

case class ResolvedPath(file: Path, folder: Path)

object ResolvePath {
  def notFound(p: Path): Nothing =
    throw new RuntimeException(s"Could not resolve path: $p")

  def apply(p: Path): ResolvedPath =
    exists(p) match {
      case true ⇒
        p.isDir match {
          case true ⇒
            ResolvedPath(p / "index.js", p)
          case false ⇒
            ResolvedPath(p, p.copy(p.segments.dropRight(1)))
        }
      case false ⇒
        val withExtension: Path =
          p.copy(p.segments.dropRight(1) :+ p.segments.last + ".js")

        exists(withExtension) match {
          case true ⇒
            ResolvedPath(withExtension, p.copy(p.segments.dropRight(1)))
          case false ⇒
            notFound(p)
        }
    }
}
