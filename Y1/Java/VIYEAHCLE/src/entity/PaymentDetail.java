package entity;

public class PaymentDetail
{
    private String username;
    private String email;
    private String carModel;
    private String licenseNum;
    private String payMethod;
    private String startDate;
    private String endDate;
    private String rate;
    private String subTotal;
    private String finalTax;
    private String receiptDate;
    private String carClass;
    private String duration;
    private String duPrefix;
    private String finalAmount;
    private String payStatus;
    private String carCode;
    private String deposit;
    private String checkStatus;

    public PaymentDetail(String username,String email,String licenseNum,String startDate,String endDate,String carModel,String rate,
    String subTotal,String payMethod,String finalTax,String receiptDate,String carClass,String duration,String duPrefix,String finalAmount,
    String payStatus,String carCode,String deposit,String checkStatus)
    {
        this.username = username;
        this.email = email;
        this.licenseNum = licenseNum;
        this.startDate = startDate;
        this.endDate = endDate;
        this.carModel = carModel;
        this.rate = rate;
        this.subTotal = subTotal;
        this.payMethod = payMethod;
        this.finalTax = finalTax;
        this.receiptDate = receiptDate;
        this.carClass = carClass;
        this.duration = duration;
        this.duPrefix = duPrefix;
        this.finalAmount = finalAmount;
        this.payStatus = payStatus;
        this.carCode = carCode;
        this.deposit = deposit;
        this.checkStatus = checkStatus;
    }

    public String paymentToCSV()
    {
        return username + "," + email + "," + licenseNum + "," + startDate + "," + endDate + "," + carModel + "," + rate + "," + 
        subTotal + "," + payMethod + "," + finalTax + "," + receiptDate + "," + carClass + "," + duration + "," + duPrefix + "," + 
        finalAmount + "," + payStatus + "," + carCode + "," + deposit + "," + checkStatus;
    }

    public String getUsername()
    {
        return username;
    }

    public String getEmail()
    {
        return email;
    }

    public String getLicense()
    {
        return licenseNum;
    }

    public String getStartDate()
    {
        return startDate;
    }

    public String getEndDate()
    {
        return endDate;
    }

    public String getCarModel()
    {
        return carModel;
    }

    public String getRate()
    {
        return rate;
    }
    public String getSubTotal()
    {
        return subTotal;
    }

    public String getPayMethod()
    {
        return payMethod;
    }

    public String getFinalTax()
    {
        return finalTax;
    }
    
    public String getReceiptDate()
    {
        return receiptDate;
    }

    public String getCarClass()
    {
        return carClass;
    }

    public String getDuration()
    {
        return duration;
    }

    public String getDuPrefix()
    {
        return duPrefix;
    }

    public String getFinalAmount()
    {
        return finalAmount;
    }

    public String getPayStatus()
    {
        return payStatus;
    }

    public String getCarCode()
    {
        return carCode;
    }

    public String getDeposit()
    {
        return deposit;
    }

    public String getCheckStatus()
    {
        return checkStatus;
    }

    public void setPayStatus(String newPayStatus)
    {
        this.payStatus = newPayStatus;
    }

    public void setPayMethod(String newPayMethod)
    {
        this.payMethod = newPayMethod;
    }

    public void setCheckStatus(String newCheckStatus)
    {
        this.checkStatus = newCheckStatus;
    }

    @Override
    public String toString()
    {
        String output = String.format("          %-10s           *         %-12s          *",username,payStatus);
        return output;
    }
}
