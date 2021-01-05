/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Backend;

import Helper.DBConnection;
import java.sql.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 *
 * @author Umut
 */
public class Transfer {
    private Connection conn;
    
    public Transfer() {
        conn = (new DBConnection()).connect();
    }
    
    public String[] getCustomerIds(String user_id) {
        String sorgu = "SELECT id FROM users WHERE account_type = 3 AND id != ?";
        
        try {
            PreparedStatement ps = conn.prepareStatement(sorgu);
            ps.setString(1, user_id);
            ResultSet rs = ps.executeQuery();
            
            String[] ids = new String[100];
            for(int i=0; rs.next() == true; i++) {
                ids[i] = rs.getString("id");
            }
            
            return ids;

        } catch (Exception e) {
            System.out.println(e);
            String[] response = {"false", "Bir hata ile karşılaştık."};
            return response;
        }
    }
    
    public String[][] getTransfers(String user_id) {
        String sorgu = "SELECT amount, from_id, to_id, users1.full_name AS from_name, users2.full_name AS to_name FROM money_transfers "
                + "INNER JOIN users AS users1 ON money_transfers.from_id = users1.id "
                + "INNER JOIN users AS users2 ON money_transfers.to_id = users2.id "
                + "WHERE from_id = ? OR to_id != ?";
        
        try {
            PreparedStatement ps = conn.prepareStatement(sorgu);
            ps.setString(1, user_id);
            ps.setString(2, user_id);
            ResultSet rs = ps.executeQuery();
            
            String[][] transfers = new String[100][3];
            for(int i=0; rs.next() == true; i++) {
                String[] row = {rs.getString("from_name"), rs.getString("to_name"), rs.getString("amount")};
                transfers[i] = row;
            }
            
            return transfers;

        } catch (Exception e) {
            System.out.println(e);
            String[][] response = {{"false", "Bir hata ile karşılaştık."}};
            return response;
        }
    }
    
    public String[] paraGonder(String id_from, String balance, String amount, String id_to) {
        String sql_from = "UPDATE users SET balance = ? WHERE id = ?";
        String sql_to = "UPDATE users SET balance = ? WHERE id = ?";
        balance = balance.replace(',','.');
        amount = amount.replace(',','.');
        
        try {
            double balance_from = Double.parseDouble(balance) - Double.parseDouble(amount);
                
            if(balance_from < 0) {
                String[] response = {"false", "Yetersiz Bakiye!"};
                return response;
            }
            
            PreparedStatement ps1 = conn.prepareStatement(sql_from);
            ps1.setDouble(1, balance_from);
            ps1.setString(2, id_from);
            ps1.execute();
            
            PreparedStatement ps2 = conn.prepareStatement("SELECT * FROM users WHERE id = ?");
            ps2.setString(1, id_to);
            ResultSet rs = ps2.executeQuery();
            rs.next();
            
            double balance_to = Double.parseDouble(rs.getString("balance")) + Double.parseDouble(amount);
            
            PreparedStatement ps3 = conn.prepareStatement(sql_to);
            ps3.setDouble(1, balance_to);
            ps3.setString(2, id_to);
            ps3.execute();
            
            PreparedStatement ps4 = conn.prepareStatement("INSERT INTO money_transfers (amount, from_id, to_id)"
                    + "VALUES (?, ?, ?)");
            ps4.setDouble(1, Double.parseDouble(amount));
            ps4.setInt(2, Integer.parseInt(id_from));
            ps4.setInt(3, Integer.parseInt(id_to));
            ps4.execute();
            
            String[] response = {"true", String.format("%.2f", balance_from),
                rs.getString("full_name") + " adlı kişiye para gönderiminiz gerçekleşmiştir."};
            return response;

        } catch (Exception e) {
            System.out.println(e);
            String[] response = {"false", "Bir hata ile karşılaştık."};
            return response;
        }
    }
}
