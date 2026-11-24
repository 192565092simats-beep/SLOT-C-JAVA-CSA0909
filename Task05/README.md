# Task 5: Apply static Members and the Object Life Cycle

**Bloom's Level: K4 – Analyse | Marks: 3**

---

## 1. Static Members Justification

### Why Static Variables are Necessary
1. **Global Sequential Ticket Counter (`globalTicketCounter`)**:
   - Must guarantee strictly increasing, non-repeating ticket IDs across the entire campus.
   - **Why an instance variable would fail**: If declared as an instance variable inside each `ParkingFacility` or `Gate`, each gate instance would maintain its own isolated counter starting from 1. North Gate and South Gate would both issue `TKT-0001`, causing critical database key collisions and ticketing chaos.
2. **Campus-Wide Occupancy (`totalCampusOccupancy`)**:
   - Represents a consolidated state of campus capacity across all floors and gates. An instance variable would only record local gate entries.

### Appropriate Use of Static Blocks & Static Methods
- **Static Initialization Block (`static { ... }`)**:
  - Executes once when the class is loaded by the JVM ClassLoader before any instance is created or static method called. Used to load system configuration, base rate tables, and initialize global counters.
- **Static Factory / Utility Methods (`generateNextTicketId()`, `getTotalCampusOccupancy()`)**:
  - Do not depend on instance state (`this`). Can be invoked directly via class name (`FacilityManager.generateNextTicketId()`).

---

## 2. Object Lifecycle and Garbage Collection

### What Happens When a Vehicle Leaves?
1. The driver hands in the ticket. The system computes duration and marks `isPaid = true`.
2. The slot is released (`slots[floor][slot] = null`).
3. The facility drops its active ticket mapping reference.
4. **GC Eligibility**:
   - As long as a variable in an active stack frame holds a reference to the `ParkingTicket`, it remains reachable.
   - Once the local reference variable goes out of scope or is set to `null` (`t = null`), and no active collections retain a reference to it, the object becomes **unreachable from the GC Root set**.
   - It is marked during the next Garbage Collection cycle (e.g., ZGC/G1GC young generation evacuation) and its memory in the Eden/Survivor heap space is reclaimed.

---

## 3. Object Passing and Return Semantics

- In Java, all arguments are passed **strictly by value**.
- When an object reference is passed to a method (`modifyState(LocalTicket t)`):
  - A copy of the reference pointer (memory address) is pushed onto the method's stack frame.
  - Reassigning the parameter reference (`t = new Ticket(...)`) modifies only the local pointer in the current stack frame and has zero effect on the caller's variable.
  - Calling a mutator method (`t.closeTicket()`) dereferences the pointer to mutate the underlying object in the heap, reflecting across all callers holding references to that same object.

---

## 4. Alternative Concept & Complexity Analysis

### Multi-Facility Analysis: Static Counter vs. Instance Counter (North & South Gates)

Suppose the university operates $k$ facilities/gates and issues $n$ tickets over the day.

| Approach | Architecture & Collision Risk | Space Used by Counter | Multi-Facility Coordination |
| :--- | :--- | :--- | :--- |
| **Approach 1: Static Class Counter** | Single static integer in JVM Metaspace / Class block. | **$O(1)$ space**: Exactly 1 variable ($4\text{ bytes}$) regardless of $k$ or $n$. | **Guaranteed collision-free** across all gates within the JVM process. |
| **Approach 2: Instance Counter per Facility** | Each facility $F_1, F_2, \dots, F_k$ holds an `int localCounter`. | **$O(k)$ space**: $k$ integer variables across $k$ facility objects. | **Collisions occur** unless ticket IDs are namespaced (e.g., `"NORTH-0001"`, `"SOUTH-0001"`). |

**Recommended Solution for Multiple Gates**:
To support distributed or independent gates without central locking bottlenecks, use a **composite namespaced sequence**:
$$\text{TicketID} = \text{FacilityCode} + \text{"-"} + \text{LocalAtomicCounter}$$
For example, North Gate issues `NG-1001`, `NG-1002`, and South Gate issues `SG-1001`, `SG-1002`. This retains $O(1)$ lock-free issuance while ensuring absolute campus-wide uniqueness.

---

## 5. Compilation and Execution

```bash
# Compilation
javac Task05/src/FacilityManager.java Task05/src/StaticLifecycleDemo.java -d Task05/bin

# Execution
java -cp Task05/bin StaticLifecycleDemo
```

### Execution Output
```
[Static Init Block] SIMATS Smart Campus Parking Complex initialized.
==========================================================
 TASK 5: Apply static Members and the Object Life Cycle   
==========================================================
Facility: SIMATS Smart Campus Parking Complex
Initial Global Ticket Counter: 1000
Initial Total Occupancy: 0

Issued tickets:
  t1: Ticket(TKT-1001, TN01AB1111)
  t2: Ticket(TKT-1002, TN02CD2222)
Total Occupancy after 2 entries: 2

--- Pass-by-Value Demonstration ---
After attemptReassign(t1), t1 is still: TKT-1001 (Call-by-value preserved)

--- Object Lifecycle & GC Eligibility ---
  [Lifecycle] Ticket TKT-1001 closed for vehicle TN01AB1111
Occupancy after t1 exits: 1
t1 set to null. Heap object is now UNREACHABLE and eligible for GC.
System.gc() called. Memory reclaimed by JVM according to GC policy.
==========================================================
```
