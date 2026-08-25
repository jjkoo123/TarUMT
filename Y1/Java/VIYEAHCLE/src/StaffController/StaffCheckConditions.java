package StaffController;

import entity.Car;
import entity.CarSpec;
import entity.Logo;
import entity.PayList;
import entity.PaymentDetail;
import entity.PenaltyDetail;
import entity.Penaltys;
import entity.SaveProgress;
import java.util.Map;
import java.util.Scanner;

public class StaffCheckConditions extends Car
{
    public void sline(int amt)
    {
        System.out.print(" ");
        for(int i =0;i<amt;i++)
        {
            System.out.print("* ");
        }
        System.out.print("\n");
    }

    public double calcuPenalty(double total)
    {
        return total * 0.4;
    }

    public void checkListColumn()
    {
        this.sline(41);
        System.out.println("*                 *                               *                               *");
        System.out.println("*      CODE       *          RENTER NAME          *         PAYMENT STATUS        *");
        System.out.println("*                 *                               *                               *");
        this.sline(41);
    }

    public void checkConfirmation(String payCode)
    {
        SaveProgress.outputPayment();
        Car carobj = Car.getCarObj();
        StaffPages staffObj = new StaffPages();
        Map<String,CarSpec> carList = carobj.getCarList();
        PayList payObj = PayList.getPaymentObj();
        Map<String,PaymentDetail> payList = payObj.getPayList();
        Penaltys penalObj = Penaltys.getPenaltyObj();
        Map<String,PenaltyDetail> penaltyLists = penalObj.getPenaltyList();
        Scanner scanner = new Scanner(System.in);
        String ans;
        Logo logoObj = new Logo();

        logoObj.goodBad();

        while(true)
        {
            try
            {
                System.out.print("IS THE CAR IN GOOD OR BAD CONDITION ? <g =GOOD / b = BAD> : ");
                ans = scanner.nextLine().trim().toUpperCase();
    
                if(ans.equals("G"))
                {
                    PaymentDetail paydetail = payList.get(payCode);
                    CarSpec carspec = carList.get(paydetail.getCarCode());
                    carspec.setCarStatus("AVAILABLE");
                    paydetail.setCheckStatus("CHECKED");
                    System.out.println("DONE CHECKING THE CONDITION :)");
                    SaveProgress.savePenalty(penaltyLists);
                    SaveProgress.saveCarList(carList);
                    SaveProgress.savePayment(payList);
                    staffObj.zeroToExit();
                    break;
                }
                else if(ans.equals("B"))
                {
                    String penalCode = generateCodes(penaltyLists,"PE");
                    PaymentDetail payDetail = payList.get(payCode);
                    double totalAmt = Double.parseDouble(payDetail.getFinalAmount());
                    double penaltyAmt = calcuPenalty(totalAmt);
                    String penalAmt = String.valueOf(penaltyAmt);
                    String totalAmts = String.valueOf(totalAmt);
                    CarSpec carspec = carList.get(payDetail.getCarCode());
                    carspec.setCarStatus("AVAILABLE");
                    payDetail.setCheckStatus("CHECKED");
                    penaltyLists.put(penalCode,new PenaltyDetail(payDetail.getUsername(),penalAmt,"TO PAY",totalAmts,""));
                    SaveProgress.savePenalty(penaltyLists);
                    SaveProgress.saveCarList(carList);
                    SaveProgress.savePayment(payList);
                    System.out.println("THE PENALTY\'S FEE HAVE SENT TO THE RENTER :)\n");
                    staffObj.zeroToExit();
                    break;
                }
                else
                {
                    System.out.println("INVALID INPUT~~~ PLEASE ENTER AGAIN~~~ \n");
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

    public String generateCodes(Map<String,PenaltyDetail> penaltyList,String prefix)
    {
        SaveProgress.outputPenalty();
        int maxNum = 0;
        int newCode;
        String prefixs = prefix;

        if(!penaltyList.containsKey("PE0001"))
        {
            newCode = 0001;
        }
        else
        {
            for(String i:penaltyList.keySet())
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

    public void checkList()
    {
        SaveProgress.outputPayment();
        PayList payObj = PayList.getPaymentObj();
        Map<String,PaymentDetail> payList = payObj.getPayList();

        this.checkListColumn();

        for(Map.Entry<String,PaymentDetail> entry : payList.entrySet())
        {
            String key = entry.getKey();
            PaymentDetail paydetail = payList.get(key);
            if(paydetail.getCheckStatus().equals("HAVENT CHECK"))
            {
                System.out.println("*                 *                               *                               *");
                System.out.printf("*      %-6s     *",entry.getKey());
                System.out.println(entry.getValue());
                System.out.println("*                 *                               *                               *");
                this.sline(41);
            }
        }
    }

    public void checkConditionPage()
    {
        SaveProgress.outputPayment();
        StaffPages staffObj = new StaffPages();
        PayList payObj = PayList.getPaymentObj();
        Map<String,PaymentDetail> payList = payObj.getPayList();
        Penaltys penalObj = Penaltys.getPenaltyObj();
        Map<String,PenaltyDetail> penaltyLists = penalObj.getPenaltyList();
        Scanner scanner = new Scanner(System.in);
        String ans;
        Logo logoObj = new Logo();
        String keys;
        PaymentDetail paydetail;
        boolean hasPayment = false;

        staffObj.clearScreen();

        logoObj.checkLogo();

        for(Map.Entry<String,PaymentDetail> entry:payList.entrySet())
        {
            keys=entry.getKey();
            paydetail = payList.get(keys);
            if(paydetail.getCheckStatus().equals("HAVENT CHECK"))
            {
                hasPayment = true;
                break;
            }
        }

        if (!hasPayment)
        {
            logoObj.emptyLogo();
            System.out.println("\n");
            System.out.println("THERE\'S NO CAR\'S CONDITION NEED TO BE CHECK :)\n");
            staffObj.zeroToExit();
        }
        else
        {
            this.checkList();
        }

        System.out.println("\n");

        while(true)
        {
            try
            {
                System.out.print("PLEASE SELECT ANY RECORD TO CHECK CONDITION <0 to exit> : ");
                ans = scanner.nextLine().trim().toUpperCase();

                if(ans.equals("0"))
                {
                    staffObj.askOpt();
                    break;
                }

                if(!payList.containsKey(ans))
                {
                    System.out.println("PLEASE SELECT RECORD SHOWN ABOVE ONLY :)\n");
                    continue;
                }
                
                if(payList.containsKey(ans))
                {
                    this.checkConfirmation(ans);
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
