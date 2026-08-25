package models.tickets;

public class FourDTicket extends CinemaTicket {

    public FourDTicket(double basePrice) {
        super(basePrice);
    }

    @Override
    public double calculatePrice() {
        return this.basePrice * 1.25; // 25% surcharge for 4D
    }
}