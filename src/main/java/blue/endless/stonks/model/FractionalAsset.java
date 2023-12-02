package blue.endless.stonks.model;

import org.jetbrains.annotations.NotNull;

/**
 * Tagging subclass for an Asset that can be owned fractionally.
 */
public class FractionalAsset extends Asset {

	public FractionalAsset(String symbol, @NotNull AssetType category, @NotNull Dividend dividend) {
		super(symbol, category, dividend);
		if (!category.allowsFractionalOwnership()) throw new IllegalArgumentException("AssetType "+category+" disallows fractional ownership. Use NonFractionalAsset instead.");
	}
	
	public FractionalAsset(String symbol, @NotNull AssetType category) {
		super(symbol, category);
		if (!category.allowsFractionalOwnership()) throw new IllegalArgumentException("AssetType "+category+" disallows fractional ownership. Use NonFractionalAsset instead.");
	}
}
