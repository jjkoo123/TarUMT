import java.util.Scanner;

public class p1q1 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter name: ");
        String name = sc.nextLine();

        System.out.print("How many subjects you have in this semester?: ");
        int subjects = sc.nextInt();
        sc.nextLine();

        System.out.print("What is the programme that you are currently studying?: ");
        String programme = sc.nextLine();

        System.out.println("\nWelcome " + name + " from " + programme + " !");
        System.out.println("Good luck in all " + subjects + " courses you have this semester!");
    }
}
