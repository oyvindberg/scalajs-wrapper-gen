package com.olvind
package mui

import argonaut.CodecJson

import scalaz.{-\/, \/-}
import argonaut.Argonaut._

case class JsonField(name: PropName, `type`: Option[PropTypeUnparsed], header: PropTypeUnparsed, desc: String)
case class JsonSection(name: String, infoArray: List[JsonField])

object JsonSection{
  implicit val S                                        = CodecJson.derived[String]
  implicit val I1: CodecJson[PropName]                  =
    implicitly[CodecJson[String]].xmap(PropName)(_.value)
  implicit val I2: CodecJson[PropTypeUnparsed]          =
    implicitly[CodecJson[String]].xmap(PropTypeUnparsed)(_.value)
  implicit val FieldCodecJson: CodecJson[JsonField]     =
    casecodec4(JsonField.apply, JsonField.unapply)("name", "type", "header", "desc")
  implicit val SectionCodecJson: CodecJson[JsonSection] =
    casecodec2(JsonSection.apply, JsonSection.unapply)("name", "infoArray")
}

object MuiDocs extends DocProvider[MuiComponent] {
  def apply(prefix: String, comp: MuiComponent): (Map[PropName, PropComment], Option[ParsedMethodClass]) = {
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

        val methodClassOpt: Option[ParsedMethodClass] =
          methodSectionOpt map {
            case section: JsonSection =>
              ParsedMethodClass(
                prefix + comp.name.value + "M",
                section.infoArray map { f =>
                  ParsedMethod(
                    MuiTypeMapperMethod(comp.name, f.name),
                    Some(PropComment.clean(f.desc))
                  )
                }
              )
          }
        (propComments, methodClassOpt)
      case -\/(error) =>
        throw new RuntimeException(comp.name + ": " + error)
    }
  }
}
