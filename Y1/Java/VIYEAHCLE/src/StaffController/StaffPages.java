package StaffController;

import MainPage.MainPages;
import entity.Car;
import entity.Logo;
import entity.SaveProgress;
import entity.Staff;
import java.util.Scanner;

public class StaffPages extends Car
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

    //to print lines
    public void line(int amt)
    {
        System.out.print(" ");
        for(int i =0;i<amt;i++)
        {
            System.out.print("* ");
        }
        System.out.print("\n");
    }

    //its a menu for Staff Page
    public void staffMenu()
    {
        this.line(17);
        System.out.print("*                                 *\n");
        System.out.print("*   1.       ADD CAR LIST         *\n");
        System.out.print("*                                 *\n");
        this.line(17);
        System.out.print("*                                 *\n");
        System.out.print("*   2.      REMOVE CAR LIST       *\n");
        System.out.print("*                                 *\n");
        this.line(17);
        System.out.print("*                                 *\n");
        System.out.print("*   3.      CHECK CONDITION       *\n");
        System.out.print("*                                 *\n");
        this.line(17);
        System.out.print("*                                 *\n");
        System.out.print("*   4.       VIEW CAR LIST        *\n");
        System.out.print("*                                 *\n");
        this.line(17);
        System.out.print("*                                 *\n");
        System.out.print("*   5.            EXIT            *\n");
        System.out.print("*                                 *\n");
        this.line(17);
        System.out.print("\n");
    }

    public void zeroToExit()
    {
        Scanner scanner = new Scanner(System.in);
        String ans;

        while(true)
        {
            try 
            {
                System.out.print("PLEASE TYPE 0 TO EXIT : ");
                ans = scanner.nextLine();

                if(ans.equals("0"))
                {
                    this.askOpt();
                    break;
                }
                else
                {
                    System.out.println("INVALID INPUT~~ PLEASE ENTER AGAIN :)\n");
                    continue;
                }
                
            } 
            catch (Exception e) 
            {
                System.out.println(e.getMessage());
            }
            break;
        }
    }

    //to ask user still want to change or not
    public void confirmation(String prefix)
    {
        SaveProgress.outputCarList();
        Scanner scanner = new Scanner(System.in);
        String ans;
        StaffAddCar add = new StaffAddCar();
        StaffRemoveCar remove = new StaffRemoveCar();
        StaffCheckConditions condition = new StaffCheckConditions();
        
        while(true)
       {
            try
            {
                if(prefix.equals("CHECK"))
                {
                    System.out.print("STILL WANT TO CHANGE STATUS ? <y/n> : ");
                    ans = scanner.nextLine().toUpperCase().trim();
                    switch(ans)
                    {
                        case "Y":case "y":condition.checkConditionPage();break;
                        case "N":case "n":this.askOpt();break;
                        default:
                        System.out.println("INVALID INPUT~~~ PLEASE ENTER <y/n> :)\\n");
                        continue;
                    }
                }
                else if(prefix.equals("ADD"))
                {
                    System.out.print("STILL WANT TO ADD CAR LIST <y/n> : ");
                    ans = scanner.nextLine().toUpperCase().trim();
                    switch(ans)
                    {
                        case "Y":case "y":add.addCarList();break;
                        case "N":case "n":this.askOpt();break;
                        default:
                        System.out.println("INVALID INPUT~~~ PLEASE ENTER <y/n> :)\n");
                        continue;
                    }
                }
                else if(prefix.equals("REMOVE"))
                {
                    System.out.print("STILL WANT TO REMOVE CAR LIST <y/n> : ");
                    ans = scanner.nextLine().toUpperCase().trim();
                    switch(ans)
                    {
                        case "Y":case "y":remove.removeCarList();break;
                        case "N":case "n":this.askOpt();break;
                        default:
                        System.out.println("INVALID INPUT~~~ PLEASE ENTER <y/n> :)\\n");
                        continue;
                    }
                }
            }
            catch(Exception e)
            {
                System.out.println(e.getMessage());
                continue;
            }
            break;
        }
    }

    //methods for asking user which options want to proceed
    public void askOpt()
    {
        Scanner scanner = new Scanner(System.in);
        Logo staffLogoObj = new Logo();
        MainPages mainObj = new MainPages();
        StaffAddCar add = new StaffAddCar();
        StaffRemoveCar remove = new StaffRemoveCar();
        StaffCheckConditions condition = new StaffCheckConditions();
        StaffViewCars view = new StaffViewCars();

        this.clearScreen();

        staffLogoObj.staffLogo();

        System.out.println("LOGIN SUCCESSFUL~~~\n");
        this.staffMenu();

        while(true)
        {
            try
            {
                System.out.print("\nPLEASE ENTER ANY OPTION <1~5>: ");
                String optModify = scanner.nextLine();
                
                switch(optModify)
                {
                    case "1":add.addCarList();break;
                    case "2":remove.removeCarList();break;
                    case "3":condition.checkConditionPage();break;
                    case "4":view.viewCarList();break;
                    case "5":mainObj.loginPage();break;
                    default:
                    System.out.println("INVALID INPUT~~~ PLEASE ENTER <1 ~ 5> :)");
                    continue;
                }
            }
            catch(Exception e)
            {
                System.out.print("INVALID INPUT~~~ PLEASE ENTER <1 ~ 5> ;)");
            }
            break;
        }
    }

    public void StaffloginPage()
    {
        Staff staffObj = new Staff();
        Logo logoObj = new Logo();
        Scanner scanner = new Scanner(System.in);
        MainPages mainObj = new MainPages();

        String staffPin = staffObj.getStaffPin();
        String staffName = staffObj.getStaffName();

        this.clearScreen();

        logoObj.staffLogo();

        System.out.println("\n\n");

        while(true)
        {
            try
            {
                logoObj.staffUserName();
                System.out.print("PLEASE ENTER YOUR STAFF USERNAME <0 to exit> : ");
                String nameAns = scanner.nextLine();

                if(nameAns.equals("0"))
                {
                    mainObj.loginPage();
                    break;
                }
                if(!nameAns.equals(staffName))
                {
                    System.out.println("INVALID INPUT~~~ PLEASE ENTER THE CORRECT USERNAME :)\n");
                    continue;
                }
            }
            catch(Exception e)
            {
                System.out.println("INVALID INPUT~~~ PLEAS MAKE SURE USERNAME AND PASSWORD ARE BOTH CORRECT :)\n");
            }
            break;
        }

        while(true)
        {
            try
            {
                logoObj.staffUserPass();
                System.out.print("PLEASE ENTER YOUR STAFF PASSWORD <0 to exit> : ");
                String pinAns = scanner.nextLine();

                if(pinAns.equals("0"))
                {
                    mainObj.loginPage();
                    break;
                }
                if(!pinAns.equals(staffPin))
                {
                    System.out.println("INVALID INPUT~~~ PLEASE ENTER THE CORRECT PASSWORD :)\n");
                    continue;
                }
                else 
                {
                    this.askOpt();
                    break;
                }
            }
            catch(Exception e)
            {
                System.out.println(e.getMessage());
            }
            break;
        }
    }
}
