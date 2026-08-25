package StaffController;
import MainController.BookingPages;
import entity.Car;
import entity.CarSpec;
import entity.Logo;
import entity.SaveProgress;
import java.util.Map;
import java.util.Scanner;

public class StaffAddCar extends Car
{
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

    public String generateCode(Map<String,CarSpec> carList,String prefix)
    {
        SaveProgress.outputCarList();
        int maxNum = 0;
        int newCode;
        String prefixs = prefix;

        if(!carList.containsKey("EC0001")||!carList.containsKey("IN0001")||!carList.containsKey("LU0001"))
        {
            newCode = 0001;
        }
        else
        {
            for(String i:carList.keySet())
            {
                if(i.startsWith(prefix))
                {
                    /*because my code is 'EC0001',
                    so i seperate the alphabet and number,starts with 2 index place,
                    and use the number add 1 to generate new code */
                    int extractNum = Integer.parseInt(i.substring(2));
                    if(extractNum>maxNum)
                    {
                        maxNum = extractNum;
                    }
                } 
            }
        newCode = maxNum+1;
        }
        return String.format("%s%04d",prefixs,newCode);
    }

    public void addUserInput(String carClass,String prefix)
    {
        SaveProgress.outputCarList();
        Car carObj = Car.getCarObj();
        Map<String,CarSpec> carList = carObj.getCarList();
        StaffPages staffObj = new StaffPages();
        Scanner scanner = new Scanner(System.in);
        String carSeatss="",carCapacitys="",carModelss="";
        while(true)
        {
            try
            {
                System.out.print("PLEASE ENTER CAR MODEL\'S NAME <0 to exit> : ");
                carModelss = scanner.nextLine().trim().toUpperCase();
    
                if(carModelss.equals("0"))
                {
                    staffObj.askOpt();
                    break;
                }

                boolean carExists = false;
                for(CarSpec car: carList.values())
                {
                    //is to compare whether the car exists by using equalsIgnoreCase (str1.equalsIgnoreCase.(str2)) to compare
                    if(car.getCarModels().equalsIgnoreCase(carModelss))
                    {
                        carExists = true;
                        break;
                    }
                }
                if(carExists)
                {
                    System.out.println("PLEASE DON\'T ENTER THE SAME CAR MODEL NAME :)\n");
                    continue;
                }
                else if(carModelss.length()<10)
                {
                    System.out.println("PLEASE AT LEAST ENTER 10 CHARACTERS :)\n");
                    continue;
                }
                else if(carModelss.isEmpty())
                {
                    System.out.println("PLEASE DON\'T LEAVE IT EMPTY :)\n");
                    continue;
                }
                else if(carModelss.length()>20)
                {   
                    System.out.println("CAR MODEL'S NAME CAN\'T BE MORE THAN 20 WORDS :)\n");
                    continue;
                }
            }
            catch(Exception e)
            {
                System.out.println(e.getMessage());
            }
            break;
        }

        while(true)
        {
            try
            {
                System.out.print("PLEASE ENTER CAR\'S SEATS <MAXIMUM 7 SEATS> <0 to exit> : ");
                carSeatss = scanner.nextLine().trim();

                if(carSeatss.equals("0"))
                {
                    staffObj.askOpt();
                    break;
                }

                if(!carSeatss.matches("\\d+"))
                {
                    System.out.println("PLEASE ENTER INTEGER VALUE :)\n");
                    continue;
                }
    
                int seats = Integer.parseInt(carSeatss);
                if(seats > 7 || seats < 2)
                {
                    System.out.println("PLEASE ENTER AT LEAST 2 TO 7 SEATS :)\n");
                    continue;
                }
            }
            catch(Exception e)
            {
                System.out.println(e.getMessage());
            }
            break;
        }

        while(true)
        {
            try
            {
                System.out.print("PLEASE ENTER CAR\'S LUGGAGE CAPACITY <MAXIMUM 5 BAGS> <0 to exit> : ");
                carCapacitys = scanner.nextLine().trim();

                if(carCapacitys.equals("0"))
                {
                    staffObj.askOpt();
                    break;
                }

                if(!carCapacitys.matches("\\d+"))
                {
                    System.out.println("PLEASE ENTER INTEGER VALUE :)\n");
                    continue;
                }
                int bags = Integer.parseInt(carCapacitys);
                if(bags>5)
                {
                    System.out.println("PLEASE DON\'T ENTER MORE THAN 5 BAGS :)\n");
                    continue;
                }
                else
                {
                    break;
                }
            }
            catch(Exception e)
            {
                System.out.println(e.getMessage());
            }
            break;
        }
            String carCode = this.generateCode(carList,prefix);
            carList.put(carCode, new CarSpec(carClass,carSeatss,carCapacitys,carModelss,"AVAILABLE"));
            SaveProgress.saveCarList(carList);

            System.out.println("NEW CAR LIST BEING ADDED SUCCESSFULLY~~~\n");
            staffObj.confirmation("ADD");
    }

    public void addCarList()
    {
        SaveProgress.outputCarList();
        Scanner scanner = new Scanner(System.in);
        BookingPages bookingObj = new BookingPages();
        StaffPages staffObj = new StaffPages();
        Logo staffLogoObj = new Logo();

        this.clearScreen();

        staffLogoObj.addLogo();
        bookingObj.bookingMenu();

        String ans;

        while(true)
        {
            try
            {
                System.out.print("WHICH ONE OF THE CLASS YOU WANNA TO ADD ? : ");
                ans = scanner.nextLine();
                switch(ans)
                {
                    case "1":this.addCarPage("EC");break;
                    case "2":this.addCarPage("IN");break;
                    case "3":this.addCarPage("LU");break;
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

        //add car page 
    public void addCarPage(String prefix)
    {
        SaveProgress.outputCarList();
        Car carObj = Car.getCarObj();
        Map<String,CarSpec> carList = carObj.getCarList();
        Logo logoObj = new Logo();

        this.clearScreen();

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
            this.addUserInput("ECONOMY","EC");
        }
        else if(prefix.equals("IN"))
        {
            logoObj.inteLogo();
            System.out.println("\nCURRENT CAR LIST~~");
            super.intermediateCarList();
            this.addUserInput("INTERMEDIATE","IN");
        }
        else if(prefix.equals("LU"))
        {
            logoObj.luxLogo();
            System.out.println("\nCURRENT CAR LIST~~");
            super.luxuryCarList();
            this.addUserInput("LUXURY","LU");
        }
    }
}
