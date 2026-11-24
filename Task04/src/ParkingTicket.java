import java.util.Objects;

/**
 * Task 4 - Classes, Objects and Constructors
 * Represents a parking ticket issued to a vehicle.
 */
public class ParkingTicket {
    private final String ticketId;
    private final String vehicleRegNo;
    private final int floor;
    private final int slot;
    private final long entryTime;
    private boolean isPaid;

    // 1. Default constructor (used for placeholder/unassigned state)
    public ParkingTicket() {
        this("TKT-0000", "UNKNOWN", 0, 0, System.currentTimeMillis(), false);
    }

    // 2. Overloaded Constructor (Convenience: assumes current time and unpaid)
    public ParkingTicket(String ticketId, String vehicleRegNo, int floor, int slot) {
        this(ticketId, vehicleRegNo, floor, slot, System.currentTimeMillis(), false);
    }

    // 3. Master Parameterised Constructor (Validates and sets all fields)
    public ParkingTicket(String ticketId, String vehicleRegNo, int floor, int slot, long entryTime, boolean isPaid) {
        if (floor < 0 || slot < 0) {
            throw new IllegalArgumentException("Floor and slot must be non-negative");
        }
        this.ticketId = Objects.requireNonNull(ticketId, "ticketId cannot be null");
        this.vehicleRegNo = Objects.requireNonNull(vehicleRegNo, "vehicleRegNo cannot be null");
        this.floor = floor;
        this.slot = slot;
        this.entryTime = entryTime;
        this.isPaid = isPaid;
    }

    // 4. Copy Constructor (Creates duplicate snapshot or re-issues ticket)
    public ParkingTicket(ParkingTicket other) {
        Objects.requireNonNull(other, "Source ticket cannot be null");
        this.ticketId = other.ticketId + "-DUP";
        this.vehicleRegNo = other.vehicleRegNo;
        this.floor = other.floor;
        this.slot = other.slot;
        this.entryTime = other.entryTime;
        this.isPaid = other.isPaid;
    }

    // Static Factory Method alternative
    public static ParkingTicket issue(int sequence, String vehicleRegNo, int floor, int slot) {
        String id = String.format("TKT-%04d", sequence);
        return new ParkingTicket(id, vehicleRegNo, floor, slot);
    }

    public String getTicketId() { return ticketId; }
    public String getVehicleRegNo() { return vehicleRegNo; }
    public int getFloor() { return floor; }
    public int getSlot() { return slot; }
    public long getEntryTime() { return entryTime; }
    public boolean isPaid() { return isPaid; }
    public void markPaid() { this.isPaid = true; }

    @Override
    public String toString() {
        return String.format("Ticket[ID=%s, Reg=%s, Pos=(F%d, S%d), Time=%d, Paid=%b]",
                ticketId, vehicleRegNo, floor, slot, entryTime, isPaid);
    }
}
