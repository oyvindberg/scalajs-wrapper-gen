package com.olvind
package mui

import ammonite.ops._

import scala.language.implicitConversions

object MuiLibrary extends Library[ComponentDef] {

  override val importName = VarName("mui")
  override val location   = home / "pr" / "material-ui" / "build"
  override val prefixOpt  = Some("Mui")
  override val nameOpt    = Some("materialui")

  override val docProvider = DocProvider.Dummy
  override val typeMapper  = MuiTypeMapper


  //todo: make requiresjs figure this stuff out!

  object AppBar extends ComponentDef {
    override val name = CompName("AppBar")
  }
  object AppCanvas extends ComponentDef {
    override val name = CompName("AppCanvas")
  }
  object Avatar extends ComponentDef {
    override val name = CompName("Avatar")
    override val multipleChildren = false
  }
  object AutoComplete extends ComponentDef {
    override val name = CompName("AutoComplete")
  }
  object Badge extends ComponentDef {
    override val name = CompName("Badge")
  }

  object BeforeAfterWrapper extends ComponentDef {
    override val name = CompName("BeforeAfterWrapper")
  }

  trait MuiButtons extends ComponentDef {
    override val shared = Some(CompName("EnhancedButton"))
  }

  object FlatButton extends MuiButtons {
    override val name = CompName("FlatButton")
  }
  object RaisedButton extends MuiButtons {
    override val name = CompName("RaisedButton")
  }
  object FloatingActionButton extends MuiButtons {
    override val name = CompName("FloatingActionButton")
  }
  object IconButton extends MuiButtons {
    override val name = CompName("IconButton")
  }

  object Card extends ComponentDef{
    override val name = CompName("Card")
  }
  object CardHeader extends ComponentDef{
    override val name = CompName("CardHeader")
  }
  object CardExpandable extends ComponentDef{
    override val name = CompName("CardExpandable")
  }
  object CardMedia extends ComponentDef{
    override val name = CompName("CardMedia")
  }
  object CardTitle extends ComponentDef{
    override val name = CompName("CardTitle")
  }
  object CardActions extends ComponentDef{
    override val name = CompName("CardActions")
  }
  object CardText extends ComponentDef{
    override val name = CompName("CardText")
  }

  object DatePicker extends ComponentDef {
    override val shared = Option(CompName("TextField"))
    override val name = CompName("DatePicker")
    override val postlude: Option[String] = Some(
      """
        |case class Wordings(ok: String, cancel: String){
        |  val toJS = JSMacro[Wordings](this)
        |}
        |
      """.stripMargin)
  }
  object Dialog extends ComponentDef {
    override val name = CompName("Dialog")
  }

  object Divider extends ComponentDef {
    val name = CompName("Divider")
  }

  object DropDownMenu extends ComponentDef {
    override val name = CompName("DropDownMenu")
  }

  object FontIcon extends ComponentDef {
    override val name = CompName("FontIcon")
  }

  object GridList extends ComponentDef {
    override val name = CompName("GridList")
  }
  object GridTile extends ComponentDef {
    override val name = CompName("GridTile")
  }

  object IconMenu extends ComponentDef {
    override val name = CompName("IconMenu")
  }
  object Drawer extends ComponentDef {
    override val name = CompName("Drawer")
  }

  object MuiList extends ComponentDef {
    override val name = CompName("List")
  }

  object ListDivider extends ComponentDef {
    override val name = CompName("ListDivider")
    override val deprecated: Boolean = true
  }

  object ListItem extends ComponentDef {
    override val name = CompName("ListItem")
  }

  object Menu extends ComponentDef {
    override val name = CompName("Menu")
    override val postlude = Some(
      """
        |@js.native
        |trait MuiMenuItemProps extends js.Object {
        |	def value: js.UndefOr[String] = js.native
        |}
      """.stripMargin)
  }

  object MenuItem extends ComponentDef {
    override val name = CompName("MenuItem")
    override val shared = Option(CompName("ListItem"))
  }

  object Overlay extends ComponentDef {
    override val name = CompName("Overlay")
  }

  object Paper extends ComponentDef {
    override val name = CompName("Paper")
  }

  object Popover extends ComponentDef {
    override val multipleChildren: Boolean = false
    override val name = CompName("Popover")
  }

  object LinearProgress extends ComponentDef {
    override val name = CompName("LinearProgress")
  }

  object CircularProgress extends ComponentDef {
    override val name = CompName("CircularProgress")
  }

  object RefreshIndicator extends ComponentDef {
    override val name = CompName("RefreshIndicator")
  }

  object CircleRipple extends ComponentDef {
    override val name = CompName("CircleRipple")
  }
  object FocusRipple extends ComponentDef {
    override val name = CompName("FocusRipple")
  }
  object TouchRipple extends ComponentDef {
    override val name = CompName("TouchRipple")
  }

  object SelectField extends ComponentDef {
    override val shared = Option(CompName("DropDownMenu"))
    override val name = CompName("SelectField")
    override val postlude = Some("""case class MuiSelectItem(payload: String, text: String){
                                   |	val toJS = JSMacro[MuiSelectItem](this)
                                   |}
                                   |""".stripMargin)
  }
  object Slider extends ComponentDef {
    override val name = CompName("Slider")
  }


  object Snackbar extends ComponentDef {
    override val name = CompName("Snackbar")
  }

  trait MuiSwitches extends ComponentDef {
    override val shared = Some(CompName("EnhancedSwitch"))
  }

  object Checkbox extends MuiSwitches {
    override val name = CompName("Checkbox")
  }
  object RadioButton extends MuiSwitches {
    override val name = CompName("RadioButton")
  }
  object RadioButtonGroup extends ComponentDef {
    override val name = CompName("RadioButtonGroup")
  }
  object Toggle extends MuiSwitches {
    override val name = CompName("Toggle")
  }

  object Table extends ComponentDef {
    override val name = CompName("Table")
  }
  object TableBody extends ComponentDef {
    override val name = CompName("TableBody")
  }
  object TableHeader extends ComponentDef {
    override val name = CompName("TableHeader")
  }
  object TableHeaderColumn extends ComponentDef {
    override val name = CompName("TableHeaderColumn")
  }
  object TableFooter extends ComponentDef {
    override val name = CompName("TableFooter")
  }
  object TableRow extends ComponentDef {
    override val name = CompName("TableRow")
  }
  object TableRowColumn extends ComponentDef {
    override val name = CompName("TableRowColumn")
  }

  object Tab extends ComponentDef {
    override val name = CompName("Tab")
  }
  object Tabs extends ComponentDef {
    override val name = CompName("Tabs")
  }

  object ThemeWrapper extends ComponentDef {
    override val name = CompName("ThemeWrapper")
  }

  object TextField extends ComponentDef {
    override val name = CompName("TextField")
    override val shared = Some(CompName("EnhancedTextarea"))
  }

  object TimePicker extends ComponentDef {
    override val shared = Option(CompName("TextField"))
    override val name = CompName("TimePicker")
  }

  object Toolbar extends ComponentDef{
    override val name = CompName("Toolbar")
  }
  object ToolbarGroup extends ComponentDef{
    override val name = CompName("ToolbarGroup")
  }
  object ToolbarSeparator extends ComponentDef{
    override val name = CompName("ToolbarSeparator")
  }
  object ToolbarTitle extends ComponentDef{
    override val name = CompName("ToolbarTitle")
  }
  object Tooltip extends ComponentDef {
    override val name = CompName("Tooltip")
  }

  object RenderToLayer extends ComponentDef {
    override val name = CompName("RenderToLayer")
  }
  object EnhancedTextarea extends ComponentDef {
    override val name = CompName("EnhancedTextarea")
  }
  object Subheader extends ComponentDef {
    override val name = CompName("Subheader")
  }
//  export Step from './Step';
//  export StepButton from './StepButton';
//  export StepContent from './StepContent';
//  export StepLabel from './StepLabel';
//  export Stepper from './Stepper';

  object Step extends ComponentDef {
    override val name = CompName("Step")
  }
  object StepButton extends ComponentDef {
    override val name = CompName("StepButton")
  }
  object StepContent extends ComponentDef {
    override val name = CompName("StepContent")
  }
  object StepLabel extends ComponentDef {
    override val name = CompName("StepLabel")
  }
  object Stepper extends ComponentDef {
    override val name = CompName("Stepper")
  }
  object FlatButtonLabel extends ComponentDef {
    override val name = CompName("FlatButtonLabel")
  }

  val components = List(
    EnhancedTextarea,
    Step, StepButton, StepContent, StepLabel, Stepper,
    AppBar,
    Avatar,
    AutoComplete,
    Badge,
    Card, CardHeader, CardExpandable, CardMedia, CardTitle, CardActions, CardText,
    DatePicker,
    Dialog,
    DropDownMenu,
    FlatButton,
    FloatingActionButton,
    FontIcon,
    GridList,
    GridTile,
    IconButton,
    IconMenu,
    Drawer,
    MuiList,
    ListItem,
    Menu, MenuItem,
    Popover,
    CircularProgress, LinearProgress,
    RaisedButton,
    RefreshIndicator,
    Paper,
    SelectField,
    Slider,
    Snackbar,
    Checkbox, RadioButton, RadioButtonGroup, Toggle,
    Table, TableBody, TableHeader, TableHeaderColumn, TableFooter, TableRow, TableRowColumn,
    Tab, Tabs,
    TextField,
    TimePicker,
    Toolbar, ToolbarGroup, ToolbarSeparator, ToolbarTitle
    //    Subheader,
//    Divider
//    RenderToLayer,
//    FlatButtonLabel,
//    AppCanvas,
//    BeforeAfterWrapper,
//    Overlay,
//    CircleRipple, TouchRipple, FocusRipple,
//    Tooltip
  )
}