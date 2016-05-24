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
    case object BeforeAfterWrapper extends ComponentDef(CompName("BeforeAfterWrapper"))
    case object EnhancedButton     extends ComponentDef(CompName("EnhancedButton"))
    case object EnhancedSwitch     extends ComponentDef(CompName("EnhancedSwitch"))
    case object Overlay            extends ComponentDef(CompName("Overlay"))
    case object CircleRipple       extends ComponentDef(CompName("CircleRipple"))
    case object FocusRipple        extends ComponentDef(CompName("FocusRipple"))
    case object TouchRipple        extends ComponentDef(CompName("TouchRipple"))
    case object RenderToLayer      extends ComponentDef(CompName("RenderToLayer"))
    case object EnhancedTextarea   extends ComponentDef(CompName("EnhancedTextarea"))
  }

  case object AppBar extends ComponentDef(CompName("AppBar")) {
    override val shared = Option(Paper)
  }

  case object AppCanvas extends ComponentDef(CompName("AppCanvas"))

  case object Avatar extends ComponentDef(CompName("Avatar")){
    override val multipleChildren = false
  }

  case object AutoComplete extends ComponentDef(CompName("AutoComplete")) {
    override val shared = Some(TextField)
  }

  case object Badge extends ComponentDef(CompName("Badge"))

  case object FlatButton extends ComponentDef(CompName("FlatButton")){
    override val shared = Some(Internal.EnhancedButton)
  }
  case object RaisedButton extends ComponentDef(CompName("RaisedButton")){
    override val shared = Some(Internal.EnhancedButton)
  }
  case object FloatingActionButton extends ComponentDef(CompName("FloatingActionButton")){
    override val shared = Some(Internal.EnhancedButton)
  }
  case object IconButton extends ComponentDef(CompName("IconButton")){
    override val shared = Some(Internal.EnhancedButton)
  }

  case object Card extends ComponentDef(CompName("Card")){
    override val shared = Some(Paper)
  }
  case object CardHeader extends ComponentDef(CompName("CardHeader"))
  case object CardExpandable extends ComponentDef(CompName("CardExpandable"))
  case object CardMedia extends ComponentDef(CompName("CardMedia"))
  case object CardTitle extends ComponentDef(CompName("CardTitle"))
  case object CardActions extends ComponentDef(CompName("CardActions"))
  case object CardText extends ComponentDef(CompName("CardText"))

  case object DatePicker extends ComponentDef(CompName("DatePicker")){
    override val shared = Option(TextField)
    override val postlude: Option[String] = Some(
      """
        |case class Wordings(ok: String, cancel: String){
        |  val toJS = JSMacro[Wordings](this)
        |}
        |
      """.stripMargin)
  }

  case object Dialog extends ComponentDef(CompName("Dialog"))
  case object Divider extends ComponentDef(CompName("Divider"))
  case object Drawer extends ComponentDef(CompName("Drawer"))
  case object DropDownMenu extends ComponentDef(CompName("DropDownMenu"))
  case object FontIcon extends ComponentDef(CompName("FontIcon"))
  case object GridList extends ComponentDef(CompName("GridList"))
  case object GridTile extends ComponentDef(CompName("GridTile"))

  case object IconMenu extends ComponentDef(CompName("IconMenu")){
    override val shared = Option(Menu)
  }

  case object MuiList extends ComponentDef(CompName("List"))

  case object ListItem extends ComponentDef(CompName("ListItem")) {
    override val shared = Some(Internal.EnhancedButton)
  }

  case object Menu extends ComponentDef(CompName("Menu")){
    override val shared = Some(MuiList)
    override val postlude = Some(
      """
        |@js.native
        |trait MuiMenuItemProps extends js.Object {
        |	def value: js.UndefOr[String] = js.native
        |}
      """.stripMargin)
  }

  case object MenuItem extends ComponentDef(CompName("MenuItem")){
    override val shared = Option(ListItem)
  }

  case object Paper extends ComponentDef(CompName("Paper"))

  case object Popover extends ComponentDef(CompName("Popover")) {
    override val multipleChildren: Boolean = false
  }

  case object PopoverAnimationVertical extends ComponentDef(CompName("PopoverAnimationVertical"))

  case object LinearProgress extends ComponentDef(CompName("LinearProgress"))

  case object CircularProgress extends ComponentDef(CompName("CircularProgress"))

  case object RefreshIndicator extends ComponentDef(CompName("RefreshIndicator"))

  case object SelectField extends ComponentDef(CompName("SelectField")){
    override val shared = Option(DropDownMenu)
  }

  case object Slider extends ComponentDef(CompName("Slider"))

  case object Snackbar extends ComponentDef(CompName("Snackbar"))

  case object Checkbox extends ComponentDef(CompName("Checkbox")){
    override val shared = Some(Internal.EnhancedSwitch)
  }
  case object RadioButton extends ComponentDef(CompName("RadioButton")){
    override val shared = Some(Internal.EnhancedSwitch)
  }

  case object RadioButtonGroup extends ComponentDef(CompName("RadioButtonGroup")){
    override val shared = Some(RadioButton)
  }
  case object Toggle extends ComponentDef(CompName("Toggle")){
    override val shared = Some(Internal.EnhancedSwitch)
  }

  case object Table extends ComponentDef(CompName("Table"))
  case object TableBody extends ComponentDef(CompName("TableBody"))
  case object TableHeader extends ComponentDef(CompName("TableHeader"))
  case object TableHeaderColumn extends ComponentDef(CompName("TableHeaderColumn"))
  case object TableFooter extends ComponentDef(CompName("TableFooter"))
  case object TableRow extends ComponentDef(CompName("TableRow"))
  case object TableRowColumn extends ComponentDef(CompName("TableRowColumn"))

  case object Tab extends ComponentDef(CompName("Tab")){
    override val shared = Some(Internal.EnhancedButton)
  }

  case object Tabs extends ComponentDef(CompName("Tabs"))

  case object TextField extends ComponentDef(CompName("TextField")){
    override val shared = Some(Internal.EnhancedTextarea)
  }

  case object TimePicker extends ComponentDef(CompName("TimePicker")){
    override val shared = Option(TextField)
  }

  case object Toolbar extends ComponentDef(CompName("Toolbar"))
  case object ToolbarGroup extends ComponentDef(CompName("ToolbarGroup"))
  case object ToolbarSeparator extends ComponentDef(CompName("ToolbarSeparator"))
  case object ToolbarTitle extends ComponentDef(CompName("ToolbarTitle"))

  case object Tooltip extends ComponentDef(CompName("Tooltip"))

  case object Subheader extends ComponentDef(CompName("Subheader"))

  case object Step extends ComponentDef(CompName("Step"))
  case object StepButton extends ComponentDef(CompName("StepButton")){
    override val shared = Some(Internal.EnhancedButton)
  }
  case object StepContent extends ComponentDef(CompName("StepContent"))
  case object StepLabel extends ComponentDef(CompName("StepLabel"))
  case object Stepper extends ComponentDef(CompName("Stepper"))

  case object ThemeProvider extends ComponentDef(CompName("MuiThemeProvider"))

  val components = List(
    AppBar,
    AutoComplete,
    Avatar,
    Badge,
    Card,
    CardActions,
    CardExpandable,
    CardHeader,
    CardMedia,
    CardText,
    CardTitle,
    Checkbox,
    CircularProgress,
    DatePicker,
    Dialog,
    Divider,
    Drawer,
    DropDownMenu,
    FlatButton,
    FloatingActionButton,
    FontIcon,
    GridList,
    GridTile,
    IconButton,
    IconMenu,
    LinearProgress,
    ListItem,
    Menu,
    MenuItem,
    MuiList,
    Paper,
    Popover,
    PopoverAnimationVertical,
    RadioButton,
    RadioButtonGroup,
    RaisedButton,
    RefreshIndicator,
    SelectField,
    Slider,
    Snackbar,
    Step,
    StepButton,
    StepContent,
    StepLabel,
    Stepper,
    Subheader,
    Tab,
    Table,
    TableBody,
    TableFooter,
    TableHeader,
    TableHeaderColumn,
    TableRow,
    TableRowColumn,
    Tabs,
    TextField,
    ThemeProvider,
    TimePicker,
    Toggle,
    Toolbar,
    ToolbarGroup,
    ToolbarSeparator,
    ToolbarTitle
  )
}