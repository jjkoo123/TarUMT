/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package UserManagement;

/**
 *
 * @author User
 */
import FileManagement.FileManager;
import java.util.List;
import java.util.Scanner;
import java.util.ArrayList;
import java.io.IOException;

public class Login {

    private static final String FILE_NAME = "customer.dat";

    public static Customer cust(Scanner scan) {
        System.out.print("Enter Customer ID: ");
        String id = scan.nextLine();
        System.out.print("Enter Password: ");
        String pw = scan.nextLine();

        List<Customer> customers = loadCustomers();

        for (Customer customer : customers) {
            if (customer.getuserID().equals(id) && customer.getPassword().equals(pw)) {
                System.out.println("Login successful! Welcome, " + customer.getName());
                return customer;
            }
        }

        System.out.println("Invalid customer account.");
        return null;
    }

    public static Staff staff(Scanner scan) {
        Staff adminStaff = new Staff("Administrator", "111111", "admin");

        System.out.print("Enter Staff ID: ");
        String id = scan.nextLine();
        System.out.print("Enter Password: ");
        String pw = scan.nextLine();

        if (id.equals(adminStaff.getStaffID()) && pw.equals(adminStaff.getPassword())) {
            return adminStaff;
        } else {
            System.out.println("Invalid staff account.");
            return null;
        }
    }

    private static List<Customer> loadCustomers() {
        try {
            return FileManager.loadFromFile(FILE_NAME);
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading customers: " + e.getMessage());
            return new ArrayList<>();
        }
    }
}
