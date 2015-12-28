package com

package object olvind {
  def padTo(s: String)(n: Int) = s + (" " * (n - s.length))
  def indent(n: Int) = "\t" * n
}
