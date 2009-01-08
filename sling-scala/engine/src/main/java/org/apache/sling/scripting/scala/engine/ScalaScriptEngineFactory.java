package org.apache.sling.scripting.scala.engine;

import java.io.File;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;

import javax.script.ScriptEngine;

import org.apache.sling.scripting.scala.interpreter.BundleFS;
import org.apache.sling.scripting.scala.interpreter.ScalaInterpreter;

import org.apache.sling.scripting.api.AbstractScriptEngineFactory;
import org.osgi.framework.Bundle;
import org.osgi.service.component.ComponentContext;

import scala.tools.nsc.Settings;
import scala.tools.nsc.io.AbstractFile;
import scala.tools.nsc.reporters.ConsoleReporter;


/**
 * @scr.component
 * @scr.service interface="javax.script.ScriptEngineFactory"
 */
public class ScalaScriptEngineFactory extends AbstractScriptEngineFactory {
    public final static String SCALA_SCRIPT_EXTENSION = "scala";
    public final static String SCALA_MIME_TYPE = "application/x-scala";
    public final static String SHORT_NAME = "scala";

    private ScalaInterpreter interpreter;

    protected void activate(ComponentContext context) {
        Bundle[] bundles = context.getBundleContext().getBundles();
        AbstractFile[] bundleFs = new AbstractFile[bundles.length];
        for (int k = 1; k < bundles.length; k++) { // system bundle is special
            bundleFs[k] = BundleFS.create(bundles[k]);
        }

        try { // system bundle is special
            URL url = bundles[0].getResource("/");
            File file = new File(url.toURI());
            bundleFs[0] = new scala.tools.nsc.io.PlainFile(file);
        }
        catch (URISyntaxException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // todo fix: boot delegation depends on URLClassLoader. Do instanceof check
        URL[] bootUrls = ((URLClassLoader)bundles[0].getClass().getClassLoader().getParent()).getURLs();
        StringBuilder bootPath = new StringBuilder();
        for (int k = 0; k < bootUrls.length; k++) {
            bootPath.append(bootUrls[k].getPath()).append(';');
        }

        Settings settings = new Settings();
        settings.classpath().v_$eq(bootPath.toString()); // todo fix: dont override default, append
        interpreter = new ScalaInterpreter(settings, bundleFs, new ConsoleReporter(settings, null,
                new PrintWriter(System.out)));  // todo: redirect compiler messages to log
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
        return "scala";
    }

    public String getLanguageVersion(){
        return "2.7.2";
    }

}


