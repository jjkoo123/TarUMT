package models;

public class Booking {
    private final String username;
    private final String movieTitle;
    private final String cinemaType;
    private final String showTime;
    private final int row;
    private final int col;
    private final String paymentMethod;
    private final double price;

    public Booking(String username, String movieTitle, String cinemaType, String showTime, int row, int col, String paymentMethod,
            double price) {
        this.username = username;
        this.movieTitle = movieTitle;
        this.cinemaType = cinemaType;
        this.showTime = showTime;
        this.row = row;
        this.col = col;
        this.paymentMethod = paymentMethod;
        this.price = price;
    }

    public String getUsername() {
        return username;
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

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public double getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return "Movie: " + movieTitle + " (" + cinemaType + ") | Time: " + showTime + " | Seat: " + (char) ('A' + row) + (col + 1);
    }
}
