import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import models.Booking;
import models.Movie;
import models.User;
import services.TicketService;
import utils.ConsoleUI;
import utils.SeatLayout;

public class CustomerController {
    public static void handleCustomerGuestEntry() {
            Main.clearScreen();
            ConsoleUI.headerArt("customer");
            ConsoleUI.sectionTitle("CUSTOMER ENTRY");
            ConsoleUI.prompt("Enter your name (or press Enter for Guest): ");
            String name = Main.scanner.nextLine().trim();
    
            if (name.isEmpty()) {
                name = generateGuestName();
            }
    
            User customer = new models.Customer(name, "");
            ConsoleUI.success("Welcome, " + customer.getUsername() + "!");
            showCustomerMenu(customer);
        }
    
        public static void showCustomerMenu(User user) {
    
            while (true) {
                Main.clearScreen();
                ConsoleUI.headerArt("customer");
                ConsoleUI.pageTitle("CUSTOMER MENU");
                ConsoleUI.info("Customer: " + user.getUsername());
                ConsoleUI.menuBox("CUSTOMER ACTIONS", new String[] {
                        "  [1] Book Movie Ticket",
                        "  [2] View My Bookings",
                        "  [3] Cancel a Booking",
                        "  [4] Logout"
                });
                int choice = Main.readInt("Choose option: ");
    
                switch (choice) {
                    case 1:
                        movieSelectionFlow(user);
                        break;
    
                    case 2:
                        showBookingHistory(user);
                        break;
    
                    case 3:
                        handleCancelBooking(user);
                        break;
    
                    case 4:
                        ConsoleUI.info("Customer logged out.");
                        return;
    
                    default:
                        ConsoleUI.error("Invalid choice.");
                        Main.waitForEnter();
                }
            }
        }
    
        public static void movieSelectionFlow(User user) {
            Movie movie = AdminController.selectMovie();
            if (movie == null) {
                ConsoleUI.info("Booking cancelled.");
                return;
            }
    
            String cinemaType = selectCinemaTypePrompt(movie);
            if (cinemaType == null) {
                ConsoleUI.info("Booking cancelled.");
                return;
            }
    
            String selectedTime = AdminController.selectTime(movie, cinemaType);
            if (selectedTime == null) {
                ConsoleUI.info("Booking cancelled.");
                return;
            }
    
            // Proceed to booking
            bookSeatFlow(user, movie, selectedTime, cinemaType);
        }
    
        public static void bookSeatFlow(User user, Movie movie, String time, String cinemaType) {
            Main.clearScreen();
            ConsoleUI.headerArt("ticket");
            ConsoleUI.pageTitle("BOOK SEAT");
            ConsoleUI.keyValueAligned("Movie", movie.getTitle());
            ConsoleUI.keyValueAligned("Type", cinemaType);
            ConsoleUI.keyValueAligned("Time", time);
            ConsoleUI.divider();
    
            SeatLayout seatLayout = Main.bookingService.getSeatLayout(movie.getTitle(), cinemaType, time);
            seatLayout.displaySeats();
    
            List<int[]> selectedSeats = new ArrayList<>();
            String seatsFormatted = "";

            while (true) {
                String input = Main.readString("Enter seat(s) separated by comma (e.g. A3, A4) or '0' to cancel: ").toUpperCase();
                if ("0".equals(input.trim())) {
                    ConsoleUI.info("Booking cancelled.");
                    Main.waitForEnter();
                    return;
                }
                
                selectedSeats.clear();
                boolean allValid = true;
                List<String> formattedNames = new ArrayList<>();
                
                String[] parts = input.split(",");
                for (String part : parts) {
                    String seatChoice = part.trim();
                    if (seatChoice.length() >= 2) {
                        char rowChar = seatChoice.charAt(0);
                        try {
                            int colNum = Integer.parseInt(seatChoice.substring(1));
                            int r = rowChar - 'A';
                            int c = colNum - 1;
                            
                            if (seatLayout.isValidSeat(r, c)) {
                                if (seatLayout.isSeatAvailable(r, c)) {
                                    selectedSeats.add(new int[]{r, c});
                                    formattedNames.add(String.format("%c%d", rowChar, colNum));
                                } else {
                                    ConsoleUI.error("Seat " + seatChoice + " is already booked!");
                                    allValid = false;
                                    break;
                                }
                            } else {
                                ConsoleUI.error("Invalid seat: " + seatChoice + ". Not on layout.");
                                allValid = false;
                                break;
                            }
                        } catch (NumberFormatException e) {
                            ConsoleUI.error("Invalid seat format: " + seatChoice);
                            allValid = false;
                            break;
                        }
                    } else {
                        ConsoleUI.error("Invalid seat format: " + seatChoice);
                        allValid = false;
                        break;
                    }
                }
                
                if (allValid && !selectedSeats.isEmpty()) {
                    seatsFormatted = String.join(", ", formattedNames);
                    break;
                } else if (allValid) {
                    ConsoleUI.error("Please enter at least one seat.");
                }
            }
    
            double unitPrice = calculateTicketPrice(movie.getPrice(), cinemaType);
            double totalPrice = unitPrice * selectedSeats.size();
    
            String paymentMethod = choosePaymentMethod(movie, time, seatsFormatted, totalPrice, cinemaType);
            if (paymentMethod == null) {
                ConsoleUI.info("Payment cancelled.");
                Main.waitForEnter();
                return;
            }
            
            ConsoleUI.spacer();
            ConsoleUI.showLoading("Processing Payment...");

            int successCount = 0;
            for (int[] pos : selectedSeats) {
                Booking booking = Main.bookingService.createBooking(user, movie, cinemaType, time, pos[0], pos[1], paymentMethod, unitPrice);
                if (booking != null) {
                    TicketService.printTicket(movie.getTitle(), time, pos[0], pos[1], paymentMethod, unitPrice, user.getUsername(), cinemaType);
                    successCount++;
                } else {
                    ConsoleUI.warning("Seat " + String.format("%c%d", (char)('A' + pos[0]), pos[1] + 1) + " was occupied!");
                }
            }

            ConsoleUI.success("Successfully booked " + successCount + " seat(s)!");
            Main.waitForEnter();
        }
    
        public static void showBookingHistory(User user) {
            var bookings = Main.bookingService.getBookingsForUser(user.getUsername());
    
            if (bookings.isEmpty()) {
                ConsoleUI.info("No bookings found.");
                Main.waitForEnter();
                return;
            }
    
            Main.clearScreen();
            ConsoleUI.headerArt("ticket");
            ConsoleUI.pageTitle("MY BOOKINGS");
            String[] headers = { "No", "Movie", "Type", "Time", "Seat", "Payment", "Price" };
            List<String[]> rows = new ArrayList<>();
            for (int i = 0; i < bookings.size(); i++) {
                Booking b = bookings.get(i);
                String seatStr = String.format("%c%d", (char) ('A' + b.getRow()), b.getCol() + 1);
                
                String cinemaType = b.getCinemaType();
    
                rows.add(new String[] {
                        String.valueOf(i + 1),
                        b.getMovieTitle(),
                        cinemaType,
                        b.getShowTime(),
                        seatStr,
                        b.getPaymentMethod(),
                        String.format("Rm%.2f", b.getPrice())
                });
            }
            ConsoleUI.tableBox("MY BOOKINGS", headers, rows);
            Main.waitForEnter();
        }
    
        public static void handleCancelBooking(User user) {
            while (true) {
                var bookings = Main.bookingService.getBookingsForUser(user.getUsername());
                if (bookings.isEmpty()) {
                    ConsoleUI.info("You have no bookings to cancel.");
                    Main.waitForEnter();
                    return;
                }
        
                Main.clearScreen();
                ConsoleUI.headerArt("ticket");
                ConsoleUI.pageTitle("CANCEL BOOKING");
        
                String[] headers = { "No", "Movie", "Type", "Time", "Seat", "Price" };
                List<String[]> rows = new ArrayList<>();
                rows.add(new String[] { "0", "Back", "-", "-", "-", "-" });
                for (int i = 0; i < bookings.size(); i++) {
                    Booking b = bookings.get(i);
                    String seatStr = String.format("%c%d", (char) ('A' + b.getRow()), b.getCol() + 1);
                    
                    String cinemaType = b.getCinemaType();
        
                    rows.add(new String[] {
                            String.valueOf(i + 1),
                            b.getMovieTitle(),
                            cinemaType,
                            b.getShowTime(),
                            seatStr,
                            String.format("Rm%.2f", b.getPrice())
                    });
                }
                ConsoleUI.tableBox("SELECT BOOKING TO CANCEL", headers, rows);
        
                int choice = Main.readInt("Enter the booking number to cancel (0 to go back): ");
                if (choice == 0)
                    return;
        
                if (choice > 0 && choice <= bookings.size()) {
                    Booking b = bookings.get(choice - 1);
                    ConsoleUI.warning("Are you sure you want to cancel your booking for " + b.getMovieTitle() + "?");
                    String confirm = Main.readString("Type 'YES' to confirm: ");
                    if (confirm.equalsIgnoreCase("YES")) {
                        ConsoleUI.showLoading("Processing Refund...");
                        if (Main.bookingService.cancelBooking(b)) {
                            ConsoleUI.success("Booking cancelled successfully. Rm" + String.format("%.2f", b.getPrice())
                                    + " has been refunded.");
                        } else {
                            ConsoleUI.error("Failed to cancel booking.");
                        }
                    } else {
                        ConsoleUI.info("Cancellation aborted.");
                    }
                } else {
                    ConsoleUI.error("Invalid booking number.");
                }
                Main.waitForEnter();
            }
        }
    
        public static String selectCinemaTypePrompt(models.Movie movie) {
        String rawTypes = movie.getCinemaType();
        String[] types = rawTypes.split(",");
        List<String> validTypes = new ArrayList<>();
        for (String t : types) {
            String trimmed = t.trim();
            if (!trimmed.isEmpty()) {
                validTypes.add(trimmed);
            }
        }
        
        while (true) {
            Main.clearScreen();
            ConsoleUI.headerArt("movie");
            ConsoleUI.pageTitle("SELECT CINEMA TYPE");
            
            String[] menuOptions = new String[validTypes.size() + 1];
            for (int i = 0; i < validTypes.size(); i++) {
                menuOptions[i] = (i + 1) + ". " + validTypes.get(i);
            }
            menuOptions[validTypes.size()] = "0. Cancel";
            
            ConsoleUI.menuBox("Available Types for " + movie.getTitle(), menuOptions);
            
            int maxIdx = validTypes.size();
            int typeChoice = Main.readInt("Enter choice (0-" + maxIdx + "): ");
            
            if (typeChoice == 0) {
                return null;
            } else if (typeChoice > 0 && typeChoice <= maxIdx) {
                return validTypes.get(typeChoice - 1);
            } else {
                ConsoleUI.error("Invalid choice. Please enter a number between 0 and " + maxIdx + ".");
                Main.waitForEnter();
            }
        }
    }
    
        public static double calculateTicketPrice(double basePrice, String cinemaType) {
            models.tickets.CinemaTicket ticket;

            if ("3D".equalsIgnoreCase(cinemaType)) {
                ticket = new models.tickets.ThreeDTicket(basePrice);
            } else if ("4D".equalsIgnoreCase(cinemaType)) {
                ticket = new models.tickets.FourDTicket(basePrice);
            } else if ("IMAX".equalsIgnoreCase(cinemaType)) {
                ticket = new models.tickets.ImaxTicket(basePrice);
            } else {
                ticket = new models.tickets.StandardTicket(basePrice);
            }

            // Demonstrates polymorphism through calculatePrice()
            return ticket.calculatePrice();
        }
    
        public static String normalizeTime(String startTime) {
            try {
                String normalizedTime = startTime.toUpperCase(java.util.Locale.US).replaceAll("([AP]M)", " $1").replaceAll("\\s+", " ").trim();
                if (normalizedTime.startsWith("0")) {
                    normalizedTime = normalizedTime.substring(1);
                }
                return normalizedTime;
            } catch (Exception e) {
                return startTime; // fallback
            }
        }
        
        public static String calculateEndTime(String startTime, int durationMins) {
            try {
                String normalizedTime = normalizeTime(startTime);
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("h:mm a", Locale.US);
                LocalTime start = LocalTime.parse(normalizedTime, dtf);
                LocalTime end = start.plusMinutes(durationMins);
                return end.format(dtf);
            } catch (Exception e) {
                // Fallback if the time format is non-standard
                return "???";
            }
        }
    
        public static String choosePaymentMethod(Movie movie, String time, String seatsFormatted, double totalPrice, String cinemaType) {
            String[] methods = { "Credit Card", "E-Wallet", "Cash" };
    
            Main.clearScreen();
            ConsoleUI.headerArt("payment");
            ConsoleUI.pageTitle("PAYMENT SUMMARY & CONFIRMATION");
    
            ConsoleUI.keyValueAligned("Movie", movie.getTitle());
            ConsoleUI.keyValueAligned("Genre", movie.getGenre());
            ConsoleUI.keyValueAligned("Type", cinemaType);
            ConsoleUI.keyValueAligned("Showtime", time + " to " + calculateEndTime(time, movie.getDurationMins()));
            ConsoleUI.keyValueAligned("Seats", seatsFormatted);
            ConsoleUI.keyValueAligned("Total Price", String.format("Rm%.2f", totalPrice));
            ConsoleUI.divider();
    
            String[] options = new String[methods.length + 1];
            options[0] = "  [0] Cancel Booking & Go Back";
            for (int i = 0; i < methods.length; i++) {
                options[i + 1] = "  [" + (i + 1) + "] Pay with " + methods[i];
            }
            ConsoleUI.menuBox("CHOOSE PAYMENT METHOD", options);
    
            while (true) {
                int choice = Main.readInt("Choice: ");
                if (choice == 0) {
                    return null;
                }
                if (choice >= 1 && choice <= methods.length) {
                    return methods[choice - 1];
                }
                ConsoleUI.error("Invalid payment option. Try again.");
            }
        }
    
        public static String generateGuestName() {
            String datePart = LocalDate.now().toString().replace("-", "");
            String id = String.format("%03d", Main.guestCounter++);
            return "Guest-" + datePart + "-" + id;
        }
    
        public static boolean containsIgnoreCase(List<String> values, String target) {
            for (String value : values) {
                if (value.equalsIgnoreCase(target)) {
                    return true;
                }
            }
            return false;
        }
    
        public static List<String> parseCommaSeparatedValues(String input) {
            List<String> values = new ArrayList<>();
            if (input == null || input.isBlank()) {
                return values;
            }
    
            String[] tokens = input.split(",");
            for (String token : tokens) {
                String clean = token.trim();
                if (!clean.isEmpty() && !containsIgnoreCase(values, clean)) {
                    values.add(clean);
                }
            }
            return values;
        }
}
