package models;

import utils.SeatLayout;

public class MovieSession {
    private final String movieTitle;
    private final String cinemaType;
    private final String showTime;
    private final SeatLayout seatLayout;

    public MovieSession(String movieTitle, String cinemaType, String showTime) {
        this.movieTitle = movieTitle;
        this.cinemaType = cinemaType;
        this.showTime = showTime;
        this.seatLayout = new SeatLayout();
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public String getCinemaType() {
        return cinemaType;
    }

    public String getShowTime() {
        return showTime;
    }

    public SeatLayout getSeatLayout() {
        return seatLayout;
    }
}