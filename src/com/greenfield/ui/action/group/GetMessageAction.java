package com.greenfield.ui.action.group;

import java.io.File;
import java.util.Arrays;

import com.greenfield.common.base.BaseAction;
import com.greenfield.common.object.group.MessageVO;
import com.greenfield.common.object.group.UserGroupVO;
import com.greenfield.ui.handler.group.GroupHandler;
import com.greenfield.ui.handler.group.MessageHandler;

public class GetMessageAction extends BaseAction {
	private static final long serialVersionUID = 1L;
	
	/** page id string and mode */
	public static final String BLANK = "blank";
	public static final String UPDATE = "update";

	/** return string */
	
	public static final String MESSAGE_HOME 		= "messagehome";
	
	public static final String GET_PAGE_LIST		= "getpagelist";  // page info, for a set of pages
	public static final String GET_MESSAGE_LIST		= "getmessagelist";  // one page - a set of messages (subject/partial content, simple author info)
	public static final String MESSAGE_DETAIL		= "messagedetail";
	// public static final String GET_REPLY_LIST		= "getreplylist";   ??? need to consider paging for replies as well
	
	public static final String REPLY_MESSAGE 		= "replymessage";
	public static final String SEARCH_MESSAGE 		= "searchmessage";     // search in one or multiple groups
	
	
	private MessageVO inputVO = new MessageVO();

	public String executeAction() throws Exception {

		String retString = MESSAGE_HOME;
		String mode = inputVO.getMode();
		String type = inputVO.getType();

		System.out.println("getMessageAction: mode: " + mode);
		// System.out.println("type: " + inputVO.getType());
	

		try {
			if (mode == null || mode.equals("") || mode.equals("blank")) {
				mode = MESSAGE_HOME;
				inputVO.setMode(mode);
			}
			
			if (type == null || type.equals("")) {
					inputVO.setType(BLANK);
			}

			inputVO.setUserId(user.getUserId());
			
			if (mode.equals(MESSAGE_HOME)) {
				MessageHandler handler = new MessageHandler();
				handler.execute(user, inputVO, sessionContext);
				retString = mode;
			} 
		} catch (Exception e) {

			// c_logger.instr(ke);
			e.printStackTrace();
			inputVO.setMessage("Some system error: " + e.getMessage());
			inputVO.setSuccess(false);
			
			throw e;
		}

		System.out.println("return: " + retString);
		return retString;
	}

	public MessageVO getInputVO() {
		return inputVO;
	}

	public void setInputVO(MessageVO inputVO) {
		this.inputVO = inputVO;
	}

	
	

}
