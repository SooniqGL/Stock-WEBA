package com.greenfield.ui.handler.group;

import java.util.HashMap;

import org.apache.log4j.Logger;

import com.greenfield.common.nosql.base.CassandraDao;
import com.greenfield.common.nosql.object.message.GroupMCount;
import com.greenfield.common.util.DateUtil;

/**
 *  1) at start, update counts for all the groups for the latest group;
 *  2) call the update every a few minutes when system is running;
 *  3) keep the counters static, and available to all callers - to show the messages;
 *  4) Return the current row id for a given group;  If there is no row id registered, then create one, and return it;
 *  5) At the start up, if one message counter is > MAX_LIMIT, then recreate the row_id, and save to DB;
 *  
 *  Work table: group_m_count; get message count from group_l_store table for a given group.
 *
 */
public class MessageCountMgr {
	private static final Logger LOGGER = Logger.getLogger(MessageCountMgr.class);
	
	private static HashMap<String, String> rowIdHash = new HashMap<String, String>();		// group id -> current row id
	private static HashMap<String, Long> messageCountHash = new HashMap<String, Long>();   // group id -> total cnt
	
	
	public static String getRowIdForGroup(String groupId) {
		if (rowIdHash.containsKey(groupId)) {
			return rowIdHash.get(groupId);
		} else {
			String rowId = groupId + ".1";
			initCassandraMessageCount(groupId, 1, 0l);
			rowIdHash.put(groupId, rowId);
			messageCountHash.put(groupId, new Long(0l));
			
			return rowId;
		}
	}

	private static void initCassandraMessageCount(String groupId, int rowSeq, long cnt) {
		CassandraDao cassDao = new CassandraDao();
		try {
			GroupMCount m = new GroupMCount();
			m.setGroupId(groupId);
			m.setMsgCnt(0l);
			cassDao.insert(m);
		} catch (Exception ex) {
			LOGGER.warn("initCassandraMessageCount for groupId: " + groupId + ", rowId: " + rowSeq + ", error: " + ex.getMessage());
		} finally {
			cassDao.shutdown();
		}
	}
	
}
