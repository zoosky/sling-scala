/*
 * ScalaScriptEngine.java
 *
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.apache.sling.scripting.scala;


import javax.jcr.Node;
import javax.jcr.Property;
import javax.jcr.Value;
import javax.naming.NamingException;

import java.io.File;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.LinkedList;

import javax.script.ScriptContext;
import javax.script.Bindings;
import javax.script.ScriptException;
import javax.script.ScriptEngineFactory;
import java.util.Map;
import java.util.HashMap;
import javax.jcr.RepositoryException;

import scala.tools.nsc.Interpreter;
import scala.tools.nsc.Settings;

import org.apache.sling.api.scripting.SlingBindings;
import org.apache.sling.api.scripting.SlingScriptHelper;
import org.apache.sling.scripting.api.AbstractSlingScriptEngine;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.jcr.api.SlingRepository;
import org.apache.sling.commons.testing.jcr.RepositoryTestBase;
import org.apache.sling.commons.json.jcr.JsonItemWriter;
//import org.apache.sling.commons.testing.jcr.RepositoryTestBase;
/**
 *
 * @author Janandith
 */

public class ScalaScriptEngine extends AbstractSlingScriptEngine{
    
	private Settings scalaInterpreterSettings = null;
	private Interpreter scalaInterpreter = null;
	private PrintWriter interpreterOutputStream = null;
	private StringWriter interpreterTextOutput = null;	

	/** Defining library paths */
	
    private String scalaLibrary = "/tmp/scala-library-2.7.1.jar";
    private String jcrNodeLibrary = "/tmp/jcr-1.0.jar";
//    private String jackrabbitAPI = "/tmp/jackrabbit-api-1.4.jar";
//    private String jackrabbitClassloader = "/tmp/jackrabbit-classloader-1.4.jar";
//    private String jackrabbitCommons = "/tmp/jackrabbit-jcr-commons-1.4.2.jar";
//    private String jackrabitCore = "/tmp/jackrabbit-core-1.4.3.jar";
//    private String jackrabbitRmi = "/tmp/jackrabbit-jcr-rmi-1.4.1.jar";
//    private String jackrabbitServer = "/tmp/jackrabbit-jcr-server-1.4.jar";
//    private String jackrabbitOcm = "/tmp/jackrabbit-ocm-1.4.jar";
//    private String jackrabbitSpi = "/tmp/jackrabbit-spi-1.4.jar";
//    private String jackrabbitSpiCommons = "/tmp/jackrabbit-spi-commons-1.4.jar";
//    private String jackrabbitText = "/tmp/jackrabbit-text-extractors-1.4.jar";
//    private String jackrabbitWebDav ="/tmp/jackrabbit-webdav-1.4.jar";
   
    
    private String feedClassPath = scalaLibrary + File.pathSeparator + jcrNodeLibrary ;
//    + File.pathSeparator + jackrabbitAPI + File.pathSeparator + jackrabbitClassloader + File.pathSeparator + jackrabbitCommons + File.pathSeparator + jackrabbitOcm + File.pathSeparator + jackrabbitRmi + File.pathSeparator + jackrabbitServer + File.pathSeparator + jackrabbitSpi + File.pathSeparator + jackrabbitSpiCommons + File.pathSeparator + jackrabbitText + File.pathSeparator + jackrabbitWebDav + File.pathSeparator + jackrabitCore;

    /** Creates a new instance of ScalaScriptEngine */
    public ScalaScriptEngine(ScriptEngineFactory factory) {     
        super(factory);
        /** Initialize output streams */
		interpreterTextOutput  = new StringWriter();
		interpreterOutputStream = new PrintWriter(interpreterTextOutput);		
		scalaInterpreterSettings = new Settings();  
        }
    
    public Object eval(Reader script , ScriptContext scriptContext) throws ScriptException {  

    	
        Bindings bindings = scriptContext.getBindings(ScriptContext.ENGINE_SCOPE);	
        String scriptName = "";   
        
        /** The SlingScriptHelper is the sling object to the script so all public methods in this class can be accessed */
        
        SlingScriptHelper helper = (SlingScriptHelper) bindings.get(SlingBindings.SLING);
      

        if (helper == null) {
            throw new ScriptException("SlingScriptHelper missing from bindings");
        }
        else if(helper != null)
        {
            scriptName = helper.getScript().getScriptResource().getPath();
        }
        
       
        // ensure GET request
        if (!"GET".equals(helper.getRequest().getMethod())) {
            throw new ScriptException("FreeMarker templates only support GET requests");
        }	     

	    try{    	
	        StringBuffer scriptString = new StringBuffer();
	        BufferedReader bufferedScript = new BufferedReader(script);
	        String nextLine = bufferedScript.readLine();
	        
	        while (nextLine != null) {
	            scriptString.append(nextLine);
	            scriptString.append("\n");
	            nextLine = bufferedScript.readLine();
	        }


	        scalaInterpreterSettings.verbose().value_$eq(true);
	        scalaInterpreterSettings.classpath().value_$eq(feedClassPath);
	        scalaInterpreter = new Interpreter(scalaInterpreterSettings , interpreterOutputStream); 
            
            Iterator bindingsIterator = bindings.keySet().iterator();          
            /** Iterating the binding varibles in sling to bind to scala */
            while(bindingsIterator.hasNext()){
            	Object key = bindingsIterator.next();

            	if(key.toString().equals("currentNode")){
            		
//            		Node currentNode = (javax.jcr.Node)bindings.get(key);
//            		Property p = currentNode.getProperty("title");

            		
            		Map<String, Object> b = new HashMap<String, Object>();
            		b.put("node", (javax.jcr.Node)bindings.get("currentNode"));           		
                    scalaInterpreter.bind("b", "java.util.Map[String, Object]" , b);    
//                    scalaInterpreter.bind("currentNode", "Object" , currentNode);
            	}
            }
            
//            String code = "import javax.jcr._; import java.util._; import org.apache.jackrabbit.core._ ; var i = bindings.keySet().iterator(); var b = i.next() ; var c = i.next() ; var d = i.next() ; var e = i.next() ; var f = i.next() ; var g = i.next() ; var n = bindings.get(g); var cast:NodeImpl = n match {  case x:NodeImpl => x  case _ => null }; cast.getProperty(\"title\");";

//            String code = "import javax.jcr._; import java.util._;  var i = currentNode.asInstanceOf[javax.jcr.Node]";            				
            	
            try{      	
//            	scalaInterpreter.compileString(scriptString.toString());
            	scalaInterpreter.interpret(scriptString.toString());
            	
            }catch(Exception ex){
            	throw new Exception("Error: Cannot compile code");
            }
            System.out.println(interpreterTextOutput.toString());
            Writer contextWriter = scriptContext.getWriter();
            contextWriter.write(interpreterTextOutput.toString());
            contextWriter.flush();          
	    }
	    catch(Exception ex)
	    {
	    	ex.printStackTrace();
	    	System.err.println("ERROR: couldn't evaluate script");
	    }

		return null;
	
    }
    
    
//
//    protected Node getNewNode() throws RepositoryException, NamingException {
//        return RepositoryTestBase.getTestRootNode().addNode("testi" + (++RepositoryTestBase.counter));
//    }


}
