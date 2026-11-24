/**
 * Task 4 - Constructor Demonstrations
 */
public class ConstructorDemo {

    public static void main(String[] args) {
        System.out.println("==========================================================");
        System.out.println("  TASK 4: Design Classes, Objects and Constructors        ");
        System.out.println("==========================================================");

        // 1. Default constructor
        ParkingTicket defaultTicket = new ParkingTicket();
        System.out.println("1. Default Ticket: " + defaultTicket);

        // 2. Overloaded constructor with constructor chaining (this(...))
        ParkingTicket chainedTicket = new ParkingTicket("TKT-1001", "TN01AB1111", 0, 1);
        System.out.println("2. Chained Parameterised Ticket: " + chainedTicket);

        // 3. Copy Constructor (Re-issuing duplicate ticket)
        ParkingTicket duplicateTicket = new ParkingTicket(chainedTicket);
        System.out.println("3. Copy-Constructed (Duplicate) Ticket: " + duplicateTicket);

        // Verify deep copy identity
        System.out.println("   Are references identical? " + (chainedTicket == duplicateTicket) + " (Correct: independent heap objects)");
        System.out.println("   Do slots match? " + (chainedTicket.getSlot() == duplicateTicket.getSlot()));

        // 4. Static Factory Method
        ParkingTicket factoryTicket = ParkingTicket.issue(1002, "TN02CD2222", 1, 0);
        System.out.println("4. Factory Created Ticket: " + factoryTicket);

        // 5. Memory Model Explanation
        System.out.println("\nMemory Model Trace:");
        System.out.println("  Stack: Stores local reference variables (chainedTicket, duplicateTicket).");
        System.out.println("  Heap:  Stores the actual ParkingTicket instance payload and Strings.");
        System.out.println("==========================================================");
    }
}
