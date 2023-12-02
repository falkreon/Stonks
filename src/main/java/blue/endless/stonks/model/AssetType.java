package blue.endless.stonks.model;

public enum AssetType {
	/**
	 * Represents uninvested cash in a portfolio. One "share" of cash is always worth one dollar.
	 */
	CASH("Cash", true),
	
	/**
	 * A regular stock, which can only be invested in in whole-share increments.
	 */
	STOCK("Stock", false),
	
	/**
	 * A mutual fund, where an investor can obtain fractional shares.
	 */
	MUTUAL_FUND("Mutual Fund", true),
	
	/**
	 * Fractional ownership in a stock. This is a different financial instrument, and works more like a mutual fund in
	 * a single stock!
	 */
	STOCK_SLICE("Stock Slice", true);
	
	
	private final String name;
	private boolean allowsFractionalOwnership;
	
	AssetType(String name, boolean fractional) {
		this.name = name;
		this.allowsFractionalOwnership = fractional;
	}
	
	public String getName() {
		return name;
	}
	
	public boolean allowsFractionalOwnership() {
		return allowsFractionalOwnership;
	}
}
