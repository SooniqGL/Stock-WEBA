/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.greenfield.test;

import com.greenfield.common.base.AppContext;
import com.greenfield.common.util.DBComm;
import com.greenfield.common.util.DumpObject;
import com.greenfield.common.object.SearchResult;
import com.greenfield.common.object.stock.Stock;
import com.greenfield.common.object.stock.StockDailyInfo;
import com.greenfield.common.object.user.UserInfo;

import java.util.Vector;

/**
 *
 * @author qin
 */
public class PostgreSQLTest2 {
    public static void main(String[] argv) {
      System.out.println("Testing ...");

      try {
        AppContext.init();  // read properties from properties file
        
        DBComm postgresDB = new DBComm(AppContext.POSTGRES_DB);
        
        postgresDB.openConnection();
        /*
        String select_sql = "select user_id, login_id, password, lname, fname, mname, title, " +
			"fax, phone, second_phone, email, type_cd, status_cd, comments, addr1, addr2, city, " +
			"state, zip, country_cd,  to_char(cre_date, 'MM/DD/YYYY') cre_date, receive_email " +
			"from web_user where 1 = 1 ";
        
        SearchResult list = postgresDB.runQuery(select_sql, UserInfo.class.getName());
        Vector uslist = list.getResult();
        for (int i = 0; i < uslist.size(); i ++) {
            UserInfo info = (UserInfo) uslist.get(i);
            DumpObject.generalToString(info);
        } */
     
        // empty string is not good for date!  Can use null instead.
        /*
        insert into login_history (log_id, user_id, login_id, password, is_valid, login_dt, 
        logout_dt, ip_addr, browser_version) values ('LOG3', 'USR01', 'admin',    
        'admin2', 'Y', '25-Dec-11', null, '127.0.0.1', '');
         
        insert into login_history (log_id, user_id, login_id, password, is_valid, login_dt, 
         ip_addr, browser_version) values ('LOG3', 'USR01', 'admin',    
        'admin2', 'Y', '25-Dec-11', '127.0.0.1', '')
         */
        /*
        
        String insert_sql = "insert into stock_daily_price (stock_id, m_date, open, " +
			" max, min, close, volume, adjusted_close ) values (" +
                "'stock1''0','2011/03/18','4.50','8.9','3.7', '6.79', '334567', '6.7899') ";
			 
        postgresDB.runUpdate(insert_sql);
        /*
        insert_sql = "insert into stock_daily_price (stock_id, m_date, open, " +
			" max, min, close, volume, adjusted_close ) values (" +
                "'stock1''0','04/28/2012','14.50','18.9','13.7', '16.79', '1334567', '16.7899') ";
			 
        postgresDB.runUpdate(insert_sql);
       */

        // rownum = 1 is not good in Postgres!!!!  -> limit/offset
        // dd-mmm-yy is accept as input in Postgres as well- good;  YYYY-MM-DD is accepted
        // YYYY/MM/DD, AND MM/DD/YYYY are also accepted!
        // order by is good to use as well; desc - will change order
        // if use order by, then "limit #" is after "order by" -> " order by m_date desc limit 1"

        String select_sql2 = "select stock_id, to_char(m_date, 'YYYY/MM/DD') m_date, open, " +
			"max, min, close, volume, adjusted_close, to_char(m_date, 'D') day_in_week " +
			"from stock_daily_price where " +
                " m_date >= to_date('2001/03/31', 'YYYY/MM/DD') order by m_date desc limit 10";
        
        SearchResult list2 = postgresDB.runQuery(select_sql2, StockDailyInfo.class.getName());
        Vector uslist2 = list2.getResult();
        for (int i = 0; i < uslist2.size(); i ++) {
            StockDailyInfo info = (StockDailyInfo) uslist2.get(i);
            DumpObject.generalToString(info);
        } 
        
        /*
        String select_sql3 = "select to_char(min(m_date), 'YYYY/MM/DD') first_date, " +
			"to_char(max(m_date), 'YYYY/MM/DD') last_date " +
			"from stock_daily_price where 1 = 1 ";

        SearchResult res = postgresDB.runQuery(select_sql3, "com.greenfield.common.object.stock.Stock");
        if (res != null && res.getResult() != null) {
                Stock stockInfo = (Stock) res.getResult().get(0);
                DumpObject.generalToString(stockInfo);
        } */
        
        postgresDB.closeConnection();
      
      } catch (Exception cnfe) {
        System.out.println("Error ....");
        cnfe.printStackTrace();
        System.exit(1);
      }
    }
}
