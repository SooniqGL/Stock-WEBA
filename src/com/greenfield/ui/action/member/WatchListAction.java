/*
 * Created on Jun 20, 2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.greenfield.ui.action.member;



import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.greenfield.common.base.BaseAction;
import com.greenfield.ui.handler.member.AlertListHandler;
import com.greenfield.ui.handler.member.MemberHomeHandler;
import com.greenfield.ui.handler.member.WatchListHandler;
import com.greenfield.common.object.member.MemberHomeVO;
import com.greenfield.common.object.member.WatchListVO;

/**
 * @author zhangqx
 *
 */
public class WatchListAction extends BaseAction {
	/** page id string and mode */
	public static final String BLANK = "blank";
	public static final String UPDATE_FOLDER = "updatefolder";
	public static final String ADD_WATCH = "addwatch";
	public static final String UPDATE_WATCH = "updatewatch";
	public static final String VIEW_WATCH_LIST = "viewwatchlist";
	public static final String DELETE_WATCH = "deletewatch";
	public static final String ALERT_LIST = "alertlist";
	public static final String MEMBER_HOME = "memberhome";
	public static final String NEW_FOLDER = "newfolder";
	
	/** TYPE */
	public static final String LIST = "list";
	public static final String CHART = "chart";
	public static final String ONE = "one";
	public static final String UPDATE = "update";
	public static final String ADD_LIST_TO_FOLDER = "addlist";
	public static final String ADD_ONE_TO_FOLDER = "addone";
	public static final String DELETE_FOLDER = "delete";
	public static final String RENAME_FOLDER = "rename";
	public static final String MOVE_ENTRY = "move";
	public static final String COPY_ENTRY = "copy";
	

	private WatchListVO inputVO = new WatchListVO();
	
	public String executeAction()
		throws Exception {
						
		//String method = request.getMethod();
		//if (method.equalsIgnoreCase("get")) {
		//	return LOGIN; 
		//}
		

		WatchListVO watchVO = getInputVO();
		String retString = "";
		String mode = watchVO.getMode();
		String type = watchVO.getType();
		watchVO.setUserId(user.getUserId());
		
		//System.out.println("mode: " + mode);
		//System.out.println("type: " + type);
		
		if (mode == null || mode.equals("") || mode.equals(BLANK)) {
			mode = VIEW_WATCH_LIST;
			type = LIST;
			watchVO.setMode(mode);
			watchVO.setType(type);
		}
		
		try {
			if (mode.equals(UPDATE_FOLDER)) {
				//// handle the adding here
				WatchListHandler watchHandler = new WatchListHandler();
				watchVO = (WatchListVO) watchHandler.execute(user, watchVO, sessionContext);
					
				if (type == null || type.equals(BLANK)) {
					watchVO.setMessage("");
					retString = UPDATE_FOLDER;
				} else if (type.equals(ADD_LIST_TO_FOLDER)) {
					retString = VIEW_WATCH_LIST;
				} else if (type.equals(MOVE_ENTRY) || type.equals(COPY_ENTRY)) {
					if (watchVO.getSuccess()) {
						retString = VIEW_WATCH_LIST;
					} else {
						retString = MEMBER_HOME;
					}
				} else {
					retString = MEMBER_HOME;
				}
			} else if (mode.equals(ADD_WATCH)) {
				// handle the update here - type = update
				WatchListHandler watchHandler = new WatchListHandler();
				watchVO = (WatchListVO) watchHandler.execute(user, watchVO, sessionContext);

				if (type == null || type.equals(BLANK)) {
					if (watchVO.isAlreadyInWatch()) {
						retString = UPDATE_WATCH;
					} else {
						retString = ADD_WATCH;
					}
				} else {
					if (watchVO.getSuccess()) {		
						//retString = VIEW_WATCH_LIST;
						retString = ADD_WATCH;
					} else {
						retString = ADD_WATCH;
					}
				}
			} else if (mode.equals(UPDATE_WATCH)) {
				// handle the update here - type = update
				WatchListHandler watchHandler = new WatchListHandler();
				watchVO = (WatchListVO) watchHandler.execute(user, watchVO, sessionContext);
	
				if (type == null || type.equals(BLANK)) {
					retString = UPDATE_WATCH;
				} else {
					if (watchVO.getSuccess()) {		
						retString = VIEW_WATCH_LIST;
					} else {
						retString = UPDATE_WATCH;
					}
				}
			} else if (mode.equals(NEW_FOLDER)) {
				// create new folder
				WatchListHandler watchHandler = new WatchListHandler();
				watchVO = (WatchListVO) watchHandler.execute(user, watchVO, sessionContext);

				if (type == null || type.equals(BLANK)) {
					retString = NEW_FOLDER;
				} else {
					//if (watchVO.getSuccess()) {		
					//	retString = MEMBER_HOME;
					//} else {
						retString = NEW_FOLDER;
					//}
				}
			} else if (mode.equals(DELETE_WATCH) || mode.equals(VIEW_WATCH_LIST)) {
				// handle the update here - type = update
				WatchListHandler watchHandler = new WatchListHandler();
				watchVO = (WatchListVO) watchHandler.execute(user, watchVO, sessionContext);
				retString = VIEW_WATCH_LIST;
			
			} else if (mode.equals(ALERT_LIST)) {
				// handle the update here - type = update
				AlertListHandler alertHandler = new AlertListHandler();
				watchVO = (WatchListVO) alertHandler.execute(user, watchVO, sessionContext);
				retString = ALERT_LIST;
						
			} else {
				retString = mode;
			}
		} catch (Exception e) {
			
			//c_logger.instr(ke);	
			e.printStackTrace();
			//request.setAttribute("error", "Some system error: " + e.getMessage());
		}
		
		if (retString.equals(VIEW_WATCH_LIST)) {
			// default the view screen to display just list, not charts
			if (type == null || type.equals("") || !type.equals(CHART)) {
				watchVO.setType(LIST);
			}
		} 
				
		
		return retString;  
	}

	public WatchListVO getInputVO() {
		return inputVO;
	}

	public void setInputVO(WatchListVO inputVO) {
		this.inputVO = inputVO;
	}
	
	
}