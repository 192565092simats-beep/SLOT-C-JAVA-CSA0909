# Task 9: Introduce Abstraction

**Bloom's Level: K4 – Analyse | Marks: 4**

---

## 1. Abstract Classes vs. Interfaces: Design Rationale

### Why `Vehicle` is an Abstract Class
- **Shared State & Identity**: Every vehicle in the facility possesses an immutable registration number (`private final String regNo`) and constructor initialization. Interfaces cannot declare non-static, non-final instance fields or constructors.
- **Partial Template Implementation**: The duration ceiling formula in `calculateFee(int minutes)` is invariant across all vehicles, while `ratePerHour()` is deferred as `abstract`. An abstract class captures this template method pattern cleanly.

### Why Capabilities are Interfaces
- **`Chargeable`**: Represents a pluggable capability ("CAN-DO" behavior) specific to electric vehicles. Non-electric cars, bicycles, or visitors should not inherit EV charging methods.
- **`Payable`**: Unifies any entity that generates a billable total (`getPayableAmount()`), allowing parking tickets, vehicle fines, or merchandise to be processed through a unified checkout pipeline.
- **`Displayable`**: Standardizes display formatting with static currency helpers and default summaries.

---

## 2. Multiple Behaviors via Multiple Interfaces

Java strictly prohibits multiple inheritance of state/classes (`class A extends B, C` is invalid) to avoid the C++ diamond problem. However, Java allows **multiple inheritance of type through interfaces**:
```java
public class ElectricCar extends Car implements Chargeable, Payable, Displayable {
    // Inherits Vehicle and Car state
    // Fulfills Chargeable contract for battery charging
    // Fulfills Payable contract for fee checkout
    // Fulfills Displayable contract for UI rendering
}
```

---

## 3. Utility of `default` and `static` Interface Methods

1. **`default` Methods**:
   - Allow adding concrete behavior to an interface without breaking existing implementing classes.
   - Example in `Payable`:
     ```java
     default void printReceipt() {
         System.out.printf("  [RECEIPT] Total Payable: %s%n", Displayable.formatCurrency(getPayableAmount()));
     }
     ```
   - Example in `Chargeable`: `estimateChargingTimeMinutes()` provides a standard battery charging rate estimate.
2. **`static` Methods**:
   - Provide cohesive utility functions localized directly inside the interface namespace.
   - Example in `Displayable`: `Displayable.formatCurrency(double amount)`.

---

## 4. Alternative Concept & Complexity Analysis

### Vehicle as Interface vs. Chargeable as Abstract Class

Suppose the design inverted the abstractions:
- `interface Vehicle` with default methods
- `abstract class Chargeable`

| Limitation / Consequence | What Breaks / Cannot Be Expressed |
| :--- | :--- |
| **Instance State / Encapsulation** | An interface cannot declare instance variables (`private final String regNo`). Every subclass (`Car`, `TwoWheeler`) would be forced to duplicate `regNo`, constructor logic, and getters, completely destroying DRY (Don't Repeat Yourself). |
| **Constructors** | Interfaces have no constructors. Super constructor chaining (`super(regNo)`) is impossible. |
| **Multiple Class Inheritance Prohibition** | If `Chargeable` is an abstract class, then `ElectricCar` **cannot extend both `Car` and `Chargeable`** (`class ElectricCar extends Car, Chargeable` fails compilation). It would be structurally impossible to represent an EV that is both a Car and Chargeable. |
| **Method Dispatch Overhead** | Class virtual dispatch (`invokevirtual`) resolves in ~1-2 CPU cycles via fixed vtable offsets. Interface calls (`invokeinterface`) historically require searching an itable (though modern HotSpot JVM JIT with monomorphic/bimorphic inline caching optimizes this to comparable speed). |

---

## 5. Compilation and Execution

```bash
# Compilation
javac Task09/src/Chargeable.java Task09/src/Payable.java Task09/src/Displayable.java Task09/src/Vehicle.java Task09/src/Car.java Task09/src/ElectricCar.java Task09/src/AbstractionDemo.java -d Task09/bin

# Execution
java -cp Task09/bin AbstractionDemo
```

### Execution Output
```
==========================================================
         TASK 9: Introduce Abstraction                    
==========================================================
1. Interface Displayable & Default Methods:
  Car [TN01AA1000] - Base Rate: Rs 30.00/hr
  ElectricCar [TN02BB2000] - Base Rate: Rs 30.00/hr
    Includes EV Capability: Fast Charging @ Rs 12.00/kWh

2. Interface Payable & Polymorphic Processing:
  [RECEIPT] Total Payable: Rs 90.00
  [RECEIPT] Total Payable: Rs 270.00

3. Interface Default Method (Charging Time Estimate):
  Estimated time to charge from 10 kWh to 40 kWh: 81.8 minutes
==========================================================
```
