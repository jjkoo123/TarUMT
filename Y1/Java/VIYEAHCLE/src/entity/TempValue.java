package entity;

public class TempValue 
{
    private String classPrefix;
    private String startDates; 
    private String endDates;
    private String duPrefix;
    private String lengthDuration;
    private String duration;
    private String chosenCar;
    private String username; 
    private String email;
    private String licenseNum;
    private String rate;

    public TempValue(String classPrefix,String startDates,String endDates,String duPrefix,
    String lengthDuration,String duration,String chosenCar,String username,String email,
    String licenseNum,String rate)
    {
        this.classPrefix = classPrefix;
        this.startDates = startDates;
        this.endDates = endDates;
        this.duPrefix = duPrefix;
        this.lengthDuration = lengthDuration;
        this.duration = duration;
        this.chosenCar = chosenCar;
        this.username = username;
        this.email = email;
        this.licenseNum = licenseNum;
        this.rate = rate;
    }

    public String getClassPrefix()
    {
        return classPrefix;
    }
    public String getStartDates()
    {
        return startDates;
    }

    public String getEndDates()
    {
        return endDates;
    }

    public String getDuPrefix()
    {
        return duPrefix;
    }

    public String getLengthDuration()
    {
        return lengthDuration;
    }
    
    public String getDuration()
    {
        return duration;
    }
    public String getChosenCar()
    {
        return chosenCar;
    }
    public String getUsername()
    {
        return username;
    }
    public String getEmail()
    {
        return email;
    }
    public String getLicenseNum()
    {
        return licenseNum;
    }

    public String getRate()
    {
        return rate;
    }

    public void setClassPrefix(String newClassPrefix)
    {
        this.classPrefix = newClassPrefix;
    }
    public void setStartDates(String newStartDates)
    {
        this.startDates = newStartDates;
    }

    public void setEndDates(String newEndDates)
    {
        this.endDates = newEndDates;
    }

    public void setDuPrefix(String newDuPrefix)
    {
        this.duPrefix = newDuPrefix;
    }

    public void setLengthDuration(String newLengthDuration)
    {
        this.lengthDuration = newLengthDuration;
    }
    public void setDuration(String newDuration)
    {
       this.duration = newDuration;
    }
    public void setChosenCar(String newChosenCar)
    {
        this.chosenCar = newChosenCar;
    }

    public void setUsername(String newUsername)
    {
        this.username = newUsername;
    } 

    public void setEmail(String newEmail)
    {
        this.email = newEmail;
    }

    public void setLicenseNum(String newLicenseNum)
    {
        this.licenseNum = newLicenseNum;
    }

    public void setRate(String newRate)
    {
        this.rate = newRate;
    }
}
