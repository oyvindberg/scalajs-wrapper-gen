package com.olvind
package mui

import ammonite.ops._

import scala.language.implicitConversions

object MuiLibrary extends Library[ComponentDef] {

  override val importName   = VarName("mui")
  override val location     = home / "pr" / "material-ui" / "build"
  override val prefixOpt    = Some("Mui")
  override val name         = "materialui"
  override val typeMapper   = MuiTypeMapper
  override val memberMapper = MuiTypeMemberMethodMapper
  override val outputPath   = home/'pr / "scalajs-react-components"/'core /'src/'main/'scala/'chandu0101/'scalajs/'react/'components/name

  //todo: make requiresjs figure this stuff out!
  case object Internal {
    val BeforeAfterWrapper = ComponentDef(CompName("BeforeAfterWrapper"))
    val EnhancedButton     = ComponentDef(CompName("EnhancedButton"))
    val EnhancedSwitch     = ComponentDef(CompName("EnhancedSwitch"))
    val Overlay            = ComponentDef(CompName("Overlay"))
    val CircleRipple       = ComponentDef(CompName("CircleRipple"))
    val FocusRipple        = ComponentDef(CompName("FocusRipple"))
    val TouchRipple        = ComponentDef(CompName("TouchRipple"))
    val RenderToLayer      = ComponentDef(CompName("RenderToLayer"))
    val EnhancedTextarea   = ComponentDef(CompName("EnhancedTextarea"))
  }

  def DropDownMenu = ComponentDef(CompName("DropDownMenu"))
  def List_        = ComponentDef(CompName("List"))
  def ListItem     = ComponentDef(CompName("ListItem"), Some(Internal.EnhancedButton))
  def Menu         = ComponentDef(CompName("Menu"), Some(List_))
  def Paper        = ComponentDef(CompName("Paper"))
  def RadioButton  = ComponentDef(CompName("RadioButton"), Some(Internal.EnhancedSwitch))
  def TextField    = ComponentDef(CompName("TextField"), Some(Internal.EnhancedTextarea))
  
  val components: Seq[ComponentDef] =
    Seq(
      ComponentDef(CompName("AppBar"), Option(Paper)),
//      ComponentDef(CompName("AppCanvas")),
      ComponentDef(CompName("AutoComplete"), Some(TextField)),
      ComponentDef(CompName("Avatar"), multipleChildren = false),
      ComponentDef(CompName("Badge")),
      ComponentDef(CompName("Card"), Some(Paper)),
      ComponentDef(CompName("CardActions")),
      ComponentDef(CompName("CardExpandable")),
      ComponentDef(CompName("CardHeader")),
      ComponentDef(CompName("CardMedia")),
      ComponentDef(CompName("CardText")),
      ComponentDef(CompName("CardTitle")),
      ComponentDef(CompName("Checkbox"), Some(Internal.EnhancedSwitch)),
      ComponentDef(CompName("CircularProgress")),
      ComponentDef(CompName("DatePicker"), Option(TextField)),
      ComponentDef(CompName("Dialog")),
      ComponentDef(CompName("Divider")),
      ComponentDef(CompName("Drawer")),
      DropDownMenu,
      ComponentDef(CompName("FlatButton"), Some(Internal.EnhancedButton)),
      ComponentDef(CompName("FloatingActionButton"), Some(Internal.EnhancedButton)),
      ComponentDef(CompName("FontIcon")),
      ComponentDef(CompName("GridList")),
      ComponentDef(CompName("GridTile")),
      ComponentDef(CompName("IconButton"), Some(Internal.EnhancedButton)),
      ComponentDef(CompName("IconMenu"), Option(Menu)),
      ComponentDef(CompName("LinearProgress")),
      List_,
      ListItem,
      Menu,
      ComponentDef(CompName("MenuItem"), Option(ListItem)),
      ComponentDef(CompName("MuiThemeProvider")),
      Paper,
      ComponentDef(CompName("Popover")),
      ComponentDef(CompName("PopoverAnimationVertical")),
      RadioButton,
      ComponentDef(CompName("RadioButtonGroup"), Some(RadioButton)),
      ComponentDef(CompName("RaisedButton"), Some(Internal.EnhancedButton)),
      ComponentDef(CompName("RefreshIndicator")),
      ComponentDef(CompName("SelectField"), Option(DropDownMenu)),
      ComponentDef(CompName("Slider")),
      ComponentDef(CompName("Snackbar")),
      ComponentDef(CompName("Step")),
      ComponentDef(CompName("StepContent")),
      ComponentDef(CompName("StepLabel")),
      ComponentDef(CompName("Stepper")),
      ComponentDef(CompName("Subheader")),
      ComponentDef(CompName("Tab"), Some(Internal.EnhancedButton)),
      ComponentDef(CompName("Table")),
      ComponentDef(CompName("TableBody")),
      ComponentDef(CompName("TableFooter")),
      ComponentDef(CompName("TableHeader")),
      ComponentDef(CompName("TableHeaderColumn")),
      ComponentDef(CompName("TableRow")),
      ComponentDef(CompName("TableRowColumn")),
      ComponentDef(CompName("Tabs")),
      TextField,
      ComponentDef(CompName("TimePicker"), Option(TextField)),
      ComponentDef(CompName("Toggle"), Some(Internal.EnhancedSwitch)),
      ComponentDef(CompName("Toolbar")),
      ComponentDef(CompName("ToolbarGroup")),
      ComponentDef(CompName("ToolbarSeparator")),
      ComponentDef(CompName("ToolbarTitle"))
//      ComponentDef(CompName("Tooltip"))
    )
}