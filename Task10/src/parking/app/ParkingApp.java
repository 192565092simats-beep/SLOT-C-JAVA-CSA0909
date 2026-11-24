package parking.app;

import parking.model.ParkingTicket;
import parking.model.UserCategory;
import parking.service.ParkingService;

/**
 * Task 10 - Package Encapsulation, Validation & Autoboxing Hazard Demo
 */
public class ParkingApp {

    public static void demonstrateAutoboxingRisk() {
        System.out.println("\n--- Wrapper Classes & Autoboxing Comparison Risks ---");
        // Values within [-128, 127] are cached by JVM Integer.valueOf()
        Integer cachedA = 100;
        Integer cachedB = 100;
        System.out.println("Cached Integers (100 == 100): " + (cachedA == cachedB) + " (Reference equality: TRUE due to IntegerCache)");

        // Values outside cache allocate separate heap objects
        Integer nonCachedA = 200;
        Integer nonCachedB = 200;
        System.out.println("Non-cached Integers (200 == 200 using '=='): " + (nonCachedA == nonCachedB) + " (Reference equality: FALSE! Risk of bug)");
        System.out.println("Non-cached Integers (200.equals(200)):        " + (nonCachedA.equals(nonCachedB)) + " (Value equality: TRUE, always use .equals())");

        // Unboxing null risk
        try {
            Integer nullWrapper = null;
            int unboxed = nullWrapper; // Throws NullPointerException!
        } catch (NullPointerException npe) {
            System.out.println("Unboxing null Integer threw: " + npe.getClass().getSimpleName() + " (Autoboxing NPE hazard)");
        }
    }

    public static void main(String[] args) {
        System.out.println("==========================================================");
        System.out.println(" TASK 10: Enforce Encapsulation through Packages & Modifiers");
        System.out.println("==========================================================");

        ParkingService service = new ParkingService(new int[]{2, 1});

        // 1. Valid Operations across packages
        System.out.println("\n1. Allocating valid parking slots:");
        ParkingTicket t1 = service.parkVehicle("TN01A1111", UserCategory.STAFF);
        ParkingTicket t2 = service.parkVehicle("TN02B2222", UserCategory.VISITOR);
        System.out.println("  Staff Ticket:   " + t1);
        System.out.println("  Visitor Ticket: " + t2);

        // 2. Encapsulated validation rejecting duplicate entry
        System.out.println("\n2. Validation Defense: Duplicate Registration Plate:");
        try {
            service.parkVehicle("TN01A1111", UserCategory.STUDENT);
        } catch (IllegalStateException ex) {
            System.out.println("  Caught expected validation error: " + ex.getMessage());
        }

        // 3. Encapsulated validation rejecting negative duration
        System.out.println("\n3. Validation Defense: Negative Checkout Duration:");
        try {
            service.processExit("TN01A1111", -45, 30.0);
        } catch (IllegalArgumentException ex) {
            System.out.println("  Caught expected validation error: " + ex.getMessage());
        }

        // 4. Successful Checkout and Fee Waiver
        double staffFee = service.processExit("TN01A1111", 120, 30.0);
        double visitorFee = service.processExit("TN02B2222", 120, 30.0);
        System.out.printf("%n4. Checkout Settlement:%n");
        System.out.printf("  Staff Fee (Monthly Pass Waiver):   Rs %.2f%n", staffFee);
        System.out.printf("  Visitor Fee (2 hrs @ Rs 30/hr):    Rs %.2f%n", visitorFee);

        // 5. Autoboxing hazards
        demonstrateAutoboxingRisk();
        System.out.println("==========================================================");
    }
}
