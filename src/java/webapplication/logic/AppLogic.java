package webapplication.logic;

import java.sql.Connection;
import java.sql.SQLException;
import javax.naming.Context;
import javax.sql.DataSource;

public class AppLogic {
    
    public static Connection connect() throws SQLException{
        
        try{
            
            Context initContext = new javax.naming.InitialContext();
            Context envContext  = (javax.naming.Context)initContext.lookup("java:/comp/env");
            
            DataSource dataSource = (DataSource)envContext.lookup("jdbc/baza");
            Connection connection = dataSource.getConnection();
            return connection;
            
        }catch(Exception e){
            throw new SQLException("Nie pobrano połączenia z kontekstu");
        }
           
    }
    
}
