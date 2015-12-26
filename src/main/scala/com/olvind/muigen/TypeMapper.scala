package com.olvind
package muigen

object TypeMapper {
  def apply(compName: CompName, fieldName: PropName, typeString: String): PropType = {
    def is(s: String) =
      fieldName.value.toLowerCase contains s.toLowerCase
    def split(drop: Int, s: String) =
      s.split("[\'\"\\(\\)\\[\\],\\s]").map(_.trim).filterNot(_.isEmpty).drop(drop)

    (compName.value, fieldName.value, typeString) match {
      /* Double => Int */
      case (_,                     "autoHideDuration",     "number")               => PropTypeClass("Int")
      case (_,                     "cellHeight",           "number")               => PropTypeClass("Int")
      case (_,                     "cols",                 "number")               => PropTypeClass("Int")
      case (_,                     "columnNumber",         "number")               => PropTypeClass("Int")
      case (_,                     "columnId",             "number")               => PropTypeClass("Int")
      case (_,                     "initialSelectedIndex", "number")               => PropTypeClass("Int")
      case (_,                     "left",                 "number")               => PropTypeClass("Int")
      case (_,                     "maxHeight",            "number")               => PropTypeClass("Int")
      case (_,                     "nestedLevel",          "number")               => PropTypeClass("Int")
      case (_,                     "padding",              "number")               => PropTypeClass("Int")
      case (_,                     "rowNumber",            "number")               => PropTypeClass("Int")
      case (_,                     "rows",                 "number")               => PropTypeClass("Int")
      case (_,                     "rowsMax",              "number")               => PropTypeClass("Int")
      case (_,                     "selectedIndex",        "number")               => PropTypeClass("Int")
      case (_,                     "size",                 "number")               => PropTypeClass("Int")
      case (_,                     "top",                  "number")               => PropTypeClass("Int")
      case (_,                     "touchTapCloseDelay",   "number")               => PropTypeClass("Int")
      case (_,                     "zDepth",               _       )               => PropTypeClass("ZDepth")
      case (_, _, e) if e.toLowerCase.contains("index")                            => PropTypeClass("Int")

      case ("DatePicker",       "value",                "any")                  => PropTypeClass("js.Date")
      case ("DatePicker",       "defaultDate",          "object")               => PropTypeClass("js.Date")
      case ("DatePicker",       "maxDate",              "object")               => PropTypeClass("js.Date")
      case ("DatePicker",       "minDate",              "object")               => PropTypeClass("js.Date")
      case ("DatePicker",       "minDate",              "object")               => PropTypeClass("js.Date")
      case ("DatePicker",       "wordings",             "object")               => PropTypeClass("Wordings")
      case ("DatePicker",       "initialDate",          "object")               => PropTypeClass("js.Date")
      case ("Dialog",           "width",                "any")                  => PropTypeClass("Int")
      case ("DropDownMenu",     "menuItems",            "array")                => PropTypeClass("js.Array[MuiDropDownMenuItem]")
      case ("DropDownIcon",     "menuItems",            "array")                => PropTypeClass("js.Array[MuiMenuItemJson]")
      case ("LeftNav",          "menuItems",            "array")                => PropTypeClass("js.Array[MuiMenuItemJson]")
      case ("ListItem",         "nestedItems",          "arrayOf(element)")     => PropTypeClass("js.Array[ReactElement]")
      case ("Menu",             "value",                "any")                  => PropTypeClass("String | js.Array[String]")
      case ("SelectField",      "menuItems",            "array")                => PropTypeClass("js.Array[MuiSelectItem]")
      case ("Slider",           "defaultValue",         "valueInRangePropType") => PropTypeClass("Double")
      case ("Slider",           "max",                  "minMaxPropType")       => PropTypeClass("Double")
      case ("Slider",           "min",                  "minMaxPropType")       => PropTypeClass("Double")
      case ("Slider",           "value",                "valueInRangePropType") => PropTypeClass("Double")
      case ("TextField",        "value",                "any")                  => PropTypeClass("String")
      case ("TextField",        "defaultValue",         "any")                  => PropTypeClass("String")
      case ("TimePicker",       "defaultTime",          "object")               => PropTypeClass("js.Date")

      case (_, _, e) if e.contains("oneOfType")                                   => PropTypeClass((split(1, e) map (t => apply(compName, fieldName, t)) map (_.typeName)).mkString(" | "))
      case (_, _, "string") if is("color")                                        => PropTypeClass("MuiColor")
      case (c, "label", _) if c.contains("Button")               => PropTypeClass("String")

      case (_, _, "object") if is("style")                       => PropTypeClass("CssProperties")
      case (_, _, "valueLink")                                   => PropTypeClass("js.Any")
      case (_, "valueLink", _)                                   => PropTypeClass("js.Any")
      case (_, _, "time")                                        => PropTypeClass("js.Date")
      case (_, _, "date")                                        => PropTypeClass("js.Date")
      case (_, _, "instanceOf(Date)")                            => PropTypeClass("js.Date")
      case (_, _, "string")                                      => PropTypeClass("String")
      case (_, _, "bool")                                        => PropTypeClass("Boolean")
      case (_, _, "null")                                        => PropTypeClass("js.UndefOr[Nothing]")
      case (_, _, "element")                                     => PropTypeClass("ReactElement")
      case (_, _, "node")                                        => PropTypeClass("ReactNode")
      case (_, _, "number")                                      => PropTypeClass("Double")
      case (_, _, "integer")                                     => PropTypeClass("Int")
      case (_, _, enum) if enum.startsWith("oneOf")              => PropTypeEnum(compName, fieldName, split(1, enum))
      case (_, _, "Mui.origin")                                  => PropTypeClass("Origin")
      case (_, _, "Mui.cornersAndCenter")                        => PropTypeClass("CornersAndCenter")
      case (_, _, "Mui.corners")                                 => PropTypeClass("Corners")
      case (_, _, "Mui.stringOrNumber")                          => PropTypeClass("String | Double")
      case (_, _, "func")                                        => PropTypeClass(TypeMapperFunction(compName, fieldName))

      /* dubious */
      case ("AutoComplete", "menuProps", "object") => PropTypeClass("js.Any")
      case ("GridTile", "rootClass", "object") => PropTypeClass("js.Any")
      case ("Popover", "anchorEl", "object") => PropTypeClass("js.Any")
      case ("DropDownMenu", "value", "any") => PropTypeClass("js.Any")
      case ("MenuItem", "value", "any") => PropTypeClass("js.Any")
      case ("SelectField", "value", "any") => PropTypeClass("js.Any")
      case ("SelectField", "selectFieldRoot", "object") => PropTypeClass("js.Any")
      case ("Tab", "value", "any") => PropTypeClass("js.Any")
      case ("Tabs", "value", "any") => PropTypeClass("js.Any")

      case ("AutoComplete", "dataSource", "array") => PropTypeClass("js.Array[js.Any]")

    }
  }
}
