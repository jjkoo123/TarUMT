import java.util.Date;
public class Account{
    private String accountNumber;
    private double accountBalance;
    private Date dateCreated;

    public Account(
        String accountNumber,
        double accountBalance
    ){
        this.accountNumber = accountNumber;
        this.accountBalance = accountBalance;
        dateCreated = new Date(); //current system date/time
    }

    public String getAccountNumber(){
        return accountNumber;
    }

    public double getAccountBalance(){
        return accountBalance;
    }

    public Date getDateCreated(){
        return dateCreated;
    }

    public void setAccountNumber(String accountNumber){
        this.accountNumber = accountNumber;
    }

    public void setAccountBalance(double accountBalance){
        this.accountBalance = accountBalance;
    }

    public void setDateCreated(Date dateCreated){
        this.dateCreated = dateCreated;
    }

    public void deposit(double amount){
        this.accountBalance += amount;
    }

    public void withdraw(double amount){
        this.accountBalance -= amount;
    }
    
}