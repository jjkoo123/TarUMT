/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package BookingManagement;

import java.io.Serializable;

/**
 *
 * @author User
 */
public class Seat extends Item implements Serializable {

    public Seat() {
        super(generateRandomId("S"));
    }

    public Seat(String id) {
        super(id);
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
