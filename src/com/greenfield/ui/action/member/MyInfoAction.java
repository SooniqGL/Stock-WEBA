/*
 * Created on Jun 20, 2006
 */
package com.greenfield.ui.action.member;

import javax.servlet.http.HttpServletRequest;

import com.greenfield.common.base.BaseAction;
import com.greenfield.ui.handler.member.UserInfoHandler;
import com.greenfield.common.object.member.MyInfoVO;

/**
 * @author zhangqx
 */
public class MyInfoAction extends BaseAction {

	/** page id string and mode */
	public static final String BLANK = "blank";
	public static final String UPDATE = "update";

	/** return string */
	public static final String PROFILE = "profile";
	public static final String UPDATEMYINFO 	= "updatemyinfo";
	public static final String CHANGEPASSWORD 	= "changepassword";
	
	/** change password 2, comes from login, and will go to regular home after it is done */
	public static final String CHANGEPASSWORD2 	= "changepassword2";
	public static final String GO_HOME          = "gohome";

	private MyInfoVO inputVO = new MyInfoVO();

	public String executeAction() throws Exception {

		String retString = PROFILE;
		String mode = inputVO.getMode();
		String type = inputVO.getType();

		System.out.println("mode: " + mode);
		System.out.println("type: " + inputVO.getType());

		try {
			if (mode == null || mode.equals("") || mode.equals("blank")) {
				mode = PROFILE;
				inputVO.setMode(mode);
			}
			
			if (type == null || type.equals("")) {
					inputVO.setType(BLANK);
			}

			inputVO.setUserId(user.getUserId());
			if (mode.equals(PROFILE)) {
				UserInfoHandler handler = new UserInfoHandler();
				handler.execute(inputVO, inputVO, sessionContext);
			} else if (mode.equals(UPDATEMYINFO)) {
				UserInfoHandler handler = new UserInfoHandler();
				handler.execute(inputVO, inputVO, sessionContext);
				
				if (type != null && type.equals(UPDATE)) {
					// if it is submit, and success
					if (inputVO.getSuccess()) {
						retString = PROFILE;
					} else {
						retString = UPDATEMYINFO;
					}
					
				} else {
					retString = UPDATEMYINFO;
				}
				
			// handle change password from login or profile manager
		    // the difference is the layout
			} else if (mode.equals(CHANGEPASSWORD) || mode.equals(CHANGEPASSWORD2)) {
				UserInfoHandler handler = new UserInfoHandler();
				handler.execute(inputVO, inputVO, sessionContext);
				
				if (type != null && type.equals(UPDATE)) {
					// if it is submit, and success
					if (inputVO.getSuccess()) {
						if (mode.equals(CHANGEPASSWORD)) {
							retString = PROFILE;
						} else {
							retString = GO_HOME;
						}
					} else {
						retString = mode;
					}
					
				} else {
					retString = mode;
				}
			}
		} catch (Exception e) {

			// c_logger.instr(ke);
			e.printStackTrace();
			inputVO.setMessage("Some system error: " + e.getMessage());
			inputVO.setSuccess(false);
		}

		return retString;
	}

	public MyInfoVO getInputVO() {
		return inputVO;
	}

	public void setInputVO(MyInfoVO inputVO) {
		this.inputVO = inputVO;
	}

}