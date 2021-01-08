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
public interface IBills {
    public String[] getBills(String user_id);
    public String[][] getPayments(String user_id);
    public String[] payBill(String user_id, String balance, String amount, String bill_name);
}
