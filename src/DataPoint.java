
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import java.util.Date;
import java.sql.SQLException;
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
@DatabaseTable
public class DataPoint {
    
    @DatabaseField(generatedId = true)
    private Integer id;
    
    @DatabaseField
    private String name;
    
    @DatabaseField
    private String data;
    
    //Taken from https://stackoverflow.com/a/7083613
    @DatabaseField(dataType = DataType.DATE_STRING, format = "yyyy-MM-dd HH:mm:ss",
      columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private Date date;
    
    private DatabaseConnection dbc;

    public DataPoint() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    
    public DataPoint(String name, String data, DatabaseConnection dbc) {
        this.name = name;
        this.data = data;
        this.dbc = dbc;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
    
    public void save() {
        
        try {
            Dao dao = DaoManager.createDao(dbc.getConnectionSource(), this.getClass());
            dao.create(this);         
            
        } catch (SQLException ex) {
            Logger.getLogger(DataPoint.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
}
