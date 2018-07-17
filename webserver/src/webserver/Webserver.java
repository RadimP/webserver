package webserver;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.rowset.CachedRowSet;

/**
 *
 * @author Student
 */
public class Webserver {

    // static DataFromDatabase dtb = new DataFromDatabase();
    int port = 9000;

    //  static ArrayList<Object[]> data;
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        try {
            HttpServer server = HttpServer.create(new InetSocketAddress(9000), 0);
            server.createContext("/", new RootHandler());
            server.createContext("/echoHeader", new EchoHeaderHandler());
            server.createContext("/echoGet", new EchoGetHandler());
            server.createContext("/echoPost", new EchoPostHandler());
            server.setExecutor(null);
            server.start();
        } catch (IOException ex) {
            Logger.getLogger(Webserver.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static class RootHandler implements HttpHandler {

        @Override

        public void handle(HttpExchange he) throws IOException {
            String response = "<h1>Server start success if you see this message</h1>" + "<h1>Port: " + "9000" + "</h1> <br><button onclick=\"loadDoc()\">Click me</button>\n"
                    + "\n";
            he.sendResponseHeaders(200, response.length());
            OutputStream os = he.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }

    }

    public static class EchoHeaderHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange he) throws IOException {
            Headers headers = he.getRequestHeaders();
            Set<Map.Entry<String, List<String>>> entries = headers.entrySet();
            String response = "";
            for (Map.Entry<String, List<String>> entry : entries) {
                response += entry.toString() + "\n";
            }
            he.sendResponseHeaders(200, response.length());
            OutputStream os = he.getResponseBody();
            os.write(response.toString().getBytes());
            os.close();
        }
    }

    public static class EchoGetHandler implements HttpHandler {

        @Override

        public void handle(HttpExchange he) throws IOException {
            String sqlquerry = "SELECT * FROM film where ";
            // parse request
            LinkedHashMap<String, Object> parameters = new LinkedHashMap<String, Object>();
            URI requestedUri = he.getRequestURI();
            String query = requestedUri.getRawQuery();
            //  String text = query.substring(query.indexOf("%")+1, query.indexOf("%")+3);
            System.out.println(query);
            //  System.out.println(text);
            //           int num = Integer.parseInt(text, 16);
//String bin = Integer.toString(num, 2);
//System.out.println();

            parseQuery(query, parameters);
            // System.out.println(parameters);
            String[] keys = new String[parameters.size()];
            Object[] values = new Object[parameters.size()];
            int index = 0;
            for (Map.Entry<String, Object> mapEntry : parameters.entrySet()) {
                values[index] = mapEntry.getValue();
                index++;
            }
            //   values = parameters.values().toArray();;
//System.out.println(values.length);
//System.out.println(parameters.entrySet());
//System.out.println(Arrays.deepToString(values));

            // serializes target to Json
            //MyType target2 = gson.fromJson(json, MyType.class);
            // send response
            Gson gson = new Gson();
            String json = "";
            switch ((String) values[0]) {

                case "selectall":
                    String sqldotaz1 = "SELECT * FROM film";
                    ArrayList<Object[]> data1 = getData(sqldotaz1);
                            
                    String[] columnames1 = getColumnnames(sqldotaz1);
                    Object[][] array = resultOfQuerryWithHeadings(data1, columnames1);
                    json = gson.toJson(array);
                
                    break;
                case "selectone":
                    String sqldotaz2 = "SELECT * FROM film";
                    ArrayList<Object[]> data2 = null;
                    String[] columnames2 = DataFromDatabase.getColumnNamesAsArray(sqldotaz2);
                    try {
                        data2 = DataFromDatabase.extractDataToArrayListFromCRS(PrepareStatement.executeSelectDatabySelectedValue(columnames2, "film", (String) values[1], (String) values[2]));
                    } catch (SQLException ex) {
                        Logger.getLogger(Webserver.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    json = gson.toJson(data2);
                                    

                    break;
                default:

                    break;

            }
sendResponse(he, json);
        }
    }

    public static class EchoPostHandler implements HttpHandler {

        @Override

        public void handle(HttpExchange he) throws IOException {
            // parse request
            LinkedHashMap<String, Object> parameters = new LinkedHashMap<String, Object>();
            InputStreamReader isr = new InputStreamReader(he.getRequestBody(), "utf-8");
            BufferedReader br = new BufferedReader(isr);
            String query = br.readLine();
            parseQuery(query, parameters);
            String[] keys = new String[parameters.size()];
            Object[] values = new Object[parameters.size()];
            int index = 0;
            for (Map.Entry<String, Object> mapEntry : parameters.entrySet()) {
                values[index] = mapEntry.getValue();
                index++;
            }
            
            
            Gson gson = new Gson();
            String json = "";
            switch ((String) values[0]) {

                case "selectall":
                    CachedRowSet crs = null;
                    ArrayList<Object[]> data1 = null;
                    try {
                        crs = PrepareStatement.executeSelectAllDataFromTable((String) values[1]);
                        data1 = DataFromDatabase.extractDataToArrayListFromCRS(crs);
                    } catch (SQLException ex) {
                        Logger.getLogger(Webserver.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    String[] columnames1 = getColumnnames(crs);
                    Object[][] array = resultOfQuerryWithHeadings(data1, columnames1);
                    json = gson.toJson(array);
                   
                    break;
                    
                case "selectone":
                    String sqldotaz2 = "SELECT * FROM " + values[1];
                    ArrayList<Object[]> data2 = null;
                    String[] columnames2 = getColumnnames(sqldotaz2);
                    try {
                        data2 = DataFromDatabase.extractDataToArrayListFromCRS(PrepareStatement.executeSelectDatabySelectedValue(columnames2, (String) values[1], (String) values[2], (String) values[3]));
                    } catch (SQLException ex) {
                        Logger.getLogger(Webserver.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    json = gson.toJson(data2);
                         
                    break;

                case "selectdates":
                    String sqldotaz3 = "SELECT " + values[1] + " FROM " + values[2];
                    ArrayList<Object[]> data3 = null;
                    String[] columnames3 = getColumnnames(sqldotaz3);
                    try {
                        data3 = DataFromDatabase.extractDataToArrayListFromCRS(PrepareStatement.executeSelectAllDates(columnames3, (String) values[2]));
                    } catch (SQLException ex) {
                        Logger.getLogger(Webserver.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    String[] dates = datesToArray(data3);
                    TreeSet<String> str2 = selectAndSortDistinctDates(dates);                
                    json = gson.toJson(str2);
                                    
                    break;
              
                    case "updaterow":
                    Object[][] items;
                    Gson gson1 = new GsonBuilder().create();
                    items = gson1.fromJson((String) values[2], Object[][].class);
                    System.out.println(Arrays.deepToString(values));
                  //   System.out.println(Arrays.deepToString(items));   
            
          // for(int i=0; i<items.length; i++)   {                      
            {
                try {
                    
                json=PrepareStatement.executeBatchUpdateEditedValueInDTB((String) values[1], items, (String) values[4], values[3]);
             //  json=PrepareStatement.executeUpdateEditedValueInDTB((String) values[1], (String) items[i][0], (String)items[i][1], (String) values[4], values[3]);
                    
                } catch (SQLException ex) {
                    Logger.getLogger(Webserver.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
                
         //  }
                json="update done";   
                         
                    break;

                default:

                    break;

            }
         sendResponse(he, json);
        }
    }

    private static void parseQuery(String query, LinkedHashMap<String, Object> parameters) throws UnsupportedEncodingException {

        if (query != null) {
            String pairs[] = query.split("[&]");
            //  System.out.println(Arrays.toString(pairs));
            for (int i = 0; i < pairs.length; i++) {
                String param[] = pairs[i].split("[=]");
                //    System.out.println(Arrays.toString(param));
                String key = null;
                String value = null;
                if (param.length > 0) {
                    key = URLDecoder.decode(param[0],
                            System.getProperty("file.encoding"));
                }

                if (param.length > 1) {
                    value = URLDecoder.decode(param[1],
                            System.getProperty("file.encoding"));
                }

                if (parameters.containsKey(key)) {
                    Object obj = parameters.get(key);
                    if (obj instanceof List<?>) {
                        List<String> values = (List<String>) obj;
                        values.add(value);

                    } else if (obj instanceof String) {
                        List<String> values = new ArrayList<String>();
                        values.add((String) obj);
                        values.add(value);
                        parameters.put(key, values);
                    }
                } else {
                    parameters.put(key, value);
                }
            }
        }
    }

    private static Object[][] resultOfQuerryWithHeadings(ArrayList<Object[]> data, String[] columnames) {
        Object[][] array = new Object[data.size() + 1][data.get(0).length];
        for (int k = 0; k < data.get(0).length; k++) {
            array[0][k] = columnames[k];
        }
        for (int i = 1; i < data.size() + 1; i++) {
            for (int j = 0; j < data.get(0).length; j++) {//System.out.println(data.get(i)[j]);
                array[i][j] = data.get(i - 1)[j];
            }
        }
        return array;
    }
    private static String[] datesToArray(ArrayList<Object[]> data3) {
        String[] dates = new String[data3.size() * data3.get(0).length];
                    int counter = 0;
                    for (int i = 0; i < data3.size(); i++) {
                        for (int j = 0; j < data3.get(0).length; j++) {
                            dates[counter] = (String) data3.get(i)[j];
                            counter++;
                        }
                    }
    return dates;
    }
    private static TreeSet<String> selectAndSortDistinctDates(String[] dates) {
    Datescomparator comp = new Datescomparator();
                  TreeSet<String> str2 = new TreeSet<>(comp);
                   for(String date: dates) {str2.add(date);} 
                   return str2;
    
    }
    private static String[] getColumnnames(CachedRowSet crs) {
    String[] columnames = DataFromDatabase.getColumnNamesAsArrayFromCRS(crs);
    return columnames;
    }
    private static String[] getColumnnames(String sqlquerry) {
    String[] columnames = DataFromDatabase.getColumnNamesAsArray(sqlquerry);
    return columnames;
    }
   private static ArrayList<Object[]> getData(String sqlquerry) {
   ArrayList<Object[]> data1 = null;
                   try {
                            data1 = DataFromDatabase.extractDataToArrayList(sqlquerry);
                        } catch (SQLException ex) {
                            Logger.getLogger(Webserver.class.getName()).log(Level.SEVERE, null, ex);
                        }
   return data1;
   }
  private static void sendResponse(HttpExchange he, String json) {
        try {
            String response = json;
            int responselength1 = response.getBytes(StandardCharsets.UTF_8).length;
            System.out.println(responselength1);
            Headers responseHeaders1 = he.getResponseHeaders();
            responseHeaders1.add("Access-Control-Allow-Origin", "*");
            he.sendResponseHeaders(200, responselength1);
            try (OutputStream os1 = he.getResponseBody()) {
                os1.write(response.getBytes());
                os1.flush();
            }
        } catch (IOException ex) {
            Logger.getLogger(Webserver.class.getName()).log(Level.SEVERE, null, ex);
        }
  
  }
}
