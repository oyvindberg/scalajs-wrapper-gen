package com.olvind

import com.olvind.requiresjs._
import jdk.nashorn.internal.ir.FunctionNode
import ammonite.ops._
/**
  * Created by oyvindberg on 23/05/16.
  */
class JsParserTest extends org.scalatest.FunSuite
                           with org.scalatest.Matchers {

  private val resources = cwd / "src" / "test" / "resources"
  private val mui15     = resources / "mui15" / "comps"

  test("Divider.propTypes = propTypes;") {

    val parsed: ParsedFile =
      JsParser(mui15 / "Divider.js")

    val visitor: CreateClassVisitor[FunctionNode] =
      new CreateClassVisitor[FunctionNode](parsed.result, mui15)
    println(visitor.propTypeObjs)
  }

  test("Drawer.propTypes = {...}") {
    val result: Required = Require(mui15 / "Drawer.js")
    println(flattenScan(result).keySet)
  }
}
