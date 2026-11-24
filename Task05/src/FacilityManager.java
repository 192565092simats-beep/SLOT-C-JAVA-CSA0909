/**
 * Task 5 - Static Members and Object Life Cycle
 * Demonstrates static state, static blocks, and object lifecycle management.
 */
public class FacilityManager {

    // 1. Static Variables (Class-level state shared across all instances)
    private static int globalTicketCounter;
    private static int totalCampusOccupancy;
    private static final String FACILITY_NAME;

    // 2. Static Initialisation Block
    static {
        globalTicketCounter = 1000; // Ticket IDs begin at 1001
        totalCampusOccupancy = 0;
        FACILITY_NAME = "SIMATS Smart Campus Parking Complex";
        System.out.println("[Static Init Block] " + FACILITY_NAME + " initialized.");
    }

    // 3. Static Methods
    public static synchronized String generateNextTicketId() {
        globalTicketCounter++;
        return "TKT-" + globalTicketCounter;
    }

    public static synchronized void recordVehicleEntry() {
        totalCampusOccupancy++;
    }

    public static synchronized void recordVehicleExit() {
        if (totalCampusOccupancy > 0) {
            totalCampusOccupancy--;
        }
    }

    public static int getTotalCampusOccupancy() {
        return totalCampusOccupancy;
    }

    public static int getCurrentTicketCounter() {
        return globalTicketCounter;
    }

    public static String getFacilityName() {
        return FACILITY_NAME;
    }
}
