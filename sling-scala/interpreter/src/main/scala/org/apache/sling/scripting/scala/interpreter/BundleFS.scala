import scala.tools.nsc.io.AbstractFile
import java.io.{File, InputStream, OutputStream, IOException}
import java.net.URL
import org.osgi.framework.Bundle

package org.apache.sling.scripting.scala.interpreter {

object BundleFS {
  def create(bundle: Bundle): AbstractFile = {
    assert(bundle ne null)

    abstract class BundleEntry(url: URL, parent: DirEntry) extends AbstractFile {
      lazy val (path: String, name: String) = getPathAndName(url)
      lazy val fullName: String = (path::name::Nil).filter(!_.isEmpty).mkString("/")
      def file: File = null
      def lastModified: Long =
        try { url.openConnection.getLastModified }
        catch { case _ => 0 }

      def container: AbstractFile =
        if (parent ne null) parent
        else throw new IOException("No container")

      def input: InputStream = url.openStream()
      def output = throw new IOException("not supported: output")

      private def getPathAndName(url: URL) = {
        val u = url.getPath
        var k = u.length
        while( (k > 0) && (u(k - 1) == '/') )
          k = k - 1

        var j = k
        while( (j > 0) && (u(j - 1) != '/') )
          j = j - 1

        (u.substring(if (j > 0) 1 else 0, if (j > 1) j - 1 else j), u.substring(j, k))
      }
    }

    class DirEntry(url: URL, parent: DirEntry) extends BundleEntry(url, parent) {
      def isDirectory: Boolean = true

      def elements: Iterator[AbstractFile] = {
        new Iterator[AbstractFile]() {
          val dirs = bundle.getEntryPaths(fullName)
          def hasNext = dirs.hasMoreElements
          def next = {
            val entry = dirs.nextElement.asInstanceOf[String]
            val entryUrl = bundle.getResource("/" + entry)
            if (entry.endsWith("/"))
              new DirEntry(entryUrl, DirEntry.this)
            else
                new FileEntry(entryUrl, DirEntry.this)
          }
        }
      }

      def lookupName(name: String, directory: Boolean): AbstractFile = {
        val entry = bundle.getEntry(fullName + "/" + name)
        if (entry eq null) null
        else {
          if (directory)
            new DirEntry(entry, DirEntry.this)
          else
            new FileEntry(entry, DirEntry.this)
        }
      }

    }

    class FileEntry(url: URL, parent: DirEntry) extends BundleEntry(url, parent) {
      def isDirectory: Boolean = false
      override def size: Option[Int] = Some(bundle.getEntry(fullName).openConnection().getContentLength())
      def elements: Iterator[AbstractFile] = Iterator.empty
      def lookupName(name: String, directory: Boolean): AbstractFile = null
    }

    new DirEntry(bundle.getResource("/"), null)
  }

}

}