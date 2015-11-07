package com.olvind.muigen

import com.olvind.muigen.StringUtils.padTo

sealed trait OutField{
  val name: String
  val jsonOpt: Option[JsonField]
  val baseType: OutParam
  val typeName: String
  def toString(fs: FieldStats): String

  final def typeNameLength  = typeName.length
  final def fieldNameLength = name.length
  final def comment: String =
    jsonOpt.fold("") { j =>
      val header = Some(j.header).filterNot(_ == "optional").fold("")(_ + ":")
      s"\t/* $header ${j.desc}*/\n"
    }
}

final case class  ReqField(name: String, baseType: OutParam, jsonOpt: Option[JsonField]) extends OutField{
  override val typeName = baseType.typeName
  override def toString(fs: FieldStats): String =
    s"$comment\t${padTo(name + ": ")(fs.maxFieldNameLen + 2)}${typeName}"
}

final case class  OptField(name: String, baseType: OutParam, jsonOpt: Option[JsonField]) extends OutField{
  override val typeName = s"js.UndefOr[${baseType.typeName}]"
  override def toString(fs: FieldStats): String =
    s"$comment\t${padTo(name + ": ")(fs.maxFieldNameLen + 2)}${padTo(typeName)(fs.maxTypeNameLen + 2)} = js.undefined"
}

sealed trait OutParam{def typeName: String}
case class OutParamClass(override val typeName: String) extends OutParam

case class OutParamEnum(component: String, name: String, ss: Seq[String]) extends OutParam{
  require(ss.nonEmpty)
  override val typeName = component + name.capitalize
  def enumClass =
    OutEnumClass(typeName, ss)
}

object OutParam {

  val Params = "function\\(([^\\)]+)\\)".r

  def mapFunction(compName: String, name: String, s: String) = s match {
    case "function()"                         => OutParamClass("Callback")
    case Params(params)                       =>
      val mappedParams = params.split(",").map(_.trim).filterNot(_.isEmpty).map(OutParam.mapType(compName, name))
      val paramPart = if (mappedParams.length == 1) mappedParams.head.typeName else mappedParams.map(_.typeName).mkString("(", ", ", ")")
      OutParamClass(s"$paramPart => Callback")
    case f if f.contains("(e)")               => OutParamClass(s"${OutParam.mapType(compName, name)("event").typeName} => Callback")
  }

  def mapType(compName: String, fieldName: String)(t: String): OutParam = {
    def is(s: String) = fieldName.toLowerCase contains s.toLowerCase
    def split(drop: Int, s: String) =
      s.split("[\"\\(\\)\\[\\],\\W]").map(_.trim).filterNot(_.isEmpty).drop(drop)

    t match {
      case e if e.contains(" or ")                        => OutParamClass((e.split(" or ") map mapType(compName, fieldName) map (_.typeName)).mkString(" | "))
      case e if e.contains("oneOfType")                   => OutParamClass((split(1, e) map mapType(compName, fieldName) map (_.typeName)).mkString(" | "))
      case "string" if is("color")                        => OutParamClass("MuiColor") //at least for IconMenus
      case "item"                                         => OutParamClass("js.Any") //at least for IconMenus
      case "element: IconButton"                          => OutParamClass("ReactElement") //at least for IconMenus
      case "element"                                      => OutParamClass("ReactElement") //at least for IconMenus
      case "node"                                         => OutParamClass("ReactNode") //at least for IconMenus
      case "value"                                        => OutParamClass("Double") //at least for slider
      case "menuItem"                                     => OutParamClass("js.Any")
      case "selectedIndex"                                => OutParamClass("Int")
      case "number"                                       => OutParamClass("Double")
      case "integer"                                      => OutParamClass("Int")
      case "array" | "Array of elements"                  => OutParamClass("js.Array[js.Any]")
      case "event"                                        =>
        val eventType =
          if      (is("touch")) "ReactTouchEvent"
          else if (is("key"))   "ReactKeyboardEvent"
          else if (is("mouse")) "ReactMouseEvent"
          else if (is("wheel")) "ReactWheelEvent"
          else if (is("drag"))  "ReactDragEvent"
          else                  "ReactEvent"

        OutParamClass(eventType)
      case "time"                                         => OutParamClass("js.Date")
      case "date" | "date object"                         => OutParamClass("js.Date")
      case "string"                                       => OutParamClass("String")
      case "string (label)"                               => OutParamClass("String")
      case "object" if is("style")                        => OutParamClass("CssProperties")
      case "object"                                       => OutParamClass("js.Any")
      case "bool" | "boolean" | "boole"                   => OutParamClass("Boolean")
      case "nill" | "null"                                => OutParamClass("js.UndefOr[Nothing]")
      case "string|ReactComponent"                        => OutParamClass("String | ReactElement")
      case """"left"|"right""""                           => OutParamEnum(compName, fieldName, Seq("left", "right"))
      case """"top"|"bottom""""                           => OutParamEnum(compName, fieldName, Seq("top", "bottom"))
      case "number (0-5)"                                 => OutParamEnum(compName, fieldName, (0 to 5).map(_.toString))
      case enum if enum.startsWith("oneOf")               => OutParamEnum(compName, fieldName, split(1, enum))
      case enum if enum.startsWith("one of")              => OutParamEnum(compName, fieldName, split(2, enum))
      case "func" | "function"                            =>
        (compName, fieldName) match {
          case ("MuiDatePicker", "DateTimeFormat")    => OutParamClass("js.Date => String")
          case ("MuiDatePicker", "formatDate")        => OutParamClass("js.Date => String")
          case ("MuiDatePicker", "shouldDisableDate") => OutParamClass("js.Date => Boolean")
          case ("EnhancedSwitch", "onSwitch")         => OutParamClass("(ReactEvent, Boolean) => Callback")
          case ("MuiTextField", "onEnterKeyDown")     => OutParamClass("ReactEventI => Callback")
        }
      case "buttonClicked" => OutParamClass("ReactEvent")
      case "isKeyboardFocused" => OutParamClass("Boolean")
      case "this" => OutParamClass("js.Any")
      case "checked" => OutParamClass("Boolean")
      case "selected" => OutParamClass("Boolean")
      case "toggled" => OutParamClass("Boolean")
      case "selectedRows" => OutParamClass("String | js.Array[Int]")
      case "rowNumber" => OutParamClass("Int")
      case "columnId" => OutParamClass("Int")
      case "tab" => OutParamClass("js.Any")
      case "ReactClass" => OutParamClass("js.Any")
      case "e" => OutParamClass("ReactEvent")
    }
  }
}