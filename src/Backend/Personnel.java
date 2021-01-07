/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Backend;

import Helper.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 *
 * @author Umut
 */
public class Personnel {
    private Connection conn;
    
    public Personnel() {
        conn = (new DBConnection()).connect();
    }
    
    public String[][] getCustomers() {
        String sorgu = "SELECT * FROM users WHERE account_type = 3";
        
        try {
            PreparedStatement ps = conn.prepareStatement(sorgu);
            ResultSet rs = ps.executeQuery();
            
            String[][] customers = new String[100][3];
            for(int i=0; rs.next() == true; i++) {
                String status = rs.getString("approved").equals("1") ? "Onaylandı" : "Onaylanmadı";
                
                String[] row = {rs.getString("full_name"), rs.getString("email"), rs.getString("tc_no"),
                    rs.getString("address"), status};
                customers[i] = row;
            }
            
            return customers;

        } catch (Exception e) {
            System.out.println(e);
            String[][] response = {{"false", "Bir hata ile karşılaştık."}};
            return response;
        }
    }
}
