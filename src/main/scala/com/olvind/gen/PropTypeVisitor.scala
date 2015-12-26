package com.olvind
package gen

import jdk.nashorn.internal.ir.{IdentNode, PropertyNode, ObjectNode}
import scala.collection.JavaConverters._

case class PropTypeVisitor(o: ObjectNode, jsContent: String, is: Seq[Import]) extends MyNodeVisitor(o){
  var propTypes: Option[Map[PropName, (PropString, Option[PropComment])]] = None
  o accept this

  def mapPropType(start: Int, ps: List[PropertyNode]): List[(PropName, (PropString, Option[PropComment]))] = {
    ps match {
      case Nil => Nil
      case p :: pt =>
        val commentOS = Some(jsContent.substring(start, p.getStart).trim).filterNot(_.isEmpty) map PropComment.clean
        val typeS     = PropString(jsContent.substring(p.getValue.getStart, p.getValue.getFinish))

        (PropName(p.getKeyName) -> (typeS, commentOS)) +: mapPropType(p.getValue.getFinish + 1, pt)
    }
  }

  override def enterPropertyNode(n: PropertyNode) =
    matcher((n.getKey, n.getValue)){
      case (i: IdentNode, o: ObjectNode) if i.getName == "propTypes" =>
        propTypes = Some(
          mapPropType(o.getStart + 1, o.getElements.asScala.toList).toMap
        )
    }
}
