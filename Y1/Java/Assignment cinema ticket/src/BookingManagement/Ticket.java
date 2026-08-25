package BookingManagement;

import java.io.Serializable;
import java.time.format.DateTimeFormatter;
import java.util.List;
import MovieManagement.*;
import UserManagement.*;

public class Ticket extends Item implements Serializable {

    private Booking booking;
    private Movie movie;
    private String movieId;
    private String movieTitle;
    private Hall hall;
    private Showtime showtime;
    private List<String> seats;
    private double totalAmount;
    private Customer customer;

    private transient MovieController movieController;

    public Ticket(Booking booking, Showtime showtime, List<String> seats, double totalAmount, Customer customer) {
        super(generateRandomId("T"));
        this.booking = booking;
        this.showtime = showtime;
        this.seats = seats;
        this.totalAmount = totalAmount;
        this.customer = customer;


        if (showtime != null && showtime.getMovie() != null) {
            this.movieId = showtime.getMovie().getId();
            this.movieTitle = showtime.getMovie().getTitle(); 
        } else {
            this.movieId = "UNKNOWN";
            this.movieTitle = "Unknown Movie";
        }

        if (showtime != null) {
            this.hall = showtime.getHall();
        }
    }

    public Ticket(Booking booking, Showtime showtime, List<String> seats, double totalAmount, Customer customer, String movieId) {
        super(generateRandomId("T"));
        this.booking = booking;
        this.showtime = showtime;
        this.seats = seats;
        this.totalAmount = totalAmount;
        this.customer = customer;
        this.movieId = movieId;
        this.movieTitle = "Unknown Movie"; 


        if (showtime != null && showtime.getMovie() != null && showtime.getMovie().getId().equals(movieId)) {
            this.movieTitle = showtime.getMovie().getTitle();
        }

        if (showtime != null) {
            this.hall = showtime.getHall();
        }
    }

    public Booking getBooking() {
        return booking;
    }

    public Hall getHall() {
        return hall;
    }

    public Showtime getShowtime() {
        return showtime;
    }

    public List<String> getSeats() {
        return seats;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public Customer getCustomer() {
        return customer;
    }

    public String getMovieId() {
        return movieId;
    }

    public Movie getMovie() {

        if (movieController != null) {
            Movie movie = movieController.getMovieById(movieId);
            if (movie != null) {
                return movie;
            }
        }


        if (showtime != null && showtime.getMovie() != null
                && showtime.getMovie().getId().equals(movieId)) {
            return showtime.getMovie();
        }

        return null;
    }

    public String getMovieTitle() {
        if (movieController != null) {
            Movie movie = movieController.getMovieById(movieId);
            if (movie != null) {
                return movie.getTitle();
            }
        }

        if (showtime != null && showtime.getMovie() != null
                && showtime.getMovie().getId().equals(movieId)) {
            return showtime.getMovie().getTitle();
        }

        return movieTitle != null ? movieTitle : "Unknown Movie";
    }

    public void setMovieController(MovieController movieController) {
        this.movieController = movieController;
        if (movieController != null) {
            Movie movie = movieController.getMovieById(movieId);
            if (movie != null) {
                this.movieTitle = movie.getTitle();
            }
        }
    }

    @Override
    public String toString() {

        return "\n============ Ticket ============"
                + "\nTicket ID   : " + getId()
                + "\nBooking ID  : " + booking.getId()
                + "\nMovie       : " + getMovieTitle()
                + "\nHall Name   : " + hall.getHallName()
                + "\nHall Type   : " + BookingController.getDetailedHallType(showtime.getHall())
                + "\nDate & Time : " + showtime.getDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
                + "\nSeats       : " + String.join(", ", seats)
                + "\nTotal       : RM" + String.format("%.2f", totalAmount)
                + "\nCustomer    : " + customer.getName()
                + "\n================================\n";
    }
}
