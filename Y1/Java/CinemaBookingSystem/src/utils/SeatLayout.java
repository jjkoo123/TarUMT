package utils;


import utils.ConsoleUI;

public class SeatLayout {
    public static final int ROWS = 5;
    public static final int COLS = 5;

    private final char[][] seats = new char[ROWS][COLS];

    public SeatLayout() {
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                seats[i][j] = 'O';
            }
        }
    }

    public void displaySeats() {
        ConsoleUI.pageTitle("SEAT LAYOUT");
        ConsoleUI.info("Legend: O = Available, X = Booked");
        ConsoleUI.divider();
        System.out.print("      ");
        for (int col = 0; col < COLS; col++) {
            System.out.printf(" %-2d ", col + 1);
        }
        System.out.println("\n");

        for (int i = 0; i < seats.length; i++) {
            System.out.printf("  %c  |", (char) ('A' + i));
            for (int j = 0; j < seats[i].length; j++) {
                if (seats[i][j] == 'O') {
                    System.out.print(" " + ConsoleUI.ANSI_GREEN + "[O]" + ConsoleUI.ANSI_RESET);
                } else {
                    System.out.print(" " + ConsoleUI.ANSI_RED + "[X]" + ConsoleUI.ANSI_RESET);
                }
            }
            System.out.println(" | " + (char) ('A' + i));
            System.out.println();
        }
        ConsoleUI.divider();
        System.out.print("      ");
        for (int col = 0; col < COLS; col++) {
            System.out.printf(" %-2d ", col + 1);
        }
        System.out.println("\n");
        ConsoleUI.info("Screen is this way ^");
    }

    public boolean isSeatAvailable(int row, int col) {
        return isValidSeat(row, col) && seats[row][col] == 'O';
    }

    public boolean bookSeat(int row, int col) {
        if (!isValidSeat(row, col)) {
            return false;
        }

        if (seats[row][col] == 'O') {
            seats[row][col] = 'X';
            return true;
        }
        return false;
    }

    public boolean unbookSeat(int row, int col) {
        if (!isValidSeat(row, col)) {
            return false;
        }

        if (seats[row][col] == 'X') {
            seats[row][col] = 'O';
            return true;
        }
        return false;
    }

    public boolean isValidSeat(int row, int col) {
        return row >= 0 && row < ROWS && col >= 0 && col < COLS;
    }

    public int getAvailableSeats() {
        int count = 0;
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                if (seats[i][j] == 'O') {
                    count++;
                }
            }
        }
        return count;
    }

    public int getTotalSeats() {
        return ROWS * COLS;
    }
}