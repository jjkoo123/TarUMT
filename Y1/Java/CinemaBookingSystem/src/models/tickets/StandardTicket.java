package models.tickets;

public class StandardTicket extends CinemaTicket {
    
    public StandardTicket(double basePrice) {
        super(basePrice);
    }

    @Override
    public double calculatePrice() {
        return this.basePrice; // Standard tickets have no surcharge
    }
}