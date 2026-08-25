public class q3 {
    
   

    // Method to convert Celsius to Fahrenheit
    public static double celsiusToFahrenheit(double celsius) {
        return (celsius * 9 / 5) + 32;
    }

    // Method to convert Fahrenheit to Celsius
    public static double fahrenheitToCelsius(double fahrenheit) {
        return (fahrenheit - 32) * 5 / 9;
    }

    public static void main(String[] args) {
        // Printing the Celsius to Fahrenheit Table
        System.out.println("Celsius   Fahrenheit");
        for (double celsius = 0; celsius <= 50; celsius += 10) {
            double fahrenheit = celsiusToFahrenheit(celsius);
            System.out.printf("%.1f       %.4f\n", celsius, fahrenheit);
        }

        System.out.println(); // Blank line between the tables

        // Printing the Fahrenheit to Celsius Table
        System.out.println("Fahrenheit   Celsius");
        for (double fahrenheit = 32; fahrenheit <= 122; fahrenheit += 10) {
            double celsius = fahrenheitToCelsius(fahrenheit);
            System.out.printf("%.1f         %.4f\n", fahrenheit, celsius);
        }
    }
}


