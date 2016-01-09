package com.olvind
package requiresjs

import java.io.File

import ammonite.ops._
import jdk.nashorn.internal.ir.FunctionNode

import scala.collection.mutable.ArrayBuffer
import scala.util.Try

object Require {
  def exists(path: Path): Boolean =
    new File(path.toString).exists()

  def apply(moduleName: VarName, p: Path): Required =
    apply(moduleName, p, new ScanCtx)

  private def apply(moduleName: VarName, p: Path, ctx: ScanCtx): Required = {
    val path   = p / "index.js"
    val parsed = ctx.parsedFile(path)
    val c      = CreateClassVisitor(parsed.result, p)

    val modules: ArrayBuffer[Required] =
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

            Multiple(varName, filePath,
              containedComponents.components.toList.map {
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
      }

    Multiple(moduleName, p, modules)
  }
}
