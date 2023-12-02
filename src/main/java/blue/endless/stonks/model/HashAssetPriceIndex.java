package blue.endless.stonks.model;

import java.time.LocalDate;
import java.util.Map;

/**
 * AssetPriceIndex backed by a HashMap. Consider replacing with a FastUtil map for better performance.
 */
public class HashAssetPriceIndex implements AssetPriceIndex {
	
	private final LocalDate closingDate;
	private final Map<String, Double> data;
	
	public HashAssetPriceIndex(LocalDate closingDate, Map<String, Double> data) {
		this.closingDate = closingDate;
		this.data = Map.copyOf(data);
	}
	
	@Override
	public LocalDate getClosingDate() {
		return closingDate;
	}

	@Override
	public double getInvestmentValue(String symbol) throws InvestmentNotFoundException {
		Double value = data.get(symbol);
		if (value == null) throw new InvestmentNotFoundException(symbol);
		return value;
	}

}
