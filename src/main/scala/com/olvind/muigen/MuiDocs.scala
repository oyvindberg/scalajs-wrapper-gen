package com.olvind
package muigen

import argonaut.CodecJson

import scalaz.{-\/, \/-}
import argonaut.Argonaut._

case class JsonField(name: PropName, `type`: Option[PropString], header: PropString, desc: String)
case class JsonSection(name: String, infoArray: List[JsonField])

object JsonSection{
  implicit val S = CodecJson.derived[String]
  implicit val I1: CodecJson[PropName] =
    implicitly[CodecJson[String]].xmap(PropName)(_.value)
  implicit val I2: CodecJson[PropString] =
    implicitly[CodecJson[String]].xmap(PropString)(_.value)
  implicit val FieldCodecJson: CodecJson[JsonField] =
    casecodec4(JsonField.apply, JsonField.unapply)("name", "type", "header", "desc")
  implicit val SectionCodecJson: CodecJson[JsonSection] =
    casecodec2(JsonSection.apply, JsonSection.unapply)("name", "infoArray")
}

object MuiDocs {
  def apply(comp: ManualComponent): (Map[PropName, PropComment], Option[OutMethodClass]) = {
    comp.json.decodeEither[List[JsonSection]] match {
      case \/-(sections) =>
        val sMap: Map[String, JsonSection] =
          sections.map(s => s.name -> s).toMap

        val eventsFromDoc: List[JsonField] =
          sMap.get(comp.overrideEvents getOrElse "Events").toList.flatMap(_.infoArray)

        val propsFromDoc: Seq[JsonField] =
          comp.propsSections.map(sMap.apply).flatMap(_.infoArray)

        val fromDoc: Map[PropName, JsonField] =
          (eventsFromDoc ++ propsFromDoc).map(f => (f.name, f)).toMap

        val propComments: Map[PropName, PropComment] =
          fromDoc mapValues {
            j =>
              val header = Some(j.header).filterNot(_.value.trim == "optional").fold("")(_ + ":")
              PropComment.clean(s"$header ${j.desc}")
          }

        val methodSectionOpt: Option[JsonSection] =
          sMap.get(comp.overrideMethods getOrElse "Methods")

        val methodClassOpt: Option[OutMethodClass] =
          methodSectionOpt map {
            case section: JsonSection =>
              val methodOut = OutMethodClass(comp.name + "M")
              section.infoArray.foreach { f =>
                methodOut.addField(
                  ReqField(
                    f.name.clean,
                    PropTypeClass("Unit"),
                    Some(PropComment.clean(f.desc)),
                    None,
                    None
                  )
                )
              }
              methodOut
          }
        (propComments, methodClassOpt)
      case -\/(error) =>
        throw new RuntimeException(comp.name + ": " + error)
    }
  }
}
