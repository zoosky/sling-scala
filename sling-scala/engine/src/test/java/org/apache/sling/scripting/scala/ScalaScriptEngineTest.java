package org.apache.sling.scripting.scala;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.naming.NamingException;
import javax.script.ScriptException;

import org.apache.sling.scripting.scala.interpreter.ScalaBindings;

public class ScalaScriptEngineTest extends ScalaTestBase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    public void testEvalString() throws ScriptException, InvocationTargetException {
        String code = "print(1 + 2)";
        assertEquals("3", evalScala(code));
    }

    public void testEvalError() throws InvocationTargetException {
        String code = "syntax error";
        try {
            evalScala(code);
            assertTrue("Expecting ScriptException", false);
        }
        catch (ScriptException e) {
            // expected
        }
    }

    public void testError() throws ScriptException {
        String err = "Some error here";
        String code = "throw new Error(\"" + err + "\")";
        try {
            evalScala(code);
            assertTrue("Expecting InvocationTargetException", false);
        }
        catch (InvocationTargetException e) {
            Throwable inner = e.getCause();
            assertEquals("Inner exception is java.lang.Error", Error.class, inner.getClass());
            assertEquals("Inner exception message is \"" + err + "\"", err, inner.getMessage());
        }
    }

    public void testEvalScript() throws URISyntaxException, IOException, ScriptException,
            InvocationTargetException {

        assertEquals("3", evalScalaScript("simple.scs"));
    }

    public void testNodeAccess() throws RepositoryException, NamingException, ScriptException, InvocationTargetException {
        Node n = getTestRootNode();
        String code = "print(n.getPath)";
        ScalaBindings bindings = new ScalaBindings();
        bindings.put("n", n, Node.class);
        assertEquals(n.getPath(), evalScala(code, bindings));
    }

}
