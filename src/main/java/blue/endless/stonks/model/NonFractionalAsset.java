package blue.endless.stonks.model;

import java.util.Optional;

import blue.endless.jankson.JsonObject;
import blue.endless.jankson.annotation.Serializer;

/**
 * Tagging subclass for Assets which are not able to be owned fractionally.
 */
public class NonFractionalAsset extends Asset {

	public NonFractionalAsset(String symbol, AssetType category, Dividend dividend) {
		this(symbol, category, Optional.of(dividend));
	}
	
	public NonFractionalAsset(String symbol, AssetType category) {
		this(symbol, category, Optional.empty());
	}
	
	public NonFractionalAsset(String symbol, AssetType category, Optional<Dividend> dividend) {
		super(symbol, category, dividend);
		if (category.allowsFractionalOwnership()) throw new IllegalArgumentException("AssetType "+category+" allows fractional ownership. Use FractionalAsset instead.");
	}
	
	@Override
	@Serializer
	public JsonObject toJson() {
		return super.toJson();
	}
}
