import scala.collection.Map
import scala.collection.mutable.HashMap

package org.apache.sling.scripting.scala {

class Bindings extends Map[String, Tuple2[AnyRef, Class[_]]] {

  private val bindings = new HashMap[String, Tuple2[AnyRef, Class[_]]]

  def size = bindings.size
  def get(key: String) = bindings.get(key)
  def elements = bindings.elements

  def getValue(key: String): AnyRef = if (bindings.contains(key)) bindings(key)._1 else null
  def getClass(key: String): Class[_] = if (bindings.contains(key)) bindings(key)._2 else null
}

}