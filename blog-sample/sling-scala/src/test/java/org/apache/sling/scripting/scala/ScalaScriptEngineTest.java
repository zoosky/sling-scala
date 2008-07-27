package org.apache.sling.scripting.scala;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ScalaScriptEngineTest extends ScalaTestBase {

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
}
