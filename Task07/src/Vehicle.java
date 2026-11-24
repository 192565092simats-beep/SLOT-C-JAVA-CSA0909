import java.util.Objects;

/**
 * Task 7 - Superclass demonstrating proper Object-class overrides and final usage
 */
public abstract class Vehicle {
    // 1. final variable: registration number is immutable once issued
    private final String regNo;

    protected Vehicle(String regNo) {
        this.regNo = Objects.requireNonNull(regNo, "regNo cannot be null").toUpperCase().trim();
    }

    // 2. final method: Identity accessor cannot be corrupted by subclasses
    public final String getRegNo() {
        return regNo;
    }

    protected abstract double ratePerHour();

    public double calculateFee(int minutes) {
        int hours = (minutes + 59) / 60;
        return hours * ratePerHour();
    }

    // 3. Overriding toString() for human-readable diagnostic output
    @Override
    public String toString() {
        return String.format("%s[%s, Rate=Rs %.1f/hr]", getClass().getSimpleName(), regNo, ratePerHour());
    }

    // 4. Overriding equals(Object o) based on unique registration number
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Vehicle other)) return false;
        return Objects.equals(this.regNo, other.regNo);
    }

    // 5. Overriding hashCode() to maintain general contract with equals()
    @Override
    public int hashCode() {
        return Objects.hash(regNo);
    }
}
