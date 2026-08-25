/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package MovieManagement;

/**
 *
 * @author User
 */
public class NormalHall extends Hall {

    private boolean hasReclinerSeats;
    private static final double BASE_PRICE = 38.00;

    public NormalHall(String id, String hallName, boolean hasReclinerSeats) {
        super(id, hallName);
        this.hasReclinerSeats = hasReclinerSeats;
    }

    public boolean hasReclinerSeats() {
        return hasReclinerSeats;
    }

    @Override
    public double calculatePrice() {
        return BASE_PRICE * 0.9; //discout 10% 
    }

    @Override
    public double getBasePrice() {
        return BASE_PRICE;
    }

    @Override
    public String toString() {
        return super.toString()
                + ", HasReclinerSeats = " + hasReclinerSeats;
    }
}
