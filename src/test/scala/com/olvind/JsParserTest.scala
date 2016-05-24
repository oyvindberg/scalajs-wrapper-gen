package com.olvind

import ammonite.ops._
import com.olvind.requiresjs._

class JsParserTest extends org.scalatest.FunSuite
                           with org.scalatest.Matchers {

  private val mui15 = cwd / "src" / "test" / "resources" / "mui15" / "comps"

  test("Divider.propTypes = propTypes;") {
    val result = Require(mui15 / "Divider.js")
    println(flattenScan(result).keySet)
  }

  test("Drawer.propTypes = {...}") {
    val result: Required = Require(mui15 / "Drawer.js")
    println(flattenScan(result).keySet)
  }
}
