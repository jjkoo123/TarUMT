import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * MAIN FILE
 *
 * Starts the program, prepares sample data, and calls the functions from
 * MovieRecommendationFunctions. When your team combines all components,
 * replace the sample data with your menu and shared data from other members.
 */
public class MovieRecommendationMain {

    public static void main(String[] args) {
        MovieRecommendationFunctions system = new MovieRecommendationFunctions();

        printProgramTitle();
        loadSampleMovies(system);
        loadSampleConnections(system);
        loadSampleWatchHistory(system);

        // Demonstrate all functions required for Member 3.
        system.printMovieList("BFS Traversal Starting from Inception",
                system.breadthFirstSearch("M01"));

        system.printMovieList("DFS Traversal Starting from Inception",
                system.depthFirstSearch("M01"));

        system.printMovieList("Movies in the Sci-Fi Genre",
                system.findMoviesByGenre("sci-fi"));

        system.printMovieList("Movies Directly Similar to Inception",
                system.findSimilarMovies("M01"));

        // Uses BFS as the main recommendation algorithm.
        system.printRecommendations(system.recommendMovies("U01", 5));
    }

    /**
     * Adds the sample movie vertices. Each movie has an ID, title, genres, and rating.
     */
    private static void loadSampleMovies(MovieRecommendationFunctions system) {
        system.addMovie(new Movie("M01", "Inception", setOf("Sci-Fi", "Thriller"), 4.8));
        system.addMovie(new Movie("M02", "Interstellar", setOf("Sci-Fi", "Drama"), 4.7));
        system.addMovie(new Movie("M03", "The Matrix", setOf("Sci-Fi", "Action"), 4.6));
        system.addMovie(new Movie("M04", "John Wick", setOf("Action", "Thriller"), 4.3));
        system.addMovie(new Movie("M05", "Avengers: Endgame", setOf("Action", "Sci-Fi"), 4.5));
        system.addMovie(new Movie("M06", "The Dark Knight", setOf("Action", "Crime"), 4.8));
        system.addMovie(new Movie("M07", "Toy Story", setOf("Animation", "Family"), 4.2));
        system.addMovie(new Movie("M08", "Coco", setOf("Animation", "Family"), 4.7));
        system.addMovie(new Movie("M09", "The Martian", setOf("Sci-Fi", "Adventure"), 4.4));
        system.addMovie(new Movie("M10", "A Quiet Place", setOf("Horror", "Thriller"), 4.1));
    }

    /**
     * Adds sample undirected graph edges that connect similar movies.
     */
    private static void loadSampleConnections(MovieRecommendationFunctions system) {
        system.addSimilarMovieConnection("M01", "M02");
        system.addSimilarMovieConnection("M01", "M03");
        system.addSimilarMovieConnection("M01", "M09");
        system.addSimilarMovieConnection("M01", "M10");
        system.addSimilarMovieConnection("M02", "M05");
        system.addSimilarMovieConnection("M03", "M05");
        system.addSimilarMovieConnection("M03", "M06");
        system.addSimilarMovieConnection("M04", "M06");
        system.addSimilarMovieConnection("M07", "M08");
    }

    /**
     * User U01 has watched Inception and John Wick. These movies must not
     * appear in the recommendation result.
     */
    private static void loadSampleWatchHistory(MovieRecommendationFunctions system) {
        system.markMovieAsWatched("U01", "M01");
        system.markMovieAsWatched("U01", "M04");
    }

    private static void printProgramTitle() {
        System.out.println("=====================================================================================================");
        System.out.println("                    MOVIE RECOMMENDATION SYSTEM - MEMBER 3 DEMO");
        System.out.println("=====================================================================================================");
    }

    /**
     * Helper method to make genre sets easier to read when creating sample data.
     */
    private static Set<String> setOf(String... values) {
        Set<String> result = new LinkedHashSet<>();
        Collections.addAll(result, values);
        return result;
    }
}
