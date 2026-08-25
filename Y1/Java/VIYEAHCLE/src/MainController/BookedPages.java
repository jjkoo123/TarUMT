package MainController;

import MainPage.MainPages;
import entity.Logo;
import entity.PayList;
import entity.PaymentDetail;
import entity.SaveProgress;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

public class BookedPages extends PayList
{
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

    public void line(int amt) {
        System.out.print(" ");
        for (int i = 0; i < amt; i++) {
            System.out.print("* ");
        }
        System.out.print("\n");
    }

    @Override
    public void emptyLength(int amt)
    {
        for(int i =0;i<amt;i++)
        {
            System.out.print(" "); 
        }
    }

    public void bookListColumn()
    {
        this.line(41);
        System.out.println("*                 *                               *                               *");
        System.out.println("*      CODE       *          RENTER NAME          *         PAYMENT STATUS        *");
        System.out.println("*                 *                               *                               *");
        this.line(41);
    }

    public void bookedList()
    {
        SaveProgress.outputPayment();
        PayList payObj = PayList.getPaymentObj();
        Map<String,PaymentDetail> payList = payObj.getPayList();
        TreeMap<String,PaymentDetail> sortedPayList = new TreeMap<>(payList);

        this.bookListColumn();

        for(Map.Entry<String,PaymentDetail> entry : sortedPayList.entrySet())
        {
            System.out.println("*                 *                               *                               *");
            System.out.printf("*      %-6s     *",entry.getKey());
            System.out.println(entry.getValue());
            System.out.println("*                 *                               *                               *");
            this.line(41);
        }
    }
    
    public void bookedHistory()
    {
        Logo logoObj = new Logo();

        Scanner scanner = new Scanner(System.in);
        String ans;

        MainPages mainObj = new MainPages();

        PayList payObj = PayList.getPaymentObj();
        Map<String,PaymentDetail> payList = payObj.getPayList();

        PaymentPages paypageobj = new PaymentPages();

        this.clear();

        logoObj.payHistoryLogo();

        if(payList.isEmpty())
        {
            logoObj.emptyLogo();
            System.out.println("\nTHERE\'S NO ANY BOOKED CAR HISTORY EXIST~~~\n");
            paypageobj.zeroExit();
        }
        else
        {
            this.bookedList();
        }

        System.out.println("\n");

        while(true)
        {
            try
            {
                System.out.print("PLEASE SELECT ANY BOOKED RECORD TO COMPLETE FULL PAYMENT OR CANCEL <e.g. RC0001> <0 to exit> : ");
                ans = scanner.nextLine().trim().toUpperCase();

                if(ans.equals("0"))
                {
                    mainObj.userOpt();
                    break;
                }

                if(!payList.containsKey(ans))
                {
                    System.out.println("PLEASE SELECT RECORD SHOWN ABOVE ONLY :)\n");
                    continue;
                }

                PaymentDetail payDetail = payList.get(ans);

                if(payDetail.getPayStatus().equals("PAYMENT DONE"))
                {
                    super.receipt(ans);
                    System.out.println("HERE\'S YOUR RECEIPT~~~ THANKS FOR USING OUR SYSTEM :)\n");
                    logoObj.thankYou();
                    paypageobj.zeroExit();
                    break;
                }
                else
                {
                    paypageobj.paymentOpt(ans);
                    break;
                }
            }
            catch (Exception e)
            {
                System.out.println(e.getMessage());
            }
            break;
        }
    }
}
