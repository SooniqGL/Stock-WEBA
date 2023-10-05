/*
 * Created on Dec 30, 2006
 */
package com.greenfield.ui.handler.social;

import java.sql.ResultSet;
import java.util.Vector;

import com.greenfield.common.handler.BaseDAO;
import com.greenfield.common.object.SearchResult;
import com.greenfield.common.util.OracleUtil;
import com.greenfield.common.object.forum.ForumMessage;

/**
 * @author qin
 */
public class ForumMessageDAO extends BaseDAO {
	/**
	 * Insert a new record to DB
	 * message_id		number primary key,
	 * parent_id		number,
	 * has_children	char(1),
	 * m_date		date,
	 * user_id		varchar2(16),
	 * subject		varchar2(100),
	 * content		varchar2(2000)  

	 * @param info
	 * @throws Exception
	 */
	public void insertForumMessageToDB(ForumMessage message) throws Exception {
		// message.getMDate() is in the to_date() form
		String insert_sql = "insert into forum_message (message_id, parent_id, subject_id, has_children, m_date, "
			+ "user_id, subject, content) values ('" 
			+ Math.round(message.getMessageId()) + "', '" + Math.round(message.getParentId()) + "', '"
			+ Math.round(message.getSubjectId()) + "', '" + message.getHasChildren() + "', " 
			+ message.getMDate() + ", '" + message.getUserId() + "', '" 
			+ OracleUtil.getOracleInputString(message.getSubject()) 
			+ "', '" + OracleUtil.getOracleInputString(message.getContent()) + "')";
	
		// debug
		//System.out.println("sql: " + insert_sql);
		database.runUpdate(insert_sql);
		
	}

	/**
	 * Insert forum_subject
	 * @param forumMessage
	 * @throws Exception
	 */
	public void insertForumSubjectToDB(ForumMessage message) throws Exception {
		String insert_sql = "insert into forum_subject (subject_id, message_id) values ('"
			+ Math.round(message.getSubjectId()) + "', '" + Math.round(message.getMessageId()) + "')";

		// debug
		//System.out.println("sql: " + insert_sql);
		database.runUpdate(insert_sql);
		
	}
	
	public void updateForumMessageToDB(double messageId) throws Exception {
		String update_sql = "update forum_message set has_children = 'Y' where message_id = '"
			+ Math.round(messageId) + "'";

		// debug
		//System.out.println("sql: " + update_sql);
		database.runUpdate(update_sql);
	
	}

	/**
	 * Get Message list
	 * @param marketType
	 * @param startDate
	 * @return
	 * @throws Exception
         * In DB, need to use timestamp, instead of date.
	 */
	public Vector getForumMessageListFromDB(double messageId, double parentId) throws Exception {
            // in Postgres - only mm/dd/yyyy is saved
            // 'mm/dd/yyyy is used instead of 'mm/dd/yyyy HH24:MI:SS'
            
            String select_sql = "select a.message_id, a.parent_id, a.subject_id, a.has_children, "
			+ "to_char(a.m_date, 'mm/dd/yyyy HH24:MI:SS') m_date, a.user_id, a.subject, "
			+ "a.content, b.login_id from forum_message a, web_user b where a.user_id = b.user_id ";
			
		if (messageId != 0) {
			select_sql += "and a.message_id = '" + Math.round(messageId) + "' ";
		}
	
		if (parentId != 0) {
			select_sql += "and a.message_id != a.parent_id and a.parent_id = '" + Math.round(parentId) + "' ";
		}
		
		select_sql += "order by message_id";
	
		//debug
		//System.out.println("sql: " + select_sql);
		
		Vector retList = null;
		SearchResult res = database.runQuery(select_sql, "com.greenfield.common.object.forum.ForumMessage");
		if (res != null && res.getResult() != null && res.getResult().size() > 0) {
			retList = res.getResult();
		}


		return retList;
	}
	
	// get range of subjects
	public Vector getForumSubjectListFromDB(double startId, double endId) throws Exception {
		String select_sql = "select a.subject_id, b.message_id, b.parent_id, b.has_children, "
			+ "to_char(b.m_date, 'mm/dd/yyyy HH24:MI:SS') m_date, b.user_id, b.subject, "
			+ "b.content, c.login_id from forum_subject a, forum_message b, web_user c " 
			+ "where a.message_id = b.message_id and b.user_id = c.user_id ";
		
		select_sql += "and a.subject_id >= " + Math.round(startId);
		select_sql += " and a.subject_id <= " + Math.round(endId);
	
		select_sql += " order by subject_id";

		// debug
		//System.out.println("sql: " + select_sql);
		
		Vector retList = null;
		SearchResult res = database.runQuery(select_sql, "com.greenfield.common.object.forum.ForumMessage");
		if (res != null && res.getResult() != null && res.getResult().size() > 0) {
			retList = res.getResult();
		}

		return retList;
	}

	public double getForumSubjectIdFromDB(double messageId) throws Exception {
		String select_sql = "select subject_id from forum_message where message_id = " + Math.round(messageId);

		//System.out.println("sql: " + select_sql);
		
		ResultSet result = database.getResultSet(select_sql);
		double subjectId = 0;
		if (result != null && result.next() == true) {
			subjectId = result.getDouble(1);
		} 

		// reset
		database.resetResultSet();
		return subjectId;
	}

	/**
	 * Get the last subject_id
	 * @return
	 * @throws Exception
	 */
	public double getFirstSubjectIdFromDB() throws Exception {
		String select_sql = "select min(subject_id) from forum_subject";

		//System.out.println("sql: " + select_sql);
		
		ResultSet result = database.getResultSet(select_sql);
		double lastSubjectId = 0;
		if (result != null && result.next() == true) {
			lastSubjectId = result.getDouble(1);
		} 
	
		// reset
		database.resetResultSet();

		return lastSubjectId;
	}
	
	public double getLastSubjectIdFromDB() throws Exception {
		String select_sql = "select max(subject_id) from forum_subject";

		//System.out.println("sql: " + select_sql);
	
		ResultSet result = database.getResultSet(select_sql);
		double lastSubjectId = 0;
		if (result != null && result.next() == true) {
			lastSubjectId = result.getDouble(1);
		} 

		// reset
		database.resetResultSet();

		return lastSubjectId;
	}
	
}