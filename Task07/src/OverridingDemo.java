import java.util.HashSet;
import java.util.Set;

/**
 * Task 7 - Method Overriding & Object Contracts Demonstration
 */
public class OverridingDemo {

    // Monolithic switch alternative for complexity comparison
    public static double calculateFeeSwitch(String vehicleType, int minutes) {
        int hours = (minutes + 59) / 60;
        return switch (vehicleType.toUpperCase()) {
            case "TWOWHEELER" -> hours * 10.0;
            case "CAR" -> hours * 30.0;
            case "ELECTRICCAR" -> hours * 30.0; // EV charging separate
            default -> throw new IllegalArgumentException("Unknown vehicle type: " + vehicleType);
        };
    }

    public static void main(String[] args) {
        System.out.println("==========================================================");
        System.out.println("     TASK 7: Override and Specialise Behaviour            ");
        System.out.println("==========================================================");

        Car c1 = new Car("TN02B2222");
        Car c2 = new Car("tn02b2222"); // lowercase normalized in constructor
        ElectricCar ev1 = new ElectricCar("TN02B2222", 40);
        Car c3 = new Car("TN99ZZ9999");

        System.out.println("1. Overridden toString():");
        System.out.println("   c1:  " + c1);
        System.out.println("   ev1: " + ev1);

        System.out.println("\n2. Overridden equals() & hashCode() Consistency:");
        System.out.println("   c1.equals(c2): " + c1.equals(c2) + " (Same plate, different case -> Equal)");
        System.out.println("   c1.equals(ev1): " + c1.equals(ev1) + " (Same plate across Vehicle hierarchy -> Equal)");
        System.out.println("   c1.equals(c3): " + c1.equals(c3) + " (Different plates -> Not equal)");
        System.out.println("   c1.hashCode() == c2.hashCode(): " + (c1.hashCode() == c2.hashCode()));

        // HashSet deduplication test
        Set<Vehicle> vehicleSet = new HashSet<>();
        vehicleSet.add(c1);
        vehicleSet.add(c2);
        vehicleSet.add(ev1);
        vehicleSet.add(c3);
        System.out.println("   Elements in Set after adding c1, c2, ev1 (all TN02B2222) and c3: "
                + vehicleSet.size() + " (Correct: Deduplicated based on equals/hashCode)");

        System.out.println("\n3. Method Overriding Execution:");
        System.out.println("   c1 fee for 130 min: Rs " + c1.calculateFee(130));
        System.out.println("   ev1 fee for 130 min + 10 kWh: Rs " + ev1.calculateFee(130, 10));

        System.out.println("\n4. Monolithic switch comparison:");
        System.out.println("   c1 fee via switch: Rs " + calculateFeeSwitch("CAR", 130));
        System.out.println("==========================================================");
    }
}
