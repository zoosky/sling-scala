package org.apache.sling.scripting.scala;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.io.StringWriter;
import java.net.URISyntaxException;
import java.net.URL;

import javax.script.ScriptEngine;
import javax.script.ScriptException;

import org.apache.sling.scripting.scala.engine.ScalaScriptEngineFactory;

// todo add tests
public class ScalaScriptEngineTest extends ScalaTestBase {

    private ScriptEngine scalaScriptEngine;
    private StringWriter out;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        scalaScriptEngine = new ScalaScriptEngineFactory().getScriptEngine();
        out = new StringWriter();
        scalaScriptEngine.getContext().setWriter(out);
    }

    protected String getScriptOutput() {
        return out.toString().trim();
    }

    protected Reader getScript(String scriptName) throws FileNotFoundException, URISyntaxException {
        URL url = getClass().getResource(SCRIPTS + scriptName);
        return new FileReader(new File(url.toURI()));
    }

    public void testAdd() throws ScriptException {
        scalaScriptEngine.eval("println(2 + 4)");
        assertEquals("6", getScriptOutput());
    }
/*
    public void testSimpleExpression() throws FileNotFoundException, ScriptException, URISyntaxException {
        scalaScriptEngine.eval(getScript("testSimpleExpression.scs"));
        assertEquals("6", getScriptOutput());
    }

    public void testStringBinding() throws IOException, ScriptException, URISyntaxException {
        SimpleBindings bindings = new SimpleBindings();
        String testString = "Hello, scala";
        bindings.put("testString", testString);
        scalaScriptEngine.eval(getScript("testStringBinding.scs"), bindings);
        assertEquals(testString, getScriptOutput());
    }

    public void testNodeBinding() throws RepositoryException, NamingException,
            FileNotFoundException, ScriptException, URISyntaxException {

        Node n = getTestRootNode();
        SimpleBindings bindings = new SimpleBindings();
        bindings.put("node", n);
        scalaScriptEngine.eval(getScript("testNodeBinding.scs"), bindings);
        assertEquals(n.getPath(), getScriptOutput());
    }
*/
}
