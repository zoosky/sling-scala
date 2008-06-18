package org.apache.sling.scripting.scala;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.Property;
import javax.jcr.PropertyIterator;
import javax.jcr.PropertyType;
import javax.jcr.RepositoryException;
import javax.jcr.Value;
import javax.jcr.ValueFormatException;
import java.jcr.nodetype.NodeType;

import org.apache.sling.jcr.resource.JcrResourceUtil;

	private Node node;
	
	public class ScalaNode {
			
	}

	public void nodeConstructor(Object resource){
		this.node = (Node) resource;
	}
	
    public Object getChildren() {
        try {
            return toScriptableItemMap(node.getNodes());
        } catch (RepositoryException re) {
            return null;
        }
    }
 
    public Object getProperties() {
        try {
            return toScriptableItemMap(node.getProperties());
        } catch (RepositoryException re) {
            log.warn("Cannot get properties of " + jsFunction_getPath(), re);
            return null;
        }
    }
    
    public String getUUID() {
        try {
            return node.getUUID();
        } catch (RepositoryException e) {
            return "";
        }
    }
    
    public Iterator<?> getReferences() {
        try {
            return node.getReferences();
        } catch (RepositoryException e) {
            return Collections.EMPTY_LIST.iterator();
        }
    }
    
    public int getIndex() {
        try {
            return node.getIndex();
        } catch (RepositoryException e) {
            return 1;
        }
    }
    
    public Object getPrimaryNodeType() {
        try {
            return node.getPrimaryNodeType();
        } catch (RepositoryException e) {
            return null;
        }
    }
    
    public NodeType[] getMixinNodeType() {
        try {
            return node.getMixinNodeType();
        } catch (RepositoryException e) {
            return new NodeType[0];
        }
    }
    
    public Object getDefinition() {
        try {
            return node.getDefinition();
        } catch (RepositoryException e) {
            return null;
        }
    }
    
    public boolean getCheckedOut() {
        try {
            return node.isCheckedOut();
        } catch (RepositoryException e) {
            return false;
        }
    }
    
    public Object getLock() {
        try {
            return node.getLock();
        } catch (RepositoryException e) {
            return null;
        }
    }
    
    public boolean getLocked() {
        try {
            return node.isLocked();
        } catch (RepositoryException e) {
            return false;
        }
    }
    
    public Object getSession() {
        try {
            return node.getSession();
        } catch (RepositoryException e) {
            return null;
        }
    }
    
    public String getPath() {
        try {
            return node.getPath();
        } catch (RepositoryException e) {
            return node.toString();
        }
    }   
    
    public String getName() {
        try {
            return node.getName();
        } catch (RepositoryException e) {
            return node.toString();
        }
    }    

    public int getDepth() {
        try {
            return node.getDepth();
        } catch (RepositoryException e) {
            return -1;
        }
    }   
    

    public boolean getNew() {
        return node.isNew();
    }

    public boolean getModified() {
        return node.isModified();
    }
    
    public void remove() throws RepositoryException {
        node.remove();
    }

    public boolean jsFunction_hasNode(String path) throws RepositoryException {
        return node.hasNode(path);
    }    
    
    public Object getProperty(String name) throws RepositoryException {
        Object[] args = { node.getProperty(name) };
        return new ScalaProperty();
    }
    
    private Object toScriptableItemMap(Iterator<?> iter) {
        Object[] args = (iter != null) ? new Object[] { iter } : null;
        return new ScalaItemMap();
    }    