package entity;

import java.util.HashMap;
import java.util.Map;

public class PayList
{
    private static PayList paymentObj;
    private HashMap <String,PaymentDetail> payList;

    public PayList()
    {
        payList = SaveProgress.outputPayment();
    }

    public void receipt()
    {
        
        System.out.println();
    }

    public static PayList getPaymentObj()
    {
        if(paymentObj == null)
        {
            paymentObj = new PayList();
        }

        return paymentObj;
    }

    public Map<String,PaymentDetail> getPayList()
    {
        return payList;
    }

    //its a method for print empty line
    public void emptyLength(int amt)
    {
        for(int i =0;i<amt;i++)
        {
            System.out.print(" "); 
        }
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

    public void lines(int amt)
    {
        for(int i =0;i<amt;i++)
        {
            System.out.print("-");
        }

        System.out.print("\n");
    }

    public void emptyline()
    {
        System.out.println("|                                                                                                  |");
    }

    public void receipt(String payCode)
    {
        SaveProgress.outputPayment();
        PayList payObj = PayList.getPaymentObj();
        Map<String,PaymentDetail> payList = payObj.getPayList();
        PaymentDetail pay = payList.get(payCode);
        double doubleRate = Double.parseDouble(pay.getRate());
        double doubleTotal = Double.parseDouble(pay.getSubTotal());
        double doubleTotalAmt = Double.parseDouble(pay.getFinalAmount());
        double doubleTax = Double.parseDouble(pay.getFinalTax());
        double doubleDeposit = Double.parseDouble(pay.getDeposit());

        String formatRate = String.format("%.2f",doubleRate);
        String formatTotal = String.format("%.2f",doubleTotal);
        String formatTotalAmount = String.format("%.2f",doubleTotalAmt);
        String formatTax = String.format("%.2f",doubleTax);
        String formatDeposit = String.format("%.2f",doubleDeposit);

        this.clear();

        this.lines(100);
        System.out.printf("|  VIYEAHCLE CAR RENTAL RECEIPT NO. %-5s                                                         |%n",payCode);
        System.out.printf("|                                                                            %20s  |%n",pay.getReceiptDate());
        this.emptyline();
        System.out.println("|  _    _ _____ __   __ _______ _______ _     _ _______        _______                             |");
        System.out.println("|   \\  /    |     \\_/   |______ |_____| |_____| |       |      |______                             |");
        System.out.println("|    \\/   __|__    |    |______ |     | |     | |_____  |_____ |______                             |");
        this.emptyline();
        this.lines(100);
        System.out.println("|                 |                                                                                |");
        System.out.println("|    ENTERPRISE   |  Ground Floor, Bangunan Tan Sri Khaw Kai                                       |");
        System.out.println("|     LOCATION    |  Boh (Block A), Jalan Genting Kelang, Setapak,                                 |");
        System.out.println("|                 |  53300 Kuala Lumpur, Federal Territory of Kuala Lumpur.                        |");
        this.lines(100);
        System.out.println("|    CUSTOMER     |     CUSTOMER NAME              CUSTOMER EMAIL              LICENSE NUMBER      |");
        System.out.printf("|   INFORMATION   |     %-15s            %-20s        %-8s            |%n",pay.getUsername(),pay.getEmail(),pay.getLicense());
        this.lines(100);
        System.out.println("|        START DATE         |         END DATE         |                CAR\'S MODEL                |");
        this.lines(100);
        System.out.printf("|    %-20s   |   %-20s   |                %-25s  |%n",pay.getStartDate(),pay.getEndDate(),pay.getCarModel()); 
        this.lines(100); 
        System.out.println("|        CAR\'S CLASS        |         DURATION         |      RATE (RM)      |      TOTAL (RM)     |");
        this.lines(100); 
        System.out.printf("|       %-12s        |         %-2s %-5s         |      %-10s     |      %-10s     |%n",pay.getCarClass(),pay.getDuration(),pay.getDuPrefix(),formatRate,formatTotal);
        this.lines(100); 
        System.out.printf("|                                                                          SUBTOTAL : RM %-10s|%n",formatTotal);
        this.lines(100); 
        System.out.println("|   TAXES AND SURCHARGES                                                                           |");
        System.out.printf("|   SALES TAX   ( 6 PERCENTS )                                                        RM %-10s|%n",formatTax);
        System.out.printf("|   DEPOSIT     ( 20 PERCENTS )                                                   (-) RM %-10s|%n",formatDeposit);
        System.out.printf("|   PAYMENT METHOD                                                                  %-14s |%n",pay.getPayMethod());
        this.lines(100);
        System.out.printf("|   TOTAL CHARGES                                                                     RM %-10s|%n",formatTotalAmount);
        this.lines(100);

        System.out.print("\n");
    }
}
