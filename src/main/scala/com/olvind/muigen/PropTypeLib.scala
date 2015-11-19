package com.olvind.muigen

import ammonite.ops._

object PropTypeLib {
  def flatten(r: Ret): Seq[Comp] = {
    r match {
      case Folder(name, cs) =>
        cs flatMap flatten
      case Comp(name, props) =>
        Seq(Comp("Mui" + name, props))
    }
  }
  val result: Ret = PropTypeParser(home / "pr" / "material-ui" / "lib")
  val results = flatten(result)
}
