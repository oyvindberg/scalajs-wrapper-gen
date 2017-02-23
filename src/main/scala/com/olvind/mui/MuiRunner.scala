package com.olvind
package mui

import ammonite.ops.FileType.Dir
import ammonite.ops.{Path, cwd}

import scala.util.{Failure, Success, Try}

object MuiRunner extends App {
  object ExistingFolder {
    def unapply(s: String): Option[Path] =
      Try{
        val p = Path(s, cwd)
        (p, p.fileType)
      } match {
        case Success((p, Dir)) =>
          Some(p)
        case Success((p, other)) =>
          println(s"Illegal argument: s. must be folder")
          None
        case Failure(th) =>
          println(s"Illegal argument $s: ${th.getMessage}")
          None
      }
  }

  args.toList match {
    case ExistingFolder(buildFolder) :: ExistingFolder(outputFolder) :: Nil =>
      Runner(MuiLibrary(buildFolder), outputFolder)
    case _ =>
      println("Syntax: MuiRunner <directory with transpiled javascript", "output folder")
  }
}