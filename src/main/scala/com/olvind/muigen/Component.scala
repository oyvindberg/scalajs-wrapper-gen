package com.olvind.muigen

trait Component {
  val name: String
  val json: String
  val children: Boolean
  val propsSections: Seq[String] = Seq("Props")
  val overrideMethods: Option[String] = None
  val overrideEvents: Option[String] = None
  val shared: Option[SharedComponent] = None
  val postlude: Option[String] = None
}

trait SharedComponent {
  val name: String
  val inheritProps: List[OutField]
  val inheritEvents: List[OutField]
}

object ButtonSharedComponent extends SharedComponent {
  val name = "EnhancedButton"
  val inheritProps = List[OutField](
    OptField("centerRipple",           OutParam.mapType(name, "centerRipple"        )("boolean"                         ), None),
    OptField("containerElement",       OutParam.mapType(name, "containerElement"    )("oneOfType([string, element])" ), None),
    OptField("disabled",               OutParam.mapType(name, "disabled"            )("boolean"                         ), None),
    OptField("disableFocusRipple",     OutParam.mapType(name, "disableFocusRipple"  )("boolean"                         ), None),
    OptField("disableKeyboardFocus",   OutParam.mapType(name, "disableKeyboardFocus")("boolean"                         ), None),
    OptField("disableTouchRipple",     OutParam.mapType(name, "disableTouchRipple"  )("boolean"                         ), None),
    OptField("keyboardFocused",        OutParam.mapType(name, "keyboardFocused"     )("boolean"                         ), None),
    OptField("linkButton",             OutParam.mapType(name, "linkButton"          )("boolean"                         ), None),
    OptField("focusRippleColor",       OutParam.mapType(name, "focusRippleColor"    )("string"                       ), None),
    OptField("touchRippleColor",       OutParam.mapType(name, "touchRippleColor"    )("string"                       ), None),
    OptField("focusRippleOpacity",     OutParam.mapType(name, "focusRippleOpacity"  )("number"                       ), None),
    OptField("touchRippleOpacity",     OutParam.mapType(name, "touchRippleOpacity"  )("number"                       ), None),
    OptField("tabIndex",               OutParam.mapType(name, "tabIndex"            )("number"                       ), None)
  )
  val inheritEvents = List[OutField](
    OptField("onBlur",                 OutParamClass("ReactEventH => Callback"), None),
    OptField("onFocus",                OutParamClass("ReactFocusEventH => Callback"), None),
    OptField("onKeyboardFocus",        OutParamClass("ReactKeyboardEventH => Callback"), None),
    OptField("onKeyDown",              OutParamClass("ReactKeyboardEventH => Callback"), None),
    OptField("onKeyUp",                OutParamClass("ReactKeyboardEventH => Callback"), None),
    OptField("onTouchTap",             OutParamClass("ReactTouchEventH => Callback"), None)
  )
}

object SwitchSharedComponent extends SharedComponent {
  val name = "EnhancedSwitch"
  val inheritProps = List[OutField](
    OptField("id",                 OutParam.mapType(name, "id"                )("string"),                   None),
//    ReqField("inputType",          OutParam.mapType(name, "inputType"         )("string"),                   None),
//    ReqField("switchElement",      OutParam.mapType(name, "switchElement"     )("element"),                  None),
//    ReqField("switched",           OutParam.mapType(name, "switched"          )("boolean"),                     None),
    OptField("rippleStyle",        OutParam.mapType(name, "rippleStyle"       )("object"),               None),
    OptField("rippleColor",        OutParam.mapType(name, "rippleColor"       )("string"),               None),
    OptField("iconStyle",          OutParam.mapType(name, "iconStyle"         )("object"),               None),
    OptField("thumbStyle",         OutParam.mapType(name, "thumbStyle"        )("object"),               None),
    OptField("trackStyle",         OutParam.mapType(name, "trackStyle"        )("object"),               None),
    OptField("labelStyle",         OutParam.mapType(name, "labelStyle"        )("object"),               None),
    OptField("name",               OutParam.mapType(name, "name"              )("string"),               None),
    OptField("value",              OutParam.mapType(name, "value"             )("string"),               None),
    OptField("label",              OutParam.mapType(name, "label"             )("node"),                 None),
    OptField("required",           OutParam.mapType(name, "required"          )("boolean"),              None),
    OptField("disabled",           OutParam.mapType(name, "disabled"          )("boolean"),              None),
    OptField("defaultSwitched",    OutParam.mapType(name, "defaultSwitched"   )("boolean"),              None),
    OptField("labelPosition",      OutParam.mapType(name, "labelPosition"     )("oneOf [left, right]"), None),
    OptField("disableFocusRipple", OutParam.mapType(name, "disableFocusRipple")("boolean"),              None),
    OptField("disableTouchRipple", OutParam.mapType(name, "disableTouchRipple")("boolean"),              None)
  )
  val inheritEvents = List[OutField](
    OptField("onParentShouldUpdate", OutParamClass("Boolean => Callback"), None),
    OptField("onSwitch",             OutParamClass("(ReactEvent, Boolean) => Callback"), None)
  )
}

object Component {
  val nonexisting = Some("nonexisting")

  object MuiAppBar extends Component {
    override val children = true
    val name = "MuiAppBar"
    val json = """[{"name":"Props","infoArray":[{"name":"iconClassNameLeft","type":"string","header":"optional","desc":"The classname of the icon on the left of the app bar. If you are using a stylesheet for your icons, enter the class name for the icon to be used here."},{"name":"iconClassNameRight","type":"string","header":"optional","desc":"Similiar to the iconClassNameLeft prop except that it applies to the icon displayed on the right of the app bar."},{"name":"iconElementLeft","type":"element","header":"optional","desc":"The custom element to be displayed on the left side of the app bar such as an SvgIcon."},{"name":"iconElementRight","type":"element","header":"optional","desc":"Similiar to the iconElementLeft prop except that this element is displayed on the right of the app bar."},{"name":"iconStyleRight","type":"string","header":"optional","desc":"Override the inline-styles of the element displayed on the right side of the app bar."},{"name":"style","type":"object","header":"optional","desc":"Override the inline-styles of the app bar's root element."},{"name":"showMenuIconButton","type":"boolean","header":"default: true","desc":"Determines whether or not to display the Menu icon next to the title. Setting this prop to false will hide the icon."},{"name":"title","type":"node","header":"optional","desc":"The title to display on the app bar. Could be number, string, element or an array containing these types."},{"name":"zDepth","type":"oneOf [0,1,2,3,4,5]","header":"default: 1","desc":"The zDepth of the app bar. The shadow of the app bar is also dependent on this property."}]},{"name":"Events","infoArray":[{"name":"onLeftIconButtonTouchTap","header":"AppBar.onLeftIconButtonTouchTap(e)","desc":"Callback function for when the left icon is selected via a touch tap."},{"name":"onRightIconButtonTouchTap","header":"AppBar.onRightIconButtonTouchTap(e)","desc":"Callback function for when the right icon is selected via a touch tap."}]}]"""
  }
  object MuiAvatar extends Component {
    override val children = false
    val name = "MuiAvatar"
    val json = """[{"name":"Props","infoArray":[{"name":"icon","type":"element","header":"optional","desc":"This is the SvgIcon or FontIcon to be used inside the avatar."},{"name":"backgroundColor","type":"string","header":"default: grey400","desc":"The backgroundColor of the avatar. Does not apply to image avatars."},{"name":"color","type":"string","header":"default: white","desc":"The icon or letter color."},{"name":"size","type":"integer","header":"default: 40","desc":"This is the size of the avatar in pixels"},{"name":"src","type":"string","header":"optional","desc":"If passed in, this component will render an img element. Otherwise, a div will be rendered."},{"name":"style","type":"object","header":"optional","desc":"Override the inline-styles of the root element."}]}]"""
  }

  trait MuiButtons extends Component {
    override val children = true
    override val shared = Some(ButtonSharedComponent)
  }
  object MuiFlatButton extends MuiButtons {
    val name = "MuiFlatButton"
    override val json = """[{"name":"Flat Button","infoArray":[{"name":"containerElement","type":"oneOfType [string, element]","header":"default: button","desc":"This component will render a button element by default and an anchor element if linkButton is set to true. However, you can override this behavior by passing in a string or another react element into this prop. This is useful for generating link buttons with the react router link element."},{"name":"disabled","type":"boolean","header":"optional","desc":"Disables the button if set to true."},{"name":"hoverColor","type":"string","header":"optional","desc":"Override the inline hover color of the button's root element."},{"name":"label","type":"string","header":"optional","desc":"This is what will be displayed inside the button. If a label is specified, the text within the label prop will be displayed. Otherwise, the component will expect children which will then be displayed (in our example, we are nesting an <input type=\"file\" />and a span that acts as our label to be displayed.) This only applies to flat and raised buttons."},{"name":"labelStyle","type":"object","header":"optional","desc":"Override the inline-styles of the button's label element."},{"name":"labelPosition","type":"oneOf [before, after]","header":"default: \"before\"","desc":"Place label before or after the passed children"},{"name":"linkButton","type":"boolean","header":"default: false","desc":"If true, an anchor element will be generated instead of a button element."},{"name":"primary","type":"boolean","header":"default: false","desc":"If true, the button will use the primary button colors."},{"name":"secondary","type":"boolean","header":"default: false","desc":"If true, the button will use the secondary button colors."},{"name":"rippleColor","type":"string","header":"optional","desc":"Override the inline color of the button's ripple element."},{"name":"style","type":"object","header":"optional","desc":"Override the inline-styles of the button's root element."}]}]"""
    override val propsSections = Seq("Flat Button")
  }
  object MuiRaisedButton extends MuiButtons {
    val name = "MuiRaisedButton"
    override val json = """[{"name":"Raised Button","infoArray":[{"name":"containerElement","type":"oneOfType [string, element]","header":"default: button","desc":"This component will render a button element by default and an anchor element if linkButton is set to true. However, you can override this behavior by passing in a string or another react element into this prop. This is useful for generating link buttons with the react router link element."},{"name":"disabled","type":"boolean","header":"optional","desc":"Disables the button if set to true."},{"name":"fullWidth","type":"boolean","header":"optional","desc":"If true, will change the width of the button to span the full width of the parent."},{"name":"label","type":"string","header":"optional","desc":"This is what will be displayed inside the button. If a label is specified, the text within the label prop will be displayed. Otherwise, the component will expect children which will then be displayed (in our example, we are nesting an <input type=\"file\" />and a span that acts as our label to be displayed.) This only applies to flat and raised buttons."},{"name":"labelPosition","type":"oneOf [before, after]","header":"default: \"before\"","desc":"Place label before or after the passed children"},{"name":"labelStyle","type":"object","header":"optional","desc":"Override the inline-styles of the button's label element."},{"name":"linkButton","type":"boolean","header":"default: false","desc":"If true, an anchor element will be generated instead of a button element."},{"name":"primary","type":"boolean","header":"default: false","desc":"If true, the button will use the primary button colors."},{"name":"secondary","type":"boolean","header":"default: false","desc":"If true, the button will use the secondary button colors."},{"name":"backgroundColor","type":"string","header":"optional","desc":"Override the background color. Always takes precedence unless the button is disabled."},{"name":"labelColor","type":"string","header":"optional","desc":"Override the label color. Always takes precedence unless the button is disabled."},{"name":"disabledBackgroundColor","type":"string","header":"optional","desc":"Override the background color if the button is disabled."},{"name":"disabledLabelColor","type":"string","header":"optional","desc":"Override the label color if the button is disabled."},{"name":"style","type":"object","header":"optional","desc":"Override the inline-styles of the button's root element."}]}]"""
    override val propsSections = Seq("Raised Button")
  }
  object MuiFloatingActionButton extends MuiButtons {
    val name = "MuiFloatingActionButton"
    override val json = """[{"name":"Floating Action Button","infoArray":[{"name":"backgroundColor","type":"string","header":"optional","desc":"This value will override the default background color for the button. However it will not override thedefault disabled background color. This has to be set separately using the disabledColor attribute."},{"name":"containerElement","type":"oneOfType [string, element]","header":"default: button","desc":"This component will render a button element by default and an anchor element if linkButton is set to true. However, you can override this behavior by passing in a string or another react element into this prop. This is useful for generating link buttons with the react router link element."},{"name":"disabled","type":"boolean","header":"optional","desc":"Disables the button if set to true."},{"name":"disabledColor","type":"string","header":"optional","desc":"This value will override the default background color for the button when it is disabled."},{"name":"iconClassName","type":"string","header":"optional","desc":"The icon within the FloatingActionButton is a FontIcon component. This property is the classname of the icon to be displayed inside the button. An alternative to adding an iconClassName would be to manually insert a FontIcon component or custom SvgIcon component or as a child of FloatingActionButton."},{"name":"iconStyle","type":"object","header":"optional","desc":"This is the equivalent to iconClassName except that it is used for overriding the inline-styles of the FontIcon component."},{"name":"linkButton","type":"boolean","header":"default: false","desc":"If true, an anchor element will be generated instead of a button element."},{"name":"mini","type":"boolean","header":"default: false","desc":"If true, the button will be a small floating action button."},{"name":"secondary","type":"boolean","header":"default: false","desc":"If true, the button will use the secondary button colors."},{"name":"style","type":"object","header":"optional","desc":"Override the inline-styles of the button's root element."}]}]"""
    override val propsSections = Seq("Floating Action Button")
  }
  
  trait MuiCards extends Component {
    val json = """[{"name":"Card.Props","infoArray":[{"name":"initiallyExpanded","type":"boolean","header":"optional","desc":"Whether this card is initially expanded."}]},{"name":"Props","infoArray":[{"name":"expandable","type":"boolean","header":"optional","desc":"Whether this card component is expandable. Can be set on any child of the Card component."},{"name":"actAsExpander","type":"boolean","header":"optional","desc":"Whether a click on this card component expands the card. Can be set on any child of the Card component."},{"name":"showExpandableButton","type":"boolean","header":"optional","desc":"Whether this card component include a button to expand the card. CardTitle, CardHeader and CardActions implement showExpandableButton. Any child component of Card can implements showExpandableButton or forwards the property to a child component supporting it."},{"name":"style","type":"object","header":"optional","desc":"Override the inline-styles of the card's root element."}]},{"name":"Card.Events","infoArray":[{"name":"onExpandChange","type":"function(isExpanded)","header":"optional","desc":"Fired when the expandable state changes."}]}]"""
  }
  object MuiCard extends MuiCards{
    override val children = true
    override val name = "MuiCard"
    override val propsSections = Seq("Card.Props", "Props")
  }
  object MuiCardHeader extends MuiCards{
    override val children = false
    override val name = "MuiCardHeader"
  }
  object MuiCardMedia extends MuiCards{
    override val children = true
    override val name = "MuiCardMedia"
  }
  object MuiCardTitle extends MuiCards{
    override val children = false
    override val name = "MuiCardTitle"
  }
  object MuiCardActions extends MuiCards{
    override val children = true
    override val name = "MuiCardActions"
  }
  object MuiCardText extends MuiCards{
    override val children = true
    override val name = "MuiCardText"
  }

  object MuiDatePicker extends Component {
    override val children = false
    val name = "MuiDatePicker"
    val json = """[{"name":"Props","infoArray":[{"name":"DateTimeFormat","type":"func","header":"default: custom function defined inside utils/date-time.js that only supports en-US locale","desc":"Constructor for time formatting. Follow this specificaction: ECMAScript Internationalization API 1.0 (ECMA-402)."},{"name":"locale","type":"string","header":"default: en-US","desc":"Locale used for formatting date. If you are not using the default value, you have to provide a DateTimeFormat that supports it. You can use Intl.DateTimeFormat if it's supported by your environment. https://github.com/andyearnshaw/Intl.js is a good polyfill."},{"name":"wordings","type":"object","header":"default: {ok: 'OK', cancel: 'Cancel' }","desc":"Wordings used inside the button of the dialog."},{"name":"autoOk","type":"boolean","header":"default: false","desc":"If true, automatically accept and close the picker on select a date."},{"name":"defaultDate","type":"date","header":"optional","desc":"This is the initial date value of the component. If either `value` or `valueLink` is provided they will override this prop with `value` taking precedence."},{"name":"disableYearSelection","type":"boolean","header":"optional","desc":"If true, year selection will be disabled, otherwise, year selection will be enabled."},{"name":"floatingLabelText","type":"string","header":"optional","desc":"The text string to use for the floating label element."},{"name":"formatDate","type":"function","header":"default: formats to M/D/YYYY","desc":"This function is called to format the date to display in the input box. By default, date objects are formatted to M/D/YYYY."},{"name":"hintText","type":"string","header":"optional","desc":"The hint text string to display. Note, floatingLabelText will overide this."},{"name":"maxDate","type":"date","header":"optional","desc":"The ending of a range of valid dates. The range includes the endDate. The default value is current date + 100 years."},{"name":"minDate","type":"date","header":"optional","desc":"The beginning of a range of valid dates. The range includes the startDate. The default value is current date - 100 years."},{"name":"mode","type":"oneOf [portrait, landscape]","header":"default: portrait","desc":"Tells the component to display the picker in portrait or landscape mode."},{"name":"shouldDisableDate","type":"function","header":"optional","desc":"Called during render time of a given day. If this method returns false the day is disabled otherwise it is displayed normally."},{"name":"style","type":"object","header":"optional","desc":"Override the inline-styles of DatePicker's root element."},{"name":"textFieldStyle","type":"object","header":"optional","desc":"Override the inline-styles of DatePicker's TextField element."}]},{"name":"Methods","infoArray":[{"name":"getDate","header":"DatePicker.getDate()","desc":"Returns the current date value."},{"name":"setDate","header":"DatePicker.setDate(d)","desc":"Sets the date value to d, where d is a date object."},{"name":"openDialog","header":"DatePicker.openDialog()","desc":"Opens the date-picker dialog programmatically. Use this if you want to open the dialog in response to some event other than focus/tap on the input field, such as an external button click."},{"name":"focus","header":"DatePicker.focus()","desc":"An alias for the `openDialog()` method to allow more generic use alongside `TextField`."}]},{"name":"Events","infoArray":[{"name":"onChange","header":"function(null, date)","desc":"Callback function that is fired when the date value changes. Since there is no particular event associated with the change the first argument will always be null and the second argument will be the new Date instance."},{"name":"onDismiss","header":"function()","desc":"Fired when the datepicker dialog is dismissed."},{"name":"onFocus","header":"function(event)","desc":"Callback function that is fired when the datepicker field gains focus."},{"name":"onShow","header":"function()","desc":"Fired when the datepicker dialog is shown."}]}]"""
    override val postlude: Option[String] = Some(
      """
        |case class Wordings(ok: String, cancel: String){
        |  val toJS = JSMacro[Wordings](this)
        |}
        |
      """.stripMargin)
  }
  object MuiDialog extends Component {
    override val children = true
    val name = "MuiDialog"
    val json = """[{"name":"Props","infoArray":[{"name":"actions","type":"array","header":"optional","desc":"This prop can be either a JSON object containing the actions to render, or an array of react objects."},{"name":"actionFocus","type":"string","header":"optional","desc":"The ref of the action to focus on when the dialog is displayed."},{"name":"style","type":"object","header":"optional","desc":"Override the inline-styles of the dialog's root element."},{"name":"bodyStyle","type":"object","header":"optional","desc":"Overrides the inline-styles of the dialog container under the title."},{"name":"contentClassName","type":"string","header":"optional","desc":"The className to add to the dialog window content container. This is the Paper element that is seen when the dialog is shown."},{"name":"contentStyle","type":"object","header":"optional","desc":"Overrides the inline-styles of the dialog window content container."},{"name":"modal","type":"boolean","header":"default: false","desc":"Force the user to use one of the actions in the dialog. Clicking outside the dialog will not dismiss the dialog."},{"name":"openImmediately","type":"boolean","header":"default: false","desc":"Deprecated: Set to true to have the dialog automatically open on mount."},{"name":"defaultIsOpen","type":"boolean","header":"default: false","desc":"Set to true to have the dialog automatically open on mount."},{"name":"isOpen","type":"boolean","header":"default: null","desc":"Controls whether the Dialog is opened or not."},{"name":"title","type":"node","header":"optional","desc":"The title to display on the dialog. Could be number, string, element or an array containing these types."},{"name":"autoDetectWindowHeight","type":"boolean","header":"default: true","desc":"If set to true, the height of the dialog will be auto detected. A max height will be enforced so that the content does not extend beyond the viewport."},{"name":"autoScrollBodyContent","type":"boolean","header":"default: false","desc":"If set to true, the body content of the dialog will be scrollable."}]},{"name":"Methods","infoArray":[{"name":"isOpen","header":"Dialog.isOpen()","desc":"Get the dialog open state."}]},{"name":"Events","infoArray":[{"name":"onRequestClose","header":"function(buttonClicked)","desc":"Fired when the dialog is requested to be closed by a click outside the dialog or on the buttons."}]}]"""
  }
  object MuiDropDownMenu extends Component {
    override val children = false
    val name = "MuiDropDownMenu"
    val json = """[{"name":"Props","infoArray":[{"name":"displayMember","type":"string","header":"default: text","desc":"DropDownMenu will use text as default value, with this property you can choose another name."},{"name":"valueMember","type":"string","header":"default: payload","desc":"DropDownMenu will use payload as default value, with this property you can choose another name."},{"name":"autoWidth","type":"boolean","header":"default: true","desc":"The width will automatically be set according to the items inside the menu. To control this width in Css instead, set this prop to false."},{"name":"menuItems","type":"array","header":"required","desc":"JSON data representing all menu items in the dropdown."},{"name":"menuItemStyle","type":"object","header":"optional","desc":"Overrides the inline-styles of the MenuItems when the DropDownMenu is expanded."},{"name":"selectedIndex","type":"integer","header":"default: 0","desc":"Index of the item selected."},{"name":"underlineStyle","type":"object","header":"optional","desc":"Overrides the styles of DropDownMenu's underline."},{"name":"iconStyle","type":"object","header":"optional","desc":"Overrides the styles of DropDownMenu's icon element."},{"name":"labelStyle","type":"object","header":"optional","desc":"Overrides the styles of DropDownMenu's label when the DropDownMenu is inactive."},{"name":"style","type":"object","header":"optional","desc":"Overrides the inline-styles of DropDownMenu's root element."},{"name":"disabled","type":"boolean","header":"default: false","desc":"Disables the menu."},{"name":"openImmediately","type":"boolean","header":"default: false","desc":"Set to true to have the DropDownMenu automatically open on mount."}]},{"name":"Events","infoArray":[{"name":"onChange","header":"function(event, selectedIndex, menuItem)","desc":"Fired when a menu item is clicked that is not the one currently selected."}]}]"""
    override val postlude: Option[String] = Some(
      """
        |case class MuiDropDownMenuItem(payload: String, text: String) {
        |  val toJS = JSMacro[MuiDropDownMenuItem](this)
        |}
        |
        |object MuiDropDownMenuItem {
        |  def fromJson(obj: js.Dynamic) =
        |    MuiDropDownMenuItem(text = obj.text.toString, payload = obj.payload.toString)
        |}
      """.stripMargin)
  }
  object MuiFontIcon extends Component {
    override val children = false
    override val name = "MuiFontIcon"
    override val json = """[{"name":"Properties","infoArray":[{"name":"color","type":"string","header":"optional","desc":"This is the fill color of the svg icon. If not specified, this component will default to muiTheme.palette.textColor."},{"name":"hoverColor","type":"string","header":"optional","desc":"This is the icon color when the mouse hovers over the icon."},{"name":"style","type":"object","header":"optional","desc":"Override the inline-styles of the svg icon's root element."}]}]"""
    override val propsSections = Seq("Properties")
  }

  trait MuiGrid extends Component {
    override val children = true
    val json = """[{"name":"GridList Props","infoArray":[{"name":"cols","type":"integer","header":"optional","desc":"Number of columns. Defaults to 2."},{"name":"padding","type":"integer","header":"optional","desc":"Number of px for the padding/spacing between items. Defaults to 4."},{"name":"cellHeight","type":"integer","header":"optional","desc":"Number of px for one cell height. Defaults to 180."},{"name":"style","type":"object","header":"optional","desc":"Override the inline-styles of the grid list's root element."}]},{"name":"GridTile Props","infoArray":[{"name":"title","type":"node","header":"optional","desc":"Title to be displayed on tile."},{"name":"subtitle","type":"node","header":"optional","desc":"String or element serving as subtitle (support text)."},{"name":"titlePosition","type":"oneOf [top, bottom]","header":"optional","desc":"Position of the title bar (container of title, subtitle and action icon). Defaults to \"bottom\"."},{"name":"titleBackground","type":"string","header":"optional","desc":"Style used for title bar background. Defaults to \"rgba(0, 0, 0, 0.4)\". Useful for setting custom gradients for example"},{"name":"actionIcon","type":"element","header":"optional","desc":"An IconButton element to be used as secondary action target (primary action target is the tile itself)."},{"name":"actionPosition","type":"oneOf [left, right]","header":"optional","desc":"Position of secondary action IconButton. Defaults to \"right\"."},{"name":"cols","type":"integer","header":"optional","desc":"Width of the tile in number of grid cells. Defaults to 1."},{"name":"rows","type":"integer","header":"optional","desc":"Height of the tile in number of grid cells. Defaults to 1."},{"name":"style","type":"object","header":"optional","desc":"Override the inline-styles of the grid tile's root element."},{"name":"rootClass","type":"string|ReactComponent","header":"optional","desc":"Either a string used as tag name for the tile root element, or a ReactComponent. Defaults to \"div\".This is useful when you have, for example, a custom implementation of a navigation link (that knowsabout your routes) and you want to use it as primary tile action. In case you pass a ReactComponent, please make sure that it passes all props, accepts styles overrides and render it's children."},{"name":"children","type":"node","header":"required","desc":"Theoretically you can pass any node as children, but the main use case is to pass an img, in whichcase GridTile takes care of making the image \"cover\" available space (similar to background-size: cover or to object-fit:cover)"}]}]"""
  }
  object MuiGridList extends MuiGrid {
    val name = "MuiGridList"
    override val propsSections = Seq("GridList Props")
  }
  object MuiGridTile extends MuiGrid {
    val name = "MuiGridTile"
    override val propsSections = Seq("GridTile Props")
  }
  val iconJson = """[{"name":"Properties","infoArray":[{"name":"color","type":"string","header":"optional","desc":"This is the fill color of the svg icon. If not specified, this component will default to muiTheme.palette.textColor."},{"name":"hoverColor","type":"string","header":"optional","desc":"This is the icon color when the mouse hovers over the icon."},{"name":"style","type":"object","header":"optional","desc":"Override the inline-styles of the svg icon's root element."}]}]"""

  object MuiIconButton extends Component {
    override val children = true
    val name = "MuiIconButton"
    val json = """[{"name":"Props","infoArray":[{"name":"iconClassName","type":"string","header":"optional","desc":"If you are using a stylesheet for your icons, enter the class name for the icon to be used here."},{"name":"iconStyle","type":"object","header":"optional","desc":"Overrides the inline-styles of the icon element."},{"name":"style","type":"object","header":"optional","desc":"Override the inline-styles of the button's root element."},{"name":"tooltip","type":"string","header":"optional","desc":"The tooltip text to show."},{"name":"tooltipPosition","type":"string","header":"default: bottom-center","desc":"Allows the tooltip to be viewed with different alignments: \"bottom-center\", \"top-center\", \"bottom-right\", \"top-right\", \"bottom-left\" and \"top-left\""},{"name":"tooltipStyles","type":"object","header":"optional","desc":"Allows modification of tooltip styles."},{"name":"touch","type":"boolean","header":"default: false","desc":"If true, this component will render the touch sized tooltip."}]},{"name":"Events","infoArray":[{"name":"onBlur","header":"IconButton.onBlur(e)","desc":"Callback function for when the component loses focus."},{"name":"onFocus","header":"IconButton.onFocus(e)","desc":"Callback function for when the component gains focus."}]}]""" }

  object MuiIconMenu extends Component {
    override val children = true
    val name = "MuiIconMenu"
    val json = """[{"name":"Props","infoArray":[{"name":"closeOnItemTouchTap","type":"boolean","header":"default: true","desc":"If true, menu will close after an item is touchTapped."},{"name":"desktop","type":"boolean","header":"default: false","desc":"Indicates if the menu should render with compact desktop styles."},{"name":"style","type":"object","header":"optional","desc":"Override the inline-styles of the icon menu's root element."},{"name":"iconStyle","type":"object","header":"optional","desc":"The style object to use to override underlying icon style."},{"name":"iconButtonElement","type":"element: IconButton","header":"required","desc":"This is the IconButton to render. This button will open the menu."},{"name":"openDirection","type":"oneOf [bottom-left, bottom-right, top-left, top-right]","header":"default: bottom-left","desc":"This is the placement of the menu relative to the IconButton."},{"name":"menuStyle","type":"object","header":"optional","desc":"The style object to use to override underlying menu style."},{"name":"multiple","type":"boolean","header":"default: false","desc":"If true, the value can an array and allow the menu to be a multi-select."},{"name":"value","type":"oneOfType [string, array]","header":"optional","desc":"The value of the selected menu item. If passed in, this will make the menu a controlled component. This component also supports valueLink."},{"name":"width","type":"oneOfType [string, number]","header":"optional","desc":"Sets the width of the menu. If not specified, the menu width will be dictated by its children. The rendered width will always be a keyline increment (64px for desktop, 56px otherwise)."},{"name":"touchTapCloseDelay","type":"integer","header":"default: 200","desc":"Sets the delay in milliseconds before closing the menu when an item is clicked."}]},{"name":"Events","infoArray":[{"name":"onItemTouchTap","header":"function(event, item)","desc":"Fired when a menu item is touchTapped."},{"name":"onChange","header":"function(event, value)","desc":"Fired when a menu item is touchTapped and the menu item value is not equal to the current menu value."}]}]"""
  }
  object MuiLeftNav extends Component {
    override val children = true
    val name = "MuiLeftNav"
    val json = """[{"name":"Props","infoArray":[{"name":"disableSwipeToOpen","type":"boolean","header":"default: false","desc":"Indicates whether swiping sideways when the nav is closed should open the nav."},{"name":"docked","type":"boolean","header":"default: true","desc":"Indicates that the left nav should be docked. In this state, the overlay won't show and clicking on a menu item will not close the left nav."},{"name":"header","type":"element","header":"optional","desc":"A react component that will be displayed above all the menu items. Usually, this is used for a logo or a profile image."},{"name":"menuItems","type":"array","header":"optional","desc":"JSON data representing all menu items to render."},{"name":"openRight","type":"boolean","header":"default: false","desc":"Positions the LeftNav to open from the right side."},{"name":"selectedIndex","type":"integer","header":"optional","desc":"Indicates the particular item in the menuItems array that is currently selected."},{"name":"style","type":"object","header":"optional","desc":"Override the inline-styles of LeftNav's root element."},{"name":"menuItemClassName","type":"string","header":"optional","desc":"Class name for the menuItem."},{"name":"menuItemClassNameSubheader","type":"string","header":"optional","desc":"Class name for the subheader menuItem."},{"name":"menuItemClassNameLink","type":"string","header":"optional","desc":"Class name for the link menuItem."}]},{"name":"Methods","infoArray":[{"name":"close","header":"LeftNav.close()","desc":"Closes the component, hiding it from view."},{"name":"toggle","header":"LeftNav.toggle()","desc":"Toggles between the open and closed states."}]},{"name":"Events","infoArray":[{"name":"onChange","header":"function(event, selectedIndex, menuItem)","desc":"Fired when a menu item is clicked that is not the one currently selected. Note that this requires the injectTapEventPlugin component. See the \"Get Started\" section for more detail."},{"name":"onNavOpen","header":"function()","desc":"Fired when the component is opened"},{"name":"onNavClose","header":"function()","desc":"Fired when the component is closed"}]}]"""
  }

  trait MuiLists extends Component {
    val json = """[{"name":"List Props","infoArray":[{"name":"insetSubheader","type":"boolean","header":"default: false","desc":"If true, the subheader will be indented by 72px."},{"name":"style","type":"object","header":"optional","desc":"Override the inline-styles of the list's root element."},{"name":"subheader","type":"node","header":"optional","desc":"The subheader string that will be displayed at the top of the list."},{"name":"subheaderStyle","type":"object","header":"optional","desc":"The style object to override subheader styles."}]},{"name":"ListItem Props","infoArray":[{"name":"autoGenerateNestedIndicator","type":"boolean","header":"default: true","desc":"Generate a nested list indicator icon when nested list items are detected. Set to false if you do not want an indicator auto-generated. Note that an indicator will not be created if a rightIcon/Button has been specified."},{"name":"disabled","type":"boolean","header":"default: false","desc":"If true, the list-item will not be clickable and will not display hover affects. This is automatically disabled if leftCheckbox or rightToggle is set."},{"name":"insetChildren","type":"boolean","header":"default: false","desc":"If true, the children will be indented by 72px. Only needed if there is no left avatar or left icon."},{"name":"leftAvatar","type":"element","header":"optional","desc":"This is the Avatar element to be displayed on the left side."},{"name":"leftCheckbox","type":"element","header":"optional","desc":"This is the Checkbox element to be displayed on the left side."},{"name":"leftIcon","type":"element","header":"optional","desc":"This is the SvgIcon or FontIcon to be displayed on the left side."},{"name":"nestedItems","type":"Array of elements","header":"optional","desc":"An array of ListItems to nest underneath the current ListItem."},{"name":"nestedLevel","type":"integer","header":"optional","desc":"Controls how deep a ListItem appears. This property is automatically managed so modify at your own risk."},{"name":"initiallyOpen","type":"boolean","header":"default: false","desc":"Controls whether or not the child ListItems are initially displayed."},{"name":"primaryText","type":"node","header":"optional","desc":"This is the block element that contains the primary text. If a string is passed in, a div tag will be rendered."},{"name":"rightAvatar","type":"element","header":"optional","desc":"This is the avatar element to be displayed on the right side."},{"name":"rightIcon","type":"element","header":"optional","desc":"This is the SvgIcon or FontIcon to be displayed on the right side."},{"name":"rightIconButton","type":"element","header":"optional","desc":"This is the IconButton to be displayed on the right side. Hovering over this button will remove the ListItem hover. Also, clicking on this button will not trigger a ListItem ripple. The event will be stopped and prevented from bubbling up to cause a ListItem click."},{"name":"rightToggle","type":"element","header":"optional","desc":"This is the Toggle element to display on the right side."},{"name":"secondaryText","type":"node","header":"optional","desc":"This is the block element that contains the secondary text. If a string is passed in, a div tag will be rendered."},{"name":"secondaryTextLines","type":"oneOf [1,2]","header":"default: 1","desc":"Can be 1 or 2. This is the number of secondary text lines before ellipsis will show."},{"name":"style","type":"object","header":"optional","desc":"Override the inline-styles of the list item's root element."}]},{"name":"ListItem Events","infoArray":[{"name":"onKeyboardFocus","type":"function(event, isKeyboardFocused)","header":"optional","desc":"Called when the ListItem has keyboard focus."},{"name":"onMouseLeave","type":"function(event)","header":"optional","desc":"Called when the mouse is no longer over the ListItem."},{"name":"onMouseEnter","type":"function(event)","header":"optional","desc":"Called when the mouse is over the ListItem."},{"name":"onNestedListToggle","type":"function(this)","header":"optional","desc":"Called when the ListItem toggles its nested ListItems."},{"name":"onTouchStart","type":"function(event)","header":"optional","desc":"Called when touches start."},{"name":"onTouchTap","type":"function(event)","header":"optional","desc":"Called when a touch tap event occures on the component."}]}]"""
  }

  object MuiList extends MuiLists {
    override val children = true
    val name = "MuiList"
    override val propsSections = Seq("List Props")
  }

  object MuiListDivider extends MuiLists {
    override val children = false
    val name = "MuiListDivider"
    override val propsSections = Nil
  }
  object MuiListItem extends MuiLists {
    override val children = true
    val name = "MuiListItem"
    override val propsSections = Seq("ListItem Props")
    override val overrideEvents = Some("ListItem Events")
  }

  trait MuiMenus extends Component {
    val json = """[{"name":"Menu Props","infoArray":[{"name":"animated","type":"boolean","header":"default: false","desc":"If true, the menu will apply transitions when added it gets added to the DOM. In order for transitions to work, wrap the menu inside a ReactTransitionGroup."},{"name":"autoWidth","type":"boolean","header":"default: true","desc":"If true, the width will automatically be set according to the items inside the menu using the proper keyline increment."},{"name":"desktop","type":"boolean","header":"default: false","desc":"Indicates if the menu should render with compact desktop styles."},{"name":"style","type":"object","header":"optional","desc":"Override the inline-styles of the menu's root element."},{"name":"listStyle","type":"object","header":"optional","desc":"The style object to use to override underlying list style."},{"name":"maxHeight","type":"integer","header":"optional","desc":"The maxHeight of the menu in pixels. If specified, the menu will scroll if larger than the maxHeight."},{"name":"menuItems","type":"array","header":"optional","desc":"JSON data representing all menu items to render."},{"name":"multiple","type":"boolean","header":"default: false","desc":"If true, the value can an array and allow the menu to be a multi-select."},{"name":"openDirection","type":"oneOf [bottom-left, bottom-right, top-left, top-right]","header":"default: bottom-left","desc":"This is the placement of the menu relative to the IconButton."},{"name":"value","type":"oneOfType [string, array]","header":"optional","desc":"The value of the selected menu item. If passed in, this will make the menu a controlled component. This component also supports valueLink."},{"name":"width","type":"oneOfType [string, number]","header":"optional","desc":"Sets the width of the menu. If not specified, the menu width will be dictated by its children. The rendered width will always be a keyline increment (64px for desktop, 56px otherwise)."},{"name":"zDepth","type":"oneOf [0,1,2,3,4,5]","header":"optional","desc":"Sets the width of the menu. If not specified, the menu width will be dictated by its children. The rendered width will always be a keyline increment (64px for desktop, 56px otherwise)."}]},{"name":"MenuItem Props","infoArray":[{"name":"checked","type":"boolean","header":"default: false","desc":"If true, a left check mark will be rendered"},{"name":"desktop","type":"boolean","header":"default: false","desc":"Indicates if the menu should render with compact desktop styles."},{"name":"disabled","type":"boolean","header":"default: false","desc":"Disables a menu item."},{"name":"style","type":"object","header":"optional","desc":"Override the inline-styles of the menu item's root element."},{"name":"innerDivStyle","type":"object","header":"optional","desc":"Style overrides for the inner div."},{"name":"insetChildren","type":"boolean","header":"default: false","desc":"If true, the children will be indented. Only needed when there is no leftIcon."},{"name":"leftIcon","type":"element","header":"optional","desc":"This is the SvgIcon or FontIcon to be displayed on the left side."},{"name":"primaryText","type":"node","header":"optional","desc":"This is the block element that contains the primary text. If a string is passed in, a div tag will be rendered."},{"name":"rightIcon","type":"element","header":"optional","desc":"This is the SvgIcon or FontIcon to be displayed on the right side."},{"name":"secondaryText","type":"node","header":"optional","desc":"This is the block element that contains the secondary text. If a string is passed in, a div tag will be rendered."},{"name":"value","type":"string","header":"optional","desc":"The value of the menu item."}]},{"name":"Events","infoArray":[{"name":"onEscKeyDown","header":"function(event)","desc":"Fired when an Esc key is keyed down."},{"name":"onItemTouchTap","header":"function(event, item)","desc":"Fired when a menu item is touchTapped."},{"name":"onChange","header":"function(event, value)","desc":"Fired when a menu item is touchTapped and the menu item value is not equal to the current menu value."}]}]"""
  }
  object MuiMenu extends MuiMenus {
    override val children = true
    val name = "MuiMenu"
    override val propsSections = Seq("Menu Props")
  }
  object MuiMenuDivider extends MuiMenus {
    override val children = false
    val name = "MuiMenuDivider"
    override val propsSections = Nil
  }
  object MuiMenuItem extends MuiMenus {
    override val children = false
    val name = "MuiMenuItem"
    override val propsSections = Seq("MenuItem Props")
    override val overrideEvents = nonexisting
  }

  object MuiPaper extends Component {
    override val children = true
    val name = "MuiPaper"
    val json = """[{"name":"Props","infoArray":[{"name":"circle","type":"boolean","header":"default: false","desc":"Set to true to generate a circlular paper container."},{"name":"rounded","type":"boolean","header":"default: true","desc":"By default, the paper container will have a border radius. Set this to false to generate a container with sharp corners."},{"name":"style","type":"object","header":"optional","desc":"Override the inline-styles of Paper's root element."},{"name":"zDepth","type":"oneOf [0, 1, 2, 3, 4, 5]","header":"default: 1","desc":"This number represents the zDepth of the paper shadow."},{"name":"transitionEnabled","type":"boolean","header":"default: true","desc":"Set to false to disable CSS transitions for the paper element."}]}]"""
  }

  trait MuiProgress extends Component {
    override val children = false
    val json = """[{"name":"Props","infoArray":[{"name":"mode","type":"oneOf [determinate, indeterminate]","header":"default: indeterminate","desc":"The mode of show your progress, indeterminate for when there is no value for progress. "},{"name":"value","type":"number","header":"default: 0","desc":"The value of progress, only works in determinate mode. "},{"name":"max","type":"number","header":"default: 100","desc":"The max value of progress, only works in determinate mode. "},{"name":"min","type":"number","header":"default: 0","desc":"The min value of progress, only works in determinate mode. "},{"name":"size","type":"number","header":"default: 1","desc":"The size of the progress."},{"name":"style","type":"object","header":"optional","desc":"Override the inline-styles of the progress's root element."}]}]"""
  }

  object MuiLinearProgress extends MuiProgress {
    val name = "MuiLinearProgress"
  }

  object MuiCircularProgress extends MuiProgress {
    val name = "MuiCircularProgress"
  }

  object MuiRefreshIndicator extends Component {
    override val children = false
    val name = "MuiRefreshIndicator"
    val json = """[{"name":"Props","infoArray":[{"name":"left","type":"integer","header":"required","desc":"The absolute left position of the indicator in pixels."},{"name":"percentage","type":"number","header":"default: 0","desc":"The confirmation progress to fetch data. Max value is 100"},{"name":"size","type":"integer","header":"default: 40","desc":"Size in pixels."},{"name":"status","type":"oneOf [ready, loading, hide]","header":"default: hide","desc":"The display status of the indicator. If the status is \"ready\", the indicator will display the ready state arrow. If the status is \"loading\", it will display the loading progress indicator. If the status is \"hide\", the indicator will be hidden."},{"name":"style","type":"object","header":"optional","desc":"Override the inline-styles of the indicator's root element."},{"name":"top","type":"integer","header":"required","desc":"The absolute right position of the indicator in pixels."}]}]"""
  }

  object MuiSlider extends Component {
    override val children = false
    val name = "MuiSlider"
    val json = """[{"name":"Props","infoArray":[{"name":"name","type":"string","header":"required","desc":"The name of the slider. Behaves like the name attribute of an input element."},{"name":"defaultValue","type":"number","header":"default: 0","desc":"The default value of the slider."},{"name":"description","type":"string","header":"optional","desc":"Describe the slider."},{"name":"disabled","type":"boolean","header":"default: false","desc":"If true, the slider will not be interactable."},{"name":"error","type":"string","header":"optional","desc":"An error message for the slider."},{"name":"max","type":"number","header":"default: 1","desc":"The maximum value the slider can slide to on a scale from 0 to 1 inclusive. Cannot be equal to min."},{"name":"min","type":"number","header":"default: 0","desc":"The minimum value the slider can slide to on a scale from 0 to 1 inclusive. Cannot be equal to max."},{"name":"required","type":"boolean","header":"default: true","desc":"Whether or not the slider is required in a form."},{"name":"step","type":"number","header":"default: 0.01","desc":"The granularity the slider can step through values."},{"name":"style","type":"object","header":"optional","desc":"Override the inline-styles of the Slider's root element."},{"name":"value","type":"number","header":"optional","desc":"The value of the slider."}]},{"name":"Events","infoArray":[{"name":"onBlur","type":"function(event)","header":"optional","desc":"Callback function that is fired when the focus has left the slider."},{"name":"onChange","type":"function(event, value)","header":"optional","desc":"Callback function that is fired when the user changes the slider's value."},{"name":"onDragStart","type":"function(event)","header":"optional","desc":"Callback function that is fired when the slider has begun to move."},{"name":"onDragStop","type":"function(event)","header":"optional","desc":"Callback function that is fried when teh slide has stopped moving."},{"name":"onFocus","type":"function(event)","header":"optional","desc":"Callback fired when the user has focused on the slider."}]}]"""
  }

  object MuiSnackbar extends Component {
    override val children = false
    val name = "MuiSnackbar"
    val json = """[{"name":"Props","infoArray":[{"name":"action","type":"string","header":"optional","desc":"The name of the action on the snackbar."},{"name":"autoHideDuration","type":"integer","header":"optional","desc":"The number of milliseconds to wait before automatically dismissing. If no value is specified the snackbar will dismiss normally. If a value is provided the snackbar can still be dismissed normally. If a snackbar is dismissed before the timer expires, the timer will be cleared."},{"name":"message","type":"string","header":"required","desc":"The message to be displayed on the snackbar."},{"name":"openOnMount","type":"boolean","header":"default: false","desc":"If true, the snackbar will open once mounted."},{"name":"style","type":"object","header":"optional","desc":"Override the inline-styles of the Snackbar's root element."}]},{"name":"Methods","infoArray":[{"name":"dismiss","header":"Snackbar.dismiss()","desc":"Hides the snackbar."},{"name":"show","header":"Snackbar.show()","desc":"Shows the snackbar."}]},{"name":"Events","infoArray":[{"name":"onActionTouchTap","header":"function(event)","desc":"Fired when the action button is touchtapped."},{"name":"onDismiss","header":"function()","desc":"Fired when the snackbar is dismissed."},{"name":"onShow","header":"function()","desc":"Fired when the snackbar is shown."}]}]"""
  }

  trait MuiSwitches extends Component {
    override val children: Boolean = false
  }
  object MuiCheckbox extends MuiSwitches {
    val name = "MuiCheckbox"
    override val json = """[{"name":"Checkbox Props","infoArray":[{"name":"checkedIcon","type":"element","header":"optional","desc":"The SvgIcon to use for the checked state. This is useful to create icon toggles."},{"name":"defaultChecked","type":"boolean","header":"default:false","desc":"The default state of our checkbox component."},{"name":"iconStyle","type":"object","header":"optional","desc":"Overrides the inline-styles of the icon element."},{"name":"label","type":"node","header":"optional","desc":"The text that is displayed beside the checkbox."},{"name":"labelStyle","type":"object","header":"optional","desc":"Overrides the inline-styles of the Checkbox element label."},{"name":"labelPosition","type":"oneOf [left, right]","header":"default:\"right\"","desc":"Where the label will be placed next to the checkbox. Options include \"left\" and \"right\" (case-sensitive). Default option is \"right\"."},{"name":"style","type":"object","header":"optional","desc":"Override the inline-styles of the Checkbox's root element."},{"name":"unCheckedIcon","type":"element","header":"optional","desc":"The SvgIcon to use for the unchecked state. This is useful to create icon toggles."}]},{"name":"Checkbox Methods","infoArray":[{"name":"isChecked","header":"Checkbox.isChecked()","desc":"Returns true if the checkbox is currently checked. Returns false otherwise"},{"name":"setChecked","header":"Checkbox.setChecked(newCheckedValue)","desc":"Sets the checkbox to the value of newCheckedValue. This method cannot be used while \"checked\" is defined as a property."}]},{"name":"Checkbox Events","infoArray":[{"name":"onCheck","type":"function(event, checked)","header":"optional","desc":"Callback function that is fired when the checkbox is checked."}]}]"""
    override val propsSections   = Seq("Checkbox Props")
    override val overrideMethods = Some("Checkbox Methods")
    override val overrideEvents  = Some("Checkbox Events")
    override val shared          = Some(SwitchSharedComponent)
  }
  object MuiRadioButton extends MuiSwitches {
    val name = "MuiRadioButton"
    override val json = """[{"name":"Radio Button Props","infoArray":[{"name":"defaultChecked","type":"boolean","header":"default:false","desc":"The default value of the radio button when the page finishes loading."},{"name":"iconStyle","type":"object","header":"optional","desc":"Overrides the inline-styles of the icon element."},{"name":"label","type":"node","header":"optional","desc":"The text that is displayed beside the radio button."},{"name":"labelStyle","type":"object","header":"optional","desc":"Overrides the inline-styles of the RadioButton element label."},{"name":"labelPosition","type":"oneOf [left, right]","header":"default:\"right\"","desc":"Where the label will be placed next to the radio button. Options include \"left\" and \"right\" (case-sensitive). Default option is \"right\"."},{"name":"style","type":"object","header":"optional","desc":"Override the inline-styles of the RadioButton's root element."},{"name":"value","type":"string","header":"optional","desc":"The value of our radio button component."}]},{"name":"Radio Button Group","infoArray":[{"name":"defaultSelected","type":"string","header":"optional","desc":"Sets the default radio button to be the one whose value matches defaultSelected (case-sensitive). This will override any individual radio button with the defaultChecked or checked property stated."},{"name":"labelPosition","type":"oneOf [left, right]","header":"optional","desc":"Where the label will be placed for all radio buttons. Options include \"left\" and \"right\" (case-sensitive). This will override any labelPosition properties defined for an individual radio button."},{"name":"name","type":"string","header":"required","desc":"The name that will be applied to all radio buttons inside it."},{"name":"style","type":"object","header":"optional","desc":"Override the inline-styles of the RadioButtonGroup's root element."},{"name":"valueSelected","type":"string","header":"optional","desc":"The value of the currently selected radio button."}]},{"name":"Radio Button Group Methods","infoArray":[{"name":"getSelectedValue","header":"RadioButtonGroup.getSelectedValue()","desc":"Returns the string value of the radio button that is currently selected. If nothing has been selected, an empty string is returned."},{"name":"setSelectedValue","header":"RadioButtonGroup.setSelectedValue(newSelectionValue)","desc":"Sets the selected radio button to the radio button whose value matches newSelectionValue"},{"name":"clearValue","header":"RadioButtonGroup.clearValue()","desc":"Clears the selected value for the radio button group."}]},{"name":"Radio Button Group Events","infoArray":[{"name":"onChange","type":"function(event, selected)","header":"optional","desc":"Callback function that is fired when a radio button has been clicked. Returns the event and the value of the radio button that has been selected."}]}]"""
    override val propsSections   = Seq("Radio Button Props")
    override val overrideMethods = nonexisting
    override val overrideEvents  = nonexisting
    override val shared          = Some(SwitchSharedComponent)
  }
  object MuiRadioButtonGroup extends MuiSwitches {
    override val children: Boolean = true
    val name = "MuiRadioButtonGroup"
    override val json = """[{"name":"Radio Button Props","infoArray":[{"name":"defaultChecked","type":"boolean","header":"default:false","desc":"The default value of the radio button when the page finishes loading."},{"name":"iconStyle","type":"object","header":"optional","desc":"Overrides the inline-styles of the icon element."},{"name":"label","type":"node","header":"optional","desc":"The text that is displayed beside the radio button."},{"name":"labelStyle","type":"object","header":"optional","desc":"Overrides the inline-styles of the RadioButton element label."},{"name":"labelPosition","type":"oneOf [left, right]","header":"default:\"right\"","desc":"Where the label will be placed next to the radio button. Options include \"left\" and \"right\" (case-sensitive). Default option is \"right\"."},{"name":"style","type":"object","header":"optional","desc":"Override the inline-styles of the RadioButton's root element."},{"name":"value","type":"string","header":"optional","desc":"The value of our radio button component."}]},{"name":"Radio Button Group","infoArray":[{"name":"defaultSelected","type":"string","header":"optional","desc":"Sets the default radio button to be the one whose value matches defaultSelected (case-sensitive). This will override any individual radio button with the defaultChecked or checked property stated."},{"name":"labelPosition","type":"oneOf [left, right]","header":"optional","desc":"Where the label will be placed for all radio buttons. Options include \"left\" and \"right\" (case-sensitive). This will override any labelPosition properties defined for an individual radio button."},{"name":"name","type":"string","header":"required","desc":"The name that will be applied to all radio buttons inside it."},{"name":"style","type":"object","header":"optional","desc":"Override the inline-styles of the RadioButtonGroup's root element."},{"name":"valueSelected","type":"string","header":"optional","desc":"The value of the currently selected radio button."}]},{"name":"Radio Button Group Methods","infoArray":[{"name":"getSelectedValue","header":"RadioButtonGroup.getSelectedValue()","desc":"Returns the string value of the radio button that is currently selected. If nothing has been selected, an empty string is returned."},{"name":"setSelectedValue","header":"RadioButtonGroup.setSelectedValue(newSelectionValue)","desc":"Sets the selected radio button to the radio button whose value matches newSelectionValue"},{"name":"clearValue","header":"RadioButtonGroup.clearValue()","desc":"Clears the selected value for the radio button group."}]},{"name":"Radio Button Group Events","infoArray":[{"name":"onChange","type":"function(event, selected)","header":"optional","desc":"Callback function that is fired when a radio button has been clicked. Returns the event and the value of the radio button that has been selected."}]}]"""
    override val propsSections   = Seq("Radio Button Group")
    override val overrideMethods = Some("Radio Button Group Methods")
    override val overrideEvents  = Some("Radio Button Group Events")
  }
  object MuiToggle extends MuiSwitches {
    val name = "MuiToggle"
    override val json = """[{"name":"Toggle Props","infoArray":[{"name":"defaultToggled","type":"boolean","header":"default:false","desc":"The value of the toggle button. Is true when toggle has been turned on. False otherwise."},{"name":"elementStyle","type":"object","header":"optional","desc":"Overrides the inline-styles of the Toggle element."},{"name":"label","type":"node","header":"optional","desc":"The text that is displayed beside the toggle switch."},{"name":"labelStyle","type":"object","header":"optional","desc":"Overrides the inline-styles of the Toggle element label."},{"name":"labelPosition","type":"oneOf [left, right]","header":"default:\"left\"","desc":"Where the label will be placed next to the toggle switch. Options include \"left\" and \"right\" (case-sensitive). Default option is \"left\"."},{"name":"name","type":"string","header":"optional","desc":"This is the name of the toggle."},{"name":"style","type":"object","header":"optional","desc":"Override the inline-styles of the Toggle's root element."},{"name":"value","type":"string","header":"optional","desc":"The value of our toggle component."}]},{"name":"Toggle Methods","infoArray":[{"name":"isToggled","header":"Toggle.isToggled()","desc":"Returns true if the checkbox is currently checked. Returns false otherwise"},{"name":"setToggled","header":"Toggle.setToggled(newToggledValue)","desc":"Sets the toggle to the value of newToggledValue. This method cannot be used while \"checked\" is defined as a property."}]},{"name":"Toggle Events","infoArray":[{"name":"onToggle","type":"function(event, toggled)","header":"optional","desc":"Callback function that is fired when the toggle switch is toggled."}]}]"""
    override val propsSections   = Seq("Toggle Props")
    override val overrideMethods = Some("Toggle Methods")
    override val overrideEvents  = Some("Toggle Events")
    override val shared          = Some(SwitchSharedComponent)
  }

  trait MuiTables extends Component {
    override val children = true
    override val json = """[{"name":"Table Props","infoArray":[{"name":"allRowsSelected","type":"boolean","header":"default: false","desc":"Set to true to indicate that all rows should be selected."},{"name":"fixedFooter","type":"boolean","header":"optional","desc":"If true, the footer will appear fixed below the table. The default value is true."},{"name":"fixedHeader","type":"boolean","header":"optional","desc":"If true, the header will appear fixed above the table. The default value is true."},{"name":"height","type":"string","header":"optional","desc":"The height of the table."},{"name":"multiSelectable","type":"boolean","header":"optional","desc":"If true, multiple table rows can be selected. CTRL/CMD+Click and SHIFT+Click are valid actions. The default value is false."},{"name":"selectable","type":"boolean","header":"optional","desc":"If true, table rows can be selected. If multiple row selection is desired, enable multiSelectable. The default value is true."},{"name":"style","type":"object","header":"optional","desc":"Override the inline-styles of the table's root element."}]},{"name":"Table Header Props","infoArray":[{"name":"adjustForCheckbox","type":"boolean","header":"default: true","desc":"Controls whether or not header rows should be adjusted for a checkbox column. If the select all checkbox is true, this property will not influence the number of columns. This is mainly useful for \"super header\" rows so that the checkbox column does not create an offset that needs to be accounted for manually."},{"name":"displaySelectAll","type":"boolean","header":"default: true","desc":"Controls whether or not the select all checkbox is displayed."},{"name":"enableSelectAll","type":"boolean","header":"default: true","desc":"If set to true, the select all button will be interactable. If set to false, the button will not be interactable. To hide the checkbox, set displaySelectAll to false."},{"name":"selectAllSelected","type":"boolean","header":"default: true","desc":"If set to true the select all checkbox will be programmatically checked and will not trigger the select all event."},{"name":"style","type":"object","header":"optional","desc":"Override the inline-styles of the table header's root element."}]},{"name":"Table Body Props","infoArray":[{"name":"allRowsSelected","type":"boolean","header":"default: false","desc":"Set to true to indicate that all rows should be selected."},{"name":"deselectOnClickAway","type":"boolean","header":"default: true","desc":"Controls whether or not to deselect all selected rows after clicking outside the table."},{"name":"displayRowCheckbox","type":"boolean","header":"optional","desc":"Controls the display of the row checkbox. The default value is true."},{"name":"multiSelectable","type":"boolean","header":"optional","desc":"If true, multiple table rows can be selected. CTRL/CMD+Click and SHIFT+Click are valid actions. The default value is false."},{"name":"preScanRows","type":"boolean","header":"default: true","desc":"Controls whether or not the rows are pre-scanned to determine initial state. If your table has a large number of rows and you are experiencing a delay in rendering, turn off this property."},{"name":"selectable","type":"boolean","header":"optional","desc":"If true, table rows can be selected. If multiple row selection is desired, enable multiSelectable. The default value is true."},{"name":"showRowHover","type":"boolean","header":"optional","desc":"If true, table rows will be highlighted when the cursor is hovering over the row. The default value is false."},{"name":"stripedRows","type":"boolean","header":"optional","desc":"If true, every other table row starting with the first row will be striped. The default value is false."},{"name":"style","type":"object","header":"optional","desc":"Override the inline-styles of the table body's root element."}]},{"name":"Table Footer Props","infoArray":[{"name":"adjustForCheckbox","type":"boolean","header":"default: true","desc":"Controls whether or not header rows should be adjusted for a checkbox column. If the select all checkbox is true, this property will not influence the number of columns. This is mainly useful for \"super header\" rows so that the checkbox column does not create an offset that needs to be accounted for manually."},{"name":"style","type":"object","header":"optional","desc":"Override the inline-styles of the table footer's root element."}]},{"name":"Table Row Props","infoArray":[{"name":"displayBorder","type":"boolean","header":"default: true","desc":"If true, row border will be displayed for the row. If false, no border will be drawn."},{"name":"hoverable","type":"boolean","header":"default: false","desc":"Controls whether or not the row reponseds to hover events."},{"name":"rowNumber","type":"integer","header":"optional","desc":"Number to identify the row. This property is automatically populated when used with the TableBody component."},{"name":"selectable","type":"boolean","header":"default: true","desc":"If true, table rows can be selected. If multiple row selection is desired, enable multiSelectable. The default value is true."},{"name":"selected","type":"boolean","header":"default: false","desc":"Indicates that a particular row is selected. This property can be used to programmatically select rows."},{"name":"striped","type":"boolean","header":"default: false","desc":"Indicates whether or not the row is striped."},{"name":"style","type":"object","header":"optional","desc":"Override the inline-styles of the table row's root element."}]},{"name":"Table Header Column Props","infoArray":[{"name":"columnNumber","type":"integer","header":"optional","desc":"Number to identify the header row. This property is automatically populated when used with TableHeader."},{"name":"tooltip","type":"string","header":"optional","desc":"The string to supply to the tooltip. If not string is supplied no tooltip will be shown."},{"name":"tooltipStyle","type":"object","header":"optional","desc":"Additional styling that can be applied to the tooltip."},{"name":"style","type":"object","header":"optional","desc":"Override the inline-styles of the table header column's root element."}]},{"name":"Table Row Column Props","infoArray":[{"name":"columnNumber","type":"integer","header":"optional","desc":"Number to identify the header row. This property is automatically populated when used with TableHeader."},{"name":"hoverable","type":"boolean","header":"default: false","desc":"If true, this column responds to hover events."},{"name":"style","type":"object","header":"optional","desc":"Override the inline-styles of the table row column's root element."}]},{"name":"Table Events","infoArray":[{"name":"onRowSelection","type":"function(selectedRows)","header":"optional","desc":"Called when a row is selected. selectedRows is an array of all row selections. IF all rows have been selected, the string \"all\" will be returned instead to indicate that all rows have been selected."},{"name":"onCellClick","type":"function(rowNumber, columnId)","header":"optional","desc":"Called when a row cell is clicked. rowNumber is the row number and columnId is the column number or the column key."},{"name":"onRowHover","type":"function(rowNumber)","header":"optional","desc":"Called when a table row is hovered. rowNumber is the row number of the hovered row."},{"name":"onRowHoverExit","type":"function(rowNumber)","header":"optional","desc":"Called when a table row is no longer hovered. rowNumber is the row number of the row that is no longer hovered."},{"name":"onCellHover","type":"function(rowNumber, columnId)","header":"optional","desc":"Called when a table cell is hovered. rowNumber is the row number of the hovered row and columnId is the column number or the column key of the cell."},{"name":"onCellHoverExit","type":"function(rowNumber, columnId)","header":"optional","desc":"Called when a table cell is no longer hovered. rowNumber is the row number of the row and columnId is the column number or the column key of the cell."}]},{"name":"Table Header Events","infoArray":[{"name":"onSelectAll","type":"function(checked)","header":"optional","desc":"Called when the select all checkbox has been toggled."}]}]"""
  }

  object MuiTable extends MuiTables {
    val name = "MuiTable"
    override val propsSections   = Seq("Table Props")
    override val overrideMethods = nonexisting
    override val overrideEvents  = Some("Table Events")
  }
  object MuiTableBody extends MuiTables {
    val name = "MuiTableBody"
    override val propsSections   = Seq("Table Body Props")
    override val overrideMethods = nonexisting
    override val overrideEvents  = nonexisting
  }
  object MuiTableHeader extends MuiTables {
    val name = "MuiTableHeader"
    override val propsSections   = Seq("Table Header Props")
    override val overrideMethods = nonexisting
    override val overrideEvents  = Some("Table Header Events")
  }
  object MuiTableHeaderColumn extends MuiTables {
    val name = "MuiTableHeaderColumn"
    override val propsSections   = Seq("Table Header Column Props")
    override val overrideMethods = nonexisting
    override val overrideEvents  = nonexisting
  }
  object MuiTableFooter extends MuiTables {
    val name = "MuiTableFooter"
    override val propsSections   = Seq("Table Footer Props")
    override val overrideMethods = nonexisting
    override val overrideEvents  = nonexisting
  }
  object MuiTableRow extends MuiTables {
    val name = "MuiTableRow"
    override val propsSections   = Seq("Table Row Props")
    override val overrideMethods = nonexisting
    override val overrideEvents  = nonexisting
  }
  object MuiTableRowColumn extends MuiTables {
    val name = "MuiTableRowColumn"
    override val propsSections   = Seq("Table Row Column Props")
    override val overrideMethods = nonexisting
    override val overrideEvents  = nonexisting
  }

  trait MuiTabsT extends Component {
    override val children = true
    override val json = """[{"name":"Tabs Props","infoArray":[{"name":"contentContainerStyle","type":"object","header":"optional","desc":"Override the inline-styles of the content's container."},{"name":"initialSelectedIndex","type":"integer","header":"optional","desc":"Specify initial visible tab index. Initial selected index is set by default to 0. If initialSelectedIndex is set but larger than the total amount of specified tabs, initialSelectedIndex will revert back to default"},{"name":"inkBarStyle","type":"object","header":"optional","desc":"Override the inline-styles of the InkBar."},{"name":"style","type":"object","header":"optional","desc":"Override the inline-styles of the Tabs' root element."},{"name":"tabItemContainerStyle","type":"object","header":"optional","desc":"Override the inline-styles of the tab-labels container."},{"name":"tabTemplate","type":"ReactClass","header":"optional","desc":"Override the default tab template used to wrap the content of each tab element."},{"name":"value","type":"oneOfType [string, number]","header":"optional","desc":"Makes Tabs controllable and selects the tab whose value prop matches this prop."}]},{"name":"Tabs Events","infoArray":[{"name":"onChange","type":"function(value, e, tab)","header":"optional","desc":"Fired on touch or tap of a tab. Passes the value of the tab, the touchTap event and the tab element."}]},{"name":"Tab Props","infoArray":[{"name":"label","type":"string","header":"optional","desc":"Sets the text value of the tab item to the string specified."},{"name":"value","type":"string","header":"optional","desc":"If value prop passed to Tabs component, this value prop is also required. It assigns a value to the tab so that it can be selected by the Tabs."}]},{"name":"Tab Events","infoArray":[{"name":"onActive","type":"function(tab)","header":"optional","desc":"Fired when the active tab changes by touch or tap. Use this event to specify any functionality when an active tab changes. For example - we are using this to route to home when the third tab becomes active. This function will always recieve the active tab as it's first argument."}]}]"""
  }
  object MuiTab extends MuiTabsT {
    val name = "MuiTab"
    override val propsSections   = Seq("Tab Props")
    override val overrideMethods = nonexisting
    override val overrideEvents  = Some("Tab Events")
  }
  object MuiTabs extends MuiTabsT {
    val name = "MuiTabs"
    override val propsSections   = Seq("Tabs Props")
    override val overrideMethods = nonexisting
    override val overrideEvents  = Some("Tabs Events")
  }

  object MuiTextField extends Component {
    override val children = false
    val name = "MuiTextField"
    val json = """[{"name":"Props","infoArray":[{"name":"disabled","type":"boolean","header":"optional","desc":"Disables the text field if set to true."},{"name":"defaultValue","type":"string","header":"optional","desc":"The text string to use for the default value."},{"name":"errorStyle","type":"object","header":"optional","desc":"The style object to use to override error styles."},{"name":"errorText","type":"string","header":"optional","desc":"The error text string to display."},{"name":"floatingLabelStyle","type":"object","header":"optional","desc":"The style object to use to override floating label styles."},{"name":"floatingLabelText","type":"string","header":"optional","desc":"The text string to use for the floating label element."},{"name":"fullWidth","type":"boolean","header":"optional","desc":"If true, the field receives the property width 100%."},{"name":"hintStyle","type":"object","header":"optional","desc":"Override the inline-styles of the TextField's hint text element."},{"name":"hintText","type":"string","header":"optional","desc":"The hint text string to display."},{"name":"inputStyle","type":"object","header":"optional","desc":"Override the inline-styles of the TextField's input element."},{"name":"multiLine","type":"boolean","header":"default: false","desc":"If true, a textarea element will be rendered. The textarea also grows and shrinks according to the number of lines."},{"name":"rows","type":"integer","header":"default: 1","desc":"Number of rows to display when multiLine option is set to true."},{"name":"rowsMax","type":"integer","header":"default: null","desc":"Maximum number of rows to display when multiLine option is set to true."},{"name":"onEnterKeyDown","type":"function","header":"optional","desc":"The function to call when the user presses the Enter key."},{"name":"style","type":"object","header":"optional","desc":"Override the inline-styles of the TextField's root element."},{"name":"underlineStyle","type":"object","header":"optional","desc":"Override the inline-styles of the TextField's underline element."},{"name":"underlineFocusStyle","type":"object","header":"optional","desc":"Override the inline-styles of the TextField's underline element when focussed."},{"name":"underlineDisabledStyle","type":"object","header":"optional","desc":"Override the inline-styles of the TextField's underline element when disabled."},{"name":"type","type":"string","header":"optional","desc":"Specifies the type of input to display such as \"password\" or \"text\"."}]},{"name":"Methods","infoArray":[{"name":"blur","header":"TextField.blur()","desc":"Removes focus on the input element."},{"name":"clearValue","header":"TextField.clearValue()","desc":"Clears the value on the input element."},{"name":"focus","header":"TextField.focus()","desc":"Sets the focus on the input element."},{"name":"getValue","header":"TextField.getValue()","desc":"Returns the value of the input."},{"name":"setErrorText","header":"TextField.setErrorText(newErrorText)","desc":"Sets the error text on the input element."},{"name":"setValue","header":"TextField.setValue(newValue)","desc":"Sets the value of the input element."}]},{"name":"Events","infoArray":[{"name":"onBlur","header":"function(event)","desc":"Callback function that is fired when the textfield losesfocus."},{"name":"onChange","header":"function(event)","desc":"Callback function that is fired when the textfield's value changes."},{"name":"onFocus","header":"function(event)","desc":"Callback function that is fired when the textfield gains focus."}]}]"""
  }

  object MuiTimePicker extends Component {
    override val children = false
    val name = "MuiTimePicker"
    val json = """[{"name":"Props","infoArray":[{"name":"autoOk","type":"boolean","header":"default: false","desc":"If true, automatically accept and close the picker on set minutes."},{"name":"defaultTime","type":"date","header":"optional","desc":"This is the initial time value of the component."},{"name":"floatingLabelText","type":"string","header":"optional","desc":"The text string to use for the floating label element."},{"name":"format","type":"oneOf [ampm, 24hr]","header":"default: ampm","desc":"Tells the component to display the picker in ampm (12hr) format or 24hr format."},{"name":"hintText","type":"string","header":"optional","desc":"The hint text string to display. Note, floatingLabelText will overide this."},{"name":"pedantic","type":"boolean","header":"default: false","desc":"It's technically more correct to refer to \"12 noon\" and \"12 midnight\" rather than \"12 a.m.\" and \"12 p.m.\" and it avoids real confusion between different locales. By default (for compatibility reasons) TimePicker uses (12 a.m./12 p.m.) To use (noon/midnight) set pedantic={true}."},{"name":"style","type":"object","header":"optional","desc":"Override the inline-styles of TimePicker's root element."},{"name":"textFieldStyle","type":"object","header":"optional","desc":"Override the inline-styles of TimePicker's TextField element."}]},{"name":"Methods","infoArray":[{"name":"getTime","header":"TimePicker.getTime()","desc":"Returns the current time value."},{"name":"setTime","header":"TimePicker.setTime(t)","desc":"Sets the time value to t, where t is a date object."},{"name":"formatTime","header":"TimePicker.formatTime(time)","desc":"Formats the Date object to a current component's time format."},{"name":"openDialog","header":"TimePicker.openDialog()","desc":"Opens the time-picker dialog programmatically. Use this if you want to open the dialog in response to some event other than focus/tap on the input field, such as an external button click."},{"name":"focus","header":"TimePicker.focus()","desc":"An alias for the `openDialog()` method to allow more generic use alongside `TextField`."}]},{"name":"Events","infoArray":[{"name":"onChange","header":"function(null, time)","desc":"Callback function that is fired when the time value changes. The time value is passed in a Date Object.Since there is no particular event associated with the change the first argument will always be null and the second argument will be the new Date instance."},{"name":"onDismiss","header":"function()","desc":"Fired when the timepicker dialog is dismissed."},{"name":"onFocus","header":"function(event)","desc":"Callback function that is fired when the timepicker field gains focus."},{"name":"onShow","header":"function()","desc":"Fired when the timepicker dialog is shown."}]}]"""
  }

  trait MuiToolbars extends Component {
    //hack alert: removed som pointless entries from json here
    val json = """[{"name":"ToolbarGroup","infoArray":[{"name":"float","type":"string","header":"optional","desc":"Optional pull \"left\" or \"right\""},{"name":"style","type":"object","header":"optional","desc":"Override the inline-styles of the toolbar group's root element."}]},{"name":"ToolbarSeparator","infoArray":[{"name":"style","type":"object","header":"optional","desc":"Override the inline-styles of the toolbar separator's root element."}]},{"name":"ToolbarTitle","infoArray":[{"name":"text","type":"string","header":"optional","desc":"The text to be displayed for the element."},{"name":"style","type":"object","header":"optional","desc":"Override the inline-styles of the toolbar title's root element."}]}]"""
  }
  object MuiToolbar extends MuiToolbars{
    override val children = true
    override val name = "MuiToolbar"
    override val propsSections   = Nil
  }
  object MuiToolbarGroup extends MuiToolbars{
    override val children = true
    override val name = "MuiToolbarGroup"
    override val propsSections   = Seq("ToolbarGroup")
  }
  object MuiToolbarSeparator extends MuiToolbars{
    override val children = false
    override val name = "MuiToolbarSeparator"
    override val propsSections   = Seq("ToolbarSeparator")
  }
  object MuiToolbarTitle extends MuiToolbars{
    override val children = false
    override val name = "MuiToolbarTitle"
    override val propsSections   = Seq("ToolbarTitle")
  }

  val components = List(
    MuiAppBar,
    MuiAvatar,
    MuiCard, MuiCardHeader, MuiCardMedia, MuiCardTitle, MuiCardActions, MuiCardText,
    MuiDatePicker,
    MuiDialog,
    MuiDropDownMenu,
    MuiFlatButton,
    MuiFloatingActionButton,
    MuiFontIcon,
    MuiGridList,
    MuiGridTile,
    MuiIconButton,
    MuiIconMenu,
    MuiLeftNav,
    MuiList, MuiListDivider, MuiListItem,
    MuiMenu,
    MuiMenuItem,
    MuiPaper,
    MuiCircularProgress, MuiLinearProgress,
    MuiRaisedButton,
    MuiRefreshIndicator,
    MuiSlider,
    MuiSnackbar,
    MuiCheckbox, MuiRadioButton, MuiRadioButtonGroup, MuiToggle,
    MuiTable, MuiTableBody, MuiTableHeader, MuiTableHeaderColumn, MuiTableFooter, MuiTableRow, MuiTableRowColumn,
    MuiTab, MuiTabs,
    MuiTextField,
    MuiTimePicker,
    MuiToolbar, MuiToolbarGroup, MuiToolbarSeparator, MuiToolbarTitle
  )
}