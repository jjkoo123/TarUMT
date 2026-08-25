/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package MovieManagement;

import java.io.Serializable;

/**
 *
 * @author User
 */
public abstract class Hall implements Serializable {

    private static final long serialVersionUID = 1L;
    private String id;
    private String hallName;

    public Hall() {

    }

    public Hall(String id, String hallName) {
        this.id = id;
        this.hallName = hallName;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHallName() {
        return hallName;
    }

    public void setHallName(String hallName) {
        this.hallName = hallName;
    }

    public abstract double getBasePrice();

    public abstract double calculatePrice();

    @Override
    public String toString() {
        return "Hall Type = " + getClass().getSimpleName()
                + ", id = " + id
                + ", hallName = " + hallName
                + ", Base Price = " + String.format("%.2f", getBasePrice())
                + ", TotalPrice = " + String.format("%.2f", calculatePrice());
    }

}
