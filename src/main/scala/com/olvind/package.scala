package com

package object olvind {
  def padTo(s: String)(n: Int): String =
    s + (" " * (n - s.length))

  def indent(n: Int): String =
    "  " * n
}
