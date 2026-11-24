# Task 12: Day-4 Design Challenge – Re-evaluate and Adapt the Design

**Bloom's Level: K5 – Evaluate | Marks: 8**

---

## A. Impact Analysis (2 Marks)

### 1. Summary of Requirements Changes
1. **New Vehicle Categories**:
   - **E-Bikes**: Charged a fixed flat fee (e.g. Rs 15.0 flat) regardless of elapsed parking duration.
   - **Delivery Vans**: Time-of-day dependent pricing: charged at double the standard car rate (2 $\times$ Rs 30 = Rs 60.0/hr) during peak business hours (9:00 AM – 5:00 PM), and normal car rate (Rs 30.0/hr) during off-peak hours.
2. **Visitor One-Day Pass**:
   - Visitors can purchase a one-day pass (Rs 100 flat) that waives all hourly parking charges for that 24-hour cycle.
3. **Multi-Story Physical Expansion**:
   - Facility expanded from 3 to 5 floors.
   - Floors have non-uniform capacities (e.g., Floor 0: 30 slots, Floor 1: 25 slots, Floor 2: 20 slots, Floor 3: 15 slots, Floor 4: 10 slots).

### 2. Affected Classes, Methods, and Data Structures
- **Array Layout**: Rectangular 2D arrays (`String[5][30]`) would waste memory on upper floors. The **jagged array** (`String[][] floorSlots`) adapts dynamically to any array of floor capacities passed to its constructor.
- **Billing Signature**: The base `calculateFee(int minutes)` in `Vehicle` must now incorporate entry hour context (`int entryHour`) to evaluate delivery van peak window restrictions (9 AM – 5 PM).
- **Pass Verification**: Exit processing must inspect if an active `OneDayPass` is attached to the ticket before computing duration tariffs.

---

## B. Evaluation of the Original Design (2 Marks)

| Evaluation Question | Assessment of Original Architecture |
| :--- | :--- |
| **Can new vehicle types be added only by writing new subclasses?** | **YES**. By subclassing `Vehicle`, `EBike` and `DeliveryVan` are introduced without altering any existing vehicle logic, strictly adhering to the **Open/Closed Principle (OCP)**. |
| **Are pricing rules kept in one place or spread across many classes?** | Vehicle-specific rate algorithms are localized inside their respective domain subclasses (`EBike` encapsulates flat rate; `DeliveryVan` encapsulates peak window logic). The controller does not hardcode vehicle-specific rates. |
| **Can existing tested classes remain unchanged?** | **YES**. `TwoWheeler`, `Car`, and `ElectricCar` continue to operate with zero regression risk. Only the abstract method signature in `Vehicle` was overloaded/extended to accept `entryHour`. |

---

## C. Adaptation or Redesign Strategy (2 Marks)

### Design Decision: **MODIFY / EXTEND (Targeted Adaptation)**
A complete ground-up redesign is unnecessary because the original object-oriented design cleanly decoupled slot representation (jagged arrays), vehicle pricing (polymorphism), and pass validation.

### Revised Architecture Overview
1. **`DynamicParkingFacility`**:
   - Uses a jagged array `String[5][]` initialized from `int[] slotsPerFloor = {30, 25, 20, 15, 10}`.
   - Tracks active vehicle instances, entry timestamps, and optional visitor day passes.
2. **`EBike extends Vehicle`**:
   - Overrides `calculateFee(duration, entryHour)` returning `FLAT_FEE = 15.0`.
3. **`DeliveryVan extends Vehicle`**:
   - Inspects `entryHour`: If $9 \le \text{entryHour} < 17$, applies double rate ($2 \times 30 = \text{Rs } 60/\text{hr}$); otherwise applies normal rate ($\text{Rs } 30/\text{hr}$).
4. **`OneDayPass`**:
   - Encapsulates pass identity, validity period, and flat purchase cost (Rs 100.0).

---

## D. Evidence-Based Defence (2 Marks)

### 1. Minimal Blast Radius
- **Zero** existing concrete vehicle classes required modification.
- **Two new subclasses** (`EBike`, `DeliveryVan`) and **one pass class** (`OneDayPass`) were added.

### 2. Extensibility Proof for Future Categories
Adding another future vehicle type (e.g. `HeavyTruck` at Rs 80/hr with height restriction) requires simply writing:
```java
public class HeavyTruck extends Vehicle { ... }
```
with zero modifications to `DynamicParkingFacility`, `EBike`, or `Car`.

### 3. Empirical Test Results

| Test Scenario | Parameters | Expected Output | Actual Output | Verification |
| :--- | :--- | :--- | :--- | :--- |
| **E-Bike (Short Stay)** | 30 minutes, 8 AM | Rs 15.00 flat | Rs 15.00 | **PASS** |
| **E-Bike (Long Stay)** | 480 minutes (8 hrs) | Rs 15.00 flat | Rs 15.00 | **PASS** |
| **Delivery Van (Peak)** | 10 AM entry, 120 min | $2 \times \text{Rs } 60 = \text{Rs } 120.00$ | Rs 120.00 | **PASS** |
| **Delivery Van (Off-Peak)** | 7 PM entry, 120 min | $2 \times \text{Rs } 30 = \text{Rs } 60.00$ | Rs 60.00 | **PASS** |
| **Visitor One-Day Pass** | Delivery Van, 6 hrs, Peak | Hourly fee waived (Rs 0.00) | Rs 0.00 | **PASS** |

### 4. Trade-off Analysis
- **Polymorphism vs Centralized Pricing Rules**: Embedding peak logic inside `DeliveryVan` keeps the class autonomous. However, if business analysts frequently change campus peak hours, moving time windows into a dedicated `PricingStrategy` interface would allow runtime configuration without code compilation.

---

## E. Compilation and Execution

```bash
# Compilation
javac Task12/src/Vehicle.java Task12/src/EBike.java Task12/src/DeliveryVan.java Task12/src/OneDayPass.java Task12/src/DynamicParkingFacility.java Task12/src/DesignChallengeDemo.java -d Task12/bin

# Execution
java -cp Task12/bin DesignChallengeDemo
```

### Execution Output
```
==========================================================
  TASK 12: DAY-4 DESIGN CHALLENGE - RE-EVALUATE & ADAPT   
==========================================================
Facility Layout (5 Floors with Variable Slots):
  Floor 0: Capacity = 30 slots | Occupied =  0 | Free = 30
  Floor 1: Capacity = 25 slots | Occupied =  0 | Free = 25
  Floor 2: Capacity = 20 slots | Occupied =  0 | Free = 20
  Floor 3: Capacity = 15 slots | Occupied =  0 | Free = 15
  Floor 4: Capacity = 10 slots | Occupied =  0 | Free = 10

--- Testing New Vehicle Category 1: E-Bike (Flat Fee Rs 15) ---
  EBike 30 min stay:   Rs 15.00 (Expected: Rs 15.00 flat)
  EBike 480 min stay:  Rs 15.00 (Expected: Rs 15.00 flat)

--- Testing New Vehicle Category 2: Delivery Van (Peak Hours Pricing) ---
  Van Peak (10 AM entry, 2 hrs): Rs 120.00 (Expected: 2 * Rs 60 = Rs 120.00)
  Van Off-Peak (7 PM entry, 2 hrs): Rs 60.00 (Expected: 2 * Rs 30 = Rs 60.00)

--- Testing Visitor One-Day Pass Feature ---
  Issued: OneDayPass[PASS-2026-001 | Holder: John Doe (Visitor) | Cost: Rs 100.0]
  [PASS APPLIED] One-Day Pass active (PASS-2026-001). Hourly fee waived!
  Exit fee with One-Day Pass: Rs 0.00 (Hourly charge fully waived)

Final Facility Status:
Facility Layout (5 Floors with Variable Slots):
  Floor 0: Capacity = 30 slots | Occupied =  0 | Free = 30
  Floor 1: Capacity = 25 slots | Occupied =  0 | Free = 25
  Floor 2: Capacity = 20 slots | Occupied =  0 | Free = 20
  Floor 3: Capacity = 15 slots | Occupied =  0 | Free = 15
  Floor 4: Capacity = 10 slots | Occupied =  0 | Free = 10
==========================================================
```
