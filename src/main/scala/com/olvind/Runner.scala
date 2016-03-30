package com.olvind

import java.io.File

class Runner[D <: ComponentDef](library: Library[D]) {
  val basedir = new File("/Users/oyvindberg/pr/scalajs-react-components/core/src/main/scala/chandu0101/scalajs/react/components")
  val WRITE   = true

  def printToFile(f: java.io.File)(op: java.io.PrintWriter => Unit): Unit =
    if (WRITE){
      val p = new java.io.PrintWriter(f)
      try { op(p) } finally { p.close() }
    }

  val prelude =
    s"""package chandu0101.scalajs.react.components
      |${library.nameOpt.fold("")(name => s"package $name\n")}
      |import chandu0101.macros.tojs.JSMacro
      |import japgolly.scalajs.react._
      |import scala.scalajs.js
      |import scala.scalajs.js.`|`
      |
      |/**
      | * This file is generated - submit issues instead of PR against it
      | */
    """.stripMargin

  val foundComponents: Map[CompName, requiresjs.FoundComponent] = {
    val res1: requiresjs.Required =
      requiresjs.Require(
        library.importName,
        library.location
      )
    requiresjs.flattenScan(res1)
  }

  val (mainFiles: Seq[PrimaryOutFile], secondaryFiles: Seq[SecondaryOutFile]) =
    library.components.foldLeft((Seq.empty[PrimaryOutFile], Seq.empty[SecondaryOutFile])){
      case ((ps, ss), c) =>
        val pc     = ParseComponent(foundComponents, library, c)
        val (p, s) = Printer(library.prefixOpt.getOrElse(""), pc)
        (ps :+ p, ss ++ s)
  }

  val destFolder = library.destinationFolder(basedir)
  destFolder.mkdir()

  printToFile(new File(destFolder, "gen-types.scala")){
    w =>
      w.println(prelude)
      secondaryFiles.sortBy(_.content).distinct.foreach{
        case file =>
          w.println(file.content)
          w.println("")
      }
  }

  mainFiles foreach {
    case PrimaryOutFile(compName, content, secondaries) =>
      printToFile(library.destinationFile(basedir, compName)){
        w =>
          w.println(prelude + content)
          secondaries.foreach{
            case SecondaryOutFile(_, c) =>
              w.println("")
              w.println(c)
          }
      }
  }
}
