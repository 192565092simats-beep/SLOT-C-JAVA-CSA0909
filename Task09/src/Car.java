public class Car extends Vehicle implements Payable {
    private int durationMinutes;

    public Car(String regNo) {
        super(regNo);
    }

    public void setDurationMinutes(int durationMinutes) {
        this.durationMinutes = durationMinutes;
    }

    @Override
    protected double ratePerHour() {
        return 30.0;
    }

    @Override
    public double getPayableAmount() {
        return calculateFee(durationMinutes);
    }
}
