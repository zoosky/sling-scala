package org.apache.sling.scripting.scala;

import javax.script.ScriptEngineFactory;

import junit.framework.TestCase;

import org.apache.sling.scripting.scala.engine.ScalaScriptEngineFactory;

public class ScalaScriptEngineFactoryTest extends TestCase {

    public void testScriptEngineFactoryInit() {
        ScriptEngineFactory scalaEngineFactory = new ScalaScriptEngineFactory();
        assertNotNull(scalaEngineFactory);
    }

    public void testScriptEngineFactoryEngine() {
        try {
            new ScalaScriptEngineFactory().getScriptEngine();
            assertTrue("Expecting IllegalStateException", false);
        }
        catch (IllegalStateException e) {
            // expected
        }
    }

    public void testScriptEngineFactoryLanguage() {
        String language = new ScalaScriptEngineFactory().getLanguageName();
        assertEquals("scala", language);
    }

    public void testScriptEngineFactoryLanguageVersion() {
        String version = new ScalaScriptEngineFactory().getLanguageVersion();
        assertEquals("2.7.2", version);
    }

}
