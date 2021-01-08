/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Helper;

import java.sql.Connection;

/**
 *
 * @author Umut
 */
public class User {
    public Connection conn;
    
    public User() {
        conn = (new DBConnection()).connect();
    }
}
