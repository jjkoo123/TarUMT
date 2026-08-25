package services;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import models.*;

public class MovieService extends BaseService {
    private static final String MOVIE_SEPARATOR = "|";
    private static final String SHOWTIME_SEPARATOR = ";";
    private final List<Movie> movies = new ArrayList<>();

    public MovieService() {
        super("movies.txt");
        loadFromFile();
    }

    public List<Movie> getMovies() {
        return Collections.unmodifiableList(movies);
    }

    public Movie getMovieByChoice(int choice) {
        if (choice < 1 || choice > movies.size()) {
            return null;
        }
        return movies.get(choice - 1);
    }

    public String getShowTimeByChoice(Movie movie, int choice) {
        List<String> showTimes = movie.getShowTimes();
        if (choice < 1 || choice > showTimes.size()) {
            return null;
        }
        return showTimes.get(choice - 1);
    }

    public boolean addMovie(String title, String genre, String cinemaType, int durationMins, List<String> showTimes, double price) {
        String cleanTitle = sanitizeTitle(title);
        if (cleanTitle == null || showTimes == null || showTimes.isEmpty()) {
            return false;
        }

        if (findMovieIndexByTitle(cleanTitle) != -1) {
            return false;
        }

        List<String> cleanShowTimes = sanitizeShowTimes(showTimes);

        if (cleanShowTimes.isEmpty()) {
            return false;
        }

        movies.add(new Movie(cleanTitle, genre, cinemaType, durationMins, cleanShowTimes, price));
        saveToFile();
        return true;
    }

    public boolean setMoviePrice(String title, double newPrice) {
        int idx = findMovieIndexByTitle(title);
        if (idx == -1) return false;
        Movie m = movies.get(idx);
        Movie updated = new Movie(m.getTitle(), m.getGenre(), m.getCinemaType(), m.getDurationMins(), m.getShowTimes(), newPrice);
        movies.set(idx, updated);
        saveToFile();
        return true;
    }

    public boolean removeMovie(String title) {
        int index = findMovieIndexByTitle(title);
        if (index == -1) {
            return false;
        }

        movies.remove(index);
        saveToFile();
        return true;
    }

    public boolean addCinemaType(String title, String newTypesStr) {
        int idx = findMovieIndexByTitle(title);
        if (idx == -1) return false;

        Movie m = movies.get(idx);
        
        List<String> existing = new ArrayList<>();
        for (String c : m.getCinemaType().split(",")) {
            String trimmed = c.trim();
            if (!trimmed.isEmpty()) existing.add(trimmed);
        }
        
        for (String t : newTypesStr.split(",")) {
            String trimmed = t.trim();
            if (!trimmed.isEmpty() && !existing.contains(trimmed)) {
                existing.add(trimmed);
            }
        }
        
        String updatedCinemaType = String.join(", ", existing);

        Movie updated = new Movie(m.getTitle(), m.getGenre(), updatedCinemaType, m.getDurationMins(), m.getShowTimes(), m.getPrice());
        movies.set(idx, updated);
        saveToFile();
        return true;
    }

    public boolean addShowTime(String title, String showTime) {
        int index = findMovieIndexByTitle(title);
        String cleanShowTime = sanitizeShowTime(showTime);
        if (index == -1 || cleanShowTime == null) {
            return false;
        }

        Movie movie = movies.get(index);
        List<String> updatedShowTimes = new ArrayList<>(movie.getShowTimes());

        for (String existing : updatedShowTimes) {
            if (existing.equalsIgnoreCase(cleanShowTime)) {
                return false;
            }
        }

        updatedShowTimes.add(cleanShowTime);
        movies.set(index, new Movie(movie.getTitle(), movie.getGenre(), movie.getCinemaType(), movie.getDurationMins(), updatedShowTimes, movie.getPrice()));
        saveToFile();
        return true;
    }

    public boolean removeShowTime(String title, String showTime) {
        int index = findMovieIndexByTitle(title);
        String cleanShowTime = sanitizeShowTime(showTime);
        if (index == -1 || cleanShowTime == null) {
            return false;
        }

        Movie movie = movies.get(index);
        List<String> updatedShowTimes = new ArrayList<>(movie.getShowTimes());
        boolean removed = false;

        for (int i = 0; i < updatedShowTimes.size(); i++) {
            if (updatedShowTimes.get(i).equalsIgnoreCase(cleanShowTime)) {
                updatedShowTimes.remove(i);
                removed = true;
                break;
            }
        }

        if (!removed || updatedShowTimes.isEmpty()) {
            return false;
        }

        movies.set(index, new Movie(movie.getTitle(), movie.getGenre(), movie.getCinemaType(), movie.getDurationMins(), updatedShowTimes, movie.getPrice()));
        saveToFile();
        return true;
    }

    @Override
    public void loadFromFile() {
        movies.clear();

        try {
            File file = new File(filePath);
            Scanner scanner = new Scanner(file);

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                if (line.isEmpty()) {
                    continue;
                }

                String[] parts = line.split("\\|", 5);
                if (parts.length < 4) {
                    continue;
                }

                String title = sanitizeTitle(parts[0]);
                String genre = parts[1];
                
                String cinemaType = "Standard";
                int durationIndex = 2;
                if (parts.length == 5) {
                    cinemaType = parts[2];
                    durationIndex = 3;
                }

                int duration;
                try {
                    duration = Integer.parseInt(parts[durationIndex]);
                } catch (NumberFormatException e) {
                    duration = 120; // Default
                }

                String[] times = parts[durationIndex + 1].split(SHOWTIME_SEPARATOR);
                List<String> showTimes = new ArrayList<>();

                for (String time : times) {
                    String clean = sanitizeShowTime(time);
                    if (clean != null && !containsIgnoreCase(showTimes, clean)) {
                        showTimes.add(clean);
                    }
                }

                if (title != null && !showTimes.isEmpty() && findMovieIndexByTitle(title) == -1) {
                    movies.add(new Movie(title, genre, cinemaType, duration, showTimes, 18.0));
                }
            }

            scanner.close();
        } catch (FileNotFoundException e) {
            // Ignore and initialize defaults below.
        }

        if (movies.isEmpty()) {
            initializeDefaultMovies();
            saveToFile();
        }
    }

    private void initializeDefaultMovies() {
        movies.add(new Movie("Avengers: Endgame", "Action / Sci-Fi", "IMAX", 181, List.of("10:00 AM", "2:00 PM", "8:00 PM"), 20.0));
        movies.add(new Movie("The Batman", "Action / Crime", "Standard", 176, List.of("11:00 AM", "3:00 PM", "9:00 PM"), 18.0));
        movies.add(new Movie("Spider-Man: No Way Home", "Action / Adventure", "3D", 148,
                List.of("9:30 AM", "1:30 PM", "7:30 PM"), 18.0));
        movies.add(new Movie("Dune: Part Two", "Sci-Fi / Adventure", "IMAX", 166, List.of("12:00 PM", "4:30 PM", "10:00 PM"), 25.0));
        movies.add(new Movie("Oppenheimer", "Biography / Drama", "Standard", 180, List.of("1:00 PM", "6:00 PM"), 15.0));
    }

    @Override
    public void saveToFile() {
        try (FileWriter writer = new FileWriter(filePath)) {
            for (Movie movie : movies) {
                String line = movie.getTitle() + MOVIE_SEPARATOR
                        + movie.getGenre() + MOVIE_SEPARATOR
                        + movie.getCinemaType() + MOVIE_SEPARATOR
                        + movie.getDurationMins() + MOVIE_SEPARATOR
                        + String.join(SHOWTIME_SEPARATOR, movie.getShowTimes());
                writer.write(line + System.lineSeparator());
            }
        } catch (IOException e) {
            System.out.println("Failed to save movies.");
        }
    }

    private int findMovieIndexByTitle(String title) {
        String cleanTitle = sanitizeTitle(title);
        if (cleanTitle == null) {
            return -1;
        }

        for (int i = 0; i < movies.size(); i++) {
            if (movies.get(i).getTitle().equalsIgnoreCase(cleanTitle)) {
                return i;
            }
        }
        return -1;
    }

    private String sanitizeTitle(String title) {
        if (title == null) {
            return null;
        }

        String clean = title.trim();
        if (clean.isEmpty() || clean.contains(MOVIE_SEPARATOR) || clean.contains(SHOWTIME_SEPARATOR)) {
            return null;
        }

        return clean;
    }

    private String sanitizeShowTime(String showTime) {
        if (showTime == null) {
            return null;
        }

        String clean = showTime.trim();
        if (clean.isEmpty() || clean.contains(MOVIE_SEPARATOR) || clean.contains(SHOWTIME_SEPARATOR)) {
            return null;
        }

        return clean;
    }

    private List<String> sanitizeShowTimes(List<String> showTimes) {
        List<String> cleanShowTimes = new ArrayList<>();

        for (String showTime : showTimes) {
            String clean = sanitizeShowTime(showTime);
            if (clean != null && !containsIgnoreCase(cleanShowTimes, clean)) {
                cleanShowTimes.add(clean);
            }
        }

        return cleanShowTimes;
    }

    private boolean containsIgnoreCase(List<String> values, String target) {
        for (String value : values) {
            if (value.equalsIgnoreCase(target)) {
                return true;
            }
        }
        return false;
    }
}
