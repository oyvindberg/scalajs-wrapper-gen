package com.olvind
package muigen

object TypeMapper {
  def apply(compName: CompName, fieldName: PropName)(t: String): OutParam = {
    def is(s: String) =
      fieldName.value.toLowerCase contains s.toLowerCase
    def split(drop: Int, s: String) =
      s.split("[\'\"\\(\\)\\[\\],\\s]").map(_.trim).filterNot(_.isEmpty).drop(drop)

    (compName.value, fieldName.value, t) match {
      /* Double => Int */
      case (_,                     "autoHideDuration",     "number")               => OutParamClass("Int")
      case (_,                     "cellHeight",           "number")               => OutParamClass("Int")
      case (_,                     "cols",                 "number")               => OutParamClass("Int")
      case (_,                     "columnNumber",         "number")               => OutParamClass("Int")
      case (_,                     "columnId",             "number")               => OutParamClass("Int")
      case (_,                     "initialSelectedIndex", "number")               => OutParamClass("Int")
      case (_,                     "left",                 "number")               => OutParamClass("Int")
      case (_,                     "maxHeight",            "number")               => OutParamClass("Int")
      case (_,                     "nestedLevel",          "number")               => OutParamClass("Int")
      case (_,                     "padding",              "number")               => OutParamClass("Int")
      case (_,                     "rowNumber",            "number")               => OutParamClass("Int")
      case (_,                     "rows",                 "number")               => OutParamClass("Int")
      case (_,                     "rowsMax",              "number")               => OutParamClass("Int")
      case (_,                     "selectedIndex",        "number")               => OutParamClass("Int")
      case (_,                     "size",                 "number")               => OutParamClass("Int")
      case (_,                     "top",                  "number")               => OutParamClass("Int")
      case (_,                     "touchTapCloseDelay",   "number")               => OutParamClass("Int")
      case (_,                     "zDepth",               _       )               => OutParamClass("MuiZDepth")
      case (_, _, e) if e.toLowerCase.contains("index")                            => OutParamClass("Int")

      case ("MuiDatePicker",       "value",                "any")                  => OutParamClass("js.Date")
      case ("MuiDatePicker",       "defaultDate",          "object")               => OutParamClass("js.Date")
      case ("MuiDatePicker",       "maxDate",              "object")               => OutParamClass("js.Date")
      case ("MuiDatePicker",       "minDate",              "object")               => OutParamClass("js.Date")
      case ("MuiDatePicker",       "minDate",              "object")               => OutParamClass("js.Date")
      case ("MuiDatePicker",       "wordings",             "object")               => OutParamClass("Wordings")
      case ("MuiDialog",           "width",                "any")                  => OutParamClass("Int")
      case ("MuiSlider",           "defaultValue",         "valueInRangePropType") => OutParamClass("Double")
      case ("MuiSlider",           "max",                  "minMaxPropType")       => OutParamClass("Double")
      case ("MuiSlider",           "min",                  "minMaxPropType")       => OutParamClass("Double")
      case ("MuiSlider",           "value",                "valueInRangePropType") => OutParamClass("Double")
      case ("MuiTextField",        "value",                "any")                  => OutParamClass("String")
      case ("MuiTextField",        "defaultValue",         "any")                  => OutParamClass("String")
      case ("MuiTimePicker",       "defaultTime",          "object")               => OutParamClass("js.Date")

      case (_, _, e) if e.contains("oneOfType")                                   => OutParamClass((split(1, e) map (t => apply(compName, fieldName)(t)) map (_.typeName)).mkString(" | "))
      case (_, _, "string") if is("color")                                        => OutParamClass("MuiColor")
      case (c, "label", _) if c.contains("Button")               => OutParamClass("String")

      case (_, _, "object") if is("style")                       => OutParamClass("CssProperties")
      case (_, _, "valueLink")                                   => OutParamClass("js.Any")
      case (_, "valueLink", _)                                   => OutParamClass("js.Any")
      case (_, _, "time")                                        => OutParamClass("js.Date")
      case (_, _, "date")                                        => OutParamClass("js.Date")
      case (_, _, "instanceOf(Date)")                            => OutParamClass("js.Date")
      case (_, _, "string")                                      => OutParamClass("String")
      case (_, _, "bool")                                        => OutParamClass("Boolean")
      case (_, _, "null")                                        => OutParamClass("js.UndefOr[Nothing]")
      case (_, _, "element")                                     => OutParamClass("ReactElement")
      case (_, _, "node")                                        => OutParamClass("ReactNode")
      case (_, _, "number")                                      => OutParamClass("Double")
      case (_, _, "integer")                                     => OutParamClass("Int")
      case (_, _, "array")                                       => OutParamClass("js.Array[js.Any]")
      case (_, _, enum) if enum.startsWith("oneOf")              => OutParamEnum(compName, fieldName, split(1, enum))
      case (_, _, "Mui.origin")                                  => OutParamClass("MuiOrigin")
      case (_, _, "Mui.cornersAndCenter")                        => OutParamClass("MuiCornersAndCenter")
      case (_, _, "Mui.corners")                                 => OutParamClass("MuiCorners")
      case (_, _, "Mui.stringOrNumber")                          => OutParamClass("String | Double")
      case (_, _, "func")                                        => OutParamClass(TypeMapperFunction(compName, fieldName))

      /* dubious */
      case ("MuiAutoComplete", "menuProps", "object") => OutParamClass("js.Any")
      case ("MuiGridTile", "rootClass", "object") => OutParamClass("js.Any")
      case ("MuiPopover", "anchorEl", "object") => OutParamClass("js.Any")
      case ("MuiDropDownMenu", "value", "any") => OutParamClass("js.Any")
      case ("MuiListItem", "nestedItems", "arrayOf(element)") => OutParamClass("js.Array[js.Any]")
      case ("MuiMenu", "value", "any") => OutParamClass("js.Any")
      case ("MuiMenuItem", "value", "any") => OutParamClass("js.Any")
      case ("MuiSelectField", "value", "any") => OutParamClass("js.Any")
      case ("MuiSelectField", "selectFieldRoot", "object") => OutParamClass("js.Any")
      case ("MuiTab", "value", "any") => OutParamClass("js.Any")
      case ("MuiTabs", "value", "any") => OutParamClass("js.Any")

    }
  }
}
