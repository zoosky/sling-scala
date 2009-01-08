package org.apache.sling.scripting.scala.engine;

import static org.apache.sling.scripting.scala.engine.ExceptionHelper.initCause;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.HashMap;

import javax.jcr.Node;
import javax.script.Bindings;
import javax.script.ScriptContext;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptException;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.scripting.SlingBindings;
import org.apache.sling.api.scripting.SlingScriptHelper;
import org.apache.sling.scripting.api.AbstractSlingScriptEngine;
import org.apache.sling.scripting.scala.interpreter.ScalaInterpreter;
import org.slf4j.Logger;

import scala.Tuple2;
import scala.collection.immutable.EmptyMap;
import scala.collection.immutable.Map;


public class ScalaScriptEngine extends AbstractSlingScriptEngine {
    private final ScalaInterpreter interpreter;

    public ScalaScriptEngine(ScalaInterpreter interpreter, ScriptEngineFactory scriptEngineFactory) {
        super(scriptEngineFactory);
        this.interpreter = interpreter;
    }

    @Override
    public Object eval(String script, ScriptContext context) throws ScriptException {
        try {
            Bindings bindings = context.getBindings(ScriptContext.ENGINE_SCOPE);
            TypeHints typeHints = new TypeHints(bindings);
            Map<String, Tuple2<Object, Class<?>>> scalaBindings = new EmptyMap<String, Tuple2<Object, Class<?>>>();
            for (Object name : bindings.keySet()) {
                scalaBindings = scalaBindings.update((String) name,
                        new Tuple2<Object, Class<?>>(bindings.get(name), typeHints.get(name)));
            }

            final Writer stdOutWriter = new BufferedWriter(context.getWriter());
            interpreter.stdOut_$eq(new OutputStream() {
                @Override
                public void write(int b) throws IOException {
                    stdOutWriter.write(b);
                }
            });

            // todo fix: set stdErr when ScalaInterpreter supports it
            // final Writer errOutWriter = new BufferedWriter(context.getErrorWriter());
            // interpreter.stdErr_$eq(new OutputStream() {
            //     @Override
            //     public void write(int b) throws IOException {
            //         errOutWriter.write(b);
            //     }
            // });

            final Reader stdInReader = new BufferedReader(context.getReader());
            interpreter.stdIn_$eq(new InputStream() {
                @Override
                public int read() throws IOException {
                    return stdInReader.read();
                }
            });

            interpreter.interprete(getScriptName(bindings), script, scalaBindings);
            stdOutWriter.flush();
        }
        catch (IOException e) {
            throw initCause(new ScriptException("Error: Cannot execute script"), e);
        }
        return null;
    }

    public Object eval(Reader scriptReader, ScriptContext context) throws ScriptException {
        BufferedReader br = new BufferedReader(scriptReader);
        StringBuffer script = new StringBuffer();
        String line;
        try {
            while ((line = br.readLine()) != null) {
                script.append(line).append("\n");
            }
        }
        catch (IOException e) {
            throw initCause(new ScriptException("Error: Cannot execute script"), e);
        }
        return eval(script.toString(), context);
    }

    // -----------------------------------------------------< private >---

    private String getScriptName(Bindings bindings) {
        SlingScriptHelper helper = (SlingScriptHelper) bindings.get(SlingBindings.SLING);
        if (helper == null) {
            throw new IllegalArgumentException("Bindings does not contain script helper object");
        }
        else {
            return helper.getScript().getScriptResource().getPath();
        }
    }

    @SuppressWarnings("serial")
    private static class TypeHints extends HashMap<String, Class<?>> {
        public static final Class<SlingHttpServletRequest> REQUEST_TYPE = SlingHttpServletRequest.class;
        public static final Class<SlingHttpServletResponse> RESPONSE_TYPE = SlingHttpServletResponse.class;
        public static final Class<Reader> READER_TYPE = Reader.class;
        public static final Class<SlingScriptHelper> SLING_TYPE = SlingScriptHelper.class;
        public static final Class<Resource> RESOURCE_TYPE = Resource.class;
        public static final Class<PrintWriter> OUT_TYPE = PrintWriter.class;
        public static final Class<Boolean> FLUSH_TYPE = Boolean.class;
        public static final Class<Logger> LOG_TYPE = Logger.class;
        public static final Class<Node> NODE_TYPE = Node.class;

        private static final java.util.Map<String, Class<?>> TYPES = new HashMap<String, Class<?>>() {{
            put(SlingBindings.REQUEST, REQUEST_TYPE);
            put(SlingBindings.RESPONSE, RESPONSE_TYPE);
            put(SlingBindings.READER, READER_TYPE);
            put(SlingBindings.SLING, SLING_TYPE);
            put(SlingBindings.RESOURCE, RESOURCE_TYPE);
            put(SlingBindings.OUT, OUT_TYPE);
            put(SlingBindings.FLUSH, FLUSH_TYPE);
            put(SlingBindings.LOG, LOG_TYPE);
            put("currentNode", NODE_TYPE);
        }};

        public TypeHints(Bindings bindings) {
            super();
            for (Object name : bindings.keySet()) {
                setType((String) name, TYPES.get(name));
            }
        }

        public void setType(String name, Class<?> type) {
            if (type != null) {
                put(name, type);
            }
        }

        public Class<?> getType(String name) {
            Class<?> c = get(name);
            return c == null ? Object.class : c;
        }

        /**
         * Compile time assertion enforcing type safety
         */
        private static class CompileTimeAssertion {
            static {
                SlingBindings b = new SlingBindings();
                b.setRequest(REQUEST_TYPE.cast(null));
                b.setResponse(RESPONSE_TYPE.cast(null));
                b.setReader(READER_TYPE.cast(null));
                b.setSling(SLING_TYPE.cast(null));
                b.setResource(RESOURCE_TYPE.cast(null));
                b.setOut(OUT_TYPE.cast(null));
                b.setFlush(FLUSH_TYPE.cast(null));
                b.setLog(LOG_TYPE.cast(null));
            }
        }

    }

}


