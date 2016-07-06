package com.olvind
package requiresjs

import ammonite.ops._
import jdk.nashorn.internal.ir.{FunctionNode, Node, ObjectNode}

import scala.language.postfixOps

object Require {
  def apply(p: Path): Required =
    recurse(p, new ScanCtx)

  private def recurse(requiredPath: Path, ctx: ScanCtx): Required =
    ctx.required(requiredPath, doRecurse(requiredPath))

  private def doRecurse(requiredPath: Path)(ctx: ScanCtx): Required = {
    val ResolvedPath(filePath: Path, folderPath: Path) =
      ResolvePath(requiredPath)

    val ParsedFile(_, fileStr: String, fileParsed: FunctionNode) =
      ctx.parsedFile(filePath)

    val imports:       Seq[Import]                      = VisitorImports(fileParsed, folderPath).value
    val components:    Map[CompName, ObjectNode]        = VisitorComponents(fileParsed).value
    val memberMethods: Map[CompName, Set[MemberMethod]] = VisitorComponentMembers(fileParsed).value
    val exports:       Seq[Node]                        = VisitorExports(fileParsed).value

    //todo: split require/react parsing!
    def component(compName: CompName, o: ObjectNode) =
      Single(
        compName,
        FoundComponent(
          name      = compName,
          file      = filePath,
          jsContent = fileStr.substring(o.getStart, o.getFinish),
          props     = VisitorPropType(compName, o, fileStr, imports).value,
          methods   = memberMethods.get(compName)
        )
      )

    components.toList.distinct match {
      case Nil ⇒
        /* todo: Parse exports! */
        val modules: Seq[Required] =
          imports.collect {
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
