package com.olvind
package requiresjs

import ammonite.ops._
import jdk.nashorn.internal.ir.{FunctionNode, ObjectNode}

object Require {
  def apply(p: Path): Required =
    recurse(p, new ScanCtx)

  private def recurse(_p: Path, ctx: ScanCtx): Required = {

    val (filePath, folderPath) =
      exists(_p) match {
        case true ⇒
          _p.isDir match {
            case true ⇒
              (_p / "index.js", _p)
            case false ⇒
              (_p, _p.copy(_p.segments.dropRight(1)))
          }
        case false ⇒
          (_p.copy(_p.segments.dropRight(1) :+ _p.segments.last + ".js"), _p.copy(_p.segments.dropRight(1)))
      }

    val parsedFile: ParsedFile =
      ctx.parsedFile(filePath)

    val c: CreateClassVisitor[FunctionNode] =
      CreateClassVisitor(parsedFile.result, folderPath)

    def component(compName: CompName, o: ObjectNode): Single =
      Single(
        compName,
        FoundComponent(
          name      = compName,
          file      = filePath,
          jsContent = parsedFile.content.substring(o.getStart, o.getFinish),
          propsOpt  = PropTypeVisitor(compName, o, parsedFile.content, c.imports).propTypes,
          methods   = c.memberMethods.get(compName)
        )
      )

    c.propTypeObjs.toList match {
      case Nil ⇒
        val modules: Seq[Required] =
          c.imports collect {
            case Import(varName, Left(requiredPath: Path)) =>
              recurse(requiredPath, ctx)
          }
        /* todo: Parse exports! */

        Multiple(filePath, modules)
//        SingleNotComp(_p)
      case (compName, o) :: Nil ⇒
        component(compName, o)
      case many ⇒
        Multiple(
          filePath,
          many.map { case (compName, o) ⇒ component(compName, o) }
        )
    }
  }
}
