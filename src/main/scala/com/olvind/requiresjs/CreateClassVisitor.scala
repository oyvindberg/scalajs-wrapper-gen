package com.olvind
package requiresjs

import ammonite.ops.Path
import jdk.nashorn.internal.ir._

import scala.collection.JavaConverters._
import scala.collection.mutable

case class CreateClassVisitor(n: FunctionNode, currentPath: Path) extends MyNodeVisitor(n){
  val propTypeObjs  = mutable.Map.empty[CompName, ObjectNode]
  val memberMethods = mutable.Map.empty[CompName, Set[MemberMethod]]

  private[CreateClassVisitor] var nameStack: List[VarName] = Nil

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

  override def enterBinaryNode(bn: BinaryNode): Boolean = {
    (bn.lhs(), bn.rhs()) match {
      case (a: AccessNode, o: ObjectNode) if a.getProperty == "propTypes" ⇒
        propTypeObjs(CompName(a.getBase.asInstanceOf[IdentNode].getName)) = o
      case other ⇒ ()
    }
    true
  }

  override def enterCallNode(n: CallNode): Boolean =
    matcher((n.getFunction, n.getArgs.asScala.toList)){
      /* old style createClass way of creating react components.
          We dig out `propTypes` out of the structure */
      case (a: AccessNode, List(o: ObjectNode)) if a.getProperty == "createClass" =>
        o.getElements.asScala.collect {
          case p: PropertyNode ⇒ (p.getKey, p.getValue)
        }.collectFirst {
          case (i: IdentNode, o: ObjectNode) if i.getName == "propTypes" =>
            nameStack.headOption match {
              case Some(name) => propTypeObjs(CompName(name.value)) = o
              case None => ()
            }
        }

      /* dig out all member methods from a class variant */
      case (createClassName: IdentNode, (compName: IdentNode) :: (members: LiteralNode.ArrayLiteralNode) :: Nil) if createClassName.getName.contains("createClass") ⇒
        members.getArray.collect {
          case member: ObjectNode ⇒
            matcher(member.getElements.asScala.toList) {
              case (name: PropertyNode) :: (value: PropertyNode) :: Nil ⇒
                matcher((name.getValue, value.getValue)) {
                  case (fi: LiteralNode[_], f: FunctionNode) ⇒
                    val m = MemberMethod(fi.getString, f.getParameters.asScala.map(_.getName))
                    val compName_ = CompName(compName.getName)
                    memberMethods.get(compName_) match {
                      case Some(existing) ⇒
                        memberMethods(compName_) = existing + m
                      case None ⇒
                        memberMethods(compName_) = Set(m)
                    }
                }
                name.getKeyName
            }
        }
    }

  n accept this
  require(nameStack.isEmpty)
}
