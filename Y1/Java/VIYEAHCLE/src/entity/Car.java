package entity;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class Car
{
    private static Car carObj;//singleton pattern's object
    private HashMap <String,CarSpec> carList;

    //constructor for class Car
    public Car()
    {
        carList = SaveProgress.outputCarList(); // Load from text file
        if (carList.isEmpty()) {
            defaultModels(); // If no saved progress, use defaults
        }
    }

    public void defaultModels()
    {
        carList.put("EC0001",new CarSpec("ECONOMY","5","2","PERODUA MYVI","AVAILABLE"));
        carList.put("EC0002",new CarSpec("ECONOMY","5","2","PERODUA BEZZA","AVAILABLE"));
        carList.put("LU0001",new CarSpec("LUXURY","5","2","MERCEDES C CLASS","AVAILABLE"));
        carList.put("LU0002",new CarSpec("LUXURY","5","2","MERCEDES G CLASS","AVAILABLE"));
        carList.put("IN0001",new CarSpec("INTERMEDIATE","5","2","HONDA CITY","AVAILABLE"));
        carList.put("IN0002",new CarSpec("INTERMEDIATE","5","2","PROTON X70","AVAILABLE"));
    }

    /*singleton pattern for creating object for car list,
    to make sure every carlist is the same in every class*/
    public static Car getCarObj()
    {
        if (carObj == null)
        {
            carObj = new Car();
        }

        return carObj;
    }

    //to return car list's value
    public Map<String,CarSpec> getCarList()
    {
        return carList;
    }

    //to print lines
    public void ecoLines()
    {
        for(int i =0;i<97;i++)
        {
            System.out.print("-");
        }
        System.out.println();
    }

    //to print lines
    public void inteLines()
    {
        for(int i =0;i<97;i++)
        {
            System.out.print("-");
        }
        System.out.println();
    }

    //methods for print economy's column
    public void ecoColumns()
    {
        System.out.println("|  CODE  |  CAR\'S CLASS   | CAR\'S SEATS | LUGGAGE CAPACITY |     CAR\'S MODEL      |   STATUS    |");
    }

    public void carColumns()
    {
        System.out.println("|  CODE  |  CAR\'S CLASS   | CAR\'S SEATS | LUGGAGE CAPACITY |     CAR\'S MODEL      |   STATUS    |");
    }

    //methods for print intermediate's column
    public void inteColumns()
    {
        System.out.println("|  CODE  |  CAR\'S CLASS   | CAR\'S SEATS | LUGGAGE CAPACITY |     CAR\'S MODEL      |   STATUS    |");
    }

    //its a economoy car list methods
    public void economyCarList()
    {
        Car carObj = Car.getCarObj();
        //to get latest car list
        Map<String,CarSpec> carList = carObj.getCarList();
        //treemap is to sort the carlist that inside the hashmap
        TreeMap<String,CarSpec> sortedCarList = new TreeMap<>(carList);//to sort the car list
        this.ecoLines();
        this.ecoColumns();
        this.ecoLines();
                /*entrySet is a method from Map,
        to return whole set of carlist(key,value)*/ 
        for(Map.Entry<String,CarSpec> entry : sortedCarList.entrySet())
        {
            if(entry.getKey().startsWith("EC"))
            {
                System.out.printf("| %-5s ",entry.getKey());
                System.out.println(entry.getValue());
                this.ecoLines();
            }
        }
        System.out.print("\n");
    }


    //its a intermediate car list methods
    public void intermediateCarList()
    {
        Car carObj = Car.getCarObj();
        //to get latest car list
        Map<String,CarSpec> carList = carObj.getCarList();
        //treemap is to sort the carlist that inside the hashmap
        TreeMap<String,CarSpec> sortedCarList = new TreeMap<>(carList);//to sort the car list
        this.inteLines();
        this.inteColumns();
        this.inteLines();
        /*entrySet is a method from Map,
        to return whole set of carlist(key,value)*/ 
        for(Map.Entry<String,CarSpec> entry : sortedCarList.entrySet())
        {
            if(entry.getKey().startsWith("IN"))
            {
                System.out.printf("| %-5s ",entry.getKey());
                System.out.println(entry.getValue());
                this.inteLines();
            }
        }
        System.out.print("\n");
    }

    //its a luxury car list methods
    public void luxuryCarList()
    {   
        Car carObj = Car.getCarObj();
        //to get latest car list
        Map<String,CarSpec> carList = carObj.getCarList();
        //treemap is to sort the carlist that inside the hashmap
        TreeMap<String,CarSpec> sortedCarList = new TreeMap<>(carList);//to sort the car list
        this.ecoLines();
        this.ecoColumns();
        this.ecoLines();
        /*entrySet is a method from Map,
        to return whole set of carlist(key,value)*/ 
        for(Map.Entry<String,CarSpec> entry : sortedCarList.entrySet())
        {
            if(entry.getKey().startsWith("LU"))
            {
                System.out.printf("| %-5s ",entry.getKey());
                System.out.println(entry.getValue());
                this.ecoLines();
            }
        }
        System.out.print("\n");
    }
}
