
/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.sling.scripting.scala;

import junit.framework.TestCase;

public class NodeBindingTest extends TestCase {
	public void testNothing() {
	}
}

//import java.io.File;
//import java.io.PrintWriter;
//import java.io.StringWriter;
//import java.text.SimpleDateFormat;
//import java.util.Calendar;
//import java.util.HashMap;
//import java.util.Map;
//import org.junit.*;
//
//import javax.jcr.Node;
//import javax.jcr.Property;
//import javax.naming.NamingException;
//
//import org.apache.sling.scripting.RepositoryScriptingTestBase;
//import org.apache.sling.scripting.ScriptEngineHelper;
//import org.apache.sling.commons.json.jcr.JsonItemWriter;
//
//import scala.tools.nsc.Interpreter;
//import scala.tools.nsc.Settings;
///** Test the ScriptableNode class "live", by retrieving
// *  Nodes from a Repository and executing javascript code
// *  using them.
// */
//public class NodeBindingTest extends RepositoryScriptingTestBase {
//
//    private Node node;
//    private Property textProperty;
//    private String testText;
//    private Property numProperty;
//    private double testNum;
//    private Property calProperty;
//    private Calendar testCal;
//    private ScriptEngineHelper.Data data;
//    protected ScriptEngineHelper script;
//    
//	private StringWriter interpreterTextOutput  = new StringWriter();   
//	private Settings scalaInterpreterSettings = new Settings();
//	private PrintWriter interpreterOutputStream = new PrintWriter(interpreterTextOutput);
//	private Interpreter scalaInterpreter = null;
//	
//    @Override
//    protected void setUp() throws Exception {
//        super.setUp();
//        script = new ScriptEngineHelper();
//        node = getNewNode();
//        testText = "Test-" + System.currentTimeMillis();
//        node.setProperty("text", testText);
//        node.setProperty("otherProperty", node.getPath());
//        
//        testNum = System.currentTimeMillis();
//        node.setProperty("num", testNum);
//        
//        testCal = Calendar.getInstance();
//        node.setProperty("cal", testCal);
//        
//        data = new ScriptEngineHelper.Data();
//        data.put("node", node);
//        textProperty = node.getProperty("text");
//        data.put("property", textProperty);
//        numProperty = node.getProperty("num");
//        data.put("numProperty", numProperty);
//        calProperty = node.getProperty("cal");
//        data.put("calProperty", calProperty);
//    }
//
//    public void testCodeExecuteSuccess() throws Exception {
////        final ScriptEngineHelper.Data data = new ScriptEngineHelper.Data();
////        data.put("node", getTestRootNode());
//		Map<String, Object> bindings = new HashMap<String, Object>(); 
//		bindings.put("currentNode" , node);
//        scalaInterpreter.bind("bindings", "java.util.Map[String, Object]" , bindings); 
//        String code =
//        	   "import javax.jcr.Node; " +
//        	   "println(\"this is my script\"); " +
//        	   "val node = bindings.get(\"currentNode\"); " +
//        	   "val n: Node = node.asInstanceOf[Node]; " +
//        	   "println(\"node \" + n.getName)";
//      
//    	Object r = scalaInterpreter.interpret(scriptString.toString());
//    	String expected = "Success";
//    	String result = r.toString();
//        assertEquals(expected , result);
//        // reult gives Success if code was executed correctly
//        // Actual result may not be the same every time because I havent got the correct result I expect
//        // It gives a better picture if the evaluation procedure is visible therefore the interpreter's output is 
//        //printed to the console.
//    	System.out.println(interpreterTextOutput.toString());
//
//        
//    }
//
//
//    
//    protected void initInterpreter(String name , String type , String Value){
//        String scalaLibrary = "/tmp/scala-library-2.7.1.jar";
//        String jcrNodeLibrary = "/tmp/jcr-1.0.jar";
//        String feedClassPath = scalaLibrary + File.pathSeparator + jcrNodeLibrary ;
//				
//        scalaInterpreterSettings.verbose().value_$eq(true);
//        scalaInterpreterSettings.classpath().value_$eq(feedClassPath);
//        scalaInterpreter = new Interpreter(scalaInterpreterSettings , interpreterOutputStream); 
//           
//    }
//}
