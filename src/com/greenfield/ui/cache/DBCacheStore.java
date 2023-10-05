/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.greenfield.ui.cache;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * @author Qin
 */
public class DBCacheStore extends CacheBase {
    private Map cache = null;
    private static DBCacheStore theObj = null;

    private static final int INIT_SIZE = 1000;
    private static final float LOAD_FACTOR = 0.75f;
    private static final boolean ACCESS_ORDER = true;  // true for access-order, false for insertion-order
    private static final int MAX_SIZE = 2000;

    private AtomicInteger fromCacheCounter;
    private AtomicInteger putCacheCounter;

    // make this construct private
    private DBCacheStore() {
        cache = Collections.synchronizedMap(
                new ExMap(INIT_SIZE, LOAD_FACTOR, ACCESS_ORDER, MAX_SIZE));
        fromCacheCounter = new AtomicInteger(0);
        putCacheCounter = new AtomicInteger(0);
    }
	
    public static synchronized DBCacheStore getInstance() {
        if (theObj == null) {
            theObj = new DBCacheStore();
        }

        return theObj;
    }
	
    public Object getObject(String key) throws Exception {
        if (key == null || key.equals("")) {
            return null;
        }

        Object outputObj = cache.get(key);
        if (outputObj != null) {
            // debug
            System.out.println("Get object from cache: " + key);
            fromCacheCounter.incrementAndGet();
        } else {
            // not found
        }

        return outputObj;
    }
    
    public Object removeObject(String key) throws Exception {
        if (key == null || key.equals("")) {
            return null;
        }

        Object outputObj = cache.get(key);
        if (outputObj != null) {
            // debug
            System.out.println("Going to remove the object from cache: " + key);
            synchronized(cache) {
                // remove the object from cache if exists
                cache.remove(key);
            }           
        } else {
            // not found
        }

        return outputObj;
    }
	
    /**
     * Load object from DB to the Cache.
     */
    public void loadObject(String key, Object inputObj) throws Exception {
        if (key == null || key.equals("")) {
            throw new Exception("DBCacheStore:loadObject: key is null.");
        }

        if (inputObj != null) {
            synchronized(cache) {
                // put to the cache, overwrite if it exists already
                cache.put(key, inputObj);
                putCacheCounter.incrementAndGet();
            }
        } else {
            // nothing is found in DB, surprised!
            System.out.println("Warning: DBCacheStore-loadObject() nothing for id: " + key);
        }
    }

    public void reset() {
        synchronized (cache) {
            cache.clear();
            fromCacheCounter = new AtomicInteger(0);
            putCacheCounter = new AtomicInteger(0);
        }
    }

    public void snap() {
            System.out.println("DBCacheStore: " + cache.size() 
                    + ", put: " + putCacheCounter 
                    + ", get: " + fromCacheCounter);
    }
}

/* end */
