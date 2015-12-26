package com.olvind
package gen

import jdk.nashorn.internal.ir.{BlockLexicalContext, Node}
import jdk.nashorn.internal.ir.visitor.NodeVisitor

abstract class MyNodeVisitor[N <: Node](n: N) extends NodeVisitor(new BlockLexicalContext){
  def matcher[M](m: M)(f: PartialFunction[M, Unit]): Boolean =
    if (f.isDefinedAt(m)) {
      f(m)
      true
    } else true

}
