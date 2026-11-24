/**
 * One-Day Pass for Visitors
 * Cost: Rs 100 flat. Exempts visitor from hourly parking fees for the entire day.
 */
public class OneDayPass {
    private final String passId;
    private final String holderName;
    private final double cost;
    private final long issueDateEpoch;

    public OneDayPass(String passId, String holderName) {
        this.passId = passId;
        this.holderName = holderName;
        this.cost = 100.0; // Rs 100 purchase price
        this.issueDateEpoch = System.currentTimeMillis();
    }

    public String getPassId() { return passId; }
    public String getHolderName() { return holderName; }
    public double getCost() { return cost; }
    public boolean isValidForDay() { return true; }

    @Override
    public String toString() {
        return String.format("OneDayPass[%s | Holder: %s | Cost: Rs %.1f]", passId, holderName, cost);
    }
}
