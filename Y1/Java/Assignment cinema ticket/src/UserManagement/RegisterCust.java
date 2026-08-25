/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package UserManagement;

/**
 *
 * @author User
 */
import java.util.ArrayList;
import java.util.Scanner;
import FileManagement.FileManager;
import java.io.IOException;

public class RegisterCust {

    private static ArrayList<Customer> customerList = new ArrayList<>();
    private static final String FILE_NAME = "customer.dat";

    public static void register(Scanner scan) {
        boolean loginSuccessful = false;
        loadCustomers();

        while (!loginSuccessful) {
            String userID;
            String name = "";
            boolean validUserID = false;
            while (!validUserID) {
                System.out.print("Enter your name:  ");
                name = scan.nextLine();
                try {
                    if (name == null || name.trim().isEmpty()) {
                        System.out.println("No input");
                        continue;
                    } else if (name.matches(".*[0-9].*")) {
                        System.out.println("Invalid name. Use only alphabets");
                        continue;
                    } else {
                        validUserID = true;
                    }
                } catch (Exception e) {
                    System.out.print("Error processing name: " + e.getMessage());
                }
            }

            do {
                System.out.print("Enter your username: ");
                userID = scan.nextLine();

                if (isUserIDTaken(userID)) {
                    System.out.println("Username '" + userID + "' is already registered. Please choose another username.");
                    return;
                } else {
                    validUserID = true;
                }
            } while (!validUserID);

            int telNo = 0;
            boolean validPhone = false;
            while (!validPhone) {
                System.out.print("Enter your telNo(+60): ");
                try {
                    if (!scan.hasNextInt()) {
                        System.out.println("Invalid! Only numbers.");
                        scan.next();
                        continue;
                    } else {
                        telNo = scan.nextInt();
                        validPhone = true;
                    }
                } catch (Exception e) {
                    System.out.println("Error processing phone number" + e.getMessage());
                    scan.nextLine();
                }
            }

            scan.nextLine();

            String email = "";
            boolean validateEmail = false;
            while (!validateEmail) {
                try {
                    System.out.print("Enter your email:  ");
                    email = scan.nextLine();
                    if (email != null && !email.trim().isEmpty()) {
                        if (!(email.contains("@") && email.contains("."))) {
                            System.out.println("Invalid email format. Must contain '@' and '.'");
                        } else {
                            validateEmail = true;
                            System.out.println("Email verified, please press enter to continue");
                        }
                    }
                } catch (Exception e) {
                    System.out.println("Error processing email" + e.getMessage());
                    scan.nextLine();
                }
            }
            scan.nextLine();

            System.out.print("Enter your password (must be at least 8 characters long including numbers(0-9) and alphabets): ");
            String password = scan.nextLine();
            System.out.print("Reenter your password: ");
            String password2 = scan.nextLine();

            if (verifyCust(password, password2)) {
                while (true) {
                    int confirmation = confirmCust(userID, telNo, email, scan);

                    if (confirmation == 1) {
                        Customer newCustomer = new Customer(name, password, telNo, userID, email);
                        customerList.add(newCustomer);
                        saveCustomers();
                        System.out.println("Registration successful!");
                        loginSuccessful = true;
                        break;
                    } else if (confirmation == 2) {
                        System.out.println("Please re-enter your details.");
                        return;
                    } else {
                        System.out.println("Invalid selection. Please select 1 or 2.");
                    }
                }
            } else {
                System.out.println("Registration failed. Please try again.");
                return;
            }
        }
    }

    private static boolean isUserIDTaken(String userID) {
        for (Customer customer : customerList) {
            if (customer.getuserID().equals(userID)) {
                return true;
            }
        }
        return false;
    }

    public static boolean verifyCust(String password, String password2) {
        boolean valid = true;

        if (!password.equals(password2)) {
            System.out.println("Passwords do not match.");
            valid = false;
        }

        if (password.length() < 8) {
            System.out.println("Password must be at least 8 characters long.");
            valid = false;
        }

        if (!(password.matches(".*[0-9].*") && password.matches(".*[a-zA-Z].*"))) {
            System.out.println("Password must contain both letters and numbers.");
            valid = false;
        }

        return valid;
    }

    public static int confirmCust(String userID, int telNo, String email, Scanner scan) {
        System.out.println("\nPlease check your personal details: ");
        System.out.println("======================================");
        System.out.println("userID: " + userID);
        System.out.println("telNo : +60" + telNo);
        System.out.println("email : " + email);
        System.out.println("======================================\n");

        System.out.print("Is this information correct? (1. yes, 2. no): ");
        while (!scan.hasNextInt()) {
            System.out.println("Invalid input! Please enter a number between 1 and 2.");
            scan.next(); // Clear the invalid input
            System.out.print("Enter your choice: ");
        }
        int confirmation = scan.nextInt();
        scan.nextLine();
        return confirmation;
    }

    private static void saveCustomers() {
        try {
            FileManager.saveToFile(FILE_NAME, customerList);
        } catch (IOException e) {
            System.out.println("Error saving customers: " + e.getMessage());
        }
    }

    private static void loadCustomers() {
        try {
            customerList = FileManager.loadFromFile(FILE_NAME);
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading customers: " + e.getMessage());
            customerList = new ArrayList<>();
        }
    }
}
