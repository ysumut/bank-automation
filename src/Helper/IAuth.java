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
public interface IAuth {
    public String[] login(String tc_no, String password);
    public String[] register(String[] data);
}
