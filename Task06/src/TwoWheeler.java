/**
 * Hierarchical subclass extending Vehicle directly
 */
public class TwoWheeler extends Vehicle {
    public TwoWheeler(String regNo) {
        super(regNo);
        System.out.println("    [2. TwoWheeler Constructor] TwoWheeler instance ready.");
    }

    @Override
    public double ratePerHour() {
        return 10.0;
    }
}
