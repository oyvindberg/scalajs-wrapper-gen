package com.olvind.mui

import com.olvind.ComponentDef

trait MuiComponent extends ComponentDef {
  val propsSections: Seq[String] = Seq("Props")
  val overrideMethods: Option[String] = None
  val overrideEvents: Option[String] = None
}