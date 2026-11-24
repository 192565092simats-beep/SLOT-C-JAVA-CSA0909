/**
 * Multilevel subclass: Vehicle -> Car -> ElectricCar
 */
public class ElectricCar extends Car {
    private final int batteryCapacityKWh;

    public ElectricCar(String regNo, int batteryCapacityKWh) {
        super(regNo); // Invokes Car constructor, which in turn invokes Vehicle constructor
        this.batteryCapacityKWh = batteryCapacityKWh;
        System.out.println("    [3. ElectricCar Constructor] ElectricCar ready with " + batteryCapacityKWh + " kWh battery.");
    }

    public int getBatteryCapacityKWh() {
        return batteryCapacityKWh;
    }

    public double calculateChargingFee(int kWh) {
        return kWh * 12.0; // Rs 12 per kWh
    }
}
