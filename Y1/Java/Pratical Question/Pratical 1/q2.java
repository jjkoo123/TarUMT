import java.util.Scanner;

public class q2 {
    private static final int meter_per_km = 1000;
    private static final int cm_per_km = 100000;
    private static final int mm_per_km = 1000000;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        double kilometers = readKilometers(sc);

        double meters = kilometers * meter_per_km;
        double cm = kilometers * cm_per_km;
        double mm = kilometers * mm_per_km;

        displayResults(kilometers, meters, cm, mm);

        sc.close();
    }

    public static double readKilometers(Scanner sc) {
        System.out.print("Enter distance in kilometers: ");
        return sc.nextDouble();
    }

    public static void displayResults(double km, double m, double cm, double mm) {
        System.out.println("\nDistance in kilometers: " + km + " kilometers");
        System.out.println("Distance in meters: " + m + " meters");
        System.out.println("Distance in centimeters: " + cm + " centimeters");
        System.out.println("Distance in millimeters: " + mm + " millimeters");
    }
}
