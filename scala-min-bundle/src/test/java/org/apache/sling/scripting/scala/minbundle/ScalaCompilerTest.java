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
package org.apache.sling.scripting.scala.minbundle;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import junit.framework.TestCase;
import scala.tools.nsc.Interpreter;
import scala.tools.nsc.Settings;

/** Minimal test of the Scala compiler, embedded */
public class ScalaCompilerTest extends TestCase {
	
	public void testScalaCompile() throws IOException {
	    final String code = "object MyClass { def execute = Console.println(\"this is my script\") }";
	    Interpreter interp = new Interpreter( new Settings(null) );
	    interp.compileString(code);
	    
	    final ByteArrayOutputStream out = new ByteArrayOutputStream();
	    final PrintStream oldOut = System.out;
	    try {
	    	System.setOut(new PrintStream(out));
		    interp.interpret("MyClass.execute");
		    out.flush();
	    } finally {
	    	System.setOut(oldOut);
	    }
	    
	    assertEquals("this is my script", out.toString().trim());
	}
}