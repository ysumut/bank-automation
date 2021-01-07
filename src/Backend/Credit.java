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
public class Credit {
    private Connection conn;
    
    public Credit() {
        conn = (new DBConnection()).connect();
    }
    
    public String[] isApprovedCard(String user_id) {
        String sorgu = "SELECT * FROM credit_cards WHERE user_id = ? AND approved = 1";
        
        try {
            PreparedStatement ps = conn.prepareStatement(sorgu);
            ps.setString(1, user_id);
            ResultSet rs = ps.executeQuery();
            
            if(rs.next()) {
                String[] response = {"true", "approved", String.format("%.2f", rs.getDouble("limit"))};
                return response;
            }
            else {
                String[] response = {"true", "not approved"};
                return response;
            }

        } catch (Exception e) {
            System.out.println(e);
            String[] response = {"false", "Bir hata ile karşılaştık."};
            return response;
        }
    }
    
    public String[] applyCard(String user_id) {
        String sorgu1 = "SELECT * FROM credit_cards WHERE user_id = ?";
        String sorgu2 = "INSERT INTO credit_cards (user_id, limit, approved) VALUES (? ,3500 ,0)";
        
        try {
            PreparedStatement ps1 = conn.prepareStatement(sorgu1);
            ps1.setString(1, user_id);
            ResultSet rs = ps1.executeQuery();
            
            if(rs.next()) {
                String[] response = {"true", "Zaten başvuruda bulundunuz. Lütfen yöneticinin onayını bekleyiniz."};
                return response;
            }
            
            PreparedStatement ps2 = conn.prepareStatement(sorgu2);
            ps2.setString(1, user_id);
            ps2.execute();
            
            String[] response = {"true", "Kredi kartı başvurunuz alınmıştır. Onaylandıktan sonra işlem yapabileceksiniz."};
            return response;

        } catch (Exception e) {
            System.out.println(e);
            String[] response = {"false", "Bir hata ile karşılaştık."};
            return response;
        }
    }
    
    public String[] limitGuncelle(String user_id, String limit) {
        String sorgu = "UPDATE credit_cards SET limit = ? WHERE user_id = ?";
        limit = limit.replace(',','.');
        
        try {
            PreparedStatement ps = conn.prepareStatement(sorgu);
            ps.setString(1, limit);
            ps.setString(2, user_id);
            ps.execute();
            
            String[] response = {"true", "Limit güncellemesi yapılmıştır.", limit};
            return response;

        } catch (Exception e) {
            System.out.println(e);
            String[] response = {"false", "Bir hata ile karşılaştık."};
            return response;
        }
    }
    
    public String[][] getMyCreditApplications(String user_id) {
        String sorgu = "SELECT * FROM credits WHERE user_id = ?";
        
        try {
            PreparedStatement ps = conn.prepareStatement(sorgu);
            ps.setString(1, user_id);
            ResultSet rs = ps.executeQuery();
            
            String[][] credits = new String[100][3];
            for(int i=0; rs.next() == true; i++) {
                String status = "";
                if(rs.getInt("approved") == 1) status = "Onaylandı";
                if(rs.getInt("approved") == 0) status = "Bekliyor";
                if(rs.getInt("approved") == -1) status = "Reddedildi";
                
                String[] row = {rs.getString("amount"), status};
                credits[i] = row;
            }
            
            return credits;

        } catch (Exception e) {
            System.out.println(e);
            String[][] response = {{"false", "Bir hata ile karşılaştık."}};
            return response;
        }
    }
    
    public String[] applyCredit(String user_id, String amount, String type) {
        String sorgu = "INSERT INTO credits (user_id, amount, approved, type, credit_id) VALUES (? ,? ,0, ?, ?)";
        amount = amount.replace(',','.');
        
        try {
            int randomID = (int)(1 + (Math.random() * (99999 - 1)));
            
            PreparedStatement ps = conn.prepareStatement(sorgu);
            ps.setString(1, user_id);
            ps.setDouble(2, Double.parseDouble(amount));
            ps.setString(3, type);
            ps.setInt(4, randomID);
            ps.execute();
            
            String[] response = {"true", type + " başvurunuz alınmıştır. Onaylandıktan sonra hesabınıza yatacaktır."};
            return response;

        } catch (Exception e) {
            System.out.println(e);
            String[] response = {"false", "Bir hata ile karşılaştık."};
            return response;
        }
    }
}
