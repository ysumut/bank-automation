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
public class Bills {
    private Connection conn;
    
    public Bills() {
        conn = (new DBConnection()).connect();
    }
    
    public String[] getBills(String user_id) {
        String sorgu = "SELECT * FROM bills";
        
        try {
            PreparedStatement ps = conn.prepareStatement(sorgu);
            ResultSet rs = ps.executeQuery();
            
            String[] bills = new String[5];
            for(int i=0; rs.next() == true; i++) {
                bills[i] = rs.getString("name");
            }
            
            return bills;

        } catch (Exception e) {
            System.out.println(e);
            String[] response = {"false", "Bir hata ile karşılaştık."};
            return response;
        }
    }
    
    public String[][] getPayments(String user_id) {
        String sorgu = "SELECT * FROM bill_payments WHERE user_id = ?";
        
        try {
            PreparedStatement ps = conn.prepareStatement(sorgu);
            ps.setString(1, user_id);
            ResultSet rs = ps.executeQuery();
            
            String[][] payments = new String[100][3];
            for(int i=0; rs.next() == true; i++) {
                String[] row = {rs.getString("bill_name"), rs.getString("amount")};
                payments[i] = row;
            }
            
            return payments;

        } catch (Exception e) {
            System.out.println(e);
            String[][] response = {{"false", "Bir hata ile karşılaştık."}};
            return response;
        }
    }
    
    public String[] payBill(String user_id, String balance, String amount, String bill_name) {
        String sorgu = "UPDATE users SET balance = ? WHERE id = ?";
        balance = balance.replace(',','.');
        amount = amount.replace(',','.');
        
        try {
            double new_balance = Double.parseDouble(balance) - Double.parseDouble(amount);
                
            if(new_balance < 0) {
                String[] response = {"false", "Yetersiz Bakiye!"};
                return response;
            }
            
            PreparedStatement ps1 = conn.prepareStatement(sorgu);
            ps1.setDouble(1, new_balance);
            ps1.setString(2, user_id);
            ps1.execute();
            
            PreparedStatement ps2 = conn.prepareStatement("INSERT INTO bill_payments (user_id, bill_name, amount)"
                    + "VALUES (?, ?, ?)");
            ps2.setString(1, user_id);
            ps2.setString(2, bill_name);
            ps2.setDouble(3, Double.parseDouble(amount));
            ps2.execute();
            
            String[] response = {"true", String.format("%.2f", new_balance)};
            return response;

        } catch (Exception e) {
            System.out.println(e);
            String[] response = {"false", "Bir hata ile karşılaştık."};
            return response;
        }
    }
}
