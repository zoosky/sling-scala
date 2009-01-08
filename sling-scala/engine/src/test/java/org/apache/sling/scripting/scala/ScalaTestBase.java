package org.apache.sling.scripting.scala;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.net.URL;

import org.apache.sling.commons.testing.jcr.RepositoryTestBase;
import org.apache.sling.scripting.scala.interpreter.ScalaInterpreter;

import scala.Console;
import scala.Tuple2;
import scala.collection.immutable.EmptyMap;
import scala.collection.immutable.Map;
import scala.tools.nsc.Settings;
import scala.tools.nsc.reporters.ConsoleReporter;

public class ScalaTestBase extends RepositoryTestBase {
    public static final String NL = System.getProperty("line.separator");

    protected static final String SCRIPT_PATH = "/scripts/";

    private ScalaInterpreter interpreter;
    private ByteArrayOutputStream interpreterOut;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        Settings settings = new Settings();
        String testCp = System.getProperty("surefire.test.class.path");
        String javaCp = System.getProperty("java.class.path");
        settings.classpath().v_$eq(testCp != null ? testCp : javaCp);

        interpreter = new ScalaInterpreter(settings, null,
                new ConsoleReporter(settings, null, new PrintWriter(Console.out())));

        interpreterOut = new ByteArrayOutputStream();
        interpreter.stdOut_$eq(interpreterOut);
    }

    protected String getScript(String scriptName) throws URISyntaxException, IOException {
        URL url = getClass().getResource(SCRIPT_PATH + scriptName);
        BufferedReader reader = new BufferedReader(new FileReader(new File(url.toURI())));

        StringBuilder script = new StringBuilder();

        String line;
        while ((line = reader.readLine()) != null) {
            script.append(line).append(NL);
        }

        return script.toString();
    }

    protected String evalScala(String code) {
        Map<String, Tuple2<Object, Class<?>>> bindings = new EmptyMap<String, Tuple2<Object, Class<?>>>();
        return evalScala(createScriptName(), code, bindings);
    }

    protected String evalScala(String code, Map<String, Tuple2<Object, Class<?>>> bindings) {
        return evalScala(createScriptName(), code, bindings);
    }

    protected String evalScala(String name, String code, Map<String, Tuple2<Object, Class<?>>> bindings) {
        interpreterOut.reset();
        interpreter.interprete(name, code, bindings); // todo fix evaluate return value
        return interpreterOut.toString();
    }

    protected String evalScalaScript(String scriptName) throws URISyntaxException, IOException {
        String code = getScript(scriptName);
        return evalScala(code);
    }

    protected String evalScalaScript(String scriptName, Map<String, Tuple2<Object, Class<?>>> bindings)
            throws URISyntaxException, IOException {

        String code = getScript(scriptName);
        return evalScala(scriptName, code, bindings);
    }

    protected String createScriptName() {
        return "testi";
    }

}
