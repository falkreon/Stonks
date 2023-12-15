package blue.endless.stonks.model;

import java.util.Locale;
import java.util.Objects;
import java.util.Optional;

import org.jetbrains.annotations.NotNull;

import blue.endless.jankson.JsonNull;
import blue.endless.jankson.JsonObject;
import blue.endless.jankson.JsonPrimitive;
import blue.endless.jankson.annotation.Deserializer;
import blue.endless.jankson.annotation.Serializer;

/**
 * Represents an investment asset. Assets by themselves have no price - the price floats on the market. They do have
 * complex rules for how they are held, traded, and how investors are awarded. Each asset is uniquely identified by a
 * stock exchange symbol.
 * 
 * <p>Note that a given Asset or symbol is only valid on the stock exchange it came from! The same Asset is not
 * offered on multiple exchanges, so if an Asset comes from NYSE, its prices can only be found from there.
 */
public abstract class Asset {
	private final String symbol;
	private final AssetType category;
	private final Optional<Dividend> dividend;
	
	/**
	 * Creates a new Asset which awards a dividend each quarter.
	 * @param symbol the exchange symbol for this stock or mutual fund, usually four captial letters
	 * @param category whether this is a stock or a mutual fund
	 * @param dividend the dividend this stock awards
	 * @see #Asset(String, AssetType)
	 */
	public Asset(String symbol, AssetType category, Dividend dividend) {
		this(symbol, category, Optional.of(dividend));
	}
	
	/**
	 * Creates a new Asset which does not award a dividend.
	 * @param symbol the exchange symbol for this stock or mutual fund, usually four capital letters.
	 * @param category whether this is a stock or a mutual fund
	 * @see #Asset(String, AssetType, Dividend)
	 */
	public Asset(String symbol, AssetType category) {
		this(symbol, category, Optional.empty());
	}
	
	public Asset(String symbol, AssetType category, Optional<Dividend> dividend) {
		Objects.requireNonNull(symbol);
		Objects.requireNonNull(category);
		Objects.requireNonNull(dividend);
		
		this.symbol = symbol;
		this.category = category;
		this.dividend = dividend;
	}
	
	/**
	 * Returns the symbol this investment Asset is identified by on the exchange it is traded on
	 * @return The stock or mutual fund symbol
	 */
	public String getSymbol() {
		return symbol;
	}
	
	/**
	 * Gets the type of investment instrument that this Asset represents.
	 * @return {@link AssetType#STOCK STOCK} if this is a stock, or {@link AssetType#MUTUAL_FUND MUTUAL_FUND}
	 *         if this is a mutual fund.
	 */
	public AssetType getCategory() {
		return category;
	}
	
	public boolean hasDividend() {
		return dividend.isPresent();
	}
	
	/**
	 * Gets the quarterly award paid out by this Asset
	 */
	public Optional<Dividend> getDividend() {
		return dividend;
	}
	
	@Override
	public boolean equals(Object other) {
		//Two Assets are considered equal if they have the same stock exchange symbol.
		return other != null &&
			other instanceof Asset otherAsset &&
			otherAsset.symbol.equals(this.symbol);
	}
	
	@Serializer
	public JsonObject toJson() {
		JsonObject result = new JsonObject();
		result.put("symbol", JsonPrimitive.of(symbol));
		result.put("asset-type", JsonPrimitive.of(category.name().toLowerCase(Locale.ROOT)));
		if (dividend.isPresent()) {
			result.put("dividend", dividend.get().toJson());
		} else {
			result.put("dividend", JsonNull.INSTANCE);
		}
		
		return result;
	}
	
	@Deserializer
	public static Asset fromJson(JsonObject obj) {
		//I'm nervous about the Optional here, so let's unpack manually
		
		String symbol = obj.get(String.class, "symbol");
		AssetType type = AssetType.valueOf(obj.get(String.class, "asset-type").toUpperCase());
		
		Objects.requireNonNull(symbol);
		Objects.requireNonNull(type);
		
		Optional<Dividend> dividend = Optional.empty();
		if (obj.containsKey("dividend") && (obj.get("dividend") instanceof JsonObject div)) {
			dividend = Optional.of(Dividend.fromJson(div));
		}
		
		return (type.allowsFractionalOwnership()) ?
			new FractionalAsset(symbol, type, dividend) :
			new NonFractionalAsset(symbol, type, dividend);
	}
	
	public static Asset create(String symbol, AssetType type) {
		if (type.allowsFractionalOwnership()) {
			return new FractionalAsset(symbol, type);
		} else {
			return new NonFractionalAsset(symbol, type);
		}
	}
}
