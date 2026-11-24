import java.util.Objects;

/**
 * Task 12 - Abstract Base Vehicle
 */
public abstract class Vehicle {
    private final String regNo;

    protected Vehicle(String regNo) {
        this.regNo = Objects.requireNonNull(regNo, "regNo cannot be null").toUpperCase().trim();
    }

    public String getRegNo() {
        return regNo;
    }

    public abstract double calculateFee(int durationMinutes, int entryHour);

    @Override
    public boolean equals(Object o) {
        return o instanceof Vehicle other && regNo.equals(other.regNo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(regNo);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [" + regNo + "]";
    }
}
