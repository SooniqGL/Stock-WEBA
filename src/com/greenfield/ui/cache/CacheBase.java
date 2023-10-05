package com.greenfield.ui.cache;


public abstract class CacheBase {
	public abstract Object getObject(String key) throws Exception;
	public abstract void loadObject(String key, Object obj) throws Exception;
        public abstract Object removeObject(String key) throws Exception;
	public abstract void snap();
	public abstract void reset();
	
	// A utility function used by children classes
	protected String nvl(String str) {
		if (str == null) {
			return "";
		} else {
			return str;
		}
	}

}
