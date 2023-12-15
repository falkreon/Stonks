package blue.endless.stonks.model;

import java.util.Locale;

import blue.endless.jankson.JsonObject;
import blue.endless.jankson.JsonPrimitive;
import blue.endless.jankson.annotation.Deserializer;
import blue.endless.jankson.api.DeserializationException;
import blue.endless.jankson.api.Marshaller;
import blue.endless.stonks.sim.AssetRegistry;

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
	
	public JsonObject toJson() {
		JsonObject result = new JsonObject();
		result.put("asset-type", JsonPrimitive.of(getAsset().getCategory().name().toLowerCase(Locale.ROOT)));
		result.put("asset", JsonPrimitive.of(getAsset().getSymbol()));
		result.put("price", JsonPrimitive.of(getPurchasePricePerShare()));
		return result;
	}
	
	public String toSaveText() {
		return getAsset().getCategory() + "\t" + getAsset().getSymbol() + "\t" + purchasePricePerShare;
	}
	
	@Deserializer
	public static Investment fromJson(JsonObject obj, Marshaller m) throws DeserializationException {
		try {
			AssetType assetType = AssetType.valueOf(obj.get(String.class, "asset-type").toUpperCase());
			String symbol = obj.get(String.class, "asset");
			if (assetType==null || symbol==null) throw new DeserializationException("Missing required keys: asset-type, asset");
			
			Asset asset = AssetRegistry.getOrCreate(symbol, assetType);
			
			double price = obj.getDouble("price", 1.0);
			
			if (asset instanceof FractionalAsset fractional) {
				return FractionalInvestment.fromJson(obj, fractional, price);
			} else if (asset instanceof NonFractionalAsset nonFractional) {
				return NonFractionalInvestment.fromJson(obj, nonFractional, price);
			} else {
				throw new DeserializationException("Inconsistent state: Asset '" + symbol + "' is neither fractional nor non-fractional.");
			}
		
		} catch (Exception ex) {
			// Just in case I missed something, redirect it to DeserializationException and throw a dialog rather than crashing
			throw new DeserializationException(ex);
		}
	}
	
	public static Investment fromSaveText(String line) throws DeserializationException {
		String[] pieces = line.split("\t");
		if (pieces.length < 3) throw new DeserializationException("Not enough information on this line (required: asset-type, symbol, price)");
		
		try {
			AssetType assetType = AssetType.valueOf(pieces[0]);
			String symbol = pieces[1];
			Asset asset = AssetRegistry.getOrCreate(symbol, assetType);
			
			double price = Double.valueOf(pieces[2]);
			
			if (asset instanceof FractionalAsset fractional) {
				return FractionalInvestment.fromSaveText(line, fractional, price);
			} else if (asset instanceof NonFractionalAsset nonFractional) {
				return NonFractionalInvestment.fromSaveText(line, nonFractional, price);
			} else {
				throw new DeserializationException("Inconsistent state: Asset '" + symbol + "' is neither fractional nor non-fractional.");
			}
			
		} catch (Exception ex) {
			throw new DeserializationException(ex);
		}
	}
}
