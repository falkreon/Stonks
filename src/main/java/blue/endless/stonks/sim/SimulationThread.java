package blue.endless.stonks.sim;

import blue.endless.stonks.gui.MarketView;
import blue.endless.stonks.gui.PortfolioView;
import blue.endless.stonks.model.AssetPriceIndex;

public class SimulationThread extends Thread {
	
	private volatile boolean keepRunning = true;
	private MarketView marketView = null;
	private PortfolioView portfolioView = null;
	
	public void stopRunning() {
		keepRunning = false;
	}
	
	public SimulationThread() {
		this.setDaemon(true); // Won't prevent the application from exiting on window close
		this.setName("DayAdvancer");
	}
	
	/**
	 * Attaches a MarketView to get daily stock index updates. Only one MarketView can be attached at a time
	 */
	public void attachMarketView(MarketView view) {
		this.marketView = view;
	}
	
	public void attachPortfolioView(PortfolioView view) {
		this.portfolioView = view;
	}
	
	@Override
	public void run() {
		while(keepRunning) {
			try {
				Thread.sleep(250);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			
			AssetRegistry.getMarket().nextDay();
			AssetPriceIndex marketIndex = AssetRegistry.getMarket().getClosingPrices();
			if (marketView != null) marketView.supplyPrices(marketIndex);
			if (portfolioView != null) portfolioView.supplyPrices(marketIndex);
			
		}
	}
}
