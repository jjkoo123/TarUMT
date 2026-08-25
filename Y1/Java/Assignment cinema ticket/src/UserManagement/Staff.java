/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package UserManagement;

/**
 *
 * @author User
 */
public class Staff extends Person {

    private String staffID;

    public Staff(String name, String password, String staffID) {
        super(name, password);
        this.staffID = staffID;
    }

    public String getStaffID() {
        return staffID;
    }

    public void setStaffID(String staffID) {
        this.staffID = staffID;
    }

    @Override
    public String displayMenu() {
        return "\n _____________________________________________________"
                + "\n|                                                     |"
                + "\n|                      Staff Menu                     |"
                + "\n|_____________________________________________________|"
                + "\n|                                                     |"
                + "\n|               [1.] Movie Management                 |"
                + "\n|                                                     |"
                + "\n|               [2.] Showtime Management              |"
                + "\n|                                                     |"
                + "\n|               [3.] Hall Management                  |"
                + "\n|                                                     |"
                + "\n|               [0.] Back                             |"
                + "\n|_____________________________________________________|"
                + "\n|                                                     |"
                + "\n|        Choose with [1.], [2.], [3.] & [0.]          |"
                + "\n|_____________________________________________________|";
    }

}
