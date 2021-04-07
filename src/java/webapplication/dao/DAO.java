package webapplication.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import webapplication.logic.AppLogic;
import webapplication.model.User;

public class DAO {
    
    Connection connection;
    
    public List<User> getAllUsers(){
        
        connection();
        ResultSet rs;
        
        List<Integer> id = new ArrayList<>();
        List<String> nazwisko = new ArrayList<>();
        List<String> imie = new ArrayList<>();
        List<User> users = new ArrayList<>();
        
        try{
            rs = connection.createStatement(java.sql.ResultSet.TYPE_SCROLL_INSENSITIVE,java.sql.ResultSet.CONCUR_READ_ONLY).executeQuery("select * db_user_table");
            rs.first();
            do{
                id.add(rs.getInt("id"));
                nazwisko.add(rs.getString("nazwisko"));
                imie.add(rs.getString("imie"));
            }while(rs.next());
        } catch (SQLException ex) {
            String blad = ex.toString();
            System.err.println(blad);
        }
        
        for(int i=0;i<id.size();i++){
            users.add(new User(id.get(i),nazwisko.get(i),imie.get(i)));
        }
        
        return users;
    }
    
    public void connection(){
        
        try {       
            connection = AppLogic.connect();
        } catch (SQLException ex) {
            String blad = ex.toString();
            System.err.println(blad);
        }
        
    }
    
}
