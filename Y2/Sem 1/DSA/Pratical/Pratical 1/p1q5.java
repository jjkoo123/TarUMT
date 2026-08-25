
import java.util.ArrayList;
import java.util.List;

public class p1q5 {
    


    public static void main(String[] args){

        

        List<Integer> arr = new ArrayList<>();
        arr.add(25);
        arr.add(14);
        arr.add(39);
        arr.add(49);

        arr.remove(1); // Remove the second number (14)
        System.out.println(arr);
    }
}
