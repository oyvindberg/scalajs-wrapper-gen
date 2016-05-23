package com.olvind
package mui

import ammonite.ops._

import scala.language.implicitConversions

case object MuiLibrary extends Library[ComponentDef] {

  override val importName  = VarName("mui")
  override val location    = home / "pr" / "material-ui" / "build"
  override val prefixOpt   = Some("Mui")
  override val nameOpt     = Some("materialui")
  override val typeMapper   = MuiTypeMapper
  override def memberMapper = MuiTypeMemberMethodMapper

  //todo: make requiresjs figure this stuff out!
  case object Internal {
    case object BeforeAfterWrapper extends ComponentDef {
      override val name = CompName("BeforeAfterWrapper")
    }
    case object EnhancedButton extends ComponentDef {
      override val name = CompName("EnhancedButton")
    }
    case object EnhancedSwitch extends ComponentDef {
      override val name = CompName("EnhancedSwitch")
    }
    case object Overlay extends ComponentDef {
      override val name = CompName("Overlay")
    }
    case object CircleRipple extends ComponentDef {
      override val name = CompName("CircleRipple")
    }
    case object FocusRipple extends ComponentDef {
      override val name = CompName("FocusRipple")
    }
    case object TouchRipple extends ComponentDef {
      override val name = CompName("TouchRipple")
    }

    case object RenderToLayer extends ComponentDef {
      override val name = CompName("RenderToLayer")
    }
    case object EnhancedTextarea extends ComponentDef {
      override val name = CompName("EnhancedTextarea")
    }
  }

  case object AppBar extends ComponentDef {
    override val shared = Option(Paper)
    override val name = CompName("AppBar")
  }

  case object AppCanvas extends ComponentDef {
    override val name = CompName("AppCanvas")
  }

  case object Avatar extends ComponentDef {
    override val name = CompName("Avatar")
    override val multipleChildren = false
  }

  case object AutoComplete extends ComponentDef {
    override val shared = Some(TextField)
    override val name = CompName("AutoComplete")
  }

  case object Badge extends ComponentDef {
    override val name = CompName("Badge")
  }

  case object FlatButton extends ComponentDef {
    override val name = CompName("FlatButton")
    override val shared = Some(Internal.EnhancedButton)
  }
  case object RaisedButton extends ComponentDef {
    override val name = CompName("RaisedButton")
    override val shared = Some(Internal.EnhancedButton)
  }
  case object FloatingActionButton extends ComponentDef {
    override val name = CompName("FloatingActionButton")
    override val shared = Some(Internal.EnhancedButton)
  }
  case object IconButton extends ComponentDef {
    override val name = CompName("IconButton")
    override val shared = Some(Internal.EnhancedButton)
  }

  case object Card extends ComponentDef{
    override val shared = Some(Paper)
    override val name = CompName("Card")
  }
  case object CardHeader extends ComponentDef{
    override val name = CompName("CardHeader")
  }
  case object CardExpandable extends ComponentDef{
    override val name = CompName("CardExpandable")
  }
  case object CardMedia extends ComponentDef{
    override val name = CompName("CardMedia")
  }
  case object CardTitle extends ComponentDef{
    override val name = CompName("CardTitle")
  }
  case object CardActions extends ComponentDef{
    override val name = CompName("CardActions")
  }
  case object CardText extends ComponentDef{
    override val name = CompName("CardText")
  }

  case object DatePicker extends ComponentDef {
    override val shared = Option(TextField)
    override val name = CompName("DatePicker")
    override val postlude: Option[String] = Some(
      """
        |case class Wordings(ok: String, cancel: String){
        |  val toJS = JSMacro[Wordings](this)
        |}
        |
      """.stripMargin)
  }

  case object Dialog extends ComponentDef {
    override val name = CompName("Dialog")
  }

  case object Divider extends ComponentDef {
    val name = CompName("Divider")
  }

  case object DropDownMenu extends ComponentDef {
    override val name = CompName("DropDownMenu")
  }

  case object FontIcon extends ComponentDef {
    override val name = CompName("FontIcon")
  }

  case object GridList extends ComponentDef {
    override val name = CompName("GridList")
  }
  case object GridTile extends ComponentDef {
    override val name = CompName("GridTile")
  }

  case object IconMenu extends ComponentDef {
    override val shared = Option(Menu)
    override val name = CompName("IconMenu")
  }

  case object Drawer extends ComponentDef {
    override val name = CompName("Drawer")
  }

  case object MuiList extends ComponentDef {
    override val name = CompName("List")
  }

  case object ListItem extends ComponentDef {
    override val shared = Some(Internal.EnhancedButton)
    override val name = CompName("ListItem")
  }

  case object Menu extends ComponentDef {
    override val shared = Some(MuiList)
    override val name = CompName("Menu")
    override val postlude = Some(
      """
        |@js.native
        |trait MuiMenuItemProps extends js.Object {
        |	def value: js.UndefOr[String] = js.native
        |}
      """.stripMargin)
  }

  case object MenuItem extends ComponentDef {
    override val name = CompName("MenuItem")
    override val shared = Option(ListItem)
  }

  case object Paper extends ComponentDef {
    override val name = CompName("Paper")
  }

  case object Popover extends ComponentDef {
    override val multipleChildren: Boolean = false
    override val name = CompName("Popover")
  }

  case object PopoverAnimationVertical extends ComponentDef {
    override val name = CompName("PopoverAnimationVertical")
  }

  case object LinearProgress extends ComponentDef {
    override val name = CompName("LinearProgress")
  }

  case object CircularProgress extends ComponentDef {
    override val name = CompName("CircularProgress")
  }

  case object RefreshIndicator extends ComponentDef {
    override val name = CompName("RefreshIndicator")
  }

  case object SelectField extends ComponentDef {
    override val shared = Option(DropDownMenu)
    override val name = CompName("SelectField")
  }

  case object Slider extends ComponentDef {
    override val name = CompName("Slider")
  }

  case object Snackbar extends ComponentDef {
    override val name = CompName("Snackbar")
  }

  case object Checkbox extends ComponentDef {
    override val name = CompName("Checkbox")
    override val shared = Some(Internal.EnhancedSwitch)
  }
  case object RadioButton extends ComponentDef {
    override val name = CompName("RadioButton")
    override val shared = Some(Internal.EnhancedSwitch)
  }

  case object RadioButtonGroup extends ComponentDef {
    override val shared = Some(RadioButton)
    override val name = CompName("RadioButtonGroup")
  }
  case object Toggle extends ComponentDef {
    override val name = CompName("Toggle")
    override val shared = Some(Internal.EnhancedSwitch)
  }

  case object Table extends ComponentDef {
    override val name = CompName("Table")
  }
  case object TableBody extends ComponentDef {
    override val name = CompName("TableBody")
  }
  case object TableHeader extends ComponentDef {
    override val name = CompName("TableHeader")
  }
  case object TableHeaderColumn extends ComponentDef {
    override val name = CompName("TableHeaderColumn")
  }
  case object TableFooter extends ComponentDef {
    override val name = CompName("TableFooter")
  }
  case object TableRow extends ComponentDef {
    override val name = CompName("TableRow")
  }
  case object TableRowColumn extends ComponentDef {
    override val name = CompName("TableRowColumn")
  }

  case object Tab extends ComponentDef {
    override val shared = Some(Internal.EnhancedButton)
    override val name = CompName("Tab")
  }

  case object Tabs extends ComponentDef {
    override val name = CompName("Tabs")
  }

  case object TextField extends ComponentDef {
    override val name = CompName("TextField")
    override val shared = Some(Internal.EnhancedTextarea)
  }

  case object TimePicker extends ComponentDef {
    override val shared = Option(TextField)
    override val name = CompName("TimePicker")
  }

  case object Toolbar extends ComponentDef{
    override val name = CompName("Toolbar")
  }
  case object ToolbarGroup extends ComponentDef{
    override val name = CompName("ToolbarGroup")
  }
  case object ToolbarSeparator extends ComponentDef{
    override val name = CompName("ToolbarSeparator")
  }
  case object ToolbarTitle extends ComponentDef{
    override val name = CompName("ToolbarTitle")
  }

  case object Tooltip extends ComponentDef {
    override val name = CompName("Tooltip")
  }

  case object Subheader extends ComponentDef {
    override val name = CompName("Subheader")
  }

  case object Step extends ComponentDef {
    override val name = CompName("Step")
  }
  case object StepButton extends ComponentDef {
    override val shared = Some(Internal.EnhancedButton)
    override val name = CompName("StepButton")
  }
  case object StepContent extends ComponentDef {
    override val name = CompName("StepContent")
  }
  case object StepLabel extends ComponentDef {
    override val name = CompName("StepLabel")
  }
  case object Stepper extends ComponentDef {
    override val name = CompName("Stepper")
  }

  case object ThemeProvider extends ComponentDef {
    override val name = CompName("MuiThemeProvider")
  }

  val components = List(
    Step, StepButton, StepContent, StepLabel, Stepper,
    AppBar,
    Avatar,
    AutoComplete,
    Badge,
    Card, CardHeader, CardExpandable, CardMedia, CardTitle, CardActions, CardText,
    DatePicker,
    Dialog,
    Divider,
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
    Popover, PopoverAnimationVertical,
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
    Toolbar, ToolbarGroup, ToolbarSeparator, ToolbarTitle,
    ThemeProvider
  )
}