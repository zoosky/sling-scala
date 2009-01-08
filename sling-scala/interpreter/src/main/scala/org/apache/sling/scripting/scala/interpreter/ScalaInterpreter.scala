import scala.tools.nsc.{Settings, Global}
import scala.tools.nsc.reporters.Reporter
import scala.tools.nsc.io.AbstractFile
import scala.tools.nsc.util.BatchSourceFile
import java.net.URLClassLoader
import java.io.{File, InputStream, OutputStream}

package org.apache.sling.scripting.scala.interpreter {

// todo fix: add output directory param
// todo fix: add secondary constructors
class ScalaInterpreter(settings: Settings, classes: Array[AbstractFile], reporter: Reporter) {
  private final val NL = System.getProperty("line.separator");

  protected val compiler: Global = new ScalaCompiler(settings, reporter, classes)
  protected val parentClassLoader: ClassLoader = getClass.getClassLoader

  // todo: add option for compilation if out dated only

  // todo fix: use dot in name for building package structure
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
    "        stdOut.flush" + NL +
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
  def interprete(name: String, code: String, bindings: Bindings,
                 in: Option[InputStream], out: Option[OutputStream]): Reporter = {
    compile(name, code, bindings)
    if (reporter.hasErrors)
      reporter
    else {
      execute(name, bindings, in, out)
    }
  }

  @throws(classOf[InterpreterException])
  def interprete(name: String, code: String, bindings: Bindings): Reporter =
    interprete(name, code, bindings, None, None)

  @throws(classOf[InterpreterException])
  def interprete(name: String, code: String, bindings: Bindings, in: InputStream, out: OutputStream): Reporter =
    interprete(name, code, bindings, option(in), option(out))

  @throws(classOf[InterpreterException])
  def execute(name: String, bindings: Bindings, in: Option[InputStream], out: Option[OutputStream]): Reporter = {
    try {
      val classLoader = new URLClassLoader(
        // todo fix: use compiler.genJVM.outputDir and AbstractFileClassLoader for output dir
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

  @throws(classOf[InterpreterException])
  def execute(name: String, bindings: Bindings): Reporter =
    execute(name, bindings, None, None)

  @throws(classOf[InterpreterException])
  def execute(name: String, bindings: Bindings, in: InputStream, out: OutputStream): Reporter =
    execute(name, bindings, option(in), option(out))

  private def option[T](opt: T): Option[T] =
    if (null == opt) None else Some(opt)

}


}