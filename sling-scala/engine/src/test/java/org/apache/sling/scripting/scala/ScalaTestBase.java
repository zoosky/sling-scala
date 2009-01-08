package org.apache.sling.scripting.scala;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map.Entry;

import javax.script.Bindings;

import org.apache.sling.commons.testing.jcr.RepositoryTestBase;

import scala.tools.nsc.Interpreter;
import scala.tools.nsc.Settings;

/**
 * todo: update to ScalaInterpreter
 * Base class to scala tests
 */
public class ScalaTestBase extends RepositoryTestBase {
    protected static final String SCRIPTS = "/scripts/";

    protected String evalScala(String code, Bindings bindings)
            throws IOException {

        Settings settings = new Settings();
        // settings.classpath().value_$eq(ScalaScriptEngine.getCompilerClassPath());
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Interpreter scalaInterpreter = new Interpreter(settings);
        scalaInterpreter.beQuiet();

        if (bindings != null) {
            for (Object o : bindings.entrySet()) {
                @SuppressWarnings("unchecked")
                Entry<String, Object> e = (Entry<String, Object>) o;
                scalaInterpreter.bind(e.getKey(), e.getValue().getClass().getName(), e.getValue());
            }
        }

        scalaInterpreter.bind("__sysout", out.getClass().getName(), out);
        scalaInterpreter.interpret("Console.setOut(__sysout)");

        scalaInterpreter.interpret(code.trim());
        out.flush();
        return out.toString().trim();
    }

    protected String evalScala(String code) throws IOException {
        return evalScala(code, null);
    }

    protected String evalScalaScript(String name, Bindings bindings) throws IOException {
        InputStream is = getClass().getResourceAsStream(name);
        if (is == null) {
            throw new IOException("Class resource not found at path " + name);
        }

        byte[] buffer = new byte[16384];
        int n;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        while ((n = is.read(buffer, 0, buffer.length)) > 0) {
            bos.write(buffer, 0, n);
        }

        String encoding = "UTF-8";
        return evalScala(new String(buffer, encoding), bindings);
    }

    protected String evalScalaScript(String name) throws IOException {
        return evalScalaScript(name, null);
    }

}
