/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package UserManagement;

import MovieManagement.*;
import BookingManagement.*;
import java.util.Scanner;
import java.util.ArrayList;

/**
 *
 * @author User
 */
public class CustomerMenu {

    public static void show(Customer customer) {

        Scanner scanner = new Scanner(System.in);
        MovieController movieCtrl = new MovieController();
        HallController hallCtrl = new HallController();
        ShowtimeController showtimeController = new ShowtimeController(hallCtrl, movieCtrl);
        BookingController bookCtrl = new BookingController(showtimeController, customer, movieCtrl);
        boolean continueMenu = true;

        while (continueMenu) {
            System.out.println(customer.displayMenu());
            System.out.print("Enter your choice:  ");

            while (!scanner.hasNextInt()) {
                System.out.println("Invalid input! Please enter a number between 0 and 5.");
                scanner.next(); // Clear the invalid input
                System.out.print("Enter your choice: ");
            }

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    movieCtrl.viewMovies();
                    break;
                case 2:
                    showtimeController.viewShowtimes();
                    break;
                case 3:
                    bookCtrl.addBooking();
                    break;
                case 4:
                    bookCtrl.viewTickets();
                    break;
                case 5:
                    CustomerProfile.edit(customer, scanner);
                    if (!customerExist(customer)) {
                        System.out.println("Your account no longer exists. Returning to main menu.");
                        return;
                    }
                    break;
                case 0:
                    continueMenu = false;
                    return;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    private static boolean customerExist(Customer customer) {
        try {
            ArrayList<Customer> customerList = (ArrayList<Customer>) FileManagement.FileManager.loadFromFile("customer.dat");

            for (Customer c : customerList) {
                if (c.getuserID().equals(customer.getuserID())) {
                    return true;
                }
            }

            return false;
        } catch (Exception e) {
            return false;
        }
    }
}
