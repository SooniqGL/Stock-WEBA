package com.greenfield.ui.handler.group;

import java.util.Map;
import java.util.Vector;

import org.apache.log4j.Logger;

import com.greenfield.common.nosql.base.CassandraDao;
import com.greenfield.common.object.ajax.MessageQueryResponse;
import com.greenfield.common.paging.IndexPair;
import com.greenfield.ui.handler.ajax.MessageConstants;

public class MessageAjaxHandler {
	private static final Logger LOGGER = Logger.getLogger(MessageAjaxHandler.class); 

	/**
	 * 
	 * @param cassDao
	 * @param groupId
	 * @param mode  		-- subject, time, replies
	 * @param type  		-- indexmsg, index, msg
	 * @param startIndex	-- msgId - null/empty if first
	 * @param order			-- P/N - previous set, or next set
	 * @return
	 */
	public static MessageQueryResponse getPageList(CassandraDao cassDao, 
			String groupId, String mode, String type, String startIndex, String order) {

		MessageQueryResponse ajaxResponse = new MessageQueryResponse();

		
		// debug
		//System.out.println("getPageList for group info: " + groupId);
				
		try {
			// get the group id 
			if (groupId == null || groupId.equals("")) {
				throw new Exception("MessageAjaxHandler - getPageList, groupId not defined.");
			}
						
			// for type, default to indexmsg
			if (type == null || type.equals("")) {
				type = MessageConstants.INDEXMSG;
			}
			
			// default - go next set of data
			if (order == null || order.equals("")) {
				order = MessageConstants.NEXT;
			}
			
			// default - current row  ??? every message has "row sequence, group"
			//if (rowId == null || rowId.equals("")) {
			//	rowId = current;
			//}
			
			// figure out rowId
			// big issue: if data is crossing rows, then what to do???
			
			String rowId = null; //MessageRowidMgr.getRowIdForGroup(cassDao, groupId);  //??? get current row, or get from request?
			
			// mode: subject, time, replies -> go to different tables for different modes
			Vector<IndexPair> indexList = null;
			if (mode.equals(MessageConstants.SUBJECT_LIST)) {
				indexList = null; //getIndexPairListFromSTable(cassDao, rowId, startIndex, order);
			}
			
			
		} catch (Exception e) {
			// warning
			LOGGER.warn("Error: getPageList for group: " + groupId + ", error: " + e.getMessage());
			ajaxResponse.setSuccess(false);
			ajaxResponse.setMessage("Error: there was general error in handle your request.  Please try later.");
		}
		
		return ajaxResponse;
	}
}
