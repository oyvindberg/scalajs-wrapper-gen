package com.olvind
package mui

import ammonite.ops._

import scala.language.implicitConversions

case class MuiLibrary(location: Path) extends Library {
  override val prefixOpt = Some("Mui")
  override val name = "materialui"
  override val typeMapper = MuiTypeMapper
  override val memberMapper = MuiTypeMemberMethodMapper

  override val inheritance: Map[CompName, CompName] =
    Map(
      "ListItem" -> "EnhancedButton",
      "Menu" -> "List",
      "RadioButton" -> "EnhancedSwitch",
      "TextField" -> "EnhancedTextarea",
      "AppBar" -> "Paper",
      "AutoComplete" -> "TextField",
      "Card" -> "Paper",
      "Checkbox" -> "EnhancedSwitch",
      "Chip" -> "EnhancedButton",
      "DatePicker" -> "TextField",
      "FlatButton" -> "EnhancedButton",
      "FloatingActionButton" -> "EnhancedButton",
      "IconButton" -> "EnhancedButton",
      "IconMenu" -> "Menu",
      "MenuItem" -> "ListItem",
      "RadioButtonGroup" -> "RadioButton",
      "RaisedButton" -> "EnhancedButton",
      "SelectField" -> "DropDownMenu",
      "StepButton" -> "EnhancedButton",
      "Tab" -> "EnhancedButton",
      "TimePicker" -> "TextField",
      "Toggle" -> "EnhancedSwitch"
    ).map { case (k, v) => (CompName(k), CompName(v)) }
}
