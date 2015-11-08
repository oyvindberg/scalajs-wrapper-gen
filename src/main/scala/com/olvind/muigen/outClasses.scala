package com.olvind.muigen

import scala.collection.mutable

sealed trait OutClass {
  val fields: mutable.ArrayBuffer[OutField] =
    mutable.ArrayBuffer.empty[OutField]

  def addField(f: OutField) =
    if (fields.exists(_.name == f.name)) () else fields += f

  def fieldStats =
    FieldStats(
      maxFieldNameLen = fields.map(_.fieldNameLength).max,
      maxTypeNameLen  = fields.map(_.typeNameLength).max
    )

  def enumClases: Seq[OutEnumClass] =
    fields.map(_.baseType).collect{
      case o: OutParamEnum => o.enumClass
    }
}

case class FieldStats(maxFieldNameLen: Int, maxTypeNameLen: Int)

case class OutComponentClass(name: String) extends OutClass
case class OutMethodClass(name: String) extends OutClass
case class OutEnumClass(name: String, members: Seq[String])
