package entity;

public class CarSpec
{
    private String carClass;
    private String carSeats;
    private String carCapacity;
    private String carModels; 
    private String carStatus;

    public CarSpec(String carClass,String carSeats,String carCapacity,String carModels,String carStatus)
    {
        this.carClass =  carClass;
        this.carSeats = carSeats;
        this.carCapacity = carCapacity;
        this.carModels = carModels;
        this.carStatus = carStatus;
    }

    public String carToCSV()
    {
        return carClass + "," + carSeats + "," + carCapacity + "," + carModels + "," + carStatus;
    }

    public static CarSpec fromCSV(String csv) {
        String[] parts = csv.split(",");
        return new CarSpec(parts[0], parts[1], parts[2], parts[3], parts[4]);
    }

    public String getCarClass()
    {
        return carClass;
    }

    public String getCarSeats()
    {
        return carSeats;
    }

    public String getCarCapacity()
    {
        return carCapacity;
    }

    public String getCarModels()
    {
        return carModels;
    }
    
    public String getCarStatus()
    {
        return carStatus;
    }

    public void setCarStatus(String carStatus)
    {
        this.carStatus = carStatus;
    }

    @Override 
    //to display car spec
    public String toString()
    {
        String output = String.format("| %-14s | %-11s | %-16s | %-20s | %-11s |",carClass,carSeats+" SEATS",carCapacity+" BAGS",carModels,carStatus);
        return output;
    }   
}
