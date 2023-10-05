package com.greenfield.ui.base;

import com.greenfield.common.base.BaseAction;
import com.greenfield.ui.cache.ScanCachePool;



/**
 * Welcome Action class.
 * 
 * @author Qin Zhang
 */
public class HomeAction extends BaseAction {
	
	private static final long serialVersionUID = 1L;

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
				
		System.out.println("home ->");
                //ScanCachePool.resetPool();

		//return the user to the welcome screen.
		return SUCCESS;
	}
	
}

