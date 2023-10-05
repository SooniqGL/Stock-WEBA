/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.greenfield.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author qin
 */
public class PostgreSQLTest {
    public static void main(String[] argv) {
  System.out.println("Checking if Driver is registered with DriverManager.");
  
  try {
    Class.forName("org.postgresql.Driver");
  } catch (ClassNotFoundException cnfe) {
    System.out.println("Couldn't find the driver!");
    System.out.println("Let's print a stack trace, and exit.");
    cnfe.printStackTrace();
    System.exit(1);
  }
  
  System.out.println("Registered the driver ok, so let's make a connection.");
  
  Connection c = null;
  
  try {
    // The second and third arguments are the username and password,
    // respectively. They should be whatever is necessary to connect
    // to the database.
    c = DriverManager.getConnection("jdbc:postgresql://localhost/postgres",
                                    "postgres", "*");
  } catch (SQLException se) {
    System.out.println("Couldn't connect: print out a stack trace and exit.");
    se.printStackTrace();
    System.exit(1);
  }
  
  if (c != null)
    System.out.println("Hooray! We connected to the database!");
  else
    System.out.println("We should never get here.");

    
    Statement s = null;
    try {
      s = c.createStatement();
    } catch (SQLException se) {
      System.out.println("We got an exception while creating a statement:" +
                         "that probably means we're no longer connected.");
      se.printStackTrace();
      System.exit(1);
    }
    ResultSet rs = null;
    try {
      rs = s.executeQuery("SELECT * FROM films");
    } catch (SQLException se) {
      System.out.println("We got an exception while executing our query:" +
                         "that probably means our SQL is invalid");
      se.printStackTrace();
      System.exit(1);
    }

    int index = 0;

    try {
      while (rs.next()) {
          System.out.println("Here's the result of row " + index++ + ":");
          System.out.println(rs.getString(1));
          System.out.println(rs.getString(2));
          System.out.println(rs.getInt(3));
          System.out.println(rs.getDate(4));
          System.out.println(rs.getString(5));
          System.out.println(rs.getString(6));
      }
    } catch (SQLException se) {
      System.out.println("We got an exception while getting a result:this " +
                         "shouldn't happen: we've done something really bad.");
      se.printStackTrace();
      System.exit(1);
    }
    
    
    
    
    Statement s2 = null;
try {
    rs.close();
    s.close();
    
  //s2 = c.createStatement();
} catch (SQLException se) {
  System.out.println("We got an exception while creating a statement:" +
                     "that probably means we're no longer connected.");
  se.printStackTrace();
  System.exit(1);
}

int m = 0;
/*
try {
  m = s2.executeUpdate("INSERT INTO films VALUES " +
                      "('UB100', 'Practical PostgreSQL', 1212, '2011-11-30', 'Comedy 2', '55 minutes')");
} catch (SQLException se) {
  System.out.println("We got an exception while executing our query:" +
                     "that probably means our SQL is invalid");
  se.printStackTrace();
  System.exit(1);
} */

System.out.println("Successfully modified " + m + " rows.\n");


    }  
}
