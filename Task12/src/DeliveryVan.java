/**
 * New Category 2: Delivery Van
 * Charged at double the car rate (2 * Rs 30 = Rs 60/hr) during 9 AM - 5 PM (Hours 9 to 17).
 * Standard rate (Rs 30/hr) during off-peak hours.
 */
public class DeliveryVan extends Vehicle {
    private static final double BASE_RATE_PER_HOUR = 30.0;

    public DeliveryVan(String regNo) {
        super(regNo);
    }

    @Override
    public double calculateFee(int durationMinutes, int entryHour) {
        if (durationMinutes <= 0) return 0.0;
        int hours = (durationMinutes + 59) / 60;

        // Peak hours: 9 AM (09:00) to 5 PM (17:00)
        boolean isPeak = (entryHour >= 9 && entryHour < 17);
        double applicableRate = isPeak ? (BASE_RATE_PER_HOUR * 2.0) : BASE_RATE_PER_HOUR;

        return hours * applicableRate;
    }
}
