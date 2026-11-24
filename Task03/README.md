# Task 3: Design the Control Logic

**Bloom's Level: K4 – Analyse | Marks: 4**

---

## 1. Control Logic Design Analysis

### Search Strategy and Early Termination
- The allocation method iterates sequentially using a nested loop: outer loop over floors (`f = 0` to `slots.length - 1`), inner loop over slots (`s = 0` to `slots[f].length - 1`).
- **Immediate Return**: The moment a free slot (`slots[f][s] == null`) is located, the vehicle registration number is assigned, and an array `{f, s}` is returned immediately (`return new int[]{f, s};`). This terminates both loops instantly without checking remaining slots.
- **Exhaustion Handling**: If the loops complete without finding an empty slot, the method returns `null`, indicating that the facility is full.

### Fee Slabs Using `if–else` vs `switch`
When calculating fees across tiered slabs (e.g., first hour flat, subsequent hours rate, daily maximum caps), `if–else if–else` chains evaluate continuous inequality ranges ($0 < t \le 60$, $60 < t \le 300$, $t > 300$), whereas `switch` statements evaluate discrete enum values or discrete integer hours.

### Role of `break`, `continue`, and `return`
- `return`: Eliminates unnecessary iterations by immediately exiting upon finding a match or detecting invalid arguments.
- `break`: Can terminate the inner slot loop to jump to the next floor when a floor condition is met.
- `continue`: Skips reserved or incompatible slots (e.g., skipping regular slots when an EV requires a charging slot).

---

## 2. Algorithmic Pseudocode (SIMATS Engineering Standard)

### Operation 1: Slot Allocation
```text
ALGORITHM AllocateSlot(slots[][], vehicleNo)
INPUT: 2D jagged array slots, String vehicleNo
OUTPUT: Array of integers [floor, slot] if allocated, NULL otherwise

BEGIN
    FOR floor <- 0 TO LENGTH(slots) - 1 DO
        FOR slot <- 0 TO LENGTH(slots[floor]) - 1 DO
            IF slots[floor][slot] == NULL THEN
                slots[floor][slot] <- vehicleNo
                RETURN [floor, slot]
            ENDIF
        ENDFOR
    ENDFOR
    RETURN NULL
END
```

### Operation 2: Fee Computation with Slabs
```text
ALGORITHM ComputeParkingFee(durationMinutes, ratePerHour, hasPass, isEV, kWhConsumed)
INPUT: durationMinutes (Integer), ratePerHour (Real), hasPass (Boolean), isEV (Boolean), kWhConsumed (Real)
OUTPUT: totalFee (Real)

BEGIN
    IF hasPass == TRUE THEN
        baseFee <- 0.0
    ELSE
        billableHours <- (durationMinutes + 59) / 60
        IF billableHours <= 0 THEN
            baseFee <- 0.0
        ELSE IF billableHours == 1 THEN
            baseFee <- ratePerHour
        ELSE IF billableHours <= 5 THEN
            baseFee <- ratePerHour + (billableHours - 1) * (ratePerHour * 0.9)
        ELSE
            baseFee <- MIN(500.0, ratePerHour * billableHours)
        ENDIF
    ENDIF

    chargingFee <- 0.0
    IF isEV == TRUE THEN
        chargingFee <- kWhConsumed * 12.0
    ENDIF

    totalFee <- baseFee + chargingFee
    RETURN totalFee
END
```

---

## 3. Alternative Concept & Complexity Analysis

### Linear Array Scan vs. Per-Floor Stack of Free Slots

Suppose the facility maintains:
1. `int freeCount[F]`: An array tracking the number of free slots on each floor.
2. `Stack<Integer> freeSlots[F]`: A stack for each floor initialized with all slot indices.

| Metric | Linear Scan (`allocate()` & `release()`) | Stack-Based Free List (`allocate()` & `release()`) |
| :--- | :--- | :--- |
| **`allocate()` Time Complexity** | **Worst Case**: $O(F \times S)$ when full or free slot is at the end.<br>**Best Case**: $O(1)$ at floor 0 slot 0. | **Worst Case**: $O(F)$ to find first floor where `freeCount[f] > 0`, followed by $O(1)$ `pop()`. If a global min-heap/queue is used: $O(1)$. |
| **`release()` Time Complexity** | $O(1)$ direct array index update `slots[floor][slot] = null`. | $O(1)$ stack push `freeSlots[floor].push(slot)` + increment `freeCount[floor]`. |
| **Extra Space Complexity** | $O(1)$ auxiliary space beyond the grid itself. | $O(F \times S)$ additional references and stack nodes to hold available slot numbers. |

**Trade-off Summary**: The linear scan uses zero extra memory and is highly efficient for small facilities ($F \le 5, S \le 100$, where max operations $\le 500$ CPU cycles). For massive parking lots with thousands of slots, the Stack/Heap-based design guarantees instant $O(1)$ allocation at the expense of $O(F \times S)$ auxiliary memory.

---

## 4. Compilation and Execution

```bash
# Compilation
javac Task03/src/ParkingFacility.java -d Task03/bin

# Execution
java -cp Task03/bin ParkingFacility
```

### Execution Output
```
[0, 0]
[0, 1]
[1, 0]
null
true false false
[0, 1]
```
