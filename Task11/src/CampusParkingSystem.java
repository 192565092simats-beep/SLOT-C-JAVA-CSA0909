import java.util.*;

/**
 * Task 11 - Complete Integrated Campus Smart-Parking System
 */
public class CampusParkingSystem {

    public enum VehicleType { TWO_WHEELER, CAR, EV }
    public enum UserType { STUDENT, STAFF, VISITOR }

    public static class Ticket {
        private final String ticketId;
        private final String regNo;
        private final VehicleType vehicleType;
        private final UserType userType;
        private final int floor;
        private final int slot;
        private final long entryTime;

        public Ticket(String ticketId, String regNo, VehicleType vehicleType, UserType userType, int floor, int slot) {
            this.ticketId = ticketId;
            this.regNo = regNo;
            this.vehicleType = vehicleType;
            this.userType = userType;
            this.floor = floor;
            this.slot = slot;
            this.entryTime = System.currentTimeMillis();
        }

        public String getTicketId() { return ticketId; }
        public String getRegNo() { return regNo; }
        public VehicleType getVehicleType() { return vehicleType; }
        public UserType getUserType() { return userType; }
        public int getFloor() { return floor; }
        public int getSlot() { return slot; }
        public long getEntryTime() { return entryTime; }

        @Override
        public String toString() {
            return String.format("Ticket[%s | %s | %s | %s | Floor %d Slot %d]",
                    ticketId, regNo, vehicleType, userType, floor, slot);
        }
    }

    private final String[][] slots; // slots[floor][slot] -> vehicle regNo or null
    private final Map<String, Ticket> activeTickets;
    private int ticketSequence = 1000;
    private double totalDailyRevenue = 0.0;

    public CampusParkingSystem(int[] slotsPerFloor) {
        slots = new String[slotsPerFloor.length][];
        for (int f = 0; f < slotsPerFloor.length; f++) {
            slots[f] = new String[slotsPerFloor[f]];
        }
        activeTickets = new HashMap<>();
    }

    public synchronized Ticket parkVehicle(String regNo, VehicleType vehicleType, UserType userType) {
        if (regNo == null || regNo.trim().isEmpty()) {
            throw new IllegalArgumentException("Registration number cannot be empty");
        }
        String cleanReg = regNo.trim().toUpperCase();
        if (activeTickets.containsKey(cleanReg)) {
            throw new IllegalStateException("Duplicate entry: Vehicle " + cleanReg + " already parked");
        }

        for (int f = 0; f < slots.length; f++) {
            for (int s = 0; s < slots[f].length; s++) {
                if (slots[f][s] == null) {
                    slots[f][s] = cleanReg;
                    ticketSequence++;
                    String ticketId = "TKT-" + ticketSequence;
                    Ticket ticket = new Ticket(ticketId, cleanReg, vehicleType, userType, f, s);
                    activeTickets.put(cleanReg, ticket);
                    return ticket;
                }
            }
        }
        return null; // System full
    }

    public synchronized double processExit(String regNo, int durationMinutes, int evKWh) {
        if (durationMinutes < 0) {
            throw new IllegalArgumentException("Duration cannot be negative");
        }
        String cleanReg = regNo.trim().toUpperCase();
        Ticket ticket = activeTickets.remove(cleanReg);
        if (ticket == null) {
            throw new NoSuchElementException("Vehicle " + cleanReg + " not found in active parking");
        }

        // Release slot
        slots[ticket.getFloor()][ticket.getSlot()] = null;

        double fee = 0.0;
        // Staff and students park base free
        if (ticket.getUserType() != UserType.STUDENT && ticket.getUserType() != UserType.STAFF) {
            int hours = durationMinutes == 0 ? 0 : (durationMinutes + 59) / 60;
            double rate = switch (ticket.getVehicleType()) {
                case TWO_WHEELER -> 10.0;
                case CAR, EV -> 30.0;
            };
            fee += hours * rate;
        }

        // EV charging is billed regardless of pass status
        if (ticket.getVehicleType() == VehicleType.EV && evKWh > 0) {
            fee += evKWh * 12.0;
        }

        totalDailyRevenue += fee;
        return fee;
    }

    public double getTotalDailyRevenue() {
        return totalDailyRevenue;
    }

    public int getFreeSlotCount() {
        int count = 0;
        for (String[] floor : slots) {
            for (String s : floor) {
                if (s == null) count++;
            }
        }
        return count;
    }
}
