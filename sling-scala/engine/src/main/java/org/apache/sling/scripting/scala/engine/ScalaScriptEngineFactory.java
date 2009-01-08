package org.apache.sling.scripting.scala.engine;

import static org.apache.sling.scripting.scala.engine.ExceptionHelper.initCause;

import java.io.File;
import java.io.Reader;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptException;

import org.apache.sling.jcr.api.SlingRepository;
import org.apache.sling.scripting.api.AbstractScriptEngineFactory;
import org.apache.sling.scripting.api.AbstractSlingScriptEngine;
import org.apache.sling.scripting.scala.interpreter.BundleFS;
import org.apache.sling.scripting.scala.interpreter.JcrFS;
import org.apache.sling.scripting.scala.interpreter.ScalaInterpreter;
import org.osgi.framework.Bundle;
import org.osgi.service.component.ComponentContext;
import org.slf4j.LoggerFactory;

import scala.tools.nsc.Settings;
import scala.tools.nsc.io.AbstractFile;
import scala.tools.nsc.io.PlainFile;

/**
 * @scr.component
 * @scr.service
 */
public class ScalaScriptEngineFactory extends AbstractScriptEngineFactory {
    private static final String PATH_SEPARATOR = System.getProperty("path.separator");

    public final static String[] SCALA_SCRIPT_EXTENSIONS = {"scala", "scs"};
    public final static String SCALA_MIME_TYPE = "application/x-scala";
    public final static String SHORT_NAME = "scala";
    public final static String VERSION = "2.7.2";

    /** @scr.reference */
    private SlingRepository repository;

    private ComponentContext context;
    private ScalaScriptEngine scriptEngine;

    public ScalaScriptEngineFactory() {
        super();
        setExtensions(SCALA_SCRIPT_EXTENSIONS);
        setMimeTypes(SCALA_MIME_TYPE);
        setNames(SHORT_NAME);
    }

    protected void activate(ComponentContext context) {
        this.context = context;
    }

    protected void deactivate(ComponentContext context) {
        scriptEngine = null;
        this.context = null;
    }

    public ScriptEngine getScriptEngine(){
        if (context == null) {
            throw new IllegalStateException("Bundle not activated");
        }

        if (scriptEngine == null) {
            try {
                Bundle[] bundles = context.getBundleContext().getBundles();
                Settings settings = createSettings(bundles);
                scriptEngine = new ScalaScriptEngine(
                        new ScalaInterpreter(
                            settings,
                            new LogReporter(
                                    LoggerFactory.getLogger(ScalaInterpreter.class),
                                    settings),
                            createClassPath(bundles),
                            getOutDir()),
                        this);
            }
            catch (final RepositoryException e) {
                return new AbstractSlingScriptEngine(this) {
                    public Object eval(Reader reader, ScriptContext context) throws ScriptException {
                        throw initCause(new ScriptException("Cannot access output directory: " + getOutputPath()), e);
                    }
                };
            }
        }
        return scriptEngine;
    }

    public String getLanguageName(){
        return SHORT_NAME;
    }

    public String getLanguageVersion(){
        return VERSION;
    }

    public String getOutputPath() {
        // todo fix: make configurable
        return "/var/classes";
    }

    // -----------------------------------------------------< private >---

    private static Settings createSettings(Bundle[] bundles) {
        Settings settings = new Settings();
        URL[] bootUrls = getUrlClassLoader(bundles[0]).getURLs();
        StringBuilder bootPath = new StringBuilder(settings.classpath().v());
        for (int k = 0; k < bootUrls.length; k++) {
            bootPath.append(PATH_SEPARATOR).append(bootUrls[k].getPath());
        }

        settings.classpath().v_$eq(bootPath.toString());
        return settings;
    }

    private static AbstractFile[] createClassPath(Bundle[] bundles) {
        AbstractFile[] bundleFs = new AbstractFile[bundles.length];
        for (int k = 1; k < bundles.length; k++) { // system bundle is special, skip it
            bundleFs[k] = BundleFS.create(bundles[k]);
        }

        try { // handle system bundle
            URL url = bundles[0].getResource("/");
            bundleFs[0] = new PlainFile(new File(url.toURI()));
        }
        catch (URISyntaxException e) {
            throw initCause(new IllegalArgumentException("Can't determine url of system bundle"), e);
        }
        return bundleFs;
    }

    private AbstractFile getOutDir() throws RepositoryException {
        Session session = repository.loginAdministrative(null);
        Node node = (Node) session.getItem(getOutputPath());
        return JcrFS.create(node);
    }

    private static URLClassLoader getUrlClassLoader(Bundle bundle) {
        ClassLoader classLoader = bundle.getClass().getClassLoader().getParent();
        if (!(classLoader instanceof URLClassLoader)) {
            throw new IllegalArgumentException("Classloader of bundle is not a URLClassLoader");
        }
        return (URLClassLoader) classLoader;
    }

}


