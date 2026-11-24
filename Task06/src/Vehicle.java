/**
 * Task 6 - Superclass in Inheritance Hierarchy
 */
public abstract class Vehicle {
    private final String regNo;

    public Vehicle(String regNo) {
        System.out.println("    [1. Vehicle Constructor] Initializing regNo: " + regNo);
        this.regNo = regNo;
    }

    public String getRegNo() {
        return regNo;
    }

    public abstract double ratePerHour();

    public double calculateFee(int minutes) {
        int hours = (minutes + 59) / 60;
        return hours * ratePerHour();
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [regNo=" + regNo + ", rate=" + ratePerHour() + "/hr]";
    }
}
