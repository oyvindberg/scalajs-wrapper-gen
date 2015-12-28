package com.olvind

import ammonite.ops.Path

trait Wrapper[A]{
  def value: A
  override def toString = value.toString
}

case class CompName(value: String) extends Wrapper[String]{
  def map(f: String => String) =
    CompName(f(value))
}

case class PropName(value: String) extends AnyVal{
  def clean: PropName =
    PropName(value.replaceAll("Deprecated:", "").replaceAll("or children", "").trim)
}
case class PropComment private (value: String) extends Wrapper[String]

object PropComment{
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

case class PropUnparsed(
  fromComp:   CompName,
  unparsed:   PropTypeUnparsed,
  commentOpt: Option[PropComment]
)

case class PropTypeUnparsed(value: String) extends Wrapper[String]

case class VarName(value: String) extends Wrapper[String]

case class Import(
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
