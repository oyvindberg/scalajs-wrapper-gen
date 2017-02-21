package com.olvind
package mui

object MuiTypeMapperFunction {
  def apply(compName: CompName, name: PropName): String =
    (compName.value, name.value) match {
      case ("AppBar",           "onLeftIconButtonTouchTap")  => "ReactTouchEventH => Callback"
      case ("AppBar",           "onRightIconButtonTouchTap") => "ReactTouchEventH => Callback"
      case ("AppBar",           "onTitleTouchTap")           => "ReactTouchEventH => Callback"
      case ("AutoComplete",     "onNewRequest")              => "(Value, js.UndefOr[Int], js.Array[Value]) => Callback"
      case ("AutoComplete",     "onUpdateInput")             => "(SearchText, js.Array[Value]) => Callback"
      case ("AutoComplete",     "filter")                    => "AutoCompleteFilter"
      case ("AutoComplete",     "animation")                 => "js.Function"
      case ("AutoComplete",     "onClose")                   => "Callback"
      case ("Card",             "onExpandChange")            => "Boolean => Callback"
      case ("CardExpandable",   "onExpanding")               => "Callback"
      case ("Checkbox",         "onCheck")                   => "(ReactEventH, Boolean) => Callback"
      case ("Chip",             "onKeyboardFocus")           => "(ReactKeyboardEvent, Boolean) => Callback"
      case ("Chip",             "onRequestDelete")           => "ReactEvent => Callback"
      case ("DatePicker",       "onShow")                    => "Callback"
      case ("DatePicker",       "onDismiss")                 => "Callback"
      case ("DatePicker",       "DateTimeFormat")            => "js.Function"
      case ("DatePicker",       "onChange")                  => "(js.UndefOr[Nothing], js.Date) => Callback"
      case ("DatePicker",       "shouldDisableDate")         => "js.Date => Boolean"
      case ("DatePicker",       "formatDate")                => "js.Date => String"
      case ("DatePicker",       "onAccept")                  => "js.Date => Callback"
      case ("DatePickerDialog", "onAccept")                  => "js.Date => Callback"
      case ("DatePickerDialog", "DateTimeFormat")            => "js.Function"
      case ("DatePickerDialog", "shouldDisableDate")         => "js.Date => Boolean"
      case ("Dialog",           "onRequestClose")            => "Boolean => Callback"
      case ("DialogInline",     "onRequestClose")            => "Boolean => Callback"
      case ("Drawer",           "onRequestChange")           => "(Boolean, String) => Callback"
      case ("DropDownIcon",     "onChange")                  => "(ReactEventI, Int, T) => Callback"
      case ("DropDownMenu",     "onChange")                  => "(ReactEventI, Int, T) => Callback"
      case ("DropDownMenu",     "animation")                 => "js.Function"
      case ("DropDownMenu",     "onClose")                   => "Callback"
      case ("EnhancedButton",   "onClick")                   => "ReactEventH => Callback"
      case ("EnhancedButton",   "onKeyboardFocus")           => "ReactKeyboardEventH => Callback"
      case ("EnhancedSwitch",   "onParentShouldUpdate")      => "Boolean => Callback"
      case ("EnhancedSwitch",   "onSwitch")                  => "(ReactEventI, Boolean) => Callback"
      case ("EnhancedTextarea", "onChange")                  => "ReactEventTA => Callback"
      case ("EnhancedTextarea", "onHeightChange")            => "(ReactEvent, Int)=> Callback"
      case ("FlatButton",       "onKeyboardFocus")           => "ReactKeyboardEventH => Callback"
      case ("IconButton",       "onKeyboardFocus")           => "ReactKeyboardEventH => Callback"
      case ("IconButton",       "onMouseOut")                => "ReactMouseEventH => Callback"
      case ("IconMenu",         "animation")                 => "js.Function"
      case ("IconMenu",         "onChange")                  => "(ReactEventH, js.UndefOr[T]) => Callback"
      case ("IconMenu",         "onItemTouchTap")            => "(ReactTouchEventH, ReactElement) => Callback"
      case ("IconMenu",         "onKeyboardFocus")           => "ReactKeyboardEventH => Callback"
      case ("IconMenu",         "onRequestChange")           => "(Boolean, String) => Callback"
      case ("ListItem",         "onKeyboardFocus")           => "ReactKeyboardEventH => Callback"
      case ("ListItem",         "onNestedListToggle")        => "js.Any => Callback"
      case ("Menu",             "onEscKeyDown")              => "ReactKeyboardEventH => Callback"
      case ("Menu",             "onChange")                  => "(ReactEventH, T | js.Array[T]) => Callback"
      case ("Menu",             "onMenuItemFocusChange")     => "(js.UndefOr[ReactEvent], Int) => Callback"
      case ("Menu",             "onItemTouchTap")            => "(ReactUIEventH, JsComponentM[HasValue[T], _, TopNode]) => Callback"
      case ("MenuItem",         "animation")                 => "js.Function"
      case ("MenuItem",         "onClick")                   => "ReactEventH => Callback"
      case ("Popover",          "onRequestClose")            => "Callback"
      case ("Popover",          "animation")                 => "js.Any"
      case ("RadioButton",      "onCheck")                   => "(ReactEventH, Boolean) => Callback"
      case ("RadioButtonGroup", "onChange")                  => "(ReactEventI, String) => Callback"
      case ("SelectField",      "onChange")                  => "(ReactEventI, Int, T) => Callback"
      case ("Slider",           "onChange")                  => "(ReactEventH, Double) => Callback"
      case ("Slider",           "onDragStart")               => "ReactDragEventH => Callback"
      case ("Slider",           "onDragStop")                => "ReactDragEventH => Callback"
      case ("Slider",           "onFocus")                   => "ReactFocusEventH => Callback"
      case ("Snackbar",         "onActionTouchTap")          => "ReactTouchEventH => Callback"
      case ("Snackbar",         "onRequestClose")            => "String => Callback"
      case ("StepContent",      "transition")                => "js.Any"
      case ("Stepper",          "updateCompletedStatus")     => "(Int, ReactNode) => CallbackTo[Boolean]"

      case (s, "onStepHeaderHover") if s.contains("Step") => "Int => Callback"
      case (s, "onStepHeaderTouch") if s.contains("Step") => "(Int, js.Any) => Callback"
      case (s, "updateCompletedStatusOfStep") if s.contains("Step") => "(Int, ReactNode) => Callback"

      case ("Table",             "onCellClick" | "onCellHover" | "onCellHoverExit") => "(RowId, ColumnId, ReactEvent) => Callback"
      case ("TableBody",         "onCellClick" | "onCellHover" | "onCellHoverExit") => "(ReactEvent, RowId, ColumnId) => Callback"
      case ("TableRow",          "onCellClick" | "onCellHover" | "onCellHoverExit") => "(ReactEvent, ColumnId) => Callback"
      case ("TableHeaderColumn", "onHover"   )      => "Callback"
      case ("TableHeaderColumn", "onHoverExit")     => "Callback"
      case ("TableHeaderColumn", "onClick"   )      => "(ReactEvent, ColumnId) => Callback"
      case ("TableRowColumn",    "onClick"   )      => "(ReactEvent, ColumnId) => Callback"
      case ("Table",             "onRowHover")      => "RowId => Callback"
      case ("TableBody",         "onRowHover")      => "(ReactEvent, RowId) => Callback"
      case ("TableRow",          "onRowHover")      => "ReactEvent => Callback"
      case ("TableRow",          "onRowClick")      => "(ReactEventH, RowId) => Callback"
      case ("Table",             "onRowHoverExit" ) => "RowId => Callback"
      case ("TableBody",         "onRowHoverExit" ) => "RowId => Callback"
      case ("TableRow",          "onRowHoverExit" ) => "(ReactEvent, RowId) => Callback"
      case ("Table",             "onRowSelection" ) => "String | js.Array[RowId] => Callback"
      case ("TableBody",         "onRowSelection" ) => "String | js.Array[RowId] => Callback"
      case ("TableRowColumn",    "onHover"        ) => "(ReactEventH, ColumnId) => Callback"
      case ("TableRowColumn",    "onHoverExit"    ) => "(ReactEventH, ColumnId) => Callback"
      case ("TableHeader",       "onSelectAll"    ) => "Boolean => Callback"
      case ("Tab",               "onActive")        => "ReactElement => Callback"
      case ("Tabs",              "onChange")        => "(T, ReactEventH, ReactElement) => Callback"
      case ("Tabs",              "tabTemplate")     => "js.Any"
      case ("TextField",         "onChange")        => "ReactEventI => Callback"
      case ("TextField",         "onEnterKeyDown")  => "ReactKeyboardEventI => Callback"
      case ("TextField",         "onBlur")          => "ReactEventI => Callback"
      case ("TextField",         "onFocus")         => "ReactFocusEventI => Callback"
      case ("TextField",         "onKeyDown")       => "ReactKeyboardEventI => Callback"
      case ("TimePicker",        "onChange")        => "(js.UndefOr[Nothing], js.Date) => Callback"
      case ("TimePicker",        "onShow")          => "Callback"
      case ("TimePicker",        "onDismiss")       => "Callback"
      case ("Toggle",            "onToggle")        => "(ReactEventI, Boolean) => Callback"

      case (_, "onBlur")       => "ReactFocusEventH => Callback"
      case (_, "onFocus")      => "ReactFocusEventH => Callback"
      case (_, "onKeyDown")    => "ReactKeyboardEventH => Callback"
      case (_, "onKeyUp")      => "ReactKeyboardEventH => Callback"
      case (_, "onMouseEnter") => "ReactMouseEventH => Callback"
      case (_, "onMouseLeave") => "ReactMouseEventH => Callback"
      case (_, "onMouseUp")    => "ReactMouseEventH => Callback"
      case (_, "onMouseDown")  => "ReactMouseEventH => Callback"
      case (_, "onTouchStart") => "ReactTouchEventH => Callback"
      case (_, "onTouchEnd")   => "ReactTouchEventH => Callback"
      case (_, "onTouchTap")   => "ReactTouchEventH => Callback"
    }
}
