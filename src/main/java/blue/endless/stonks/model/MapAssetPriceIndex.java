package blue.endless.stonks.model;

import java.util.Map;
import java.util.Set;

/**
 * AssetPriceIndex backed by a 'Map.copyOf' unmodifiable map. Consider replacing with a FastUtil map for better performance.
 */
public class MapAssetPriceIndex implements AssetPriceIndex {
	
	private final Map<String, Double> data;
	
	public MapAssetPriceIndex(Map<String, Double> data) {
		this.data = Map.copyOf(data);
	}
	
	@Override
	public double getInvestmentValue(String symbol) throws InvestmentNotFoundException {
		Double value = data.get(symbol);
		if (value == null) throw new InvestmentNotFoundException(symbol);
		return value;
	}
	
	@Override
	public Set<String> getIndexedAssets() {
		return Set.copyOf(data.keySet()); //Defensive copy to help rule out CoModEx - shouldn't matter for unmodifiable map but whatever
	}

}
