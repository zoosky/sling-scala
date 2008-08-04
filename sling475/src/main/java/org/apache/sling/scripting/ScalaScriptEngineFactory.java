/*
 * ScalaScriptEngineFactory.java
 *
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.apache.sling.scripting.scala;

import javax.script.ScriptEngine;


import org.apache.sling.scripting.api.AbstractScriptEngineFactory;

/**
 *
 * @author Janandith
 */


public class ScalaScriptEngineFactory extends AbstractScriptEngineFactory {
    
    public final static String SCALA_SCRIPT_EXTENSION = "scala";

    public final static String SCALA_MIME_TYPE = "text/scala";

    public final static String SHORT_NAME = "scala";
    
    /** Creates a new instance of ScalaScriptEngineFactory */
    public ScalaScriptEngineFactory() {      
        setExtensions(SCALA_SCRIPT_EXTENSION);
        setMimeTypes(SCALA_MIME_TYPE);
        setNames(SHORT_NAME);       
    }
       
    public ScriptEngine getScriptEngine(){    
        return new ScalaScriptEngine(this);
    }
    
    public String getLanguageName(){   
        return "scala";
    }
    
    public String getLanguageVersion(){     
        return "2.7.1";
    }

}


