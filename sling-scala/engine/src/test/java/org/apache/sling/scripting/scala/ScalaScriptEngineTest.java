package org.apache.sling.scripting.scala;

import java.io.IOException;
import java.net.URISyntaxException;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.naming.NamingException;

import org.apache.sling.scripting.scala.interpreter.ScalaBindings;


public class ScalaScriptEngineTest extends ScalaTestBase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    public void testEvalString() {
        String code = "print(1 + 2)";
        assertEquals("3", evalScala(code));
    }

    public void testEvalScript() throws URISyntaxException, IOException {
        assertEquals("3", evalScalaScript("simple.scs"));
    }

    public void testNodeAccess() throws RepositoryException, NamingException {
        Node n = getTestRootNode();
        String code = "print(n.getPath)";
        ScalaBindings bindings = new ScalaBindings();
        bindings.put("n", n, Node.class);
        assertEquals(n.getPath(), evalScala(code, bindings));
    }

}
