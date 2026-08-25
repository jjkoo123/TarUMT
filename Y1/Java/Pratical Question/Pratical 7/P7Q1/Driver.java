public class Driver{
    public static void main(String[] args) {
        PatientBill[] bills = new PatientBill[4];
        bills[0] = new OutPatientBill("John Doe", 30.00);
        bills[1] = new InPatientBill("Jane Smith", 500.00, 50.00, 'S', 8);
        bills[2] = new OutPatientBill("Alice Johnson", 50.00);
        bills[3] = new InPatientBill("Bob Brown", 200.00, 12.50, 'P', 10);
    

    for(int i = 0; i < bills.length; i++){
        System.out.println("\n" +bills [i]);
        System.out.println("Total Charges: " + bills[i].calculateTotalCharges());
        }

        System.out.println("\nTotal Bills: RM%.2f" + computeTotalCharges(bills));
    }

    public static double computeTotalCharges(PatientBill[] arr) {
        double total = 0.0;

        for (int i = 0; i < arr.length; i++) {
            total += arr[i].calculateTotalCharges();
        }
        return total;
    }
}