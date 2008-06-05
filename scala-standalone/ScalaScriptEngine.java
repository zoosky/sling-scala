
import java.io.BufferedReader;
import java.io.File;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringWriter;

import javax.script.ScriptContext;
import javax.script.ScriptException;
import javax.script.ScriptEngineFactory;

import scala.tools.nsc.Interpreter;
import scala.tools.nsc.Settings;


public class ScalaScriptEngine extends AbstractSlingScriptEngine{
	
	Settings interpreterSettings = null;
	Interpreter interpreter = null;
	File engineLog = new File("index.html");
	PrintWriter logPrinter = null;
	StringWriter  interpreterOutput = null;
    
	public ScalaScriptEngine(ScriptEngineFactory factory) {
		

		super(factory);
		
		try{
			
	    interpreterOutput  = new StringWriter();
		logPrinter = new PrintWriter(interpreterOutput);		
		interpreterSettings = new Settings();
	

		
		interpreter = new Interpreter(interpreterSettings , logPrinter);
		
		}catch(Exception e){
			
			e.printStackTrace();
			
		}
		
		
	}
	
	public Object eval(Reader script , ScriptContext scriptContext) throws ScriptException
	{
		Object result =null;
		
        try {
            StringBuffer scriptString = new StringBuffer();
            BufferedReader bufferedScript = new BufferedReader(script);
            String nextLine = bufferedScript.readLine();
            while (nextLine != null) {
                scriptString.append(nextLine);
                scriptString.append("\n");
                nextLine = bufferedScript.readLine();
            }

		 result = interpreter.interpret(script.toString());
		
        }
        catch(Exception ex)
        {
        	ex.printStackTrace();
        }
		
		return result;
	}
	
	
	public Object eval(String script , ScriptContext scriptContext) throws ScriptException
	{
		Object result = interpreter.interpret(script);
		System.out.println(interpreterOutput.toString());
		return result;

	}
	
	

}
