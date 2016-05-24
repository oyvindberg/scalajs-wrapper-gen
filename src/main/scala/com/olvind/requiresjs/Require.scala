package com.olvind
package requiresjs

import ammonite.ops._
import jdk.nashorn.internal.ir.{FunctionNode, ObjectNode}

import scala.util.Try

object Require {
  def apply(moduleName: VarName, p: Path): Required =
    recurse(moduleName, p, new ScanCtx)

  private def recurse(moduleName: VarName,
                      _p:         Path,
                      ctx:        ScanCtx): Required = {

    val (file, folder) =
      _p.isFile match {
        case true  ⇒ (_p,              _p.copy(_p.segments.dropRight(1)))
        case false ⇒ (_p / "index.js", _p)
      }

    val parsed: ParsedFile =
      ctx.parsedFile(file)

    val c: CreateClassVisitor[FunctionNode] =
      CreateClassVisitor(parsed.result, folder)

    val modules: Seq[Required] =
      c.imports map {
        case Import(varName, Left(requiredPath: Path)) =>
          if (Try(requiredPath.isDir).getOrElse(false)) {
            recurse(varName, requiredPath, ctx)
          } else {
            val requiredJsFile: Path = //meh api
              requiredPath.copy(requiredPath.segments.dropRight(1) :+ requiredPath.segments.last + ".js")

            resolved(varName, ctx, requiredJsFile, folder)
          }
        case Import(varName, Right(ignored)) =>
          ExternalDep(VarName(ignored))
      }

    Multiple(moduleName, _p, modules)
  }

  private def resolved(varName:   VarName,
                       ctx:       ScanCtx,
                       filePath:  Path,
                       outerPath: Path): Required = {

    val parsedFile: ParsedFile =
      ctx.parsedFile(filePath)

    val containedComponents: CreateClassVisitor[FunctionNode] =
      CreateClassVisitor(parsedFile.result, outerPath)

    def single(compName: CompName, o: ObjectNode): Single =
      Single(
        compName,
        FoundComponent(
          name      = compName,
          file      = filePath,
          jsContent = parsedFile.content.substring(o.getStart, o.getFinish),
          propsOpt  = PropTypeVisitor(compName, o, parsedFile.content, containedComponents.imports).propTypes,
          methods   = containedComponents.memberMethods.get(compName)
        )
      )

    containedComponents.propTypeObjs.toList match {
      case Nil ⇒
        SingleNotComp(varName)
      case (compName, o) :: Nil ⇒
        single(compName, o)
      case many ⇒
        Multiple(varName,
          filePath,
          many.map { case (compName, o) ⇒ single(compName, o) }
        )
    }
  }
}
