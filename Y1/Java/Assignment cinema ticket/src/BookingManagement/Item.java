/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package BookingManagement;

import java.io.Serializable;
import java.util.*;

/**
 *
 * @author User
 */
public class Item implements Serializable {

    private static final long serialVersionUID = 1L;
    protected String id;
    protected boolean isBooked;

    public Item(String id) {
        this.id = id;
        this.isBooked = false;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isBooked() {
        return isBooked;
    }

    public void setBooked(boolean isBooked) {
        this.isBooked = isBooked;
    }

    @Override
    public String toString() {
        return "Id: " + id + " | Booked: " + isBooked;
    }

    public static String generateRandomId(String prefix) {
        return prefix + "-" + UUID.randomUUID().toString().substring(0, 8);
    }
}
