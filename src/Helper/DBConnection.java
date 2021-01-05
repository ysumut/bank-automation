/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Helper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
/**
 *
 * @author Umut
 */
public class DBConnection {
    private Connection conn;
    private String url = "jdbc:derby://localhost:1527/yubbank";
    private String username = null;
    private String password = null;
    
    public Connection connect() {
        try {
            conn = DriverManager.getConnection(url, username, password);
        } 
        catch(SQLException e) {
            System.out.println("DB Bağlanamadı");
            System.out.println(e);
        }
        
        return conn;
    }
}
