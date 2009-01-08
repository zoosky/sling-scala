package org.apache.sling.scripting.scala;

import java.io.IOException;
import java.net.URISyntaxException;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.naming.NamingException;

import scala.Tuple2;
import scala.collection.immutable.EmptyMap;
import scala.collection.immutable.Map;


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
        // todo fix: make bindings more convenient to use
        Map<String, Tuple2<Object, Class<?>>> bindings = new EmptyMap<String, Tuple2<Object, Class<?>>>();
        bindings = bindings.update("n", new Tuple2<Object, Class<?>>(n, Node.class));
        assertEquals(n.getPath(), evalScala(code, bindings));
    }

}
