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
public class Auth {
    private Connection conn;
    
    public Auth() {
        conn = (new DBConnection()).connect();
    }
    
    public String[] login(String email, String password) {
        String sorgu = "SELECT * FROM users WHERE tc_no=? AND password=?";
        
        try {
            PreparedStatement ps = conn.prepareStatement(sorgu);
            ps.setString(1, email);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                if(rs.getInt("approved") == 0) {
                    String[] response = {"false", "Hesabınız onaylanmadı. Onaylandıktan sonra giriş yapabilirsiniz."};
                    return response;
                }
                
                String[] response = {"true", rs.getString("id"), rs.getString("full_name"), 
                    String.format("%.2f", rs.getDouble("balance")), rs.getString("account_type")};
                return response;
            } 
            else {
                String[] response = {"false", "TC No veya şifre hatalı."};
                return response;
            }

        } catch (Exception e) {
            System.out.println(e);
            String[] response = {"false", "Bir hata ile karşılaştık."};
            return response;
        }
    }
    
    public String[] register(String[] data) {
        boolean control = true;
        for(int i=0; i < data.length; i++) {
            if(data[i].equals("")) control = false;
        }
        
        if(!control) {
            String[] response = {"false", "Lütfen boş kısımları doldurunuz."};
            return response;
        }
        
        String sorgu1 = "SELECT * FROM users WHERE email = ? OR tc_no = ?";
        String sorgu2 = "INSERT INTO users (id, full_name, email, password, account_type, balance, tc_no, address, approved)"
                + " VALUES (?, ?, ?, ?, 3, 0, ?, ?, 0)";
        try {
            PreparedStatement ps1 = conn.prepareStatement(sorgu1);
            ps1.setString(1, data[1]);
            ps1.setString(2, data[3]);
            ResultSet rs = ps1.executeQuery();
            
            if(rs.next()) {
                String[] response = {"false", "Girdiğiniz email veya TC no kullanılıyor."};
                return response;
            }
            
            int randomID = (int)(1 + (Math.random() * (99999 - 1)));
            
            PreparedStatement ps2 = conn.prepareStatement(sorgu2);
            ps2.setInt(1, randomID);
            ps2.setString(2, data[0]);
            ps2.setString(3, data[1]);
            ps2.setString(4, data[2]);
            ps2.setString(5, data[3]);
            ps2.setString(6, data[4]);
            ps2.execute();
            
            String[] response = {"true", "Kayıt başarılı! Hesabınız onaylandıktan sonra giriş yapabileceksiniz."};
            return response;

        } catch (Exception e) {
            System.out.println(e);
            String[] response = {"false", "Bir hata ile karşılaştık."};
            return response;
        }
    }
}
