package com.olvind
package requiresjs

object flattenScan {
  def apply(r: Required): Map[CompName, FoundComponent] =
    r match {
      case Single(n, c)     =>
        Map(n -> c)
      case Multiple(_, rs) =>
        (rs flatMap apply).toMap
      case NotFound(_) ⇒
        Map.empty
    }
}
