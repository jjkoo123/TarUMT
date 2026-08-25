import java.util.Scanner;

public class q1 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter the number of quiz scores: ");
        int numScores = sc.nextInt();

        double[] scores = new double[numScores];

        for (int i = 0; i < numScores; i++) {
            System.out.print("Score " + (i + 1) + ": ");
            scores[i] = sc.nextDouble();

        }

        double sum = 0;
        for (int i = 0; i < numScores; i++) {
            sum += scores[i];
        }
        double average = sum / numScores;

        int aboveOrEqual = 0;
        int below = 0;
        for (int i = 0; i < numScores; i++) {
            if (scores[i] >= average) {
                aboveOrEqual++;
            } else {
                below++;
            }
        }

        System.out.println("\nResults");
        System.out.println("=======");
        System.out.println("Average is " + average);
        System.out.println("Number of scores above or equal to the average is " + aboveOrEqual);
        System.out.println("Number of scores below the average is " + below);

        // Close the scanner
        sc.close();
    }
}
