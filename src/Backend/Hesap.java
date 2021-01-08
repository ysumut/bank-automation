/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Backend;

import Helper.User;
import java.sql.PreparedStatement;

/**
 *
 * @author Umut
 */
public class Hesap extends User {
    
    // Para Çekme
    public String[] paraIslemi(String id, String balance, String amount) {
        String sorgu = "UPDATE users SET balance = ? WHERE id = ?";
        balance = balance.replace(',','.');
        amount = amount.replace(',','.');
        
        try {
            double new_balance = Double.parseDouble(balance) - Double.parseDouble(amount);
                
            if(new_balance < 0) {
                String[] response = {"false", "Yetersiz Bakiye!"};
                return response;
            }
            
            PreparedStatement ps = conn.prepareStatement(sorgu);
            ps.setDouble(1, new_balance);
            ps.setString(2, id);
            ps.execute();
            
            String[] response = {"true", String.format("%.2f", new_balance)};
            return response;

        } catch (Exception e) {
            System.out.println(e);
            String[] response = {"false", "Bir hata ile karşılaştık."};
            return response;
        }
    }
    
    // Para Yatırma
    public String[] paraIslemi(int id, String balance, String amount) {
        String sorgu = "UPDATE users SET balance = ? WHERE id = ?";
        balance = balance.replace(',','.');
        amount = amount.replace(',','.');
        
        try {
            double new_balance = Double.parseDouble(balance) + Double.parseDouble(amount);
            
            PreparedStatement ps = conn.prepareStatement(sorgu);
            ps.setDouble(1, new_balance);
            ps.setInt(2, id);
            ps.execute();
            
            String[] response = {"true", String.format("%.2f", new_balance)};
            return response;

        } catch (Exception e) {
            System.out.println(e);
            String[] response = {"false", "Bir hata ile karşılaştık."};
            return response;
        }
    }
}
