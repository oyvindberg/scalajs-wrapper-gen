package com.olvind

import ammonite.ops.Path

package object gen {
  def add(p: Path, frag: String): Path =
    frag
      .split("/")
      .filterNot(_.isEmpty)
      .filterNot(_ == ".")
      .foldLeft(p)(_ / _)
}
