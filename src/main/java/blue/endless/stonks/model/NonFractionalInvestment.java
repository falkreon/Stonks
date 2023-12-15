package blue.endless.stonks.model;

import blue.endless.jankson.JsonObject;
import blue.endless.jankson.JsonPrimitive;
import blue.endless.jankson.annotation.Serializer;
import blue.endless.jankson.api.DeserializationException;

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
	public NonFractionalInvestment(NonFractionalAsset asset, double purchasePrice) {
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
	
	@Serializer
	@Override
	public JsonObject toJson() {
		JsonObject result = super.toJson();
		result.put("shares", JsonPrimitive.of((long) sharesHeld));
		return result;
	}
	
	@Override
	public String toSaveText() {
		return super.toSaveText() + "\t" + sharesHeld;
	}
	
	public static NonFractionalInvestment fromJson(JsonObject obj, NonFractionalAsset asset, double price) {
		int shares = obj.getInt("shares", 1); //This key can be optional
		return new NonFractionalInvestment(asset, price, shares);
	}
	
	public static NonFractionalInvestment fromSaveText(String s, NonFractionalAsset asset, double price) {
		String[] parts = s.split("\t");
		if (parts.length < 4) throw new IllegalArgumentException("Missing share count");
		int shares = Integer.parseInt(parts[3]);
		
		return new NonFractionalInvestment(asset, price, shares);
	}
}
