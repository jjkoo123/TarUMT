package entity;

import java.util.HashMap;
import java.util.Map;

public class RentalDetails
{
    private static RentalDetails rentalObj;
    private HashMap <String,RentalPrice> rentList; 

    public RentalDetails()
    {
        rentList = new HashMap<>();
        rentList.put("ECONOMY",new RentalPrice(90.00,600.00,1900.00));
        rentList.put("INTERMEDIATE",new RentalPrice(130.00,900.00,2600.00));
        rentList.put("LUXURY",new RentalPrice(450.00,2200.00,7000.00));

    }

    public static RentalDetails getRentalObj()
    {
        if (rentalObj == null)
        {
            rentalObj = new RentalDetails();
        }

        return rentalObj;
    }

    //to print lines
    public void lines()
    {
        for(int i =0;i<64;i++)
        {
            System.out.print("-");
        }
        System.out.println();
    }

    public void columns()
    {
        System.out.println("| CAR\'S CLASS     | DAILY        | WEEKLY       | MONTHLY      |");
    }

    public Map<String,RentalPrice> getRentList()
    {
        return rentList;
    }

    public void ecoRent()
    {
        RentalDetails rentalObj = RentalDetails.getRentalObj();
        Map<String,RentalPrice> rentList = rentalObj.getRentList();
        this.lines();
        this.columns();
        this.lines();

        for(Map.Entry<String,RentalPrice> entry : rentList.entrySet())
        {
            if(entry.getKey().startsWith("ECONOMY"))
            {
                System.out.printf("| %-15s ",entry.getKey());
                System.out.println(entry.getValue());
                this.lines();
            }
        }
    }

    public void inteRent()
    {
        RentalDetails rentalObj = RentalDetails.getRentalObj();
        Map<String,RentalPrice> rentList = rentalObj.getRentList();
        this.lines();
        this.columns();
        this.lines();

        for(Map.Entry<String,RentalPrice> entry : rentList.entrySet())
        {
            if(entry.getKey().startsWith("INTERMEDIATE"))
            {
                System.out.printf("| %-15s ",entry.getKey());
                System.out.println(entry.getValue());
                this.lines();
            }
        }
    }

    public void luxRent()
    {
        RentalDetails rentalObj = RentalDetails.getRentalObj();
        Map<String,RentalPrice> rentList = rentalObj.getRentList();
        this.lines();
        this.columns();
        this.lines();

        for(Map.Entry<String,RentalPrice> entry : rentList.entrySet())
        {
            if(entry.getKey().startsWith("LUXURY"))
            {
                System.out.printf("| %-15s ",entry.getKey());
                System.out.println(entry.getValue());
                this.lines();
            }
        }
    }
}
