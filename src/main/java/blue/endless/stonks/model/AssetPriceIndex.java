package blue.endless.stonks.model;

import java.util.Set;

/**
 * Represents a stock exchange's price index, the current prices of a stock as of some closing date.
 */
public interface AssetPriceIndex {
	
	/**
	 * Gets the value recorded for the provided asset symbol.
	 * @param symbol the stock symbol to query
	 * @return the value of one share of the asset
	 * @throws InvestmentNotFoundException if the investment does not have a price in this index
	 */
	public double getInvestmentValue(String symbol) throws InvestmentNotFoundException;
	
	/**
	 * Gets the value recorded for the provided Asset
	 * @param asset the Asset to query
	 * @return the value of one share of the Asset
	 * @throws InvestmentNotFoundException
	 */
	public default double getInvestmentValue(Asset asset) throws InvestmentNotFoundException {
		return getInvestmentValue(asset.getSymbol());
	}
	
	/**
	 * Gets a Set of asset symbols listed in this Index
	 * @return An unmodifiable Set of asset symbols
	 */
	public Set<String> getIndexedAssets();
}
