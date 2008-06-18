package org.apache.sling.scripting.scala;

import java.util.Iterator;
import java.util.Map;
import java.util.LinkedHashMap;

import javax.jcr.Item;
import javax.jcr.RepositoryException;

/** Data Sructure to hold items to access */

public class ScalaItemMap {
	
	private Map<String,Item> items = new LinkedHashMAp<String , Item>();
	
	public ScalaItemMap() {
		
	}
	
	public void insertResource(Object resource) {		
		if (resource instanceof Iterator) {
			Iterator<?> itemIterator = (Iterator<?>) resource;
			while(itemIterator.hasNext()){
				Item item = (Item) itemIterator.next();
				try{
						items.put(item.getName() , item);
				}catch(RepositoryException re){
					System.out.println("Cannot get name of item" + item , re);
				}
			}
		}
	}
	
	public Item getItem(int index) {
		if(index<0 || index >= items.size()) {
			return null;
		}
		
		Iterator<Item> itemsIter = items.values().iterator();
		while(itemsIter.hasNext() && index > 0) {
			itemsIter.next();
			index--;
		}
	}
	

}
