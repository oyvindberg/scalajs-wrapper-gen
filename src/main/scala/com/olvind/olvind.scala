package com

import java.io.File

import ammonite.ops.Path

package object olvind {
  def padTo(s: String)(n: Int): String =
    s + (" " * (n - s.length))

  def indent(n: Int): String =
    "  " * n

  def add(_p: Path, frags: String): Path =
    frags.split("/").filterNot(_.isEmpty).foldLeft(_p){
      case (p, ".")  ⇒ p
      case (p, "..") ⇒ p.copy(p.segments.dropRight(1))
      case (p, frag) ⇒ p / frag
    }

  def exists(path: Path): Boolean =
    new File(path.toString).exists
}
