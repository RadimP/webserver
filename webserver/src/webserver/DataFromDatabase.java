/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webserver;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Vector;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.RowSetMetaData;
import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetFactory;
import javax.sql.rowset.RowSetProvider;

/**
 *
 * @author RadimP
 */
public class DataFromDatabase {

  
    public static CachedRowSet getCachedRowSet(String sqldotaz) {
         CachedRowSet crs = null;
                    String sql = sqldotaz;
            
            try (Statement stmt = HelperMethods.getDBConnection().createStatement();
                    ResultSet rs = stmt.executeQuery(sql); ) {
                try {
                RowSetFactory factory = RowSetProvider.newFactory();    
                    crs = factory.createCachedRowSet();
                } catch (SQLException ex) {
                    Logger.getLogger(DataFromDatabase.class.getName()).log(Level.SEVERE, null, ex);
                }
                crs.populate(rs);
            } catch (SQLException ex) {
                Logger.getLogger(DataFromDatabase.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        
        return crs;
    }

    private static int getColumnCount(CachedRowSet crs) throws SQLException {
        int columns;
        RowSetMetaData md = (RowSetMetaData) crs.getMetaData();
        return columns = md.getColumnCount();
    }

  /*  private static Vector<Vector<Object>> extractDataFromCachedRowSet(CachedRowSet crs, int columncount) throws SQLException {
        Vector<Vector<Object>> data = new Vector<Vector<Object>>();
        while (crs.next()) {
            Vector<Object> row = new Vector<Object>(columncount);
            for (int i = 1; i <= columncount; i++) { if (crs.getObject(i) != null) {
                if (crs.getObject(i).toString().length() > 4 && crs.getObject(i).toString().charAt(4) != '-') {
                    row.addElement(crs.getObject(i));
                } else {
                    row.addElement(HelperMethods.toStandardCzechDateString(crs.getObject(i).toString()));
                }
            } else {row.addElement(crs.getObject(i));}}
            data.addElement(row);
        }
        return data;
    }*/

 /*   public static Vector<Vector<Object>> getDataFromSQLDatabase(String sqldotaz) {
        String sql = sqldotaz;
        CachedRowSet crs = null;
        Vector<Vector<Object>> data = new Vector<Vector<Object>>();
        try {
            crs = DataFromDatabase.getCachedRowSet(sqldotaz);
             
            int columns = getColumnCount(crs);
            data = extractDataFromCachedRowSet(crs, columns);
        } catch (SQLException ex) {
            Logger.getLogger(DataFromDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }
        return data;
    } */

    public static String[] getColumnNamesAsArray(String sqldotaz) {
        String[] columnNamesArray = null;
        CachedRowSet crs = null;
        try {
            crs = DataFromDatabase.getCachedRowSet(sqldotaz);
            RowSetMetaData md = (RowSetMetaData) crs.getMetaData();
            int columns = md.getColumnCount();
            columnNamesArray = new String[columns];
            for (int i = 1; i <= columns; i++) {
                columnNamesArray[i - 1] = md.getColumnName(i);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DataFromDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }
        return columnNamesArray;
    }
public static String[] getColumnNamesAsArrayFromCRS(CachedRowSet crs) {
        String[] columnNamesArray = null;
         try {
            RowSetMetaData md = (RowSetMetaData) crs.getMetaData();
            int columns = md.getColumnCount();
            columnNamesArray = new String[columns];
            for (int i = 1; i <= columns; i++) {
                columnNamesArray[i - 1] = md.getColumnName(i);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DataFromDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }
        return columnNamesArray;
    }

 /*   public static Vector<Object> getColumnNamesFromSQLDatabase(String sqldotaz) {
        CachedRowSet crs = null;
        Vector<Object> columnNames = new Vector<Object>();
        try {
            crs = DataFromDatabase.getCachedRowSet(sqldotaz);
            RowSetMetaData md = (RowSetMetaData) crs.getMetaData();
            int columns = md.getColumnCount();
            for (int i = 1; i <= columns; i++) {
                columnNames.addElement(md.getColumnName(i));
            }
        } catch (SQLException ex) {
            Logger.getLogger(DataFromDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }
        return columnNames;
    }*/

       
    public static ArrayList<Object[]> extractDataToArrayList(String sqldotaz) throws SQLException {
        ResultSet rs = null;
        int columncount = 0;
        ArrayList<Object[]> temp = new ArrayList<>();
        try (Statement stmt = HelperMethods.getDBConnection().createStatement();
               ) {
            rs  = stmt.executeQuery(sqldotaz);
            columncount = rs.getMetaData().getColumnCount();
            int counter=0;
        
        while (rs.next()) {
            Object[] row = new Object[columncount];
            for (int i = 1; i <= columncount; i++) { if (rs.getObject(i) != null) {
                if (rs.getObject(i).toString().length() > 4 && rs.getObject(i).toString().charAt(4) != '-') {
                    row[i-1] =rs.getObject(i);
                } else {
                    row[i-1] =HelperMethods.toStandardCzechDateString(rs.getObject(i).toString());
                }
            } else {row[i-1]=rs.getObject(i);}}
            temp.add(row);
            counter++;
        }
        } catch (SQLException ex) {
            Logger.getLogger(DataFromDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }        
        return temp;
    }
    
    public static ArrayList<Object[]> extractDataToArrayListFromCRS(CachedRowSet rs) throws SQLException {
        
        int columncount = rs.getMetaData().getColumnCount();
        ArrayList<Object[]> temp = new ArrayList<>();
        int counter=0;
        
        while (rs.next()) {
            Object[] row = new Object[columncount];
            for (int i = 1; i <= columncount; i++) { if (rs.getObject(i) != null) {
                if (rs.getObject(i).toString().length() > 4 && rs.getObject(i).toString().charAt(4) != '-') {
                    row[i-1] =rs.getObject(i);
                } else {
                    row[i-1] =HelperMethods.toStandardCzechDateString(rs.getObject(i).toString());
                }
            } else {row[i-1]=rs.getObject(i);}}
            temp.add(row);
            counter++;
        }                
        return temp;
    }
}   
