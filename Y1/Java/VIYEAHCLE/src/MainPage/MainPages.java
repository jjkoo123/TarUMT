package MainPage;

import MainController.*;
import StaffController.StaffPages;
import entity.Logo;
import java.util.Scanner;

public class MainPages {

    //its a clear screen methods
    public void clearScreen() {
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
    public void emptyLength(int amt) {
        for (int i = 0; i < amt; i++) {
            System.out.print(" ");
        }
    }

    public void line(int amt) {
        System.out.print(" ");
        for (int i = 0; i < amt; i++) {
            System.out.print("* ");
        }
        System.out.print("\n");
    }

    //its a method for main menu
    public void userOptions() {
        //main menu options
        System.out.println("\n\n                            \u2764 MAIN MENU \u2764");
        this.emptyLength(20);
        this.line(14);
        this.emptyLength(20);
        System.out.print("*                           *\n");
        this.emptyLength(20);
        System.out.print("* 1.     BOOKING PAGE       *\n");
        this.emptyLength(20);
        System.out.print("*                           *\n");
        this.emptyLength(20);
        this.line(14);
        this.emptyLength(20);
        System.out.print("*                           *\n");
        this.emptyLength(20);
        System.out.print("* 2.      BOOKED CAR        *\n");
        this.emptyLength(20);
        System.out.print("*                           *\n");
        this.emptyLength(20);
        this.line(14);
        this.emptyLength(20);
        System.out.print("*                           *\n");
        this.emptyLength(20);
        System.out.print("* 3.    PENALTY RECORD      *\n");
        this.emptyLength(20);
        System.out.print("*                           *\n");
        this.emptyLength(20);
        this.line(14);
        this.emptyLength(20);
        System.out.print("*                           *\n");
        this.emptyLength(20);
        System.out.print("* 4.         EXIT           *\n");
        this.emptyLength(20);
        System.out.print("*                           *\n");
        this.emptyLength(20);
        this.line(14);
    }

    //its a login page menu
    public void loginOptions() {
        System.out.println("\n\n");
        this.emptyLength(5);
        this.line(14);
        this.emptyLength(5);
        System.out.print("*                           *\n");
        this.emptyLength(5);
        System.out.print("* 1.         USER           *\n");
        this.emptyLength(5);
        System.out.print("*                           *\n");
        this.emptyLength(5);
        this.line(14);
        this.emptyLength(5);
        System.out.print("*                           *\n");
        this.emptyLength(5);
        System.out.print("* 2.         STAFF          *\n");
        this.emptyLength(5);
        System.out.print("*                           *\n");
        this.emptyLength(5);
        this.line(14);
        this.emptyLength(5);
        System.out.print("*                           *\n");
        this.emptyLength(5);
        System.out.print("* 3.         EXIT           *\n");
        this.emptyLength(5);
        System.out.print("*                           *\n");
        this.emptyLength(5);
        this.line(14);
    }

    //its a method for user page
    public void userOpt() {
        this.clearScreen();

        Logo mainLogoObj = new Logo();
        BookingPages bookingObj = new BookingPages();//object for accesing BookingPage class's methods and attributes 
        BookedPages bookedObj = new BookedPages();//object for accesing Payment class's methods and attributes 
        PenaltyPages penaObj = new PenaltyPages();//object for accesing RentalPage class's methods and attributes 

        mainLogoObj.mainLogo();//outputs for system's logo

        this.userOptions();//outputs for options

        System.out.println("\n\n*** WELCOME TO VIYEAHCLE RENTAL SYSTEM ***\n");//welcome message

        //ask for choices
        Scanner scanner = new Scanner(System.in);

        while (true) {
            try {
                System.out.print("\nPLEASE CHOOSE ANY OPTION INDICATES ABOVE : ");
                String mainAns = scanner.nextLine();

                switch (mainAns) {
                    case "1":
                        bookingObj.bookingOpt();
                        break;
                    case "2":
                        bookedObj.bookedHistory();
                        break;
                    case "3":
                        penaObj.penaltyPage();
                        break;
                    case "4":
                        this.loginPage();
                        break;
                    default:
                        System.out.println("\nINVALID INPUT~~~ PLEASE CHOOSE BETWEEN <1 ~ 4> :)");
                        continue;
                }
            } catch (Exception e) {
                System.out.println("\nERROR: sINVALID INPUT~~~ PLEASE CHOOSE BETWEEN <1 ~ 5> ;)" + e);
                continue;
            }
            break;//to end the while loop
        }
    }

    //its a login page
    public void loginPage() {
        Logo logo = new Logo();
        StaffPages staffObj = new StaffPages();
        Scanner scanner = new Scanner(System.in);
        String ans;

        this.clearScreen();
        logo.loginLogo();
        this.loginOptions();

        System.out.println("\n\n*** WELCOME TO VIYEAHCLE RENTAL SYSTEM ***\n");

        while (true) {
            try {
                System.out.print("ARE YOU A USER OR A STAFF ? : ");
                ans = scanner.nextLine();

                switch (ans) {
                    case "1":
                        this.userOpt();
                        break;
                    case "2":
                        staffObj.StaffloginPage();
                        break;
                    case "3":
                        logo.exitLogo();
                        System.exit(0);
                        break;
                    default:
                        System.out.println("PLEASE CHOOSE EITHER <1~3> :)\n");
                        continue;
                }
            } catch (Exception e) {
                System.out.println("PLEASE CHOOSE EITHER <1~3> :)\n");
                continue;
            }
            break;
        }
    }

    //main methods
    public static void main(String[] args) {
        MainPages mainObj = new MainPages();
        mainObj.loginPage();
    }
}
