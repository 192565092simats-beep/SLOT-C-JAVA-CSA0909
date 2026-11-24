import java.util.Arrays;

/**
 * Task 12 - Day-4 Design Challenge Demonstration
 */
public class DesignChallengeDemo {

    public static void main(String[] args) {
        System.out.println("==========================================================");
        System.out.println("  TASK 12: DAY-4 DESIGN CHALLENGE - RE-EVALUATE & ADAPT   ");
        System.out.println("==========================================================");

        // 1. Extended facility: 5 floors with variable capacities
        int[] fiveFloorsConfig = {30, 25, 20, 15, 10}; // Total = 100 slots
        DynamicParkingFacility facility = new DynamicParkingFacility(fiveFloorsConfig);
        facility.printFacilityLayout();

        System.out.println("\n--- Testing New Vehicle Category 1: E-Bike (Flat Fee Rs 15) ---");
        EBike ebikeShort = new EBike("EB-01");
        EBike ebikeLong = new EBike("EB-02");

        facility.park(ebikeShort, 8, null);
        facility.park(ebikeLong, 10, null);

        double feeEBike1 = facility.exit("EB-01", 30);  // 30 min -> Flat Rs 15
        double feeEBike2 = facility.exit("EB-02", 480); // 8 hours -> Flat Rs 15
        System.out.printf("  EBike 30 min stay:   Rs %.2f (Expected: Rs 15.00 flat)%n", feeEBike1);
        System.out.printf("  EBike 480 min stay:  Rs %.2f (Expected: Rs 15.00 flat)%n", feeEBike2);

        System.out.println("\n--- Testing New Vehicle Category 2: Delivery Van (Peak Hours Pricing) ---");
        // Delivery Van 1: Enters at 10 AM (Peak: 9 AM - 5 PM) -> Double car rate: 2 * 30 = Rs 60/hr
        DeliveryVan vanPeak = new DeliveryVan("VAN-PEAK-01");
        facility.park(vanPeak, 10, null);
        double feeVanPeak = facility.exit("VAN-PEAK-01", 120); // 2 hours @ 60 = Rs 120
        System.out.printf("  Van Peak (10 AM entry, 2 hrs): Rs %.2f (Expected: 2 * Rs 60 = Rs 120.00)%n", feeVanPeak);

        // Delivery Van 2: Enters at 7 PM (Off-Peak: outside 9 AM - 5 PM) -> Standard car rate: Rs 30/hr
        DeliveryVan vanOffPeak = new DeliveryVan("VAN-OFFPEAK-02");
        facility.park(vanOffPeak, 19, null);
        double feeVanOffPeak = facility.exit("VAN-OFFPEAK-02", 120); // 2 hours @ 30 = Rs 60
        System.out.printf("  Van Off-Peak (7 PM entry, 2 hrs): Rs %.2f (Expected: 2 * Rs 30 = Rs 60.00)%n", feeVanOffPeak);

        System.out.println("\n--- Testing Visitor One-Day Pass Feature ---");
        OneDayPass visitorPass = new OneDayPass("PASS-2026-001", "John Doe (Visitor)");
        System.out.println("  Issued: " + visitorPass);

        DeliveryVan vanWithPass = new DeliveryVan("VAN-PASS-99");
        facility.park(vanWithPass, 11, visitorPass); // Enters during peak hours with One-Day Pass
        double feeWithPass = facility.exit("VAN-PASS-99", 360); // 6 hours stay
        System.out.printf("  Exit fee with One-Day Pass: Rs %.2f (Hourly charge fully waived)%n", feeWithPass);

        System.out.println("\nFinal Facility Status:");
        facility.printFacilityLayout();
        System.out.println("==========================================================");
    }
}
