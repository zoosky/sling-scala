package org.apache.sling.scripting.scala;

import javax.jcr.Property;
import javax.jcr.PropertyType;
import javax.jcr.RepositoryException;

public class ScalaProperty {
	
	private com.sun.xml.internal.bind.v2.runtime.property.Property property;
	
	public ScalaProperty(){
		
	}

	public void propertyConstructor(Object resource) {
		this.property = (Property) resource;
	}
	
	/** TO DO:
	 * 	public Object getNode()
	 * @return
	 */
	
	public Object getValue(){
		try {
			return property.getValue();
		}catch (RepositoryException re) {
			return null;
		}
	}

	public Object getValues(){
		try {
			return property.getValues();
		}catch (RepositoryException re) {
			return null;
		}
	}

	public Object getValues(){
		try {
			return property.getValues();
		}catch (RepositoryException re) {
			return null;
		}
	}
	
	public Object getString() {
		try{
			return property.getString();
		}catch(RepositoryException re){
			return null;
		}
	}
	
	public Object getStream() {
		try{
			return property.getStream();
		}catch(RepositoryException re){
			return null;
		}
	}	

	public Object getLong() {
		try{
			return property.getLong();
		}catch(RepositoryException re){
			return null;
		}
	}
	
	public Object getDouble() {
		try{
			return property.getDouble();
		}catch(RepositoryException re){
			return null;
		}
	}	
	
	public Object getDate() {
		try{
			return property.getDate();
		}catch(RepositoryException re){
			return null;
		}
	}	
	
	public Object getBoolean() {
		try{
			return property.getBoolean();
		}catch(RepositoryException re){
			return null;
		}
	}
	
	public Object getLenght() {
		try{
			return property.getLenght();
		}catch(RepositoryException re){
			return null;
		}
	}	
	
	public long[] getLengths() {
		try{
			return property.getLengths();
		}catch(RepositoryException re){
			return null;
		}
	}	
	
	public Object getDefinition() {
		try{
			return property.getDefinition();
		}catch(RepositoryException re){
			return null;
		}
	}
	
	public int getType() {
		try{
			return property.getType();
		}catch(RepositoryException re){
			return null;
		}
	}
	
	public Object getSession() {
		try{
			return property.getSession();
		}catch(RepositoryException re){
			return null;
		}
	}
	
	public String getPath() {
		try{
			return property.getPath();
		}catch(RepositoryException re){
			return null;
		}
	}
	
	public String getName() {
		try{
			return property.getName();
		}catch(RepositoryException re){
			return null;
		}
	}
	
	public int getDepth() {
		try{
			return property.getDepth();
		}catch(RepositoryException re){
			return null;
		}
	}
	
	public Object getIsNew() {
		return property.isNew();
	}
	
	public Object getModified() {
		return property.isModified();
	}
	
	@Override
	public String toString() {
		try {
			return property.getValue().getString();
		}catch (RepositoryException e){
			return property.toString();
		}
	}
}
