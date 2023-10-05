/*
 * Created on Nov 21, 2006
 */
package com.greenfield.ui.action.analyze;


import com.greenfield.common.base.BaseAction;
import com.greenfield.ui.handler.market.MarketAgeHandler;
import com.greenfield.ui.handler.market.MarketPulseHandler;
import com.greenfield.common.object.market.MarketPulseVO;

/**
 * @author zhangqx
 */
public class MarketPulseAction extends BaseAction {
	
	/** page id string and mode */
	public static final String BLANK = "blank";
	public static final String EMA_PULSE = "ema";
		
	/** return string */
	public static final String MARKET_PULSE = "marketpulse";
	public static final String MARKET_AGE 	= "marketage";
	
	private MarketPulseVO inputVO = new MarketPulseVO();
	
	public String executeAction()
		throws Exception {
								
		String retString = MARKET_PULSE;
		String mode = inputVO.getMode();		
		// System.out.println("mode: " + mode);
		
		try {
			if (mode == null || mode.equals("")) {
				inputVO.setMode(BLANK);
			}
			
			if (mode.equals(MARKET_AGE)) { 
				MarketAgeHandler handler = new MarketAgeHandler();
				handler.execute(user, inputVO, sessionContext);	
				retString = MARKET_AGE;
			} else {
				MarketPulseHandler handler = new MarketPulseHandler();
				handler.execute(user, inputVO, sessionContext);	
				
				inputVO.setDisplayTitle(getTitle(inputVO.getMarketType(), inputVO.getChartType()));
			}
								
		} catch (Exception e) {
			
			//c_logger.instr(ke);	
			e.printStackTrace();
			inputVO.setMessage("Some system error: " + e.getMessage());
			inputVO.setSuccess(false);
		}
				
		
		return retString;  
	}

	public MarketPulseVO getInputVO() {
		return inputVO;
	}

	public void setInputVO(MarketPulseVO inputVO) {
		this.inputVO = inputVO;
	}
	
	
	public String getTitle(String marketType, String chartType) {
		String title = "";
		
		if (chartType != null) {
			if (chartType.equals("MB")) {
				title = "BPI / Advance-Decline";
			} else if (chartType.equals("ME")) {
				title = "EMA Stats";
			} else if (chartType.equals("MW")) {
				title = "William Index / New Highs & Lows";
			}
		}
		
		if (marketType != null) {
			if (marketType.equals("N")) {
				title += " (New York Market)";
			} else if (marketType.equals("Q")) {
				title += " (Nasdaq Market)";
			}
		}
		
		return title;
	}
	
	
}