package StaffController;

import MainController.BookingPages;
import entity.Car;
import entity.CarSpec;
import entity.Logo;
import entity.SaveProgress;
import java.util.Map;
import java.util.Scanner;

public class StaffRemoveCar extends Car
{
    public void removeUserInput(String prefix)
    {
        SaveProgress.outputCarList();
        Scanner scanner = new Scanner(System.in);
        StaffPages staffObj = new StaffPages();
        Car carObj = Car.getCarObj();
        Map<String,CarSpec> carList = carObj.getCarList();

        String ans;
        while(true)
        {
            try
            {
                System.out.print("PLEASE ENTER THE CODE THAT YOU WANNA TO REMOVE <0 to exit> : ");
                ans = scanner.nextLine().trim().toUpperCase();

                if(ans.equals("0"))
                {
                    this.removeCarList();
                    break;
                }

                if(!ans.startsWith(prefix))
                {
                    System.out.println("PLEASE ENTER THE CODE THAT STARTS WITH "+prefix+" :)\n");
                    continue;
                }
                else if(!carList.containsKey(ans))
                {
                    System.out.println("PLEASE ENTER CODE THAT SHOWN ABOVE ONLY :)\n");
                    continue;
                }
                else if(ans.isEmpty())
                {
                    System.out.println("PLEASE DON\'T LEAVE IT EMPTY :)\n");
                    continue;
                }
                CarSpec removedKey = carList.get(ans);
                String removedCar = removedKey.getCarModels();
                carList.remove(ans);
                SaveProgress.saveCarList(carList);
                System.out.println("YOU\'VE REMOVED <"+removedCar+"> SUCCESSFULLY~~~\n");
                staffObj.confirmation("REMOVE");
                break;
            }
            catch(Exception e)
            {
                System.out.println("PLEASE ENTER CODE THAT SHOWN ABOVE ONLY :)\n");
            }
            break;
        }
    }

    public void removeCarList()
    {
        SaveProgress.outputCarList();
        Scanner scanner = new Scanner(System.in);
        BookingPages bookingObj = new BookingPages();
        StaffPages staffObj = new StaffPages();
        Logo staffLogoObj = new Logo();

        staffObj.clearScreen();

        staffLogoObj.removeLogo();
        bookingObj.bookingMenu();

        String ans;

        while(true)
        {
            try
            {
                System.out.print("WHICH ONE OF THE CLASS YOU WANNA TO REMOVE ? : ");
                ans = scanner.nextLine();
                switch(ans)
                {
                    case "1":this.removeCarPage("EC");break;
                    case "2":this.removeCarPage("IN");break;
                    case "3":this.removeCarPage("LU");break;
                    case "4":staffObj.askOpt();break;
                    default:
                    System.out.println("PLEASE CHOOSE BETWEEN <1~4> :)\n");
                    continue;
                }
            }
            catch(Exception e)
            {
                System.out.println("PLEASE CHOOSE BETWEEN <1~4> :)\n");
                continue;
            }
            break;
        }
    }  

    public void removeCarPage(String prefix)
    {
        SaveProgress.outputCarList();
        Car carObj = Car.getCarObj();
        Map<String,CarSpec> carList = carObj.getCarList();
        Logo logoObj = new Logo();
        StaffPages staffObj = new StaffPages();

        staffObj.clearScreen();

        if(carList.isEmpty())
        {
            logoObj.emptyLogo();
            System.out.println("THE CAR LIST IS EMPTY~~~ PLEASE ADD SOME NEW CAR :)\n");
        }

        if(prefix.equals("EC"))
        {
            logoObj.ecoLogo();
            System.out.println("\nCURRENT CAR LIST~~");
            super.economyCarList();
            this.removeUserInput("EC");
        }
        else if(prefix.equals("IN"))
        {
            logoObj.inteLogo();
            System.out.println("\nCURRENT CAR LIST~~");
            super.intermediateCarList();
            this.removeUserInput("IN");
        }
        else if(prefix.equals("LU"))
        {
            logoObj.luxLogo();
            System.out.println("\nCURRENT CAR LIST~~");
            super.luxuryCarList();
            this.removeUserInput("LU");
        }
    }
}
