package com.olvind
package gen

import java.io.File

import ammonite.ops._
import jdk.nashorn.internal.ir.FunctionNode

import scala.util.Try

object Run extends App {
  def indented(n: Int)(s: String) =
    println((" " * n) + s)

  def print(indent: Int)(r: Result): Unit = {
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
  val ctx = new Ctx
  val result = PropTypeParser(VarName("mui"), home / "pr" / "material-ui" / "lib", ctx)
  println(flattenRes(result).keySet)
}

sealed trait Result
case class Module(name: VarName, path: Path, rs: Iterable[Result]) extends Result
case class Single(compName: CompName, c: Component) extends Result

object flattenRes {
  def apply(r: Result): Map[CompName, Component] =
    r match {
      case Single(n, c)     => Map(n -> c)
      case Module(_, _, rs) => (rs flatMap apply).toMap
    }
}
object PropTypeParser {
  def exists(path: Path): Boolean =
    new File(path.toString).exists()

  def apply(moduleName: VarName, p: Path, ctx: Ctx): Result = {
    val path   = p / "index.js"
    val parsed = ctx.parsedFile(path)
    val c      = CreateClassVisitor(parsed.result, p)

    Module(moduleName, p,
      c.imports map {
        case Import(varName, Left(_filePath)) =>
          if (Try(_filePath.isDir).getOrElse(false)){
            apply(varName, _filePath, ctx)
          } else {
            val filePath: Path =
              Path(_filePath.toString() + ".js")
            val parsedComp: ParsedFile =
              ctx.parsedFile(filePath)
            val containedComponents: CreateClassVisitor[FunctionNode] =
              CreateClassVisitor(parsedComp.result, p)

            Module(varName, filePath,
              containedComponents.components.map {
                case (compName, o) =>
                  Single(
                    compName,
                    Component(
                      name      = compName,
                      file      = filePath,
                      imports   = containedComponents.imports,
                      jsContent = parsedComp.content.substring(o.getStart, o.getFinish),
                      propsOpt  = PropTypeVisitor(o, parsedComp.content, containedComponents.imports).propTypes
                    )
                  )
              }
            )
          }
    })
  }
}
