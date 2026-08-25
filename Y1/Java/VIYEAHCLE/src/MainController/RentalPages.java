package MainController;

import MainPage.MainPages;
import entity.Car;
import entity.CarSpec;
import entity.RentalDetails;
import entity.RentalPrice;
import entity.SaveProgress;
import entity.TempKeys;
import entity.TempValue;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Map;
import java.util.Scanner;

public class RentalPages extends RentalDetails
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

    public void loadProgress()
    {
        SaveProgress.outputCarList();
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

    public LocalDateTime chosenSpec(Map<String,TempValue> templist,String tempCode,LocalDateTime startDate)
    {
        TempValue tempValue = templist.get(tempCode);
        Car carObj = Car.getCarObj();
        Map<String,CarSpec> carList = carObj.getCarList();
        CarSpec carSpec = carList.get(tempValue.getChosenCar());
        LocalDateTime endDate = startDate;
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        int intDuration = Integer.valueOf(tempValue.getDuration());

        this.line(30);
        System.out.printf("*                         *                                  *%n");
        System.out.printf("*       RENTER NAME       *  %-30s  *%n",tempValue.getUsername());
        System.out.printf("*                         *                                  *%n");
        this.line(30);
        System.out.printf("*                         *                                  *%n");
        System.out.printf("*      EMAIL ADDRESS      *  %-30s  *%n",tempValue.getEmail());
        System.out.printf("*                         *                                  *%n");
        this.line(30);
        System.out.printf("*                         *                                  *%n");
        System.out.printf("*        LICENSE NO       *  %-30s  *%n",tempValue.getLicenseNum());
        System.out.printf("*                         *                                  *%n");
        this.line(30);
        System.out.printf("*                         *                                  *%n");
        System.out.printf("*        CAR MODEL        *  %-30s  *%n",carSpec.getCarModels());
        System.out.printf("*                         *                                  *%n");
        this.line(30);
        if(tempValue.getDuPrefix().equals("DAY"))
        {
            endDate = startDate.plusDays(intDuration);
            System.out.printf("*                         *                                  *%n");
            System.out.printf("*        DURATION         *  %-30s  *%n",intDuration+" DAYS");
            System.out.printf("*                         *                                  *%n");
            this.line(30);
            System.out.printf("*                         *                                  *%n");
            System.out.printf("*       START DATE        *  %-30s  *%n",startDate.format(format));
            System.out.printf("*                         *                                  *%n");
            this.line(30);
            System.out.printf("*                         *                                  *%n");
            System.out.printf("*        END DATE         *  %-30s  *%n",endDate.format(format)); 
            System.out.printf("*                         *                                  *%n");
            this.line(30);
        }
        else if(tempValue.getDuPrefix().equals("WEEK"))
        {
            endDate = startDate.plusWeeks(intDuration);
            System.out.printf("*                         *                                  *%n");
            System.out.printf("*        DURATION         *  %-30s  *%n",intDuration+" WEEKS");
            System.out.printf("*                         *                                  *%n");
            this.line(30);
            System.out.printf("*                         *                                  *%n");
            System.out.printf("*       START DATE        *  %-30s  *%n",startDate.format(format));
            System.out.printf("*                         *                                  *%n");
            this.line(30);
            System.out.printf("*                         *                                  *%n");
            System.out.printf("*        END DATE         *  %-30s  *%n",endDate.format(format)); 
            System.out.printf("*                         *                                  *%n");
            this.line(30);
        }
        else if(tempValue.getDuPrefix().equals("MONTH"))
        {
            endDate = startDate.plusMonths(intDuration);
            System.out.printf("*                         *                                  *%n");
            System.out.printf("*        DURATION         *  %-30s  *%n",intDuration+" MONTHS");
            System.out.printf("*                         *                                  *%n");
            this.line(30);
            System.out.printf("*                         *                                  *%n");
            System.out.printf("*       START DATE        *  %-30s  *%n",startDate.format(format));
            System.out.printf("*                         *                                  *%n");
            this.line(30);
            System.out.printf("*                         *                                  *%n");
            System.out.printf("*        END DATE         *  %-30s  *%n",endDate.format(format)); 
            System.out.printf("*                         *                                  *%n");
            this.line(30);
        }
        return endDate;
    }

    public void confirmation(Map<String,TempValue> templist,String tempCode)
    {
        TempValue tempValue = templist.get(tempCode);
        Scanner scanner = new Scanner(System.in);
        PaymentPages paymentObj = new PaymentPages();
        MainPages mainObj = new MainPages();
        DateTimeFormatter datetimeFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String startDates = tempValue.getStartDates();
        LocalDateTime ldStartdate = LocalDateTime.parse(startDates,datetimeFormat);
        LocalDateTime endDates = this.chosenSpec(templist,tempCode,ldStartdate);
        String endDate = endDates.format(datetimeFormat);


        this.clearScreen();
        System.out.println("HERE\'S YOUR CHOSEN SPECS AND RENT DURATION~~~ :)");
        this.chosenSpec(templist,tempCode,ldStartdate);

        System.out.println("\n            *** IMPORTANT NOTES ***");

        String ans;

        while(true)
        {
            try
            {
                System.out.println("PLEASE CHECK BEFORE PROCEED TO PAYMENT GATEWAY :)\n");

                System.out.print("DOES THE INFORMATION SHOWN ABOVE MEET YOUR EXPECTATION ? <y=YES> <n=NO> : ");
                ans = scanner.nextLine().trim().toUpperCase();

                switch(ans)
                {
    
                    case "Y":
                    tempValue.setStartDates(startDates);
                    tempValue.setEndDates(endDate);
                    templist.put(tempCode,tempValue);
                    paymentObj.payDeposit(templist,tempCode);break;
                    case "N":mainObj.userOpt();break;
                    default:
                    System.out.println("INVALID INPUT~~~ PLEASE ENTER <y/n> ONLY~~~ :)\n");
                    continue;
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

    public void enterIdentity(Map<String,TempValue> templist,String tempCode)
    {
        TempValue tempValue = templist.get(tempCode);
        Scanner scanner = new Scanner(System.in);
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("HH:mm");
        DateTimeFormatter datetimeFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDate date = null;
        LocalTime time = null;
        String username,email,licenseNum;

        while(true)
        {   
            try
            {   
                System.out.print("\nPLEASE ENTER THE DATE YOU WANT TO RENT <YYYY-MM-DD> <e.g. 2025-12-20> :");
                String setDate = scanner.nextLine();
                if(setDate.isEmpty())
                {
                    System.out.println("PLEASE DON\'T LEAVE IT EMPTY :)\n");
                    continue;
                }
                else
                {
                    date = LocalDate.parse(setDate,dateFormat);
                }

            }
            catch(DateTimeParseException e)
            {
                System.out.println("INVALID FORMAT~~~ PLEASE ENTER WITH YYYT-MM-DD FORMAT (e.g. 2025-12-20)");
                System.out.println("MAKE SURE YOUR MONTH DOESN\'T MORE THAN 12 MONTHS");
                System.out.println("AND THE DAY SHOULD LESS THAN OR EQUALS TO 31 :)\n");
                System.out.println(e.getMessage());
                continue;
            }
            break;
        }

        while(true)
        {
            try
            {
                System.out.print("\nPLEASE ENTER THE TIME YOU WANT TO RENT <HH:MM> <e.g. 14:00> :");
                String setTime = scanner.nextLine();
                if(setTime.isEmpty())
                {
                    System.out.println("PLEASE DON\'T LEAVE IT EMPTY :)\n");
                    continue;
                }
                else
                {
                    time = LocalTime.parse(setTime,timeFormat);
                }
            }
            catch(DateTimeParseException e)
            {
                System.out.println("INVALID FORMAT~~~ PLEASE ENTER WITH HH:MM FORMAT (e.g. 14:00)");
                System.out.println("MAKE SURE THE HOUR NOT MORE THAN 24 HOURS AND MINUTES NOT MORE THAN 60 MINUTES :)\n");
                continue;
            }
            break;
        }

        LocalDateTime startDate = LocalDateTime.of(date,time);

        while(true)
        {
            try
            {
                System.out.print("\nPLEASE ENTER YOUR NAME <maximum 15 characters> : ");
                username = scanner.nextLine().trim().toUpperCase();
                if(username.length()>15)
                {
                    System.out.println("INVALID INPUT~~~ MAXIMUM CHARACTERS IS 15 ONLY :)\n");
                    continue;
                }
                else if(username.isEmpty())
                {
                    System.out.println("PLEASE DON\'T LEAVE IT EMPTY :)\n");
                    continue;
                }
            }
            catch(Exception e)
            {
                System.out.println("INVALID INPUT~~~ PLEASE ENTER AGAIN :)\n");
                continue;
            }
            break;
        }

        while(true)
        {
            try
            {
                System.out.print("\nPLEASE ENTER YOUR EMAIL ADDRESS <maximum 20 characters> <e.g. xxxx@gmail.com> : ");
                email = scanner.nextLine().trim().toLowerCase();
                if(!email.endsWith("@gmail.com"))
                {
                    System.out.println("PLEASE MAKE SURE YOUR EMAIL IS ENDS WITH @GMAIL.COM\n");
                    continue;
                }
                else if(email.isEmpty())
                {
                    System.out.println("PLEASE DON\'T LEAVE IT EMPTY :)\n");
                    continue;
                }
                else if(email.length()>30)
                {
                    System.out.println("PLEASE DON\'T ENTER MORE THAN 20 CHARACTERS :)\n");
                    continue;
                }
                
            }
            catch(Exception e)
            {
                System.out.println(e.getMessage());
                continue;
            }
            break;
        }

        while(true)
        {
            try
            {
                System.out.print("\nPLEASE ENTER YOUR LICENSE NUMBER <e.g. 8 digit numbers> : ");
                licenseNum = scanner.nextLine().trim();

                if(licenseNum.isEmpty())
                {
                    System.out.println("PLEASE DON\'T LEAVE IT EMPTY :)\n");
                    continue;
                }

                if(!licenseNum.matches("[0-9]+"))
                {
                    System.out.println("\nPLEASE MAKE SURE LICENSE NUMBERS ONLY CONTAIN NUMBERS :)\n");
                    continue;
                }
                
                if(licenseNum.length()!=8)
                {
                    System.out.println("PLEASE ENTER EXACTLY 8 DIGIT NUMBERS :)\n");
                    continue;
                }

                String startDates = startDate.format(datetimeFormat);
                tempValue.setStartDates(startDates);
                tempValue.setUsername(username);
                tempValue.setEmail(email);
                tempValue.setLicenseNum(licenseNum);
                templist.put(tempCode,tempValue);
                this.confirmation(templist,tempCode);
                break;
            }
            catch(Exception e)
            {
                System.out.println(e.getMessage());
            }
            break;
        }
    }

    public void rentDuration(Map<String,TempValue> templist,String tempCode)
    {
        TempValue tempValue = templist.get(tempCode);
        Scanner scanner = new Scanner(System.in); 
        int duration;

        while(true)
        {
            try
            {
                String ans;
                if(tempValue.getDuPrefix().equals("DAILY"))
                {
                    System.out.print("\nPLEASE ENTER HOW MANY DAYS THAT YOU LIKE TO RENT <maximum 30 days> : ");
                    ans = scanner.nextLine().trim();
                    if(ans.isEmpty())
                    {
                        System.out.println("PLEASE DON\'T LEAVE IT EMPTY :)\n");
                        continue;
                    }
                    duration = Integer.parseInt(ans);
                    if(duration > 30||duration <1)
                    {
                        System.out.println("PLEASE ENTER 1 to 30 DAYS :)\n");
                        continue;
                    }
                    else
                    {
                        tempValue.setDuPrefix("DAY");
                        tempValue.setDuration(ans);
                        templist.put(tempCode,tempValue);
                        this.enterIdentity(templist,tempCode);
                        break;
                    }
                }
                else if(tempValue.getDuPrefix().equals("WEEKLY"))
                {
                    System.out.print("\nPLEASE ENTER HOW MANY WEEKS THAT YOU LIKE TO RENT <maximum 8 weeks> : ");
                    ans = scanner.nextLine().trim();
                    if(ans.isEmpty())
                    {
                        System.out.println("PLEASE DON\'T LEAVE IT EMPTY :)\n");
                        continue;
                    }
                    duration = Integer.parseInt(ans);
                    if(duration > 8||duration <1)
                    {
                        System.out.println("PLEASE DON\'T ENTER MORE THAN 8 WEEKS :)\n");
                        continue;
                    }
                    else
                    {
                        tempValue.setDuPrefix("WEEK");
                        tempValue.setDuration(ans);
                        templist.put(tempCode,tempValue);
                        this.enterIdentity(templist,tempCode);
                        break;
                    }
                }
                else if(tempValue.getDuPrefix().equals("MONTHLY"))
                {
                    System.out.print("\nPLEASE ENTER HOW MANY MONTHS THAT YOU LIKE TO RENT <maximum 4 months> : ");
                    ans = scanner.nextLine().trim();
                    if(ans.isEmpty())
                    {
                        System.out.println("PLEASE DON\'T LEAVE IT EMPTY :)\n");
                        continue;
                    }
                    duration = Integer.parseInt(ans);
                    if(duration > 4||duration <1)
                    {
                        System.out.println("PLEASE DON\'T ENTER MORE THAN 4 MONTHS :)\n");
                        continue;
                    }
                    else if(duration==0)
                    {
                        System.out.println("PLEASE DON\'T LEAVE IT EMPTY :)\n");
                        continue;
                    }
                    else
                    {
                        tempValue.setDuPrefix("MONTH");
                        tempValue.setDuration(ans);
                        templist.put(tempCode,tempValue);
                        this.enterIdentity(templist,tempCode);
                        break;
                    }
                }
            }
            catch(Exception e)
            {
                System.out.println("INVALID INPUT~~~ PLEASE ENTER AGAIN~~~\n");
                System.out.println(e.getMessage());
                scanner.nextLine();
                continue;
            }
            break;
        }
    }

    public void rentPage(String classPrefix,String chosenCar)
    {
        this.loadProgress();
        MainPages mainObj = new MainPages();
        Scanner scanner = new Scanner(System.in);
        RentalDetails rentObj = RentalDetails.getRentalObj();
        Map<String,RentalPrice> rentlist= rentObj.getRentList();
        TempKeys tempObj = TempKeys.getTempKeyObj();
        Map<String,TempValue> templist = tempObj.getTempList();
        String tempCode = "TEST";
        String ans;

        System.out.println("YOU\'VE CHOSE "+classPrefix+" CLASS~~~ DIAGRAM BELOW ARE THE PRICES :)\n");

        if(classPrefix.equals("ECONOMY"))
        {
            this.ecoRent(); 
        }
        else if(classPrefix.equals("INTERMEDIATE"))
        {
            this.inteRent();
        }
        else if(classPrefix.equals("LUXURY"))
        {
            this.luxRent();
        }

        while(true)
        {
            try
            {
                System.out.print("\nPLEASE CHOOSE ANY PERIOD OF TIME <d = Daily> <w = Weekly> <m = Monthly> <0 to exit> : ");
                ans = scanner.nextLine().trim().toUpperCase();
                RentalPrice rentPrice = rentlist.get(classPrefix);
                double rate;
                switch(ans)
                {
                    case "D":rate = rentPrice.getDailyPrice();
                             String rateDaily = String.valueOf(rate);
                             templist.put(tempCode,new TempValue(classPrefix, "", "", "DAILY", 
                             "", "", chosenCar, "", "", "", rateDaily));
                             this.rentDuration(templist,tempCode);
                             break;
                    case "W":rate = rentPrice.getWeeklyPrice();
                             String rateWeekly = String.valueOf(rate);
                             templist.put(tempCode,new TempValue(classPrefix, "", "", "WEEKLY", 
                             "", "", chosenCar, "", "", "", rateWeekly));
                             this.rentDuration(templist,tempCode);
                             break;
                    case "M":rate = rentPrice.getMonthlyPrice();
                             String rateMonthly = String.valueOf(rate);
                             templist.put(tempCode,new TempValue(classPrefix, "", "", "MONTHLY", 
                             "", "", chosenCar, "", "", "", rateMonthly));
                             this.rentDuration(templist,tempCode);
                             break;
                    case "0":mainObj.userOpt();
                             break;
                    default:
                    System.out.println("INVALID INPUT~~~ PLEASE CHOOSE GIVEN OPTIONS\n");
                    continue;
                }
            }
            catch(Exception e)
            {
                System.out.println("INVALID INPUT~~~ PLEASE CHOOSE GIVEN OPTIONS\n");
                continue;
            }
            break;
        }
    }
}
