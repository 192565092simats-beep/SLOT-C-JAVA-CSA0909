/**
 * Task 6 - Inheritance Demonstration & Constructor Execution Trace
 */
public class InheritanceDemo {

    // --- Alternative Concept: Composition Design ---
    static class VehicleType {
        final String typeName;
        final double hourlyRate;
        final boolean isElectric;

        VehicleType(String typeName, double hourlyRate, boolean isElectric) {
            this.typeName = typeName;
            this.hourlyRate = hourlyRate;
            this.isElectric = isElectric;
        }
    }

    static class ComposedVehicle {
        private final String regNo;
        private final VehicleType type; // HAS-A relationship

        ComposedVehicle(String regNo, VehicleType type) {
            this.regNo = regNo;
            this.type = type;
        }

        public double calculateFee(int minutes) {
            int hours = (minutes + 59) / 60;
            return hours * type.hourlyRate;
        }
    }

    public static void main(String[] args) {
        System.out.println("==========================================================");
        System.out.println("      TASK 6: Design the Inheritance Hierarchy            ");
        System.out.println("==========================================================");

        System.out.println("\n--- Constructor Execution Trace: new ElectricCar(\"TN03C3\", 50) ---");
        ElectricCar ev = new ElectricCar("TN03C3", 50);

        System.out.println("\nInstance Details:");
        System.out.println("  " + ev);
        System.out.println("  Parking fee for 130 min: Rs " + ev.calculateFee(130));
        System.out.println("  Charging fee for 10 kWh: Rs " + ev.calculateChargingFee(10));

        System.out.println("\n--- Alternative: Composition Comparison ---");
        VehicleType carType = new VehicleType("CAR", 30.0, false);
        ComposedVehicle composedCar = new ComposedVehicle("TN02B2", carType);
        System.out.println("  Composed vehicle created: " + composedCar.regNo + " (" + composedCar.type.typeName + ")");
        System.out.println("  Composed parking fee (130 min): Rs " + composedCar.calculateFee(130));
        System.out.println("==========================================================");
    }
}
