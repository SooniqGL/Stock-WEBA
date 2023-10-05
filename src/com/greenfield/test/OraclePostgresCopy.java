/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.greenfield.test;

import com.greenfield.common.base.AppContext;
import com.greenfield.common.dao.analyze.StockAdminDAO;
import com.greenfield.common.util.DBComm;
import com.greenfield.common.object.stock.Stock;
import com.greenfield.common.object.stock.StockDailyInfo;

import java.util.Vector;

/**
 *
 * @author qin
 */
public class OraclePostgresCopy {
    public static void main(String[] argv) {
      System.out.println("Testing ...");
      OraclePostgresCopy copier = new OraclePostgresCopy();

      try {
        AppContext.init();  // read properties from properties file
        
        DBComm oracleDB = new DBComm(AppContext.ORACLE_DB);
        oracleDB.openConnection();
        StockAdminDAO oracleStockDAO = new StockAdminDAO();
        oracleStockDAO.setDatabase(oracleDB);
        
        DBComm postgresDB = new DBComm(AppContext.POSTGRES_DB);
        postgresDB.openConnection();
        StockAdminDAO postgresStockDAO = new StockAdminDAO();
        postgresStockDAO.setDatabase(postgresDB);
        
        // get stock list and insert
        Vector<Stock> stockList = copier.copyStockListFromOracleToPostgres(oracleStockDAO, postgresStockDAO);
        System.out.println("Stocklist: " + stockList.size());
        for (int i = 0; i < stockList.size(); i ++) {
            copier.copyStockDailyPriceFromOracleToPostgres(oracleStockDAO, postgresStockDAO, stockList.get(i).getStockId());
        }
        
        // close connections
        oracleDB.closeConnection();
        postgresDB.closeConnection();
      } catch (Exception e) {
          e.printStackTrace();
      }
    }
    
    
    /*
     * Return stock list in Oracle;
     * Insert each to the Postgres!
     */
    private Vector copyStockListFromOracleToPostgres(StockAdminDAO oracleStockDAO, 
            StockAdminDAO postgresStockDAO) throws Exception {
        Vector stockList = null;
        
        stockList = oracleStockDAO.getStockListFromDB(null, null, null);
        
        // postgresStockDAO.loadStockListTable(stockList);
        return stockList;
    }
    
    // get all the data from Oracle, and insert into Postgres
    private void copyStockDailyPriceFromOracleToPostgres(StockAdminDAO oracleStockDAO, 
            StockAdminDAO postgresStockDAO, String stockId) throws Exception {
        
        Vector dailyList = oracleStockDAO.getStockDailyPriceListFromDB(stockId, null, null);
        System.out.println("StockID: " + stockId + ", Daily list: " + dailyList.size());
        
        for (int i = 0; i < dailyList.size(); i ++ ) {
            StockDailyInfo info = (StockDailyInfo) dailyList.get(i);
            postgresStockDAO.insertStockDailyPrice(info);
        }
    }
    
  
}
