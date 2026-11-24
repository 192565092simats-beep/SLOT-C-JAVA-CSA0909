import java.util.Objects;

/**
 * Task 1 - Problem Analysis & Objects
 * Campus Smart-Parking System
 * 
 * Demonstrates:
 * 1. Object-Oriented Vehicle representation vs. Parallel Arrays representation
 * 2. Search by registration number in both representations
 * 3. Adding a new attribute (extensibility comparison)
 */
public class ProblemAnalysisDemo {

    // --- Design A: Object-Oriented Representation ---
    static class VehicleObject {
        private final String regNo;
        private final String type;
        private final long entryTime;
        // Adding a new attribute in OOP is cleanly encapsulated
        private String ownerCategory; // e.g. "STUDENT", "STAFF", "VISITOR"

        public VehicleObject(String regNo, String type, long entryTime, String ownerCategory) {
            this.regNo = Objects.requireNonNull(regNo, "Registration number cannot be null");
            this.type = Objects.requireNonNull(type, "Type cannot be null");
            this.entryTime = entryTime;
            this.ownerCategory = ownerCategory;
        }

        public String getRegNo() { return regNo; }
        public String getType() { return type; }
        public long getEntryTime() { return entryTime; }
        public String getOwnerCategory() { return ownerCategory; }
        public void setOwnerCategory(String category) { this.ownerCategory = category; }

        @Override
        public String toString() {
            return String.format("VehicleObject[regNo=%s, type=%s, entryTime=%d, owner=%s]",
                    regNo, type, entryTime, ownerCategory);
        }
    }

    // --- Design B: Parallel Arrays Representation ---
    static class ParallelArraysParking {
        String[] regNos;
        String[] types;
        long[] entryTimes;
        // Adding a new attribute requires adding an entire parallel array
        String[] ownerCategories;
        int count;

        public ParallelArraysParking(int capacity) {
            regNos = new String[capacity];
            types = new String[capacity];
            entryTimes = new long[capacity];
            ownerCategories = new String[capacity];
            count = 0;
        }

        public void addVehicle(String regNo, String type, long entryTime, String owner) {
            regNos[count] = regNo;
            types[count] = type;
            entryTimes[count] = entryTime;
            ownerCategories[count] = owner;
            count++;
        }

        public int findByRegNo(String regNo) {
            for (int i = 0; i < count; i++) {
                if (regNos[i].equalsIgnoreCase(regNo)) {
                    return i; // index into all parallel arrays
                }
            }
            return -1;
        }
    }

    public static void main(String[] args) {
        System.out.println("==========================================================");
        System.out.println(" TASK 1: Understand the Problem and Identify the Objects ");
        System.out.println("==========================================================");

        // 1. Instantiate Object-Oriented Model
        VehicleObject[] objectFleet = new VehicleObject[] {
            new VehicleObject("TN01AB1111", "CAR", 1000L, "STAFF"),
            new VehicleObject("TN02CD2222", "TWO_WHEELER", 1020L, "STUDENT"),
            new VehicleObject("TN03EF3333", "EV", 1045L, "VISITOR")
        };

        System.out.println("\n[Design A - OOP Model] Parked vehicles:");
        for (VehicleObject v : objectFleet) {
            System.out.println("  " + v);
        }

        // Search in OOP Model
        String searchTarget = "TN02CD2222";
        VehicleObject foundObj = null;
        for (VehicleObject v : objectFleet) {
            if (v.getRegNo().equalsIgnoreCase(searchTarget)) {
                foundObj = v;
                break;
            }
        }
        System.out.println("\nSearch for '" + searchTarget + "' in OOP: " + foundObj);

        // 2. Instantiate Parallel Arrays Model
        ParallelArraysParking parallelFleet = new ParallelArraysParking(10);
        parallelFleet.addVehicle("TN01AB1111", "CAR", 1000L, "STAFF");
        parallelFleet.addVehicle("TN02CD2222", "TWO_WHEELER", 1020L, "STUDENT");
        parallelFleet.addVehicle("TN03EF3333", "EV", 1045L, "VISITOR");

        int index = parallelFleet.findByRegNo(searchTarget);
        System.out.println("\n[Design B - Parallel Arrays] Search for '" + searchTarget + "':");
        if (index != -1) {
            System.out.println("  Found at index " + index + ": regNo=" + parallelFleet.regNos[index]
                    + ", type=" + parallelFleet.types[index]
                    + ", entryTime=" + parallelFleet.entryTimes[index]
                    + ", owner=" + parallelFleet.ownerCategories[index]);
        }

        // 3. Extensibility Comparison Demonstration
        System.out.println("\nExtensibility Comparison:");
        System.out.println("  - OOP: Simply added field 'ownerCategory' to VehicleObject class.");
        System.out.println("         All existing constructor/method references remain cohesive.");
        System.out.println("  - Parallel Arrays: Required creating a brand-new array 'ownerCategories[]'");
        System.out.println("         and updating every method signature, loop, resize, and indexing logic.");
        System.out.println("==========================================================");
    }
}
