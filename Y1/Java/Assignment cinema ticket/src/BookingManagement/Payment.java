/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package BookingManagement;

import java.time.LocalDateTime;
import java.io.Serializable;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author User
 */
public class Payment extends Item implements Serializable {

    private String bookingId;
    private double amount;
    private LocalDateTime timestamp;

    public Payment(String bookingId, double amount) {
        super(generateRandomId("P"));
        this.bookingId = bookingId;
        this.amount = amount;
        this.timestamp = LocalDateTime.now();
    }

    public String getBookingId() {
        return bookingId;
    }

    public double getAmount() {
        return amount;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return "Payment ID: " + getId()
                + "\nAmount    : RM" + String.format("%.2f", amount)
                + "\nPaid On   : " + timestamp.format(formatter);
    }
}
