# Task 7: Override and Specialise Behaviour

**Bloom's Level: K4 – Analyse | Marks: 4**

---

## 1. Method Overriding in the Vehicle Hierarchy

### Overridden Methods
1. **`ratePerHour()`**:
   - Declared `protected abstract` in `Vehicle`. Overridden in each concrete subclass (`TwoWheeler` returns Rs 10.0, `Car` returns Rs 30.0) to specialize pricing without repeating time rounding arithmetic.
2. **`equals(Object o)` & `hashCode()`**:
   - Overridden in `Vehicle` to establish domain identity based strictly on vehicle registration number (`regNo`), regardless of sub-type classification.
3. **`toString()`**:
   - Overridden to provide clear diagnostic representations including subclass simple name, registration plate, and applicable rate.

---

## 2. Rules of Overriding and `@Override` Annotation

### The Purpose of `@Override`
- Informs the Java compiler that the method is intended to override an inherited superclass method.
- **Fail-Fast Safety**: If a developer makes a typo in the method name (e.g. `ratePerhour()`) or changes parameter types, the compiler generates a compile-time error instead of silently declaring an overloaded method.

### Java Overriding Rules
1. **Method Signature**: Method name and formal parameter list must match the superclass method exactly.
2. **Return Type**: Must be identical or a **covariant subtype** (e.g., if superclass returns `Vehicle`, subclass may return `Car`).
3. **Access Privilege**: Subclass access modifier cannot be more restrictive than superclass (e.g., `protected` in superclass can become `protected` or `public`, but never `private` or package-private).
4. **Exception Handling**: Subclass method cannot declare broader checked exceptions than the superclass method.

---

## 3. Justified Usage of the `final` Keyword

- **`final` Field (`private final String regNo`)**:
  - A vehicle's license plate is an immutable real-world identity. Making it `final` guarantees that once set in the constructor, it can never be altered or corrupted during parking transactions.
- **`final` Method (`public final String getRegNo()`)**:
  - Ensures that subclasses cannot tamper with or override identity retrieval logic.
- **`final` Class**:
  - Used on leaf classes (e.g., `public final class TwoWheeler`) when further subclassing is neither designed nor permitted, preventing the fragile base-class problem.

---

## 4. `equals()` and `hashCode()` Contract Implementation

To ensure two `Vehicle` objects with the same registration number are treated as identical across all Java collections (`HashSet`, `HashMap`):
```java
@Override
public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Vehicle other)) return false;
    return Objects.equals(this.regNo, other.regNo);
}

@Override
public int hashCode() {
    return Objects.hash(regNo);
}
```
**Consistency Rule**: If `v1.equals(v2) == true`, then `v1.hashCode() == v2.hashCode()` must strictly hold. This ensures that parking slots and billing systems can recognize vehicle re-entry or duplicate entries instantly.

---

## 5. Alternative Concept & Complexity Analysis

### Method Overriding vs. Monolithic `switch` on Vehicle Type

| Criteria | Polymorphic Overriding | Monolithic `switch(type)` |
| :--- | :--- | :--- |
| **Time Complexity ($k$ types)** | **$O(1)$ constant time**: Single virtual method table (vtable) dispatch lookup. | **$O(1)$ to $O(k)$**: Compiles to `tableswitch` or sequential `lookupswitch` comparisons. |
| **Open/Closed Principle (OCP)** | **Fully Compliant**: Closed for modification, open for extension. Adding new vehicle type $k+1$ requires **0 modifications** to existing tested classes. | **Violates OCP**: Every new vehicle category requires modifying and re-testing the central `calculateFee` switch method across all callers. |
| **Number of Classes Modified on New Type** | **0 existing classes modified**; 1 new class added. | **Central class modified**; all test suites must be re-run for regressions. |
| **Encapsulation & Cohesion** | High: Pricing rules and EV charging calculations remain localized within their respective domain class. | Low: Pricing logic leaked into an external procedural switch statement. |

---

## 6. Compilation and Execution

```bash
# Compilation
javac Task07/src/Vehicle.java Task07/src/Car.java Task07/src/ElectricCar.java Task07/src/OverridingDemo.java -d Task07/bin

# Execution
java -cp Task07/bin OverridingDemo
```

### Execution Output
```
==========================================================
     TASK 7: Override and Specialise Behaviour            
==========================================================
1. Overridden toString():
   c1:  Car[TN02B2222, Rate=Rs 30.0/hr]
   ev1: ElectricCar[TN02B2222, Rate=Rs 30.0/hr]

2. Overridden equals() & hashCode() Consistency:
   c1.equals(c2): true (Same plate, different case -> Equal)
   c1.equals(ev1): true (Same plate across Vehicle hierarchy -> Equal)
   c1.equals(c3): false (Different plates -> Not equal)
   c1.hashCode() == c2.hashCode(): true
   Elements in Set after adding c1, c2, ev1 (all TN02B2222) and c3: 2 (Correct: Deduplicated based on equals/hashCode)

3. Method Overriding Execution:
   c1 fee for 130 min: Rs 90.0
   ev1 fee for 130 min + 10 kWh: Rs 210.0

4. Monolithic switch comparison:
   c1 fee via switch: Rs 90.0
==========================================================
```
