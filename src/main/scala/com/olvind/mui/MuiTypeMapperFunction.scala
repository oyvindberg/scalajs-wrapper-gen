package com.olvind
package mui

object MuiTypeMapperFunction {
  val TouchTapEventIHandler = "TouchTapEventI => Callback"
  val TouchEventIHandler    = "ReactTouchEventI => Callback"
  val MouseEventIHandler    = "ReactMouseEventI => Callback"
  val FocusEventIhandler    = "ReactFocusEventI => Callback"
  val KeyboardEventIHandler = "ReactKeyboardEventI => Callback"
  val Callback             = "Callback"

  def apply(compName: CompName, name: PropName): String =
    (compName.value, name.value) match {
      case ("AppBar",            "onLeftIconButtonTouchTap")  => TouchTapEventIHandler
      case ("AppBar",            "onRightIconButtonTouchTap") => TouchTapEventIHandler
      case ("AppBar",            "onTitleTouchTap")           => TouchTapEventIHandler
      case ("AutoComplete",      "onNewRequest")              => "(DataItem, Int) => Callback"
      case ("AutoComplete",      "onUpdateInput")             => "(String, js.Array[DataItem]) => Callback"
      case ("AutoComplete",      "filter")                    => "(String, String, DataItem) => Boolean"
      case ("AutoComplete",      "animation")                 => "js.Function"
      case ("AutoComplete",      "onClose")                   => Callback
      case ("Card",              "onExpandChange")            => "Boolean => Callback"
      case ("CardExpandable",    "onExpanding")               => Callback
      case ("Checkbox",          "onCheck")                   => "(ReactMouseEventI, Boolean) => Callback"
      case ("Chip",              "onRequestDelete")           => TouchEventIHandler
      case ("DatePicker",        "DateTimeFormat")            => "js.Function"
      case ("DatePicker",        "formatDate")                => "js.Date => String"
      case ("DatePicker",        "onChange")                  => "(js.UndefOr[Nothing], js.Date) => Callback"
      case ("DatePicker",        "onDismiss")                 => Callback
      case ("DatePicker",        "onShow")                    => Callback
      case ("DatePicker",        "shouldDisableDate")         => "js.Date => Boolean"
      case ("DatePickerDialog",  "onAccept")                  => "js.Date => Callback"
      case ("DatePickerDialog",  "DateTimeFormat")            => "js.Function"
      case ("DatePickerDialog",  "shouldDisableDate")         => "js.Date => Boolean"
      case ("Dialog",            "onRequestClose")            => "Boolean => Callback"
      case ("DialogInline",      "onRequestClose")            => "Boolean => Callback"
      case ("Drawer",            "onRequestChange")           => "(Boolean, String) => Callback"
      case ("DropDownMenu",      "onChange")                  => "(TouchTapEventI, Int, T) => Callback"
      case ("DropDownMenu",      "animation")                 => "js.Function"
      case ("DropDownMenu",      "onClose")                   => Callback
      case ("EnhancedButton",    "onClick")                   => "ReactEventI => Callback"
      case ("EnhancedSwitch",    "onParentShouldUpdate")      => "Boolean => Callback"
      case ("EnhancedSwitch",    "onSwitch")                  => "(ReactMouseEventI, Boolean) => Callback"
      case ("EnhancedTextarea",  "onChange")                  => "ReactEventI => Callback"
      case ("EnhancedTextarea",  "onHeightChange")            => "(ReactEventI, Int)=> Callback"
      case ("IconButton",        "onMouseOut")                => MouseEventIHandler
      case ("IconMenu",          "animation")                 => "js.Function"
      case ("IconMenu",          "onChange")                  => "(ReactEventI, js.UndefOr[T]) => Callback"
      case ("IconMenu",          "onItemTouchTap")            => "(ReactTouchEventI, ReactElement) => Callback"
      case ("IconMenu",          "onRequestChange")           => "(Boolean, String) => Callback"
      case ("ListItem",          "onNestedListToggle")        => "js.Any => Callback"
      case ("Menu",              "onEscKeyDown")              => KeyboardEventIHandler
      case ("Menu",              "onChange")                  => "(TouchTapEventI, T | js.Array[T]) => Callback"
      case ("Menu",              "onMenuItemFocusChange")     => "(js.UndefOr[ReactEventI], Int) => Callback"
      case ("Menu",              "onItemTouchTap")            => "(TouchTapEventI, JsComponentM[HasValue[T], _, TopNode]) => Callback"
      case ("MenuItem",          "animation")                 => "js.Function"
      case ("MenuItem",          "onClick")                   => "ReactEventI => Callback"
      case ("Popover",           "onRequestClose")            => "String => Callback"
      case ("Popover",           "animation")                 => "js.Function"
      case ("RadioButton",       "onCheck")                   => "(ReactEventI, String) => Callback"
      case ("RadioButtonGroup",  "onChange")                  => "(ReactEventI, String) => Callback"
      case ("SelectField",       "onChange")                  => "(TouchTapEventI, Int, T) => Callback"
      case ("Slider",            "onChange")                  => "(ReactMouseEventI, Double) => Callback"
      case ("Slider",            "onDragStart")               => "ReactDragEventI => Callback"
      case ("Slider",            "onDragStop")                => "ReactDragEventI => Callback"
      case ("Slider",            "onFocus")                   => FocusEventIhandler
      case ("Snackbar",          "onActionTouchTap")          => TouchEventIHandler
      case ("Snackbar",          "onRequestClose")            => "String => Callback"
      case ("StepContent",       "transition")                => "js.Any"
      case ("Stepper",           "updateCompletedStatus")     => "(Int, ReactNode) => CallbackTo[Boolean]"
      case ("Table",             "onCellClick")               => "(RowId, ColumnId) => Callback"
      case ("Table",             "onCellHover")               => "(RowId, ColumnId) => Callback"
      case ("Table",             "onCellHoverExit")           => "(RowId, ColumnId) => Callback"
      case ("Table",             "onRowHover")                => "RowId => Callback"
      case ("Table",             "onRowHoverExit")            => "RowId => Callback"
      case ("Table",             "onRowSelection")            => "String | js.Array[RowId] => Callback"
      case ("TableBody",         "onCellClick")               => "(RowId, ColumnId) => Callback"
      case ("TableBody",         "onCellHover" )              => "(RowId, ColumnId) => Callback"
      case ("TableBody",         "onCellHoverExit")           => "(RowId, ColumnId) => Callback"
      case ("TableBody",         "onRowHoverExit" )           => "RowId => Callback"
      case ("TableBody",         "onRowHover")                => "RowId => Callback"
      case ("TableBody",         "onRowSelection" )           => "String | js.Array[RowId] => Callback"
      case ("TableHeader",       "onSelectAll")               => "Boolean => Callback"
      case ("TableHeaderColumn", "onHover"   )                => Callback
      case ("TableHeaderColumn", "onHoverExit")               => Callback
      case ("TableHeaderColumn", "onClick"   )                => "(ReactMouseEventI, ColumnId) => Callback"
      case ("TableRow",          "onCellClick")               => "(ReactMouseEventI, RowId, ColumnId) => Callback"
      case ("TableRow",          "onCellHover" )              => "(ReactMouseEventI, RowId, ColumnId) => Callback"
      case ("TableRow",          "onCellHoverExit")           => "(ReactMouseEventI, RowId, ColumnId) => Callback"
      case ("TableRow",          "onRowHover")                => "(ReactMouseEventI, RowId) => Callback"
      case ("TableRow",          "onRowClick")                => "(ReactMouseEventI, RowId) => Callback"
      case ("TableRow",          "onRowHoverExit")            => "(ReactMouseEventI, RowId) => Callback"
      case ("TableRowColumn",    "onClick")                   => "(ReactMouseEventI, ColumnId) => Callback"
      case ("TableRowColumn",    "onHover" )                  => "(ReactMouseEventI, ColumnId) => Callback"
      case ("TableRowColumn",    "onHoverExit")               => "(ReactMouseEventI, ColumnId) => Callback"
      case ("Tab",               "onActive")                  => "ReactElement => Callback"
      case ("Tabs",              "onChange")                  => "(T, ReactEventI, ReactElement) => Callback"
      case ("Tabs",              "tabTemplate")               => "js.Any"
      case ("TextField",         "onChange")                  => "(ReactEventI, String) => Callback"
      case ("TextField",         "onEnterKeyDown")            => KeyboardEventIHandler
      case ("TimePicker",        "onChange")                  => "(js.UndefOr[Nothing], js.Date) => Callback"
      case ("TimePicker",        "onShow")                    => Callback
      case ("TimePicker",        "onDismiss")                 => Callback
      case ("Toggle",            "onToggle")                  => "(ReactMouseEventI, Boolean) => Callback"

      case (_,                   "onBlur")                    => FocusEventIhandler
      case (_,                   "onFocus")                   => FocusEventIhandler
      case (_,                   "onKeyDown")                 => KeyboardEventIHandler
      case (_,                   "onKeyUp")                   => KeyboardEventIHandler
      case (_,                   "onKeyboardFocus")           => "(ReactFocusEventI, Boolean) => Callback"
      case (_,                   "onMouseEnter")              => MouseEventIHandler
      case (_,                   "onMouseLeave")              => MouseEventIHandler
      case (_,                   "onMouseUp")                 => MouseEventIHandler
      case (_,                   "onMouseDown")               => MouseEventIHandler
      case (_,                   "onTouchStart")              => TouchEventIHandler
      case (_,                   "onTouchEnd")                => TouchEventIHandler
      case (_,                   "onTouchTap")                => TouchTapEventIHandler
    }
}
