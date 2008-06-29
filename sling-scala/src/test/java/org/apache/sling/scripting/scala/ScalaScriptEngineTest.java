package org.apache.sling.scripting.scala;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.FileReader;

import javax.script.Bindings;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;

import org.apache.sling.scripting.scala.ScalaScriptEngineFactory;
import org.apache.sling.scripting.scala.ScalaScriptEngine;

import scala.tools.nsc.Interpreter;
import scala.tools.nsc.Settings;

import junit.framework.TestCase;
import javax.jcr.Node;
import java.lang.Object;

public class ScalaScriptEngineTest extends TestCase{

	
    private String scalaLibrary = "/tmp/scala-library/2.7.1/scala-library-2.7.1.jar";
    private String jcrNodeLibrary = "/tmp/jcr-1.0.jar";
    private String jackrabbitAPI = "/tmp/jackrabbit-api-1.4.jar";
    private String jackrabbitClassloader = "/tmp/jackrabbit-classloader-1.4.jar";
    private String jackrabbitCommons = "/tmp/jackrabbit-jcr-commons-1.4.2.jar";
    private String jackrabitCore = "/tmp/jackrabbit-core-1.4.3.jar";
    private String jackrabbitRmi = "/tmp/jackrabbit-jcr-rmi-1.4.1.jar";
    private String jackrabbitServer = "/tmp/jackrabbit-jcr-server-1.4.jar";
    private String jackrabbitOcm = "/tmp/jackrabbit-ocm-1.4.jar";
    private String jackrabbitSpi = "/tmp/jackrabbit-spi-1.4.jar";
    private String jackrabbitSpiCommons = "/tmp/jackrabbit-spi-commons-1.4.jar";
    private String jackrabbitText = "/tmp/jackrabbit-text-extractors-1.4.jar";
    private String jackrabbitWebDav ="/tmp/jackrabbit-webdav-1.4.jar";
    private String feedClassPath = "" + scalaLibrary + File.pathSeparator + jcrNodeLibrary + File.pathSeparator + jackrabbitAPI + File.pathSeparator + jackrabbitClassloader + File.pathSeparator + jackrabbitCommons + File.pathSeparator + jackrabbitOcm + File.pathSeparator + jackrabbitRmi + File.pathSeparator + jackrabbitServer + File.pathSeparator + jackrabbitSpi + File.pathSeparator + jackrabbitSpiCommons + File.pathSeparator + jackrabbitText + File.pathSeparator + jackrabbitWebDav + File.pathSeparator + jackrabitCore;


		public void testScriptEngineScalaIteratorSimpleScript(){
			
			Settings scalaInterpreterSettings = new Settings();
			StringWriter interpreterTextOutput = new StringWriter();		
			PrintWriter interpreterOutputStream = new PrintWriter(interpreterTextOutput);
			scalaInterpreterSettings.classpath().value_$eq(feedClassPath);
			Interpreter scalaInterpreter = new Interpreter(scalaInterpreterSettings,interpreterOutputStream);
			
			String code = "4+2";
			scalaInterpreter.interpret(code);
			System.out.println("Interpreter Output:");
			System.out.println(interpreterTextOutput.toString());			
			assertEquals("res0: Int = 6", interpreterTextOutput.toString());
		}
		
		public void testScriptEngineScalaIteratorBindString(){
			
			Settings scalaInterpreterSettings = new Settings();
			StringWriter interpreterTextOutput = new StringWriter();		
			PrintWriter interpreterOutputStream = new PrintWriter(interpreterTextOutput);
			scalaInterpreterSettings.classpath().value_$eq(feedClassPath);
			
			Interpreter scalaInterpreter = new Interpreter(scalaInterpreterSettings,interpreterOutputStream);
			String s = "This is the bound String: value";
			scalaInterpreter.bind("testString", "java.lang.String" , s);
			
			String code = readScript("TestScriptOne.scala");
			scalaInterpreter.interpret(code);
			System.out.println("Interpreter Output:");
			System.out.println(interpreterTextOutput.toString());			
			
		}		
	
//		public void testScriptEngineScalaIteratorBindingNode(){
//			
//			Settings scalaInterpreterSettings = new Settings();
//			StringWriter interpreterTextOutput = new StringWriter();		
//			PrintWriter interpreterOutputStream = new PrintWriter(interpreterTextOutput);
//			scalaInterpreterSettings.classpath().value_$eq(feedClassPath);
//			Interpreter scalaInterpreter = new Interpreter(scalaInterpreterSettings,interpreterOutputStream);
//
//		
//			scalaInterpreter.bind("n", "javax.jcr.Node" , new Node());
//			String code = readScript("TestScriptTwo.scala");
//			scalaInterpreter.interpret(code);
//			System.out.println("Interpreter Output:");
//			System.out.println(interpreterTextOutput.toString());			
//			
//		}		
//
//		public void testScriptEngineScalaIteratorBindBinding(){
//			
//			Settings scalaInterpreterSettings = new Settings();
//			StringWriter interpreterTextOutput = new StringWriter();		
//			PrintWriter interpreterOutputStream = new PrintWriter(interpreterTextOutput);
//			scalaInterpreterSettings.classpath().value_$eq(feedClassPath);
//			Interpreter scalaInterpreter = new Interpreter(scalaInterpreterSettings,interpreterOutputStream);		
//			Node n;
//			Bindings bind;
//			bind.put("currentNode", n);
//			scalaInterpreter.bind("bind", "javax.script.Bindings" , bind);
//			String code = readScript("TestScriptThree.scala");
//			scalaInterpreter.interpret(code);
//			System.out.println("Interpreter Output:");
//			System.out.println(interpreterTextOutput.toString());			
//			
//		}	
		
		
	    public String readScript(String file){
	    	
	    	File script = new File(file);
	    	
	        StringBuffer scriptString = new StringBuffer();
	        try{
	        BufferedReader bufferedScript = new BufferedReader(new FileReader(script));
	        String nextLine = bufferedScript.readLine();
	        
	        while (nextLine != null) {
	            scriptString.append(nextLine);
	            scriptString.append("\n");
	            nextLine = bufferedScript.readLine();
	        } 
	        }catch(FileNotFoundException e){
	        	System.err.println("ERROR: Cannot open File");
	        }catch(IOException e){
	        	System.err.println("ERROR: Cannot read File");
	        }
	        return scriptString.toString();
	    }


	
}
