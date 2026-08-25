/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package BookingManagement;

import FileManagement.FileManager;
import java.io.IOException;
import java.util.*;

public class PaymentController {

    private List<Payment> payments = new ArrayList<>();
    private final String FILE_NAME = "payments.dat";

    public PaymentController() {
        try {
            payments = FileManager.loadFromFile(FILE_NAME);
        } catch (Exception e) {
            payments = new ArrayList<>();
        }
    }

    public Payment processPayment(String bookingId, int seatCount, double pricePerSeat) {
        double total = seatCount * pricePerSeat;
        Payment payment = new Payment(bookingId, total);
        payments.add(payment);
        savePayments();
        return payment;
    }

    public void viewPayments() {
        if (payments.isEmpty()) {
            System.out.println("No payments made yet.");
            return;
        }
        System.out.println("=== Payment History ===");
        for (Payment p : payments) {
            System.out.println(p);
        }
    }

    private void savePayments() {
        try {
            FileManager.saveToFile(FILE_NAME, payments);
        } catch (IOException e) {
            System.out.println("Error saving payments: " + e.getMessage());
        }
    }
}
