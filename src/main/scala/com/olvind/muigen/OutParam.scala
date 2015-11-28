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

        /* todo: double defined */
      case ("MuiDatePicker",       "DateTimeFormat",       "func")                => OutParamClass("js.Function")
      case ("MuiDatePicker",       "formatDate",           "func")                => OutParamClass("js.Date => String")
      case ("MuiDatePicker",       "shouldDisableDate",    "func")                => OutParamClass("js.Date => Boolean")
      case ("MuiDatePicker",       "wordings",             "object")              => OutParamClass("Wordings")
      case ("MuiTabs",             "tabTemplate",          "ReactClass")          => OutParamClass("js.Any")

      case ("MuiDialog",           "actions",              "array")               => OutParamClass("js.Array[ReactElement]")
      case ("MuiDropDownMenu",     "menuItems",            "array")               => OutParamClass("js.Array[MuiDropDownMenuItem]")
      case ("MuiGridTile",         "rootClass",            "ReactComponent")      => OutParamClass("js.Any")
      case ("MuiIconMenu",         "iconButtonElement",    "element: IconButton") => OutParamClass("ReactElement")
      case ("MuiIconMenu",         "value",                "array")               => OutParamClass("js.Array[ReactElement]")
      case ("MuiIconMenu",         "anchorOrigin",         "origin object")       => OutParamClass("Origin")
      case ("MuiIconMenu",         "targetOrigin",         "origin object")       => OutParamClass("Origin")
      case ("MuiLeftNav",          "menuItems",            "array")               => OutParamClass("js.Array[MuiMenuItemJson]")
      case ("MuiListItem",         "nestedItems",          "Array of elements")   => OutParamClass("js.Array[ReactElement]")
      case ("MuiMenu",             "value",                "array")               => OutParamClass("js.Array[String]")
      case ("MuiPopover",          _,                      "origin object")       => OutParamClass("Origin")
      case ("MuiPopover",          "anchorEl",             "object")              => OutParamClass("js.Any")
      case ("MuiSelectField",      "selectFieldRoot",      "object")              => OutParamClass("CssProperties")
      case ("MuiSelectField",      "menuItems",            "array")               => OutParamClass("js.Array[MuiSelectItem]")
      case (_, _, e) if e.contains("oneOfType")                                   => OutParamClass((split(1, e) map mapType(compName, fieldName) map (_.typeName)).mkString(" | "))
      case (_, _, "string") if is("color")                                        => OutParamClass("MuiColor")
      case (c, "label", _) if c.contains("Button")                  => OutParamClass("String")

      case (_, _, "valueLink")                                   => OutParamClass("js.Any")
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
    }
  }
}