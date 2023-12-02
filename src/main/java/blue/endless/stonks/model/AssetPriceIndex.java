package blue.endless.stonks.model;

import java.time.LocalDate;

/**
 * Represents a stock exchange's price index, the current prices of a stock as of some closing date.
 */
public interface AssetPriceIndex {
	/**
	 * Gets the closing date which the prices were measured at.
	 * @return The closing date for prices listed in this Index
	 */
	public LocalDate getClosingDate();
	
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
		if (asset.getCategory() == AssetType.CASH) return 1.0d;
		return getInvestmentValue(asset.getSymbol());
	}
}
