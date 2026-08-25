/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package MovieManagement;

/**
 *
 * @author User
 */
import FileManagement.FileManager;
import java.io.IOException;
import java.util.*;

public class HallController {

    private List<Hall> halls;
    private final String FILE_NAME = "halls.dat";
    private Scanner scanner;

    public HallController() {
        halls = new ArrayList<>();
        scanner = new Scanner(System.in);
        try {
            halls = FileManager.loadFromFile(FILE_NAME);

        } catch (Exception e) {
            System.out.println("Failed to load halls. Starting fresh.");
            halls = new ArrayList<>();
        }
    }

    public void addHall() {
        System.out.println("\n============================");
        System.out.println("=        Add New Hall      =");
        System.out.println("============================\n");
        String id = UUID.randomUUID().toString().substring(0, 3);
        System.out.println("Generated Hall ID: " + id);

        System.out.print("Enter hall name: ");
        String hallName = scanner.nextLine();

        System.out.println("Select Hall Type:");
        System.out.println("[1] Normal");
        System.out.println("[2] 3D");
        System.out.println("[3] IMAX");
        System.out.print("Choice: ");

        while (!scanner.hasNextInt()) {
            System.out.println("Invalid input! Please enter a number between 1 and 3.");
            scanner.next(); // Clear the invalid input
            System.out.print("Enter your choice: ");
        }
        int type = scanner.nextInt();
        scanner.nextLine(); // clear buffer

        Hall hall;

        switch (type) {
            case 1: // NormalHall
                String reclinerInput;
                while (true) {
                    System.out.print("Has recliner seats? (yes/no): ");
                    reclinerInput = scanner.nextLine().trim().toLowerCase();
                    if (reclinerInput.equals("yes") || reclinerInput.equals("no")) {
                        break;
                    }
                    System.out.println("Invalid input. Please enter 'yes' or 'no'.");
                }
                boolean hasRecliner = reclinerInput.equals("yes");
                hall = new NormalHall(id, hallName, hasRecliner);
                break;

            case 2: // ThreeDHall
                String glassesInput;
                while (true) {
                    System.out.print("Provides 3D glasses? (yes/no): ");
                    glassesInput = scanner.nextLine().trim().toLowerCase();
                    if (glassesInput.equals("yes") || glassesInput.equals("no")) {
                        break;
                    }
                    System.out.println("Invalid input. Please enter 'yes' or 'no'.");
                }
                boolean providesGlasses = glassesInput.equals("yes");
                hall = new ThreeDHall(id, hallName, providesGlasses);
                break;

            case 3: // IMAXHall
                System.out.print("Enter sound system brand (e.g., Dolby Atmos): ");
                String soundSystem = scanner.nextLine();
                hall = new IMAXHall(id, hallName, soundSystem);
                break;

            default:
                System.out.println("Invalid type selected. Hall added failed...");
                return;
        }

        halls.add(hall);
        saveHalls();
        System.out.println("Hall added successfully: " + hall + "\n");
    }

    public void viewHalls() {
        System.out.println("\n========================");
        System.out.println("=       Hall List      =");
        System.out.println("========================\n");
        if (halls.isEmpty()) {
            System.out.println("No hall available.\n");
            return;
        }
        for (Hall hall : halls) {
            System.out.println(hall);
        }
    }

    public void deleteHall() {
        System.out.println("\n==============================");
        System.out.println("=         Delete Hall        =");
        System.out.println("==============================\n");
        viewHalls();
        System.out.print("Enter the hall ID to delete: ");
        String id = scanner.nextLine();

        Iterator<Hall> iterator = halls.iterator();
        while (iterator.hasNext()) {
            Hall hall = iterator.next();
            if (hall.getId().equals(id)) {
                iterator.remove();
                saveHalls();
                System.out.println("Hall deleted successfully!\n");
                return;
            }
        }
        System.out.println("Hall not found.\n");

    }

    public void saveHalls() {
        try {
            FileManager.saveToFile(FILE_NAME, halls);
        } catch (IOException e) {
            System.out.println("Error saving halls: " + e.getMessage());
        }
    }

    public Hall getHallById(String id) {
        for (Hall h : halls) {
            if (h.getId().equals(id)) {
                return h;
            }
        }
        return null;
    }

    public void run() {
        while (true) {
            System.out.println("\n============================");
            System.out.println("=      Hall Management     =");
            System.out.println("============================\n");
            System.out.println("[1.] Add Hall");
            System.out.println("[2.] View Halls");
            System.out.println("[3.] Delete Hall");
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
                    addHall();
                    break;
                case 2:
                    viewHalls();
                    break;
                case 3:
                    deleteHall();
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
