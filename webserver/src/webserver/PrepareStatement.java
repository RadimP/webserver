/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webserver;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetFactory;
import javax.sql.rowset.RowSetProvider;

/**
 *
 * @author RadimP
 */
public class PrepareStatement {

    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost/jeden_svet";
    static final Properties PROPERTIES = new Properties();

    public static CachedRowSet executeSelectDatabySelectedValue(String[] namesofcolumnsDTBtable, String nameofDTBtable, String columnnameDTBtable, Object value) throws SQLException {
        String querry = null;
        CachedRowSet crs = null;
        ResultSet rs = null;
        RowSetFactory factory = RowSetProvider.newFactory();
        if ("Datum".equals(columnnameDTBtable)) {
            value = HelperMethods.toStandardDatabaseDateString(value.toString());
        }
        String sql0_dotaz = "select " + HelperMethods.prependTablenameToColumnames(namesofcolumnsDTBtable, nameofDTBtable) + " from " + nameofDTBtable + " where " + columnnameDTBtable + " = ?;";
        try (Connection connection = HelperMethods.getDBConnection(); PreparedStatement st = connection.prepareStatement(sql0_dotaz)) {
            st.setObject(1, value);
            querry = st.toString().split(": ")[1];
            System.out.println(querry);
            crs = factory.createCachedRowSet();
            crs.populate(st.executeQuery());
            st.closeOnCompletion();
        } catch (SQLException ex) {
            Logger.getLogger(PrepareStatement.class.getName()).log(Level.SEVERE, null, ex);
        }
        return crs;
    }

    public static CachedRowSet executeSelectAllDates(String[] namesofcolumnsDTBtable, String nameofDTBtable) throws SQLException {
        String querry = null;
        CachedRowSet crs = null;
        ResultSet rs = null;
        RowSetFactory factory = RowSetProvider.newFactory();
        String sql0_dotaz = "select " + HelperMethods.prependTablenameToColumnames(namesofcolumnsDTBtable, nameofDTBtable) + " from " + nameofDTBtable + ";";
        try (Connection connection = HelperMethods.getDBConnection(); PreparedStatement st = connection.prepareStatement(sql0_dotaz)) {
            querry = st.toString().split(": ")[1];
            System.out.println(querry);
            crs = factory.createCachedRowSet();
            crs.populate(st.executeQuery());
            st.closeOnCompletion();
        } catch (SQLException ex) {
            Logger.getLogger(PrepareStatement.class.getName()).log(Level.SEVERE, null, ex);
        }
        return crs;
    }

    public static CachedRowSet executeSelectAllDataFromTable(Object value) throws SQLException {
        String querry = null;
        CachedRowSet crs = null;
        ResultSet rs = null;
        RowSetFactory factory = RowSetProvider.newFactory();
        String sql0_dotaz = "select * from " + value;
        try (Connection connection = HelperMethods.getDBConnection(); PreparedStatement st = connection.prepareStatement(sql0_dotaz)) {
            querry = st.toString().split(": ")[1];
            System.out.println(querry);
            crs = factory.createCachedRowSet();
            crs.populate(st.executeQuery());
            st.closeOnCompletion();
        } catch (SQLException ex) {
            Logger.getLogger(PrepareStatement.class.getName()).log(Level.SEVERE, null, ex);
        }
        return crs;
    }

    public static String executeSelectDatabySelectedValueUsingOneInnerJoin(String[] namesofcolumnsDTBtable_1, String nameofDTBtable_1, String nameofDTBtable_2, String columnnameDTBtable_1, String columnnameDTBtable_2, String conditioncolumnname, Object value) throws SQLException {
        String querry = null;
        if ("Datum".equals(conditioncolumnname)) {
            value = HelperMethods.toStandardDatabaseDateString(value.toString());
        }
        String sql1_dotaz = "select distinct " + HelperMethods.prependTablenameToColumnames(namesofcolumnsDTBtable_1, nameofDTBtable_1) + " from "
                + nameofDTBtable_1 + " inner join " + nameofDTBtable_2 + " on " + nameofDTBtable_1 + "." + columnnameDTBtable_1 + " = "
                + nameofDTBtable_2 + "." + columnnameDTBtable_2 + " where " + nameofDTBtable_2 + "." + conditioncolumnname + " = ?;";
        try (Connection connection = HelperMethods.getDBConnection(); PreparedStatement st = connection.prepareStatement(sql1_dotaz)) {
            st.setObject(1, value);
            querry = st.toString().split(": ")[1];
            System.out.println(querry);
            st.execute();
            st.closeOnCompletion();

        } catch (SQLException ex) {
            Logger.getLogger(PrepareStatement.class.getName()).log(Level.SEVERE, null, ex);
        }
        return querry;
    }

    public static String executeSelectDatabySelectedValueUsingOneInnerJoinThreeColumns(Object value) throws SQLException {
        String querry = null;
        String sql2_dotaz = "select predstaveni.idPredstaveni, film.JmenoF, predstaveni.idFilm from film inner join predstaveni on predstaveni.idFilm=film.idFilm where predstaveni.idFilm = ?;";
        try (Connection connection = HelperMethods.getDBConnection(); PreparedStatement st = connection.prepareStatement(sql2_dotaz)) {
            st.setObject(1, value);
            querry = st.toString().split(": ")[1];
            System.out.println(querry);
            st.execute();
            st.closeOnCompletion();

        } catch (SQLException ex) {
            Logger.getLogger(PrepareStatement.class.getName()).log(Level.SEVERE, null, ex);
        }
        return querry;
    }

    public static String executeSelectCinemaMoviesSchedule(Object value) throws SQLException {
        String querry = null;
        String sql2_dotaz = "select predstaveni.Datum, film.JmenoF, film.Reziser, film.Popis from film inner join predstaveni on predstaveni.idFilm=film.idFilm inner join  kino on kino.idKino = predstaveni.idKino where kino.idKino = ?;";
        try (Connection connection = HelperMethods.getDBConnection(); PreparedStatement st = connection.prepareStatement(sql2_dotaz)) {
            st.setObject(1, value);
            querry = st.toString().split(": ")[1];
            System.out.println(querry);
            st.execute();
            st.closeOnCompletion();

        } catch (SQLException ex) {
            Logger.getLogger(PrepareStatement.class.getName()).log(Level.SEVERE, null, ex);
        }
        return querry;
    }

    public static String executeUpdateEditedValueInDTB(String nameofDTBtable, String nameofupdatedcolumn, Object newvalue, String nameofconditioncolumn, Object valueofcondition) throws SQLException {
        String querry = null;
        if ("Datum".equals(nameofupdatedcolumn)) {
            newvalue = HelperMethods.toStandardDatabaseDateString(newvalue.toString());
        }
        String sql_dotaz = "UPDATE " + nameofDTBtable + " SET " + nameofupdatedcolumn + " = ? WHERE " + nameofconditioncolumn + " = ?;";
        try (Connection connection = HelperMethods.getDBConnection(); PreparedStatement st = connection.prepareStatement(sql_dotaz)) {
            st.setObject(1, newvalue);
            st.setObject(2, valueofcondition);
            querry = st.toString().split(": ")[1];
            System.out.println(querry);
            st.execute();
            st.closeOnCompletion();

        } catch (SQLException ex) {
            Logger.getLogger(PrepareStatement.class.getName()).log(Level.SEVERE, null, ex);
        }
        return querry;
    }

    public static String executeBatchUpdateEditedValueInDTB(String nameofDTBtable, Object[][] items, String nameofconditioncolumn, Object valueofcondition) throws SQLException {
        String querry = null;
        
        for(int i=0; i<items.length; i++)   { 
        if ("Datum".equals(items[i][0])) {
            items[i][1] = HelperMethods.toStandardDatabaseDateString( items[i][1].toString());
        }    
            
        String sql_dotaz = "UPDATE " + nameofDTBtable + " SET " + items[i][0] + " = ? WHERE " + nameofconditioncolumn + " = ?;";
        try (Connection connection = HelperMethods.getDBConnection(); PreparedStatement st = connection.prepareStatement(sql_dotaz)) {
            st.setObject(1, items[i][1]);
            st.setObject(2, valueofcondition);
            querry = st.toString().split(": ")[1];
            System.out.println(querry);
            st.execute();
            st.closeOnCompletion();

        } catch (SQLException ex) {
            Logger.getLogger(PrepareStatement.class.getName()).log(Level.SEVERE, null, ex);
        }}
        return querry;
    }

    public static String executeInsertNewItemIntoPredstaveniTable(String date, String idFilm) throws SQLException {
        String querry = null;
        date = HelperMethods.toStandardDatabaseDateString(date);
        String sql_dotaz = "INSERT INTO predstaveni (Datum, idFilm) values (?, ?);";
        try (Connection connection = HelperMethods.getDBConnection(); PreparedStatement st = connection.prepareStatement(sql_dotaz)) {
            st.setObject(1, date);
            st.setObject(2, idFilm);
            querry = st.toString().split(": ")[1];
            System.out.println(querry);
            st.execute();
            st.closeOnCompletion();

        } catch (SQLException ex) {
            Logger.getLogger(PrepareStatement.class.getName()).log(Level.SEVERE, null, ex);
        }
        return querry;
    }

    public static String executeInsertNewItemIntoFilmTable(String JmenoF, String Reziser, String Rok, String Popis) throws SQLException {
        String querry = null;
        String sql_dotaz = "INSERT INTO film (JmenoF, Reziser, Rok, Popis) values (?, ?, ?, ?);";
        try (Connection connection = HelperMethods.getDBConnection(); PreparedStatement st = connection.prepareStatement(sql_dotaz)) {
            st.setObject(1, JmenoF);
            st.setObject(2, Reziser);
            st.setObject(3, Rok);
            st.setObject(4, Popis);
            querry = st.toString().split(": ")[1];
            System.out.println(querry);
            st.execute();
            st.closeOnCompletion();

        } catch (SQLException ex) {
            Logger.getLogger(PrepareStatement.class.getName()).log(Level.SEVERE, null, ex);
        }
        return querry;
    }

    public static String executeInsertNewItemIntoKinoTable(String Nazev, String Ulice, String C_popisne, String C_orientacni, String Obec, String PSC) throws SQLException {
        String querry = null;
        String sql_dotaz = "INSERT INTO kino (Nazev, Ulice, C_popisne, C_orientacni, Obec, PSC) values (?, ?, ?, ?, ?, ?);";
        try (Connection connection = HelperMethods.getDBConnection(); PreparedStatement st = connection.prepareStatement(sql_dotaz)) {
            st.setObject(1, Nazev);
            st.setObject(2, Ulice);
            st.setObject(3, C_popisne);
            st.setObject(4, C_orientacni);
            st.setObject(5, Obec);
            st.setObject(6, PSC);
            querry = st.toString().split(": ")[1];
            System.out.println(querry);
            st.execute();
            st.closeOnCompletion();

        } catch (SQLException ex) {
            Logger.getLogger(PrepareStatement.class.getName()).log(Level.SEVERE, null, ex);
        }
        return querry;
    }

    public static String executeDeleteidFilmFromPredstaveni(Object value) throws SQLException {
        String querry = null;
        String sql0_dotaz = "update predstaveni set idFilm = null  where idFilm = ?;";
        try (Connection connection = HelperMethods.getDBConnection(); PreparedStatement st = connection.prepareStatement(sql0_dotaz)) {
            st.setObject(1, value);
            querry = st.toString().split(": ")[1];
            System.out.println(querry);
            st.execute();
            st.closeOnCompletion();

        } catch (SQLException ex) {
            Logger.getLogger(PrepareStatement.class.getName()).log(Level.SEVERE, null, ex);
        }
        return querry;
    }

    public static boolean logIn(String name, String password) throws SQLException {
        String sql0_dotaz = "select * from login where Username = ? and User_password = ?;";
        boolean result = false;
        try (Connection connection = HelperMethods.getLoginDBConnection(); PreparedStatement st = connection.prepareStatement(sql0_dotaz)) {
            st.setObject(1, name);
            st.setObject(2, password);
            ResultSet resultset = st.executeQuery();
            while (resultset.next()) {
                String username = resultset.getString("Username");
                System.out.println(username);
                String userpassword = resultset.getString("User_password");
                System.out.println(userpassword);
                if (username.equals(name) && userpassword.equals(password)) {
                    result = true;
                }
            }
            System.out.println(result);
            st.closeOnCompletion();

        } catch (SQLException ex) {
            Logger.getLogger(PrepareStatement.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    public static String executeDeleteidKinoFromPredstaveni(Object value) throws SQLException {
        String querry = null;
        String sql0_dotaz = "update predstaveni set idKino = null  where idKIno = ?;";
        try (Connection connection = HelperMethods.getDBConnection(); PreparedStatement st = connection.prepareStatement(sql0_dotaz)) {
            st.setObject(1, value);
            querry = st.toString().split(": ")[1];
            System.out.println(querry);
            st.execute();
            st.closeOnCompletion();

        } catch (SQLException ex) {
            Logger.getLogger(PrepareStatement.class.getName()).log(Level.SEVERE, null, ex);
        }
        return querry;
    }

    public static String executeSearchDataFullfilingQuantitativeRelation(String[] namesofcolumnsDTBtable, String nameofDTBtable, String columnnameDTBtable, String relation, Object value) throws SQLException {
        String querry = null;
        if (columnnameDTBtable == "Datum") {
            value = HelperMethods.toStandardDatabaseDateString(value.toString());
        }
        String sql_relation = "select " + HelperMethods.prependTablenameToColumnames(namesofcolumnsDTBtable, nameofDTBtable) + " from " + nameofDTBtable + " where " + columnnameDTBtable + relation
                + "?;";
        try (Connection connection = HelperMethods.getDBConnection(); PreparedStatement st = connection.prepareStatement(sql_relation)) {
            st.setObject(1, value);
            querry = st.toString().split(": ")[1];
            System.out.println(querry);
            st.execute();
            st.closeOnCompletion();

        } catch (SQLException ex) {
            Logger.getLogger(PrepareStatement.class.getName()).log(Level.SEVERE, null, ex);
        }
        return querry;
    }

    public static String executeSearchTextData(String[] namesofcolumnsDTBtable, String nameofDTBtable, String columnnameDTBtable, Object value) throws SQLException {
        String querry = null;
        if (columnnameDTBtable == "Datum") {
            value = HelperMethods.toStandardDatabaseDateString(value.toString());
        }
        String sql_text = "select " + HelperMethods.prependTablenameToColumnames(namesofcolumnsDTBtable, nameofDTBtable) + " from " + nameofDTBtable + " where " + columnnameDTBtable + " like " + "? COLLATE utf8_bin;";
        try (Connection connection = HelperMethods.getDBConnection(); PreparedStatement st = connection.prepareStatement(sql_text)) {
            st.setObject(1, value);
            querry = st.toString().split(": ")[1];
            System.out.println(querry);
            st.execute();
            st.closeOnCompletion();
        } catch (SQLException ex) {
            Logger.getLogger(PrepareStatement.class.getName()).log(Level.SEVERE, null, ex);
        }
        return querry;
    }

    public static String executeDeleteSelectedRows(String nameofDTBtable, String condition_columnnameDTBtable, Object condition_value) throws SQLException {
        String querry = null;
        String sql_delete = "DELETE FROM " + nameofDTBtable + " WHERE " + condition_columnnameDTBtable + " = ?;";
        try (Connection connection = HelperMethods.getDBConnection(); PreparedStatement st = connection.prepareStatement(sql_delete)) {
            st.setObject(1, condition_value);
            querry = st.toString().split(": ")[1];
            System.out.println(querry);
            st.execute();
            st.closeOnCompletion();
        } catch (SQLException ex) {
            Logger.getLogger(PrepareStatement.class.getName()).log(Level.SEVERE, null, ex);
        }
        return querry;
    }
}
