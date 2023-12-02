package blue.endless.stonks.model;

import org.jetbrains.annotations.NotNull;

/**
 * Tagging subclass for Assets which are not able to be owned fractionally.
 */
public class NonFractionalAsset extends Asset {

	public NonFractionalAsset(String symbol, @NotNull AssetType category, @NotNull Dividend dividend) {
		super(symbol, category, dividend);
		if (category.allowsFractionalOwnership()) throw new IllegalArgumentException("AssetType "+category+" allows fractional ownership. Use FractionalAsset instead.");
	}
	
	public NonFractionalAsset(String symbol, @NotNull AssetType category) {
		super(symbol, category);
		if (category.allowsFractionalOwnership()) throw new IllegalArgumentException("AssetType "+category+" allows fractional ownership. Use FractionalAsset instead.");
	}

}
