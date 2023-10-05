package com.greenfield.ui.cache;

import java.util.LinkedHashMap;
import java.util.Map;

public class ExMap extends LinkedHashMap {
	private int maxSize = 100;  // will be reset by constructor
	
	public ExMap(int initSize, 
			float loadFactor, 
			boolean accessOrder, 
			int maxSize) {
		super(initSize, loadFactor, accessOrder);
		this.maxSize = maxSize;
		
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.util.LinkedHashMap#removeEldestEntry(java.util.Map.Entry)
	 * Return true - if the map should remove the eldest entry;
	 * "eldest" is defined by the accessOrder.
	 * This will keep the Map in the right size.
	 */
	protected boolean removeEldestEntry(Map.Entry eldest) {
		return size() > maxSize;
	}
}
