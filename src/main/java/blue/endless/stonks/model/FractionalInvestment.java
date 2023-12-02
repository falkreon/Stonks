package blue.endless.stonks.model;

/**
 * Represents an investment in an Asset which can have fractional shares, like a {@link AssetType#MUTUAL_FUND MUTUAL_FUND}
 * or a stock slice. {@link AssetType#STOCK STOCK}s should use {@link NonFractionalInvestment}.
 */
public class FractionalInvestment extends Investment {
	private double sharesHeld;
	
	/**
	 * Creates a new Investment in a mutual fund or other fractional asset.
	 * @param asset the Asset held
	 * @param sharePrice the price-per-share in dollars that the Asset was purchased at
	 * @param sharesHeld the number of shares held by the new Investment.
	 */
	public FractionalInvestment(FractionalAsset asset, double sharePrice, double sharesHeld) {
		super(asset, sharePrice);
	}
	
	/**
	 * Creates a new Investment of a single share of a mutual fund or other fractional asset.
	 * @param asset the Asset held
	 * @param sharePrice the price-per-share in dollars that the Asset was purchased at
	 */
	public FractionalInvestment(FractionalAsset asset, double sharePrice) {
		this(asset, sharePrice, 1.0);
	}

	@Override
	public double getTotalPurchasePrice() {
		return sharesHeld * this.getPurchasePricePerShare();
	}

	@Override
	public double getTotalValueAtPrice(double pricePerShare) {
		return sharesHeld * pricePerShare;
	}
	
	/**
	 * Gets the number of shares this Investment holds.
	 * @return The number of shares in this Investment
	 */
	public double getSharesHeld() {
		return sharesHeld;
	}
	
	/**
	 * Sets a new number of shares owned. Do not do this if you purchase new shares at a different price! Use multiple
	 * Investments for purchases of Assets at different prices.
	 * @param value The new total number of shares held by the Investment at the original price.
	 */
	public void setSharesHeld(double value) {
		sharesHeld = value;
	}
}
