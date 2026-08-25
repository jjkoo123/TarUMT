public class SavingsAccount extends Account {
    private static double interestRate = 0.06;

    public SavingsAccount(String accountName, double accountBalance){
        super(accountName, accountBalance);
    }

    public static double getInterestRate(){
        return interestRate;

    }

    public static void setInterestRate(double interestRate){
        SavingsAccount.interestRate = interestRate;
    }

    public double calculateInterest(){
        return getAccountBalance() * interestRate;
    }

    public void addInterest(){
        deposit(calculateInterest());
    }
}
