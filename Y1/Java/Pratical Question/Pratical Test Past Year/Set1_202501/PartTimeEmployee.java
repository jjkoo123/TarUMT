public class PartTimeEmployee extends Employee {
    private double hourlyRate;

    public PartTimeEmployee(String employeeID, String name, int yearsOfService, double baseSalary, int hoursWorked,
        double hourlyRate) {
        super(employeeID, name, yearsOfService, baseSalary, hoursWorked);
        this.hourlyRate = hourlyRate;
    }

    public double getHourlyRate() {
        return hourlyRate;
    }

    public void setHourlyRate(double hourlyRate) {
        this.hourlyRate = hourlyRate;
    }

    public double calculateTotalSalary() {
        return hourlyRate * getHoursWorked();
    }

    public String toString() {
        return super.toString() + "\nHourly Rate: " + hourlyRate;
    }
}
