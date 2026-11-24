# Task 10: Enforce Encapsulation through Packages and Access Modifiers

**Bloom's Level: K4 – Analyse | Marks: 3**

---

## 1. Architectural Package Structure

```
parking/
├── model/           # Core domain entities with protected invariants
│   ├── ParkingSlot.java
│   ├── ParkingTicket.java
│   └── UserCategory.java
├── service/         # Business logic and transaction coordinator
│   └── ParkingService.java
└── app/             # Application entry point and client demonstration
    └── ParkingApp.java
```

---

## 2. Access Modifier Distribution & Justification

| Access Modifier | Applied Elements | Design Rationale |
| :--- | :--- | :--- |
| **`private`** | Fields: `slotGrid`, `occupiedVehicleRegNo`, `ticketId`, `regNo`, `entryTime` | Protects raw object state from unauthorized modification from outside the class boundary. Guarantees internal invariants cannot be corrupted. |
| **Package-Private (Default)** | Intermediate helper methods / constructors within `parking.model` | Facilitates trusted inter-class cooperation within the same package while hiding implementation details from outside callers. |
| **`protected`** | Subclass extension hooks (e.g., `ratePerHour()` in `Vehicle`) | Grants inheriting child classes the ability to specialize behavior while remaining inaccessible to unrelated client packages. |
| **`public`** | Service entry points (`parkVehicle()`, `processExit()`), model accessors | Forms the stable, public API boundary for external components. |

---

## 3. Data Validation in Encapsulated Methods

State mutators reject invalid domain inputs at the boundary:
1. **Negative Coordinates/Durations**:
   ```java
   if (durationMinutes < 0) throw new IllegalArgumentException("Duration cannot be negative");
   ```
2. **Double Booking Prevention**:
   ```java
   if (!isFree()) throw new IllegalStateException("Slot is already occupied");
   ```
3. **Duplicate Vehicle Detection**:
   `ParkingService` rejects parking transactions if an active ticket for the same plate is already recorded.

---

## 4. Wrapper Classes and Autoboxing Hazards

### 1. The Integer Cache Pitfall (`==` vs `.equals()`)
- Java caches `Integer` objects between `-128` and `127` via `IntegerCache`.
- When comparing two autoboxed `Integer` instances with value `100`, `a == b` returns `true` because both point to the exact same cached reference.
- When comparing two `Integer` instances with value `200`, `a == b` returns **`false`** because the JVM allocates two distinct heap objects!
- **Rule**: Never compare wrapper objects using `==`. Always use `a.equals(b)`.

### 2. Autoboxing `NullPointerException`
- Assigning a `null` `Integer` wrapper to a primitive `int` automatically invokes `intValue()`, triggering a fatal `NullPointerException`.

---

## 5. Alternative Concept & Complexity Analysis

### Private Fields with Validating Setters vs. Public Fields Validated by Caller

| Criteria | Private Fields + Validating Setters (Encapsulation) | Public Fields + Caller-Side Validation |
| :--- | :--- | :--- |
| **Single Point of Truth** | **Yes**: Validation rules exist in exactly one place (inside the setter/constructor). | **No**: Validation must be copy-pasted across every caller, API endpoint, and UI form. |
| **Code Duplication** | $O(1)$ validation code per field. | $O(M \times N)$ duplicated checks across $M$ callers and $N$ fields. |
| **Data Integrity Risk** | **Zero**: State cannot be bypassed or modified without running validation. | **Extremely High**: If even a single caller forgets the check, the database/memory contains corrupt state. |
| **Validation Time Cost** | $O(1)$ validation cost strictly on state mutation. | Identical $O(1)$ CPU cycles, but high cognitive load and testing complexity. |

---

## 6. Compilation and Execution

```bash
# Compilation (Compile package hierarchy)
javac Task10/src/parking/model/*.java Task10/src/parking/service/*.java Task10/src/parking/app/*.java -d Task10/bin

# Execution
java -cp Task10/bin parking.app.ParkingApp
```

### Execution Output
```
==========================================================
 TASK 10: Enforce Encapsulation through Packages & Modifiers
==========================================================

1. Allocating valid parking slots:
  Staff Ticket:   Ticket[TKT-1001, Reg=TN01A1111, Pos=(0, 0), User=STAFF]
  Visitor Ticket: Ticket[TKT-1002, Reg=TN02B2222, Pos=(0, 1), User=VISITOR]

2. Validation Defense: Duplicate Registration Plate:
  Caught expected validation error: Duplicate entry: Vehicle TN01A1111 is already inside facility

3. Validation Defense: Negative Checkout Duration:
  Caught expected validation error: Duration cannot be negative: -45

4. Checkout Settlement:
  Staff Fee (Monthly Pass Waiver):   Rs 0.00
  Visitor Fee (2 hrs @ Rs 30/hr):    Rs 60.00

--- Wrapper Classes & Autoboxing Comparison Risks ---
Cached Integers (100 == 100): true (Reference equality: TRUE due to IntegerCache)
Non-cached Integers (200 == 200 using '=='): false (Reference equality: FALSE! Risk of bug)
Non-cached Integers (200.equals(200)):        true (Value equality: TRUE, always use .equals())
Unboxing null Integer threw: NullPointerException (Autoboxing NPE hazard)
==========================================================
```
