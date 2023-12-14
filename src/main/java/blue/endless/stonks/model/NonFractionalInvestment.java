package blue.endless.stonks.model;

/**
 * Represents an investment in an Asset which can only be held in whole shares. The subject of this investment MUST be
 * a {@link AssetType#STOCK STOCK}; Mutual funds and "stock slices" require a {@link FractionalInvestment}.
 */
public class NonFractionalInvestment extends Investment {
	private int sharesHeld;
	
	/**
	 * Creates a new investment in a stock asset.
	 * @param asset the Asset held
	 * @param sharePrice the price-per-share in dollars that the Asset was purchased at
	 */
	public NonFractionalInvestment(Asset asset, double purchasePrice) {
		super(asset, purchasePrice);
		sharesHeld = 1;
	}
	
	/**
	 * Creates a new Investment in a stock asset.
	 * @param asset the Asset held
	 * @param sharePrice the price-per-share in dollars that the Asset was purchased at
	 * @param sharesHeld the number of shares held by the new Investment.
	 */
	public NonFractionalInvestment(Asset asset, double purchasePrice, int sharesHeld) {
		super(asset, purchasePrice);
		this.sharesHeld = sharesHeld;
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
	public int getSharesHeld() {
		return sharesHeld;
	}
	
	/**
	 * Sets a new number of shares owned. Do not do this if you purchase new shares at a different price! Use multiple
	 * Investments for purchases of Assets at different prices.
	 * @param value The new total number of shares held by the Investment at the original price.
	 */
	public void setSharesHeld(int count) {
		this.sharesHeld = count;
	}
	
	@Override
	public String toString() {
		return getAsset().getSymbol() + ": "+this.getSharesHeld() + " shares, bought at $"+this.getPurchasePricePerShare()+"/share";
	}
}
