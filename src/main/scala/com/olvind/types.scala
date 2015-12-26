package com.olvind

import ammonite.ops.Path
import com.olvind.muigen.TypeMapper

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

case class OriginalProp(
  origComp: CompName,
  s: PropString,
  oc: Option[PropComment]
)

case class PropString(value: String) extends Wrapper[String]

case class VarName(value: String) extends Wrapper[String]

case class Import(
  varName: VarName,
  target:  Either[Path, String]
)
