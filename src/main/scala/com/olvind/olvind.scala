package com

import java.io.File

import ammonite.ops.Path

package object olvind {
  def padTo(s: String)(n: Int): String =
    s + (" " * (n - s.length))

  def indent(n: Int): String =
    "  " * n

  def add(p: Path, frag: String): Path =
    frag
      .split("/")
      .filterNot(_.isEmpty)
      .filterNot(_ == ".")
      .foldLeft(p)(_ / _)

  def exists(path: Path): Boolean =
    new File(path.toString).exists
}
