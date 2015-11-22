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
  final def intro(fs: FieldStats) = {
    val fixedName = if (name == "type") "`type`" else name
    val deprecated = if (jsonOpt.toString.toLowerCase.contains("deprecated")) "\t@deprecated\n" else ""
    s"$comment$deprecated\t${padTo(fixedName + ": ")(fs.maxFieldNameLen + 2)}"
  }
}

final case class  ReqField(name: String, baseType: OutParam, jsonOpt: Option[JsonField]) extends OutField{
  override val typeName = baseType.typeName
  override def toString(fs: FieldStats): String =
    intro(fs) + typeName
}

final case class  OptField(name: String, baseType: OutParam, jsonOpt: Option[JsonField]) extends OutField{
  override val typeName = s"js.UndefOr[${baseType.typeName}]"
  override def toString(fs: FieldStats): String =
    intro(fs) + padTo(typeName)(fs.maxTypeNameLen + 2) + " = js.undefined"
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

  def mapFunction(compName: String, name: String, s: String) = {
    val ret: OutParamClass = s match {
      case "function()"                         => OutParamClass("Callback")
      case Params(params)                       =>
        val mappedParams = params.split(",").map(_.trim).filterNot(_.isEmpty).map(OutParam.mapType(compName, name))
        val paramPart = if (mappedParams.length == 1) mappedParams.head.typeName else mappedParams.map(_.typeName).mkString("(", ", ", ")")
        OutParamClass(s"$paramPart => Callback")
      case f if f.contains("(e)")               => OutParamClass(s"${OutParam.mapType(compName, name)("event").typeName} => Callback")
    }
    println(s"""("$compName", "$name") => "${ret.typeName}"""")
    ret
  }

  def mapType(compName: String, fieldName: String)(t: String): OutParam = {
    def is(s: String) =
      fieldName.toLowerCase contains s.toLowerCase
    def split(drop: Int, s: String) =
      s.split("[\"\\(\\)\\[\\],\\s]").map(_.trim).filterNot(_.isEmpty).drop(drop)

    (compName, fieldName, t) match {
        /* Double => Int */
      case (_,                     "autoHideDuration",     "number")              => OutParamClass("Int")
      case (_,                     "cellHeight",           "number")              => OutParamClass("Int")
      case (_,                     "cols",                 "number")              => OutParamClass("Int")
      case (_,                     "columnNumber",         "number")              => OutParamClass("Int")
      case (_,                     "columnId",             "number")              => OutParamClass("Int")
      case (_,                     "initialSelectedIndex", "number")              => OutParamClass("Int")
      case (_,                     "left",                 "number")              => OutParamClass("Int")
      case (_,                     "maxHeight",            "number")              => OutParamClass("Int")
      case (_,                     "nestedLevel",          "number")              => OutParamClass("Int")
      case (_,                     "padding",              "number")              => OutParamClass("Int")
      case (_,                     "rowNumber",            "number")              => OutParamClass("Int")
      case (_,                     "rows",                 "number")              => OutParamClass("Int")
      case (_,                     "rowsMax",              "number")              => OutParamClass("Int")
      case (_,                     "selectedIndex",        "number")              => OutParamClass("Int")
      case (_,                     "size",                 "number")              => OutParamClass("Int")
      case (_,                     "top",                  "number")              => OutParamClass("Int")
      case (_,                     "touchTapCloseDelay",   "number")              => OutParamClass("Int")
      case (_,                     "zDepth",               _       )              => OutParamClass("MuiZDepth")

      case (_, _, e) if e.toLowerCase.contains("index")          => OutParamClass("Int")
//      case (_, _, e) if e.toLowerCase.contains("number")         => OutParamClass("Int")

      case ("AppBar",              "onTitleTouchTap",      "func")                => OutParamClass("js.Function")
      case ("MuiDatePicker",       "DateTimeFormat",       "func")                => OutParamClass("js.Function")
      case ("MuiDatePicker",       "formatDate",           "function")            => OutParamClass("js.Date => String")
      case ("MuiDatePicker",       "shouldDisableDate",    "function")            => OutParamClass("js.Date => Boolean")
      case ("MuiDatePicker",       "wordings",             "object")              => OutParamClass("Wordings")
      case ("MuiDialog",           "actions",              "array")               => OutParamClass("js.Array[ReactElement]")
      case ("MuiDialog",           "onRequestClose",       "buttonClicked")       => OutParamClass("Boolean")
      case ("MuiDropDownMenu",     "menuItemStyle",        "array")               => OutParamClass("CssProperties")
      case ("MuiDropDownMenu",     "menuItems",            "array")               => OutParamClass("js.Array[MuiDropDownMenuItem]")
      case ("MuiDropDownMenu",     "onChange",             "menuItem")            => OutParamClass("js.Any") //todo MuiDropDownMenuItem
      case ("MuiIconMenu",         "iconButtonElement",    "element: IconButton") => OutParamClass("ReactElement")
      case ("MuiIconMenu",         "value",                "array")               => OutParamClass("js.Array[ReactElement]")
      case ("MuiIconMenu",         "onItemTouchTap",       "item")                => OutParamClass("ReactElement")
      case ("MuiIconMenu",         "onChange",             "item")                => OutParamClass("ReactElement")
      case ("MuiIconMenu",         "onChange",             "value")               => OutParamClass("js.UndefOr[String]")
      case ("MuiLeftNav",          "menuItems",            "array")               => OutParamClass("js.Array[MuiMenuItemJson]")
      case ("MuiLeftNav",          "onChange",             "menuItem")            => OutParamClass("js.Any") //todo
      case ("MuiListItem",         "nestedItems",          "Array of elements")   => OutParamClass("js.Array[ReactElement]")
      case ("MuiListItem",         "onKeyboardFocus",      "isKeyboardFocused")   => OutParamClass("Boolean")
      case ("MuiListItem",         "onNestedListToggle",   "this")                => OutParamClass("js.Any") //todo wtf
//      case ("MuiMenu",             "menuItems",            "array")               => OutParamClass("js.Array[MuiMenuItemJson]")
      case ("MuiMenu",             "value",                "array")               => OutParamClass("js.Array[String]")
      case ("MuiMenu",             "onItemTouchTap",       "item")                => OutParamClass("ReactElement")
      case ("MuiMenu",             "onChange",             "value")               => OutParamClass("String | js.Array[String]")
      case ("MuiPopover",          _,                      "origin object")       => OutParamClass("Origin")
      case ("MuiPopover",          "onRequestClose",       "func")                => OutParamClass("Origin")
      case ("MuiPopover",          "anchorEl",             "object")              => OutParamClass("js.Any")
      case ("MuiRadioButtonGroup", "onChange",             "selected")            => OutParamClass("String")
      case ("MuiSlider",           "onChange",             "value")               => OutParamClass("Double")
      case ("MuiSelectField",      "selectFieldRoot",      "object")              => OutParamClass("CssProperties")
      case ("MuiSelectField",      "onChange",             "item")                => OutParamClass("js.Any")
      case ("MuiSelectField",      "menuItems",            "array")               => OutParamClass("js.Array[MuiSelectItem]")
      case ("MuiTable",            "onRowSelection",       "selectedRows")        => OutParamClass("String | js.Array[Int]")
      case ("MuiTable",            _,                      "rowNumber")           => OutParamClass("Int")
      case ("MuiTable",            _,                      "columnId")            => OutParamClass("Int")
      case ("MuiTab",              "onActive",             "tab")                 => OutParamClass("ReactElement")
      case ("MuiTabs",             "tabTemplate",          "ReactClass")          => OutParamClass("js.Any")
      case ("MuiTabs",             "onChange",             "value")               => OutParamClass("String")
      case ("MuiTabs",             "onChange",             "e")                   => OutParamClass("ReactEvent")
      case ("MuiTabs",             "onChange",             "tab")                 => OutParamClass("ReactElement")
      case ("MuiTextField",        "onEnterKeyDown",       "func")                => OutParamClass("ReactEvent => Callback")
      case (_, _, e) if e.contains("oneOfType")                  => OutParamClass((split(1, e) map mapType(compName, fieldName) map (_.typeName)).mkString(" | "))
      case (_, _, "string") if is("color")                       => OutParamClass("MuiColor")

      case (_, f, "event") if is("touch")                        => OutParamClass("ReactTouchEvent")
      case (_, f, "event") if is("key")                          => OutParamClass("ReactKeyboardEvent")
      case (_, f, "event") if is("mouse")                        => OutParamClass("ReactMouseEvent")
      case (_, f, "event") if is("wheel")                        => OutParamClass("ReactWheelEvent")
      case (_, f, "event") if is("drag")                         => OutParamClass("ReactDragEvent")
      case (_, _, "event")                                       => OutParamClass("ReactEvent")

      case (_, _, "time")                                        => OutParamClass("js.Date")
      case (_, _, "date")                                        => OutParamClass("js.Date")
      case (_, _, "instanceOf(Date)")                            => OutParamClass("js.Date")
      case (_, _, "string")                                      => OutParamClass("String")
      case (_, f, "object") if is("style")                       => OutParamClass("CssProperties")
      case (_, _, "bool")                                        => OutParamClass("Boolean")
      case (_, _, "null")                                        => OutParamClass("js.UndefOr[Nothing]")
      case (_, _, "element")                                     => OutParamClass("ReactElement")
      case (_, _, "node")                                        => OutParamClass("ReactNode")
      case (_, _, "number")                                      => OutParamClass("Double")
      case (_, _, "integer")                                     => OutParamClass("Int")
      case (_, _, enum) if enum.startsWith("oneOf")              => OutParamEnum(compName, fieldName, split(1, enum))
      case (_, _, "checked")                                     => OutParamClass("Boolean")
      //      case (_, _, "selected")                                    => OutParamClass("Boolean")
      case (_, _, "toggled")                                     => OutParamClass("Boolean")
      case (_, _, "string|ReactComponent")                       => OutParamClass("String | ReactElement")

    }
  }
}