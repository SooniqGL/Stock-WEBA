package com.greenfield.ui.util.test;

import java.util.Vector;

import com.greenfield.common.base.AppContext;
import com.greenfield.common.dao.analyze.StockAdminDAO;
import com.greenfield.common.util.DBComm;
import com.greenfield.common.object.stock.Stock;

public class ResetStockVolumeTest {
	public static void main(String[] args) {
		try {
			ResetStockVolumeTest test = new ResetStockVolumeTest();
			test.loadVolume();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public void loadVolume() throws Exception {
		AppContext.init();
		DBComm database = new DBComm();
		
		try {
			long bt = System.currentTimeMillis();
			database.openConnection();
			
			StockAdminDAO stockDao = new StockAdminDAO();
			stockDao.setDatabase(database);
			
			Vector stockList = stockDao.getStockListFromDB(null, null, null);
			for (int i = 0; i < stockList.size(); i++) {
				Stock stk = (Stock) stockList.get(i);
				double vol = stockDao.getOneVolume(stk.getStockId());
				stk.setVolume(vol);
				stockDao.updateStock(stk);
			}
			
			long sp = System.currentTimeMillis() - bt;
			System.out.println("spend: " + sp);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			database.closeConnection();
		}
	}

}
