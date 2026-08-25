
public class p1q4 {
    public static void main(String[] args) {
        int[] my_array = {25, 14, 56, 15, 36, 56, 77, 18, 29, 49};
        System.out.println("Original array: " + java.util.Arrays.toString(my_array));

        int indexToRemove = 1; // Index of the second number (14)
        for (int i = indexToRemove; i < my_array.length - 1; i++) {
            my_array[i] = my_array[i + 1]; // Shift elements to the left
        }

        System.out.println("After removing the second number: " + java.util.Arrays.toString(my_array));
    }
}
