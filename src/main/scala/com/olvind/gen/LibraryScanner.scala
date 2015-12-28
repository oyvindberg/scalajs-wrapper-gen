package com.olvind
package gen

import java.io.File

import ammonite.ops._
import jdk.nashorn.internal.ir.FunctionNode

import scala.util.Try

object flattenRes {
  def apply(r: ScanResult): Map[CompName, FoundComponent] =
    r match {
      case Single(n, c)     => Map(n -> c)
      case Module(_, _, rs) => (rs flatMap apply).toMap
    }
}
object LibraryScanner {
  def exists(path: Path): Boolean =
    new File(path.toString).exists()

  def apply(moduleName: VarName, p: Path, ctx: ScanCtx): ScanResult = {
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
                    FoundComponent(
                      name      = compName,
                      file      = filePath,
                      imports   = containedComponents.imports,
                      jsContent = parsedComp.content.substring(o.getStart, o.getFinish),
                      propsOpt  = PropTypeVisitor(compName, o, parsedComp.content, containedComponents.imports).propTypes
                    )
                  )
              }
            )
          }
    })
  }
}
