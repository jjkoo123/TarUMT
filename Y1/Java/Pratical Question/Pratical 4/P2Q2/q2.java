public class q2 {
    
    // Method to calculate square root using approximation
    public static double sqrt(double number) {
        // Handle edge cases
        if (number < 0) {
            System.out.println("Error: Cannot calculate the square root of a negative number.");
            System.exit(1);  // Terminate the program
        }
        if (number == 0 || number == 1) {
            return number;  // Square root of 0 or 1 is the number itself
        }

        // Initialize the lower and upper limits
        double lowerLimit = (number > 1) ? 1 : number;  // For numbers > 1, lower limit is 1
        double upperLimit = (number > 1) ? number : 1;  // For numbers < 1, upper limit is 1
        double midpoint;

        // Loop until the precision requirement is met
        while ((upperLimit - lowerLimit) > 1e-8) {
            midpoint = (lowerLimit + upperLimit) / 2;
            double squareOfMidpoint = midpoint * midpoint;

            // Adjust limits based on the square of the midpoint
            if (squareOfMidpoint > number) {
                upperLimit = midpoint;
            } else {
                lowerLimit = midpoint;
            }
        }

        // Return the midpoint, which is the approximate square root
        return (lowerLimit + upperLimit) / 2;
    }

    public static void main(String[] args) {
        // Test the custom square root method
        double number = 25.0; // You can change this to any number you want to test
        double result = sqrt(number);
        System.out.println("The square root of " + number + " is approximately: " + result);
    }
}