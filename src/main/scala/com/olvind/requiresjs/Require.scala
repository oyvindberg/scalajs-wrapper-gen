/**/package com.olvind
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

  private def a(varName:    VarName,
                parsedComp: ParsedFile,
                filePath:   Path,
                v:          CreateClassVisitor[FunctionNode]): Multiple =
    Multiple(varName,
      filePath,
      v.propTypeObjs.toList map {
        case (compName, o) =>
          Single(
            compName,
            FoundComponent(
              name      = compName,
              file      = filePath,
              imports   = v.imports,
              jsContent = parsedComp.content.substring(o.getStart, o.getFinish),
              propsOpt  = PropTypeVisitor(compName, o, parsedComp.content, v.imports).propTypes,
              methods   = v.memberMethods.get(compName)
            )
          )
      }
    )

  private def apply(moduleName: VarName, p: Path, ctx: ScanCtx): Required = {
    val path   = if (p.isFile) p else p / "index.js"
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
            val parsedFile: ParsedFile =
              ctx.parsedFile(filePath)
            val containedComponents: CreateClassVisitor[FunctionNode] =
              CreateClassVisitor(parsedFile.result, p)
            a(varName, parsedFile, filePath, containedComponents)
          }
        case Import(varName, Right(ignored)) =>
          println(s"ignoring import $ignored")
          NotFound
      }

    Multiple(moduleName, p, modules)
  }
}
