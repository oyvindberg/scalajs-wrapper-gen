package com.olvind
package muigen

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

  val muiComponents: Map[CompName, gen.Component] = {
    val ctx = new gen.Ctx
    val res1: gen.Result =
      gen.PropTypeParser(
        VarName("mui"),
        home / "pr" / "material-ui" / "lib",
        ctx
      )
    gen.flattenRes(res1)
  }


  val (mainFiles: Seq[PrimaryOutFile], secondaryFiles: Seq[SecondaryOutFile]) =
    MuiComponents.components.foldLeft((Seq.empty[PrimaryOutFile], Seq.empty[SecondaryOutFile])){
      case ((ps, ss), c) =>
        val pc     = ParsedComponent(muiComponents, MuiDocs)(c)
        val (p, s) = ComponentPrinter(pc)
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
      printToFile(new File(dest, "Mui" + file.value + ".scala")){
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