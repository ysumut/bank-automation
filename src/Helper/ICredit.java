/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Helper;

/**
 *
 * @author Umut
 */
public interface ICredit {
    public String[] isApprovedCard(String user_id);
    public String[] applyCard(String user_id);
    public String[] limitGuncelle(String user_id, String limit);
    public String[][] getMyCreditApplications(String user_id);
    public String[] applyCredit(String user_id, String amount, String type);
}
