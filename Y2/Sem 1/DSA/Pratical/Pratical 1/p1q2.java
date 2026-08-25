import java.util.Scanner;

public class p1q2 {
    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);

        System.out.print("Input an integer between 0 and 1000: ");
        int num = sc.nextInt();

        int firstDigit = num % 10;
        int remaining = num / 10;

        int secondDigit = remaining % 10;
        int thirdDigit = remaining / 10;

        int fourthDigit = 0; // Since the number is between 0 and 1000, the fourth digit will always be 0
        
        int sum = firstDigit + secondDigit + thirdDigit + fourthDigit;
        System.out.println("The sum of the digits is: " + sum);
    }
}