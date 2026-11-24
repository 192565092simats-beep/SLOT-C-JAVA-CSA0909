# Task 2: Choose the Data Representation

**Bloom's Level: K4 – Analyse | Marks: 3**

---

## 1. Field-Level Data Type Selection

| Field | Chosen Data Type | Justification |
| :--- | :--- | :--- |
| **Slot Number** | `int` | Discrete index representing zero-based or one-based positions within a floor. Ranges up to a few hundred slots per floor, easily fitting standard 32-bit signed integers with zero boxing overhead. |
| **Floor Number** | `int` | Non-negative small integer indicating levels (e.g., 0 to 4 for a 5-floor building). |
| **Vehicle Number** | `String` | Alphanumeric license plates (e.g., `"TN01AB1111"`). Requires string operations (trimming, uppercase normalization, regex validation). |
| **Entry Time / Exit Time** | `long` / `LocalDateTime` | Epoch timestamp in milliseconds/seconds (`long`) allows fast subtraction to determine duration. `LocalDateTime` offers readable ISO-8601 logging. |
| **Duration (Minutes)** | `int` / `long` | Elapsed parking duration in minutes. Stored as integer to avoid floating-point accumulation drift. |
| **Parking Fee** | `double` | Monetary value in Rupees (e.g., Rs 90.0). For financial transactions requiring exact sub-cent rounding, `BigDecimal` can be used; `double` is standard for currency in basic rate models provided proper rounding is enforced. |

---

## 2. Floor and Slot Array Representation

### Evaluation: 1D vs. Rectangular 2D vs. Jagged Array
1. **Jagged Array (`String[][] slots`) - Chosen Representation**:
   - Different floors in multi-story buildings often have different parking layouts (e.g., Ground floor with driveways has 20 slots, upper floors have 50 or 100 slots).
   - Java arrays are arrays of references. A jagged array `slots[f] = new String[slotsPerFloor[f]]` allocates exactly the memory required for each floor without allocating unused "ghost" slots.
2. **Fixed Rectangular 2D Array (`String[F][S]`)**:
   - Forces every floor to have identical slot counts. If Floor 0 has 20 slots and Floor 4 has 100 slots, 80 null pointers per floor are wasted on lower levels.
3. **Single 1D Array (`String[]`)**:
   - Requires artificial index calculation: `index = floor * stride + slot`. If floors have variable capacities, calculating offset requires an irregular prefix-sum array, introducing computational overhead and potential indexing bugs.

---

## 3. Type Conversion, Casting Risks & Integer Division Pitfall

### The Risk of Integer Division
When calculating parking fees, campus policy states:
> *"Every started hour is charged in full."*

If duration is stored in minutes (e.g., $65\text{ minutes}$), a naive division by $60$ using integer arithmetic results in truncation:
```java
int minutesParked = 65;
int billableHours = minutesParked / 60; // Truncates 1.0833 to 1!
double fee = billableHours * 30.0;     // Evaluates to Rs 30.0 instead of Rs 60.0
```
Similarly, applying a cast after integer division:
```java
double fee = (double)(minutesParked / 60) * 30.0; // Still produces Rs 30.0!
```

### The Solution
We avoid precision loss through **pure integer ceiling arithmetic**:
```java
int billableHours = (minutesParked + 59) / 60; // (65 + 59) / 60 = 124 / 60 = 2 hours
double fee = billableHours * ratePerHour;       // Evaluates correctly to Rs 60.0
```
Alternatively, using `Math.ceil()` with floating-point promotion:
```java
double fee = Math.ceil(minutesParked / 60.0) * ratePerHour;
```

---

## 4. Input Reading Strategy
- **Interactive Console Input**: `java.util.Scanner` bound to `System.in` is suitable for interactive command-line sessions.
- **Batch / Automated Testing**: Programmatic arguments (`String[] args`) or automated JUnit test harnesses provide deterministic, non-blocking execution suitable for grading and CI pipelines.

---

## 5. Alternative Concept & Complexity Analysis

### Jagged Array vs Flat 1D Array vs `ArrayList<String[]>`

| Representation | Time to Locate Slot `(floor, slot)` | Memory Required (Floors with sizes $S_0, S_1, \dots, S_{F-1}$) | Handling Variable Slot Counts |
| :--- | :--- | :--- | :--- |
| **Jagged Array `String[][]`** | $O(1)$ direct array index `slots[f][s]` | $\sum_{i=0}^{F-1} S_i \times \text{refSize} + F \times \text{arrayHeader}$ (Minimal) | **Native & optimal** |
| **Flat 1D Array `String[]`** | $O(1)$ if equal size; requires prefix sum table if variable | $F \times \max(S) \times \text{refSize}$ (Wastes space if uneven) | **Rigid / Wastes memory** |
| **`ArrayList<String[]>`** | $O(1)$ list retrieval + array index | Additional `ArrayList` backing array and object wrapper overhead | **Flexible, permits dynamic floors** |

---

## 6. Compilation and Execution

```bash
# Compilation
javac Task02/src/DataRepresentationDemo.java -d Task02/bin

# Execution
java -cp Task02/bin DataRepresentationDemo
```

### Execution Output
```
==========================================================
       TASK 2: Choose the Data Representation             
==========================================================
--- 1. Billing Precision Loss & Integer Division Pitfall ---
Duration: 65 minutes, Rate: Rs 30.0/hr
  [WRONG] Integer division (65 / 60 * 30.0):         Rs 30.00 (Undercharged by Rs 30.0)
  [WRONG] Cast after int division ((double)(65/60)): Rs 30.00
  [CORRECT] Integer ceiling ((65 + 59) / 60 * 30.0):  Rs 60.00
  [CORRECT] Math.ceil(65 / 60.0) * 30.0:             Rs 60.00

--- 2. Floor & Slot Array Representations ---
Model A (Jagged Array): Successfully allocates exact memory per floor.
  Floor 0 capacity: 2 slots -> [null, TN01AB1111]
  Floor 1 capacity: 3 slots -> [null, null, TN02CD2222]
  Floor 2 capacity: 1 slots -> [null]

Model B (Flat 1D with Max Stride):
  Total cells allocated: 9 (Actual slots needed: 6, Wasted: 3 cells)

Model C (ArrayList<String[]>): Dynamic floor expansion support.
  Number of floors: 3, Floor 1 slot 2: TN02CD2222
==========================================================
```
