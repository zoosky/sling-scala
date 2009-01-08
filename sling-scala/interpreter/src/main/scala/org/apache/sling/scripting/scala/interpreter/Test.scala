import scala.tools.nsc.Settings
import scala.tools.nsc.reporters.ConsoleReporter
import java.io.PrintWriter

package org.apache.sling.scripting.scala.interpreter {

object Test { // todo move to test folder, make this a test case

   def testScalaInterpreter() {
    val settings = new Settings
    settings.classpath.value_$eq( // todo dont hc
      "C:\\Documents and Settings\\mduerig\\.m2\\repository\\org\\scala-lang\\scala-library\\2.7.2\\scala-library-2.7.2.jar;" +
      "C:\\Documents and Settings\\mduerig\\.m2\\repository\\org\\scala-lang\\scala-compiler\\2.7.2\\scala-compiler-2.7.2.jar");
    val interpreter = new ScalaInterpreter(settings, null,
            new ConsoleReporter(settings, null, new PrintWriter(Console.out)))

    val bindings = Map[String, (AnyRef, Class[_])](
      ("a", ("Hello world", classOf[String])),
      ("b", (new java.lang.Integer(2), classOf[Integer])))

    val code = "println(a + \": \" + b)"
    interpreter.interprete("testi", code, bindings)
    return
  }

  def main(args: Array[String]) {
    testScalaInterpreter
  }

}

}

