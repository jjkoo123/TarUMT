package BookingManagement;

import java.util.*;
import FileManagement.FileManager;
import MovieManagement.Showtime;
import java.io.Serializable;

public class SeatLayout implements Serializable {

    private String fileName;
    private int rows = 5;
    private int cols = 5;
    private Map<String, Seat> seatMap;
    private Showtime showtime;

    public SeatLayout() {
        this.fileName = "seat_default.dat";
        this.seatMap = new LinkedHashMap<>();
        generateSeats();
        saveSeats();
    }


    public SeatLayout(Showtime showtime) {
        this.showtime = showtime;
        this.fileName = "seat_" + showtime.getId() + "_" + showtime.getHall().getId() + ".dat";
        this.seatMap = loadSeats();

        if (seatMap == null || seatMap.isEmpty()) {
            seatMap = new LinkedHashMap<>();
            generateSeats();
            saveSeats();
        }
    }

    public Showtime getShowtime() {
        return showtime;
    }

    public void displayLayout() {
        System.out.println("\n                SCREEN");
        System.out.println("                -------");

        char currentRow = 'A';

        for (Map.Entry<String, Seat> entry : seatMap.entrySet()) {
            String seatId = entry.getKey();
            Seat seat = entry.getValue();

            if (seatId.charAt(0) != currentRow) {
                System.out.println();
                currentRow = seatId.charAt(0);
            }

            System.out.print(seatId + " [" + (seat.isBooked() ? "X" : " ") + "]  ");
        }

        System.out.println("\n");
    }

    private void generateSeats() {
        for (char row = 'A'; row < 'A' + rows; row++) {
            for (int col = 1; col <= cols; col++) {
                String seatId = "" + row + col;
                seatMap.put(seatId, new Seat(seatId));
            }
        }
    }

    public List<String> bookSeats(List<String> seatIds) {
        List<String> successfullyBooked = new ArrayList<>();
        for (String seatId : seatIds) {
            Seat seat = seatMap.get(seatId);
            if (seat != null && !seat.isBooked()) {
                seat.setBooked(true);
                successfullyBooked.add(seatId);
            } else {
                System.out.println("Seat " + seatId + " is invalid or already booked.");
            }
        }

        if (!successfullyBooked.isEmpty()) {
            saveSeats();
        }

        return successfullyBooked;
    }

    public List<String> getUnavailableSeats(List<String> requestedSeats) {
        List<String> unavailable = new ArrayList<>();
        for (String seatId : requestedSeats) {
            if (!isSeatAvailable(seatId)) {
                unavailable.add(seatId);
            }
        }
        return unavailable;
    }

    public boolean isSeatAvailable(String seatId) {
        Seat seat = seatMap.get(seatId);
        return seat != null && !seat.isBooked();
    }

    private void saveSeats() {
        try {
            FileManager.saveToFile(fileName, seatMap);
        } catch (Exception e) {
            System.out.println("Error saving seats: " + e.getMessage());
        }
    }

    private Map<String, Seat> loadSeats() {
        try {
            return FileManager.loadFromFile(fileName);
        } catch (Exception e) {
            return new LinkedHashMap<>();
        }
    }
}
