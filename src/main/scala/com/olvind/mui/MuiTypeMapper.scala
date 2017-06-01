package com.olvind
package mui

object MuiTypeMapper extends TypeMapper {
  val typeT   = Normal("T").generic("T")
  val typeTJs = Normal("T").genericJs("T")

  def apply(compName: CompName, fieldName: PropName, typeString: String): Type = {
    def is(s: String) =
      fieldName.value.toLowerCase contains s.toLowerCase
    def split(drop: Int, s: String) =
      s.split("[\'\"\\(\\)\\[\\],\\s]").map(_.trim).filterNot(_.isEmpty).drop(drop)

    (compName.value, fieldName.value, typeString) match {
      // i dont have patience to do this properly (GridList)
      case (_,                     "cellHeight",           _       ) => Normal("Int")

      case (_, _, e) if e.contains("oneOfType") =>
        Normal(split(1, e) map (t => apply(compName, fieldName, t)) map (_.name) mkString " | ")
      case (_, _, enum) if enum.startsWith("oneOf") =>
        Enum(compName, split(1, enum))

      /* Double => Int */
      case (_,                     "autoHideDuration",     "number") => Normal("Int")
      case (_,                     "cols",                 "number") => Normal("Int")
      case (_,                     "columnNumber",         "number") => Normal("Int")
      case (_,                     "columnId",             "number") => Normal("Int")
      case (_,                     "initialSelectedIndex", "number") => Normal("Int")
      case (_,                     "left",                 "number") => Normal("Int")
      case (_,                     "maxHeight",            "number") => Normal("Int")
      case (_,                     "nestedLevel",          "number") => Normal("Int")
      case (_,                     "padding",              "number") => Normal("Int")
      case (_,                     "rowNumber",            "number") => Normal("Int")
      case (_,                     "rows",                 "number") => Normal("Int")
      case (_,                     "rowsMax",              "number") => Normal("Int")
      case (_,                     "selectedIndex",        "number") => Normal("Int")
      case ("Avatar",              "size",                 "number") => Normal("Int")
      case ("RefreshIndicator",    "size",                 "number") => Normal("Int")
      case (_,                     "top",                  "number") => Normal("Int")
      case (_,                     "touchTapCloseDelay",   "number") => Normal("Int")
      case (_, _, e) if e.toLowerCase.contains("index")              => Normal("Int")

      /* specific */
      case ("AutoComplete",     "dataSource",           "array")                => Normal("js.Array[String]")
      case ("AutoComplete",     "menuProps",            "object")               => Normal("js.Object")
      case ("DatePicker",       "value",                _)                      => Normal("js.Date")
      case ("DatePicker",       "defaultDate",          "object")               => Normal("js.Date")
      case ("DatePicker",       "maxDate",              "object")               => Normal("js.Date")
      case ("DatePicker",       "minDate",              "object")               => Normal("js.Date")
      case ("DatePicker",       "wordings",             "object")               => Normal("js.Object")
      case ("DatePicker",       "initialDate",          "object")               => Normal("js.Date")
      case ("Dialog",           "width",                "any")                  => Normal("Int")
      case ("DropDownMenu",     "value",                "any")                  => typeT
      case ("EnhancedSwitch",   "value",                "any")                  => typeT
      case ("RadioButton",      "value",                "any")                  => typeT
      case ("Tab",              "index",                "any")                  => Normal("js.Any")
      case ("ListItem",         "nestedItems",          "arrayOf(element)")     => Normal("js.Array[ReactElement]")
      case ("Menu",             "value",                "any")                  => Normal("T | js.Array[T]").generic("T")
      case ("MenuItem",         "value",                "any")                  => typeT
      case ("SelectField",      "selectFieldRoot",      "object")               => Normal("CssProperties")
      case ("SelectField",      "value",                "any")                  => typeT
      case ("Slider",           "defaultValue",         "valueInRangePropType") => Normal("Double")
      case ("Slider",           "max",                  "minMaxPropType")       => Normal("Double")
      case ("Slider",           "min",                  "minMaxPropType")       => Normal("Double")
      case ("Slider",           "value",                "valueInRangePropType") => Normal("Double")
      case ("Step",             "controlButtonsGroup",  "arrayOf(node)")        => Normal("js.Array[ReactNode]")
      case ("Step",             "actions",              "arrayOf(node)")        => Normal("js.Array[ReactNode]")
      case ("Tab",              "value",                "any")                  => typeTJs
      case ("Tabs",             "value",                "any")                  => typeTJs
      case ("TextField",        "value",                "any")                  => Normal("String")
      case ("TextField",        "defaultValue",         "any")                  => Normal("String")
      case ("TimePicker",       "defaultTime",          "object")               => Normal("js.Date")
      case ("TimePicker",       "value",                "object")               => Normal("js.Date")

      /* TODO: dubious */
      case ("AutoComplete",     "dataSourceConfig",            "object")        => Normal("js.Object")
      case ("EnhancedTextarea", "defaultValue",                "any")           => Normal("js.Any")
      case ("GridTile",         "rootClass",                   "object")        => Normal("js.Any")
      case ("Popover",          "anchorEl",                    "object")        => Normal("js.Any")
      case ("Stepper",          "createIcon",                  "func")          => Normal("js.Function")
      case ("Stepper",          "updateAvatarBackgroundColor", "func")          => Normal("js.Function")

      /* mui general */
      case (_, _, "string") if is("color")    => Normal("MuiColor")
      case (_, _, "object") if is("style")    => Normal("CssProperties")
      case (_, _, "object") if is("muiTheme") => Normal("MuiTheme")
      case (_, "label", "validateLabel")      => Normal("String")
      case (_, "zDepth", _)                   => Normal("ZDepth")
      case (_, _, "Mui.origin")               => Normal("Origin")
      case (_, _, "Mui.cornersAndCenter")     => Normal("CornersAndCenter")
      case (_, _, "Mui.corners")              => Normal("Corners")
      case (_, _, "Mui.stringOrNumber")       => Normal("String | Double")

      /* general */
      case (_, "valueLink", "object")          => Normal("js.Any")
      case (_, _, "string")                    => Normal("String")
      case (_, _, "bool")                      => Normal("Boolean")
      case (_, _, "element")                   => Normal("ReactElement")
      case (_, _, "node")                      => Normal("ReactNode")
      case (_, _, "number")                    => Normal("Double")
      case (_, "children", "arrayOf(element)") => Normal("js.Array[ReactElement]")

      case ("AutoComplete"    , "popoverProps",    "object")        => Normal("js.Any")
      case ("RadioButtonGroup", "defaultSelected", "any")           => Normal("js.Any")
      case ("RadioButtonGroup", "valueSelected",   "any")           => Normal("js.Any")
      case ("Stepper"         , "children",        "arrayOf(node)") => Normal("js.Any")

      case (_, _, "func") =>
        Normal(MuiTypeMapperFunction(compName, fieldName))

      case other =>
        println(other)
        Normal("js.Any")
    }
  }
}
