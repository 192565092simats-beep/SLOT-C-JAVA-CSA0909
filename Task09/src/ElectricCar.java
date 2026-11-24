/**
 * Concrete class implementing multiple interfaces (Chargeable, Payable, Displayable via Vehicle)
 */
public class ElectricCar extends Car implements Chargeable {
    private int consumedKWh;

    public ElectricCar(String regNo) {
        super(regNo);
    }

    public void setConsumedKWh(int kWh) {
        this.consumedKWh = kWh;
    }

    @Override
    public double chargingFee(int kWh) {
        return kWh * DEFAULT_CHARGING_RATE;
    }

    @Override
    public double getPayableAmount() {
        return super.getPayableAmount() + chargingFee(consumedKWh);
    }

    @Override
    public void displaySummary() {
        super.displaySummary();
        System.out.printf("    Includes EV Capability: Fast Charging @ %s/kWh%n",
                Displayable.formatCurrency(DEFAULT_CHARGING_RATE));
    }
}
