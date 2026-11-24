/**
 * Abstract class representing core vehicle state and billing template
 */
public abstract class Vehicle implements Displayable {
    private final String regNo;

    protected Vehicle(String regNo) {
        this.regNo = regNo;
    }

    public String getRegNo() {
        return regNo;
    }

    protected abstract double ratePerHour();

    public double calculateFee(int minutes) {
        int hours = (minutes + 59) / 60;
        return hours * ratePerHour();
    }

    @Override
    public void displaySummary() {
        System.out.printf("  %s [%s] - Base Rate: %s/hr%n",
                getClass().getSimpleName(), regNo, Displayable.formatCurrency(ratePerHour()));
    }
}
