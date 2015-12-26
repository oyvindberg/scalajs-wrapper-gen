package com.olvind
package gen

import ammonite.ops.Path
import jdk.nashorn.internal.ir._

import scala.collection.JavaConverters._
import scala.collection.mutable

case class CreateClassVisitor[N <: Node](n: N, currentPath: Path) extends MyNodeVisitor(n){
  val components = mutable.Map.empty[CompName, ObjectNode]
  val imports    = mutable.ArrayBuffer.empty[Import]

  private[CreateClassVisitor] var nameStack: List[VarName] = Nil

  n accept this
  require(nameStack.isEmpty)

  override def enterPropertyNode(n: PropertyNode): Boolean = {
    matcher(n.getKey){
      case (i: IdentNode) =>
        nameStack = VarName(i.getName) :: nameStack
    }
  }

  override def leavePropertyNode(n: PropertyNode): Node = {
    (nameStack.headOption, n.getKey) match {
      case (Some(n1), n2: IdentNode) if n1.value == n2.getName =>
        nameStack = nameStack drop 1
      case _ => ()
    }
    n
  }

  override def enterVarNode(n: VarNode): Boolean =
    matcher(n.getName){
      case name =>
        nameStack = VarName(name.getName) :: nameStack
    }

  override def leaveVarNode(n: VarNode): Node = {
    (nameStack.headOption, n.getName) match {
      case (Some(n1), n2) if n1.value == n2.getName =>
        nameStack = nameStack drop 1
      case _ => ()
    }
    n
  }

  override def enterCallNode(n: CallNode): Boolean =
    matcher((n.getFunction, n.getArgs.asScala.toList)){
      case (a: AccessNode, List(o: ObjectNode)) if a.getProperty == "createClass" =>
        nameStack.headOption match {
          case Some(name) => components += (CompName(name.value) -> o)
          case None => ()
        }
      case (i: IdentNode, List(o: LiteralNode[_])) if i.getName == "require" =>
        nameStack.headOption match {
          case Some(name) =>
            val target =
              if (o.getString.startsWith(".")) Left(add(currentPath, o.getString))
              else Right(o.getString)
            imports += Import(name, target)
          case None =>
            ???
        }
      case (i: IdentNode, List(arg: IdentNode)) if i.getName == "_interopRequireDefault" =>
        nameStack.headOption match {
          case Some(name) =>
            imports.find(_.varName == VarName(arg.getName)) match {
              case Some(referenced) => imports += referenced.copy(varName = name)
              case None => ???
            }
          case None => ???
        }
    }
}
