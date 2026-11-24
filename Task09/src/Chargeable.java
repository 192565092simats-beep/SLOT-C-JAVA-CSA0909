/**
 * Capability interface for electric vehicles and charging hardware
 */
public interface Chargeable {
    double DEFAULT_CHARGING_RATE = 12.0; // static final by default in interfaces

    double chargingFee(int kWh);

    // Default method providing fallback calculation
    default double estimateChargingTimeMinutes(int currentKWh, int targetKWh) {
        int needed = Math.max(0, targetKWh - currentKWh);
        return (needed / 22.0) * 60.0; // Assuming 22 kW fast AC charger
    }
}
