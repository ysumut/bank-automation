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
public abstract class Employee {
    public Connection conn;
    
    public Employee() {
        conn = (new DBConnection()).connect();
    }
    
    public abstract String[][] getCustomers();
}
