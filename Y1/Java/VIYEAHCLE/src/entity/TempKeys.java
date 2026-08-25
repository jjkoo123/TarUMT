package entity;

import java.util.HashMap;
import java.util.Map;

public class TempKeys 
{
    private static TempKeys tempObj;
    private HashMap<String,TempValue> templist;

    public TempKeys()
    {
        templist = new HashMap<>();
    }

    public static TempKeys getTempKeyObj()
    {
        if(tempObj == null)
        {
            tempObj = new TempKeys();
        }
        return tempObj;
    }

    public Map<String,TempValue> getTempList()
    {
        return templist;
    }

    
}
