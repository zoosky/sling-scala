
import javax.script.ScriptEngineFactory;
import javax.script.ScriptEngine;



public class Scala {
	
	public static void main(String[] args)
	{
		ScriptEngineFactory f = new ScalaScriptEngineFactory();
		System.out.println("created scala factory");
		
		ScriptEngine e = f.getScriptEngine();
		System.out.println("created scala engine");	
		
        Object sresult = null;
        try {
        	sresult = e.eval("4+2");
    		System.out.println("result eveluated");	          
        } catch (Exception ex) {
        	
            ex.printStackTrace();  
        }
        System.out.println(sresult.toString());		
        
		
	}

}
