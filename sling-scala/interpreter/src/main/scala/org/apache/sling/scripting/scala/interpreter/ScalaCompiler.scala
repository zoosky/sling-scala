import scala.tools.nsc.{Settings, Global}
import scala.tools.nsc.reporters.Reporter
import scala.tools.nsc.io.AbstractFile

package org.apache.sling.scripting.scala.interpreter {

class ScalaCompiler(settings: Settings, reporter: Reporter, classes: Array[AbstractFile])
  extends Global(settings, reporter) {

  override lazy val classPath0 = new ScalaClasspath(false && onlyPresentation)

  override lazy val classPath =
    if (forMSIL)
      abort("MSIL not supported")
    else
      new classPath0.BuildClasspath(settings.classpath.value, settings.sourcepath.value,
       settings.outdir.value, settings.bootclasspath.value, settings.extdirs.value,
       settings.Xcodebase.value, classes)

}

}