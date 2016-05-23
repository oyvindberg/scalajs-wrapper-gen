package com.olvind
package mui

object MuiTypeMemberMethodMapper extends MemberMapper {

  override def apply(compName: CompName)(memberMethod: MemberMethod): ParsedMethod =
    ParsedMethod(
      apply(compName, memberMethod.paramNames, memberMethod.name),
      None
    )

  private def apply(c: CompName, args: Seq[String], m: String) =
    (c.value, args.size, m) match {
      case ("DatePicker",       0, "getDate")          => "getDate(): js.Date"
      case ("DatePicker",       1, "setDate")          => "setDate(d: js.Date): Unit"
      case ("DatePicker",       0, "openDialog")       => "openDialog(): Unit"
      case ("DatePicker",       0, "focus")            => "focus(): Unit"
      case ("Dialog",           0, "dismiss")          => "dismiss(): Unit"
      case ("Dialog",           0, "show")             => "show(): Unit"
      case ("Dialog",           0, "isOpen")           => "isOpen(): Boolean"
      case ("Drawer",           0, "open")             => "open(): Unit"
      case ("Drawer",           0, "close")            => "close(): Unit"
      case ("Drawer",           0, "toggle")           => "toggle(): Unit"
      case ("Snackbar",         0, "dismiss")          => "dismiss(): Unit"
      case ("Snackbar",         0, "show")             => "show(): Unit"
      case ("Checkbox",         0, "isChecked")        => "isChecked(): Boolean"
      case ("Checkbox",         1, "setChecked")       => "isChecked(newCheckedValue: Boolean): Unit"
      case ("RadioButtonGroup", 0, "getSelectedValue") => "getSelectedValue(): String"
      case ("RadioButtonGroup", 1, "setSelectedValue") => "setSelectedValue(newSelectionValue: String): Unit"
      case ("RadioButtonGroup", 0, "clearValue")       => "clearValue(): Unit"
      case ("Toggle",           0, "isToggled")        => "isToggled(): Boolean"
      case ("Toggle",           1, "setToggled")       => "setToggled(newToggledValue: Boolean): Unit"
      case ("TextField",        0, "blur")             => "blur(): Unit"
      case ("TextField",        0, "clearValue")       => "clearValue(): Unit"
      case ("TextField",        0, "focus")            => "focus(): Unit"
      case ("TextField",        0, "getValue")         => "getValue(): String"
      case ("TextField",        1, "setErrorText")     => "setErrorText(newErrorText: String): Unit"
      case ("TextField",        1, "setValue")         => "setValue(newValue: String): Unit"
      case ("TimePicker",       0, "getTime")          => "getTime(): js.Date"
      case ("TimePicker",       1, "setTime")          => "setTime(d: js.Date): Unit"
      case ("TimePicker",       1, "formatTime")       => "formatTime(d: js.Date): String"
      case ("TimePicker",       0, "openDialog")       => "openDialog(): Unit"
      case ("TimePicker",       0, "focus")            => "focus(): Unit"
      case other ⇒
        println(other)
        m + args.map(sanitize(_) + ": js.Any").mkString("(", ", ", ")") + ": js.Any"
    }
  def sanitize(s: String) =
    if (s == "val") "`val`" else s
}
