package com.olvind
package requiresjs

import ammonite.ops._
import jdk.nashorn.internal.ir.ObjectNode

import scala.language.postfixOps

object Require {
  def apply(p: Path): Required =
    recurse(p, new ScanCtx)

  private def recurse(requiredPath: Path, ctx: ScanCtx): Required =
    ctx.required(requiredPath, doRecurse(requiredPath))

  private def doRecurse(requiredPath: Path)(ctx: ScanCtx): Required = {
    val ResolvedPath(filePath, folderPath) = ResolvePath(requiredPath)

    val parsedFile: ParsedFile =
      ctx.parsedFile(filePath)

    val importV: VisitorImports =
      VisitorImports(parsedFile.result, folderPath)

    val componentsV: VisitorComponents =
      VisitorComponents(parsedFile.result)

    val memberV: VisitorComponentMembers =
      VisitorComponentMembers(parsedFile.result)

    //todo: split require/react parsing!
    def component(compName: CompName, o: ObjectNode): Single =
      Single(
        compName,
        FoundComponent(
          name      = compName,
          file      = filePath,
          jsContent = parsedFile.content.substring(o.getStart, o.getFinish),
          propsOpt  = VisitorPropType(compName, o, parsedFile.content, importV.value).value,
          methods   = memberV.value.get(compName)
        )
      )

    componentsV.value.toList.distinct match {
      case Nil ⇒
        /* todo: Parse exports! */
        val modules: Seq[Required] =
          importV.value.collect {
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
