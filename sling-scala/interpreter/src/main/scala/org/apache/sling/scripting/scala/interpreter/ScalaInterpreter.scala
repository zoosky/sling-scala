import scala.tools.nsc.{Settings, Global}
import scala.tools.nsc.reporters.Reporter
import scala.tools.nsc.io.AbstractFile
import scala.tools.nsc.util.BatchSourceFile
import java.net.URLClassLoader
import java.io.{File, InputStream, OutputStream}

package org.apache.sling.scripting.scala.interpreter {

class ScalaInterpreter(settings: Settings, classes: Array[AbstractFile], reporter: Reporter) {
  private final val NL = System.getProperty("line.separator");

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
    def bind(a: (String, Argument[_])) =
      "val " + a._1 + " = bindings.getValue(\"" + a._1 + "\").asInstanceOf[" + a._2.getType.getName + "]"

    "object " + name + " {" + NL +
    "  def main(bindings: org.apache.sling.scripting.scala.interpreter.Bindings," + NL +
    "           stdIn: java.io.InputStream," + NL +
    "           stdOut: java.io.OutputStream) {" + NL +
    "    def run() {" + NL +
           bindings.map(bind).mkString("", NL, NL) +
           code + "" + NL +
    "      return" + NL +
    "    }" + NL +
    "    Console.withIn(stdIn) {" + NL +
    "      Console.withOut(stdOut) {" + NL +
    "        run" + NL +
    "      }" + NL +
    "    }" + NL +
    "  }" + NL +
    "}" + NL
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

  @throws(classOf[InterpreterException])
  def interprete(name: String, code: String, bindings: Bindings): Reporter = {
    compile(name, code, bindings)
    if (reporter.hasErrors)
      reporter
    else {
      execute(name, bindings)
    }
  }

  @throws(classOf[InterpreterException])
  def execute(name: String, bindings: Bindings) = {
    try {
      val classLoader = new URLClassLoader(
        Array((new File(compiler.settings.outdir.value)).toURL), parentClassLoader)

      val script = Class.forName(name, true, classLoader)
      val initMethod = (script
        .getDeclaredMethods
        .toList
        .find(method => method.getName == "main")
        .get)

      initMethod.invoke(null, Array(bindings, in.getOrElse(java.lang.System.in),
                                              out.getOrElse(java.lang.System.out)): _*)
      reporter
    }
    catch {
      case e: java.lang.reflect.InvocationTargetException =>
        throw new InterpreterException("Error executing " + name, e.getTargetException)
      case e: Exception =>
        throw new InterpreterException("Error executing " + name, e)
    }
  }

}

}