package org.apache.sling.scripting.scala;

import java.io.IOException;
import java.net.URISyntaxException;


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

}
