class Average{
    public double calculateAverage(int a, int b){
        return (a + b) / 2.0;
    }

        public double calculateAverage(int a, int b, int c){
        return (a + b + c) / 3.0;
    }

    public double calculateAverage(double a, double b){
        return (a + b) / 2.0;
    }

        public double calculateAverage(double a, double b, double c){
        return (a + b + c) / 3.0;
    }
}


public class p2q4{
    public static void main(String[] args){
        Average avg = new Average();

        System.out.println("Average of 1 + 2: " + avg.calculateAverage(1, 2));
        System.out.println("Average of 1 + 2 + 3: " + avg.calculateAverage(1, 2, 3));
        System.out.println("Average of 1.5 + 2.5: " + avg.calculateAverage(1.5, 2.5));
        System.out.println("Average of 1.5 + 2.5 + 3.5: " + avg.calculateAverage(1.5, 2.5, 3.5));
    }
}