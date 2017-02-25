package com.olvind
package requiresjs

import ammonite.ops.Path

case class FoundComponent(
  name:      CompName,
  file:      Path,
  jsContent: String,
  props:     Map[PropName, PropUnparsed],
  methods:   Option[Set[MemberMethod]]
)