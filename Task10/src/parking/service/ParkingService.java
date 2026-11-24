package parking.service;

import parking.model.ParkingSlot;
import parking.model.ParkingTicket;
import parking.model.UserCategory;

import java.util.HashMap;
import java.util.Map;

/**
 * Service encapsulating facility operations, slot allocations, and validation
 */
public class ParkingService {
    // Encapsulated data structures
    private final ParkingSlot[][] slotGrid;
    private final Map<String, ParkingTicket> activeTickets;
    private int ticketSequence;

    public ParkingService(int[] slotsPerFloor) {
        if (slotsPerFloor == null || slotsPerFloor.length == 0) {
            throw new IllegalArgumentException("Facility must have at least 1 floor");
        }
        this.slotGrid = new ParkingSlot[slotsPerFloor.length][];
        for (int f = 0; f < slotsPerFloor.length; f++) {
            if (slotsPerFloor[f] <= 0) {
                throw new IllegalArgumentException("Floor " + f + " must have positive number of slots");
            }
            this.slotGrid[f] = new ParkingSlot[slotsPerFloor[f]];
            for (int s = 0; s < slotsPerFloor[f]; s++) {
                this.slotGrid[f][s] = new ParkingSlot(f, s);
            }
        }
        this.activeTickets = new HashMap<>();
        this.ticketSequence = 1000;
    }

    public synchronized ParkingTicket parkVehicle(String regNo, UserCategory category) {
        if (regNo == null || regNo.trim().isEmpty()) {
            throw new IllegalArgumentException("Registration plate cannot be empty");
        }
        String cleanReg = regNo.trim().toUpperCase();
        if (activeTickets.containsKey(cleanReg)) {
            throw new IllegalStateException("Duplicate entry: Vehicle " + cleanReg + " is already inside facility");
        }

        for (int f = 0; f < slotGrid.length; f++) {
            for (int s = 0; s < slotGrid[f].length; s++) {
                if (slotGrid[f][s].isFree()) {
                    slotGrid[f][s].assignVehicle(cleanReg);
                    ticketSequence++;
                    String ticketId = "TKT-" + ticketSequence;
                    ParkingTicket ticket = new ParkingTicket(ticketId, cleanReg, f, s, category);
                    activeTickets.put(cleanReg, ticket);
                    return ticket;
                }
            }
        }
        return null; // Facility full
    }

    public synchronized double processExit(String regNo, int durationMinutes, double hourlyRate) {
        if (durationMinutes < 0) {
            throw new IllegalArgumentException("Duration cannot be negative: " + durationMinutes);
        }
        String cleanReg = regNo.trim().toUpperCase();
        ParkingTicket ticket = activeTickets.remove(cleanReg);
        if (ticket == null) {
            throw new IllegalArgumentException("No active parking session found for vehicle " + cleanReg);
        }

        // Release slot safely
        slotGrid[ticket.getFloor()][ticket.getSlot()].release();

        // Students and Staff with passes park free
        if (ticket.getUserCategory() == UserCategory.STUDENT || ticket.getUserCategory() == UserCategory.STAFF) {
            return 0.0;
        }

        int hours = (durationMinutes + 59) / 60;
        return hours * hourlyRate;
    }

    public int getActiveCount() {
        return activeTickets.size();
    }
}
