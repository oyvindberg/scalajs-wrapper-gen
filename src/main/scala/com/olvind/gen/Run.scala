package com.olvind
package gen

import ammonite.ops._

object Run extends App {
  def indented(n: Int)(s: String) =
    println((" " * n) + s)

  def print(indent: Int)(r: ScanResult): Unit = {
    r match {
      case Module(name, path, cs) =>
        println("-" * 20)
        indented(indent)(name.value)
        cs foreach print(indent + 2)
        println("-" * 20)
      case Single(compName, c) =>
        indented(indent)(compName.value)
        c.propsOpt.fold(())(_ foreach {case (name, tpe) => indented(indent + 2)(name + " -> " + tpe)})
    }
  }
  val ctx = new ScanCtx
  val result = LibraryScanner(VarName("mui"), home / "pr" / "material-ui" / "lib", ctx)
  println(flattenRes(result).keySet)
}
