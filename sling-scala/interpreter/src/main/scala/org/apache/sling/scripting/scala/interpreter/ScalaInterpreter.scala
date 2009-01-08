import scala.tools.nsc.{Settings, Global}
import scala.tools.nsc.reporters.Reporter
import scala.tools.nsc.io.AbstractFile
import scala.tools.nsc.util.BatchSourceFile
import java.net.URLClassLoader
import java.io.{File, InputStream, OutputStream}

package org.apache.sling.scripting.scala.interpreter {

class ScalaInterpreter(settings: Settings, classes: Array[AbstractFile], reporter: Reporter) {
  type Bindings = Map[String, (AnyRef, Class[_])]

  private var in: Option[InputStream] = None
  def stdIn = in
  def stdIn_= (in: InputStream) {
    if (in ne null) this.in = Some(in)
  }

  private var out: Option[OutputStream] = None
  def stdOut = out
  def stdOut_= (out: OutputStream) {
    if (out ne null) this.out = Some(out)
  }

  // todo fix: redirect stdErr
  def stdErr = throw new Error("unsupported: stdErr")
  def stdErr_= (err: OutputStream) = throw new Error("unsupported: stdErr")

  protected val compiler: Global = new ScalaCompiler(settings, reporter, classes)
  protected val parentClassLoader: ClassLoader = getClass.getClassLoader

  protected def preProcess(name: String, code: String, bindings: Bindings): String = {
    def bind(e: (String, (AnyRef, Class[_]))) =
      "val " + e._1 + " = bindings(\"" + e._1 + "\")._1.asInstanceOf[" + e._2._2.getName + "]"

    // todo fix: this is not thread safe (THREAD-ISOLATED)
    // todo fix: don't hard code new line char
    // todo fix: reset stdIn and stdOut to their initial values
    "object " + name + " {\n" +
    "  type Bindings = Map[String, (AnyRef, Class[_])]\n" +
    "  def main(bindings: Bindings, stdIn: Option[java.io.InputStream], stdOut: Option[java.io.OutputStream]) {\n" +
         bindings.map(bind).mkString("", "\n", "\n") +
    "    stdIn match {\n" +
    "      case Some(in: java.io.InputStream) => Console.setIn(in)\n" +
    "      case _ => \n" +
    "    }\n" +
    "    stdOut match {\n" +
    "      case Some(out: java.io.OutputStream) => Console.setOut(out)\n" +
    "      case _ => \n" +
    "    }\n" +
    "    def run() {\n" +
          code + "\n" +
    "      return\n" +
    "    }\n" +
    "    run\n" +
    "  }\n" +
    "}\n"
  }

  def compile(name: String, code: String): Reporter = {
    reporter.reset
    val run = new compiler.Run
    if (reporter.hasErrors)
      reporter
    else {
      run.compileSources(List(new BatchSourceFile(name, code.toCharArray)))
      reporter
    }
  }

  def compile(name: String, code: String, bindings: Bindings): Reporter =
    compile(name, preProcess(name, code, bindings))

  def interprete(name: String, code: String, bindings: Bindings): Reporter = {
    compile(name, code, bindings)
    if (reporter.hasErrors)
      reporter
    else {
      execute(name, bindings)
    }
  }

  def execute(name: String, bindings: Bindings) = {
      val classLoader = new URLClassLoader(
        Array((new File(compiler.settings.outdir.value)).toURL), parentClassLoader)

      val script = Class.forName(name, true, classLoader)
      val initMethod = (script
        .getDeclaredMethods
        .toList
        .find(method => method.getName == "main")
        .get)

      initMethod.invoke(null, Array(bindings, in, out): _*)
      reporter
  }

}

}