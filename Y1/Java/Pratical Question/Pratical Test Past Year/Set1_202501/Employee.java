
public class Employee {
    private String employeeID;
    private String name;
    private int yearsOfService;
    private double baseSalary;
    private int hoursWorked;
    private static int totalEmployees;

    // no argument constructor
    public Employee() {
        this("", "", 0, 0, 0);
    }

    public Employee(String employeeID, String name, int yearsOfService, double baseSalary, int hoursWorked) {
        this.employeeID = employeeID;
        this.name = name;
        this.yearsOfService = yearsOfService;
        this.baseSalary = baseSalary;
        this.hoursWorked = hoursWorked;
        totalEmployees++;
    }

    public String getEmployeeID() {
        return employeeID;
    }

    public String getName() {
        return name;
    }

    public int getYearsOfService() {
        return yearsOfService;
    }

    public double getBaseSalary() {
        return baseSalary;
    }

    public int getHoursWorked() {
        return hoursWorked;
    }

    public static int getTotalEmployees() {
        return totalEmployees;
    }

    public void setEmployeeId(String employeeID) {
        this.employeeID = employeeID;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setYearsofService(int yearsOfService) {
        this.yearsOfService = yearsOfService;
    }

    public void setBaseSalary(double baseSalary) {
        this.baseSalary = baseSalary;
    }

    public void setHoursWorked(int hoursWorked) {
        this.hoursWorked = hoursWorked;
    }

    public static void setTotalEmployees(int totalEmployees) {
        Employee.totalEmployees = totalEmployees;
    }

    public double calculateSalary() {
        return baseSalary;
    }

    public String toString() {
        return "Employee Id: " + employeeID +
                "\nName: " + name +
                "\nYears of Servide: " + yearsOfService +
                "\nBase Salary: " + baseSalary +
                "\nHours Worked: " + hoursWorked;
    }
}
