package com.olvind.muigen

import argonaut.Argonaut._

case class JsonField(name: String, `type`: Option[String], header: String, desc: String){
  def isRequired: Boolean =
    header.toLowerCase.contains("required")
}
case class JsonSection(name: String, infoArray: List[JsonField])

object JsonSection{
  implicit def FieldCodecJson =
    casecodec4(JsonField.apply, JsonField.unapply)("name", "type", "header", "desc")
  implicit def SectionCodecJson =
    casecodec2(JsonSection.apply, JsonSection.unapply)("name", "infoArray")
}
