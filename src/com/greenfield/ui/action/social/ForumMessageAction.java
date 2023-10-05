/*
 * Created on Dec 30, 2006
 */
package com.greenfield.ui.action.social;

import javax.servlet.http.HttpServletRequest;

import com.greenfield.common.base.BaseAction;
import com.greenfield.ui.handler.social.ForumMessageHandler;
import com.greenfield.common.object.forum.ForumVO;

/**
 * @author qin
 *
 */
public class ForumMessageAction extends BaseAction {
	
	/** page id string and mode */
	public static final String BLANK 		= "blank";
	public static final String NEW_BLANK 	= "nb";  // request the blank form to create message
	public static final String NEW_ADD 		= "na";  // the form is submitted
	public static final String QUERY 		= "q";
		
	/** return string */
	public static final String NEW_MESSAGE = "newmessage";
	public static final String MESSAGE_LIST = "messagelist";
	
	private ForumVO inputVO = new ForumVO();
	
	/**
	 * 1) To make efficient page, make sure to create index for message_id in forum_subject table
	 * and parent_id in the forum_message table.
	 * 2) If possible, need to create search by keywords or date functions.
	 */
	public String executeAction()
		throws Exception {

		String retString = MESSAGE_LIST;
		String mode = inputVO.getMode();		
		// System.out.println("mode: " + mode);
		
		try {
			if (mode == null || mode.equals("")) {
				inputVO.setMode(BLANK);
			}
					
			inputVO.setUserId(user.getUserId());
			ForumMessageHandler handler = new ForumMessageHandler();
			handler.execute(user, inputVO, sessionContext);	
			
			if (mode.equals(NEW_BLANK)) {
				retString = NEW_MESSAGE;
			} else if (mode.equals(NEW_ADD) && !inputVO.getSuccess()) {
				retString = NEW_MESSAGE;
			} 			
		} catch (Exception e) {
			
			//c_logger.instr(ke);	
			e.printStackTrace();
			inputVO.setMessage("Some system error: " + e.getMessage());
			inputVO.setSuccess(false);
		}
				
		
		return retString;  
	}

	public ForumVO getInputVO() {
		return inputVO;
	}

	public void setInputVO(ForumVO inputVO) {
		this.inputVO = inputVO;
	}
	
	
}