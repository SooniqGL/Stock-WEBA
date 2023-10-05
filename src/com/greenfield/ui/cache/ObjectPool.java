/*
 * Created on Apr 11, 2010
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.greenfield.ui.cache;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

/**
 * @author qz69042
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ObjectPool {
        public static final String SET_OPERATION = "set";
        public static final String REMOVE_OPERATION = "remove";
        public static final String REMOVE_PREFIX_OPERATION = "removeprefix";

	private static ObjectPool theObject;
	
	private static HashMap objectHash = null;
	
	// defined refresh list to server the refersh purpose
	private static Vector refreshList = null;
	
	private ObjectPool() {};  // make the construct private
	
	/**
	 * synchronized - to guarantee the singlton instance for the object is used
	 * 
	 * @return
	 */
	public synchronized static ObjectPool getInstance() {
		if (theObject == null) {
			theObject = new ObjectPool();
			objectHash = new HashMap();
			refreshList = new Vector();
		}
		
		return theObject;
	}
	
	public Object getObject(String key) throws Exception {
		if (key == null || key.equals("")) {
			throw new Exception("ObjectPool: Key is null or empty in getObject().");
		}
		
		Object obj = null;
		if (objectHash.containsKey(key)) {
			// if debug, show it is from hash
			obj = objectHash.get(key);
		}
		
		return obj;
	}

        public synchronized void modifyHashObject(String mode, String key, Object obj) throws Exception {
		if (mode == null || mode.equals("")) {
			throw new Exception("ObjectPool: mode is null or empty in modifyHashObject().");
		}

                if (mode.equals(SET_OPERATION)) {
                    setObject(key, obj);
                } else if (mode.equals(REMOVE_OPERATION)) {
                    removeObject(key);
                } else if (mode.equals(REMOVE_PREFIX_OPERATION)) {
                    removeObjectsByPrefix(key);
                }

	}
	
	private void setObject(String key, Object obj) throws Exception {
		if (key == null || key.equals("")) {
			throw new Exception("ObjectPool: Key is null or empty in setObject().");
		}
		
		if (obj == null) {
			throw new Exception("ObjectPool: Object is null in setObject().");
		}
		
		// put
		objectHash.put(key, obj);
		
	}

        // called by one thread usually
        private void removeObject(String key) throws Exception {
		if (key == null || key.equals("")) {
			throw new Exception("ObjectPool: Key is null or empty in removeObject().");
		}

		// remove from hash
		objectHash.remove(key);
		
	}

        /**
         * Some prefix is used to a group of objects.
         * To remove the group, pass the prefix.
         * called by one thread usually - how to control at the time, it is not used??
         * @param keyPrefix
         * @throws Exception
         */
        private void removeObjectsByPrefix(String keyPrefix) throws Exception {
		if (keyPrefix == null || keyPrefix.equals("")) {
			throw new Exception("ObjectPool: keyPrefix is null or empty in removeObjectByPrefix().");
		}

		// remove from hash
                // you cannot iterator the keyset and
                // at the same time to remove the object
                // you will get ConcurrentModificationException!
                Iterator it = objectHash.keySet().iterator();
                Vector keyList = new Vector();
                while (it.hasNext()) {
                    String key = (String) it.next();
                    if (key.startsWith(keyPrefix)) {
                        keyList.add(key);
                    }
                }
                
                if (keyList.size() > 0) {
                    for (int i = 0; i < keyList.size(); i ++) {
                        String key = (String) keyList.get(i);
                        objectHash.remove(key);

                        // debug
                        System.out.println("Removing key: " + key);
                    }
                }

	}
	
	/**
	 * Use properties files to control the refresh matter:
	 * Key.class = the class
	 * Key.paras = parameters
	 * Recalculate the objects and set into the hashmap.
	 * For any key not defined in the properties, the content will be lost.
	 */ 
	public void refreshPool() {
		
	}

        /**
         * One thread - add, then iterator to remove -
         * It will get CurrentModificationException for HashMap/Hashtable.
         * One cannot iterator, at the same time remove!!
         * @param args
         */
	public static void main(String[] args) {
		HashMap h = new HashMap();

                h.put("k1", "123");
                h.put("k21", "123");
                h.put("k31", "123");
                h.put("k12", "123");
                h.put("k13", "123");


                Iterator it = h.keySet().iterator();
                while (it.hasNext()) {
                    System.out.println("Key: " + it.next());
                }

                Vector ls = new Vector();
                Iterator it2 = h.keySet().iterator();
                while (it2.hasNext()) {
                    String key = (String) it2.next();
                    if (key.startsWith("k1")) {
                        ls.add(key);
                        //h.remove(key);
                    }
                }

                for (int i = 0; i < ls.size(); i ++) {
                    h.remove((String) ls.get(i));
                }
                

                Iterator it3 = h.keySet().iterator();
                while (it3.hasNext()) {
                    System.out.println("Key 2:" + it3.next());
                }


	}
	
}
