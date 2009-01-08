package org.apache.sling.scripting.scala

object Utils {

  def nullOrElse[S, T](s: S)(f: S => T): T =
    if (s == null) null.asInstanceOf[T]
    else f(s)

  def valueOrElse[T](t: T)(default: => T) =
    if (t == null) default
    else t

}
