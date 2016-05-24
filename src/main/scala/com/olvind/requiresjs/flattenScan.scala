package com.olvind
package requiresjs

object flattenScan {
  def apply(r: Required): Map[CompName, FoundComponent] =
    r match {
      case Single(n, c)     =>
        Map(n -> c)
      case Multiple(_, _, rs) =>
        (rs flatMap apply).toMap
      case SingleNotComp(varName) ⇒
        println(s"Ignoring non-react dependency $varName")
        Map.empty
      case ExternalDep(varName) =>
        println(s"Ignoring external dependency $varName")
        Map.empty
    }
}
