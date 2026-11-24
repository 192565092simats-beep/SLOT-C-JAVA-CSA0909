/**
 * Interface with default and static utility methods
 */
public interface Displayable {
    void displaySummary();

    // Static helper method inside interface
    static String formatCurrency(double amount) {
        return String.format("Rs %.2f", amount);
    }
}
