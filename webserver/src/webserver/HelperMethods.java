/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webserver;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author RadimP
 */

public class HelperMethods {

    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost/jeden_svet";
    static final String DB_URL_LOGIN = "jdbc:mysql://localhost/login";
    static final Properties PROPERTIES = new Properties();

    public static Connection getDBConnection() {Connection dbConnection = null;
        try {
            setPropertiesForConnection(PROPERTIES);                            
            Class.forName(JDBC_DRIVER);           
            dbConnection = DriverManager.getConnection(
                    DB_URL, PROPERTIES);           
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(HelperMethods.class.getName()).log(Level.SEVERE, null, ex);
        }
return dbConnection;
    }
    
  public static Connection getLoginDBConnection() {Connection dbConnection = null;
        try {
            setPropertiesForConnection(PROPERTIES);                            
            Class.forName(JDBC_DRIVER);           
            dbConnection = DriverManager.getConnection(
                    DB_URL_LOGIN, PROPERTIES);           
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(HelperMethods.class.getName()).log(Level.SEVERE, null, ex);
        }
return dbConnection;
    }  

    private static void setPropertiesForConnection(Properties properties) {
        properties.setProperty("user", "root");
    properties.setProperty("password", "1111");
            properties.setProperty("useSSL", "false");
            properties.setProperty("autoReconnect", "true");     
    }
    
    
    
    

    public static String toStandardDatabaseDateString(String date) {
        String date_splitted[] = date.split("[\\p{Punct}]"); //pro případ překlepů ošetřeno dělení libovolným intepunkčním znakem
        if (date_splitted.length == 3 && date_splitted[0].length() != 4) { //druhá podmínka je zde proto, aby nebylo děleno datum, které bude zadáno již v cílovém formátu
            date = date_splitted[2] + "-" + addZeroToStart(date_splitted[1]) + "-" + addZeroToStart(date_splitted[0]);
        }
        return date;
    }

    private static String addZeroToStart(String number) {
        if (number.length() == 1) {
            number = "0" + number;
        }
        return number;
    }

    public static String toStandardCzechDateString(String date) {
        String[] parts = date.split("[\\p{Punct}&&[^.]]"); //pro případ překlepů ošetřeno dělení libovolným intepunkčním znakem kromě tečky, 
        // když tato data se bnačítají z databáze, tak by se v nich překlepy neměly vyskytovat
        if (parts.length == 3 && parts[0].length() > 2) { //druhá podmínka je zde proto, aby nebylo děleno datum, které bude zadáno již v cílovém formátu
            date = removeZeroFromStart(parts[2]) + "." + removeZeroFromStart(parts[1]) + "." + parts[0];
        }       
        return date;
    }

    private static String removeZeroFromStart(String number) {
        if (number.length() == 2 && number.charAt(0) == '0') {
            number = String.valueOf(number.charAt(1));
        }
        return number;
    }

    public static String prependTablenameToColumnames(String[] namesofcolumns, String nameofDTBtable) {
        return Arrays.toString(namesofcolumns).replace("[", nameofDTBtable + ".").replace("]", "").replaceAll(", ", ", " + nameofDTBtable + ".");

    }
    
   
}
