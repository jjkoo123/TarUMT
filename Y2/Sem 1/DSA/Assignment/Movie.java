import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * DATA FILE
 *
 * Stores the Movie object. Each movie is a vertex in the movie graph.
 */
public class Movie {
    private final String id;
    private final String title;
    private final Set<String> genres;
    private final double rating;

    public Movie(String id, String title, Set<String> genres, double rating) {
        this.id = id;
        this.title = title;
        this.genres = new LinkedHashSet<>(genres);
        this.rating = rating;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public Set<String> getGenres() {
        // Prevent other classes from changing this movie's genres by accident.
        return Collections.unmodifiableSet(genres);
    }

    public double getRating() {
        return rating;
    }
}

/**
 * DATA FILE
 *
 * Stores one recommended movie, its score, and the reasons it was suggested.
 * This class is in the same file because it is also only a data object.
 */
class Recommendation {
    private final Movie movie;
    private final int score;
    private final List<String> reasons;

    public Recommendation(Movie movie, int score, List<String> reasons) {
        this.movie = movie;
        this.score = score;
        this.reasons = new ArrayList<>(reasons);
    }

    public Movie getMovie() {
        return movie;
    }

    public int getScore() {
        return score;
    }

    public List<String> getReasons() {
        return Collections.unmodifiableList(reasons);
    }
}
