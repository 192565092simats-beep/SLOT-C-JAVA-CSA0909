# Task 1: Understand the Problem and Identify the Objects

**Bloom's Level: K4 – Analyse | Marks: 4**

---

## 1. System Inputs and Outputs

### Inputs
- **Vehicle Arrival Data**: Vehicle Registration Number (`regNo`), Vehicle Type (Two-Wheeler, Car, Electric Vehicle), Charging requirement (for EVs: Boolean), User Category (Student, Staff, Visitor), Pass validity (Monthly pass ID/status), Timestamp of entry.
- **Vehicle Departure Data**: Parking Ticket Number or Vehicle Registration Number, Timestamp of exit, Energy consumed (kWh for EV charging if applicable).
- **Facility Configuration**: Number of floors, slots per floor, vehicle-to-slot compatibility matrix, base hourly rates, charging fee rate.

### Outputs
- **At Entry**: Parking Ticket containing unique ticket number, allocated floor and slot number, entry timestamp, vehicle details. If full: "PARKING_FULL" rejection notice.
- **At Exit**: Bill/Receipt detailing ticket number, duration of stay (billable hours), base parking fee, EV charging fee (if applicable), pass discount applied (100% waiver for valid student/staff pass), total amount payable, slot release confirmation.

---

## 2. Nouns and Verbs Mapping

| Nouns in Problem (Candidate Classes) | Verbs / Actions (Candidate Methods) |
| :--- | :--- |
| `Vehicle`, `TwoWheeler`, `Car`, `ElectricCar` | `getRegNo()`, `getType()`, `calculateFee()`, `requiresCharging()` |
| `ParkingSlot` | `occupy()`, `release()`, `isAvailable()`, `getSlotNumber()` |
| `ParkingFloor` | `findAvailableSlot()`, `getFreeSlotCount()` |
| `ParkingTicket` | `issue()`, `close()`, `calculateDuration()`, `getTicketId()` |
| `ParkingFacility` / `SmartParkingSystem` | `allocateSlot()`, `processExit()`, `isFull()` |
| `User` / `ParkingPass` | `isValidPass()`, `isExemptFromFee()` |
| `ChargingStation` | `startCharging()`, `computeChargingFee()` |

---

## 3. Candidate Classes and Their Responsibilities

1. **`Vehicle` (Abstract Base Class)**:
   - *Responsibility*: Encapsulates invariant vehicle attributes (`regNo`, `vehicleType`) and defines the abstract contract for hourly rate computation (`ratePerHour()`).
   - *Justification*: Prevents code duplication across vehicle sub-types and enables polymorphic dispatch for billing.
2. **`TwoWheeler`, `Car`, `ElectricCar` (Specialized Subclasses)**:
   - *Responsibility*: Provide concrete pricing parameters (e.g., Two-Wheeler: Rs 10/hr, Car: Rs 30/hr, EV: Rs 30/hr + Rs 12/kWh).
   - *Justification*: Captures unique domain behaviors and charging attributes without polluting generic vehicle abstractions.
3. **`ParkingSlot`**:
   - *Responsibility*: Models individual physical slots, tracks occupancy state, slot identifier, and supported vehicle category.
   - *Justification*: Ensures a slot cannot be double-booked and encapsulates slot state transitions.
4. **`ParkingTicket`**:
   - *Responsibility*: Represents the contractual transaction between campus and driver; records ticket ID, allocated slot, entry/exit timestamps, and calculated fee.
   - *Justification*: Enforces auditability and ensures each parked vehicle holds an immutable, unique receipt.
5. **`ParkingFacility`**:
   - *Responsibility*: Orchestrates floor-level and slot-level allocation, handles exit processing, releases slots, and tracks global occupancy.
   - *Justification*: Acts as the primary controller / facade for system operations.

---

## 4. Business Rules

1. **Mutual Slot Exclusion**: A parking slot can accommodate exactly one vehicle at any given time.
2. **Unique Non-Reusable Ticket ID**: Every ticket issued must have a globally unique identifier that is never re-issued or reused, even after exit.
3. **Vehicle-Slot Compatibility**: Vehicles must only be assigned to slots designated for their size (e.g., cars cannot occupy two-wheeler slots; only EVs can occupy EV charging bays).
4. **Duration Rounding**: Any fraction of an hour commenced is billed as a full hour (ceiling of duration).
5. **Fee Waiver Rule**: Registered students and staff possessing a valid monthly pass are exempt from base parking fees (fee = Rs 0.0); visitors pay the full tariff. EV charging energy consumption is billed separately regardless of pass status.

---

## 5. Stated Assumptions

1. The facility operates on a multi-floor layout with predefined slot counts per floor.
2. Vehicles do not change parking slots during their stay.
3. System clock timestamps are monotonic and accurate.
4. EV charging is available only at designated EV-compatible slots with metered kWh readings.

---

## 6. Alternative Concept & Complexity Analysis

### Comparison: Object-Oriented Fleet vs. Parallel Arrays

| Metric | Object-Oriented Model (`Vehicle[]` / `List<Vehicle>`) | Parallel Arrays (`regNo[]`, `type[]`, `entryTime[]`) |
| :--- | :--- | :--- |
| **Search Time Complexity** | $O(N)$ linear scan over references; $O(1)$ if backed by `Map` | $O(N)$ linear scan comparing strings in `regNo[]` |
| **Memory per Vehicle** | ~32 bytes object header + reference pointers + primitives | Primitive arrays save object header overhead (~16-24 bytes) |
| **Data Integrity / Cohesion** | **High**: Invariants, validations, and encapsulated state live together | **Very Poor**: Arrays must be manually kept in lock-step synchronization |
| **Extensibility (Adding Attribute)** | **Effort $O(1)$**: Add field to `Vehicle` class. No caller method signatures break. | **Effort $O(M)$**: Must allocate new array (`newAttr[]`), change all creation/deletion/resizing functions across the codebase. |

---

## 7. Compilation and Execution

```bash
# Compilation
javac Task01/src/ProblemAnalysisDemo.java -d Task01/bin

# Execution
java -cp Task01/bin ProblemAnalysisDemo
```

### Execution Output
```
==========================================================
 TASK 1: Understand the Problem and Identify the Objects 
==========================================================

[Design A - OOP Model] Parked vehicles:
  VehicleObject[regNo=TN01AB1111, type=CAR, entryTime=1000, owner=STAFF]
  VehicleObject[regNo=TN02CD2222, type=TWO_WHEELER, entryTime=1020, owner=STUDENT]
  VehicleObject[regNo=TN03EF3333, type=EV, entryTime=1045, owner=VISITOR]

Search for 'TN02CD2222' in OOP: VehicleObject[regNo=TN02CD2222, type=TWO_WHEELER, entryTime=1020, owner=STUDENT]

[Design B - Parallel Arrays] Search for 'TN02CD2222':
  Found at index 1: regNo=TN02CD2222, type=TWO_WHEELER, entryTime=1020, owner=STUDENT

Extensibility Comparison:
  - OOP: Simply added field 'ownerCategory' to VehicleObject class.
         All existing constructor/method references remain cohesive.
  - Parallel Arrays: Required creating a brand-new array 'ownerCategories[]'
         and updating every method signature, loop, resize, and indexing logic.
==========================================================
```
