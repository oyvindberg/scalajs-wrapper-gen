package com.olvind

object ParseComponent {
  def apply[D <: ComponentDef](
    scope:   Map[CompName, requiresjs.FoundComponent],
    library: Library[D],
    comp:    D): ParsedComponent = {

    val (commentMap, methodClassOpt: Option[ParsedMethodClass]) =
      library.docProvider(library.prefix, comp)

    val propTypes: Map[PropName, PropUnparsed] =
      scope.get(comp.name).flatMap(_.propsOpt).getOrElse(
        throw new RuntimeException(s"No Proptypes found for ${comp.name}")
      )

    val inheritedProps: Map[PropName, PropUnparsed] =
      comp.shared match {
        case None         => Map.empty
        case Some(shared) =>
          scope.get(shared).flatMap(_.propsOpt).getOrElse(
            throw new RuntimeException(s"No Proptypes found for $shared")
          )
      }

    val basicFields: Seq[ParsedProp] =
      Seq(
        ParsedProp(PropName("key"), isRequired = false,
          PropType.Type("String"), None, None, None
        ),
        ParsedProp(PropName("ref"), isRequired = false,
          PropType.Type(methodClassOpt.fold("String")(c => c.className + " => Unit")), None, None, None
        )
  //    out.addField(ReqField(PropName("untyped"), PropTypeClass("Map[String, js.Any]"), None, None, None))
      )

    val parsedFields: Seq[ParsedProp] =
      (inheritedProps ++ propTypes)
        .filterNot(t => basicFields.exists(_.name == t._1))
        .toSeq
        .sortBy(p => (p._2.fromComp != comp.name, p._1.clean.value))
        .map {
        case (propName, PropUnparsed(origComp, tpe, commentOpt)) =>
          ParseProp(
            library,
            comp.name,
            origComp,
            propName,
            tpe,
            commentOpt orElse (commentMap get propName)
          )
      }

    ParsedComponent(comp, basicFields ++ parsedFields, methodClassOpt)
  }
}

object ParseProp {
  //  "Deprecated(string, 'Instead, use a custom `actions` property.')"
  val Pattern = "Deprecated\\(([^,]+), '(.+)'\\)".r

  def apply[D <: ComponentDef](
            library:      Library[D],
            compName:     CompName,
            origCompName: CompName,
            propName:     PropName,
            propString:   PropTypeUnparsed,
            commentOpt:   Option[PropComment]): ParsedProp = {
    val _clean: String =
      propString.value
        .replace("PropTypes.", "")
        .replace(".isRequired", "")
        /* old style */
        .replace("_react2['default'].", "")
        //todo: this is fairly mui specific
        .replace("_utilsPropTypes2['default'].", "Mui.")
        .replace("(0, _utilsDeprecatedPropType2['default'])", "Deprecated")
        /* new style */
        .replace("_react2.default.", "")
        .replace("_propTypes2.default.", "Mui.")
        .replace("(0, _deprecatedPropType2.default)", "Deprecated")

    val (typeStr: String, deprecatedOpt: Option[String]) = _clean match {
      case Pattern(tpe, depMsg) => (tpe, Some(depMsg))
      case tpe                  => (tpe, None)
    }

    val mappedType: PropType =
      library.typeMapper(origCompName, propName, typeStr)

    val isRequired: Boolean =
      propString.value.contains(".isRequired")

    val inheritedFrom: Option[CompName] =
      if (compName == origCompName) None else Some(origCompName)

    ParsedProp(
      propName,
      isRequired && inheritedFrom.isEmpty,
      mappedType,
      commentOpt,
      deprecatedOpt,
      inheritedFrom
    )
  }
}