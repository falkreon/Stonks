package blue.endless.stonks.model;

import java.util.Optional;

import blue.endless.jankson.JsonObject;
import blue.endless.jankson.annotation.Serializer;

/**
 * Tagging subclass for an Asset that can be owned fractionally.
 */
public class FractionalAsset extends Asset {

	public FractionalAsset(String symbol, AssetType category, Dividend dividend) {
		this(symbol, category, Optional.of(dividend));
	}
	
	public FractionalAsset(String symbol, AssetType category) {
		this(symbol, category, Optional.empty());
	}
	
	public FractionalAsset(String symbol, AssetType category, Optional<Dividend> dividend) {
		super(symbol, category, dividend);
		if (!category.allowsFractionalOwnership()) throw new IllegalArgumentException("AssetType "+category+" disallows fractional ownership. Use NonFractionalAsset instead.");
	}
	
	@Override
	@Serializer
	public JsonObject toJson() {
		return super.toJson();
	}
}
