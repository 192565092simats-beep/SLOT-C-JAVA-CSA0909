/**
 * Contract for fee-paying entities and tickets
 */
public interface Payable {
    double getPayableAmount();

    // Default method providing reusable receipt printing
    default void printReceipt() {
        System.out.printf("  [RECEIPT] Total Payable: %s%n", Displayable.formatCurrency(getPayableAmount()));
    }
}
