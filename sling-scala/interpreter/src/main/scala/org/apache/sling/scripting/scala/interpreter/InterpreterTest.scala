import junit.framework.TestCase
import junit.framework.Assert
import scala.tools.nsc.Settings
import scala.tools.nsc.reporters.ConsoleReporter

package org.apache.sling.scripting.scala.interpreter {

class InterpreterTest extends TestCase {

   def testScalaInterpreter() {
    val settings = new Settings
    settings.classpath.value = System.getProperty("java.class.path")

    val interpreter = new ScalaInterpreter(settings, null, new
          ConsoleReporter(settings, null, new java.io.PrintWriter(Console.out)))

    val out = new java.io.ByteArrayOutputStream
    interpreter.stdOut = out

    val bindings = Map[String, (AnyRef, Class[_])](
      ("msg", ("Hello world", classOf[String])),
      ("time", (java.util.Calendar.getInstance.getTime, classOf[java.util.Date])))

    val code = "println(msg + \": \" + time)"
    interpreter.interprete("testi", code, bindings)
    Assert.assertTrue(out.toString.startsWith("Hello world: "))
  }

}

}