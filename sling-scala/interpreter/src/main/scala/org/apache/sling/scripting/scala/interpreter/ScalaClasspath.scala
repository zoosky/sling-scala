import scala.tools.nsc.util.ClassPath
import scala.tools.nsc.io.AbstractFile

package org.apache.sling.scripting.scala.interpreter {

class ScalaClasspath(onlyPresentation: Boolean) extends ClassPath(onlyPresentation) {

  class BuildClasspath(classpath: String, source: String, output: String, boot: String, extdirs: String,
      codebase: String, classes: Array[AbstractFile])
      extends Build(classpath, source, output, boot, extdirs, codebase) {

    if (classes ne null) {
      for (file <- classes if file ne null) {
        val lib = new Library(file)
        entries += lib
      }
    }
  }

}

}