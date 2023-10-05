package com.greenfield.ui.util.test;

import java.awt.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import com.greenfield.common.base.AppContext;
import com.greenfield.ui.handler.group.ElasticSearchMgr;
import com.greenfield.ui.handler.group.MessageObject;

public class ElasticTest2 {

	public static void main(String[] args) {
		try {
			AppContext.init();
			//ElasticTest2.insertMsg();
			ElasticTest2.testSearch();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void insertMsg() throws Exception {
		ElasticSearchMgr elasticMgr = new ElasticSearchMgr();
		
		try {
			ArrayList<String> groupList = 
	    			new ArrayList<String>(
	    			Arrays.asList(
	    					"P", "45221", "G36"  
	    					));
	    	
	        MessageObject msg = new MessageObject("abcd-1333", "Subject 1: do u message jumping rabbits  aeae see this - " + System.currentTimeMillis(), "Content message see # 333 and good",
        		new Date(), "user one", "U20", groupList);
        
        	elasticMgr.indexMessageAsync(msg);
		} finally {
			elasticMgr.closeClient();
		}
	}
	
	public static void testSearch() throws Exception {
		ElasticSearchMgr elasticMgr = new ElasticSearchMgr();
		
		try {
			ArrayList<String> groupList = new ArrayList<String>();
			groupList.add("45220");
			groupList.add("PP");
			
			elasticMgr.searchMessageWithGroupFacets(groupList, "see jump message");
		} finally {
			elasticMgr.closeClient();
		}
	}
}
