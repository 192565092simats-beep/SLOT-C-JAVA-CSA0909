# Task 4: Design Classes, Objects and Constructors

**Bloom's Level: K4 – Analyse | Marks: 4**

---

## 1. Class Design: Fields, Methods & Constructor Strategy

### `ParkingTicket` Class Details
- **Fields**:
  - `private final String ticketId`: Unique identifier (e.g., `"TKT-1001"`).
  - `private final String vehicleRegNo`: Identifier of parked vehicle.
  - `private final int floor, slot`: Physical coordinates in facility.
  - `private final long entryTime`: Epoch timestamp in milliseconds.
  - `private boolean isPaid`: Mutability flag updated upon checkout payment.
- **Methods**:
  - `getTicketId()`, `getVehicleRegNo()`, `getFloor()`, `getSlot()`, `getEntryTime()`, `isPaid()`: Encapsulated accessors.
  - `markPaid()`: Mutator transitioning state to paid.
  - `toString()`: Formatted debugging representation.

---

## 2. Constructor Mechanics and Constructor Chaining

### Types of Constructors Implemented
1. **Default Constructor (`ParkingTicket()`)**:
   - Initializes sentinel values (`"TKT-0000"`, `"UNKNOWN"`, `-1`, `-1`). Useful for serialization placeholders or testing defaults.
2. **Overloaded Constructor (`ParkingTicket(ticketId, vehicleRegNo, floor, slot)`)**:
   - Convenience constructor that sets default current timestamp `System.currentTimeMillis()` and `isPaid = false`.
3. **Master Parameterised Constructor**:
   - Performs centralized validation (asserting `floor >= 0`, `slot >= 0`, and non-null arguments).
4. **Constructor Chaining (`this(...)`)**:
   - The default and overloaded constructors delegate directly to the master constructor using `this(...)`.
   - **Benefit**: Eliminates duplication of validation logic and ensures all initializations pass through a single, hardened gateway.

### Copy Constructor (`ParkingTicket(ParkingTicket other)`)
- **Need / Justification**: If a customer loses their ticket or requests a duplicate receipt, issuing a shallow copy could cause state aliasing. The copy constructor creates an independent duplicate object with an appended suffix (e.g., `"TKT-1001-DUP"`) while preserving the exact entry timestamp and slot coordinates for accurate exit billing.

---

## 3. JVM Memory Architecture: Stack vs. Heap

When executing:
```java
ParkingTicket t1 = new ParkingTicket("TKT-1001", "TN01AB1111", 0, 1);
```
1. **Stack Memory**:
   - The thread stack allocates an 8-byte reference variable `t1`.
   - Holds the 64-bit memory address pointing to the object location in the heap.
2. **Heap Memory**:
   - The `new` keyword allocates contiguous memory in the Java Garbage-Collected Heap.
   - The heap memory contains:
     - 12-byte Object Header (Mark Word + Klass pointer / compressed OOP).
     - Field values: references to `String` constants (`ticketId`, `vehicleRegNo`), primitive `int` fields (`floor`, `slot`), `long` (`entryTime`), and `boolean` (`isPaid`).
     - 4-byte padding for 8-byte memory alignment.

---

## 4. Alternative Concept & Complexity Analysis

### Comparison of Object Creation Paradigms

| Creation Approach | Guarantee of Valid Object (Invariant Enforcement) | Time Complexity ($n$ objects) | Space Complexity ($n$ objects) |
| :--- | :--- | :--- | :--- |
| **A. Constructor Chaining (`this(...)`)** | **Strongest**: Object cannot be instantiated in a partially initialized or invalid state. Fields can be declared `final` (immutable). | $O(n)$ constructor calls | $O(n)$ heap space |
| **B. Default Constructor + Setters (JavaBean)** | **Weak**: Object exists in an invalid / inconsistent state between `new` and successive setter calls. Fields cannot be `final`. Thread-safety risk. | $O(n)$ with $5\times$ method call overhead | $O(n)$ heap space |
| **C. Static Factory Method (`issue()`)** | **Strong**: Combines full invariant validation with expressive, domain-specific naming (e.g., `ParkingTicket.issue(...)`). Enables caching/pooling if needed. | $O(n)$ | $O(n)$ heap space |

---

## 5. Compilation and Execution

```bash
# Compilation
javac Task04/src/ParkingTicket.java Task04/src/ConstructorDemo.java -d Task04/bin

# Execution
java -cp Task04/bin ConstructorDemo
```

### Execution Output
```
==========================================================
  TASK 4: Design Classes, Objects and Constructors        
==========================================================
1. Default Ticket: Ticket[ID=TKT-0000, Reg=UNKNOWN, Pos=(F-1, S-1), Time=..., Paid=false]
2. Chained Parameterised Ticket: Ticket[ID=TKT-1001, Reg=TN01AB1111, Pos=(F0, S1), Time=..., Paid=false]
3. Copy-Constructed (Duplicate) Ticket: Ticket[ID=TKT-1001-DUP, Reg=TN01AB1111, Pos=(F0, S1), Time=..., Paid=false]
   Are references identical? false (Correct: independent heap objects)
   Do slots match? true
4. Factory Created Ticket: Ticket[ID=TKT-1002, Reg=TN02CD2222, Pos=(F1, S0), Time=..., Paid=false]

Memory Model Trace:
  Stack: Stores local reference variables (chainedTicket, duplicateTicket).
  Heap:  Stores the actual ParkingTicket instance payload and Strings.
==========================================================
```
