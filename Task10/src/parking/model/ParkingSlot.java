package parking.model;

/**
 * Encapsulated parking slot with validating state transitions.
 */
public class ParkingSlot {
    private final int floor;
    private final int slotNumber;
    private String occupiedVehicleRegNo; // package-private/private: managed safely

    public ParkingSlot(int floor, int slotNumber) {
        if (floor < 0) throw new IllegalArgumentException("Floor cannot be negative: " + floor);
        if (slotNumber < 0) throw new IllegalArgumentException("Slot number cannot be negative: " + slotNumber);
        this.floor = floor;
        this.slotNumber = slotNumber;
        this.occupiedVehicleRegNo = null;
    }

    public int getFloor() { return floor; }
    public int getSlotNumber() { return slotNumber; }
    public boolean isFree() { return occupiedVehicleRegNo == null; }
    public String getOccupiedVehicleRegNo() { return occupiedVehicleRegNo; }

    public void assignVehicle(String regNo) {
        if (!isFree()) {
            throw new IllegalStateException("Slot (" + floor + ", " + slotNumber + ") is already occupied by " + occupiedVehicleRegNo);
        }
        if (regNo == null || regNo.trim().isEmpty()) {
            throw new IllegalArgumentException("Vehicle registration number cannot be blank");
        }
        this.occupiedVehicleRegNo = regNo.trim().toUpperCase();
    }

    public void release() {
        if (isFree()) {
            throw new IllegalStateException("Slot (" + floor + ", " + slotNumber + ") is already free");
        }
        this.occupiedVehicleRegNo = null;
    }

    @Override
    public String toString() {
        return String.format("Slot[F%d:S%d, %s]", floor, slotNumber, isFree() ? "FREE" : "OCCUPIED(" + occupiedVehicleRegNo + ")");
    }
}
