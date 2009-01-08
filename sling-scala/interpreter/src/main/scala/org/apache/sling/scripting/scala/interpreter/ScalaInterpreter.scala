import scala.tools.nsc.{Settings, Global}
import scala.tools.nsc.interpreter.AbstractFileClassLoader
import scala.tools.nsc.io.AbstractFile
import scala.tools.nsc.reporters.Reporter
import scala.tools.nsc.util.{SourceFile, BatchSourceFile}
import java.net.URLClassLoader
import java.io.{File, InputStream, OutputStream}
import org.apache.sling.scripting.scala.Utils.{option}

package org.apache.sling.scripting.scala.interpreter {

class ScalaInterpreter(settings: Settings, reporter: Reporter, classes: Array[AbstractFile],
                       outDir: AbstractFile) {

  def this(settings: Settings, reporter: Reporter, classes: Array[AbstractFile]) =
    this(settings, reporter, classes, null)

  def this(settings: Settings, reporter: Reporter, outDir: AbstractFile) =
    this(settings, reporter, null, outDir)

  def this(settings: Settings, reporter: Reporter) =
    this(settings, reporter, null, null)

  private final val NL: String = System.getProperty("line.separator");

  protected val parentClassLoader: ClassLoader = getClass.getClassLoader

  protected val compiler: Global = {
    val c = new ScalaCompiler(settings, reporter, classes)
    if (outDir != null) c.genJVM.outputDir = outDir
    c
  }

  protected def packetize(name: String): List[String] =
    name.split('.').toList

  protected def preProcess(name: String, code: String, bindings: Bindings): String = {
    def bind(a: (String, Argument[_])) =
      "val " + a._1 + " = bindings.getValue(\"" + a._1 + "\").asInstanceOf[" + a._2.getType.getName + "]"

    val compounds = packetize(name)

    def packageDeclaration =
      if (compounds.size > 1) compounds.init.mkString("package ", ".", "") + NL
      else ""

    def className = compounds.last

    packageDeclaration +
    "object " + className + " {" + NL +
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

  protected def compile(sources: List[SourceFile]): Reporter = {
    reporter.reset
    val run = new compiler.Run
    if (reporter.hasErrors)
      reporter
    else {
      run.compileSources(sources)
      reporter
    }
  }

  def compile(name: String, code: String): Reporter =
    compile(List(new BatchSourceFile(name, code.toCharArray)))

  def compile(name: String, code: String, bindings: Bindings): Reporter =
    compile(name, preProcess(name, code, bindings))

  def compile(source: AbstractFile): Reporter = {
    compile(List(new BatchSourceFile(source)))
  }

  def compile(name: String, source: AbstractFile, bindings: Bindings): Reporter = {
    val code = new String(source.toByteArray)
    compile(name, preProcess(name, code, bindings))
  }

  @throws(classOf[InterpreterException])
  def interprete(name: String, code: String, bindings: Bindings, in: Option[InputStream],
                 out: Option[OutputStream]): Reporter = {
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
  def interprete(name: String, code: String, bindings: Bindings, in: InputStream,
                 out: OutputStream): Reporter =
    interprete(name, code, bindings, option(in), option(out))

  @throws(classOf[InterpreterException])
  def interprete(name: String, source: AbstractFile, bindings: Bindings, in: Option[InputStream],
                 out: Option[OutputStream]): Reporter = {
    compile(name, source, bindings)
    if (reporter.hasErrors)
      reporter
    else {
      execute(name, bindings, in, out)
    }
  }

  @throws(classOf[InterpreterException])
  def interprete(name: String, source: AbstractFile, bindings: Bindings): Reporter =
    interprete(name, source, bindings, None, None)

  @throws(classOf[InterpreterException])
  def interprete(name: String, source: AbstractFile, bindings: Bindings, in: InputStream,
                 out: OutputStream): Reporter =
    interprete(name, source, bindings, option(in), option(out))

  def getClassFile(name: String): AbstractFile = {
    var file: AbstractFile = compiler.genJVM.outputDir
    val pathParts = name.split("[./]").toList
    for (dirPart <- pathParts.init) {
      file = file.lookupName(dirPart, true)
      if (file == null) {
        return null
      }
    }
    file.lookupName(pathParts.last + ".class", false)
  }

  @throws(classOf[InterpreterException])
  def execute(name: String, bindings: Bindings, in: Option[InputStream], out: Option[OutputStream]): Reporter = {
    try {
      val classLoader = new AbstractFileClassLoader(compiler.genJVM.outputDir, parentClassLoader)
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

}


}