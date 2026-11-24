/**
 * New Category 1: E-Bike charged a flat fee regardless of duration
 */
public class EBike extends Vehicle {
    private static final double FLAT_FEE = 15.0; // Rs 15 flat fee

    public EBike(String regNo) {
        super(regNo);
    }

    @Override
    public double calculateFee(int durationMinutes, int entryHour) {
        if (durationMinutes <= 0) return 0.0;
        return FLAT_FEE;
    }
}
