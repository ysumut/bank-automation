/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Backend;

import Helper.User;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 *
 * @author Umut
 */
public class Password extends User {
    
    @Override
    public String[] guncelleme(String user_id, String mevcut, String yeni, String onay) {
        String sorgu1 = "SELECT * FROM users WHERE id = ?";
        String sorgu2 = "UPDATE users SET password = ? WHERE id = ?";
        
        try {
            if(!yeni.equals(onay)) {
                String[] response = {"false", "Yeni şifre ve onay şifresi farklı girildi."};
                return response;
            }
            
            PreparedStatement ps1 = conn.prepareStatement(sorgu1);
            ps1.setString(1, user_id);
            ResultSet rs = ps1.executeQuery();
            rs.next();
            
            if(!rs.getString("password").equals(mevcut)) {
                String[] response = {"false", "Mevcut şifrenizi yanlış girdiniz."};
                return response;
            }
            
            PreparedStatement ps2 = conn.prepareStatement(sorgu2);
            ps2.setString(1, yeni);
            ps2.setString(2, user_id);
            ps2.execute();
            
            String[] response = {"true", "Şifreniz güncellenmiştir."};
            return response;

        } catch (Exception e) {
            System.out.println(e);
            String[] response = {"false", "Bir hata ile karşılaştık."};
            return response;
        }
    }
}
