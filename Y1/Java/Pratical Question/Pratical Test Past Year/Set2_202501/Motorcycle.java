public class Motorcycle extends Vehicle {
    private boolean hasSidecar;

    public Motorcycle(String make, String model, int year, double mileage, int quantityOnHand, boolean hasSidecar) {
        super(make, model, year, mileage, quantityOnHand);
        this.hasSidecar = hasSidecar;
    }

    public boolean getHasSidecar() {
        return hasSidecar;
    }

    public void setHasSidecar(boolean hasSidecar) {
        this.hasSidecar = hasSidecar;
    }

    public double calculateSidecar() {
        if (hasSidecar) {
            return 35.5;

        } else {
            return 60.0;
        }
    }

    public String toString() {
        return super.toString() + "\nHas Sidecar: " + hasSidecar;
    }
}
