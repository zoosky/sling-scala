/*
 * ScalaScriptEngineFactory.java
 *
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.apache.sling.scripting.scala;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;

import javax.script.Bindings;
import javax.script.ScriptContext;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptException;

import org.apache.sling.api.scripting.SlingBindings;
import org.apache.sling.api.scripting.SlingScriptHelper;
import org.apache.sling.scripting.api.AbstractSlingScriptEngine;

import scala.tools.nsc.Interpreter;
import scala.tools.nsc.Settings;

/**
 *
 * @author Janandith
 */

public class ScalaScriptEngine extends AbstractSlingScriptEngine{
    
	Settings interpreterSettings = null;
	Interpreter interpreter = null;
	PrintWriter logPrinter = null;
	StringWriter  interpreterOut = null;	
//    	File outFile = new File("log_scala.txt");
//    	BufferedWriter writer;
    /** Creates a new instance of ScalaScriptEngine */
    public ScalaScriptEngine(ScriptEngineFactory factory) {
        
        super(factory);
		try{
			
			interpreterOut  = new StringWriter();
			logPrinter = new PrintWriter(interpreterOut);		
			interpreterSettings = new Settings();
//			
//		
			interpreter = new Interpreter(interpreterSettings , logPrinter);
	// 		writer = new BufferedWriter(new FileWriter(engineLog));
			
			}catch(Exception e){
				
				e.printStackTrace();
				
			}

    }
    
    public Object eval(Reader script , ScriptContext scriptContext) throws ScriptException {  


        Bindings bindings = scriptContext.getBindings(ScriptContext.ENGINE_SCOPE);	
        String scriptName;        
        SlingScriptHelper helper = (SlingScriptHelper) bindings.get(SlingBindings.SLING);
		Object result =null;
		
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
    
     //   String scriptName = helper.getScript().getScriptResource().getPath();
//    	Object result =null;
//	

	    
	    try {
//	    	BufferedWriter writer = new BufferedWriter(new FileWriter(outFile));    	
//	    	writer.write("Scala eval called");	  
//	    	System.out.println("Scala eval called");
	    	    	
	    	
	        StringBuffer scriptString = new StringBuffer();
	        BufferedReader bufferedScript = new BufferedReader(script);
	        String nextLine = bufferedScript.readLine();
	        
	        while (nextLine != null) {
	            scriptString.append(nextLine);
	            scriptString.append("\n");
	            nextLine = bufferedScript.readLine();
	        }

//		String message = new String("Scala Engine");
	    result = interpreter.interpret(scriptString.toString());	        
		Writer wr = scriptContext.getWriter();
		wr.write(interpreterOut.toString());
		wr.flush();          
	
//        writer.write("Engine scala running");
//	    writer.newLine();
//	    writer.close(); 

	   // throw new ScriptException("Failure running script " + scriptName);



//	
//		 result = interpreter.interpret(scriptString.toString());
//		 
		 
	    } catch (Exception e) {
	        final ScriptException se = new ScriptException("Failure running script : " + e);
	        se.initCause(e);
	        throw se;
	    }
		
		return null;
	
    }

	
        
}
