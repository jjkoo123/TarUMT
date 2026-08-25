package StaffController;

import entity.Car;
import entity.CarSpec;
import java.util.Map;
import java.util.TreeMap;

public class StaffViewCars extends Car
{
    public void carLines()
    {
        for(int i =0;i<97;i++)
        {
            System.out.print("-");
        }
        System.out.println();
    } 

    public void clear()
    {
        try 
        {
            if (System.getProperty("os.name").toLowerCase().contains("win")) {
                //Clear screen for Windows
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                //Clear screen for MacOs
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } 
        catch (Exception e) {
            e.printStackTrace();
        }
    }
        
    public void viewCarList()
    {
        StaffPages staffObj = new StaffPages();
        Car carObj = Car.getCarObj();
        Map<String,CarSpec> carList = carObj.getCarList();
        TreeMap<String,CarSpec> sortedCarList = new TreeMap<>(carList);

        this.clear();

        this.carLines();
        super.carColumns();
        this.carLines();

        for(Map.Entry<String,CarSpec> entry : sortedCarList.entrySet())
        {
            System.out.printf("| %-5s ",entry.getKey());
            System.out.println(entry.getValue());
            this.carLines();
        }

        staffObj.zeroToExit();
    }
}
