package org.apache.sling.scripting.scala.interpreter

class InterpreterException(message: String, cause: Throwable) extends Exception(message, cause) {
  def this(cause: Throwable) = this(if (cause == null) null else cause.getMessage, cause)
  def this(message: String) = this(message, null);
  def this() = this(null, null)
}

