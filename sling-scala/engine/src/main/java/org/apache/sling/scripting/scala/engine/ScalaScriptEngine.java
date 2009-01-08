package org.apache.sling.scripting.scala.engine;

import static org.apache.sling.scripting.scala.engine.ExceptionHelper.initCause;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

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
import org.apache.sling.scripting.scala.interpreter.InterpreterException;
import org.apache.sling.scripting.scala.interpreter.ScalaBindings;
import org.apache.sling.scripting.scala.interpreter.ScalaInterpreter;
import org.slf4j.Logger;

import scala.tools.nsc.reporters.Reporter;

// todo enhance: implement Compilable!?
public class ScalaScriptEngine extends AbstractSlingScriptEngine {
    public static final String NL = System.getProperty("line.separator");

    private final ScalaInterpreter interpreter;

    public ScalaScriptEngine(ScalaInterpreter interpreter, ScriptEngineFactory scriptEngineFactory) {
        super(scriptEngineFactory);
        this.interpreter = interpreter;
    }

    @Override
    public Object eval(String script, ScriptContext context) throws ScriptException {
        try {
            SlingBindings bindings = getBindings(context);
            TypeHints typeHints = new TypeHints(bindings);
            ScalaBindings scalaBindings = new ScalaBindings();
            for (Object name : bindings.keySet()) {
                scalaBindings.put((String) name, bindings.get(name), typeHints.get(name));
            }

            // todo implement: AbstractFile on top of JCR
            // todo implement: pass AbstractFile to compiler for output (compiler.genJVM.outputDir = ...)
            // todo implement: interpreter should check if script needs re-compilation
            // todo implement: use AbstractFileClassLoader for execution
            String scriptName = getScriptName(bindings);
            synchronized (interpreter) {
                check(interpreter.compile(scriptName, script, scalaBindings));
            }
            check(interpreter.execute(scriptName, scalaBindings, getInputStream(context), getOutputStream(context)));
        }
        catch (InterpreterException e) {
            throw initCause(new ScriptException("Error executing script"), e);
        }
        return null;
    }

    public Object eval(Reader scriptReader, ScriptContext context) throws ScriptException {
        BufferedReader br = new BufferedReader(scriptReader);
        StringBuffer script = new StringBuffer();
        String line;
        try {
            while ((line = br.readLine()) != null) {
                script.append(line).append(NL);
            }
        }
        catch (IOException e) {
            throw initCause(new ScriptException("Error executing script"), e);
        }
        return eval(script.toString(), context);
    }

    // -----------------------------------------------------< private >---

    private static SlingBindings getBindings(ScriptContext context) {
        Bindings bindings = context.getBindings(ScriptContext.ENGINE_SCOPE);
        if (!(bindings instanceof SlingBindings)) {
            throw new IllegalArgumentException("Bindings is not an instance of sling bindings");
        }
        return (SlingBindings) bindings;
    }

    private static String getScriptName(SlingBindings bindings) {
        SlingScriptHelper helper = bindings.getSling();
        if (helper == null) {
            throw new IllegalArgumentException("Bindings does not contain script helper object");
        }
        else {
            String path = helper.getScript().getScriptResource().getPath();
            if ( path.endsWith(".java") ) {
                path = path.substring(0, path.length() - 5);
            }

            int pos = path.lastIndexOf("/");
            return pos == -1
                ? makeJavaIdentifier(path)
                : path.substring(0, pos + 1) + makeJavaIdentifier(path.substring(pos + 1));
        }
    }

    /**
     * Converts the given identifier to a legal Java identifier
     * @param identifier Identifier to convert
     * @return Legal Java identifier corresponding to the given identifier
     */
    private static final String makeJavaIdentifier(String identifier) {
        StringBuffer id = new StringBuffer(identifier.length());
        if (!Character.isJavaIdentifierStart(identifier.charAt(0))) {
            id.append('_');
        }
        for (int i = 0; i < identifier.length(); i++) {
            char ch = identifier.charAt(i);
            if (Character.isJavaIdentifierPart(ch) && ch != '_') {
                id.append(ch);
            }
            else if (ch == '.') {
                id.append('_');
            }
            else {
                id.append(mangleChar(ch));
            }
        }
        if (isJavaKeyword(id.toString())) {
            id.append('_');
        }
        return id.toString();
    }

    /**
     * Mangle the specified character to create a legal Java class name.
     */
    private static final String mangleChar(char ch) {
        char[] result = new char[5];
        result[0] = '_';
        result[1] = Character.forDigit((ch >> 12) & 0xf, 16);
        result[2] = Character.forDigit((ch >> 8) & 0xf, 16);
        result[3] = Character.forDigit((ch >> 4) & 0xf, 16);
        result[4] = Character.forDigit(ch & 0xf, 16);
        return new String(result);
    }

    private static final Set<String> KEYWORDS = new HashSet<String>() {{
        add("abstract"); add("assert"); add("boolean"); add("break"); add("byte"); add("case"); add("catch");
        add("char"); add("class"); add("const"); add("continue"); add("default"); add("do"); add("double");
        add("else"); add("enum"); add("extends"); add("final"); add("finally"); add("float"); add("for");
        add("goto"); add("if"); add("implements"); add("import"); add("instanceof"); add("int");
        add("interface"); add("long"); add("native"); add("new"); add("package"); add("private");
        add("protected"); add("public"); add("return"); add("short"); add("static"); add("strictfp");
        add("super"); add("switch"); add("synchronized"); add("this"); add("throws"); add("transient");
        add("try"); add("void"); add("volatile"); add("while");
    }};


    /**
     * Test whether the argument is a Java keyword
     */
    private static boolean isJavaKeyword(String key) {
        return KEYWORDS.contains(key);
    }

    private static void check(Reporter result) throws ScriptException {
        if (result.hasErrors()) {
            throw new ScriptException(result.toString());
        }
    }

    private static InputStream getInputStream(final ScriptContext context) {
        return new InputStream() {
            @Override
            public int read() throws IOException {
                return new BufferedReader(context.getReader()).read();
            }
        };
    }

    private static OutputStream getOutputStream(final ScriptContext context) {
        return new OutputStream() {
            @Override
            public void write(int b) throws IOException {
                new BufferedWriter(context.getWriter()).write(b);
            }
        };
    }

    // todo fix: use when ScalaInterpreter supports it
    private static OutputStream getErrorStream(final ScriptContext context) {
        return new OutputStream() {
            @Override
            public void write(int b) throws IOException {
                new BufferedWriter(context.getErrorWriter()).write(b);
            }
        };
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

        public TypeHints(SlingBindings bindings) {
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


