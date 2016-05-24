package com.olvind
package requiresjs

import ammonite.ops._
import jdk.nashorn.internal.ir.ObjectNode

import scala.language.postfixOps

object Require {
  def apply(p: Path): Required =
    recurse(p, new ScanCtx)

  private def recurse(requiredPath: Path, ctx: ScanCtx): Required =
    ctx.required(requiredPath, _recurse(requiredPath))

  private def _recurse(requiredPath: Path)(ctx: ScanCtx): Required = {
    val ResolvedPath(filePath, folderPath) = ResolvePath(requiredPath)

    val parsedFile: ParsedFile =
      ctx.parsedFile(filePath)

    val i: ImportVisitor =
      ImportVisitor(parsedFile.result, folderPath)

    val c: CreateClassVisitor =
      CreateClassVisitor(parsedFile.result, folderPath)

    //todo: split require/react parsing!
    def component(compName: CompName, o: ObjectNode): Single =
      Single(
        compName,
        FoundComponent(
          name      = compName,
          file      = filePath,
          jsContent = parsedFile.content.substring(o.getStart, o.getFinish),
          propsOpt  = PropTypeVisitor(compName, o, parsedFile.content, i.imports).propTypes,
          methods   = c.memberMethods.get(compName)
        )
      )

    c.propTypeObjs.toList.distinct match {
      case Nil ⇒
        /* todo: Parse exports! */
        val modules: Seq[Required] =
          i.imports.collect {
            case Import(varName, Left(innerPath: Path)) =>
              recurse(innerPath, ctx)
          }.distinct

        Required(requiredPath, modules)

      case (compName, o) :: Nil ⇒
        component(compName, o)

      case many ⇒
        Required(filePath, many map (component _ tupled))
    }
  }
}
