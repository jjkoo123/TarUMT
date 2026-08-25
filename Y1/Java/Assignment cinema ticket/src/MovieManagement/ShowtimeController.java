/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package MovieManagement;

import FileManagement.FileManager;

import java.time.*;
import java.util.*;
import java.io.*;

/**
 *
 * @author User
 */
public class ShowtimeController {

    private List<Showtime> showtimes;
    private final String FILE_NAME = "showtimes.dat";
    private Scanner scanner;
    private HallController hallController;
    private MovieController movieController;

    public ShowtimeController(HallController hallController, MovieController movieController) {
        showtimes = new ArrayList<>();
        this.hallController = hallController;
        this.movieController = movieController;
        scanner = new Scanner(System.in);
        try {
            showtimes = FileManager.loadFromFile(FILE_NAME);
            for (Showtime showtime : showtimes) {
                showtime.setMovieController(movieController);
            }

        } catch (Exception e) {
            showtimes = new ArrayList<>();
        }
    }

    public void addShowtime() {
        System.out.println("\n=========================");
        System.out.println("=   Add New Showtime   =");
        System.out.println("=========================\n");

        // Select Hall
        hallController.viewHalls();
        System.out.print("Enter Hall ID: ");
        String hallId = scanner.nextLine();
        Hall hall = hallController.getHallById(hallId);
        if (hall == null) {
            System.out.println("Hall not found.");
            return;
        }

        // Select Movie
        movieController.viewMovies();
        System.out.print("Enter Movie ID: ");
        String movieId = scanner.nextLine();
        Movie movie = movieController.getMovieById(movieId);
        if (movie == null) {
            System.out.println("Movie not found.");
            return;
        }

        // Date Selection 
        List<LocalDate> dates = new ArrayList<>();
        LocalDate today = LocalDate.now();
        for (int i = 0; i <= 3; i++) {
            dates.add(today.plusDays(i));
        }

        System.out.println("\nAvailable Dates:");
        for (int i = 0; i < dates.size(); i++) {
            System.out.println((i + 1) + ". " + dates.get(i));
        }

        System.out.print("Select date (1-" + dates.size() + "): ");

        while (!scanner.hasNextInt()) {
            System.out.println("Invalid choice!");
            scanner.next();
            System.out.print("Enter your choice: ");
        }

        int dateIndex = scanner.nextInt() - 1;
        scanner.nextLine();
        if (dateIndex < 0 || dateIndex >= dates.size()) {
            System.out.println("Invalid date selection.");
            return;
        }

        LocalDate selectedDate = dates.get(dateIndex);
        List<LocalTime> timeSlots = new ArrayList<>();

        // Enter multiple times
        System.out.println("Enter showtimes for " + selectedDate + " (e.g., 13:30). Type 'done' to finish:");
        while (true) {
            System.out.print("Time: ");
            String input = scanner.nextLine();
            if (input.equalsIgnoreCase("done")) {
                break;
            }

            try {
                LocalTime time = LocalTime.parse(input);
                timeSlots.add(time);
            } catch (Exception e) {
                System.out.println("Invalid time format. Use HH:mm (e.g., 14:00).");
            }
        }
        int addedCount = 0;

        for (LocalTime time : timeSlots) {
            LocalDateTime dateTime = LocalDateTime.of(selectedDate, time);

            if (isShowtimeConflict(dateTime, hall)) {
                System.out.println("Showtime at " + time + " already exists in Hall " + hall.getHallName());
                continue; // Skip this conflicting time
            }

            String id = UUID.randomUUID().toString().substring(0, 6);
            Showtime showtime = new Showtime(id, dateTime, hall, movie.getId());

            showtime.setMovieController(movieController);
            showtimes.add(showtime);
            addedCount++;
        }

        if (addedCount > 0) {
            saveShowtimes();
            System.out.println(addedCount + " showtime added successfully.");
        } else {
            System.out.println("No showtimes were added.");
        }
    }

    public void viewShowtimes() {
        System.out.println("\n===========================================================================");
        System.out.println("=                               Showtime List                             =");
        System.out.println("===========================================================================\n");
        if (showtimes.isEmpty()) {
            System.out.println("No showtimes found.\n");
            return;
        }

        for (Showtime showtime : showtimes) {
            showtime.setMovieController(movieController);
        }
        // Group by date
        Map<LocalDate, List<Showtime>> groupedByDate = new TreeMap<>();

        for (Showtime s : showtimes) {
            LocalDate date = s.getDateTime().toLocalDate();
            groupedByDate.computeIfAbsent(date, k -> new ArrayList<>()).add(s);
        }

        for (LocalDate date : groupedByDate.keySet()) {
            System.out.println("\n=================\n=  " + date + "   =\n=================");

            List<Showtime> list = groupedByDate.get(date);
            list.sort(Comparator.comparing(s -> s.getDateTime().toLocalTime()));

            for (Showtime s : list) {
                System.out.println(s);
            }
        }

        System.out.println();
    }

    public void saveShowtimes() {
        try {
            FileManager.saveToFile(FILE_NAME, showtimes);
        } catch (IOException e) {
            System.out.println("Error saving showtimes: " + e.getMessage());
        }
    }

    public void deleteShowtime() {
        System.out.println("\n=============================================== ");
        System.out.println("=               Delete Showtime               =");
        System.out.println("===============================================\n");
        if (showtimes.isEmpty()) {
            System.out.println("No showtimes available.\n");
            return;
        }

        viewShowtimes(); // Show all available showtimes

        System.out.print("Enter Showtime ID to delete: ");
        String idToDelete = scanner.nextLine();

        Iterator<Showtime> iterator = showtimes.iterator();
        while (iterator.hasNext()) {
            Showtime s = iterator.next();
            if (s.getId().equalsIgnoreCase(idToDelete)) {
                iterator.remove();
                saveShowtimes();
                System.out.println("Showtime with ID " + idToDelete + " has been deleted.\n");
                return;
            }
        }

        System.out.println("Showtime with ID " + idToDelete + " not found.\n");
    }

    private boolean isShowtimeConflict(LocalDateTime dateTime, Hall hall) {
        for (Showtime s : showtimes) {
            if (s.getHall().getId().equals(hall.getId())
                    && s.getDateTime().toLocalDate().equals(dateTime.toLocalDate())
                    && s.getDateTime().toLocalTime().equals(dateTime.toLocalTime())) {
                return true; // Conflict found
            }
        }
        return false; // No conflict
    }

    public List<Showtime> getShowtimes() {
        return showtimes;
    }

    public Showtime getShowtimeById(String id) {
        for (Showtime s : showtimes) {
            if (s.getId().equalsIgnoreCase(id)) {
                return s;
            }
        }
        return null;
    }

    public void run() {
        while (true) {
            System.out.println("\n===========================");
            System.out.println("=   Showtime Management   =");
            System.out.println("===========================\n");
            System.out.println("[1.] Add Showtime");
            System.out.println("[2.] View Showtimes");
            System.out.println("[3.] Delete Showtime");
            System.out.println("[0.] Back");
            System.out.print("Choice: ");

            while (!scanner.hasNextInt()) {
                System.out.println("Invalid input! Please enter a number between 0 and 3.");
                scanner.next(); // Clear the invalid input
                System.out.print("Enter your choice: ");
            }

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    addShowtime();
                    break;
                case 2:
                    viewShowtimes();
                    break;
                case 3:
                    deleteShowtime();
                    break;
                case 0:
                    System.out.println("Return to main system...");
                    return;
                default:
                    System.out.println("Invalid choice! Please try again.");
            }
        }
    }

}
