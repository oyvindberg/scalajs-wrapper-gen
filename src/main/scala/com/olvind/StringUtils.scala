package com.olvind

object StringUtils{
  def padTo(s: String)(n: Int) = s + (" " * (n - s.length))
}
