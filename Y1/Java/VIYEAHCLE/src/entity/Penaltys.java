package entity;

import java.util.HashMap;
import java.util.Map;

public class Penaltys 
{
    private static Penaltys penaltyObj;
    private HashMap <String,PenaltyDetail> penaltyList;

    public Penaltys()
    {
        penaltyList = SaveProgress.outputPenalty();
    }

    public static Penaltys getPenaltyObj()
    {
        if(penaltyObj == null)
        {
            penaltyObj = new Penaltys();
        }

        return penaltyObj;
    }

    public Map<String,PenaltyDetail> getPenaltyList()
    {
        return penaltyList;
    }
}
