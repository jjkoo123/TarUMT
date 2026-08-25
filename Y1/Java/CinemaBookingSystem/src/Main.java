import java.util.Scanner;
import models.User;
import services.AuthService;
import services.BookingService;
import services.MovieService;
import utils.ConsoleUI;

public class Main {

    // Scanner for user input
    public static final Scanner scanner = new Scanner(System.in);
    public static final AuthService authService = new AuthService();
    public static final MovieService movieService = new MovieService();
    public static final BookingService bookingService = new BookingService();
    public static final double TICKET_PRICE = 18.00;
    public static int guestCounter = 1;

    public static void main(String[] args) {

        // Load users and existing bookings from file
        authService.loadFromFile();
        bookingService.loadFromFile();
        showWelcomeArt();

        while (true) {
            clearScreen();
            ConsoleUI.headerArt("main");
            ConsoleUI.pageTitle("CINEMA BOOKING SYSTEM");
            ConsoleUI.menuBox("MAIN MENU", new String[] {
                    "  [1] Customer (Guest)",
                    "  [2] Admin Login",
                    "  [3] Exit"
            });
            int choice = readInt("Choose option: ");

            switch (choice) {
                case 1:
                    CustomerController.handleCustomerGuestEntry();
                    break;
                case 2:
                    handleAdminLogin();
                    break;
                case 3:
                    showGoodbyeArt();
                    return;
                default:
                    ConsoleUI.error("Invalid choice.");
                    Main.waitForEnter();
            }
        }
    }

    // ================= ADMIN LOGIN =================
    public static void handleAdminLogin() {
        clearScreen();
        ConsoleUI.headerArt("admin");
        ConsoleUI.sectionTitle("ADMIN LOGIN");
        String username = readString("Enter username: ");
        String password = readString("Enter password: ");

        User user = authService.login(username, password);

        if (user != null && "ADMIN".equalsIgnoreCase(user.getRole())) {
            ConsoleUI.success("Login successful!");
            AdminController.showAdminMenu(user);
        } else {
            ConsoleUI.error("Invalid admin login!");
        }
    }


    public static String readString(String prompt) {
        while (true) {
            ConsoleUI.prompt(prompt);
            String input = scanner.nextLine().trim();
            if (!input.isEmpty()) {
                return input;
            }
            ConsoleUI.error("Input cannot be empty. Please try again.");
        }
    }

    
    public static double readDouble(String prompt) {
        while (true) {
            ConsoleUI.prompt(prompt);
            String input = scanner.nextLine().trim();
            if (input.isEmpty()) {
                ConsoleUI.error("Input cannot be empty. Please enter a number.");
                continue;
            }
            try {
                return Double.parseDouble(input);
            } catch (NumberFormatException e) {
                ConsoleUI.error("Please enter a valid number.");
            }
        }
    }

    public static int readInt(String prompt) {
        while (true) {
            ConsoleUI.prompt(prompt);
            String input = scanner.nextLine().trim();
            if (input.isEmpty()) {
                ConsoleUI.error("Input cannot be empty. Please enter a number.");
                continue;
            }
            if (!input.matches("\\d+")) {
                ConsoleUI.error("Please enter a valid number.");
                continue;
            }
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                ConsoleUI.error("Number is out of range.");
            }
        }
    }

    public static void waitForEnter() {
        ConsoleUI.prompt("Press Enter to continue...");
        scanner.nextLine();
    }

    public static boolean confirmAction(String promptText) {
        while (true) {
            ConsoleUI.prompt(promptText);
            String input = scanner.nextLine().trim().toUpperCase();
            if ("Y".equals(input) || "YES".equals(input)) {
                return true;
            }
            if ("N".equals(input) || "NO".equals(input)) {
                return false;
            }
            ConsoleUI.error("Please enter Y or N.");
        }
    }

    public static void showWelcomeArt() {
        clearScreen();
        System.out.println("   _____ _                              ____              _    _               ");
        System.out.println("  / ____(_)                            |  _ \\            | |  (_)              ");
        System.out.println(" | |     _ _ __   ___ _ __ ___   __ _  | |_) | ___   ___ | | ___ _ __   __ _   ");
        System.out.println(" | |    | | '_ \\ / _ \\ '_ ` _ \\ / _` | |  _ < / _ \\ / _ \\| |/ / | '_ \\ / _` |  ");
        System.out.println(" | |____| | | | |  __/ | | | | | (_| | | |_) | (_) | (_) |   <| | | | | (_| |  ");
        System.out.println("  \\_____|_|_| |_|\\___|_| |_| |_|\\__,_| |____/ \\___/ \\___/|_|\\_\\_|_| |_|\\__, |  ");
        System.out.println("                                                                        __/ |  ");
        System.out.println("                                                                       |____/   ");
        ConsoleUI.info("Welcome to Cinema Booking System");
        waitForEnter();
    }

    public static void showGoodbyeArt() {
        clearScreen();
        System.out.println("  +--------------------------------------------------------+");
        System.out.println("  |                     SEE YOU AGAIN                      |");
        System.out.println("  +--------------------------------------------------------+");
        System.out.println("                 _________");
        System.out.println("                / ======= \\");
        System.out.println("               / __________\\");
        System.out.println("              | ___________ |");
        System.out.println("              | | -       | |");
        System.out.println("              | |         | |");
        System.out.println("              | |_________| |________________________");
        System.out.println("              \\=____________/   Thanks for booking!  )");
        System.out.println("              / \"\"\"\"\"\"\"\\                         /");
        System.out.println("             / ::::::::::::: \\                    =D-'");
        System.out.println("            (_________________)");
        ConsoleUI.success("Goodbye!");
    }

    public static void clearScreen() {
        try {
            if (System.getProperty("os.name").toLowerCase().contains("windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch (Exception e) {
            System.out.print("\033[H\033[2J");
            System.out.flush();
        }
    }
}
