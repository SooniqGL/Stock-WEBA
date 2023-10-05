package com.greenfield.ui.action.group;

/*
 * Created on May 30, 2014
 */
import javax.servlet.http.HttpServletRequest;

import com.greenfield.common.base.BaseAction;
import com.greenfield.ui.handler.group.GroupHandler;
import com.greenfield.ui.handler.member.UserInfoHandler;
import com.greenfield.common.object.group.UserGroupVO;


/**
 * @author zhangqx
 */
public class GroupAction extends BaseAction {
	private static final long serialVersionUID = 1L;
	
	/** page id string and mode */
	public static final String BLANK = "blank";
	public static final String UPDATE = "update";

	/** return string */
	public static final String ADD_WATCH			= "addwatch";
	public static final String DELETE_WATCH			= "deletewatch";
	public static final String GROUP_HOME 			= "grouphome";
	public static final String EXIST_GROUP_LIST 	= "grouplist";
	public static final String GROUP_USER_LIST 		= "groupuserlist";     // LIST users in one group - ajax
	public static final String GROUP_REQUEST_LIST 	= "grouprequestlist";  // list users request for one group, page/ajax
	public static final String NEW_GROUP 			= "newgroup";          // start one group
	public static final String SEARCH_GROUP 		= "searchgroup";          // start one group
	public static final String USER_GROUP 			= "usergroup";
	public static final String UPDATE_GROUP 		= "updategroup";     // ajax
	public static final String UPDATE_REQUEST 		= "updaterequest";
	public static final String UPDATE_USER_ACCT 	= "updateuser";
	public static final String UPDATE_USER_BY_OWNER = "updateuserbyowner";   // ajax
	public static final String PROCESS_REQUESTS_BY_OWNER = "processrequestsbyowner";   // ajax call to approve/deny a list of user requests.
	public static final String NEW_INVITE 			= "newinvite";
	public static final String ANSWER_INVITE 		= "answerinvite";

	private UserGroupVO inputVO = new UserGroupVO();

	public String executeAction() throws Exception {

		String retString = GROUP_HOME;
		String mode = inputVO.getMode();
		String type = inputVO.getType();

		System.out.println("mode: " + mode);
		// System.out.println("type: " + inputVO.getType());

		try {
			if (mode == null || mode.equals("") || mode.equals("blank")) {
				mode = GROUP_HOME;
				inputVO.setMode(mode);
			}
			
			if (type == null || type.equals("")) {
					inputVO.setType(BLANK);
			}

			inputVO.setUserId(user.getUserId());
			
			if (mode.equals(GROUP_HOME) || mode.equals(GROUP_USER_LIST) || mode.equals(GROUP_REQUEST_LIST)) {
				GroupHandler handler = new GroupHandler();
				handler.execute(user, inputVO, sessionContext);
				retString = mode;
			} else if (mode.equals(NEW_GROUP)) {
				GroupHandler handler = new GroupHandler();
				handler.execute(user, inputVO, sessionContext);
				
				if (type != null && type.equals(UPDATE)) {
					// if it is submit, and success
					if (inputVO.getSuccess()) {
						retString = GROUP_HOME;
					} else {
						retString = NEW_GROUP;
					}
					
				} else {
					retString = NEW_GROUP;
				}
				
			} else if (mode.equals(NEW_INVITE)) {
				retString = NEW_INVITE;
				GroupHandler handler = new GroupHandler();
				handler.execute(user, inputVO, sessionContext);
				
			} else if (mode.equals(SEARCH_GROUP)) {
				retString = SEARCH_GROUP;
			}
		} catch (Exception e) {

			// c_logger.instr(ke);
			e.printStackTrace();
			inputVO.setMessage("Some system error: " + e.getMessage());
			inputVO.setSuccess(false);
			
			throw e;
		}

		return retString;
	}

	public UserGroupVO getInputVO() {
		return inputVO;
	}

	public void setInputVO(UserGroupVO inputVO) {
		this.inputVO = inputVO;
	}

}