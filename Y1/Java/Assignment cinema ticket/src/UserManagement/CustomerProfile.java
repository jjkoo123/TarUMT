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
import java.io.IOException;
import FileManagement.FileManager;

public class CustomerProfile {

    private static final String FILE_NAME = "customer.dat";

    public static void edit(Customer customer, Scanner scanner) {
        System.out.println("\n=============================");
        System.out.println("=       Edit Profile        =");
        System.out.println("=============================\n");

        
        System.out.println("Current Profile Information");
        System.out.println("---------------------------");
        System.out.println("Name   : " + customer.getName());
        System.out.println("User ID: " + customer.getuserID());
        System.out.println("Email  : " + customer.getEmail());
        System.out.println("Phone  : +60" + customer.getTelNo());

        System.out.println("\nWhat would you like to edit?");
        System.out.println("[1.] Name");
        System.out.println("[2.] Email");
        System.out.println("[3.] Phone Number");
        System.out.println("[4.] Password");
        System.out.println("[5.] Delete Account");
        System.out.println("[0.] Back");
        System.out.print("Enter your choice: ");

        while (!scanner.hasNextInt()) {
            System.out.println("Invalid input! Please enter a number between 0 and 4.");
            scanner.next();
            System.out.print("Enter your choice: ");
        }

        int choice = scanner.nextInt();
        scanner.nextLine();

        boolean changesMade = false;

        switch (choice) {
            case 1:
                System.out.print("Enter new name: ");
                String newName = scanner.nextLine();
                if (validateName(newName)) {
                    customer.setName(newName);
                    System.out.println("Name updated successfully!");
                    changesMade = true;
                } else {
                    System.out.println("Invalid name format. Must contain only alphabets");
                }
                break;
            case 2:
                System.out.print("Enter new email: ");
                String newEmail = scanner.nextLine();
                if (validateEmail(newEmail)) {
                    customer.setEmail(newEmail);
                    System.out.println("Email updated successfully!");
                    changesMade = true;
                } else {
                    System.out.println("Invalid email format. Must contain '@' and '.'");
                }
                break;
            case 3:
                System.out.print("Enter new phone number: ");
                try {
                    int newPhone = Integer.parseInt(scanner.nextLine());
                    customer.setTelNo(newPhone);
                    System.out.println("Phone number updated successfully!");
                    changesMade = true;
                } catch (NumberFormatException e) {
                    System.out.println("Invalid phone number format. Please enter numbers only.");
                }
                break;
            case 4:
                System.out.print("Enter current password: ");
                String currentPassword = scanner.nextLine();
                if (currentPassword.equals(customer.getPassword())) {
                    System.out.print("Enter new password (must be at least 8 characters long including numbers(0-9) and alphabets): ");
                    String newPassword = scanner.nextLine();
                    System.out.print("Confirm new password: ");
                    String confirmPassword = scanner.nextLine();

                    if (validatePassword(newPassword, confirmPassword)) {
                        customer.setPassword(newPassword);
                        System.out.println("Password updated successfully!");
                        changesMade = true;
                    }
                } else {
                    System.out.println("Incorrect current password. No changes made.");
                }
                break;
            case 5:
                if (confirmDeleteAccount(scanner)) {
                    if (deleteAccount(customer)) {
                        System.out.println("Account deleted successfully!");
                        return;
                    } else {
                        System.out.println("Failed to delete account. Please try again");
                    }
                } else {
                    System.out.println("Account deletion cancelled");
                }
                break;
            case 0:
                return;
            default:
                System.out.println("Invalid choice.");
        }

        if (changesMade) {
            saveChanges(customer);
        }
    }

    private static boolean validateEmail(String email) {
        return email.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$");
    }

    private static boolean validateName(String name) {
        return !name.matches(".*[0-9].*");
    }

    private static boolean validatePassword(String password, String confirmPassword) {
        if (!password.equals(confirmPassword)) {
            System.out.println("Passwords do not match.");
            return false;
        }

        if (password.length() < 8) {
            System.out.println("Password must be at least 8 characters long.");
            return false;
        }

        if (!(password.matches(".*[0-9].*") && password.matches(".*[a-zA-Z].*"))) {
            System.out.println("Password must contain both letters and numbers.");
            return false;
        }

        return true;
    }

    private static void saveChanges(Customer customer) {
        try {

            ArrayList<Customer> customerList = FileManager.loadFromFile(FILE_NAME);

            boolean found = false;
            for (int i = 0; i < customerList.size(); i++) {
                if (customerList.get(i).getuserID().equals(customer.getuserID())) {
                    customerList.set(i, customer);
                    found = true;
                    break;
                }
            }

            if (!found) {
                System.out.println("Error: Customer not found in the database.");
                return;
            }
            FileManager.saveToFile(FILE_NAME, customerList);
            System.out.println("Profile changes saved successfully!");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error saving profile changes: " + e.getMessage());
        }
    }

    private static boolean confirmDeleteAccount(Scanner scanner) {
        System.out.println("\n========================  WARNING: ACCOUNT DELETEION  =======================");
        System.out.println("                    You are about tp delete this account.");
        System.out.println("                 This action CANNOT be reverseble or undone");
        System.out.println("     All your data including booking history will be permenantly deleted");
        System.out.println("=============================================================================");
        System.out.println("Are you sure you want to delete your account? [1.]Yes [2.]No");
        System.out.print("Enter your choice:  ");
        while (!scanner.hasNextInt()) {
            System.out.println("Invalid input! Please enter a number between 1 and 2.");
            scanner.next();
            System.out.print("Enter your choice: ");
        }
        int response = scanner.nextInt();

        switch (response) {
            case 1:
                return true;
            case 2:
                return false;
            default:
                System.out.println("Invalid choice");
                return false;
        }

    }

    private static boolean deleteAccount(Customer customer) {
        try {
            ArrayList<Customer> customerList = (ArrayList<Customer>) FileManager.loadFromFile(FILE_NAME);

            boolean customerFound = false;

            for (int i = 0; i < customerList.size(); i++) {
                if (customerList.get(i).getuserID().equals(customer.getuserID())) {
                    customerList.remove(i);
                    customerFound = true;
                    break;
                }
            }
            if (!customerFound) {
                System.out.println("Customer not found");
                return false;
            }

            FileManager.saveToFile(FILE_NAME, customerList);
            return true;
        } catch (Exception e) {
            System.out.println("Error deleting customer" + e.getMessage());
            return false;
        }
    }
}
