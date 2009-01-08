import junit.framework.TestCase
import junit.framework.Assert.assertEquals
import scala.tools.nsc.Settings
import scala.tools.nsc.io.VirtualDirectory
import scala.tools.nsc.reporters.ConsoleReporter
import org.apache.sling.scripting.scala.Utils.valueOrElse
import java.io.PrintWriter
import javax.jcr.{Session, Repository, Node, SimpleCredentials}
import org.apache.jackrabbit.core.{TransientRepository}

package org.apache.sling.scripting.scala.interpreter {

class InterpreterTest extends TestCase {
  var session: Session = null
  var repository: Repository = null
  var testRoot: Node = null

  override def setUp() {
    super.setUp()
    repository = new TransientRepository
    session = repository.login(new SimpleCredentials("admin", "admin".toCharArray))
    testRoot = session.getRootNode.addNode("testRoot", "nt:folder")
    session.save()
  }

  override def tearDown() {
    testRoot.remove()
    testRoot = null
    session.save()
    session.logout()
    session = null
    repository = null
    super.tearDown()
  }

  private def reporter(settings: Settings) =
    new ConsoleReporter(settings, null, new java.io.PrintWriter(Console.err))

  private def classpath = {
    valueOrElse(System.getProperty("surefire.test.class.path")) {
      System.getProperty("java.class.path")
    }
  }

  private def settings = {
    val s = new Settings
    s.classpath.value = classpath
    s
  }

  def testScalaInterpreter {
    val outdir = new VirtualDirectory("outdir", None)
    val interpreter = new ScalaInterpreter(settings, reporter(settings), outdir)
    val out = new java.io.ByteArrayOutputStream

    val bindings = new ScalaBindings
    val time = java.util.Calendar.getInstance.getTime
    bindings.put("msg", "Hello world", classOf[String])
    bindings.put("time", time, classOf[java.util.Date])

    val code = "print(msg + \": \" + time)"

    interpreter.interprete("testi", code, bindings, None, Some(out))
    assertEquals("Hello world: " + time, out.toString)
  }

  def testScalaInterpreterWithJcr {
    val appNode = testRoot.addNode("app", "nt:folder")
    testRoot.save()

    val appDir = JcrFS.create(appNode)
    val outDir = appDir.subdirectoryNamed("outdir")
    val srcDir = appDir.subdirectoryNamed("srcdir")
    val interpreter = new ScalaInterpreter(settings, reporter(settings), outDir)
    val out = new java.io.ByteArrayOutputStream

    val bindings = new ScalaBindings
    val time = java.util.Calendar.getInstance.getTime
    bindings.put("msg", "Hello world", classOf[String])
    bindings.put("time", time, classOf[java.util.Date])

    val src = srcDir.fileNamed("testi")
    val writer = new PrintWriter(src.output)
    writer.print("print(msg + \": \" + time)")
    writer.close

    interpreter.interprete(src, bindings, None, Some(out))
    assertEquals("Hello world: " + time, out.toString)
  }

}

}