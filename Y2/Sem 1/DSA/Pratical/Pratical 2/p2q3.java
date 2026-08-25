
public class p2q3 {
    
    // Question 3: Using indexOf() method
    public static void searchWithIndexOf() {
        String text = "Hello World";
        String word = "World";
        
        int index = text.indexOf(word);
        
        if (index != -1) {
            System.out.println("Found " + word + " at index " + index);
        } else {
            System.out.println("Word not found");
        }
    }
    
    // Question 3.1: Using pattern matching
    public static void searchWithPatternMatching() {
        String text = "Hello World";
        String word = "World";
        
        if (text.contains(word)) {
            int index = text.indexOf(word);
            System.out.println("Found " + word + " at index " + index);
        } else {
            System.out.println("Word not found");
        }
    }
    
    public static void main(String[] args) {
        System.out.println("=== Question 3: Using indexOf() ===");
        searchWithIndexOf();
        
        System.out.println("\n=== Question 3.1: Using Pattern Matching ===");
        searchWithPatternMatching();
    }
}
