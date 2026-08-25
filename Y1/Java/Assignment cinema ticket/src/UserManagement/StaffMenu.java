/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package UserManagement;

import java.util.Scanner;
import MovieManagement.*;

/**
 *
 * @author User
 */
public class StaffMenu {

    public static void show(Staff staff) {

        MovieController movieCtrl = new MovieController();
        HallController hallCtrl = new HallController();
        ShowtimeController showtimeController = new ShowtimeController(hallCtrl, movieCtrl);
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println(staff.displayMenu());
            System.out.print("Enter your choice: ");
            while (!scanner.hasNextInt()) {
                System.out.println("Invalid input! Please enter a number between 0 and 3.");
                scanner.next(); // Clear the invalid input
                System.out.print("Enter your choice: ");
            }

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    movieCtrl.run();
                    break;
                case 2:
                    showtimeController.run();
                    break;
                case 3:
                    hallCtrl.run();
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }
}
