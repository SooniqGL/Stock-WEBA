package com.greenfield.ui.handler.group;

import java.util.HashMap;
import java.util.Vector;

import org.apache.log4j.Logger;

import com.greenfield.common.dao.group.GroupDAO;
import com.greenfield.common.nosql.base.CassandraDao;
import com.greenfield.common.nosql.object.message.GroupRowid;

// 1) if group id is given, find the current row id - if not found, create one
// 2) if group id is given, find all the row id's, do some counting ????
// 3) Go through all rows, and see if any row too fat.
public class MessageRowidMgr {
	private static final Logger LOGGER = Logger.getLogger(MessageRowidMgr.class);
	
	private static HashMap<String, String> rowIdHash = new HashMap<String, String>();		// group id -> current row id
	
	
	public static String getCurrentRowIdForGroup(CassandraDao cassDao, String groupId) {
		if (rowIdHash.containsKey(groupId)) {
			return rowIdHash.get(groupId);
		} else {
			String rowId = getCassandraGroupRowid(cassDao, groupId, 1);
			rowIdHash.put(groupId, rowId);
			
			return rowId;
		}
	}

	/**
	 * need to update the group_rowid table with group id and rowseq
	 * create table group_rowid (
	     group_id     ascii,
	     row_seq      int,
	     msg_cnt      counter,
	     primary key (group_id, row_seq)
	) with clustering order by (row_seq DESC);
	// update group_rowid set msg_cnt = msg_cnt + 1 where group_id = 'G12' and row_seq = '1';
	 * @param groupId
	 * @param rowSeq
	 */
	

	private static String getCassandraGroupRowid(CassandraDao cassDao, String groupId, int defaultRowSeq) {
		String rowId = "";
		
		try {
			int rowSeq = getCurrentRowSeq(cassDao, groupId);
			if (rowSeq == -1) {
				// not found in DB
				// do not want to change the counter - set to 0
				String cql = "update group_rowid set msg_cnt = msg_cnt + 0 where group_id = '" + groupId + "' and row_seq = " + defaultRowSeq;
				cassDao.executeSql(cql);
				rowId = groupId + "." + defaultRowSeq;
			} else {
				// found
				rowId = groupId + "." + rowSeq;
			}
		} catch (Exception ex) {
			LOGGER.warn("getCassandraGroupRowid for groupId: " + groupId + ", error: " + ex.getMessage());
		} 
		
		return rowId;
	}
	
	// if the return is -1, then record not found
	public static int getCurrentRowSeq(CassandraDao cassDao, String groupId) throws Exception {
		int rowSeq = -1;
		String select_cql = "select row_seq from group_rowid where group_id = '" + groupId + "' limit 1";
		Vector list = cassDao.select(select_cql, GroupRowid.class.getName());
		if (list != null && list.size() > 0) {
			rowSeq = ((GroupRowid) list.get(0)).getRowSeq();
		} else {
			// debug
			LOGGER.warn("getCurrentRowSeq, not found for groupId: " + groupId);
		}
		
		return rowSeq;
	}
	
	// rowId format: {group id}.{seq}
	public static String nextRowId(String rowId) {
		return null;
	}
	
	
	/**
	 * go through all the row id's, if found one row larger than the limit, then
	 * update with new row_id; can be run periodically!!!, but not so often necessarily
	 * @param groupDao
	 * @throws Exception
	 */ 
	public static void checkAllGroupRowsIfToobig(GroupDAO groupDao) throws Exception {
		// do this one later, it has to be done if rows grow more than 500K entries for a give row id.
		Vector<String> groupIdList = groupDao.getGroupIdListFromDB(null,  null, null);
	}
}
