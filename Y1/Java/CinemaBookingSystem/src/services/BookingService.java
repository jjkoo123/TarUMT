package services;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import models.*;
import utils.SeatLayout;

public class BookingService extends BaseService {
    private static final String HISTORY_BOOKINGS_FILE = "history_bookings.txt";
    private static final String FIELD_SEPARATOR = "|";
    private final Map<String, MovieSession> sessionsByShow = new HashMap<>();
    private final List<Booking> bookings = new ArrayList<>();

    public BookingService() {
        super("current_bookings.txt");
    }

    public SeatLayout getSeatLayout(String movieTitle, String cinemaType, String showTime) {
        return getOrCreateSession(movieTitle, cinemaType, showTime).getSeatLayout();
    }

    public Booking createBooking(User user, Movie movie, String cinemaType, String showTime, int row, int col, String paymentMethod,
            double price) {
        if (user == null || movie == null || cinemaType == null || showTime == null || paymentMethod == null) {
            return null;
        }

        if (showTime.isBlank() || paymentMethod.isBlank() || price < 0) {
            return null;
        }

        SeatLayout seatLayout = getSeatLayout(movie.getTitle(), cinemaType, showTime);
        if (!seatLayout.isValidSeat(row, col)) {
            return null;
        }

        boolean success = seatLayout.bookSeat(row, col);
        if (!success) {
            return null;
        }

        Booking booking = new Booking(user.getUsername(), movie.getTitle(), cinemaType, showTime, row, col, paymentMethod, price);
        bookings.add(booking);
        saveBooking(booking);
        return booking;
    }

    public boolean cancelBooking(Booking booking) {
        if (booking == null) return false;

        SeatLayout seatLayout = getSeatLayout(booking.getMovieTitle(), booking.getCinemaType(), booking.getShowTime());
        boolean seatFreed = seatLayout.unbookSeat(booking.getRow(), booking.getCol());
        boolean removed = bookings.remove(booking);
        
        if (removed || seatFreed) {
            saveToFile();
            return true;
        }
        return false;
    }

    public List<Booking> getBookingsForUser(String username) {
        List<Booking> result = new ArrayList<>();
        if (username == null || username.isBlank()) {
            return result;
        }

        for (Booking booking : bookings) {
            if (booking.getUsername().equalsIgnoreCase(username.trim())) {
                result.add(booking);
            }
        }
        return result;
    }

    public List<Booking> getAllBookings() {
        return new ArrayList<>(bookings);
    }

    public int removeBookingsForMovie(String movieTitle) {
        if (movieTitle == null || movieTitle.isBlank()) {
            return 0;
        }

        int before = bookings.size();
        bookings.removeIf(booking -> booking.getMovieTitle().equalsIgnoreCase(movieTitle.trim()));
        sessionsByShow.keySet()
                .removeIf(key -> key.toLowerCase().startsWith(movieTitle.trim().toLowerCase() + FIELD_SEPARATOR));

        if (bookings.size() != before) {
            saveToFile();
        }
        return before - bookings.size();
    }

    public int removeBookingsForSession(String movieTitle, String cinemaType, String showTime) {
        if (movieTitle == null || movieTitle.isBlank() || cinemaType == null || cinemaType.isBlank() || showTime == null || showTime.isBlank()) {
            return 0;
        }

        String cleanMovie = movieTitle.trim();
        String cleanType = cinemaType.trim();
        String cleanShow = showTime.trim();

        int before = bookings.size();
        bookings.removeIf(booking -> booking.getMovieTitle().equalsIgnoreCase(cleanMovie)
                && booking.getCinemaType().equalsIgnoreCase(cleanType)
                && booking.getShowTime().equalsIgnoreCase(cleanShow));

        sessionsByShow.remove(buildKey(cleanMovie, cleanType, cleanShow));

        if (bookings.size() != before) {
            saveToFile();
        }
        return before - bookings.size();
    }

    public int removeBookingsForShowtime(String movieTitle, String showTime) {
        if (movieTitle == null || movieTitle.isBlank() || showTime == null || showTime.isBlank()) {
            return 0;
        }

        String cleanMovie = movieTitle.trim();
        String cleanShow = showTime.trim();

        int before = bookings.size();
        bookings.removeIf(booking -> booking.getMovieTitle().equalsIgnoreCase(cleanMovie)
                && booking.getShowTime().equalsIgnoreCase(cleanShow));

        sessionsByShow.keySet().removeIf(key -> key.toLowerCase().startsWith(cleanMovie.toLowerCase() + FIELD_SEPARATOR)
                && key.toLowerCase().endsWith(FIELD_SEPARATOR + cleanShow.toLowerCase()));

        if (bookings.size() != before) {
            saveToFile();
        }
        return before - bookings.size();
    }

    @Override
    public void loadFromFile() {
        bookings.clear();
        sessionsByShow.clear();

        try {
            File file = new File(filePath);
            Scanner scanner = new Scanner(file);

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                if (line.isEmpty()) {
                    continue;
                }

                String[] data = line.split("\\|", 8);
                if (data.length < 7) {
                    continue;
                }

                try {
                    String username = data[0].trim();
                    String movieTitle = data[1].trim();
                    
                    String cinemaType = "Standard";
                    int nextIdx = 2;
                    if (data.length >= 8) {
                        cinemaType = data[2].trim();
                        nextIdx = 3;
                    }
                    
                    String showTime = data[nextIdx].trim();
                    int row = Integer.parseInt(data[nextIdx+1].trim());
                    int col = Integer.parseInt(data[nextIdx+2].trim());
                    String paymentMethod = data[nextIdx+3].trim();
                    double price = Double.parseDouble(data[nextIdx+4].trim());

                    if (username.isEmpty() || movieTitle.isEmpty() || showTime.isEmpty() || paymentMethod.isEmpty()
                            || price < 0) {
                        continue;
                    }

                    SeatLayout seatLayout = getSeatLayout(movieTitle, cinemaType, showTime);
                    if (!seatLayout.isValidSeat(row, col)) {
                        continue;
                    }

                    if (!seatLayout.bookSeat(row, col)) {
                        continue;
                    }

                    Booking booking = new Booking(username, movieTitle, cinemaType, showTime, row, col, paymentMethod, price);
                    bookings.add(booking);
                } catch (NumberFormatException ex) {
                    // Skip corrupted lines.
                }
            }

            scanner.close();
        } catch (FileNotFoundException e) {
            // No existing bookings file on first run.
        }
    }

    private String buildKey(String movieTitle, String cinemaType, String showTime) {
        return movieTitle.trim() + FIELD_SEPARATOR + cinemaType.trim() + FIELD_SEPARATOR + showTime.trim();
    }

    private MovieSession getOrCreateSession(String movieTitle, String cinemaType, String showTime) {
        String key = buildKey(movieTitle, cinemaType, showTime);
        if (!sessionsByShow.containsKey(key)) {
            sessionsByShow.put(key, new MovieSession(movieTitle, cinemaType, showTime));
        }
        return sessionsByShow.get(key);
    }

    private void saveBooking(Booking booking) {
        String line = booking.getUsername() + FIELD_SEPARATOR
                + booking.getMovieTitle() + FIELD_SEPARATOR
                + booking.getCinemaType() + FIELD_SEPARATOR
                + booking.getShowTime() + FIELD_SEPARATOR
                + booking.getRow() + FIELD_SEPARATOR + booking.getCol() + FIELD_SEPARATOR
                + booking.getPaymentMethod() + FIELD_SEPARATOR
                + booking.getPrice();
        
        try (FileWriter writer = new FileWriter(filePath, true)) {
            writer.write(line + System.lineSeparator());
        } catch (IOException e) {
            System.out.println("Failed to save booking to current bookings.");
        }
        
        try (FileWriter writer = new FileWriter(HISTORY_BOOKINGS_FILE, true)) {
            writer.write(line + System.lineSeparator());
        } catch (IOException e) {
            System.out.println("Failed to save booking to history bookings.");
        }
    }

    @Override
    public void saveToFile() {
        try (FileWriter writer = new FileWriter(filePath)) {
            for (Booking booking : bookings) {
                String line = booking.getUsername() + FIELD_SEPARATOR
                        + booking.getMovieTitle() + FIELD_SEPARATOR
                        + booking.getCinemaType() + FIELD_SEPARATOR
                        + booking.getShowTime() + FIELD_SEPARATOR
                        + booking.getRow() + FIELD_SEPARATOR + booking.getCol() + FIELD_SEPARATOR
                        + booking.getPaymentMethod() + FIELD_SEPARATOR
                        + booking.getPrice();
                writer.write(line + System.lineSeparator());
            }
        } catch (IOException e) {
            System.out.println("Failed to rewrite bookings file.");
        }
    }
}
