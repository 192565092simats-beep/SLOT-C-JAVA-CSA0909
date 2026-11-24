/**
 * Subclass extending Vehicle (Hierarchical branch)
 */
public class Car extends Vehicle {
    public Car(String regNo) {
        super(regNo);
        System.out.println("    [2. Car Constructor] Car instance ready.");
    }

    @Override
    public double ratePerHour() {
        return 30.0;
    }
}
