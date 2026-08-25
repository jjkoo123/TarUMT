
public class Employee {
    private String name;
    private int yearJoined;
    private double basicSalary;

    public Employee(String name, int yearJoined, double basicSalary) {
        this.name = name;
        this.yearJoined = yearJoined;
        this.basicSalary = basicSalary;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getYearJoined() {
        return yearJoined;
    }

    public void setYearJoined(int yearJoined) {
        this.yearJoined = yearJoined;
    }

    public double getBasicSalary() {
        return basicSalary;
    }

    @Override
    public String toString() {
        return "Name: " + name + " Year Joined: " + yearJoined + " Basic Salary: " + basicSalary;
    }

    public double calculateSalary() {
        return basicSalary;
    }
}
