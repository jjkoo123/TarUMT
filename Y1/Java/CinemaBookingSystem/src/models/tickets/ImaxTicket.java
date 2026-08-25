package models.tickets;

public class ImaxTicket extends CinemaTicket {

    public ImaxTicket(double basePrice) {
        super(basePrice);
    }

    @Override
    public double calculatePrice() {
        return this.basePrice * 1.20; // 20% surcharge for IMAX
    }
}