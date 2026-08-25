package entity;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SaveProgress
{
    private static final String CARFILE = "carProgress.txt";
    private static final String PAYFILE = "payProgress.txt";
    private static final String PENALTYFILE = "penaltyProgress.txt";
    private static final int PENALTYLISTLENGTH = 6;
    private static final int CARLISTLENGTH = 6;
    private static final int PAYLISTLENGTH = 20;

    public static void saveCarList(Map<String,CarSpec> carList)
    {
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(CARFILE,false)))
        {
            for(Map.Entry<String,CarSpec> entry:carList.entrySet())
            {
                String line = entry.getKey() + "," + entry.getValue().carToCSV();
                writer.write(line);
                writer.newLine();
            }
        }
        catch (IOException e)
        {
            System.out.println(e.getMessage());
        }
    }

    public static void savePayment(Map<String,PaymentDetail> payList)
    {
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(PAYFILE,false)))
        {
            for(Map.Entry<String,PaymentDetail> entry:payList.entrySet())
            {
                String line = entry.getKey() + "," + entry.getValue().paymentToCSV();
                writer.write(line);
                writer.newLine();
            }
        }
        catch (IOException e)
        {
            System.out.println(e.getMessage());
        }
    }

    public static void savePenalty(Map<String,PenaltyDetail> penaltyList)
    {
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(PENALTYFILE,false)))
        {
            for(Map.Entry<String,PenaltyDetail> entry:penaltyList.entrySet())
            {
                String line = entry.getKey() + "," + entry.getValue().penaltyToCSV();
                writer.write(line);
                writer.newLine();
            }
        }
        catch (IOException e)
        {
            System.out.println(e.getMessage());
        }
    }

    public static HashMap<String,CarSpec> outputCarList()
    {
        HashMap<String,CarSpec> carList = new HashMap<>();
        File file = new File(CARFILE);

        if(!file.exists())
        {
            return new HashMap<>();
        }

        try(BufferedReader reader = new BufferedReader(new FileReader(CARFILE)))
        {
            String line;
            while((line = reader.readLine()) != null)
            {
                String[] parts = line.split(","); // Split with ','
                if (parts.length == CARLISTLENGTH) { // Ensure correct format
                    carList.put(parts[0], new CarSpec(parts[1], parts[2], parts[3], parts[4], parts[5]));
                }
            }
        }
        catch (IOException e)
        {
            System.out.println(e.getMessage());
        }
        return carList;
    }


    public static HashMap<String,PaymentDetail> outputPayment()
    {
        HashMap<String,PaymentDetail> payList = new HashMap<>();
        File file = new File(PAYFILE);

        if(!file.exists())
        {
            return new HashMap<>();
        }

        try(BufferedReader reader = new BufferedReader(new FileReader(PAYFILE)))
        {
            String line;
            while((line = reader.readLine()) != null)
            {
                String[] parts = line.split(","); // Split with ','
                if (parts.length == PAYLISTLENGTH) { // Ensure correct format
                    payList.put(parts[0], new PaymentDetail(parts[1], parts[2], parts[3], parts[4], parts[5],parts[6],parts[7],
                    parts[8],parts[9],parts[10],parts[11],parts[12],parts[13],parts[14],parts[15],
                    parts[16],parts[17],parts[18],parts[19]));
                }
            }
        }
        catch (IOException e)
        {
            System.out.println(e.getMessage());
        }
        return payList;
    }

    public static HashMap<String,PenaltyDetail> outputPenalty()
    {
        HashMap<String,PenaltyDetail> penaltyList = new HashMap<>();
        File file = new File(PENALTYFILE);

        if(!file.exists())
        {
            return new HashMap<>();
        }

        try(BufferedReader reader = new BufferedReader(new FileReader(PENALTYFILE)))
        {
            String line;
            while((line = reader.readLine()) != null)
            {
                String[] parts = line.split(","); // Split with ','
                if (parts.length == PENALTYLISTLENGTH) { // Ensure correct format
                    penaltyList.put(parts[0], new PenaltyDetail(parts[1], parts[2],parts[3],parts[4],parts[5]));
                }
            }
        }
        catch (IOException e)
        {
            System.out.println(e.getMessage());
        }
        return penaltyList;
    }
}
