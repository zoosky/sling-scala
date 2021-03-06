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

import java.io.File;
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
@SuppressWarnings("serial")
public class ScalaCompileServlet extends SlingSafeMethodsServlet {
    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) 
    throws ServletException, IOException 
    {
    	String message = request.getParameter("message");
    	if(message == null) {
    		message = "This message was produced by a Scala script, use the message request parameter to replace it";
    	}
    	
    	final ClassLoader oldClassLoader = Thread.currentThread().getContextClassLoader();
    	try {
    		Thread.currentThread().setContextClassLoader(getClass().getClassLoader());
    		
    		// Being able to instantiate this (which comes from scala-library) 
    		// demonstrates that the scala Interpreter should find what it needs in the 
    		// current ClassLoader
    		new scala.Random();
    		
        	// SLING-549 - scala Interpreter needs this as it rebuilds its own classpath
        	File libJar = null;
        	libJar = new File("/tmp/scala-library-2.7.1.jar");
        	if(!(libJar.exists())) {
        		throw new IOException("Scala library must be available at " + libJar.getAbsolutePath());
        	}

        	// define and compile test code
        	final String prefix = "If you see this it means that the Scala compiler works: ";
    	    final String code = "object MyClass { def execute = Console.println(\"" + prefix + message + "\") }";
    	    final Settings settings = new Settings(null);
    	    settings.verbose().v_$eq(true);
    	    if(libJar != null) {
    	    	settings.classpath().value_$eq(libJar.getAbsolutePath());
    	    }
    	    Interpreter interp = new Interpreter(settings);
    	    interp.compileString(code);
    	    
    	    // execute test code and send output to response
    	    final PrintStream oldOut = System.out;
    	    try {
    	    	response.setContentType("text/plain");
    	    	System.setOut(new PrintStream(response.getOutputStream()));
    		    interp.interpret("MyClass.execute");
    		    response.getOutputStream().flush();
    	    } finally {
    	    	System.setOut(oldOut);
    	    }
    	    
    	} finally {
    		Thread.currentThread().setContextClassLoader(oldClassLoader);
    	}
    	
    }
}