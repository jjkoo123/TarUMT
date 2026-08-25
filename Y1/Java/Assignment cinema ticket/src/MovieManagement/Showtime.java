/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package MovieManagement;

import java.time.LocalDateTime;
import java.io.Serializable;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author User
 */
public class Showtime implements Serializable {

    private static final long serialVersionUID = 1L;
    private String id;
    private LocalDateTime dateTime;
    private Hall hall;
    private String movieId;

    private transient MovieController movieController;

    public Showtime(String id, LocalDateTime dateTime, Hall hall, String movieId) {
        this.id = id;
        this.dateTime = dateTime;
        this.hall = hall;
        this.movieId = movieId;
    }

    public Showtime(String id, LocalDateTime dateTime, Hall hall, Movie movie) {
        this.id = id;
        this.dateTime = dateTime;
        this.hall = hall;
        this.movieId = movie.getId();
    }

    public Showtime() {
    }

    public String getId() {
        return id;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public Hall getHall() {
        return hall;
    }

    public String getMovieId() {
        return movieId;
    }

    public Movie getMovie() {
        if (movieController != null) {
            return movieController.getMovieById(movieId);
        }
        return null;
    }

    public void setMovieController(MovieController movieController) {
        this.movieController = movieController;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public void setHall(Hall hall) {
        this.hall = hall;
    }

    @Override
    public String toString() {
        String hallType;
        switch (hall.getClass().getSimpleName()) {
            case "ThreeDHall":
                hallType = "3D";
                break;
            case "IMAXHall":
                hallType = "IMAX";
                break;
            case "NormalHall":
                hallType = "Normal";
                break;
            default:
                hallType = "Unknown";
                break;
        }

        String formattedDateTime = dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));

        String movieTitle = "Unknown";
        if (movieController != null) {
            Movie movie = movieController.getMovieById(movieId);
            if (movie != null) {
                movieTitle = movie.getTitle();
            }
        }

        return "====================================\n"
                + "Showtime ID   : " + id + "\n"
                + "Date & Time   : " + formattedDateTime + "\n"
                + "Hall Type     : " + hallType + "\n"
                + "Hall Name     : " + hall.getHallName() + "\n"
                + "Movie Title   : " + movieTitle + "\n"
                + "====================================";
    }

}
