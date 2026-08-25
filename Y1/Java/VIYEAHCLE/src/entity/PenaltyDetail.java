package entity;

public class PenaltyDetail
{
    private String username;
    private String finalAmount;
    private String penaltyAmt;
    private String penaltyStatus;
    private String paymentMethod;

    public PenaltyDetail(String username,String penaltyAmt,String penaltyStatus,
    String finalAmount,String paymentMethod)
    {
        this.username = username;
        this.penaltyAmt = penaltyAmt;
        this.penaltyStatus = penaltyStatus;
        this.finalAmount = finalAmount;
        this.paymentMethod = paymentMethod;
    }

    public String penaltyToCSV()
    {
        return username + "," + penaltyAmt + "," + penaltyStatus + "," + finalAmount + "," + paymentMethod;
    }

    public String getUsername()
    {
        return username;
    }

    public String getPenaltyAmt()
    {
        return penaltyAmt;
    }

    public String getPenaltyStatus()
    {
        return penaltyStatus;
    }

    public String getFinalAmount()
    {
        return finalAmount;
    }
    
    public String getPaymentMethod()
    {
        return paymentMethod;
    }

    public void setPenaltyAmount(String newPenalty)
    {
        this.penaltyAmt = newPenalty;
    }

    public void setPenaltyStatus(String newPenaltyStatus)
    {
        this.penaltyStatus = newPenaltyStatus;
    }

    public void setPaymentMethod(String newPaymentMethod)
    {
        this.paymentMethod = newPaymentMethod;
    }

    @Override
    public String toString()
    {
        return String.format("          %-10s           *         %-12s          *",
        username,penaltyStatus);
    }
}
