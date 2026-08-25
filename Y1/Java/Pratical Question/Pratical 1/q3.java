import java.util.Scanner;

public class q3 {


    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Ask the user to input an 8-digit number
        System.out.print("Enter an 8-digit credit card number: ");
        String cardNumber = scanner.nextLine();

        // Check if the input has exactly 8 digits
        if (cardNumber.length() != 8 || !cardNumber.matches("\\d+")) {
            System.out.println("Invalid input. Please enter an 8-digit number.");
            return;
        }

        // Step 1: Sum every other digit starting from the rightmost digit
        int sum1 = 0;
        for (int i = cardNumber.length() - 1; i >= 0; i -= 2) {
            sum1 += Character.getNumericValue(cardNumber.charAt(i));
        }

        // Step 2: Double each of the digits not included in the previous step
        int sum2 = 0;
        for (int i = cardNumber.length() - 2; i >= 0; i -= 2) {
            int doubled = Character.getNumericValue(cardNumber.charAt(i)) * 2;
            sum2 += (doubled > 9) ? (doubled - 9) : doubled;  // If double > 9, sum digits
        }

        // Step 3: Sum both results
        int totalSum = sum1 + sum2;

        // Step 4: Check if the last digit of the sum is 0 (valid number)
        if (totalSum % 10 == 0) {
            System.out.println("The credit card number is valid.");
        } else {
            System.out.println("The credit card number is invalid.");
        }

        scanner.close();
    }

}
