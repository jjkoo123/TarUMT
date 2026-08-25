public abstract class PatientBill {
    protected int visitID;
    protected String name;
    private static int lastAssignedID = 1001;

    public PatientBill(String name) {
        this.name = name;
        this.visitID = lastAssignedID++;
        lastAssignedID++;
    }
    @Override
    public String toString() {
        return "Patient Name: " + name; 
    }

    public abstract double calculateTotalCharges();

    public int compareTo(Object o){
        PatientBill otherBill = (PatientBill) o;

        return name.compareTo(otherBill.name);
    }

}