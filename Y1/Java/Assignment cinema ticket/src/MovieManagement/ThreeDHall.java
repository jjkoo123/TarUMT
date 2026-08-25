/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package MovieManagement;

/**
 *
 * @author User
 */
public class ThreeDHall extends Hall {

    private boolean provides3DGlasses;
    private static final double BASE_PRICE = 168.00;

    public ThreeDHall(String id, String hallName, boolean provides3DGlasses) {
        super(id, hallName);
        this.provides3DGlasses = provides3DGlasses;
    }

    public boolean isProviding3DGlasses() {
        return provides3DGlasses;
    }

    @Override
    public double calculatePrice() {
        return BASE_PRICE * 1.1 + 20; // extra 10% price, 20 for the glass fee
    }

    @Override
    public double getBasePrice() {
        return BASE_PRICE;
    }

    @Override
    public String toString() {
        return super.toString()
                + ", Provides3DGlasses = " + provides3DGlasses;
    }
}
