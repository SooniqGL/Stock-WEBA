/*
 * Created on Jan 5, 2007
 */
package com.greenfield.ui.applet.market;

import java.util.Vector;

/**
 * @author zhangqx
 *
 * Utitlity for the applet
 */
public class MarketAgeHelper {
	public void prepareData(MarketAgeContext processContext) {
		String ageContent = processContext.getAgeContent();
		if (ageContent == null || ageContent.equals("")) {
			return;   // error
		}
		
		Vector firstLevelList = MarketAgeUtil.mySplit(ageContent, "#");
		if (firstLevelList != null && firstLevelList.size() > 1) {
			processContext.setMarketName((String) firstLevelList.get(0));
		} else {
			return;   // error
		}
		
		// each entry will be a daily info
		// format: {date} "|" {kye:value} "|" {kye:value}, ...
		String key = "";
		int cnt = 0;
		int maxCnt = 0;
		for (int i = 1; i < firstLevelList.size(); i ++) {
			Vector secondLevelList = MarketAgeUtil.mySplit((String) firstLevelList.get(i), "|");
			if (secondLevelList != null && secondLevelList.size() > 2) {
				MarketAgeDaily daily = new MarketAgeDaily();
				
				// first element is date
				daily.setMDate((String)secondLevelList.get(0));
				daily.setDateInLongFormat(MarketAgeUtil.getDateStringInLongFormat((String)secondLevelList.get(0)));
				
				// from third, second one is total tickers
				daily.setTotalTickers((new Double((String)secondLevelList.get(1))).intValue());
				
				// remains are key:value pairs
				int[] cpList = new int[13];
				int[] dpList = new int[13];
				int[] cnList = new int[13];
				int[] dnList = new int[13];
				
				for (int j = 2; j < secondLevelList.size(); j ++) {
					Vector pairs = MarketAgeUtil.mySplit((String) secondLevelList.get(j), ":");
					if (pairs != null && pairs.size() == 2) {
						key = (String) pairs.get(0);
						if (key == null || key.length() < 3) {
							// error - skip - should not happen
							continue;
						}
						
						int value = (new Integer((String) pairs.get(1))).intValue();
						if (maxCnt < value) {
							maxCnt = value;
						}
						
						if (key.startsWith("CP")) {
							updateCountList(cpList, key, value);
						} else if (key.startsWith("DP")) {
							updateCountList(dpList, key, value);
						} else if (key.startsWith("CN")) {
							updateCountList(cnList, key, value);
						} else if (key.startsWith("DN")) {
							updateCountList(dnList, key, value);
						}
					}
				}
				
				daily.setCpList(cpList);
				daily.setCnList(cnList);
				daily.setDpList(dpList);
				daily.setDnList(dnList);
				
				daily.setTotalUp(sumup(cpList));
				daily.setTotalDown(sumup(cnList));
				daily.setTotalToUp(sumup(dnList));
				daily.setTotalToDown(sumup(dpList));
				processContext.addAgeDailyToList(daily);
			}
		}
		
		// max count
		processContext.setMaxCount(maxCnt);
	}

	private int sumup(int[] loclist) {
		int sum = 0;
		for (int i = 0; i < loclist.length; i ++) {
			sum += loclist[i];
		}
		
		return sum;
	}

	private void updateCountList(int[] locList, String key, int value) {
		String thirdS = key.substring(2);
		
		if (thirdS.equals("N") || thirdS.equals("P")) {
			// 13th element
			locList[12] = value;
		} else {
			// 1 - 12
			int keyInt = (new Integer(thirdS)).intValue() - 1;
			if (keyInt >= 0 && keyInt <= 11) {
				locList[keyInt] = value;
			} else {
				// error - should not happen
			}
		}
	}
	
	// New York#2006/07/06|2793.0|CN1:112|DN1:9|CN2:302|DN2:10|CN3:274|DN3:5|CN4:461|DN4:13|CN5:145|DN5:6|CN6:135|DN6:10|CN7:125|DN7:5|CN8:52|DN8:3|CN9:35|DN9:1|CN10:20|CN11:24|DN11:1|CN12:12|CNN:15|CP1:454|DP1:2|CP2:135|DP2:2|CP3:151|CP4:55|CP5:82|DP5:2|CP6:38|DP6:2|CP7:16|CP8:28|CP9:12|CP10:25|CP11:3|CP12:10|CPP:72#2006/07/07|2793.0|CN1:103|DN1:7|CN2:270|DN2:10|CN3:237|DN3:7|CN4:503|DN4:15|CN5:140|DN5:5|CN6:126|DN6:5|CN7:122|DN7:5|CN8:58|DN8:1|CN9:39|CN10:18|CN11:25|DN11:1|CN12:12|CNN:15|CP1:482|DP1:
	public static void main(String[] args) {
		MarketAgeHelper helper = new MarketAgeHelper();
		String content = "New York#2006/07/06|2793.0|CN1:112|DN1:9|CN2:302|DN2:10|CN3:274|DN3:5|CN4:461|DN4:13|CN5:145|DN5:6|CN6:135|DN6:10|CN7:125|DN7:5|CN8:52|DN8:3|CN9:35|DN9:1|CN10:20|CN11:24|DN11:1|CN12:12|CNN:15|CP1:454|DP1:2|CP2:135|DP2:2|CP3:151|CP4:55|CP5:82|DP5:2|CP6:38|DP6:2|CP7:16|CP8:28|CP9:12|CP10:25|CP11:3|CP12:10|CPP:72#2006/07/07|2793.0|CN1:103|DN1:7|CN2:270|DN2:10|CN3:237|DN3:7|CN4:503|DN4:15|CN5:140|DN5:5|CN6:126|DN6:5|CN7:122|DN7:5|CN8:58|DN8:1|CN9:39|CN10:18|CN11:25|DN11:1|CN12:12|CNN:15|CP1:482";
		
		MarketAgeContext processContext = new MarketAgeContext();
		processContext.setAgeContent(content);
		
		helper.prepareData(processContext);
	}
}
