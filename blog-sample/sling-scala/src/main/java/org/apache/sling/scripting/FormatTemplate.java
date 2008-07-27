package org.apache.sling.scripting.scala;


import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PushbackReader;
import java.io.Reader;
import java.util.Stack;



public class FormatTemplate{
	
	private FileInputStream fis = null;
	private BufferedInputStream bis = null;
	private DataInputStream dis = null;
	private File f = new File("test.scala");
	
	private String templateCode = "";
	
	public FormatTemplate(){
 
		try{
		fis = new FileInputStream(f);
		bis = new BufferedInputStream(fis);
		dis = new DataInputStream(bis);
		}catch(IOException e){
			e.printStackTrace();
		}
	}
		
	public void readFile(){
		try{
		while(dis.available() != 0){
			templateCode = templateCode + dis.readLine();
		}
		}catch(Exception e){
			e.printStackTrace();
		}

	}
	
	public String getNonFormattedCode(){
		return templateCode;
	}
	
	public void closeStreams(){
		try{
		fis.close();
		bis.close();
		dis.close();
		}catch(IOException e){
			e.printStackTrace();
		}
	}
		

}
