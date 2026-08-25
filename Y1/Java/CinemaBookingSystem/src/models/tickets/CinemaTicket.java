package models.tickets;

public abstract class CinemaTicket {
    protected double basePrice;

    public CinemaTicket(double basePrice) {
        this.basePrice = basePrice;
    }

    // Polymorphic method to be overridden by subclasses
    public abstract double calculatePrice();
}