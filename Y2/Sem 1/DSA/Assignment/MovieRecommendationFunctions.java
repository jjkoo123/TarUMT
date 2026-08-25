import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

/**
 * FUNCTION FILE
 *
 * Contains all Member 3 functions: BFS, DFS, genre search, similar-movie
 * search, recommendation, ranking, and neat console output.
 *
 * Graph representation:
 * movie ID -> set of directly similar movie IDs
 *
 * This is an adjacency list. It is memory-efficient because it only stores
 * relationships that actually exist between movies.
 */
public class MovieRecommendationFunctions {

    // Stores all movie vertices using movie ID as the key.
    private final Map<String, Movie> movies = new LinkedHashMap<>();

    // Adjacency list for the undirected similar-movie graph.
    private final Map<String, Set<String>> similarMovieGraph = new LinkedHashMap<>();

    // User ID -> set of movies watched by that user.
    private final Map<String, Set<String>> watchedMoviesByUser = new HashMap<>();

    /**
     * Adds a movie as a vertex in the graph and creates its empty adjacency list.
     */
    public void addMovie(Movie movie) {
        if (movie == null) {
            throw new IllegalArgumentException("Movie cannot be null.");
        }
        if (movies.containsKey(movie.getId())) {
            throw new IllegalArgumentException("Movie ID already exists: " + movie.getId());
        }

        movies.put(movie.getId(), movie);
        similarMovieGraph.put(movie.getId(), new LinkedHashSet<>());
    }

    /**
     * Adds an undirected edge between two movies.
     * Example: if Inception is similar to Interstellar, then Interstellar is
     * also similar to Inception.
     */
    public void addSimilarMovieConnection(String firstMovieId, String secondMovieId) {
        validateMovieExists(firstMovieId);
        validateMovieExists(secondMovieId);

        if (firstMovieId.equals(secondMovieId)) {
            throw new IllegalArgumentException("A movie cannot be connected to itself.");
        }

        similarMovieGraph.get(firstMovieId).add(secondMovieId);
        similarMovieGraph.get(secondMovieId).add(firstMovieId);
    }

    /**
     * Records that a user has watched a movie. A Set prevents duplicate entries.
     */
    public void markMovieAsWatched(String userId, String movieId) {
        validateMovieExists(movieId);
        watchedMoviesByUser
                .computeIfAbsent(userId, key -> new LinkedHashSet<>())
                .add(movieId);
    }

    /**
     * Finds all movies containing the selected genre. The comparison ignores
     * upper/lower case, so "sci-fi" and "SCI-FI" produce the same result.
     */
    public List<Movie> findMoviesByGenre(String genre) {
        List<Movie> results = new ArrayList<>();

        for (Movie movie : movies.values()) {
            for (String movieGenre : movie.getGenres()) {
                if (movieGenre.equalsIgnoreCase(genre)) {
                    results.add(movie);
                    break;
                }
            }
        }

        results.sort(Comparator.comparing(Movie::getTitle));
        return results;
    }

    /**
     * Performs Breadth-First Search (BFS) from a starting movie.
     *
     * BFS uses a Queue. It visits direct neighbours first, then movies two
     * connections away, then three connections away. This is the main
     * algorithm for the recommendation system because closer movies are more
     * likely to be relevant recommendations.
     *
     * Time complexity: O(V + E)
     * Space complexity: O(V)
     */
    public List<Movie> breadthFirstSearch(String startMovieId) {
        validateMovieExists(startMovieId);

        List<Movie> traversalOrder = new ArrayList<>();
        Set<String> visited = new HashSet<>();
        Queue<String> queue = new ArrayDeque<>();

        visited.add(startMovieId);
        queue.offer(startMovieId);

        while (!queue.isEmpty()) {
            String currentMovieId = queue.poll();
            traversalOrder.add(movies.get(currentMovieId));

            for (String neighbourId : similarMovieGraph.get(currentMovieId)) {
                if (!visited.contains(neighbourId)) {
                    visited.add(neighbourId);
                    queue.offer(neighbourId);
                }
            }
        }

        return traversalOrder;
    }

    /**
     * Performs Depth-First Search (DFS) from a starting movie.
     *
     * DFS explores one path as far as possible before backtracking. It is
     * included to satisfy the assignment traversal requirement. BFS is still
     * used as the main recommendation algorithm.
     *
     * Time complexity: O(V + E)
     * Space complexity: O(V)
     */
    public List<Movie> depthFirstSearch(String startMovieId) {
        validateMovieExists(startMovieId);

        List<Movie> traversalOrder = new ArrayList<>();
        Set<String> visited = new HashSet<>();
        depthFirstSearchHelper(startMovieId, visited, traversalOrder);
        return traversalOrder;
    }

    /**
     * Recursive helper for DFS. Marking a movie as visited before checking its
     * neighbours prevents an infinite loop when the graph contains a cycle.
     */
    private void depthFirstSearchHelper(
            String currentMovieId,
            Set<String> visited,
            List<Movie> traversalOrder) {

        visited.add(currentMovieId);
        traversalOrder.add(movies.get(currentMovieId));

        for (String neighbourId : similarMovieGraph.get(currentMovieId)) {
            if (!visited.contains(neighbourId)) {
                depthFirstSearchHelper(neighbourId, visited, traversalOrder);
            }
        }
    }

    /**
     * Returns movies directly connected to the selected movie in the graph.
     * Example: display movies similar to Inception.
     */
    public List<Movie> findSimilarMovies(String movieId) {
        validateMovieExists(movieId);

        List<Movie> similarMovies = new ArrayList<>();
        for (String neighbourId : similarMovieGraph.get(movieId)) {
            similarMovies.add(movies.get(neighbourId));
        }
        return similarMovies;
    }

    /**
     * Main BFS-based recommendation function.
     *
     * Scoring rules:
     * - +3 points for each genre matching a genre the user has watched.
     * - +4 points when directly similar to a watched movie (1 graph link).
     * - +2 points when connected through one other similar movie (2 links).
     * - +1 point when the movie rating is 4.0 or above.
     *
     * Important: watched movies are skipped and never appear in the result.
     */
    public List<Recommendation> recommendMovies(String userId, int maximumResults) {
        if (maximumResults <= 0) {
            throw new IllegalArgumentException("Maximum results must be greater than 0.");
        }

        Set<String> watchedMovieIds = watchedMoviesByUser.getOrDefault(
                userId, Collections.emptySet());

        if (watchedMovieIds.isEmpty()) {
            return Collections.emptyList();
        }

        Map<String, Integer> genrePreferenceCount = buildGenrePreferenceCount(watchedMovieIds);
        Map<String, Integer> closestSimilarityDistance = new HashMap<>();

        // Run limited BFS from each watched movie to find nearby candidates.
        for (String watchedMovieId : watchedMovieIds) {
            Map<String, Integer> distances = findBfsDistances(watchedMovieId, 2);

            for (Map.Entry<String, Integer> entry : distances.entrySet()) {
                String candidateId = entry.getKey();
                int candidateDistance = entry.getValue();

                // Distance 0 is the watched movie itself, so do not use it.
                if (candidateDistance > 0) {
                    closestSimilarityDistance.merge(
                            candidateId, candidateDistance, Math::min);
                }
            }
        }

        List<Recommendation> recommendations = new ArrayList<>();

        for (Movie candidate : movies.values()) {
            // Required rule: never recommend a movie the user already watched.
            if (watchedMovieIds.contains(candidate.getId())) {
                continue;
            }

            int score = 0;
            List<String> reasons = new ArrayList<>();

            int sharedGenreCount = calculateSharedGenreCount(candidate, genrePreferenceCount);
            if (sharedGenreCount > 0) {
                score += sharedGenreCount * 3;
                reasons.add("genre match");
            }

            Integer distance = closestSimilarityDistance.get(candidate.getId());
            if (distance != null) {
                if (distance == 1) {
                    score += 4;
                    reasons.add("1-link similar");
                } else if (distance == 2) {
                    score += 2;
                    reasons.add("2-link similar");
                }
            }

            if (candidate.getRating() >= 4.0) {
                score++;
                reasons.add("high rating");
            }

            if (score > 0) {
                recommendations.add(new Recommendation(candidate, score, reasons));
            }
        }

        // Highest score appears first. Rating and title are used to break ties.
        recommendations.sort(Comparator
                .comparingInt(Recommendation::getScore).reversed()
                .thenComparing((Recommendation item) -> item.getMovie().getRating(),
                        Comparator.reverseOrder())
                .thenComparing(item -> item.getMovie().getTitle()));

        return recommendations.subList(0, Math.min(maximumResults, recommendations.size()));
    }

    /**
     * Uses BFS to find the shortest distance from startMovieId to nearby movies.
     * Only two levels are explored to keep recommendations relevant.
     */
    private Map<String, Integer> findBfsDistances(String startMovieId, int maximumDepth) {
        Map<String, Integer> distances = new HashMap<>();
        Queue<String> queue = new ArrayDeque<>();

        distances.put(startMovieId, 0);
        queue.offer(startMovieId);

        while (!queue.isEmpty()) {
            String currentMovieId = queue.poll();
            int currentDistance = distances.get(currentMovieId);

            if (currentDistance == maximumDepth) {
                continue;
            }

            for (String neighbourId : similarMovieGraph.get(currentMovieId)) {
                if (!distances.containsKey(neighbourId)) {
                    distances.put(neighbourId, currentDistance + 1);
                    queue.offer(neighbourId);
                }
            }
        }

        return distances;
    }

    /**
     * Counts genres from the user's watched movies. These counts represent the
     * user's genre preferences and are used by the recommendation algorithm.
     */
    private Map<String, Integer> buildGenrePreferenceCount(Set<String> watchedMovieIds) {
        Map<String, Integer> genrePreferenceCount = new HashMap<>();

        for (String watchedMovieId : watchedMovieIds) {
            Movie watchedMovie = movies.get(watchedMovieId);
            for (String genre : watchedMovie.getGenres()) {
                genrePreferenceCount.merge(genre.toLowerCase(), 1, Integer::sum);
            }
        }

        return genrePreferenceCount;
    }

    /**
     * Counts how many candidate genres match the user's watched genres.
     */
    private int calculateSharedGenreCount(
            Movie candidate,
            Map<String, Integer> genrePreferenceCount) {

        int sharedGenreCount = 0;
        for (String genre : candidate.getGenres()) {
            if (genrePreferenceCount.containsKey(genre.toLowerCase())) {
                sharedGenreCount++;
            }
        }
        return sharedGenreCount;
    }

    private void validateMovieExists(String movieId) {
        if (!movies.containsKey(movieId)) {
            throw new IllegalArgumentException("Movie does not exist: " + movieId);
        }
    }

    /**
     * Prints an aligned movie table for BFS, DFS, genre search, or similar-movie search.
     */
    public void printMovieList(String heading, List<Movie> movieList) {
        printSectionTitle(heading);

        if (movieList.isEmpty()) {
            System.out.println("No movies found.");
            return;
        }

        String border = "+-----+------+------------------------------+------------------------------+--------+";
        System.out.println(border);
        System.out.printf("| %-3s | %-4s | %-28s | %-28s | %-6s |%n",
                "No.", "ID", "Title", "Genres", "Rating");
        System.out.println(border);

        for (int index = 0; index < movieList.size(); index++) {
            Movie movie = movieList.get(index);
            String genres = String.join(", ", movie.getGenres());

            System.out.printf("| %-3d | %-4s | %-28s | %-28s | %6.1f |%n",
                    index + 1,
                    movie.getId(),
                    trimText(movie.getTitle(), 28),
                    trimText(genres, 28),
                    movie.getRating());
        }
        System.out.println(border);
    }

    /**
     * Prints ranking results in a neat table for screenshots and demonstration.
     */
    public void printRecommendations(List<Recommendation> recommendations) {
        printSectionTitle("Recommended Movies");

        if (recommendations.isEmpty()) {
            System.out.println("No recommendations are available. Add watched movies first.");
            return;
        }

        String border = "+-----+------------------------------+-------+----------------------------------------------------------+";
        System.out.println(border);
        System.out.printf("| %-3s | %-28s | %-5s | %-56s |%n",
                "No.", "Recommended Movie", "Score", "Reason");
        System.out.println(border);

        for (int index = 0; index < recommendations.size(); index++) {
            Recommendation item = recommendations.get(index);
            String reason = String.join(", ", item.getReasons());

            System.out.printf("| %-3d | %-28s | %5d | %-56s |%n",
                    index + 1,
                    trimText(item.getMovie().getTitle(), 28),
                    item.getScore(),
                    trimText(reason, 56));
        }
        System.out.println(border);
    }

    private void printSectionTitle(String heading) {
        String line = "=====================================================================================================";
        System.out.println("\n" + line);
        System.out.println("  " + heading.toUpperCase());
        System.out.println(line);
    }

    private String trimText(String text, int maximumLength) {
        if (text.length() <= maximumLength) {
            return text;
        }
        return text.substring(0, maximumLength - 3) + "...";
    }
}
