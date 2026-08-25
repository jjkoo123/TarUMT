package models.tickets;

public class ThreeDTicket extends CinemaTicket {

    public ThreeDTicket(double basePrice) {
        super(basePrice);
    }

    @Override
    public double calculatePrice() {
        return this.basePrice * 1.15; // 15% surcharge for 3D
    }
}