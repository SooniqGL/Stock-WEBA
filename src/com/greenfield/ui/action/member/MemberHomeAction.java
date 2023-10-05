/*
 * Created on Nov 1, 2006
 */
package com.greenfield.ui.action.member;


import com.greenfield.common.base.BaseAction;
import com.greenfield.ui.handler.member.MemberHomeHandler;
import com.greenfield.common.object.member.MemberHomeVO;

/**
 * @author zhangqx
 */
public class MemberHomeAction extends BaseAction {
	public static final String MEMBER_HOME = "memberhome";

	private MemberHomeVO inputVO = new MemberHomeVO();
	
	/**
	 * Performs the action for the user logoff request. 
	 * 
	 * @param	form		the action form.
	 * @param	request		the http request. 
	 * @return	the action string as a result of the action performed.
	 * @exception	CnetException
	 */
	public String executeAction()
		throws Exception {
			
	
			try {
				MemberHomeHandler handler = new MemberHomeHandler();
				inputVO.setUserId(user.getUserId());
				handler.execute(user, inputVO, sessionContext);	
						
			} catch (Exception e) {
				e.printStackTrace();
				inputVO.setMessage("Some system error: " + e.getMessage());
			}
	
		//return the user to the welcome screen.
		return MEMBER_HOME;
	}

	public MemberHomeVO getInputVO() {
		return inputVO;
	}

	public void setInputVO(MemberHomeVO inputVO) {
		this.inputVO = inputVO;
	}
	
	

}

