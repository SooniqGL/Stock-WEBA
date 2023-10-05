/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.greenfield.ui.cache;

/**
 *
 * @author qz69042
 */
public class ObjectPoolKeys {
    /*******************************************
     * Keys for ScanCachePool
     * keys - need to be unique in pool level.
     *******************************************/
    // prefix for all latest scan date
    public static final String LATEST_SCAN_DATE_KEY         = "LSD-";
    
    // prefix key for all the scan date list
    public static final String SCAN_DATE_LIST_KEY           = "SDL-";

    // prefix key for all the scan REPORT date list
    public static final String SCAN_REPORTDATE_LIST_KEY     = "SRL-";

    // to hold the stock age summary list
    public static final String STOCKAGE_SUMMARY_LIST_KEY    = "SSL-";
    
 // to hold the stock age summary list
    public static final String STOCKAGE_BEST_LIST_KEY    	= "SBL-";

    
    /*******************************************
     * Keys for MarketCachePool
     * keys - need to be unique in pool level.
     *******************************************/
    // Market keys
    public static final String MARKET_INDICATORS_KEY        = "MI-";
    public static final String MARKET_PULSEDATE_LIST_KEY    = "MPL-";


     /*******************************************
     * Keys for DBCachePool
     * keys - need to be unique in pool level.
     *******************************************/
    public static final String DB_STOCK_KEY                 = "DS-";
    public static final String DB_STOCKLIST_KEY             = "DSL-";
    public static final String DB_STOCKDATE_RANGE_KEY       = "DSR-";
    public static final String DB_WATCH_STOCKIDLIST_KEY     = "DWS-";
    public static final String DB_SERVICE_CONTEXT_KEY       = "DSC-";
    public static final String DB_PORTFOLIO_LIST_KEY        = "DPL-";
    public static final String DB_EXAMSTOCKLIST_KEY         = "DEL-";
    
    public static final String DB_SERVICE_CONTEXT_KEY_X     = "DSCX-";
    
    /*******************************************
     * Keys for UserCachePool
     * keys - need to be unique in pool level.
     * Suffix may better perform the prefix if too many entries.
     *******************************************/
    public static final String USER_ID_ENCRYPT_KEY_SUFFIX   = "_ENCRYPT";


}
