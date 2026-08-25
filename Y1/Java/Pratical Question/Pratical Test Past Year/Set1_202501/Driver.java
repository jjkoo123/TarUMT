public class Driver {
    public static void main(String[] args) {
        Employee[] empArr = {
                new FullTimeEmployee("E001", "Alice Smith", 5, 50000, 2080, 5000),
                new FullTimeEmployee("E002", "Bob Johnson", 3, 450000, 2080, 3000),
                new PartTimeEmployee("E003", "Charlie Brown", 1, 0, 500, 20)
        };

        for (int i = 0; i < empArr.length; i++) {
            System.out.println("Employee " + (i + 1) + ": ");
            if (empArr[i] instanceof FullTimeEmployee)
                System.out.println("Full Time Employee");
            else if (empArr[i] instanceof PartTimeEmployee)
                System.out.println("Part Time Employee");

            System.out.println(empArr[i]);
            System.out.println("-------------------------------");
        }
        System.out.println("Total Employees: " + Employee.getTotalEmployees());
    }
}
