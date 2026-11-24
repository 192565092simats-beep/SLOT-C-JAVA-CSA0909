/**
 * Task 5 - Object Life Cycle & Static Behavior Demonstration
 */
public class StaticLifecycleDemo {

    static class LocalTicket {
        final String ticketId;
        final String regNo;

        LocalTicket(String ticketId, String regNo) {
            this(ticketId, regNo, true);
        }

        LocalTicket(String ticketId, String regNo, boolean trackOccupancy) {
            this.ticketId = ticketId;
            this.regNo = regNo;
            if (trackOccupancy) {
                FacilityManager.recordVehicleEntry();
            }
        }

        void closeTicket() {
            FacilityManager.recordVehicleExit();
            System.out.println("  [Lifecycle] Ticket " + ticketId + " closed for vehicle " + regNo);
        }

        @Override
        public String toString() {
            return "Ticket(" + ticketId + ", " + regNo + ")";
        }
    }

    // Demonstrates Java pass-by-value: modifying reference inside method
    public static void attemptReassign(LocalTicket t) {
        // 't' is a copy of the reference. Reassigning 't' here has NO effect on the caller's variable!
        t = new LocalTicket("TKT-TEMP", "DUMMY", false);
    }

    public static void modifyState(LocalTicket t) {
        // Mutating the object pointed to by 't' DOES affect the shared heap object.
        t.closeTicket();
    }

    public static void main(String[] args) {
        System.out.println("==========================================================");
        System.out.println(" TASK 5: Apply static Members and the Object Life Cycle   ");
        System.out.println("==========================================================");

        System.out.println("Facility: " + FacilityManager.getFacilityName());
        System.out.println("Initial Global Ticket Counter: " + FacilityManager.getCurrentTicketCounter());
        System.out.println("Initial Total Occupancy: " + FacilityManager.getTotalCampusOccupancy());

        // 1. Issue tickets using static generator
        String id1 = FacilityManager.generateNextTicketId();
        LocalTicket t1 = new LocalTicket(id1, "TN01AB1111");

        String id2 = FacilityManager.generateNextTicketId();
        LocalTicket t2 = new LocalTicket(id2, "TN02CD2222");

        System.out.println("\nIssued tickets:");
        System.out.println("  t1: " + t1);
        System.out.println("  t2: " + t2);
        System.out.println("Total Occupancy after 2 entries: " + FacilityManager.getTotalCampusOccupancy());

        // 2. Object Passing Semantics (Pass-by-value of reference)
        System.out.println("\n--- Pass-by-Value Demonstration ---");
        attemptReassign(t1);
        System.out.println("After attemptReassign(t1), t1 is still: " + t1.ticketId + " (Call-by-value preserved)");

        // 3. Object Lifecycle and Garbage Collection Eligibility
        System.out.println("\n--- Object Lifecycle & GC Eligibility ---");
        modifyState(t1); // marks ticket closed, decrements occupancy
        System.out.println("Occupancy after t1 exits: " + FacilityManager.getTotalCampusOccupancy());

        // Dereference t1
        t1 = null;
        System.out.println("t1 set to null. Heap object is now UNREACHABLE and eligible for GC.");

        System.gc(); // Request JVM Garbage Collector
        System.out.println("System.gc() called. Memory reclaimed by JVM according to GC policy.");
        System.out.println("==========================================================");
    }
}
