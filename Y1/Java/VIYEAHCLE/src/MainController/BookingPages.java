package MainController;

import MainPage.MainPages;
import entity.Car;
import entity.CarSpec;
import entity.Logo;
import entity.SaveProgress;
import java.util.Map;
import java.util.Scanner;

public class BookingPages extends Car
{
    //its a clear screen methods
    public void clearScreen()
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

    //its a method for print empty line
    public void emptyLength(int amt)
    {
        for(int i =0;i<amt;i++)
        {
            System.out.print(" "); 
        }
    }

    public void line(int amt)
    {
        System.out.print(" ");
        for(int i =0;i<amt;i++)
        {
            System.out.print("* ");
        }
        System.out.print("\n");
    }

    public void bookingMenu()
    {
        System.out.println("\n");
        this.emptyLength(13);
        this.line(15);
        this.emptyLength(13);
        System.out.print("*                             *\n");
        this.emptyLength(13);
        System.out.print("* 1.         ECONOMY          *\n");
        this.emptyLength(13);
        System.out.print("*                             *\n");
        this.emptyLength(13);
        this.line(15);
        this.emptyLength(13);
        System.out.print("*                             *\n");
        this.emptyLength(13);
        System.out.print("* 2.       INTERMEDIATE       *\n");
        this.emptyLength(13);
        System.out.print("*                             *\n");
        this.emptyLength(13);
        this.line(15);
        this.emptyLength(13);
        System.out.print("*                             *\n");
        this.emptyLength(13);
        System.out.print("* 3.          LUXURY          *\n");
        this.emptyLength(13);
        System.out.print("*                             *\n");
        this.emptyLength(13);
        this.line(15);
        this.emptyLength(13);
        System.out.print("*                             *\n");
        this.emptyLength(13);
        System.out.print("* 4.           EXIT           *\n");
        this.emptyLength(13);
        System.out.print("*                             *\n");
        this.emptyLength(13);
        this.line(15);
        this.emptyLength(13);
        System.out.print("\n");
    }

    public void chooseSpec(String prefix)
    {
        Car carObj = Car.getCarObj();
        Scanner scanner = new Scanner(System.in);
        RentalPages rentObj = new RentalPages();
        Map<String,CarSpec> carList = carObj.getCarList();
        Logo logoObj = new Logo();

        String chosenCar;

        while(true)
        {
            try
            {
                System.out.print("PLEASE SELECT ANY CAR THAT YOU LIKE <0 to exit> : ");
                chosenCar = scanner.nextLine().trim().toUpperCase();
                CarSpec carspec = carList.get(chosenCar);

                if(chosenCar.equals("0"))
                {
                    this.bookingOpt();
                    break;
                }

                if(!chosenCar.startsWith(prefix))
                {
                    System.out.println("INVALID INPUT~~~ PLEASE ENTER AGAIN :)\n");
                    continue;
                }
                else if(carspec.getCarStatus().equals("UNAVAILABLE"))
                {
                    System.out.println("THE CAR YOU\'VE CHOSE IS UNAVAILABLE :(");
                    System.out.println("PLEASE CHOOSE ANOTHER CAR :)\n");
                    continue;
                }
  
                this.clearScreen();

                if(prefix.equals("EC"))
                {
                    logoObj.ecoLogo();
                    System.out.println("YOU\'VE CHOSE "+carspec.getCarModels()+"~~~");
                    SaveProgress.saveCarList(carList);
                    rentObj.rentPage("ECONOMY",chosenCar);
                }
                else if(prefix.equals("IN"))
                {
                    logoObj.inteLogo();
                    System.out.println("YOU\'VE CHOSE "+carspec.getCarModels()+"~~~");
                    SaveProgress.saveCarList(carList);
                    rentObj.rentPage("INTERMEDIATE",chosenCar);
                }
                else if(prefix.equals("LU"))
                {
                    logoObj.luxLogo();
                    System.out.println("YOU\'VE CHOSE "+carspec.getCarModels()+"~~~");
                    SaveProgress.saveCarList(carList);
                    rentObj.rentPage("LUXURY",chosenCar);
                }
            }
            catch(Exception e)
            {
                System.out.println("INVALID INPUT~~~ PLEASE ENTER AGAIN :)\n");
                continue;
            }
            break;
        }
    }

    public void chooseClass(String prefix)
    {
        this.clearScreen();
        Logo logoObj = new Logo();

        if(prefix.equals("EC"))
        {
            logoObj.ecoLogo();
            System.out.println("YOU\'VE CHOSE ECONOMY CLASS~~~ WHICH IS A CLASS THAT PROVIDE PREMIUM EXPERIENCE :)\n");
            super.economyCarList();
            this.chooseSpec("EC");
        }
        else if(prefix.equals("IN"))
        {
            logoObj.inteLogo();
            System.out.println("YOU\'VE CHOSE INTERMEDIATE CLASS~~~ WHICH IS A CLASS THAT PROVIDE PREMIUM EXPERIENCE :)\n");
            super.intermediateCarList();
            this.chooseSpec("IN");
        }
        else if(prefix.equals("LU"))
        {
            logoObj.luxLogo();
            System.out.println("YOU\'VE CHOSE LUXURY CLASS~~~ WHICH IS A CLASS THAT PROVIDE PREMIUM EXPERIENCE :)\n");
            super.luxuryCarList();
            this.chooseSpec("LU");
        }
    }

    public void bookingOpt()
    {
        Scanner scanner = new Scanner(System.in);
        MainPages mainObj = new MainPages();
        Logo logoObj = new Logo();
        String ans;

        this.clearScreen();
        logoObj.bookingLogo();
        this.bookingMenu();

        while(true)
        {
            try
            {
                System.out.print("PLEASE SELECT ANY CLASS SHOWN ABOVE : ");
                ans = scanner.nextLine();

                switch(ans)
                {
                    case "1":this.chooseClass("EC");break;
                    case "2":this.chooseClass("IN");break;
                    case "3":this.chooseClass("LU");break;
                    case "4":mainObj.userOpt();break;
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
}


