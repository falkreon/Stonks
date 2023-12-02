package blue.endless.stonks.model;

/**
 * Represents a kind of loyalty reward paid to shareholders of some investment asset by the issuer or fund manager. This
 * reward can be paid in cash or stocks, and for the purposes of this assignment is awarded once each quarter as a
 * percentage of the shares held.
 */
public class Dividend {
	private final double percentage;
	private final boolean isPaidInCash;
	
	/**
	 * Instances of this class are created using static factory methods
	 * @param percentage The award value of this dividend, in shares per share invested.
	 * @param isCash True if this dividend pays its awards in cash; false if this dividend pays its awards in stock.
	 */
	private Dividend(double percentage, boolean isCash) {
		this.percentage = percentage;
		this.isPaidInCash = isCash;
	}
	
	/**
	 * Returns true if the dividend is awarded as cash. Awards of this type are taxed immediately, and reduce the
	 * issuer's cash supply.
	 * @return true if this dividend is paid in cash, otherwise false.
	 */
	public boolean isPaidInCash() {
		return isPaidInCash;
	}
	
	/**
	 * Returns true if the dividend is awarded as newly-minted shares of the asset currently held. Awards of this type
	 * are not taxed until the resulting shares are sold, and do not reduce the issuer's cash supply.
	 * @return true if this dividend is paid in shares of the asset that it originates from, otherwise false.
	 */
	public boolean isPaidInShares() {
		return !isPaidInCash;
	}
	
	/**
	 * Returns the award value of this dividend, expressed as the number of shares per share invested. For example,
	 * this method returns 0.03d for a 3% dividend.
	 * @return The percentage award of this Dividend, expressed as dividends per share invested.
	 */
	public double getPercentage() {
		return percentage;
	}
	
	/**
	 * Creates a new Dividend which awards cash.
	 * @param percentage The award value of the Dividend, in shares per share invested. For example, for a 3% dividend, use 0.03d
	 * @return The newly-created Dividend
	 */
	public static Dividend createCash(double percentage) {
		return new Dividend(percentage, true);
	}
	
	/**
	 * Creates a new Dividend which awards shares of the investment asset.
	 * @param percentage The award value of the Dividend, in shares per share invested. For example, for a 3% dividend, use 0.03d
	 * @return The newly-created Dividend
	 */
	public static Dividend createStock(double percentage) {
		return new Dividend(percentage, false);
	}
}
