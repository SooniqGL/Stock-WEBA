/*
 * Created on Jul 3, 2006
 */
package com.greenfield.ui.handler.perform;

import java.util.Vector;

import com.greenfield.common.base.WebSessionContext;
import com.greenfield.common.handler.BaseHandler;
import com.greenfield.common.object.BaseObject;
import com.greenfield.common.object.perform.PerformVO;

/**
 * @author zhangqx
 *
 * Prepare the display for the performance page.
 */
public class PerformHandler extends BaseHandler {
	public BaseObject doAction(BaseObject inputVO, WebSessionContext sessionContext) {
		PerformVO vo = (PerformVO) inputVO;
		String mode = vo.getMode();
		String trend = vo.getTrend();
		
		// load the search result from oracle
		try {
			Vector list = queryScanHistory(mode, trend);
			vo.setScanList(list);
		} catch (Exception e) {
			e.printStackTrace();
			// have trouble to query
			vo.setScanList(new Vector());
		}
		
		return vo;
	}
	
	private Vector queryScanHistory(String mode, String trend) {
		Vector list = new Vector();
		
		return list;
	}
}
