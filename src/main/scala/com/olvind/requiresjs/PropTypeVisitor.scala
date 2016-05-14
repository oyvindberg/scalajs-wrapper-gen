package com.olvind
package requiresjs

import jdk.nashorn.internal.ir._

import scala.collection.JavaConverters._

case class PropTypeVisitor(n: CompName, o: ObjectNode, jsContent: String, is: Seq[Import]) extends MyNodeVisitor(o){
  var propTypes: Option[Map[PropName, PropUnparsed]] = None
  o accept this

  def mapPropType(start: Int, ps: List[PropertyNode]): List[(PropName, PropUnparsed)] = {
    ps match {
      case Nil => Nil
      case p :: pt =>
        val commentOS = Some(jsContent.substring(start, p.getStart).trim).filterNot(_.isEmpty) map PropComment.clean
        val typeS     = PropTypeUnparsed(jsContent.substring(p.getValue.getStart, p.getValue.getFinish))

        (PropName(p.getKeyName) -> PropUnparsed(n, typeS, commentOS)) +: mapPropType(p.getValue.getFinish + 1, pt)
    }
  }

  override def enterObjectNode(o: ObjectNode): Boolean = {
    propTypes = Some(
      mapPropType(o.getStart + 1, o.getElements.asScala.toList).toMap
    )
    false
  }
}
