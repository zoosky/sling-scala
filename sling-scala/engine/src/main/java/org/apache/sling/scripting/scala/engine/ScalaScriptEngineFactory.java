package org.apache.sling.scripting.scala.engine;

import static org.apache.sling.scripting.scala.engine.ExceptionHelper.initCause;

import java.io.File;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;

import javax.script.ScriptEngine;

import org.apache.sling.scripting.api.AbstractScriptEngineFactory;
import org.apache.sling.scripting.scala.interpreter.BundleFS;
import org.apache.sling.scripting.scala.interpreter.ScalaInterpreter;
import org.osgi.framework.Bundle;
import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;

import scala.tools.nsc.Settings;
import scala.tools.nsc.io.AbstractFile;
import scala.tools.nsc.io.PlainFile;
import scala.tools.nsc.reporters.ConsoleReporter;
import scala.tools.nsc.reporters.Reporter;
import scala.tools.nsc.util.Position;
/**
 * @scr.component
 * @scr.service interface="javax.script.ScriptEngineFactory"
 */
public class ScalaScriptEngineFactory extends AbstractScriptEngineFactory {
    private static final String PATH_SEPARATOR = System.getProperty("path.separator");

    public final static String SCALA_SCRIPT_EXTENSION = "scala";
    public final static String SCALA_MIME_TYPE = "application/x-scala";
    public final static String SHORT_NAME = "scala";
    public final static String VERSION = "2.7.2";

    private ScalaInterpreter interpreter; // todo check: thread safe?

    protected void activate(ComponentContext context) {
        Bundle[] bundles = context.getBundleContext().getBundles();
        AbstractFile[] bundleFs = new AbstractFile[bundles.length];
        for (int k = 1; k < bundles.length; k++) { // system bundle is special, leave it out
            bundleFs[k] = BundleFS.create(bundles[k]);
        }

        try { // handle system bundle
            URL url = bundles[0].getResource("/");
            File file = new File(url.toURI());
            bundleFs[0] = new PlainFile(file);
        }
        catch (URISyntaxException e) {
            throw initCause(new IllegalArgumentException("Cant determine url of system bundle"), e);
        }

        URL[] bootUrls = getUrlClassLoader(bundles[0]).getURLs();
        Settings settings = new Settings();
        StringBuilder bootPath = new StringBuilder(settings.classpath().v());
        for (int k = 0; k < bootUrls.length; k++) {
            bootPath.append(PATH_SEPARATOR).append(bootUrls[k].getPath());
        }

        settings.classpath().v_$eq(bootPath.toString());
        interpreter = new ScalaInterpreter(settings, bundleFs,
                new ConsoleReporter(settings, null, new PrintWriter(System.out)));  // todo: redirect compiler messages to log
    }

    public ScalaScriptEngineFactory() {
        super();
        setExtensions(SCALA_SCRIPT_EXTENSION);
        setMimeTypes(SCALA_MIME_TYPE);
        setNames(SHORT_NAME);
    }

    public ScriptEngine getScriptEngine(){
        return new ScalaScriptEngine(interpreter, this);
    }

    public String getLanguageName(){
        return SHORT_NAME;
    }

    public String getLanguageVersion(){
        return VERSION;
    }

    // -----------------------------------------------------< private >---

    private static URLClassLoader getUrlClassLoader(Bundle bundle) {
        ClassLoader classLoader = bundle.getClass().getClassLoader().getParent();
        if (!(classLoader instanceof URLClassLoader)) {
            throw new IllegalArgumentException("Classloader of bundle is not a URLClassLoader");
        }
        return (URLClassLoader) classLoader;
    }

    // todo fix: use for redirecting compiler output
    private class LogReporter extends Reporter {
        private final Logger log;

        public LogReporter(Logger log) {
            super();
            this.log = log;
        }

        @Override
        public void info0(Position pos, String msg, Severity severity, boolean force) {
            if (INFO().equals(severity)) {
                log.info("{}: {}", pos, msg);
            }
            else if (WARNING().equals(severity)) {
                log.warn("{}: {}", pos, msg);
            }
            else if (ERROR().equals(severity)) {
                log.error("{}: {}", pos, msg);
            }
            else {
                log.warn("{}: {}", pos, msg);
            }
        }

    }

}


