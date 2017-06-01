package com.olvind
package requiresjs

import ammonite.ops.Path
import jdk.nashorn.internal.ir.FunctionNode

case class ParsedFile(path: Path, content: String, result: FunctionNode)