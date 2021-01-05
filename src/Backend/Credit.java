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
    
    public String[] isApproved(String user_id) {
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
    
    public String[] basvur(String user_id) {
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
            
            String[] response = {"true", "Kredi başvurunuz alınmıştır. Onaylandıktan sonra işlem yapabileceksiniz."};
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
}
