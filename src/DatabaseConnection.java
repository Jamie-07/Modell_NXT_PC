
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Jan-Peter Schmidt
 */
public class DatabaseConnection {
    
    private ConnectionSource cs = null;
    private static final String USER = "root";
    private static final String PASSWORD = "master2018?";
    private static final String URL = "jdbc:mysql://localhost:8888/nxt_cloud";
    
    public DatabaseConnection() {
        
        try {
            TableUtils.createTableIfNotExists(getConnectionSource(), DataPoint.class);
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public ConnectionSource getConnectionSource() {
        
        if(cs!=null) {
            return cs;
        }
        
        try {
            cs = new JdbcConnectionSource(URL, USER, PASSWORD);
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return cs;
        
    }
    
    public void storeData(HashMap<String, String> data) {
        
        data.entrySet().forEach((set) -> {
        
            DataPoint dataPoint = new DataPoint(set.getKey(), set.getValue(), this);
            System.out.println("Saving datapoint with " + dataPoint.getData());
            dataPoint.save();
        
        });
        
    }
    
}
