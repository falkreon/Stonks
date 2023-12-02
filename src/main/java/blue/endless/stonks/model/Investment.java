package blue.endless.stonks.model;

/**
 * Represents a held amount of a stock exchange Asset.
 */
public abstract class Investment {
	private final Asset asset;
	private final double purchasePricePerShare;
	
	public Investment(Asset asset, double sharePrice) {
		this.asset = asset;
		this.purchasePricePerShare = sharePrice;
	}
	
	/**
	 * Gets the Asset owned, the subject of this investment.
	 * @return the Asset this investment is in
	 */
	public Asset getAsset() {
		return asset;
	}
	
	/**
	 * Gets the purchase price per share paid when the Asset was bought
	 * @return The purchase price of the Asset, per share, in dollars.
	 */
	public double getPurchasePricePerShare() {
		return purchasePricePerShare;
	}
	
	/**
	 * Gets the total value of this Asset when it was purchased, usually the number of shares times the share price.
	 * @return The total value of this investment, in dollars.
	 */
	public abstract double getTotalPurchasePrice();
	
	/**
	 * Gets the total value of this investment, given a current pricePerShare.
	 * @param pricePerShare the current value of the Asset invested, per share in dollars
	 * @return the total value of this investment at that price
	 */
	public abstract double getTotalValueAtPrice(double pricePerShare);
}
