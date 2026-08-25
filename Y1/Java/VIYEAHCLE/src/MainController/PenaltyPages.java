package MainController;

import MainPage.MainPages;
import entity.Logo;
import entity.PayList;
import entity.PenaltyDetail;
import entity.Penaltys;
import entity.SaveProgress;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

public class PenaltyPages extends PayList
{
    public void penaltyListColumn()
    {
        this.line(41);
        System.out.println("*                 *                               *                               *");
        System.out.println("*      CODE       *          RENTER NAME          *         PENALTY STATUS        *");
        System.out.println("*                 *                               *                               *");
        this.line(41);
    }

    public void line(int amt) {
        System.out.print(" ");
        for (int i = 0; i < amt; i++) {
            System.out.print("* ");
        }
        System.out.print("\n");
    }

    public void penaltyRecord()
    {
        SaveProgress.outputPenalty();
        Penaltys penalObj = Penaltys.getPenaltyObj();
        Map<String,PenaltyDetail> penaltyList = penalObj.getPenaltyList();
        TreeMap<String,PenaltyDetail> sortedPenaltyList = new TreeMap<>(penaltyList);

        this.penaltyListColumn();

        for(Map.Entry<String,PenaltyDetail> entry : sortedPenaltyList.entrySet())
        {
            System.out.println("*                 *                               *                               *");
            System.out.printf("*      %-6s     *",entry.getKey());
            System.out.println(entry.getValue());
            System.out.println("*                 *                               *                               *");
            this.line(41);
        }
    }

    public void penaltyPage()
    {
        Logo logoObj = new Logo();
        SaveProgress.outputPenalty();
        MainPages main = new MainPages();
        Penaltys penalObj = Penaltys.getPenaltyObj();
        Map<String,PenaltyDetail> penaltyList = penalObj.getPenaltyList();
        PaymentPages paypageobj = new PaymentPages();
        Scanner scanner = new Scanner(System.in);
        String ans;

        logoObj.penalty();

        if(penaltyList.isEmpty())
        {
            logoObj.emptyLogo();
            System.out.println("\nTHERE\'S NO ANY PENALTY NEED TO PAY~~~\n");
            paypageobj.zeroExit();
        }
        else
        {
            this.penaltyRecord();
        }
        
        System.out.println("\n");


        while(true)
        {
            try
            {
                System.out.print("PLEASE SELECT ANY PENALTY RECORD THAT NEED TO BE PAY <0 to exit> : ");
                ans = scanner.nextLine().trim().toUpperCase();

                PenaltyDetail penalDetail = penaltyList.get(ans);

                if(ans.equals("0"))
                {
                    main.userOpt();
                    break;
                }

                if(!penaltyList.containsKey(ans))
                {
                    System.out.println("PLEASE SELECT PENALTY RECORD SHOWN ABOVE ONLY :)\n");
                    continue;
                }

                if(penalDetail.getPenaltyStatus().trim().equals("TO PAY")&&penaltyList.containsKey(ans))
                {
                    paypageobj.payPenalty(ans);
                    break;
                }
                else
                {
                    paypageobj.penalty(ans);
                    System.out.println("HERE\'S YOUR PENALTY\'S RECEIPT~~~ THANKS FOR YOUR PAYMENT\n");
                    logoObj.thankYou();
                    paypageobj.zeroExit();
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
