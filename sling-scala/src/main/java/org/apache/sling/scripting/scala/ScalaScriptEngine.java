/*
 * ScalaScriptEngineFactory.java
 *
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.apache.sling.scripting.scala;

import org.apache.sling.jcr.api.SlingRepository;
import org.apache.sling.jcr.resource.JcrResourceUtil;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;

import javax.jcr.Node;
import javax.jcr.Property;
import javax.jcr.Value;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.io.PrintWriter;
import java.lang.ClassLoader;
import java.lang.Thread;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;

import javax.script.ScriptContext;
import javax.script.Bindings;
import javax.script.ScriptException;
import javax.script.ScriptEngineFactory;


import scala.tools.nsc.Interpreter;
import scala.tools.nsc.Settings;


import org.apache.sling.api.scripting.SlingBindings;
import org.apache.sling.api.scripting.SlingScriptHelper;
import org.apache.sling.scripting.api.AbstractSlingScriptEngine;
import org.apache.sling.api.resource.Resource;

/**
 *
 * @author Janandith
 */

public class ScalaScriptEngine extends AbstractSlingScriptEngine{
    
	Settings iSettings = null;
	Interpreter interpreter = null;
	PrintWriter logPrinter = null;
	StringWriter  interpreterOut = null;	
	String outt =null;
	ScalaSlingResourceWrapper wp = new ScalaSlingResourceWrapper();
	String code = null;

    /** Creates a new instance of ScalaScriptEngine */
    public ScalaScriptEngine(ScriptEngineFactory factory) {
        
        super(factory);
		
		try{			
			interpreterOut  = new StringWriter();
			logPrinter = new PrintWriter(interpreterOut);		
			iSettings = new Settings(null);
			
			}catch(Exception e){
				
				e.printStackTrace();
				
			}
   
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
        
        /** This try block defines the code to compile  - as shown by Bertrand*/
        try{
            // define and compile test code
            final String prefix = "This is the title of the currentNode: ";
            code = "object MyClass { def execute = Console.println(\"" + prefix + " var x = new currentNode() x.title \") }";

        }
        catch(Exception e){
        	
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

	        /** Manually loading scala library */
	        
	        File libJar = null;
            libJar = new File("/tmp/scala-library-2.7.1.jar");
            if(!(libJar.exists())) {
//                    throw new IOException("Scala library must be available at " + libJar.getAbsolutePath());
            }
            

  
            final Settings settings = new Settings(null);
            settings.verbose().v_$eq(true);
            if(libJar != null) {
            	settings.classpath().value_$eq(libJar.getAbsolutePath());
            }

            Iterator valueIterator = bindings.keySet().iterator();
            interpreter = new Interpreter(settings , logPrinter);           
            /** Iterating the binding varibles in sling to bind to scala */
            while(valueIterator.hasNext()){
            	Object key = valueIterator.next();
            	Object mappedObject = bindings.get(key);
            	System.out.println("variable        " + key.toString() + "value     " + mappedObject.toString());

            	interpreter.bind( key.toString() , "Object" , mappedObject);
            	
            }
            

            interpreter.compileString(code);
        

            try {

            	interpreter.interpret("MyClass.execute");

            } finally {

            }

//            System.out.println(interpreterOut.toString());
            Writer wr = scriptContext.getWriter();
			wr.write(interpreterOut.toString());
			wr.flush();          
	
	 
	    }
	    catch(Exception ex)
	    {
	    	ex.printStackTrace();
	    }



		return null;
	
    }

}
