public class ElectricCar extends Car {
    private final int batteryKWh;

    public ElectricCar(String regNo, int batteryKWh) {
        super(regNo);
        this.batteryKWh = batteryKWh;
    }

    // Specializing calculateFee to include EV charging consumption
    public double calculateFee(int minutes, int kWhConsumed) {
        double parkingFee = super.calculateFee(minutes);
        double chargingFee = kWhConsumed * 12.0;
        return parkingFee + chargingFee;
    }
}
