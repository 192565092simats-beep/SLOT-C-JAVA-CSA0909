import java.util.*;

/**
 * 5-Floor Parking Facility with Variable Slots per Floor
 * Floor 0: 30 slots (Ground / High Turnaround)
 * Floor 1: 25 slots
 * Floor 2: 20 slots
 * Floor 3: 15 slots
 * Floor 4: 10 slots (Rooftop)
 */
public class DynamicParkingFacility {

    private final String[][] floorSlots; // Jagged array
    private final Map<String, Vehicle> parkedVehicles;
    private final Map<String, OneDayPass> activeDayPasses;
    private final Map<String, Integer> entryHours;

    public DynamicParkingFacility(int[] slotsPerFloor) {
        if (slotsPerFloor.length != 5) {
            System.out.println("Notice: Initializing facility with " + slotsPerFloor.length + " floors.");
        }
        this.floorSlots = new String[slotsPerFloor.length][];
        for (int f = 0; f < slotsPerFloor.length; f++) {
            this.floorSlots[f] = new String[slotsPerFloor[f]];
        }
        this.parkedVehicles = new HashMap<>();
        this.activeDayPasses = new HashMap<>();
        this.entryHours = new HashMap<>();
    }

    public synchronized int[] park(Vehicle vehicle, int entryHour, OneDayPass dayPass) {
        String reg = vehicle.getRegNo();
        if (parkedVehicles.containsKey(reg)) {
            throw new IllegalStateException("Vehicle " + reg + " is already inside facility.");
        }

        // Jagged array linear search across 5 floors
        for (int f = 0; f < floorSlots.length; f++) {
            for (int s = 0; s < floorSlots[f].length; s++) {
                if (floorSlots[f][s] == null) {
                    floorSlots[f][s] = reg;
                    parkedVehicles.put(reg, vehicle);
                    entryHours.put(reg, entryHour);
                    if (dayPass != null) {
                        activeDayPasses.put(reg, dayPass);
                    }
                    return new int[]{f, s};
                }
            }
        }
        return null; // Full
    }

    public synchronized double exit(String regNo, int durationMinutes) {
        String cleanReg = regNo.toUpperCase().trim();
        Vehicle vehicle = parkedVehicles.remove(cleanReg);
        if (vehicle == null) {
            throw new NoSuchElementException("Vehicle " + cleanReg + " not found.");
        }

        int entryHour = entryHours.remove(cleanReg);
        OneDayPass pass = activeDayPasses.remove(cleanReg);

        // Release slot in jagged array
        outer:
        for (int f = 0; f < floorSlots.length; f++) {
            for (int s = 0; s < floorSlots[f].length; s++) {
                if (cleanReg.equals(floorSlots[f][s])) {
                    floorSlots[f][s] = null;
                    break outer;
                }
            }
        }

        // Check if visitor has One-Day Pass
        if (pass != null && pass.isValidForDay()) {
            System.out.println("  [PASS APPLIED] One-Day Pass active (" + pass.getPassId() + "). Hourly fee waived!");
            return 0.0;
        }

        return vehicle.calculateFee(durationMinutes, entryHour);
    }

    public void printFacilityLayout() {
        System.out.println("Facility Layout (5 Floors with Variable Slots):");
        for (int f = 0; f < floorSlots.length; f++) {
            int occupied = 0;
            for (String s : floorSlots[f]) {
                if (s != null) occupied++;
            }
            System.out.printf("  Floor %d: Capacity = %2d slots | Occupied = %2d | Free = %2d%n",
                    f, floorSlots[f].length, occupied, floorSlots[f].length - occupied);
        }
    }
}
