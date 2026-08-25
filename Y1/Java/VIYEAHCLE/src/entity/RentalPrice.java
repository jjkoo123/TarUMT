package entity;

public class RentalPrice 
{
    private double dailyPrice;
    private double weeklyPrice;
    private double monthlyPrice;

    public RentalPrice(double dailyPrice,double weeklyPrice,double monthlyPrice)
    {
        this.dailyPrice = dailyPrice;
        this.weeklyPrice = weeklyPrice;
        this.monthlyPrice = monthlyPrice;
    }

    public double getDailyPrice()
    {
        return dailyPrice;
    }

    public double getWeeklyPrice()
    {
        return weeklyPrice;
    }

    public double getMonthlyPrice()
    {
        return monthlyPrice;
    }

    public void setDailyPrice(double newDailyPrice)
    {
        this.dailyPrice = newDailyPrice;
    }

    public void setWeeklyPrice(double newWeeklyPrice)
    {
        this.dailyPrice = newWeeklyPrice;
    }

    public void setMonthlyPrice(double newMonthlyPrice)
    {
        this.dailyPrice = newMonthlyPrice;
    }

    @Override
    public String toString()
    {
        return String.format("| RM%-10.2f | RM%-10.2f | RM%-10.2f |",dailyPrice,weeklyPrice,monthlyPrice);
    }
}
