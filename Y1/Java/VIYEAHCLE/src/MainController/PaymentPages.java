package MainController;

import MainPage.MainPages;
import entity.Car;
import entity.CarSpec;
import entity.Logo;
import entity.PayList;
import entity.PaymentDetail;
import entity.PenaltyDetail;
import entity.Penaltys;
import entity.SaveProgress;
import entity.TempValue;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;

public class PaymentPages extends PayList
{   
    public void loadProgress()
    {
        SaveProgress.outputPayment();
    }

    public double calTotal(double duration,double rate)
    {
        double total = duration * rate;
        return total;
    }

    public double calWithTax(double total)
    {
        double tax = 0.06;
        double totalTax = total * tax;
        return totalTax;
    }

    public String generateCode(Map<String,PaymentDetail> payList,String prefix)
    {
        int maxNum = 0;
        int newCode;
        String prefixs = prefix;

        if(!payList.containsKey("RC0001"))
        {
            newCode = 0001;
        }
        else
        {
            for(String i:payList.keySet())
            {
                if(i.startsWith(prefix))
                {
                    /*the receipt code is 'RC0001',
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

    public String classType(String prefix)
    {
        if(prefix.startsWith("EC"))
        {
            return "ECONOMY";
        }
        else if(prefix.startsWith("IN"))
        {
            return "INTERMEDIATE";
        }
        else if(prefix.startsWith("LU"))
        {
            return "LUXURY";
        }
        return "NOTHING";
    }

    public void zeroExit()
    {
        Scanner scanner = new Scanner(System.in);
        MainPages mainObj = new MainPages();
        String ans;

        while(true)
        {
            try
            {
                System.out.print("PLEASE ENTER 0 TO EXIT : ");
                ans = scanner.nextLine().trim().toUpperCase();
                if(ans.equals("0"))
                {
                    mainObj.userOpt();
                    break;
                }
                else
                {
                    System.out.println("INVALID INPUT~~~ PLEASE ENTER 0 :)\n");
                    continue;
                }
            }
            catch(Exception e)
            {
                System.out.println("INVALID INPUT~~~ PLEASE ENTER 0 :)\n");
            }
            break;
        }
    }

    public double calcuFinalAmt(double duration,double rate)
    {
        double tax = duration*rate*0.06;
        double total = tax + (duration*rate);
        double deposit = total * 0.20;
        double ans = total - deposit;
        return ans;
    }

    public double calcuDeposit(double total)
    {
        return total * 0.20;
    }

    public void deposit(String startDate,String endDate,String carModel,double finalAmount)
    {
        double deposit = calcuDeposit(finalAmount);
        String formatdeposit = String.format("%.2f",deposit);
        String formatfinalAmt = String.format("%.2f",finalAmount);

        super.clear();

        super.lines(100);
        super.emptyline();
        System.out.println("|  ██████  ███████ ██████   ██████  ███████ ██ ████████                                            |");
        System.out.println("|  ██   ██ ██      ██   ██ ██    ██ ██      ██    ██                                               |");
        System.out.println("|  ██   ██ █████   ██████  ██    ██ ███████ ██    ██                                               |");
        System.out.println("|  ██   ██ ██      ██      ██    ██      ██ ██    ██                                               |");
        System.out.println("|  █████   ███████ ██       ██████  ███████ ██    ██                                               |");                                  
        super.emptyline();
        super.lines(100);
        System.out.println("|        START DATE         |         END DATE         |                CAR\'S MODEL                |");
        super.lines(100);
        System.out.printf("|    %-20s   |   %-20s   |                %-25s  |%n",startDate,endDate,carModel); 
        super.lines(100); 
        System.out.printf("|                                                                          SUBTOTAL : RM %-10s|%n",formatfinalAmt);
        super.lines(100); 
        System.out.printf("|   TOTAL AMOUNT ( WITH 6 PERCENTS TAXES)                                             RM %-10s|%n",formatfinalAmt);
        System.out.printf("|   DEPOSIT ( 20 PERCENTS )                                                           RM %-10s|%n",formatdeposit);
        super.lines(100);
        System.out.printf("|   TOTAL CHARGES                                                                     RM %-10s|%n",formatdeposit);
        super.lines(100);

        System.out.print("\n");

    }

    public void penalty(String penaltyListCode)
    {
        super.clear();
        SaveProgress.outputPenalty();

        Penaltys penalObj = Penaltys.getPenaltyObj();
        Map<String,PenaltyDetail> penaltyList = penalObj.getPenaltyList();
        PenaltyDetail penaldetail = penaltyList.get(penaltyListCode);

        String finalAmount = penaldetail.getFinalAmount();
        String penalAmount = penaldetail.getPenaltyAmt();

        double doubleFinal = Double.parseDouble(finalAmount);
        double doublePenal = Double.parseDouble(penalAmount);

        String formatFinal = String.format("%.2f",doubleFinal);
        String formatPenal = String.format("%.2f",doublePenal);

        super.lines(100);
        super.emptyline();
        System.out.println("|  ██████  ███████ ███    ██  █████  ██      ████████ ██    ██                                     |");
        System.out.println("|  ██   ██ ██      ████   ██ ██   ██ ██         ██     ██  ██                                      |");
        System.out.println("|  ██████  █████   ██ ██  ██ ███████ ██         ██      ████                                       |");
        System.out.println("|  ██      ██      ██  ██ ██ ██   ██ ██         ██       ██                                        |");
        System.out.println("|  ██      ███████ ██   ████ ██   ██ ███████    ██       ██                                        |");                                                                                                                                                   
        super.emptyline();
        super.lines(100); 
        System.out.printf("|   TOTAL AMOUNT ( WITH 6 PERCENTS TAXES )                                            RM %-10s|%n",formatFinal);
        System.out.printf("|   TOTAL CHARGES ( 40 PERCENTS OF TOTAL AMOUNT )                                     RM %-10s|%n",formatPenal);
        System.out.printf("|   PAYMENT METHOD                                                                  %-14s |%n",penaldetail.getPaymentMethod());
        super.lines(100);

        System.out.print("\n");

    }

    public void paymentDone()
    {
        Logo logoObj = new Logo();
        System.out.print("\n");
        System.out.println("THANKS FOR YOUR PAYMENT :)\n");
        logoObj.thankYou();
        this.zeroExit();
    }

    public void tng()
    {
        Logo logoObj = new Logo();
        Scanner scanner = new Scanner(System.in);
        String ans;
        super.clear();
        logoObj.qrCode();
        while(true)
        {
            try
            {
                System.out.println("FOR PENALYTY\'S FEE WE ONLY ACCEPT SCAN QR CODE :)\n");
                System.out.print("PRESS ANYTHING TO PROCEED PAYMENT.......");
                ans = scanner.nextLine();
                this.paymentDone();
                break;
            }
            catch(Exception e)
            {
                System.out.println(e.getMessage());
            }
            break;
        }
    }

    public void payPenalty(String penaltyListCode)
    {
        SaveProgress.outputPenalty();
        Penaltys penalObj = Penaltys.getPenaltyObj();
        Map<String,PenaltyDetail> penaltyList = penalObj.getPenaltyList();
        PenaltyDetail payDetail = penaltyList.get(penaltyListCode);

        Logo logoObj = new Logo();

        Scanner scanner = new Scanner(System.in);
        String ans;

        this.penalty(penaltyListCode);

        logoObj.paymentMethodLogo();

        while(true)
        {
            try
            {
                System.out.print("PLEASE SELECT ANY PAYMENT METHOD <t = TOUCH N GO> <c = CREDIT CARD> <o = ONLINE BANKING> : ");
                ans = scanner.nextLine().trim().toUpperCase();

                switch(ans)
                {
                    case "t":case "T":
                    payDetail.setPenaltyStatus("PAYMENT DONE");
                    payDetail.setPaymentMethod("TOUCH N GO");
                    SaveProgress.savePenalty(penaltyList);
                    this.tng();break;
                    case "c":case "C":
                    payDetail.setPenaltyStatus("PAYMENT DONE");
                    payDetail.setPaymentMethod("CREDIT CARD");
                    SaveProgress.savePenalty(penaltyList);
                    this.paymentDone();break;
                    case "o":case "O":
                    payDetail.setPenaltyStatus("PAYMENT DONE");
                    payDetail.setPaymentMethod("ONLINE BANKING");
                    SaveProgress.savePenalty(penaltyList);
                    this.paymentDone();break;
                    default:
                    System.out.println("INVALID INPUT~~~ PLEASE ENTER AGAIN :)\n");
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

    public void payDeposit(Map<String,TempValue> templist,String tempCode)
    {
        TempValue tempValue = templist.get(tempCode);
        PayList payObj = PayList.getPaymentObj();
        Map<String,PaymentDetail> payList = payObj.getPayList();
        Car carObj = Car.getCarObj();
        Map<String,CarSpec> carList = carObj.getCarList();
        CarSpec carSpec = carList.get(tempValue.getChosenCar());
        String carModel = carSpec.getCarModels();
        Penaltys penalObj = Penaltys.getPenaltyObj();
        Map<String,PenaltyDetail> penaltyList = penalObj.getPenaltyList();

        double duration = Double.parseDouble(tempValue.getDuration());
        double rate = Double.parseDouble(tempValue.getRate());

        //to calculate total price and price with tax
        double total = this.calTotal(duration,rate);
        double totalTax = this.calWithTax(total);

        //to generate receipt code
        String paymentCode = this.generateCode(payList, "RC");

        //custom format for date and time
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm a",Locale.ENGLISH);

        //generate date for the receipt
        String receiptDate = LocalDateTime.now().format(format);

        //convert double value into string format
        String rates = String.valueOf(rate);
        String totals = String.valueOf(total);
        String totalTaxs = String.valueOf(totalTax);

        double finalAmt = calcuFinalAmt(duration, rate);
        String finalAmount = String.valueOf(finalAmt);
        double deposit = calcuDeposit(finalAmt);
        String deposits = String.valueOf(deposit);

        String payStatus = "DEPOSIT";

        //to generate the car's class type
        String carClass= this.classType(tempValue.getClassPrefix());

        Logo logoObj = new Logo();
        Scanner scanner = new Scanner(System.in);
        String payMethod;

        this.deposit(tempValue.getStartDates(),tempValue.getEndDates(),carModel,finalAmt);

        System.out.println("THE DEPOSIT WILL BE 20% OF THE TOTAL AMOUNT (INCLUDED 6% TAXES)\n");
        System.out.println("PLEASE MAKE SURE THE CAR WILL BE RETURN IN GOOD CONDITION");
        System.out.println("OTEHRWISE THERE WILL A 40 PRECENTS OF TOTAL AMOUNT'S PENALTY FEE\n");

        logoObj.paymentMethodLogo();

        while(true)
        {
            try
            {
                System.out.print("PLEASE SELECT ANY PAYMENT METHOD <t = TOUCH N GO> <c = CREDIT CARD> <o = ONLINE BANKING> : ");
                payMethod = scanner.nextLine().trim().toUpperCase();

                switch(payMethod)
                {
                    case "t":case "T":payMethod = "TOUCH N GO";break;
                    case "c":case "C":payMethod = "CREDIT CARD";break;
                    case "o":case "O":payMethod = "ONLINE BANKING";break;
                    default:
                    System.out.println("INVALID INPUT~~~~ PLEASE ENTER AGAIN~~~ :)\n");
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

        logoObj.thankYou();
        //add all the elements for the payList
        payList.put(paymentCode,new PaymentDetail(tempValue.getUsername(), tempValue.getEmail(), tempValue.getLicenseNum(),
        tempValue.getStartDates(),tempValue.getEndDates(),carModel,rates,totals,payMethod,
        totalTaxs,receiptDate,carClass,tempValue.getDuration(),tempValue.getDuPrefix(),finalAmount,payStatus,tempValue.getChosenCar(),deposits,"CHECKED"));
        templist.remove(tempCode);
        carSpec.setCarStatus("UNAVAILABLE");
        SaveProgress.savePayment(payList);
        SaveProgress.saveCarList(carList);
        SaveProgress.savePenalty(penaltyList);

        System.out.println("PLEASE REACH OUT BOOKED CAR SECTION TO COMPLETE FULL PAYMENT :)\n");

        this.zeroExit();
    }   

    public void confirmation(String payListCode)
    {
        PayList payObj = PayList.getPaymentObj();
        Map<String,PaymentDetail> paylist = payObj.getPayList();
        PaymentDetail paydetail = paylist.get(payListCode);
        Car carObj = Car.getCarObj();
        Map<String,CarSpec> carlist = carObj.getCarList();
        CarSpec chosenCar = carlist.get(paydetail.getCarCode());
        BookedPages bookObj = new BookedPages();
        Logo logoObj = new Logo();
        Scanner scanner = new Scanner(System.in);
        String ans;

        logoObj.warningLogo();

        while(true)
        {
            try
            {
                System.out.println("ARE YOU SURE YOU WANT TO CANCEL ?");
                System.out.println("YOU WILL LOST THE DEPOSIT FEE ONCE YOU CANCEL :)\n");
                System.out.print("PLEASE SELECT < y=YES / n=NO > : ");
                ans = scanner.nextLine().trim().toUpperCase();

                switch(ans)
                {
                    case "y":case "Y":
                    System.out.println("YOU\'VE CANCELED THE <"+chosenCar.getCarModels()+"> BOOKING SUCCESSFULLY :)");
                    chosenCar.setCarStatus("AVAILABLE");
                    paylist.remove(payListCode);
                    SaveProgress.savePayment(paylist);
                    SaveProgress.saveCarList(carlist);
                    break;
                    case "n":case "N":
                    bookObj.bookedHistory();
                    break;
                    default:
                    System.out.println("INVALID INPUT~~~ PLEASE ENTER AGAIN~~~\n");
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
        this.zeroExit();
    }

    public void payFullPayemnt(String payListCode)
    {
        Logo logoObj = new Logo();
        Scanner scanner = new Scanner(System.in);
        String payM;
        PayList payObj = PayList.getPaymentObj();
        Map<String,PaymentDetail> paylist = payObj.getPayList();
        PaymentDetail paydetail = paylist.get(payListCode);

        logoObj.paymentMethodLogo();

        while(true)
        {
            try
            {
                System.out.print("PLEASE SELECT ANY PAYMENT METHOD <t = TOUCH N GO> <c = CREDIT CARD> <o = ONLINE BANKING> : ");
                payM = scanner.nextLine().trim().toUpperCase();

                switch(payM)
                {
                    case "t":case "T":
                    payM = "TOUCH N GO";
                    paydetail.setPayStatus("PAYMENT DONE");
                    paydetail.setCheckStatus("HAVENT CHECK");
                    paydetail.setPayMethod(payM);
                    SaveProgress.savePayment(paylist);
                    this.paymentDone();
                    break;
                    case "c":case "C":
                    payM = "CREDIT CARD";
                    paydetail.setPayStatus("PAYMENT DONE");
                    paydetail.setPayMethod(payM);
                    paydetail.setCheckStatus("HAVENT CHECK");
                    SaveProgress.savePayment(paylist);
                    this.paymentDone();
                    break;
                    case "o":case "O":
                    payM = "ONLINE BANKING";
                    paydetail.setPayStatus("PAYMENT DONE");
                    paydetail.setPayMethod(payM);
                    paydetail.setCheckStatus("HAVENT CHECK");
                    SaveProgress.savePayment(paylist);
                    this.paymentDone();
                    break;
                    default:
                    System.out.println("INVALID INPUT~~~ PLEASE ENTER AGAIN :)\n");
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

    public void paymentOpt(String payListCode)
    {
        super.clear();
        Logo logoObj = new Logo();
        Scanner scanner = new Scanner(System.in);
        String ans;

        logoObj.receiptLogo();
        super.receipt(payListCode);
        logoObj.cancelFullLogo();

        while(true)
        {
            try
            {
                System.out.print("YOU WANT TO CANCEL OR MAKE THE FULL PAYMENT <c = CANCEL> <f = FULL PAYMENT> : ");
                ans = scanner.nextLine().trim().toUpperCase();

                switch(ans)
                {
                    case "C":
                    this.confirmation(payListCode);
                    break;
                    case "F":
                    this.payFullPayemnt(payListCode);
                    break;
                    default:
                    System.out.println("INVALID INPUT~~~ PLEASE ENTER AGAIN :)\n");
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
}
