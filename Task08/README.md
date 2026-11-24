# Task 8: Apply Polymorphism

**Bloom's Level: K4 – Analyse | Marks: 4**

---

## 1. Compile-Time vs. Runtime Polymorphism

### 1. Compile-Time Polymorphism (Method Overloading)
- Implemented in `Vehicle`:
  - `calculateFee(int minutes)`
  - `calculateFee(int inHour, int outHour)`
- **Compiler Resolution**: The Java compiler selects the appropriate overloaded method at compile time (**static binding**) based solely on the number and compile-time static types of the arguments passed.

### 2. Runtime Polymorphism (Dynamic Method Dispatch)
- Upcasting: Subclass objects (`TwoWheeler`, `Car`, `ElectricCar`) are assigned to superclass references in an array:
  ```java
  Vehicle[] parked = { new TwoWheeler("TN01A1"), new Car("TN02B2"), new ElectricCar("TN03C3") };
  ```
- When `v.calculateFee(130)` is executed in the loop:
  - The declared type of `v` is `Vehicle`.
  - The JVM resolves the call at runtime (**late / dynamic binding**) by inspecting the actual object type in heap memory and executing that specific class's `ratePerHour()` method.

---

## 2. Dynamic Method Dispatch Trace

### Traced Call: `v.calculateFee(130)` on `parked[0]`
1. **Declared Reference Type**: `Vehicle`
2. **Actual Object Type on Heap**: `TwoWheeler`
3. **Dispatch Mechanism**:
   - The JVM looks up the Virtual Method Table (vtable) pointer within the `TwoWheeler` object header.
   - Inside `calculateFee(int minutes)`, `ratePerHour()` is invoked.
   - The vtable points directly to `TwoWheeler.ratePerHour()`.
4. **Execution**:
   - `ratePerHour()` returns `10.0`.
   - Billable hours: `(130 + 59) / 60 = 3`.
   - Fee: $3 \times 10.0 = \text{Rs } 30.0$.

---

## 3. Explanation of Equality Result: `Car.equals(ElectricCar)`

The expression:
```java
new Car("TN02B2").equals(new ElectricCar("TN02B2"))
```
evaluates to **`true`**.

### Technical Reason
The `equals(Object o)` method implemented in `Vehicle` uses the `instanceof` operator:
```java
return o instanceof Vehicle && regNo.equals(((Vehicle) o).regNo);
```
Since `ElectricCar` extends `Car`, which extends `Vehicle`, `o instanceof Vehicle` evaluates to `true`. Because both objects have the registration number `"TN02B2"`, the string equality matches.

### Domain Justification: Is This Correct Behaviour?
**YES, this is the intended domain behavior.**
In a real-world parking facility, a physical parking bay is assigned to a vehicle by its unique legal identity—its license registration plate (`regNo`). Two entries presenting the same plate `"TN02B2"` represent the exact same vehicle occupying space, regardless of whether a camera or sensor categorizes it as a generic `Car` or specialized `ElectricCar`. Treating them as equal prevents duplicate ticketing or double-allocation of slots to the same vehicle.

*(Note: If strict identical class types were required instead of domain identity, the test would use `if (this.getClass() != o.getClass()) return false;`).*

---

## 4. Alternative Concept & Complexity Analysis

### Dynamic Method Dispatch vs. Procedural `instanceof` Cascades

```java
// Alternative Procedural Approach
for (Vehicle v : parked) {
    double fee;
    if (v instanceof TwoWheeler tw) {
        fee = tw.calculateFee(130);
    } else if (v instanceof ElectricCar ec) {
        fee = ec.calculateFee(130) + ec.chargingFee(10);
    } else if (v instanceof Car c) {
        fee = c.calculateFee(130);
    } else {
        throw new IllegalArgumentException();
    }
}
```

| Metric | Dynamic Method Dispatch | Cascading `instanceof` & Casts |
| :--- | :--- | :--- |
| **Time Complexity ($k$ types)** | **$O(1)$ constant time**: Fixed 1-cycle indirect vtable lookup regardless of hierarchy depth. | **$O(k)$ linear time**: Sequentially checks up to $k$ type conditions in worst case. |
| **Code Size & Redundancy** | **Minimal**: Call site is a single line: `v.calculateFee(min)`. | **Bloated**: Every billing point in the app needs the multi-branch `if-else` block. |
| **Maintenance Burden** | Adding type $k+1$ requires **0 edits** to caller loops. | Adding type $k+1$ requires locating and editing every `if-else` chain across the codebase. |

---

## 5. Compilation and Execution

```bash
# Compilation
javac Task08/src/FeeDemo.java -d Task08/bin

# Execution
java -cp Task08/bin FeeDemo
```

### Execution Output
```
TwoWheeler TN01A1 -> Rs 30.0
Car TN02B2 -> Rs 90.0
ElectricCar TN03C3 -> Rs 210.0
true
```
