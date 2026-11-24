# Campus Smart-Parking System — Java OOP Assignment 1

**Course**: Java Fundamentals & Object-Oriented Programming (CO1 - CSA0909)  
**Student Name**: Thejas Balaji  
**Register Number**: 192565092  
**Institution**: SIMATS Engineering (Saveetha Institute of Medical and Technical Sciences)  
**Java Version**: OpenJDK 25.0.1 LTS (Java 17 / 21 / 25 compatible)  
**GitHub Repository Link**: [https://github.com/192565092simats-beep/SLOT-C-JAVA-CSA0909.git](https://github.com/192565092simats-beep/SLOT-C-JAVA-CSA0909.git)

---

## Executive Summary

This repository contains the complete design, implementation, testing, and evidence-based defense of an Object-Oriented Campus Smart-Parking System developed across 12 distinct tasks. The system handles two-wheelers, cars, electric vehicles (EVs), delivery vans, and e-bikes across multi-level variable-capacity floors, incorporating automated slot allocation, dynamic method dispatch for hourly and peak billing, monthly and one-day passes, and thread-safe encapsulation.

All tasks have been committed progressively to demonstrate iterative development as required by the assignment guidelines.

---

## Repository Structure & Task Overview

| Task Folder | Title & Focus | Key Concepts & Findings | Execution Result Summary |
| :--- | :--- | :--- | :--- |
| [**Task01**](Task01/) | Problem Analysis & Candidate Objects | Identified domain nouns/verbs, business rules, assumptions; compared OOP vs parallel arrays. | Demonstrated $O(1)$ extensibility of OOP vs $O(M)$ array maintenance. |
| [**Task02**](Task02/) | Data Representation & Types | Evaluated primitive types, integer division bugs, ceiling arithmetic, jagged arrays. | Highlighted and resolved the 65-min integer division undercharge bug. |
| [**Task03**](Task03/) | Control Logic & Pseudocode | Completed `ParkingFacility` allocation & release logic with SIMATS pseudocode. | Exact output match: `[0, 0]`, `[0, 1]`, `[1, 0]`, `null`, `true false false`, `[0, 1]`. |
| [**Task04**](Task04/) | Classes, Objects & Constructors | Constructor chaining via `this(...)`, copy constructors, JVM stack/heap memory models. | Independent duplicate ticket issuance and invariant protection verified. |
| [**Task05**](Task05/) | Static Members & Lifecycle | Static ticket counter, static init block, GC unreachability, Java pass-by-value semantics. | Verified unique global sequencing and multi-facility namespace scaling. |
| [**Task06**](Task06/) | Inheritance Hierarchy | Hierarchical & Multilevel inheritance (`Vehicle` $\rightarrow$ `Car` $\rightarrow$ `ElectricCar`), `super()`, UML diagram. | Constructor execution trace traced in exact order: Object $\rightarrow$ Vehicle $\rightarrow$ Car $\rightarrow$ ElectricCar. |
| [**Task07**](Task07/) | Method Overriding & Contracts | `@Override` rules, `final` modifiers, domain `equals()` and `hashCode()` contract. | Demonstrated deduplication in hash sets based on plate equality. |
| [**Task08**](Task08/) | Polymorphism & Dynamic Dispatch | Overloading vs dynamic dispatch; completed `FeeDemo` with integer arithmetic ceiling. | Exact output: `TwoWheeler TN01A1 -> Rs 30.0`, `Car -> Rs 90.0`, `EV -> Rs 210.0`, `true`. |
| [**Task09**](Task09/) | Abstraction & Interfaces | Abstract class `Vehicle`, capability interfaces `Chargeable`, `Payable`, default/static methods. | Validated multi-interface polymorphism and architectural constraints. |
| [**Task10**](Task10/) | Packages & Encapsulation | 3-tier package architecture (`model`, `service`, `app`), validating setters, autoboxing hazards. | Caught negative duration, duplicate plate, and demonstrated `Integer` caching bug. |
| [**Task11**](Task11/) | Implementation, Testing & Defense | End-to-end integration, boundary cases, hand-calculated revenue reconciliation, benchmark. | Reconciled Rs 310.0 daily total with 0% error; HashMap showed 500x speedup over arrays. |
| [**Task12**](Task12/) | Day-4 Design Challenge | E-Bikes (flat fee), Delivery Vans (9AM-5PM peak doubling), 5 variable floors, One-Day Pass. | All changed requirements verified; adapted architecture with zero existing class modifications. |

---

## Steps to Compile and Run Each Program

Ensure a modern JDK (Java 17 or higher) is installed and available on your system path.

### Task 1: Problem Analysis Demo
```bash
javac Task01/src/ProblemAnalysisDemo.java -d Task01/bin
java -cp Task01/bin ProblemAnalysisDemo
```

### Task 2: Data Representation Demo
```bash
javac Task02/src/DataRepresentationDemo.java -d Task02/bin
java -cp Task02/bin DataRepresentationDemo
```

### Task 3: Complete the Code — Slot Allocation & Control Logic
```bash
javac Task03/src/ParkingFacility.java -d Task03/bin
java -cp Task03/bin ParkingFacility
```

### Task 4: Classes, Objects & Constructors
```bash
javac Task04/src/ParkingTicket.java Task04/src/ConstructorDemo.java -d Task04/bin
java -cp Task04/bin ConstructorDemo
```

### Task 5: Static Members & Object Lifecycle
```bash
javac Task05/src/FacilityManager.java Task05/src/StaticLifecycleDemo.java -d Task05/bin
java -cp Task05/bin StaticLifecycleDemo
```

### Task 6: Inheritance Hierarchy
```bash
javac Task06/src/Vehicle.java Task06/src/TwoWheeler.java Task06/src/Car.java Task06/src/ElectricCar.java Task06/src/InheritanceDemo.java -d Task06/bin
java -cp Task06/bin InheritanceDemo
```

### Task 7: Method Overriding & Specialisation
```bash
javac Task07/src/Vehicle.java Task07/src/Car.java Task07/src/ElectricCar.java Task07/src/OverridingDemo.java -d Task07/bin
java -cp Task07/bin OverridingDemo
```

### Task 8: Complete the Code — FeeDemo & Dynamic Dispatch
```bash
javac Task08/src/FeeDemo.java -d Task08/bin
java -cp Task08/bin FeeDemo
```

### Task 9: Abstraction (Abstract Classes & Interfaces)
```bash
javac Task09/src/Chargeable.java Task09/src/Payable.java Task09/src/Displayable.java Task09/src/Vehicle.java Task09/src/Car.java Task09/src/ElectricCar.java Task09/src/AbstractionDemo.java -d Task09/bin
java -cp Task09/bin AbstractionDemo
```

### Task 10: Packages and Encapsulation
```bash
javac Task10/src/parking/model/*.java Task10/src/parking/service/*.java Task10/src/parking/app/*.java -d Task10/bin
java -cp Task10/bin parking.app.ParkingApp
```

### Task 11: Complete System Integration & Validation Suite
```bash
javac Task11/src/CampusParkingSystem.java Task11/src/TestHarness.java -d Task11/bin
java -cp Task11/bin TestHarness
```

### Task 12: Day-4 Design Challenge Adaptation
```bash
javac Task12/src/Vehicle.java Task12/src/EBike.java Task12/src/DeliveryVan.java Task12/src/OneDayPass.java Task12/src/DynamicParkingFacility.java Task12/src/DesignChallengeDemo.java -d Task12/bin
java -cp Task12/bin DesignChallengeDemo
```

---

## Progressive Commit History

As required, this project has been built and committed step-by-step:
1. `feat(Task01): implement problem analysis and object modeling demo with documentation`
2. `feat(Task02): implement data representation choices, casting pitfall demo and documentation`
3. `feat(Task03): implement ParkingFacility slot allocation, release and control logic with pseudocode`
4. `feat(Task04): implement classes, constructor chaining, copy constructor, memory model analysis`
5. `feat(Task05): implement static members, initialization blocks, and object lifecycle demo`
6. `feat(Task06): implement inheritance hierarchy, super chaining trace, and UML design`
7. `feat(Task07): implement method overriding, final modifiers, and equals/hashCode contract`
8. `feat(Task08): complete FeeDemo code, polymorphism, dynamic method dispatch, and equality analysis`
9. `feat(Task09): implement abstraction with abstract classes, interfaces, default and static methods`
10. `feat(Task10): implement package structure, access modifiers, input validation, and wrapper hazards`
11. `feat(Task11): implement full campus parking system, test harness, reconciliation, and benchmark`
12. `feat(Task12): implement Day-4 Design Challenge with e-bikes, delivery vans, one-day pass, and 5 floors`
13. `docs: add comprehensive master README with project overview and execution instructions`
