package com.olvind
package requiresjs

import jdk.nashorn.internal.ir._

import scala.collection.mutable

sealed trait Exported
case class ExportedDefault(n: Node) extends Exported
case class ExportedNamed(name: VarName, node: Node) extends Exported

case class VisitorExports(n: FunctionNode) extends VisitorHelper[FunctionNode, Seq[Exported]](n){
  // Left if something is exported at `exported.default` for now
  private var ret: mutable.ArrayBuffer[Exported] =
    mutable.ArrayBuffer.empty

  object Exportable {
    def unapply(n: Node): Option[Node] =
      n match {
        case i: IdentNode if i.getName == "undefined" =>
          None
        case a: AccessNode if a.getProperty == "default" ⇒
          Some(a.getBase.asInstanceOf[Node])
        case _: FunctionNode | _: ObjectNode | _: IdentNode ⇒
          Some(n)
        case other ⇒
          None
      }
  }

  override def enterBinaryNode(bn: BinaryNode): Boolean =
    matcher(bn.lhs) {
      case a: AccessNode ⇒
        matcher(a.getBase) {
          case base: IdentNode if base.getName == "exports" ⇒
            (bn.rhs, a.getProperty) match {
              case (Exportable(node), "default") ⇒
                ret += ExportedDefault(node)

              case (Exportable(node), name: String) ⇒
                ret += ExportedNamed(VarName(name), node)

              case o ⇒
                println(s"ignoring $o")
                ()
            }
        }
    }

  override protected def fetchValue(): Seq[Exported] =
    ret

  override protected def assertions(): Unit = {
    require(ret.nonEmpty)
  }
}
