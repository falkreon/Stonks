package blue.endless.stonks.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

/**
 * An unordered collection of Investments with helpful functions for answering important questions about their value.
 */
public class Portfolio implements Iterable<Investment> {
	private List<Investment> investments = new ArrayList<>();
	
	/**
	 * Gets the total number of investments in this portfolio. Note that there may be multiple purchases of the same
	 * Asset at different prices; 
	 * @return
	 */
	public int size() {
		return investments.size();
	}
	
	/**
	 * Gets a List of all Assets owned in this Portfolio. Each held Asset is listed at most one time.
	 * @return
	 */
	public List<Asset> getHeldAssets() {
		return investments.stream()
				.map(Investment::getAsset)
				.distinct()
				.toList();
	}

	/**
	 * Gets all Investments in this Portfolio.
	 */
	@Override
	public Iterator<Investment> iterator() {
		return investments.iterator();
	}
	
	/**
	 * Streams all Investments in this Portfolio.
	 * @return a Stream of all Investments in this Portfolio
	 */
	public Stream<Investment> stream() {
		return investments.stream();
	}
	
	/**
	 * Gets the Investment at the specified index
	 * @param index the index of the Investment to get
	 * @return the Investment at the specified index
	 */
	public Investment get(int index) {
		return investments.get(index);
	}
	
	/**
	 * Adds an Investment to this Portfolio
	 * @param investment the Investment to add
	 */
	public void add(Investment investment) {
		investments.add(investment);
	}
	
	/**
	 * Gets the total amount of money spent in this portfolio on all the investments in the specified Asset.
	 * @param asset the Asset to query
	 * @return the total cost spent on all held shares of that Asset
	 */
	public double getTotalPurchasePrice(Asset asset) {
		return investments.stream()
				.filter(it -> it.getAsset().equals(asset))
				.mapToDouble(Investment::getTotalPurchasePrice)
				.sum();
	}
	
	/**
	 * Given a price, gets how much would be earned if all held shares in this Portfolio were sold at that price.
	 * @param asset the Asset to query
	 * @param price a price per share for that Asset
	 * @return the total value of this Portfolio's holdings in that Asset at at that price.
	 */
	public double getTotalValueAtPrice(Asset asset, double price) {
		return investments.stream()
				.filter(it -> it.getAsset().equals(asset))
				.mapToDouble(it -> it.getTotalValueAtPrice(price))
				.sum();
	}
	
	/**
	 * Gets the total amount of money spent on this Portfolio, not counting Assets that it no longer holds.
	 * @return the total cost of all investments in this Portfolio.
	 */
	public double getTotalPortfolioCost() {
		return investments.stream()
				.mapToDouble(Investment::getTotalPurchasePrice)
				.sum();
	}
	
	/**
	 * Gets the total value of all held Assets in this Portfolio, given a price index.
	 * @param index the price index to look up Asset prices in
	 * @return the total dollar amount of this Portfolio's value if sold at those prices
	 * @throws InvestmentNotFoundException if one or more Assets which are held cannot be found in the AssetPriceIndex
	 */
	public double getTotalPortfolioValue(AssetPriceIndex index) throws InvestmentNotFoundException {
		//We're doing this in a less functional way to make sure the error propagation is clear and reliable.
		double sum = 0d;
		
		for(Investment i : investments) {
			double curAssetValue = index.getInvestmentValue(i.getAsset());
			sum += i.getTotalValueAtPrice(curAssetValue);
		}
		
		return sum;
	}
}
