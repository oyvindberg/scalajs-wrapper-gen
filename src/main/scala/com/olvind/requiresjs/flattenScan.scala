package com.olvind
package requiresjs

object flattenScan {
  def apply(r: Required): Map[CompName, FoundComponent] =
    r match {
      case Single(n, c)     =>
        Map(n -> c)
      case SingleNotComp(varName) ⇒
        println(s"Ignoring non-react dependency $varName")
        Map.empty
      case Multiple(_, rs) =>
        (rs flatMap apply).toMap
    }
}
