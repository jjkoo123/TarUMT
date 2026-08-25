import java.util.ArrayList;
import java.util.List;
import models.Booking;
import models.Movie;
import models.User;
import utils.ConsoleUI;
import utils.SeatLayout;

public class AdminController {
    public static void showAdminMenu(User user) {
        while (true) {
            Main.clearScreen();
            ConsoleUI.headerArt("admin");
            ConsoleUI.pageTitle("ADMIN MENU");
            ConsoleUI.info("Welcome, " + user.getUsername());
            ConsoleUI.menuBox("ADMIN ACTIONS", new String[] {
                    "  [1] Manage Movies & Showtimes",
                    "  [2] View All Bookings",
                    "  [3] Revenue Dashboard & Statistics",
                    "  [4] Review Ticket Receipts",
                    "  [5] Logout"
            });
            int choice = Main.readInt("Choose option: ");

            switch (choice) {
                case 1:
                    manageMoviesMenu();
                    break;
                case 2:
                    showAllBookings();
                    break;
                case 3:
                    showRevenueDashboard();
                    break;
                case 4:
                    generateAllTicketReceipts();
                    break;
                case 5:
                    return;
                default:
                    ConsoleUI.error("Invalid choice.");
                    Main.waitForEnter();
            }
        }
    }

    public static void manageMoviesMenu() {
        while (true) {
            Main.clearScreen();
            ConsoleUI.headerArt("movie");
            ConsoleUI.pageTitle("MANAGE MOVIES & SHOWTIMES");
            ConsoleUI.menuBox("MOVIE ACTIONS", new String[] {
                    "  [1] View Movies & Showtimes",
                    "  [2] Add Movie",
                    "  [3] Manage Specific Movie (Add Types/Times, Delete)",
                    "  [0] Back to Main Menu"
            });
            int choice = Main.readInt("Choose option: ");

            switch (choice) {
                case 1:
                    displayMoviesWithShowtimes();
                    break;
                case 2:
                    handleAddMovie();
                    break;
                case 3:
                    manageSingleMovieFlow();
                    break;
                case 0:
                    return;
                default:
                    ConsoleUI.error("Invalid choice.");
                    Main.waitForEnter();
            }
        }
    }

    public static void manageSingleMovieFlow() {
        Movie movie = selectMovie();
        if (movie == null) return;

        while (true) {
            var allMovies = Main.movieService.getMovies();
            boolean found = false;
            for (Movie m : allMovies) {
                if (m.getTitle().equals(movie.getTitle())) {
                    movie = m;
                    found = true;
                    break;
                }
            }
            if (!found) {
                ConsoleUI.error("Movie no longer exists!");
                Main.waitForEnter();
                return;
            }

            Main.clearScreen();
            ConsoleUI.headerArt("movie");
            ConsoleUI.pageTitle("MANAGE: " + movie.getTitle());
            ConsoleUI.keyValueAligned("Genre", movie.getGenre());
            ConsoleUI.keyValueAligned("Duration", movie.getDurationMins() + " mins");
            ConsoleUI.keyValueAligned("Cinema Types", movie.getCinemaType());
            ConsoleUI.keyValueAligned("Showtimes", String.join(", ", movie.getShowTimes()));
            ConsoleUI.divider();

            ConsoleUI.menuBox("MOVIE OPTIONS", new String[] {
                    "  [1] Add Cinema Types",
                    "  [2] Add Showtime",
                    "  [3] Remove Showtime",
                    "  [4] Delete Movie entirely",
                    "  [0] Back"
            });

            int choice = Main.readInt("Choose option: ");

            switch (choice) {
                case 1:
                    handleAddCinemaType(movie);
                    break;
                case 2:
                    handleAddShowtimeToMovie(movie);
                    break;
                case 3:
                    handleRemoveShowtimeFromMovie(movie);
                    break;
                case 4:
                    if (Main.movieService.removeMovie(movie.getTitle())) {
                        int removedBookings = Main.bookingService.removeBookingsForMovie(movie.getTitle());
                        ConsoleUI.success("Movie '" + movie.getTitle() + "' removed completely!");
                        if (removedBookings > 0) {
                            ConsoleUI.info("Removed " + removedBookings + " active booking(s) for this movie.");
                        }
                        Main.waitForEnter();
                        return;
                    } else {
                        ConsoleUI.error("Failed to remove movie.");
                        Main.waitForEnter();
                    }
                    break;
                case 0:
                    return;
                default:
                    ConsoleUI.error("Invalid choice.");
                    Main.waitForEnter();
            }
        }
    }

    public static void handleAddCinemaType(Movie movie) {
        List<String> cinemaTypesList = new ArrayList<>();
        while (true) {
            Main.clearScreen();
            ConsoleUI.headerArt("movie");
            ConsoleUI.pageTitle("ADD CINEMA TYPE TO " + movie.getTitle());
            ConsoleUI.menuBox("Select Cinema Type", new String[] { 
                "1. Standard", "2. 3D", "3. 4D", "4. IMAX", "5. All" 
            });
            String choiceInput = Main.readString("Enter choice(s) separated by comma (1-5): ");
            List<String> rawChoices = CustomerController.parseCommaSeparatedValues(choiceInput);
            cinemaTypesList.clear();
            
            boolean hasError = false;
            for (String c : rawChoices) {
                if ("5".equals(c)) {
                    cinemaTypesList.clear();
                    cinemaTypesList.add("Standard");
                    cinemaTypesList.add("3D");
                    cinemaTypesList.add("4D");
                    cinemaTypesList.add("IMAX");
                    break;
                } else if ("1".equals(c)) {
                    if (!cinemaTypesList.contains("Standard")) cinemaTypesList.add("Standard");
                } else if ("2".equals(c)) {
                    if (!cinemaTypesList.contains("3D")) cinemaTypesList.add("3D");
                } else if ("3".equals(c)) {
                    if (!cinemaTypesList.contains("4D")) cinemaTypesList.add("4D");
                } else if ("4".equals(c)) {
                    if (!cinemaTypesList.contains("IMAX")) cinemaTypesList.add("IMAX");
                } else {
                    hasError = true;
                    break;
                }
            }
            
            if (hasError || cinemaTypesList.isEmpty()) {
                ConsoleUI.error("Invalid cinema type selection. Please enter numbers between 1-5 separated by commas.");
                Main.waitForEnter();
            } else {
                break;
            }
        }
        
        boolean success = Main.movieService.addCinemaType(movie.getTitle(), String.join(",", cinemaTypesList));
        if (success) {
            ConsoleUI.success("Cinema Types added successfully.");
        } else {
            ConsoleUI.error("Failed to add cinema types.");
        }
        Main.waitForEnter();
    }

    public static void handleAddShowtimeToMovie(Movie movie) {
        while (true) {
            Main.clearScreen();
            ConsoleUI.headerArt("time");
            ConsoleUI.pageTitle("ADD SHOWTIME TO " + movie.getTitle());
            String time = Main.readString("Session Time (e.g. 14:00, 2:00 PM) or 'cancel': ");
            if (time.equalsIgnoreCase("cancel")) return;
            
            String converted = convertTimeFormat(time);
            if (converted == null) {
                ConsoleUI.error("Invalid time format. Please follow e.g. 14:00 or 2:00 PM.");
                Main.waitForEnter();
            } else {
                boolean success = Main.movieService.addShowTime(movie.getTitle(), converted);
                if (success) {
                    ConsoleUI.success("Added showtime " + converted + " to " + movie.getTitle());
                } else {
                    ConsoleUI.error("Failed to add that showtime.");
                }
                break;
            }
        }
        Main.waitForEnter();
    }

    public static void handleRemoveShowtimeFromMovie(Movie movie) {
        var times = movie.getShowTimes();
        if (times.isEmpty()) {
            ConsoleUI.info("This movie has no showtimes to remove.");
            Main.waitForEnter();
            return;
        }

        while (true) {
            Main.clearScreen();
            ConsoleUI.headerArt("time");
            ConsoleUI.pageTitle("REMOVE SHOWTIME FROM " + movie.getTitle());
            for (int i = 0; i < times.size(); i++) {
                System.out.println((i + 1) + ". " + times.get(i));
            }
            
            int choice = Main.readInt("Select showtime index to remove (0 to cancel): ");
            if (choice == 0) return;
            
            if (choice > 0 && choice <= times.size()) {
                String toRemove = times.get(choice - 1);
                boolean success = Main.movieService.removeShowTime(movie.getTitle(), toRemove);
                if (success) {
                    int removedBookings = Main.bookingService.removeBookingsForShowtime(movie.getTitle(), toRemove);
                    ConsoleUI.success("Removed showtime " + toRemove + " from " + movie.getTitle());
                    if (removedBookings > 0) {
                        ConsoleUI.info("Also removed " + removedBookings + " active booking(s) for this showtime.");
                    }
                } else {
                    ConsoleUI.error("Failed to remove showtime.");
                }
                break;
            } else {
                ConsoleUI.error("Invalid choice. Please pick between 1 and " + times.size() + ".");
                Main.waitForEnter();
            }
        }
        Main.waitForEnter();
    }
    
    public static void showAllBookings() {
        var bookings = Main.bookingService.getAllBookings();
    
            if (bookings.isEmpty()) {
                ConsoleUI.info("No bookings found.");
                Main.waitForEnter();
                return;
            }
    
            Main.clearScreen();
            ConsoleUI.headerArt("ticket");
            ConsoleUI.pageTitle("ALL BOOKINGS");
            String[] headers = { "No", "User", "Movie", "Type", "Time", "Seat", "Payment", "Price" };
            List<String[]> rows = new ArrayList<>();
            for (int i = 0; i < bookings.size(); i++) {
                Booking b = bookings.get(i);
                String seatStr = String.format("%c%d", (char) ('A' + b.getRow()), b.getCol() + 1);
                
                String cinemaType = b.getCinemaType();
    
                rows.add(new String[] {
                        String.valueOf(i + 1),
                        b.getUsername(),
                        b.getMovieTitle(),
                        cinemaType,
                        b.getShowTime(),
                        seatStr,
                        b.getPaymentMethod(),
                        String.format("Rm%.2f", b.getPrice())
                });
            }
            ConsoleUI.tableBox("ALL BOOKINGS", headers, rows);
            Main.waitForEnter();
        }
    
        

    public static void generateAllTicketReceipts() {
        Main.clearScreen();
        ConsoleUI.headerArt("admin");
        ConsoleUI.pageTitle("REVIEW TICKET RECEIPTS");

        var bookings = Main.bookingService.getAllBookings();
        if (bookings.isEmpty()) {
            ConsoleUI.info("No bookings found.");
        } else {
            for (models.Booking b : bookings) {
                ConsoleUI.spacer();
                System.out.println(ConsoleUI.ANSI_CYAN + "============================================================" + ConsoleUI.ANSI_RESET);
                System.out.println(ConsoleUI.ANSI_YELLOW + "                       CINEMA TICKET                        " + ConsoleUI.ANSI_RESET);
                System.out.println(ConsoleUI.ANSI_CYAN + "============================================================" + ConsoleUI.ANSI_RESET);
                System.out.println("  Customer : " + b.getUsername());
                System.out.println("  Movie    : " + b.getMovieTitle());
                System.out.println("  Type     : " + b.getCinemaType());
                System.out.println("  Time     : " + b.getShowTime());
                System.out.println("  Seat     : " + String.format("%c%d", (char) ('A' + b.getRow()), b.getCol() + 1));
                System.out.println("  Payment  : " + b.getPaymentMethod());
                System.out.println("  Total    : Rm" + String.format("%.2f", b.getPrice()));
                System.out.println(ConsoleUI.ANSI_CYAN + "============================================================" + ConsoleUI.ANSI_RESET);
            }
            ConsoleUI.spacer();
            ConsoleUI.success("Successfully displayed " + bookings.size() + " ticket receipts.");
        }
        Main.waitForEnter();
    }

    public static void showRevenueDashboard() {
            Main.clearScreen();
            ConsoleUI.headerArt("admin");
            ConsoleUI.pageTitle("REVENUE DASHBOARD");
    
            var bookings = Main.bookingService.getAllBookings();
    
            if (bookings.isEmpty()) {
                ConsoleUI.info("System has no recorded bookings yet.");
                Main.waitForEnter();
                return;
            }
    
            double totalRevenue = 0;
            int totalTicketsSold = bookings.size();
    
            // Calculate revenue and find top movie
            java.util.Map<String, Integer> movieTicketCounts = new java.util.HashMap<>();
            java.util.Map<String, Double> movieRevenue = new java.util.HashMap<>();
            java.util.Map<String, Integer> cinemaTypeCounts = new java.util.HashMap<>();
            java.util.Map<String, Double> cinemaTypeRevenue = new java.util.HashMap<>();
            java.util.Map<String, Double> paymentMethodTotals = new java.util.HashMap<>();
    
            for (Booking b : bookings) {
                double price = b.getPrice();
                totalRevenue += price;
    
                String title = b.getMovieTitle();
                movieTicketCounts.put(title, movieTicketCounts.getOrDefault(title, 0) + 1);
                movieRevenue.put(title, movieRevenue.getOrDefault(title, 0.0) + price);
    
                String cinemaType = b.getCinemaType();
                cinemaTypeCounts.put(cinemaType, cinemaTypeCounts.getOrDefault(cinemaType, 0) + 1);
                cinemaTypeRevenue.put(cinemaType, cinemaTypeRevenue.getOrDefault(cinemaType, 0.0) + price);
    
                String payMethod = b.getPaymentMethod();
                paymentMethodTotals.put(payMethod, paymentMethodTotals.getOrDefault(payMethod, 0.0) + price);
            }
    
            String topMovie = "None";
            int topSales = 0;
            for (java.util.Map.Entry<String, Integer> entry : movieTicketCounts.entrySet()) {
                if (entry.getValue() > topSales) {
                    topSales = entry.getValue();
                    topMovie = entry.getKey();
                }
            }
    
            // Display Summary
            ConsoleUI.divider();
            ConsoleUI.keyValueAligned("Total Tickets Sold", String.valueOf(totalTicketsSold));
            ConsoleUI.keyValueAligned("Total Revenue Generated", String.format("Rm%.2f", totalRevenue));
            ConsoleUI.divider();
            ConsoleUI.keyValueAligned("Best Selling Movie", topMovie + " (" + topSales + " tickets)");
            
            ConsoleUI.divider();
            ConsoleUI.sectionTitle("Revenue by Movie");
            for (String movie : movieTicketCounts.keySet()) {
                ConsoleUI.keyValueAligned(movie, String.format("%d tickets | Rm%.2f", movieTicketCounts.get(movie), movieRevenue.get(movie)));
            }
    
            ConsoleUI.divider();
            ConsoleUI.sectionTitle("Revenue by Cinema Type");
            for (String ctype : cinemaTypeCounts.keySet()) {
                ConsoleUI.keyValueAligned(ctype, String.format("%d tickets | Rm%.2f", cinemaTypeCounts.get(ctype), cinemaTypeRevenue.get(ctype)));
            }
    
            ConsoleUI.divider();
            ConsoleUI.sectionTitle("Revenue by Payment Method");
            for (java.util.Map.Entry<String, Double> entry : paymentMethodTotals.entrySet()) {
                ConsoleUI.keyValueAligned("Via " + entry.getKey(), String.format("Rm%.2f", entry.getValue()));
            }
    
            ConsoleUI.divider();
            Main.waitForEnter();
        }
    
        public static void displayMoviesWithShowtimes() {
            while (true) {
                Movie movie = selectMovie();
                if (movie == null) {
                    return; // Go back to admin menu
                }

                while (true) {
                    String cinemaType = CustomerController.selectCinemaTypePrompt(movie);
                    if (cinemaType == null) {
                        break; // Go back to movie selection
                    }

                    while (true) {
                        String selectedTime = selectTime(movie, cinemaType);
                        if (selectedTime == null) {
                            break; // Go back to cinema type selection
                        }

                        // Display Seat Layout
                        Main.clearScreen();
                        ConsoleUI.headerArt("movie");
                        ConsoleUI.pageTitle("SEAT LAYOUT - " + movie.getTitle().toUpperCase());
                        ConsoleUI.keyValueAligned("Cinema Type", cinemaType);
                        ConsoleUI.keyValueAligned("Showtime", selectedTime);
                        ConsoleUI.divider();

                        utils.SeatLayout layout = Main.bookingService.getSeatLayout(movie.getTitle(), cinemaType, selectedTime);
                        layout.displaySeats();
                        Main.waitForEnter();
                    }
                }
            }
        }
    
        public static void handleAddMovie() {
            ConsoleUI.sectionTitle("ADD MOVIE");
            String title = Main.readString("Enter movie title: ");
            String genre = Main.readString("Enter genre (e.g. Action / Sci-Fi): ");
            
            List<String> cinemaTypesList = new ArrayList<>();
            while (true) {
                ConsoleUI.menuBox("Select Cinema Type", new String[] { 
                    "1. Standard", 
                    "2. 3D", 
                    "3. 4D", 
                    "4. IMAX",
                    "5. All" 
                });
                String choiceInput = Main.readString("Enter choice(s) separated by comma (1-5): ");
                List<String> rawChoices = CustomerController.parseCommaSeparatedValues(choiceInput);
                cinemaTypesList.clear();
                
                boolean hasError = false;
                for (String c : rawChoices) {
                    if ("5".equals(c)) {
                        cinemaTypesList.clear();
                        cinemaTypesList.add("Standard");
                        cinemaTypesList.add("3D");
                        cinemaTypesList.add("4D");
                        cinemaTypesList.add("IMAX");
                        break;
                    } else if ("1".equals(c) && !cinemaTypesList.contains("Standard")) {
                        cinemaTypesList.add("Standard");
                    } else if ("2".equals(c) && !cinemaTypesList.contains("3D")) {
                        cinemaTypesList.add("3D");
                    } else if ("3".equals(c) && !cinemaTypesList.contains("4D")) {
                        cinemaTypesList.add("4D");
                    } else if ("4".equals(c) && !cinemaTypesList.contains("IMAX")) {
                        cinemaTypesList.add("IMAX");
                    } else {
                        hasError = true;
                        break;
                    }
                }
                
                if (hasError || cinemaTypesList.isEmpty()) {
                    ConsoleUI.error("Invalid cinema type selection. Please enter numbers between 1-5 separated by commas.");
                    Main.waitForEnter();
                } else {
                    break;
                }
            }
            String cinemaType = String.join(", ", cinemaTypesList);
    
            int duration = Main.readInt("Enter duration in minutes: ");
            
            List<String> showTimes;
            while (true) {
                String rawTimes = Main.readString("Enter showtimes separated by comma (e.g. 14:00, 2:00 PM): ");
                List<String> parsedTimes = CustomerController.parseCommaSeparatedValues(rawTimes);
                showTimes = new ArrayList<>();
                
                boolean allValid = true;
                for (String st : parsedTimes) {
                    String converted = convertTimeFormat(st);
                    if (converted == null) {
                        allValid = false;
                        ConsoleUI.error("Invalid time format: '" + st + "'. Please use a format like '14:00' or '2:00 PM'.");
                        break;
                    }
                    showTimes.add(converted);
                }
                
                if (showTimes.isEmpty()) {
                    ConsoleUI.error("You must enter at least one showtime.");
                } else if (allValid) {
                    break;
                }
            }
    
            double price = Main.readDouble("Enter base ticket price for this movie (e.g. 18.0): ");
        if (Main.movieService.addMovie(title, genre, cinemaType, duration, showTimes, price)) {
                ConsoleUI.success("Movie added successfully.");
            } else {
                ConsoleUI.error("Failed to add movie. Check title/showtimes or duplicate title.");
            }
            Main.waitForEnter();
        }
    
        public static void handleRemoveMovie() {
            Movie movie = selectMovie();
            if (movie == null) {
                return;
            }
            String title = movie.getTitle();
    
            if (!Main.confirmAction("Are you sure you want to remove movie '" + title + "'? (Y/N): ")) {
                ConsoleUI.info("Removal cancelled.");
                Main.waitForEnter();
                return;
            }
    
            if (Main.movieService.removeMovie(title)) {
                int removedBookings = Main.bookingService.removeBookingsForMovie(title);
                ConsoleUI.success("Movie removed successfully.");
                if (removedBookings > 0) {
                    ConsoleUI.info("Removed " + removedBookings + " booking record(s) linked to this movie.");
                }
            } else {
                ConsoleUI.warning("Movie not found.");
            }
            Main.waitForEnter();
        }
    
        public static void handleAddShowtime() {
            ConsoleUI.sectionTitle("ADD SHOWTIME");
            Movie movie = selectMovie();
            if (movie == null) return;
            String title = movie.getTitle();
            
            String showTime;
            while(true) {
                String input = Main.readString("Enter new showtime (e.g. 14:00 or 8:00 PM): ");
                showTime = convertTimeFormat(input);
                if (showTime == null) {
                    ConsoleUI.error("Invalid time format. Please use a format like '14:00' or '8:00 PM'.");
                } else {
                    break;
                }
            }
    
            if (Main.movieService.addShowTime(title, showTime)) {
                ConsoleUI.success("Showtime added successfully.");
            } else {
                ConsoleUI.error("Failed to add showtime. Check movie title or duplicate time.");
            }
            Main.waitForEnter();
        }
    
        public static void handleRemoveShowtime() {
            ConsoleUI.sectionTitle("REMOVE SHOWTIME");
            Movie movie = selectMovie();
            if (movie == null) return;
            String title = movie.getTitle();
            
            String showTime = Main.readString("Enter showtime to remove: ");
    
            if (!Main.confirmAction("Are you sure you want to remove this showtime? (Y/N): ")) {
                ConsoleUI.info("Removal cancelled.");
                Main.waitForEnter();
                return;
            }
    
            if (Main.movieService.removeShowTime(title, showTime)) {
                System.out.print("Enter Cinema Type to delete: ");
                String cinemaType = Main.scanner.nextLine();
                int removedBookings = Main.bookingService.removeBookingsForSession(title, cinemaType, showTime);
                ConsoleUI.success("Showtime removed successfully.");
                if (removedBookings > 0) {
                    ConsoleUI.info("Removed " + removedBookings + " booking record(s) linked to this showtime.");
                }
            } else {
                ConsoleUI.error("Failed to remove showtime. Ensure movie/time exists and at least one showtime remains.");
            }
            Main.waitForEnter();
        }
    
        public static Movie selectMovie() {
            var allMovies = Main.movieService.getMovies();
            if (allMovies.isEmpty()) {
                ConsoleUI.info("No movies available.");
                Main.waitForEnter();
                return null;
            }
    
            Main.clearScreen();
            ConsoleUI.headerArt("movie");
            ConsoleUI.pageTitle("SELECT MOVIE");
    
            String[] headers = { "No", "Movie", "Genre", "Duration", "Showtimes" };
            List<String[]> rows = new ArrayList<>();
            rows.add(new String[] { "0", "Back", "-", "-", "-" });
            for (int i = 0; i < allMovies.size(); i++) {
                Movie m = allMovies.get(i);
                rows.add(new String[] {
                        String.valueOf(i + 1),
                        m.getTitle(),
                        m.getGenre(),
                        m.getDurationMins() + " mins",
                        String.join(", ", m.getShowTimes())
                });
            }
            ConsoleUI.tableBox("AVAILABLE MOVIES", headers, rows);
    
            int movieChoice = Main.readInt("Choose movie (0 to go back): ");
            if (movieChoice == 0) {
                return null;
            }
            
            if (movieChoice > 0 && movieChoice <= allMovies.size()) {
                return allMovies.get(movieChoice - 1);
            }
            
            ConsoleUI.error("Invalid movie choice.");
            Main.waitForEnter();
            return selectMovie();
        }
    
        public static String selectTime(Movie movie, String cinemaType) {
            var showTimes = movie.getShowTimes();
            Main.clearScreen();
            ConsoleUI.headerArt("time");
            ConsoleUI.pageTitle("SELECT TIME");
    
            ConsoleUI.keyValueAligned("Movie", movie.getTitle());
            ConsoleUI.keyValueAligned("Genre", movie.getGenre());
            ConsoleUI.keyValueAligned("Type", cinemaType);
            ConsoleUI.keyValueAligned("Ticket Price", String.format("Rm%.2f", CustomerController.calculateTicketPrice(movie.getPrice(), cinemaType)));
            ConsoleUI.keyValueAligned("Duration", movie.getDurationMins() + " mins");
            ConsoleUI.divider();
    
            String[] headers = { "No", "Showtime", "Availability", "Status" };
            List<String[]> rows = new ArrayList<>();
            rows.add(new String[] { "0", "Back", "-", "-" });
    
            for (int i = 0; i < showTimes.size(); i++) {
                String startTime = showTimes.get(i);
                String endTime = CustomerController.calculateEndTime(startTime, movie.getDurationMins());
    
                SeatLayout layout = Main.bookingService.getSeatLayout(movie.getTitle(), cinemaType, startTime);
                int available = layout.getAvailableSeats();
                int total = layout.getTotalSeats();
    
                String seatStatus = String.format("%d/%d seats", available, total);
                String tag = "";
                if (available == 0) {
                    tag = "[SOLD OUT]";
                } else if (available <= 5) {
                    tag = "[Selling Fast!]";
                } else {
                    tag = "Available";
                }
    
                rows.add(new String[] {
                        String.valueOf(i + 1),
                        CustomerController.normalizeTime(startTime) + " - " + endTime,
                        seatStatus,
                        tag
                });
            }
            ConsoleUI.tableBox("AVAILABLE SHOWTIMES", headers, rows);
            int timeChoice = Main.readInt("Choose time: ");
            if (timeChoice == 0) {
                return null;
            }
            
            String selectedTime = Main.movieService.getShowTimeByChoice(movie, timeChoice);
            if (selectedTime != null) {
                return selectedTime;
            }
            
            ConsoleUI.error("Invalid time selection. Please choose a valid number from the list.");
            Main.waitForEnter();
            return selectTime(movie, cinemaType);
        }

    public static String convertTimeFormat(String input) {
        if (input == null) return null;
        input = input.trim();
        // 12 hour check
        if (input.matches("^(1[0-2]|0?[1-9]):[0-5][0-9]\\s[aApP][mM]$")) {
            return input.toUpperCase();
        }
        // 24 hour check
        if (input.matches("^([01]?[0-9]|2[0-3]):[0-5][0-9]$")) {
            try {
                java.time.LocalTime time = java.time.LocalTime.parse(input, java.time.format.DateTimeFormatter.ofPattern("H:mm"));
                return time.format(java.time.format.DateTimeFormatter.ofPattern("h:mm a")).toUpperCase();
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }
}
