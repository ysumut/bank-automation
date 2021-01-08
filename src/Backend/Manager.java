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
public class Manager extends User {
    
    public String[] getCustomerIDs() {
        String sorgu = "SELECT id FROM users WHERE approved = 0";
        
        try {
            PreparedStatement ps = conn.prepareStatement(sorgu);
            ResultSet rs = ps.executeQuery();
            
            String[] usersIds = new String[100];
            for(int i=0; rs.next() == true; i++) {
                usersIds[i] = rs.getString("id");
            }
            
            return usersIds;

        } catch (Exception e) {
            System.out.println(e);
            String[] response = {"false", "Bir hata ile karşılaştık."};
            return response;
        }
    }
    
    public String[][] getCustomers() {
        String sorgu = "SELECT * FROM users WHERE approved = 0";
        
        try {
            PreparedStatement ps = conn.prepareStatement(sorgu);
            ResultSet rs = ps.executeQuery();
            
            String[][] users = new String[100][3];
            for(int i=0; rs.next() == true; i++) {
                String[] row = {rs.getString("id"), rs.getString("full_name"), rs.getString("email"),
                    rs.getString("tc_no"), rs.getString("address")};
                users[i] = row;
            }
            
            return users;

        } catch (Exception e) {
            System.out.println(e);
            String[][] response = {{"false", "Bir hata ile karşılaştık."}};
            return response;
        }
    }
    
    public String[] musteriOnayla(String user_id) {
        String sorgu = "UPDATE users SET approved = 1 WHERE id = ?";
        
        try {
            PreparedStatement ps = conn.prepareStatement(sorgu);
            ps.setString(1, user_id);
            ps.execute();
            
            String[] response = {"true", "Kullanıcı onaylandı."};
            return response;

        } catch (Exception e) {
            System.out.println(e);
            String[] response = {"false", "Bir hata ile karşılaştık."};
            return response;
        }
    }
    
    public String[] getCreditCardUserIDs() {
        String sorgu = "SELECT user_id FROM credit_cards WHERE approved = 0";
        
        try {
            PreparedStatement ps = conn.prepareStatement(sorgu);
            ResultSet rs = ps.executeQuery();
            
            String[] applicationIDs = new String[100];
            for(int i=0; rs.next() == true; i++) {
                applicationIDs[i] = rs.getString("user_id");
            }
            
            return applicationIDs;

        } catch (Exception e) {
            System.out.println(e);
            String[] response = {"false", "Bir hata ile karşılaştık."};
            return response;
        }
    }
    
    public String[][] getCreditCards() {
        String sorgu = "SELECT user_id, users.full_name AS user_name FROM credit_cards "
                + "INNER JOIN users ON credit_cards.user_id = users.id "
                + "WHERE credit_cards.approved = 0";
        
        try {
            PreparedStatement ps = conn.prepareStatement(sorgu);
            ResultSet rs = ps.executeQuery();
            
            String[][] credit_cards = new String[100][3];
            for(int i=0; rs.next() == true; i++) {
                String[] row = {rs.getString("user_id"), rs.getString("user_name")};
                credit_cards[i] = row;
            }
            
            return credit_cards;

        } catch (Exception e) {
            System.out.println(e);
            String[][] response = {{"false", "Bir hata ile karşılaştık."}};
            return response;
        }
    }
    
    public String[] applyCreditCard(String user_id) {
        String sorgu = "UPDATE credit_cards SET approved = 1 WHERE user_id = ?";
        
        try {
            PreparedStatement ps = conn.prepareStatement(sorgu);
            ps.setString(1, user_id);
            ps.execute();
            
            String[] response = {"true", "Kredi kartı onaylandı."};
            return response;

        } catch (Exception e) {
            System.out.println(e);
            String[] response = {"false", "Bir hata ile karşılaştık."};
            return response;
        }
    }
    
    public String[][] getPersonnels() {
        String sorgu = "SELECT * FROM users WHERE account_type = 2";
        
        try {
            PreparedStatement ps = conn.prepareStatement(sorgu);
            ResultSet rs = ps.executeQuery();
            
            String[][] personnels = new String[100][3];
            for(int i=0; rs.next() == true; i++) {
                String[] row = {rs.getString("id"), rs.getString("full_name"), rs.getString("email"),
                    rs.getString("tc_no"), rs.getString("address")};
                personnels[i] = row;
            }
            
            return personnels;

        } catch (Exception e) {
            System.out.println(e);
            String[][] response = {{"false", "Bir hata ile karşılaştık."}};
            return response;
        }
    }
    
    public String[] addPersonnel(String[] data) {
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
                + " VALUES (?, ?, ?, ?, 2, 0, ?, ?, 1)";
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
            
            String[] response = {"true", "Personel eklenmiştir."};
            return response;

        } catch (Exception e) {
            System.out.println(e);
            String[] response = {"false", "Bir hata ile karşılaştık."};
            return response;
        }
    }
    
    public String[] getCreditIDs() {
        String sorgu = "SELECT credit_id FROM credits WHERE approved = 0";
        
        try {
            PreparedStatement ps = conn.prepareStatement(sorgu);
            ResultSet rs = ps.executeQuery();
            
            String[] applicationIDs = new String[100];
            for(int i=0; rs.next() == true; i++) {
                applicationIDs[i] = rs.getString("credit_id");
            }
            
            return applicationIDs;

        } catch (Exception e) {
            System.out.println(e);
            String[] response = {"false", "Bir hata ile karşılaştık."};
            return response;
        }
    }
    
    public String[][] getCredits() {
        String sorgu = "SELECT user_id, amount, type, credit_id, users.full_name FROM credits "
                + "INNER JOIN users ON credits.user_id = users.id "
                + "WHERE credits.approved = 0";
        
        try {
            PreparedStatement ps = conn.prepareStatement(sorgu);
            ResultSet rs = ps.executeQuery();
            
            String[][] credits = new String[100][3];
            for(int i=0; rs.next() == true; i++) {
                String[] row = {rs.getString("credit_id"), rs.getString("full_name"), rs.getString("amount"), rs.getString("type")};
                credits[i] = row;
            }
            
            return credits;

        } catch (Exception e) {
            System.out.println(e);
            String[][] response = {{"false", "Bir hata ile karşılaştık."}};
            return response;
        }
    }
    
    public String[] applyOrRejectCredit(String credit_id, int approved) {
        String sorgu1 = "UPDATE credits SET approved = ? WHERE credit_id = ?";
        String sorgu2 = "SELECT user_id, credit_id, amount, balance FROM credits "
                + "INNER JOIN users ON credits.user_id = users.id "
                + "WHERE credit_id = ?";
        
        try {
            PreparedStatement ps1 = conn.prepareStatement(sorgu1);
            ps1.setInt(1, approved);
            ps1.setString(2, credit_id);
            ps1.execute();
            
            if(approved == 1) {
                PreparedStatement ps2 = conn.prepareStatement(sorgu2);
                ps2.setString(1, credit_id);
                ResultSet rs = ps2.executeQuery();
                rs.next();
                
                double new_balance = rs.getDouble("balance") + rs.getDouble("amount");
                
                PreparedStatement ps3 = conn.prepareStatement("UPDATE users SET balance = ? WHERE id = ?");
                ps3.setDouble(1, new_balance);
                ps3.setInt(2, rs.getInt("user_id"));
                ps3.execute();
                
                String[] response = {"true", "Kredi başvurusu onaylandı."};
                return response;
            }
            else {
                String[] response = {"true", "Kredi başvurusu reddedildi."};
                return response;
            }

        } catch (Exception e) {
            System.out.println(e);
            String[] response = {"false", "Bir hata ile karşılaştık."};
            return response;
        }
    }
}
