import scala.tools.nsc.Settings
import scala.tools.nsc.reporters.ConsoleReporter
import java.io.PrintWriter

package org.apache.sling.scripting.scala.interpreter {

object Test { // todo move to test folder, make this a test case

   def testScalaInterpreter() {
    val settings = new Settings
    settings.classpath.value = System.getProperty("java.class.path")

    val interpreter = new ScalaInterpreter(settings, null, new
          ConsoleReporter(settings, null, new PrintWriter(Console.out)))

    val bindings = Map[String, (AnyRef, Class[_])](
      ("msg", ("Hello world", classOf[String])),
      ("time", (java.util.Calendar.getInstance.getTime, classOf[java.util.Date])))

    val code = "println(msg + \": \" + time)"
    interpreter.interprete("testi", code, bindings)
  }

  def main(args: Array[String]) {
    testScalaInterpreter
  }

}

}

