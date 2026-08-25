

public class q1 {
    public static void main(String[] args) {
        // Print the table headers
        System.out.printf("%-10s%-15s%-10s\n", "Number", "SquareRoot", "Cube");
        
        // Loop through numbers from 0 to 20
        for (int number = 0; number <= 20; number++) {
            double squareRoot = Math.sqrt(number);   // Calculate square root
            double cube = Math.pow(number, 3);       // Calculate cube

            // Print the result in a table format
            System.out.printf("%-10d%-15.4f%-10.4f\n", number, squareRoot, cube);
        }
    }
}

