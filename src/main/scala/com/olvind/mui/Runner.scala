package com.olvind
package mui

import java.io.File

import ammonite.ops._

object Runner extends App {
  val WRITE = true
  val dest = new File(args.head)
  dest.mkdir()

  def printToFile(f: java.io.File)(op: java.io.PrintWriter => Unit) {
    if (WRITE){
      val p = new java.io.PrintWriter(f)
      try { op(p) } finally { p.close() }
    }
  }

  val prelude = """package chandu0101.scalajs.react.components
    |package materialui
    |
    |import chandu0101.macros.tojs.JSMacro
    |import japgolly.scalajs.react._
    |import scala.scalajs.js
    |import scala.scalajs.js.`|`
  """.stripMargin

  val foundComponents: Map[CompName, requiresjs.FoundComponent] = {
    val ctx = new requiresjs.ScanCtx
    val res1: requiresjs.Required =
      requiresjs.Require(
        VarName("mui"),
        home / "pr" / "material-ui" / "lib",
        ctx
      )
    requiresjs.flattenScan(res1)
  }


  val (mainFiles: Seq[PrimaryOutFile], secondaryFiles: Seq[SecondaryOutFile]) =
    MuiLibrary.components.foldLeft((Seq.empty[PrimaryOutFile], Seq.empty[SecondaryOutFile])){
      case ((ps, ss), c) =>
        val pc     = ParseComponent(foundComponents, MuiLibrary, c)
        val (p, s) = Printer(MuiLibrary.prefix, pc)
        (ps :+ p, ss ++ s)
  }

  printToFile(new File(dest, "gen-types.scala")){
    w =>
      w.println(prelude)
      secondaryFiles.sortBy(_.content).distinct.foreach{
        case file =>
          w.println(file.content)
          w.println("")
      }
  }

  mainFiles foreach {
    case PrimaryOutFile(file, content, secondaries) =>
      printToFile(new File(dest, MuiLibrary.prefix + file.value + ".scala")){
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