package services;
import java.util.*;
import models.*;
import utils.*;



import java.io.FileWriter;
import java.io.IOException;
import utils.ConsoleUI;

public class TicketService {
    public static void printTicket(String movie, String time, int row, int col, String paymentMethod, double price, String username, String cinemaType) {
        ConsoleUI.spacer();
        System.out.println(ConsoleUI.ANSI_CYAN + "============================================================" + ConsoleUI.ANSI_RESET);
        System.out.println(ConsoleUI.ANSI_YELLOW + "                       CINEMA TICKET                        " + ConsoleUI.ANSI_RESET);
        System.out.println(ConsoleUI.ANSI_CYAN + "============================================================" + ConsoleUI.ANSI_RESET);
        System.out.println("  Customer : " + username);
        System.out.println("  Movie    : " + movie);
        System.out.println("  Type     : " + cinemaType);
        System.out.println("  Time     : " + time);
        System.out.println("  Seat     : " + String.format("%c%d", (char) ('A' + row), col + 1));
        System.out.println("  Payment  : " + paymentMethod);
        System.out.println("  Total    : Rm" + String.format("%.2f", price));
        System.out.println(ConsoleUI.ANSI_CYAN + "============================================================" + ConsoleUI.ANSI_RESET);
        System.out.println(ConsoleUI.ANSI_GREEN + "           Thank you for booking with us! Enjoy!            " + ConsoleUI.ANSI_RESET);
        System.out.println(ConsoleUI.ANSI_CYAN + "============================================================" + ConsoleUI.ANSI_RESET);
        ConsoleUI.spacer();

        exportTicketToFile(username, movie, time, row, col, paymentMethod, price, cinemaType);
    }

    private static void exportTicketToFile(String username, String movie, String time, int row, int col, String paymentMethod, double price, String cinemaType) {
        String fileName = "Receipt_" + username + "_" + movie.replaceAll("[^a-zA-Z0-9]", "_") + ".txt";
        try (FileWriter writer = new FileWriter(fileName)) {
            writer.write("============================================================\n");
            writer.write("                       CINEMA TICKET                        \n");
            writer.write("============================================================\n");
            writer.write("Customer : " + username + "\n");
            writer.write("Movie    : " + movie + "\n");
            writer.write("Type     : " + cinemaType + "\n");
            writer.write("Time     : " + time + "\n");
            writer.write("Seat     : " + String.format("%c%d", (char) ('A' + row), col + 1) + "\n");
            writer.write("Payment  : " + paymentMethod + "\n");
            writer.write("Total    : Rm" + String.format("%.2f", price) + "\n");
            writer.write("============================================================\n");
            writer.write("           Thank you for booking with us! Enjoy!\n");
            writer.write("============================================================\n");
            ConsoleUI.success("Ticket receipt saved to file: " + fileName);
        } catch (IOException e) {
            ConsoleUI.error("Failed to export ticket file.");
        }
    }
}