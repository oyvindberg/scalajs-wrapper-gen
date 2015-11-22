package com.olvind.muigen

import ammonite.ops._

object PropTypeLib  {
  def mapType(c: String, f: String, t: String): (String, OutField) = {
    def fis(s: String) = f.toLowerCase.contains(s.toLowerCase)
    def cis(s: String) = c.toLowerCase.contains(s.toLowerCase)
    def tis(s: String) = t.toLowerCase.contains(s.toLowerCase)

    val outType = (c, f, t.replaceAll(".isRequired", "")) match {
      case (_, _, "string") if fis("color")=> "MuiColor"
      case (_, _, "string") => "String"
      case (_, _, "zDepth") => "MuiZDepth"
      case (_, _, "element") => "ReactElement"
      case (_, _, "node") => "ReactNode"
      case (_, _, "bool") => "Boolean"
      case (_, _, "number") => "Double"
      case (_, _, "func") => "ReactEvent => Callback"
      case (_, _, "array") => "js.Array[js.Any]"
      case (_, _, "object") if fis("style") => "CssProperties"
      case (_, _, "object") if fis("date") => "js.Date"
      case (_, _, "object")  => "js.Object"
      case (_, _, _) if tis("oneOf") => "js.Object"
      case (_, _, "validateLabel")  => "String"
      case (_, _, "cornersAndCenter")  => "js.Object"
      case (_, _, "corners")  => "js.Object"
      case (_, _, "arrayOf(element)")  => "js.Array[ReactElement]"
      case (_, _, "origin")  => "Origin"
      case (_, _, "minMaxPropType")  => "Double"
      case (_, _, "valueInRangePropType")  => "Double"
      case (_, _, "stringOrNumber")  => "String | Int"
    }
    if (tis("isRequired"))
      f -> ReqField(f, OutParamClass(outType), None)
    else
      f -> OptField(f, OutParamClass(outType), None)
  }

  def massage(r: Ret): Map[String, Map[String, OutField]] = {
    r match {
      case Folder(name, cs) =>
        cs.foldLeft(Map.empty[String, Map[String, OutField]]){
          case (map, c) => map ++ massage(c)
        }
      case Comp(name, hasChildren, props) =>
        val newProps = props map {
          case (pName, propType) =>
            mapType(name, pName, propType.replace("React.PropTypes.", "").replace("PropTypes.", "").replace("React.", ""))
        }
        Map("Mui" + name -> newProps.toMap)
    }
  }
  val result: Ret = PropTypeParser(home / "pr" / "material-ui" / "lib")
  val results = massage(result)
//  println(results)
}
