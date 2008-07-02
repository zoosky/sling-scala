package org.apache.sling.scripting.scala;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;
import scala.tools.nsc.Interpreter;
import scala.tools.nsc.Settings;

public class ScalaScriptEngineTest extends TestCase{

	// TODO SLING-549 for now the Scala compiler needs a 
	// String classpath
	public static final String [] CLASSPATH_FILES = {
		"scala-library-2.7.1.jar",
		
		// TODO for now, test simple non-JCR bindings only
		/*
		"jcr-1.0.jar",
		"jackrabbit-api-1.4.jar",
		"jackrabbit-classloader-1.4.jar",
		"jackrabbit-jcr-commons-1.4.2.jar",
		"jackrabbit-core-1.4.3.jar",
		"jackrabbit-jcr-rmi-1.4.1.jar",
		"jackrabbit-jcr-server-1.4.jar",
		"jackrabbit-ocm-1.4.jar",
		"jackrabbit-spi-1.4.jar",
		"jackrabbit-spi-commons-1.4.jar",
		"jackrabbit-text-extractors-1.4.jar",
		"jackrabbit-webdav-1.4.jar",
		*/
	};
	
	// Need to find the required jars there
	public static final File CLASSPATH_BASE = new File("/tmp/scala-jars"); 
	
	protected static String getClasspath() {
		final StringBuffer sb = new StringBuffer();
		
		for(String str : CLASSPATH_FILES) {
			final File f = new File(CLASSPATH_BASE, str);
			if(!f.exists()) {
				fail("SLING-549 required jar file not found: " + f.getAbsolutePath());
			}
			if(sb.length() > 0) {
				sb.append(File.pathSeparatorChar);
			}
			sb.append(f.getAbsolutePath());
		}
		return sb.toString();
	}
	
	protected String eval(String code, Settings settings, Map<String, Object> bindings) {
		if(settings == null) {
			settings = new Settings();
		}
		
		final StringWriter sw = new StringWriter();		
		final PrintWriter pw = new PrintWriter(sw);
		settings.classpath().value_$eq(getClasspath());
		
		Interpreter scalaInterpreter = new Interpreter(settings,pw);
		if(bindings != null) {
			for(Map.Entry<String, Object> e : bindings.entrySet()) {
				scalaInterpreter.bind(e.getKey(), e.getValue().getClass().getName(),e.getValue());
			}
		}
		
		scalaInterpreter.interpret(code.trim());
		pw.flush();
		
        //System.out.println("eval output:");
        //System.out.println(sw.toString());
		
		return sw.toString().trim();
	}

		public void testSimpleExpression() {
			assertEquals("res0: Int = 6", eval("4+2", null, null));
		}
		
		public void testStringBinding() throws IOException {
			final Map<String, Object> bindings = new HashMap<String, Object>();
			bindings.put("testString", "Hello, scala");
			final String code = readScript("TestScriptOne.scala");
			final String expected = "testString: java.lang.String = Hello, scala"; 
			assertEquals(expected, eval(code, null, bindings));
		}
		
		public void testMultipleStringBindings() {
			for(int i=0; i < 4; i++) {
				final Map<String, Object> bindings = new HashMap<String, Object>();
				bindings.put("testString", "Hello, scala " + i);
				final String code = "Console.println(testString);";
				final String expected = "testString: java.lang.String = Hello, scala " + i; 
				assertEquals("At step " + i, expected, eval(code, null, bindings));
			}
		}
		
	    public String readScript(String path) throws IOException {
	    	
	    	final String base = path.startsWith("/") ? "" : "/test-scripts/"; 
	    	path = base + path;
	    	
	    	final InputStream is = getClass().getResourceAsStream(path);
	    	if(is == null) {
	    		throw new IOException("Class resource not found at path " + path);
	    	}
	    	
	    	final byte [] buffer = new byte[16384];
	    	int n;
	    	final ByteArrayOutputStream bos = new ByteArrayOutputStream(); 
	    	while( (n = is.read(buffer, 0, buffer.length)) > 0) {
	    		bos.write(buffer, 0, n);
	    	}
	    	
	    	final String encoding = "UTF-8";
	    	return new String(buffer, encoding);
	    }


	
}
