import java.util.ArrayList;
import java.util.Arrays;

/**
 * Task 2 - Data Representation & Type Handling
 * Campus Smart-Parking System
 * 
 * Demonstrates:
 * 1. Data type choices for core parking entities
 * 2. Casting/Integer division precision bugs in billing calculation
 * 3. Jagged Array vs Flat 1D Array vs ArrayList<String[]> representation
 */
public class DataRepresentationDemo {

    public static void demonstrateBillingPrecisionLoss() {
        System.out.println("--- 1. Billing Precision Loss & Integer Division Pitfall ---");
        int minutesParked = 65; // 1 hour and 5 minutes. Business rule: Any started hour charged in full -> 2 hours.
        double ratePerHour = 30.0;

        // Pitfall 1: Truncating integer division
        int truncatedHours = minutesParked / 60; // 65 / 60 = 1 (fraction 5/60 discarded!)
        double buggyFee1 = truncatedHours * ratePerHour; // Rs 30.0

        // Pitfall 2: Cast after integer division
        double buggyFee2 = (double)(minutesParked / 60) * ratePerHour; // Still 1.0 * 30.0 = Rs 30.0

        // Correct Design 1: Integer arithmetic ceiling without floating point errors
        int correctBillableHoursInt = (minutesParked + 59) / 60; // (65 + 59) / 60 = 124 / 60 = 2
        double correctFeeInt = correctBillableHoursInt * ratePerHour; // Rs 60.0

        // Correct Design 2: Math.ceil using floating point conversion
        double correctHoursFloat = Math.ceil(minutesParked / 60.0);
        double correctFeeFloat = correctHoursFloat * ratePerHour; // Rs 60.0

        System.out.printf("Duration: %d minutes, Rate: Rs %.1f/hr%n", minutesParked, ratePerHour);
        System.out.printf("  [WRONG] Integer division (65 / 60 * 30.0):         Rs %.2f (Undercharged by Rs 30.0)%n", buggyFee1);
        System.out.printf("  [WRONG] Cast after int division ((double)(65/60)): Rs %.2f%n", buggyFee2);
        System.out.printf("  [CORRECT] Integer ceiling ((65 + 59) / 60 * 30.0):  Rs %.2f%n", correctFeeInt);
        System.out.printf("  [CORRECT] Math.ceil(65 / 60.0) * 30.0:             Rs %.2f%n", correctFeeFloat);
    }

    public static void demonstrateArrayRepresentations() {
        System.out.println("\n--- 2. Floor & Slot Array Representations ---");
        // Floor 0 has 2 slots, Floor 1 has 3 slots, Floor 2 has 1 slot (Jagged layout)
        int[] slotsPerFloor = {2, 3, 1};

        // Model A: Jagged Array (String[floor][slot])
        String[][] jaggedSlots = new String[slotsPerFloor.length][];
        for (int f = 0; f < slotsPerFloor.length; f++) {
            jaggedSlots[f] = new String[slotsPerFloor[f]];
        }
        jaggedSlots[0][1] = "TN01AB1111";
        jaggedSlots[1][2] = "TN02CD2222";
        System.out.println("Model A (Jagged Array): Successfully allocates exact memory per floor.");
        for (int f = 0; f < jaggedSlots.length; f++) {
            System.out.println("  Floor " + f + " capacity: " + jaggedSlots[f].length + " slots -> " + Arrays.toString(jaggedSlots[f]));
        }

        // Model B: Flat 1D Array with fixed multiplier (index = floor * maxSlotsPerFloor + slot)
        // Requires using the MAX capacity across all floors (3 slots)
        int maxSlots = 3;
        String[] flat1D = new String[slotsPerFloor.length * maxSlots]; // 3 * 3 = 9 elements (3 wasted cells!)
        // Place TN01AB1111 at floor 0, slot 1: index = 0 * 3 + 1 = 1
        flat1D[0 * maxSlots + 1] = "TN01AB1111";
        // Place TN02CD2222 at floor 1, slot 2: index = 1 * 3 + 2 = 5
        flat1D[1 * maxSlots + 2] = "TN02CD2222";
        System.out.println("\nModel B (Flat 1D with Max Stride):");
        System.out.println("  Total cells allocated: " + flat1D.length + " (Actual slots needed: 6, Wasted: 3 cells)");

        // Model C: ArrayList<String[]> (Dynamic List of Floor Arrays)
        ArrayList<String[]> dynamicFloors = new ArrayList<>();
        for (int slots : slotsPerFloor) {
            dynamicFloors.add(new String[slots]);
        }
        dynamicFloors.get(0)[1] = "TN01AB1111";
        dynamicFloors.get(1)[2] = "TN02CD2222";
        System.out.println("\nModel C (ArrayList<String[]>): Dynamic floor expansion support.");
        System.out.println("  Number of floors: " + dynamicFloors.size() + ", Floor 1 slot 2: " + dynamicFloors.get(1)[2]);
    }

    public static void main(String[] args) {
        System.out.println("==========================================================");
        System.out.println("       TASK 2: Choose the Data Representation             ");
        System.out.println("==========================================================");
        demonstrateBillingPrecisionLoss();
        demonstrateArrayRepresentations();
        System.out.println("==========================================================");
    }
}
