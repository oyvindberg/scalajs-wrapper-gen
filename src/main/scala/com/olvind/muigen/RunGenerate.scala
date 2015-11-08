package com.olvind.muigen

import java.io.File

object RunGenerate extends App{
  val dest = new File(args.head)
  dest.mkdir()

  def printToFile(f: java.io.File)(op: java.io.PrintWriter => Unit) {
    val p = new java.io.PrintWriter(f)
    try { op(p) } finally { p.close() }
  }
  val prelude = """
    |package chandu0101.scalajs.react.components
    |package materialui
    |
    |import japgolly.scalajs.react._
    |import scala.scalajs.js
  """.stripMargin

  val outFiles = Component.components flatMap ParseComponents.apply
  outFiles foreach {
    case OutFile(file, content) => printToFile(new File(dest, file + ".scala"))(_.print(prelude + content))
  }
}