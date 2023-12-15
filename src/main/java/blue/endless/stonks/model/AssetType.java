package blue.endless.stonks.model;

public enum AssetType {
	
	/**
	 * A regular stock, which can only be invested in in whole-share increments.
	 */
	STOCK("Stock", false),
	
	/**
	 * A mutual fund, where an investor can obtain fractional shares.
	 */
	MUTUAL_FUND("Mutual Fund", true);
	
	
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
