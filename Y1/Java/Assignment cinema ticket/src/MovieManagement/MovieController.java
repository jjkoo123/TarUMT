/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package MovieManagement;

import java.util.*;
import java.io.*;

/**
 *
 * @author User
 */
public class MovieController {

    private List<Movie> movies;
    private final String FILE_NAME = "movies.txt";
    private Scanner scanner;

    public MovieController() {
        movies = new ArrayList<>();
        scanner = new Scanner(System.in);
        loadMovies();
    }

    public void addMovie() {
        System.out.println("\n===========================");
        System.out.println("=      Add New Movie      =");
        System.out.println("===========================\n");

        String id = UUID.randomUUID().toString().substring(0, 8);
        System.out.println("Generated Movie ID: " + id);

        String title = promptNonEmpty("Enter movie title: ");
        String genre = promptNonEmpty("Enter genre: ");
        String runningtime = promptNonEmpty("Enter running time: ");
        String subtitles = promptNonEmpty("Enter subtitles: ");
        String director = promptNonEmpty("Enter director: ");
        String casts = promptNonEmpty("Enter casts: ");
        String language = promptNonEmpty("Enter spoken language: ");
        String description = promptNonEmpty("Enter description: ");
        String classification = promptNonEmpty("Enter classification: ");

        Movie movie = new Movie(id, title, genre, runningtime, subtitles, director, casts, language, description, classification);
        movies.add(movie);
        saveMovies();
        System.out.println("Movie added successfully with ID: " + movie.getId() + "\n");
    }

    public void viewMovies() {
        System.out.println("\n======================================");
        System.out.println("=             Movie List             =");
        System.out.println("======================================\n");
        if (movies.isEmpty()) {
            System.out.println("No movies available.\n");
            return;
        }
        for (Movie movie : movies) {
            System.out.println(movie);
        }
        System.out.println();
    }

    private void saveMovies() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (Movie movie : movies) {
                writer.write(String.join("|", Arrays.asList(
                        movie.getId(),
                        // if no input, save N/A, else save the input
                        movie.getTitle().isEmpty() ? "N/A" : movie.getTitle(),
                        movie.getGenre().isEmpty() ? "N/A" : movie.getGenre(),
                        movie.getRunningTime().isEmpty() ? "N/A" : movie.getRunningTime(),
                        movie.getSubtitles().isEmpty() ? "N/A" : movie.getSubtitles(),
                        movie.getDirector().isEmpty() ? "N/A" : movie.getDirector(),
                        movie.getCasts().isEmpty() ? "N/A" : movie.getCasts(),
                        movie.getSpokenLanguage().isEmpty() ? "N/A" : movie.getSpokenLanguage(),
                        movie.getDescription().isEmpty() ? "N/A" : movie.getDescription(),
                        movie.getClassifications().isEmpty() ? "N/A" : movie.getClassifications()
                )));
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving movies: " + e.getMessage());
        }
    }

    private void loadMovies() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split("\\|", -1);
                if (data.length == 10) {
                    Movie movie = new Movie(
                            // if data is N/A, assign empty,else retrive the data
                            data[0].equals("N/A") ? "" : data[0],
                            data[1].equals("N/A") ? "" : data[1],
                            data[2].equals("N/A") ? "" : data[2],
                            data[3].equals("N/A") ? "" : data[3],
                            data[4].equals("N/A") ? "" : data[4],
                            data[5].equals("N/A") ? "" : data[5],
                            data[6].equals("N/A") ? "" : data[6],
                            data[7].equals("N/A") ? "" : data[7],
                            data[8].equals("N/A") ? "" : data[8],
                            data[9].equals("N/A") ? "" : data[9]
                    );
                    movies.add(movie);
                }
            }
        } catch (IOException e) {
            System.out.println("No previous movies found. Starting fresh.");
        }
    }

    public void updateMovie() {
        System.out.println("\n===========================");
        System.out.println("=       Update Movie      =");
        System.out.println("===========================\n");
        System.out.print("Enter the movie ID to update: ");
        String id = scanner.nextLine();

        for (Movie movie : movies) {
            if (movie.getId().equals(id)) {
                System.out.println("Editing movie: " + movie.getTitle());

                System.out.print("Enter new title (press Enter to keep current): ");
                String title = scanner.nextLine();
                if (!title.isEmpty()) {
                    movie.setTitle(title);
                }

                System.out.print("Enter new genre (press Enter to keep current): ");
                String genre = scanner.nextLine();
                if (!genre.isEmpty()) {
                    movie.setGenre(genre);
                }

                System.out.print("Enter new running time (press Enter to keep current): ");
                String runningTime = scanner.nextLine();
                if (!runningTime.isEmpty()) {
                    movie.setRunningTime(runningTime);
                }

                System.out.print("Enter new subtitles (press Enter to keep current): ");
                String subtitles = scanner.nextLine();
                if (!subtitles.isEmpty()) {
                    movie.setSubtitles(subtitles);
                }

                System.out.print("Enter new director (press Enter to keep current): ");
                String director = scanner.nextLine();
                if (!director.isEmpty()) {
                    movie.setDirector(director);
                }

                System.out.print("Enter new casts (press Enter to keep current): ");
                String casts = scanner.nextLine();
                if (!casts.isEmpty()) {
                    movie.setCasts(casts);
                }

                System.out.print("Enter new language (press Enter to keep current): ");
                String language = scanner.nextLine();
                if (!language.isEmpty()) {
                    movie.setSpokenLanguage(language);
                }

                System.out.print("Enter new description (press Enter to keep current): ");
                String des = scanner.nextLine();
                if (!des.isEmpty()) {
                    movie.setDescription(des);
                }

                System.out.print("Enter new classification (press Enter to keep current): ");
                String classification = scanner.nextLine();
                if (!classification.isEmpty()) {
                    movie.setClassifications(classification);
                }
                saveMovies();
                System.out.println("Movie updated successfully!\n");
                return;
            }
        }
        System.out.println("Movie not found.\n");
    }

    public void deleteMovie() {
        System.out.println("\n==========================");
        System.out.println("=      Delete Movie      =");
        System.out.println("==========================\n");
        viewMovies();
        System.out.print("Enter the movie ID to delete: ");
        String id = scanner.nextLine();

        Iterator<Movie> iterator = movies.iterator();
        while (iterator.hasNext()) {
            Movie movie = iterator.next();
            if (movie.getId().equals(id)) {
                iterator.remove();
                saveMovies();
                System.out.println("Movie deleted successfully!\n");
                return;
            }
        }
        System.out.println("Movie not found.\n");
    }

    public void searchMovie() {
        System.out.println("\n=========================");
        System.out.println("=      Search Movie     =");
        System.out.println("=========================\n");
        System.out.print("Enter the movie ID: ");
        String id = scanner.nextLine();

        for (Movie movie : movies) {
            if (movie.getId().equals(id)) {

                System.out.println("= Movie Found = " + movie);

                return;
            }
        }
        System.out.println("Movie not found.\n");
    }

    public Movie getMovieById(String id) {
        for (Movie m : movies) {
            if (m.getId().equals(id)) {
                return m;
            }
        }
        return null;
    }

    private String promptNonEmpty(String prompt) {
        String input;
        do {
            System.out.print(prompt);
            input = scanner.nextLine().trim();
            if (input.isEmpty()) {
                System.out.println("Input cannot be empty. Please try again.");
            }
        } while (input.isEmpty());
        return input;
    }

    public void run() {
        while (true) {
            System.out.println("\n=================================");
            System.out.println("=   Movie Management System     =");
            System.out.println("=================================\n");
            System.out.println("[1.] Add Movie");
            System.out.println("[2.] View Movies");
            System.out.println("[3.] Update Movie");
            System.out.println("[4.] Delete Movie");
            System.out.println("[5.] Search Movie");
            System.out.println("[0.] Back");
            System.out.print("Enter your choice: ");

            while (!scanner.hasNextInt()) {
                System.out.println("Invalid input! Please enter a number between 0 and 5.");
                scanner.next(); // Clear the invalid input
                System.out.print("Enter your choice: ");
            }

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    addMovie();
                    break;
                case 2:
                    viewMovies();
                    break;
                case 3:
                    updateMovie();
                    break;
                case 4:
                    deleteMovie();
                    break;
                case 5:
                    searchMovie();
                    break;
                case 0:
                    System.out.println("Return to main system...");
                    return;
                default:
                    System.out.println("Invalid choice! Please try again.");
            }
        }
    }
}
