/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package UserManagement;

import java.util.Scanner;

/**
 *
 * @author User
 */
public class MainMenu {

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        int choice;
        do {

            displayMenu();

            while (!scan.hasNextInt()) {
                System.out.println("Invalid input! Please enter a number between 0 and 3.");
                scan.next(); // Clear invalid input
                displayMenu();    // Restart loop
            }

            choice = scan.nextInt();
            scan.nextLine();

            Staff staff = null;
            Customer customer = null;

            switch (choice) {
                case 1:
                    System.out.println("\n          |------------------------|");
                    System.out.println("          |     Customer Login     |");
                    System.out.println("          |------------------------|\n");
                    customer = Login.cust(scan);
                    break;
                case 2:
                    System.out.println("\n           |------------------------|");
                    System.out.println("           |      Staff Login       |");
                    System.out.println("           |------------------------|\n");
                    staff = Login.staff(scan);
                    break;
                case 3:
                    System.out.println("           |------------------------|");
                    System.out.println("           |   Register Customer    |");
                    System.out.println("           |------------------------|");
                    RegisterCust.register(scan);
                    break;
                case 0:
                    System.out.println("Exiting...");
                    displayGoodbye();
                    break;
                default:
                    System.out.println("Invalid choice! Please enter 0 - 3.");
                    break;
            }
            if (customer != null) {
                CustomerMenu.show(customer);
            } else if (staff != null) {
                StaffMenu.show(staff);
            }

        } while (choice != 0);
    }

    public static void displayMenu() {
        System.out.println("\n|--------------------------------------------------------|");
        System.out.println("|                                                        |");
        System.out.println("|             WELCOME TO CINEMA TICKETING SYSTEM         |");
        System.out.println("|                                                        |");
        System.out.println("|--------------------------------------------------------|");
        System.out.println("|                        MAIN MENU                       |");
        System.out.println("|--------------------------------------------------------|");
        System.out.println("|                                                        |");
        System.out.println("|                   [1.] Customer Login                  |");
        System.out.println("|                   [2.] Staff Login                     |");
        System.out.println("|                   [3.] Register Customer               |");
        System.out.println("|                   [0.] Exit                            |");
        System.out.println("|                                                        |");
        System.out.println("|--------------------------------------------------------|\n");
        System.out.print("Enter your choice:");
    }

    public static void displayGoodbye() {
        System.out.println("                                            |              ");
        System.out.println("                          __________________I______________");
        System.out.println("                         ( ______________________________ ()");
        System.out.println("                       _.-'|                              ||");
        System.out.println("                   _.-'   ||            GOODBYE           ||");
        System.out.println("  ______       _.-'       ||                              ||");
        System.out.println(" |      |_ _.-'           ||      Thank you for using     ||");
        System.out.println(" |      |_|_              ||                              ||");
        System.out.println(" |______|   `-._          ||   CINEMA TICKETING SYSTEM    ||");
        System.out.println("    / \\          `-._     ||                              ||");
        System.out.println("   /   \\             `-._ ||  Hope to see you again soon  ||");;
        System.out.println("  /     \\                `-.I______________________________||");
        System.out.println(" /       \\                --------------------------------- ");
        System.out.println("/____ ____\\________________________/________________\\______");
    }

}
