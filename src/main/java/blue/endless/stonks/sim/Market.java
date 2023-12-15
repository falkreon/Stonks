package blue.endless.stonks.sim;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import blue.endless.stonks.model.Asset;
import blue.endless.stonks.model.AssetPriceIndex;
import blue.endless.stonks.model.AssetType;
import blue.endless.stonks.model.Dividend;
import blue.endless.stonks.model.DividendType;
import blue.endless.stonks.model.FractionalAsset;
import blue.endless.stonks.model.MapAssetPriceIndex;
import blue.endless.stonks.model.NonFractionalAsset;

public class Market {
	double year = 0d;
	Map<String, AssetPerformance> simulations = new HashMap<>();
	List<Asset> assets = new ArrayList<>();
	
	public static Market create() {
		Market result = new Market();
		result.assets.clear();
		
		//Stock with dividend
		Dividend msftDividend = new Dividend(0.75, DividendType.CASH); //as of this writing it was 75 cents quarterly
		Asset msft = new NonFractionalAsset("MSFT", AssetType.STOCK, msftDividend);
		result.assets.add(msft);
		result.simulations.put("MSFT", AssetPerformance.createRandom());
		
		//Stock with no dividend
		Asset appl = new NonFractionalAsset("APPL", AssetType.STOCK);
		result.assets.add(appl);
		result.simulations.put("APPL", AssetPerformance.createRandom());
		
		//Mutual fund with dividend
		Dividend vsmpxDividend = new Dividend(0.73, DividendType.CASH); //Again, correct as of this writing 
		Asset vsmpx = new FractionalAsset("VSMPX", AssetType.MUTUAL_FUND, vsmpxDividend);
		result.assets.add(vsmpx);
		result.simulations.put("VSMPX", AssetPerformance.createRandom());
		
		return result;
	}
	
	public double getYear() {
		return year;
	}
	
	public void setYear(double year) {
		this.year = year;
	}
	
	public void nextDay() {
		this.year += 1 / 365.25; //Leap year every 4 years is integrated here as a fractional day per year. Leap-seconds are not handled.
	}
	
	/**
	 * Gets an unmodifiable map with all simulated assets in it
	 */
	public Map<String, AssetPerformance> getSimulations() {
		return Map.copyOf(simulations);
	}
	
	public AssetPriceIndex getClosingPrices() {
		HashMap<String, Double> prices = new HashMap<>();
		for(Map.Entry<String, AssetPerformance> entry : simulations.entrySet()) {
			prices.put(entry.getKey(), entry.getValue().getValue(year));
		}
		
		return new MapAssetPriceIndex(prices);
	}
}