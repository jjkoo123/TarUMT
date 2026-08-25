public class FullTimeEmployee extends Employee {
    private double annualBonus;

    public FullTimeEmployee(String employeeID, String name, int yearsOfService, double baseSalary, int hoursWorked,
            double annualbonus) {
        super(employeeID, name, yearsOfService, baseSalary, hoursWorked);
        this.annualBonus = annualbonus;
    }

    public double getAnnualBonus() {
        return annualBonus;
    }

    public void setAnnualBonus(double annualBonus) {
        this.annualBonus = annualBonus;
    }

    public double calculateTotalSalary() {
        return super.getBaseSalary() + (super.getBaseSalary() * (super.getYearsOfService() * 0.02));
    }

    public String toString() {
        return super.toString() + "\nAnnual Bonus: " + annualBonus;
    }

}
