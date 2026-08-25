package BookingManagement;

import MovieManagement.*;
import java.io.Serializable;

/**
 *
 * @author User
 */
public class Booking extends Item implements Serializable {

    private Showtime showtime;
    private Seat seat;

    public Booking() {
        super(generateRandomId("B"));
    }

    public Booking(Showtime showtime, Seat seat) {
        super(generateRandomId("B"));
        this.showtime = showtime;
        this.seat = seat;
    }

    public Showtime getShowtime() {
        return showtime;
    }

    public Seat getSeat() {
        return seat;
    }

    @Override
    public String toString() {
        return "\n========= Booking ========="
                + "\nBooking ID  : " + getId()
                + "\nMovie       : " + showtime.getMovie().getTitle()
                + "\nSeat        : " + seat
                + "\nHall Name   : " + showtime.getHall().getHallName()
                + "\nHall Type   : " + showtime.getHall().getClass().getSimpleName()
                + "\nDate & Time : " + showtime.getDateTime()
                + "\n===========================\n";
    }
}
