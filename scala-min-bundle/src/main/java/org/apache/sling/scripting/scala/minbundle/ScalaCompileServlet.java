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

import javax.servlet.ServletException;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;

import scala.tools.nsc.Interpreter;
import scala.tools.nsc.Settings;

/**
 * A SlingSafeMethodsServlet that renders the current Resource as simple HTML
 * 
 * @scr.component immediate="true" metatype="no"
 * @scr.service interface="javax.servlet.Servlet"
 * 
 * @scr.property name="service.description" value="Scale compiler test servlet"
 * @scr.property name="service.vendor" value="The Apache Software Foundation"
 * 
 * Use this as a default servlet for Sling
 * @scr.property name="sling.servlet.resourceTypes"
 *               value="sling/servlet/default"
 *               
 * @scr.property name="sling.servlet.methods" value="GET"
 * @scr.property name="sling.servlet.selectors" value="compile"
 * @scr.property name="sling.servlet.extensions" value="scala"
 */
public class ScalaCompileServlet extends SlingSafeMethodsServlet {
    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) 
    throws ServletException, IOException 
    {
    	String message = request.getParameter("message");
    	if(message == null) {
    		message = "This message was produced by a Scala script, use the message request parameter to replace it";
    	}
	    final String code = "object MyClass { def execute = Console.println(\"" + message + "\") }";
	    Interpreter interp = new Interpreter( new Settings(null) );
	    interp.compileString(code);
	    
	    final ByteArrayOutputStream out = new ByteArrayOutputStream();
	    final PrintStream oldOut = System.out;
	    try {
	    	response.setContentType("text/plain");
	    	System.setOut(new PrintStream(response.getOutputStream()));
		    interp.interpret("MyClass.execute");
		    out.flush();
	    } finally {
	    	System.setOut(oldOut);
	    }
    }
}