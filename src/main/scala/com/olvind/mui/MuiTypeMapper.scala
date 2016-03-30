package com.olvind
package mui

object MuiTypeMapper extends TypeMapper {
  import PropType.{Enum, Type}
  
  def apply(compName: CompName, fieldName: PropName, typeString: String): PropType = {
    def is(s: String) =
      fieldName.value.toLowerCase contains s.toLowerCase
    def split(drop: Int, s: String) =
      s.split("[\'\"\\(\\)\\[\\],\\s]").map(_.trim).filterNot(_.isEmpty).drop(drop)

    (compName.value, fieldName.value, typeString) match {
      case (_, _, e) if e.contains("oneOfType") =>
        Type( split(1, e) map (t => apply(compName, fieldName, t)) map (_.typeName) mkString " | ")
      case (_, _, enum) if enum.startsWith("oneOf") =>
        Enum(compName, fieldName, split(1, enum))

      /* Double => Int */
      case (_,                     "autoHideDuration",     "number") => Type("Int")
      case (_,                     "cellHeight",           "number") => Type("Int")
      case (_,                     "cols",                 "number") => Type("Int")
      case (_,                     "columnNumber",         "number") => Type("Int")
      case (_,                     "columnId",             "number") => Type("Int")
      case (_,                     "initialSelectedIndex", "number") => Type("Int")
      case (_,                     "left",                 "number") => Type("Int")
      case (_,                     "maxHeight",            "number") => Type("Int")
      case (_,                     "nestedLevel",          "number") => Type("Int")
      case (_,                     "padding",              "number") => Type("Int")
      case (_,                     "rowNumber",            "number") => Type("Int")
      case (_,                     "rows",                 "number") => Type("Int")
      case (_,                     "rowsMax",              "number") => Type("Int")
      case (_,                     "selectedIndex",        "number") => Type("Int")
      case ("Avatar",              "size",                 "number") => Type("Int")
      case ("RefreshIndicator",    "size",                 "number") => Type("Int")
      case (_,                     "top",                  "number") => Type("Int")
      case (_,                     "touchTapCloseDelay",   "number") => Type("Int")
      case (_, _, e) if e.toLowerCase.contains("index")              => Type("Int")

      /* specific */
      case ("AutoComplete", "dataSource",      "array")  => Type("js.Array[Value]")
      case ("AutoComplete", "menuProps",       "object") => Type("js.Object")
      case ("AutoComplete", "searchText",       _) => Type("SearchText")
      case ("DatePicker",       "value",                "any")                  => Type("js.Date")
      case ("DatePicker",       "defaultDate",          "object")               => Type("js.Date")
      case ("DatePicker",       "maxDate",              "object")               => Type("js.Date")
      case ("DatePicker",       "minDate",              "object")               => Type("js.Date")
      case ("DatePicker",       "wordings",             "object")               => Type("Wordings")
      case ("DatePicker",       "initialDate",          "object")               => Type("js.Date")
      case ("Dialog",           "width",                "any")                  => Type("Int")
      case ("DropDownMenu",     "menuItems",            "array")                => Type("js.Array[MuiDropDownMenuItem]")
      case ("DropDownIcon",     "menuItems",            "array")                => Type("js.Array[MuiMenuItemJson]")
      case ("Drawer",           "menuItems",            "array")                => Type("js.Array[MuiMenuItemJson]")
      case ("ListItem",         "nestedItems",          "arrayOf(element)")     => Type("js.Array[ReactElement]")
      case ("Menu",             "value",                "any")                  => Type("String | js.Array[String]")
      case ("SelectField",      "menuItems",            "array")                => Type("js.Array[MuiSelectItem]")
      case ("Slider",           "defaultValue",         "valueInRangePropType") => Type("Double")
      case ("Slider",           "max",                  "minMaxPropType")       => Type("Double")
      case ("Slider",           "min",                  "minMaxPropType")       => Type("Double")
      case ("Slider",           "value",                "valueInRangePropType") => Type("Double")
      case ("Step",             "controlButtonsGroup",  "arrayOf(node)")        => Type("js.Array[ReactNode]")
      case ("Step",             "actions",              "arrayOf(node)")        => Type("js.Array[ReactNode]")
      case ("TextField",        "value",                "any")                  => Type("String")
      case ("TextField",        "defaultValue",         "any")                  => Type("String")
      case ("TimePicker",       "defaultTime",          "object")               => Type("js.Date")
      case ("TimePicker",       "value",                "object")               => Type("js.Date")


      /* TODO: dubious */
      case ("EnhancedTextarea", "defaultValue",                "any")           => Type("js.Any")
      case ("GridTile",         "rootClass",                   "object")        => Type("js.Any")
      case ("Popover",          "anchorEl",                    "object")        => Type("js.Any")
      case ("DropDownMenu",     "value",                       "any")           => Type("js.Any")
      case ("MenuItem",         "value",                       "any")           => Type("js.Any")
      case ("SelectField",      "value",                       "any")           => Type("js.Any")
      case ("SelectField",      "selectFieldRoot",             "object")        => Type("js.Any")
      case ("Stepper",          "createIcon",                  "func")          => Type("js.Function")
      case ("Stepper",          "updateAvatarBackgroundColor", "func")          => Type("js.Function")
      case ("Tab",              "value",                       "any")           => Type("js.Any")
      case ("Tabs",             "value",                       "any")           => Type("js.Any")

      /* mui general */
      case (_, _, "string") if is("color") => Type("MuiColor")
      case (_, _, "object") if is("style") => Type("CssProperties")
      case (_, _, "object") if is("muiTheme") => Type("MuiTheme")
      case (_, "label", "validateLabel")   => Type("String")
      case (_, "zDepth", _)                => Type("ZDepth")
      case (_, _, "Mui.origin")            => Type("Origin")
      case (_, _, "Mui.cornersAndCenter")  => Type("CornersAndCenter")
      case (_, _, "Mui.corners")           => Type("Corners")
      case (_, _, "Mui.stringOrNumber")    => Type("String | Double")

      /* general */
      case (_, "valueLink", "object")             => Type("js.Any")
      case (_, _, "string")                => Type("String")
      case (_, _, "bool")                  => Type("Boolean")
      case (_, _, "element")               => Type("ReactElement")
      case (_, _, "node")                  => Type("ReactNode")
      case (_, _, "number")                => Type("Double")

      case (_, _, "func") =>
        Type(MuiTypeMapperFunction(compName, fieldName))

    }
  }
}
