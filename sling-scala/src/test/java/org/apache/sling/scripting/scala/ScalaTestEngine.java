package org.apache.sling.scripting;


import java.io.OutputStreamWriter;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.Reader;
import java.io.StringWriter;

import javax.script.Bindings;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptException;
import javax.script.SimpleBindings;
import javax.script.SimpleScriptContext;

import org.apache.sling.scripting.scala.ScalaScriptEngineFactory;
import scala.tools.nsc.Interpreter;
import scala.tools.nsc.Settings;


public class ScalaTestEngine {


	    private static ScriptEngine engine;

	    public static class Data extends HashMap<String, Object> {
	    }

	    private static ScriptEngine getEngine() {
	        if (engine == null) {
	            synchronized (ScalaTestEngine.class) {
	                engine = new ScalaScriptEngineFactory().getScriptEngine();
	            }
	        }
	        return engine;
	    }


	
}
