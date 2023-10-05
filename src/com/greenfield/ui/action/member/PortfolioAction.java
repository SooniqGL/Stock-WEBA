/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.greenfield.ui.action.member;

import com.greenfield.common.base.BaseAction;
import com.greenfield.ui.handler.member.MemberHomeHandler;
import com.greenfield.ui.handler.member.PortfolioHandler;
import com.greenfield.common.object.member.MemberHomeVO;
import com.greenfield.common.object.member.PortfolioVO;
import com.greenfield.common.object.member.PositionInfo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Qin
 */
public class PortfolioAction extends BaseAction {
    /** page id string and mode */
    public static final String BLANK = "blank";
    public static final String NEW_PORTFOLIO        	= "newportfolio";
    public static final String NEW_POSITION         	= "newposition";
    public static final String UPDATE_PORTFOLIO     	= "updateportfolio";
    public static final String UPDATE_POSITION      	= "updateposition";
    public static final String VIEW_CLOSED_POSITIONS 	= "viewclosedpositions";
    public static final String VIEW_POSITIONS       	= "viewpositions";
    public static final String VIEW_TRANSACTIONS    	= "viewtransactions";
    public static final String STOCKINFO_AJAX       	= "stockinfoajax";
    public static final String MEMBER_HOME          	= "memberhome";

    /** TYPE */
    public static final String LIST = "list";
    public static final String UPDATE = "update";
    public static final String DELETE = "delete";  // remove all the entries for portfolio in three tables
    public static final String ADD_LIST_TO_FOLDER = "addlist";

	
    private PortfolioVO inputVO = new PortfolioVO();
	
	public String executeAction()
		throws Exception {
						
		//String method = request.getMethod();
		//if (method.equalsIgnoreCase("get")) {
		//	return LOGIN; 
		//}
		

		PortfolioVO portfolioVO = getInputVO();
		String retString = "";
		String mode = portfolioVO.getMode();
		String type = portfolioVO.getType();
		portfolioVO.setUserId(user.getUserId());
		
		System.out.println("portfolio mode: " + mode);
		System.out.println("type: " + type);
		
		if (mode == null || mode.equals("") || mode.equals(BLANK)) {
			mode = VIEW_POSITIONS;
			type = LIST;
			portfolioVO.setMode(mode);
			portfolioVO.setType(type);
		}
		
		try {
			if (mode.equals(NEW_PORTFOLIO)) {
				//// handle the adding here
				PortfolioHandler portfolioHandler = new PortfolioHandler();
				portfolioVO = (PortfolioVO) portfolioHandler.execute(user, portfolioVO, sessionContext);
					
				if (type == null || type.equals(BLANK)) {
					portfolioVO.setMessage("");
					retString = NEW_PORTFOLIO;
				} else {
					retString = MEMBER_HOME;
				}
			} else if (mode.equals(UPDATE_PORTFOLIO)) {
				// handle the update here - type = update
				PortfolioHandler portfolioHandler = new PortfolioHandler();
				portfolioVO = (PortfolioVO) portfolioHandler.execute(user, portfolioVO, sessionContext);

				if (type == null || type.equals(BLANK)) {
                     retString = UPDATE_PORTFOLIO;
				} else {		
                     retString = MEMBER_HOME;
				}

                //System.out.println("return string: " + retString);
                                
			} else if (mode.equals(NEW_POSITION)) {
				// create new folder
                                portfolioVO.setMessage("");  // clear the message
				PortfolioHandler portfolioHandler = new PortfolioHandler();
				portfolioVO = (PortfolioVO) portfolioHandler.execute(user, portfolioVO, sessionContext);

				if (type == null || type.equals(BLANK)) {
					retString = NEW_POSITION;
				} else {
					if (portfolioVO.getSuccess()) {
						retString = VIEW_POSITIONS;
					} else {
						// in case of error
						retString = NEW_POSITION;
					}
				}
			} else if (mode.equals(UPDATE_POSITION)) {
				// handle the update here - type = update
				PortfolioHandler portfolioHandler = new PortfolioHandler();
				portfolioVO = (PortfolioVO) portfolioHandler.execute(user,
						portfolioVO, sessionContext);

				if (type == null || type.equals(BLANK)) {
					retString = UPDATE_POSITION;

				} else {
					retString = VIEW_POSITIONS;
				}
			
			} else if (mode.equals(VIEW_POSITIONS)) {
				// handle the update here - type = update
				PortfolioHandler portfolioHandler = new PortfolioHandler();
				portfolioVO = (PortfolioVO) portfolioHandler.execute(user,
						portfolioVO, sessionContext);
				retString = VIEW_POSITIONS;

			} else if (mode.equals(VIEW_TRANSACTIONS)) {
				// handle the update here - type = update
				PortfolioHandler portfolioHandler = new PortfolioHandler();
				portfolioVO = (PortfolioVO) portfolioHandler.execute(user,
						portfolioVO, sessionContext);
				retString = VIEW_TRANSACTIONS;

			} else if (mode.equals(VIEW_CLOSED_POSITIONS)) {
				// handle the update here - type = update
				PortfolioHandler portfolioHandler = new PortfolioHandler();
				portfolioVO = (PortfolioVO) portfolioHandler.execute(user,
						portfolioVO, sessionContext);
				retString = VIEW_CLOSED_POSITIONS;

			} else if (mode.equals(STOCKINFO_AJAX)) {
				// prepare the stock info for ajax call
				PortfolioHandler portfolioHandler = new PortfolioHandler();
				portfolioVO = (PortfolioVO) portfolioHandler.execute(user,
						portfolioVO, sessionContext);
				retString = STOCKINFO_AJAX;

			} else {
				retString = mode;
			}
		} catch (Exception e) {
			
			//c_logger.instr(ke);	
			e.printStackTrace();
			//request.setAttribute("error", "Some system error: " + e.getMessage());
		}
		
		if (retString.equals(VIEW_POSITIONS)) {
			// default the view screen to display just list, not charts
			if (type == null || type.equals("") || !type.equals(LIST)) {
				portfolioVO.setType(LIST);
			}
		} 
				
		
		return retString;  
	}

	public PortfolioVO getInputVO() {
		return inputVO;
	}

	public void setInputVO(PortfolioVO inputVO) {
		this.inputVO = inputVO;
	}
	
	
}