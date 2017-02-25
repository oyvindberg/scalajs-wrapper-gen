package com.olvind
package requiresjs

import ammonite.ops._
import jdk.nashorn.internal.ir.{FunctionNode, Node, ObjectNode}

import scala.collection.immutable.Iterable
import scala.collection.mutable
import scala.language.postfixOps

object Require {
  case class Required(components: Seq[FoundComponent], dependencies: Seq[Path])

  def apply(paths: Seq[Path]): Seq[FoundComponent] = {

    val visited = mutable.HashSet.empty[Path]

    def recursiveLookup(path: Path): Seq[FoundComponent] = {
      if (visited.contains(path)) Seq.empty
      else {
        visited += path

        parseFile(path) match {
          case Required(components: Seq[FoundComponent], dependencies) =>
            components ++ dependencies.flatMap(recursiveLookup)
        }
      }
    }

    paths.flatMap(recursiveLookup)
  }

  private def parseFile(requiredPath: Path): Required = {
    val ResolvedPath(filePath: Path, folderPath: Path) =
      ResolvePath(requiredPath)

    val ParsedFile(_, fileStr: String, fileParsed: FunctionNode) =
      JsParser(filePath)

    val imports:         Seq[Import]                      = VisitorImports(fileParsed, folderPath).value
    val foundComponents: Map[CompName, ObjectNode]        = VisitorComponents(fileParsed).value
    val memberMethods:   Map[CompName, Set[MemberMethod]] = VisitorComponentMembers(fileParsed).value
    val exports:         Seq[Node]                        = VisitorExports(fileParsed).value

    //todo: split require/react parsing!
    val components: Iterable[FoundComponent] =
      foundComponents map {
        case (compName: CompName, o: ObjectNode) =>
          FoundComponent(
            name      = compName,
            file      = filePath,
            jsContent = fileStr.substring(o.getStart, o.getFinish),
            props     = VisitorPropType(compName, o, fileStr, imports).value,
            methods   = memberMethods.get(compName)
          )
      }

    /* todo: Parse exports! */
    val dependencies: Seq[Path] =
      imports.collect {
        case Import(_, Left(innerPath: Path)) => innerPath
      }.distinct

    println(s"parseFile($requiredPath) = ${components.map(_.name.value)}")

    Required(components.toSeq, dependencies)
  }
}
