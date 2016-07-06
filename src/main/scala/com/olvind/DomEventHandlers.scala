package com.olvind

sealed abstract class DomType(val suffix: String)
case object DomNode extends DomType("")
case object DomElement extends DomType("H")
case object DomInput extends DomType("I")
case object DomTextArea extends DomType("TA")

case class DomEventHandlers(domType: DomType) {

  private def handler(name: String, params: String*): ParsedProp = {
    ParsedProp(
      name          = PropName(name),
      isRequired    = false,
      baseType      = Normal(params.map(_ + domType.suffix).mkString("(", ",", ")") + " => Callback"),
      commentOpt    = None,
      deprecatedMsg = None,
      inheritedFrom = Some(CompName("DOM"))
    )
  }

  val props: Seq[ParsedProp] =
    Seq(
      handler("onAnimationEnd",       "ReactEvent"),
      handler("onAnimationIteration", "ReactEvent"),
      handler("onAnimationStart",     "ReactEvent"),
      handler("onBlur",               "ReactFocusEvent"),
      handler("onChange",             "ReactEvent"),
      handler("onClick",              "ReactMouseEvent"),
      handler("onCompositionEnd",     "ReactCompositionEvent"),
      handler("onCompositionStart",   "ReactCompositionEvent"),
      handler("onCompositionUpdate",  "ReactCompositionEvent"),
      handler("onContextMenu",        "ReactEvent"),
      handler("onCopy",               "ReactClipboardEvent"),
      handler("onCut",                "ReactClipboardEvent"),
      handler("onDoubleClick",        "ReactMouseEvent"),
      handler("onDrag",               "ReactDragEvent"),
      handler("onDragEnd",            "ReactDragEvent"),
      handler("onDragEnter",          "ReactDragEvent"),
      handler("onDragExit",           "ReactDragEvent"),
      handler("onDragLeave",          "ReactDragEvent"),
      handler("onDragOver",           "ReactDragEvent"),
      handler("onDragStart",          "ReactDragEvent"),
      handler("onDrop",               "ReactDragEvent"),
      handler("onFocus",              "ReactFocusEvent"),
      handler("onInput",              "ReactKeyboardEvent"),
      handler("onKeyDown",            "ReactKeyboardEvent"),
      handler("onKeyPress",           "ReactKeyboardEvent"),
      handler("onKeyUp",              "ReactKeyboardEvent"),
      handler("onMouseDown",          "ReactMouseEvent"),
      handler("onMouseEnter",         "ReactMouseEvent"),
      handler("onMouseLeave",         "ReactMouseEvent"),
      handler("onMouseMove",          "ReactMouseEvent"),
      handler("onMouseUp",            "ReactMouseEvent"),
      handler("onPaste",              "ReactClipboardEvent"),
      handler("onScroll",             "ReactUIEvent"),
      handler("onSelect",             "ReactUIEvent"),
      handler("onSubmit",             "ReactEvent"),
      handler("onTouchCancel",        "ReactTouchEvent"),
      handler("onTouchEnd",           "ReactTouchEvent"),
      handler("onTouchMove",          "ReactTouchEvent"),
      handler("onTouchStart",         "ReactTouchEvent"),
      handler("onTransitionEnd",      "ReactTouchEvent"),
      handler("onWheel",              "ReactWheelEvent")
    )
}
