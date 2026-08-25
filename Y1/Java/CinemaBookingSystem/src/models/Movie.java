package models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Movie {
    private final String title;
    private final String genre;
    private final String cinemaType;
    private final int durationMins;
    private final List<String> showTimes;
    private final double price;

    public Movie(String title, String genre, String cinemaType, int durationMins, List<String> showTimes, double price) {
        this.title = title;
        this.genre = genre;
        this.cinemaType = cinemaType;
        this.durationMins = durationMins;
        this.showTimes = new ArrayList<>(showTimes);
        this.price = price;
    }

    public String getTitle() {
        return title;
    }

    public String getGenre() {
        return genre;
    }

    public String getCinemaType() {
        return cinemaType;
    }

    public int getDurationMins() {
        return durationMins;
    }

    public List<String> getShowTimes() {
        return Collections.unmodifiableList(showTimes);
    }
    public double getPrice() {
        return price;
    }
}
