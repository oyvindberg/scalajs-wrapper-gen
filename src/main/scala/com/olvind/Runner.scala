package com.olvind

import ammonite.ops.Path
import com.olvind.requiresjs._
object Runner {

  def preludeFor(library: Library): String =
    s"""package chandu0101.scalajs.react.components
       |package ${library.name}
       |
       |import chandu0101.macros.tojs.JSMacro
       |import japgolly.scalajs.react._
       |import org.scalajs.dom
       |import scala.scalajs.js
       |import scala.scalajs.js.`|`
       |
       |/**
       | * This file is generated - submit issues instead of PR against it
       | */
    """.stripMargin

  def destinationPathFor(outputPath: Path, prefixOpt: Option[String], comp: CompName): Path = {
    val baseFile = comp.value + ".scala"
    val filename = prefixOpt.fold(baseFile)(_ + baseFile)
    outputPath / filename
  }

  def apply(library: Library, outFolder: Path) = {
    val foundComponents: Seq[FoundComponent] =
      Require(library.location)

    val allFound: Map[CompName, FoundComponent] =
      foundComponents.map(c => c.name -> c).toMap

    val (mainFiles: Seq[PrimaryOutFile], secondaryFiles: Seq[SecondaryOutFile]) =
      foundComponents.foldLeft((Seq.empty[PrimaryOutFile], Seq.empty[SecondaryOutFile])){
        case ((ps, ss), c) =>

          val parsed: ParsedComponent =
            ParseComponent(allFound, library, c)

          val (primaryFile, secondaryFile) =
            Printer(library.prefixOpt.getOrElse(""), parsed)

          (ps :+ primaryFile, ss ++ secondaryFile)
      }

    outFolder.toIO.mkdir()

    val prelude: String =
      preludeFor(library)

    printToFile(outFolder / "gen-types.scala"){
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
        printToFile(destinationPathFor(outFolder, library.prefixOpt, compName)){
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
}