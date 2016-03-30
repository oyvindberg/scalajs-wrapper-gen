package com.olvind
package mui

object MuiTypeMapperMethod {
  def apply(c: CompName, m: PropName) = (c.value, m.clean.value) match {
    case ("DatePicker",       "getDate")          => "getDate(): js.Date"
    case ("DatePicker",       "setDate")          => "setDate(d: js.Date): Unit"
    case ("DatePicker",       "openDialog")       => "openDialog(): Unit"
    case ("DatePicker",       "focus")            => "focus(): Unit"
    case ("Dialog",           "dismiss")          => "dismiss(): Unit"
    case ("Dialog",           "show")             => "show(): Unit"
    case ("Dialog",           "isOpen")           => "isOpen(): Boolean"
    case ("Drawer",           "open")             => "open(): Unit"
    case ("Drawer",           "close")            => "close(): Unit"
    case ("Drawer",           "toggle")           => "toggle(): Unit"
    case ("Snackbar",         "dismiss")          => "dismiss(): Unit"
    case ("Snackbar",         "show")             => "show(): Unit"
    case ("Checkbox",         "isChecked")        => "isChecked(): Boolean"
    case ("Checkbox",         "setChecked")       => "isChecked(newCheckedValue: Boolean): Unit"
    case ("RadioButtonGroup", "getSelectedValue") => "getSelectedValue(): String"
    case ("RadioButtonGroup", "setSelectedValue") => "setSelectedValue(newSelectionValue: String): Unit"
    case ("RadioButtonGroup", "clearValue")       => "clearValue(): Unit"
    case ("Toggle",           "isToggled")        => "isToggled(): Boolean"
    case ("Toggle",           "setToggled")       => "setToggled(newToggledValue: Boolean): Unit"
    case ("TextField",        "blur")             => "blur(): Unit"
    case ("TextField",        "clearValue")       => "clearValue(): Unit"
    case ("TextField",        "focus")            => "focus(): Unit"
    case ("TextField",        "getValue")         => "getValue(): String"
    case ("TextField",        "setErrorText")     => "setErrorText(newErrorText: String): Unit"
    case ("TextField",        "setValue")         => "setValue(newValue: String): Unit"
    case ("TimePicker",       "getTime")          => "getTime(): js.Date"
    case ("TimePicker",       "setTime")          => "setTime(d: js.Date): Unit"
    case ("TimePicker",       "formatTime")       => "formatTime(d: js.Date): String"
    case ("TimePicker",       "openDialog")       => "openDialog(): Unit"
    case ("TimePicker",       "focus")            => "focus(): Unit"
  }
}
