/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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

