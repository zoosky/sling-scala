import scala.tools.nsc.io.AbstractFile
import java.io.{File, InputStream, OutputStream, IOException, ByteArrayOutputStream, ByteArrayInputStream}
import javax.jcr.{Session, Node, Property}
import org.apache.sling.scripting.scala.Utils.{nullOrElse, valueOrElse}

package org.apache.sling.scripting.scala.interpreter {

object JcrFS {
  def create(node: Node): JcrNode = node.getPrimaryNodeType.getName match {
    case "nt:file" => JcrFile(node) // todo fix: dont hc ns prefixes
    case "nt:folder" => JcrFolder(node)
    case _ => throw new IOException("Neither file nor folder: " + node.getPath)
  }

  def create(session: Session, path: String): JcrNode = {
    session.getItem(path) match {
      case n: Node => create(n)
      case _ => throw new IOException("Path not found: " + path)
    }
  }

  abstract sealed class JcrNode(node: Node) extends AbstractFile {
    assert (node != null)

    val emptyInputStream = new InputStream {
      override def read = -1
    }

    def getIndex: String =
      if (node.getIndex == 1) ""
      else "[" + node.getIndex + "]"

    def name: String = node.getName + getIndex
    def path: String = node.getPath
    def container: JcrNode = create(node.getParent)
    def file: File = null
    def lastModified: Long =
      if (node.hasProperty("jcr:content/jcr:lastModified")) node.getProperty("jcr:content/jcr:lastModified").getLong
      else {
        if (node.hasProperty("jcr:lastModified")) node.getProperty("jcr:lastModified").getLong
        else 0
      }

    override def equals(other: Any): Boolean =
      other match {
        case that: JcrNode => this.path == that.path
        case _ => false
      }

    override def hashCode: Int = path.hashCode
  }

  case class JcrFolder(node: Node) extends JcrNode(node) {
    def isDirectory = true
    def input: InputStream = throw new IOException("Cannot read from directory")
    def output: OutputStream = throw new IOException("Cannot write to directory")

    def elements: Iterator[JcrNode] =
      new Iterator[Node] {
        val childs = node.getNodes
        def hasNext = childs.hasNext
        def next = childs.next.asInstanceOf[Node]
      }
      .filter((node: Node) =>
        "nt:file" == node.getPrimaryNodeType.getName ||
        "nt:folder" == node.getPrimaryNodeType.getName)
      .map((node: Node) => create(node))

    def lookupName(name: String, directory: Boolean): AbstractFile = {
      if (node.hasNode(name)) {
        val n = node.getNode(name)
        if (directory && "nt:folder" == n.getPrimaryNodeType.getName ||
           !directory && "nt:file" == n.getPrimaryNodeType.getName)
          create(n)
        else
          null
      }
      else
        null
    }

    override def fileNamed(name: String): AbstractFile = {
      valueOrElse(lookupName(name, false)) {
        val file = node.addNode(name, "nt:file")
        val content = file.addNode("jcr:content", "nt:resource")
        content.setProperty("jcr:mimeType", "application/octet-stream") // todo fix: dont hc MIME
        content.setProperty("jcr:data", emptyInputStream)
        content.setProperty("jcr:lastModified", System.currentTimeMillis)
        node.save()
        create(file)
      }
    }

    override def subdirectoryNamed(name: String): AbstractFile = {
      valueOrElse(lookupName(name, true)) {
        val dir = node.addNode(name, "nt:folder")
        node.save()
        create(dir)
      }
    }
  }

  case class JcrFile(node: Node) extends JcrNode(node) {
    def contentNode: Node =
      if (node.hasNode("jcr:content")) node.getNode("jcr:content")
      else null

    def dataProperty: Property =
      nullOrElse(contentNode) { node =>
        if (node.hasProperty("jcr:data")) node.getProperty("jcr:data")
        else null
      }

    def isDirectory = false
    def input: InputStream = dataProperty match {
      case prop: Property => prop.getStream
      case null => emptyInputStream
    }

    def output: OutputStream = new ByteArrayOutputStream() {
      override def close() {
        super.close()
        val content = valueOrElse(contentNode) {
          node.addNode("jcr:content", "nt:resource")
        }
        content.setProperty("jcr:lastModified", System.currentTimeMillis)
        content.setProperty("jcr:data", new ByteArrayInputStream(buf, 0, size))
        node.save()
      }
    }

    override def size: Option[Int] = {
      val p = dataProperty
      if (p == null) None
      else Some(p.getLength.toInt)
    }

    def elements: Iterator[AbstractFile] = Iterator.empty
    def lookupName(name: String, directory: Boolean): AbstractFile = null
  }

}

}