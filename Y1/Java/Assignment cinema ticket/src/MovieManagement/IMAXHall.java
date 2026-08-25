/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package MovieManagement;

/**
 *
 * @author User
 */
public class IMAXHall extends Hall {

    private String soundSystemBrand;
    private static final double BASE_PRICE = 388.0;

    public IMAXHall(String id, String hallName, String soundSystemBrand) {
        super(id, hallName);
        this.soundSystemBrand = soundSystemBrand;
    }

    public String getSoundSystemBrand() {
        return soundSystemBrand;
    }

    public void setSoundSystemBrand(String soundSystemBrand) {
        this.soundSystemBrand = soundSystemBrand;
    }

    @Override
    public double getBasePrice() {
        return BASE_PRICE;
    }

    @Override
    public double calculatePrice() {
        return BASE_PRICE * 1.2; // 20% extra price
    }

    @Override
    public String toString() {
        return super.toString()
                + ", SoundSystemBrand = " + soundSystemBrand;
    }
}
