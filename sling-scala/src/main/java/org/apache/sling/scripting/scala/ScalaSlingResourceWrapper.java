package org.apache.sling.scripting.scala;

import org.apache.sling.api.scripting.SlingScriptHelper;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;

import java.util.Iterator;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.Value;
import javax.jcr.Property;
import javax.jcr.PropertyIterator;
import javax.jcr.nodetype.NodeType;
import javax.jcr.PropertyType;
import javax.jcr.Node;

public class ScalaSlingResourceWrapper {
	

	Iterator<Resource> childResources = null;
	Node currentNode = null;
	Property currentProperty = null;
	Resource currentResource = null;
	
	
	public ScalaSlingResourceWrapper(){
		
	}
	
//	public Iterator<Resource> getChildrenResourceList(Resource parent){
//		
//		childResources = ResourceResolver.listChildren(parent);
//		return childResources;
//		
//	}
	
	public Node getCurrentNode(SlingScriptHelper helper){
		
		currentResource = helper.getRequest().getResource();
		currentNode = currentResource.adaptTo(Node.class);
		
		return currentNode;
		
	}
	
	public Property getCurrentNodeProperty(String property){
		
		try{
		currentProperty = currentNode.getProperty(property);
		}catch(Exception pe){
			
		}
		
		return currentProperty;
		
	}
	
	public Property getNodeProperty(Node node , String property){
		
		Property nodeProperty = null;	
		try{
		nodeProperty = node.getProperty(property);
		}catch(Exception pe){
		
		}		
		return nodeProperty;
		
	}
	
	public Value getPropertyValue(Property property){
		
		Value valu = null;
		try{
		valu = property.getValue();
		
		}catch(Exception pe){
		
		}
		return valu;
		
	}
	

}
