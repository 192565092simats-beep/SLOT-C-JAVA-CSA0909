public class AbstractionDemo {

    public static void main(String[] args) {
        System.out.println("==========================================================");
        System.out.println("         TASK 9: Introduce Abstraction                    ");
        System.out.println("==========================================================");

        // 1. Concrete objects using Abstract Class & Interface contracts
        Car normalCar = new Car("TN01AA1000");
        normalCar.setDurationMinutes(125); // 3 hours -> Rs 90.0

        ElectricCar ev = new ElectricCar("TN02BB2000");
        ev.setDurationMinutes(125); // 3 hours -> Rs 90.0
        ev.setConsumedKWh(15);      // 15 kWh * 12 = Rs 180.0 -> Total: Rs 270.0

        System.out.println("1. Interface Displayable & Default Methods:");
        normalCar.displaySummary();
        ev.displaySummary();

        System.out.println("\n2. Interface Payable & Polymorphic Processing:");
        Payable[] billingQueue = { normalCar, ev };
        for (Payable item : billingQueue) {
            item.printReceipt(); // invokes default method in Payable interface
        }

        System.out.println("\n3. Interface Default Method (Charging Time Estimate):");
        double estMin = ev.estimateChargingTimeMinutes(10, 40);
        System.out.printf("  Estimated time to charge from 10 kWh to 40 kWh: %.1f minutes%n", estMin);
        System.out.println("==========================================================");
    }
}
