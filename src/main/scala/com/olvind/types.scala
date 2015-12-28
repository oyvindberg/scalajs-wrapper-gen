package com.olvind

import ammonite.ops.Path

trait Wrapper[A]{
  def value: A
  override def toString = value.toString
}

final case class CompName(value: String) extends Wrapper[String]{
  def map(f: String => String) =
    CompName(f(value))
}

final case class PropName(value: String) extends AnyVal {
  def clean: PropName =
    PropName(value.replaceAll("Deprecated:", "").replaceAll("or children", "").trim)
}

final case class PropComment private (value: String) extends Wrapper[String]

object PropComment {
  def clean(s: String): PropComment =
    PropComment(
      s.replaceAll("/\\*", "")
        .replaceAll("//", "")
        .replaceAll("\\*/", "")
        .split("\n")
        .map(_.dropWhile(c => c.isWhitespace || c == '*').trim)
        .filterNot(_.isEmpty)
        .mkString("\n")
    )
}

final case class VarName(value: String) extends Wrapper[String]

final case class Import(
  varName: VarName,
  target:  Either[Path, String]
)

case class Identifier private (value: String) extends Wrapper[String]

object Identifier{
  def safe(m: String): Identifier = {
    val memberName = if (m.head.isDigit) "_" + m else m
    Identifier(memberName.replace("-", "_"))
  }
}
