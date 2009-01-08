import scala.collection.Map
import scala.collection.mutable.HashMap

package org.apache.sling.scripting.scala.interpreter {

trait Argument[T <: AnyRef] {
  def getValue: T
  def getType: Class[T]
}

trait Bindings extends Map[String, Argument[_]] {
  def put(key: String, value: Argument[_]): Argument[_]

  def put[T <: AnyRef](key: String, value: T, clazz: Class[T]): Argument[_] =
    put(key, new Argument[T] {
      def getValue: T = value
      def getType: Class[T] = clazz
    })

  def getValue(key: String): AnyRef =
    get(key) match {
      case Some(a) => a.getValue
      case None => null
    }

  def getType(key: String): Class[_] =
    get(key) match {
      case Some(a) => a.getClass
      case None => null
    }
}

class ScalaBindings extends Bindings {
  private val bindings = new HashMap[String, Argument[_]]

  def size: Int = bindings.size
  def get(key: String): Option[Argument[_]] = bindings.get(key)
  def elements: Iterator[(String, Argument[_])] = bindings.elements

  def put(key: String, value: Argument[_]): Argument[_] =
    bindings.put(key, value) match {
      case Some(a) => a
      case None => null
    }
}

}

