/**/package com.olvind
package requiresjs

import java.io.File

import ammonite.ops._
import jdk.nashorn.internal.ir.{FunctionNode, ObjectNode}

import scala.collection.mutable.ArrayBuffer
import scala.util.Try

object Require {
  def exists(path: Path): Boolean =
    new File(path.toString).exists

  def apply(moduleName: VarName, p: Path): Required =
    apply(moduleName, p, new ScanCtx)

  private def apply(moduleName: VarName, p: Path, ctx: ScanCtx): Required = {
    val path   = if (p.isFile) p else p / "index.js"
    val parsed = ctx.parsedFile(path)
    val c      = CreateClassVisitor(parsed.result, p)

    val modules: ArrayBuffer[Required] =
      c.imports map {
        case Import(varName, Left(_filePath: Path)) =>
          if (Try(_filePath.isDir).getOrElse(false)){
            apply(varName, _filePath, ctx)
          } else {
            parseFile(moduleName, ctx, _filePath, p)
          }
        case Import(varName, Right(ignored)) =>
          println(s"ignoring import $ignored")
          NotFound(varName)
      }

    Multiple(moduleName, p, modules)
  }

  def parseFile(varName: VarName, ctx: ScanCtx, file: Path, outerPath: Path): Required = {

    val filePath: Path =
      Path(file.toString() + ".js")
    val parsedFile: ParsedFile =
      ctx.parsedFile(filePath)
    val containedComponents: CreateClassVisitor[FunctionNode] =
      CreateClassVisitor(parsedFile.result, outerPath)

    def single(compName: CompName, o:ObjectNode): Single =
      Single(
        compName,
        FoundComponent(
          name      = compName,
          file      = filePath,
          imports   = containedComponents.imports,
          jsContent = parsedFile.content.substring(o.getStart, o.getFinish),
          propsOpt  = PropTypeVisitor(compName, o, parsedFile.content, containedComponents.imports).propTypes,
          methods   = containedComponents.memberMethods.get(compName)
        )
      )

    containedComponents.propTypeObjs.toList match {
      case Nil ⇒
        NotFound(varName)
      case (compName, o) :: Nil ⇒
        single(compName, o)
      case many ⇒
        Multiple(varName,
          filePath,
          many.map {
            case (compName, o) ⇒ single(compName, o)
          }
        )
    }
  }
}
