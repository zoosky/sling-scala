/*
 * ScalaScriptEngineFactory.java
 *
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.apache.sling.scripting.scala;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringWriter;

import javax.script.ScriptEngineFactory;
import javax.script.ScriptContext;
import javax.script.Bindings;
import javax.script.ScriptException;


import scala.tools.nsc.Interpreter;
import scala.tools.nsc.Settings;

import org.apache.sling.scripting.api.AbstractSlingScriptEngine;
/**
 *
 * @author Janandith
 */

public class ScalaScriptEngine extends AbstractSlingScriptEngine{
    
	Settings interpreterSettings = null;
	Interpreter interpreter = null;
	PrintWriter logPrinter = null;
	StringWriter  interpreterOut = null;	
	
    /** Creates a new instance of ScalaScriptEngine */
    public ScalaScriptEngine(ScriptEngineFactory factory) {
        
        super(factory);
		try{
			
			interpreterOut  = new StringWriter();
			logPrinter = new PrintWriter(interpreterOut);		
			interpreterSettings = new Settings();
			
		
			interpreter = new Interpreter(interpreterSettings , logPrinter);
			
			}catch(Exception e){
				
				e.printStackTrace();
				
			}

    }
    
    public Object eval(Reader script , ScriptContext scriptContext) throws ScriptException {   
    
    	Object result =null;
	
	    try {
	        StringBuffer scriptString = new StringBuffer();
	        BufferedReader bufferedScript = new BufferedReader(script);
	        String nextLine = bufferedScript.readLine();
	        
	        while (nextLine != null) {
	            scriptString.append(nextLine);
	            scriptString.append("\n");
	            nextLine = bufferedScript.readLine();
	        }
	
		 result = interpreter.interpret(scriptString.toString());
		 
		 scriptContext.getWriter().write(interpreterOut.toString());
	    }
	    catch(Exception ex)
	    {
	    	ex.printStackTrace();
	    }
		
		return result;
	
    }

	
        
}
