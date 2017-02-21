package com.olvind

import ammonite.ops.Path
import com.olvind.requiresjs._

import scala.collection.mutable
class Runner[D <: ComponentDef](library: Library[D]) {

  val prelude: String =
    s"""package chandu0101.scalajs.react.components
      |package ${library.name}
      |
      |import chandu0101.macros.tojs.JSMacro
      |import japgolly.scalajs.react._
      |import scala.scalajs.js
      |import scala.scalajs.js.`|`
      |
      |/**
      | * This file is generated - submit issues instead of PR against it
      | */
    """.stripMargin

  val foundComponents: Map[CompName, requiresjs.FoundComponent] = {
    val visited: mutable.Map[Path, Int] = mutable.HashMap.empty

    def flattenScan(r: Required): Map[CompName, FoundComponent] =
      r match {
        case Single(n, c)     =>
          Map(n -> c)
        case Multiple(p, rs) =>
          if (visited.getOrElse(p, 0) > 3){
            Map.empty
          } else {
            visited(p) = visited.getOrElse(p, 0) + 1
            val requireds: Seq[Required] = rs.map(_.run).toList
            val asd = (requireds flatMap flattenScan).toMap
            println(s"Found in path $p: ${asd.keys}")

            asd
          }

        case other ⇒
          Map.empty
      }

    flattenScan(requiresjs.Require(library.location))
  }

  val (mainFiles: Seq[PrimaryOutFile], secondaryFiles: Seq[SecondaryOutFile]) =
    library.components.foldLeft((Seq.empty[PrimaryOutFile], Seq.empty[SecondaryOutFile])){
      case ((ps, ss), c) =>
        val pc     = ParseComponent(foundComponents, library, c)
        val (p, s) = Printer(library.prefixOpt.getOrElse(""), pc)
        (ps :+ p, ss ++ s)
  }

  library.outputPath.toIO.mkdir()

  printToFile(library.outputPath / "gen-types.scala"){
    w =>
      w.println(prelude)
      secondaryFiles.sortBy(_.content).distinct.foreach{
        case file =>
          w.println(file.content)
          w.println("")
      }
  }

  mainFiles foreach {
    case PrimaryOutFile(compName, content, secondaries) =>
      printToFile(library.destinationPath(compName)){
        w =>
          w.println(prelude + content)
          secondaries.foreach{
            case SecondaryOutFile(_, c) =>
              w.println("")
              w.println(c)
          }
      }
  }
}