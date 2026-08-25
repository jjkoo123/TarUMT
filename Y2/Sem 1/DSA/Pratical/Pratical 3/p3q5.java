
public class p3q5 {
    public static void main(String[] args){
        System.out.println("Fibonacci Series up to 10numbers: ");
        int fibonnacciSeriesLength = 10;
        for(int i = 0; i < fibonnacciSeriesLength; i++){
            System.out.print(fibonacciNumber(i) + " ");
        }
    }

    public static int fibonacciNumber(int n){
        if(n==0){
            return 0;
        } else if(n==1){
            return 1;
        } else {
            return fibonacciNumber(n-1) + fibonacciNumber(n-2);
        }
    }
}
