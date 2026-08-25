/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package BookingManagement;

import FileManagement.FileManager;
import UserManagement.Customer;
import MovieManagement.*;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 *
 * @author User
 */
public class BookingController {

    private final String FILE_NAME = "booking.dat";
    private final String TICKET_FILE = "ticket.dat";

    private Map<String, SeatLayout> layoutMap = new HashMap<>();
    private List<Booking> bookings = new ArrayList<>();
    private List<Ticket> tickets = new ArrayList<>();
    private Scanner scanner = new Scanner(System.in);
    private ShowtimeController showtimeController;
    private PaymentController paymentController;
    private Customer currentCustomer;
    private MovieController movieController;

    public BookingController(ShowtimeController showtimeController, Customer customer, MovieController movieController) {
        this.showtimeController = showtimeController;
        this.paymentController = new PaymentController();
        this.currentCustomer = customer;
        this.movieController = movieController;

        try {
            bookings = FileManager.loadFromFile(FILE_NAME);

        } catch (Exception e) {
            bookings = new ArrayList<>();
        }

        try {
            tickets = FileManager.loadFromFile(TICKET_FILE);
            for (Ticket ticket : tickets) {
                ticket.setMovieController(movieController);
            }
        } catch (Exception e) {
            tickets = new ArrayList<>();
        }

        for (Showtime s : showtimeController.getShowtimes()) {
            String layoutKey = s.getId() + "_" + s.getHall().getId();
            layoutMap.put(layoutKey, new SeatLayout(s));
        }

    }

    public void addBooking() {
        showtimeController.viewShowtimes();
        Showtime selectedShowtime = null;
        String chosenId = null;

        while (selectedShowtime == null) {
            System.out.print("\nEnter Showtime ID to book (or 0 to cancel): ");
            chosenId = scanner.nextLine().trim();

            if (chosenId.equals("0")) {
                System.out.println("Booking cancelled.");
                return;
            }

            selectedShowtime = showtimeController.getShowtimeById(chosenId);

            if (selectedShowtime == null) {
                System.out.println("Invalid Showtime ID. Please try again.");
            }
        }

        Hall chosenHall = selectedShowtime.getHall();

        String layoutKey = selectedShowtime.getId() + "_" + chosenHall.getId();
        SeatLayout layout = layoutMap.get(layoutKey);

        if (layout == null) {
            layout = new SeatLayout(selectedShowtime);
            layoutMap.put(layoutKey, layout);
        }

        double seatPrice = chosenHall.calculatePrice();

        layout.displayLayout();

        System.out.print("Enter Seat to book (e.g: A1,A2,A3): ");
        String[] seatArray = scanner.nextLine().trim().toUpperCase().split(",");

        Set<String> requestedSeatsSet = new LinkedHashSet<>(Arrays.asList(seatArray));
        List<String> requestedSeats = new ArrayList<>(requestedSeatsSet);

        List<String> unavailableSeats = layout.getUnavailableSeats(requestedSeats);
        if (!unavailableSeats.isEmpty()) {
            System.out.println("Unavailable Seats: " + String.join(", ", unavailableSeats));
            System.out.println("Booking cancelled!");
            return;
        }

        double totalAmount = requestedSeats.size() * seatPrice;

        String hallDetails = getDetailedHallType(chosenHall);

        System.out.println("========================================");
        System.out.println("=           Booking Summary            =");
        System.out.println("========================================");
        System.out.println("Movie       : " + selectedShowtime.getMovie().getTitle());
        System.out.println("Hall        : " + hallDetails);
        System.out.println("Date & Time : " + selectedShowtime.getDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        System.out.println("Seats       : " + String.join(", ", requestedSeats));
        System.out.println("Price/Seat  : RM" + String.format("%.2f", seatPrice));
        System.out.println("Total Amount: RM" + String.format("%.2f", totalAmount));
        System.out.print("Proceed to payment? (yes/no): ");
        String confirm = scanner.nextLine().trim().toLowerCase();

        if (!confirm.equals("yes")) {
            System.out.println("Payment cancelled. Booking discarded.");
            return;
        }

        Payment payment = paymentController.processPayment("PAYMENT", requestedSeats.size(), seatPrice);
        List<String> bookedSeats = layout.bookSeats(requestedSeats);

        if (bookedSeats.isEmpty()) {
            System.out.println("Error: Booking failed. Seats might have just been taken.");
            return;
        }

        System.out.println("\n==================================");
        System.out.println("=       Payment Successful       =");
        System.out.println("==================================");
        System.out.println(payment);

        List<Booking> currentBookingList = new ArrayList<>();
        for (String seatId : bookedSeats) {
            Booking b = new Booking(selectedShowtime, new Seat(seatId));
            bookings.add(b);
            currentBookingList.add(b); 
        }

        Movie selectedMovie = selectedShowtime.getMovie();
        Ticket ticket = new Ticket(currentBookingList.get(0), selectedShowtime, bookedSeats, totalAmount, currentCustomer);

        ticket.setMovieController(movieController);
        System.out.println(ticket);
        tickets.add(ticket);
        saveBookings();
        saveTickets();
    }

    public void saveBookings() {
        try {
            FileManager.saveToFile(FILE_NAME, bookings);
        } catch (IOException e) {
            System.out.println("Error saving bookings: " + e.getMessage());

        }
    }

    public void viewTickets() {

        try {
            tickets = FileManager.loadFromFile(TICKET_FILE);

            for (Ticket ticket : tickets) {
                ticket.setMovieController(movieController);
            }
        } catch (Exception e) {
            tickets = new ArrayList<>();
        }

        boolean found = false;

        System.out.println("\n================================");
        System.out.println("=         Your Tickets         =");
        System.out.println("================================\n");
        for (Ticket t : tickets) {
            if (t.getCustomer().getuserID().equals(currentCustomer.getuserID())) {
                System.out.println(t);
                found = true;
            }
        }
        if (!found) {
            System.out.println("You have no booked tickets yet.");
        }
    }

    public void saveTickets() {
        try {
            FileManager.saveToFile(TICKET_FILE, tickets);
        } catch (IOException e) {
            System.out.println("Error saving tickets: " + e.getMessage());
        }
    }

    public static String getDetailedHallType(Hall hall) {
        String type;

        switch (hall.getClass().getSimpleName()) {
            case "ThreeDHall":
                type = "3D Hall";
                break;
            case "IMAXHall":
                type = "IMAX Hall";
                break;
            case "NormalHall":
                type = "Normal Hall";
                break;
            default:
                type = "Hall";
                break;
        }

        if (hall instanceof ThreeDHall) {
            ThreeDHall threeDHall = (ThreeDHall) hall;
            if (threeDHall.isProviding3DGlasses()) {
                type += " (with glasses)";
            }
        } else if (hall instanceof IMAXHall) {
            IMAXHall imax = (IMAXHall) hall;
            if (imax.getSoundSystemBrand() != null && !imax.getSoundSystemBrand().equalsIgnoreCase("No")) {
                type += " (" + imax.getSoundSystemBrand() + ")";
            }
        } else if (hall instanceof NormalHall) {
            NormalHall normal = (NormalHall) hall;
            if (normal.hasReclinerSeats()) {
                type += " (Recliner)";
            }
        }

        return type;
    }

}
