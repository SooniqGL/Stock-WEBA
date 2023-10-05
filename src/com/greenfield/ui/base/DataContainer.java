package com.greenfield.ui.base;

import java.util.HashMap;

public class DataContainer {
	private HashMap<String, Object> data = new HashMap<String, Object>();
	
	public String getString(String key) {
		if (key != null && data.containsKey( key )) {
			return (String) data.get(key);
		} else {
			return null;
		}
	}
	
	public Object getObject(String key) {
		if (key != null && data.containsKey( key )) {
			return data.get(key);
		} else {
			return null;
		}
	}
	
	public void setObject(String key, Object obj) {
		if (key != null && !key.equals("") && obj != null) {
			data.put(key, obj);
		}
	}
	
	public void setString(String key, String obj) {
		if (key != null && !key.equals("") && obj != null) {
			data.put(key, obj);
		}
	}

	public HashMap<String, Object> getData() {
		return data;
	}

	public void setData(HashMap<String, Object> data) {
		this.data = data;
	}

	
}
