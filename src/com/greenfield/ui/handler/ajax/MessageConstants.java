package com.greenfield.ui.handler.ajax;

public class MessageConstants {
	// page/message type list
	public final static String SUBJECT_LIST 	= "subject";
	public final static String TIME_LIST 		= "time";
	public final static String REPLIES_LIST  	= "replies";
	
	// index list or page list or both
	public final static String INDEXMSG 		= "indexmsg";
	public final static String INDEXONLY 		= "index";
	public final static String MSGONLY  		= "msg";
	
	// previous set or next set
	public final static String PREV 			= "P";
	public final static String NEXT  			= "N";
	
	// for each display, 10 pairs of indexes; each page, 15 messages
	public final static int PAGE_SIZE 			= 15;    // 15 messages per query
	public final static int NUM_PAGES			= 10;	 // 10 sets of page indexes will be created
}
