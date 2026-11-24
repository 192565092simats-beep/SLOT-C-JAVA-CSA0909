import java.util.*;

/**
 * Task 11 - Comprehensive Test Harness & Performance Benchmark
 */
public class TestHarness {

    public static void runValidationSuite() {
        System.out.println("==========================================================");
        System.out.println("    TASK 11: Implement, Test and Defend the Design        ");
        System.out.println("==========================================================");

        // 1. Normal Operation
        System.out.println("\n[TEST GROUP 1] Normal Operation (Different vehicles entering & leaving):");
        CampusParkingSystem parking = new CampusParkingSystem(new int[]{2, 2}); // 4 total slots

        var t1 = parking.parkVehicle("TN01A1000", CampusParkingSystem.VehicleType.TWO_WHEELER, CampusParkingSystem.UserType.VISITOR);
        var t2 = parking.parkVehicle("TN02B2000", CampusParkingSystem.VehicleType.CAR, CampusParkingSystem.UserType.VISITOR);
        var t3 = parking.parkVehicle("TN03C3000", CampusParkingSystem.VehicleType.EV, CampusParkingSystem.UserType.VISITOR);
        System.out.println("  Parked 3 vehicles successfully. Remaining slots: " + parking.getFreeSlotCount());

        double f1 = parking.processExit("TN01A1000", 60, 0); // 1 hr @ 10 = Rs 10.0
        System.out.printf("  Exit TN01A1000 (Two-Wheeler, 60m): Rs %.1f (Expected: 10.0)%n", f1);
        double f2 = parking.processExit("TN02B2000", 120, 0); // 2 hrs @ 30 = Rs 60.0
        System.out.printf("  Exit TN02B2000 (Car, 120m):         Rs %.1f (Expected: 60.0)%n", f2);
        double f3 = parking.processExit("TN03C3000", 90, 10); // 2 hrs @ 30 + 10 kWh * 12 = 60 + 120 = Rs 180.0
        System.out.printf("  Exit TN03C3000 (EV, 90m + 10kWh):   Rs %.1f (Expected: 180.0)%n", f3);

        // 2. Boundary Cases
        System.out.println("\n[TEST GROUP 2] Boundary Cases:");
        // Fill to capacity
        CampusParkingSystem smallLot = new CampusParkingSystem(new int[]{1, 1}); // 2 slots
        smallLot.parkVehicle("TN10AA0001", CampusParkingSystem.VehicleType.CAR, CampusParkingSystem.UserType.VISITOR);
        var lastSlot = smallLot.parkVehicle("TN10AA0002", CampusParkingSystem.VehicleType.CAR, CampusParkingSystem.UserType.VISITOR);
        System.out.println("  Last free slot allocated: " + lastSlot);
        var overflow = smallLot.parkVehicle("TN10AA0003", CampusParkingSystem.VehicleType.CAR, CampusParkingSystem.UserType.VISITOR);
        System.out.println("  Allocation when lot full: " + overflow + " (Expected: null)");

        // Zero-minute parking
        double zeroMinFee = smallLot.processExit("TN10AA0001", 0, 0);
        System.out.printf("  Zero-minute parking fee: Rs %.1f (Expected: 0.0)%n", zeroMinFee);

        // Exact fee-slab boundary (60 min vs 61 min)
        smallLot.parkVehicle("TN10AA0001", CampusParkingSystem.VehicleType.CAR, CampusParkingSystem.UserType.VISITOR);
        double boundary60 = smallLot.processExit("TN10AA0001", 60, 0); // exactly 1 hr -> Rs 30.0
        System.out.printf("  Stay exactly at 60 min:  Rs %.1f (Expected: 30.0)%n", boundary60);

        smallLot.parkVehicle("TN10AA0001", CampusParkingSystem.VehicleType.CAR, CampusParkingSystem.UserType.VISITOR);
        double boundary61 = smallLot.processExit("TN10AA0001", 61, 0); // 1 hr 1 min -> 2 hrs -> Rs 60.0
        System.out.printf("  Stay at 61 min (1 hr 1m): Rs %.1f (Expected: 60.0)%n", boundary61);

        // 3. Invalid Cases
        System.out.println("\n[TEST GROUP 3] Invalid Cases:");
        try {
            smallLot.parkVehicle("TN10AA0002", CampusParkingSystem.VehicleType.CAR, CampusParkingSystem.UserType.VISITOR);
        } catch (IllegalStateException ex) {
            System.out.println("  Duplicate regNo caught: " + ex.getMessage());
        }

        try {
            smallLot.processExit("TN99NONEXISTENT", 60, 0);
        } catch (NoSuchElementException ex) {
            System.out.println("  Non-existent ticket exit caught: " + ex.getMessage());
        }

        try {
            smallLot.processExit("TN10AA0002", -15, 0);
        } catch (IllegalArgumentException ex) {
            System.out.println("  Negative duration caught: " + ex.getMessage());
        }

        // 4. Hand-Calculated Reconciliation Scenario
        System.out.println("\n[TEST GROUP 4] Hand-Calculated Daily Revenue Reconciliation:");
        CampusParkingSystem dayFacility = new CampusParkingSystem(new int[]{3, 3});

        // Scenario:
        // V1: Visitor Car, 120 min -> 2 hrs * 30 = Rs 60.0
        // V2: Staff Car, 180 min -> Pass holder = Rs 0.0
        // V3: Visitor Two-Wheeler, 60 min -> 1 hr * 10 = Rs 10.0
        // V4: Visitor EV, 90 min (2 hrs * 30 = 60) + 15 kWh * 12 (180) = Rs 240.0
        // Hand total = 60 + 0 + 10 + 240 = Rs 310.0
        dayFacility.parkVehicle("TN01-CAR", CampusParkingSystem.VehicleType.CAR, CampusParkingSystem.UserType.VISITOR);
        dayFacility.parkVehicle("TN02-STAFF", CampusParkingSystem.VehicleType.CAR, CampusParkingSystem.UserType.STAFF);
        dayFacility.parkVehicle("TN03-BIKE", CampusParkingSystem.VehicleType.TWO_WHEELER, CampusParkingSystem.UserType.VISITOR);
        dayFacility.parkVehicle("TN04-EV", CampusParkingSystem.VehicleType.EV, CampusParkingSystem.UserType.VISITOR);

        double rev1 = dayFacility.processExit("TN01-CAR", 120, 0);
        double rev2 = dayFacility.processExit("TN02-STAFF", 180, 0);
        double rev3 = dayFacility.processExit("TN03-BIKE", 60, 0);
        double rev4 = dayFacility.processExit("TN04-EV", 90, 15);

        double actualTotal = dayFacility.getTotalDailyRevenue();
        double expectedTotal = 310.0;
        System.out.println("  Hand-Calculated Expected Total: Rs " + expectedTotal);
        System.out.println("  System-Computed Actual Total:   Rs " + actualTotal);
        System.out.println("  Reconciliation Match: " + (Double.compare(actualTotal, expectedTotal) == 0 ? "PASSED (100% Exact Match)" : "FAILED"));

        // 5. Alternative Concept Performance Benchmark
        benchmarkExitLookup();
        System.out.println("==========================================================");
    }

    public static void benchmarkExitLookup() {
        System.out.println("\n[BENCHMARK] Active Tickets Storage Structure Comparison (Exit Search Time):");
        int N = 50000;
        CampusParkingSystem.Ticket[] ticketArray = new CampusParkingSystem.Ticket[N];
        List<CampusParkingSystem.Ticket> ticketList = new ArrayList<>(N);
        Map<String, CampusParkingSystem.Ticket> ticketMap = new HashMap<>(N);

        for (int i = 0; i < N; i++) {
            String reg = "TN" + String.format("%06d", i);
            CampusParkingSystem.Ticket t = new CampusParkingSystem.Ticket("TKT-" + i, reg,
                    CampusParkingSystem.VehicleType.CAR, CampusParkingSystem.UserType.VISITOR, 0, i);
            ticketArray[i] = t;
            ticketList.add(t);
            ticketMap.put(reg, t);
        }

        String searchTarget = "TN" + String.format("%06d", N - 1); // Worst case: at the end

        // 1. Array Linear Search
        long start = System.nanoTime();
        CampusParkingSystem.Ticket found1 = null;
        for (CampusParkingSystem.Ticket t : ticketArray) {
            if (t.getRegNo().equals(searchTarget)) {
                found1 = t;
                break;
            }
        }
        long arrayTime = System.nanoTime() - start;

        // 2. ArrayList Linear Search
        start = System.nanoTime();
        CampusParkingSystem.Ticket found2 = null;
        for (CampusParkingSystem.Ticket t : ticketList) {
            if (t.getRegNo().equals(searchTarget)) {
                found2 = t;
                break;
            }
        }
        long listTime = System.nanoTime() - start;

        // 3. HashMap O(1) Lookup
        start = System.nanoTime();
        CampusParkingSystem.Ticket found3 = ticketMap.get(searchTarget);
        long mapTime = System.nanoTime() - start;

        System.out.printf("  N = %d active parked vehicles (Worst-case exit search):%n", N);
        System.out.printf("    - Raw Array Linear Scan:   %8d ns (O(N))%n", arrayTime);
        System.out.printf("    - ArrayList Linear Scan:   %8d ns (O(N))%n", listTime);
        System.out.printf("    - HashMap O(1) Key Lookup: %8d ns (O(1) - ~%dx faster!)%n", mapTime, (arrayTime / Math.max(1, mapTime)));
    }

    public static void main(String[] args) {
        runValidationSuite();
    }
}
