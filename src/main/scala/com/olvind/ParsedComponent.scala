package com.olvind

case class ParsedMethodClass(prefix: String, name: CompName, ms: Seq[ParsedMethod]){
  val className = prefix + name.value + "M"
}

case class ParsedEnumClass(name: String, identifiers: Seq[(Identifier, String)])

case class ParsedMethod(definition: String, commentOpt: Option[PropComment]) {
  require(!definition.contains("="))
  require(!definition.startsWith("def "))
}

case class ParsedComponent(
  definition:     ComponentDef,
  fields:         Seq[ParsedProp],
  methodClassOpt: Option[ParsedMethodClass]) {

  def name = definition.name

  val childrenOpt = fields.find(_.name.value == "children")

  val enumClases: Seq[ParsedEnumClass] =
    fields.map(_.baseType).collect{
      case o: PropTypeEnum => o.enumClass
    }
}

object ParsedComponent{
  def apply(allComps: Map[CompName, gen.Component],
            docProvider: DocProvider)
           (muiComp:  ComponentDef): ParsedComponent = {

    val (commentMap, methodClassOpt: Option[ParsedMethodClass]) =
      docProvider(muiComp)

    val propTypes: Map[PropName, PropUnparsed] =
      allComps.get(muiComp.name).flatMap(_.propsOpt).getOrElse(
        throw new RuntimeException(s"No Proptypes found for ${muiComp.name}")
      )

    val inheritedProps: Map[PropName, PropUnparsed] =
      muiComp.shared match {
        case None         => Map.empty
        case Some(shared) =>
          allComps.get(shared).flatMap(_.propsOpt).getOrElse(
            throw new RuntimeException(s"No Proptypes found for $shared")
          )
      }

    val basicFields: Seq[ParsedProp] =
      Seq(
        ParsedProp(PropName("key"), isRequired = false, PropTypeClass("String"), None, None, None),
        ParsedProp(PropName("ref"), isRequired = false, PropTypeClass(methodClassOpt.fold("String")(c => c.className + " => Unit")), None, None, None)
  //    out.addField(ReqField(PropName("untyped"), PropTypeClass("Map[String, js.Any]"), None, None, None))
      )

    val parsedFields: Seq[ParsedProp] =
      (inheritedProps ++ propTypes)
        .filterNot(t => basicFields.exists(_.name == t._1))
        .toSeq
        .sortBy(p => (p._2.fromComp != muiComp.name, p._1.clean.value))
        .map {
        case (propName, PropUnparsed(origComp, tpe, commentOpt)) =>
          ParsedProp(
            muiComp.name,
            origComp,
            propName,
            tpe,
            commentOpt orElse (commentMap get propName)
          )
      }

    ParsedComponent( muiComp, basicFields ++ parsedFields, methodClassOpt)
  }
}