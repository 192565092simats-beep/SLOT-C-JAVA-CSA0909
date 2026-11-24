package parking.model;

import java.util.Objects;

/**
 * Encapsulated immutable ticket with validated domain rules
 */
public class ParkingTicket {
    private final String ticketId;
    private final String regNo;
    private final int floor;
    private final int slot;
    private final long entryTime;
    private final UserCategory userCategory;

    public ParkingTicket(String ticketId, String regNo, int floor, int slot, UserCategory category) {
        this.ticketId = Objects.requireNonNull(ticketId, "ticketId cannot be null");
        this.regNo = Objects.requireNonNull(regNo, "regNo cannot be null");
        if (floor < 0 || slot < 0) {
            throw new IllegalArgumentException("Floor and slot indices must be non-negative");
        }
        this.floor = floor;
        this.slot = slot;
        this.entryTime = System.currentTimeMillis();
        this.userCategory = Objects.requireNonNull(category, "UserCategory cannot be null");
    }

    public String getTicketId() { return ticketId; }
    public String getRegNo() { return regNo; }
    public int getFloor() { return floor; }
    public int getSlot() { return slot; }
    public long getEntryTime() { return entryTime; }
    public UserCategory getUserCategory() { return userCategory; }

    @Override
    public String toString() {
        return String.format("Ticket[%s, Reg=%s, Pos=(%d, %d), User=%s]", ticketId, regNo, floor, slot, userCategory);
    }
}
