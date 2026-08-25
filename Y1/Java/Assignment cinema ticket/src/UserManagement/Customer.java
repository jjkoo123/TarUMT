/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package UserManagement;

import java.io.Serializable;

/**
 *
 * @author User
 */
public class Customer extends Person  {

    private static final long serialVersionUID = 1L;
    private int telNo;
    private String userID;
    private String email;

    public Customer(String name, String password, int telNo, String userID, String email) {
        super(name, password);
        this.telNo = telNo;
        this.userID = userID;
        this.email = email;
    }

    public int getTelNo() {
        return telNo;
    }

    public String getuserID() {
        return userID;
    }

    public String getEmail() {
        return email;
    }

    public void setTelNo(int telNo) {
        this.telNo = telNo;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override

    public String displayMenu() {
        return  "\n _____________________________________________________"
                + "\n|                                                     |"
                + "\n|                     Customer Menu                   |"
                + "\n|_____________________________________________________|"
                + "\n|                                                     |"
                + "\n|                 [1.] Movie Listing                  |"
                + "\n|                                                     |"
                + "\n|                 [2.] View Showtime                  |"
                + "\n|                                                     |"
                + "\n|                 [3.] Make Booking                   |"
                + "\n|                                                     |"
                + "\n|                 [4.] View My Tickets                |"
                + "\n|                                                     |"
                + "\n|                 [5.] Edit Profile                   |"
                + "\n|                                                     |"
                + "\n|                 [0.] Back                           |"
                + "\n|_____________________________________________________|"
                + "\n|                                                     |"
                + "\n|    Choose with [1.], [2.], [3.], [4.],[5.] & [0.]   |"
                + "\n|_____________________________________________________|";

    }
}
