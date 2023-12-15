package blue.endless.stonks.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.swing.JComponent;

import blue.endless.stonks.model.AssetPriceIndex;
import blue.endless.stonks.model.InvestmentNotFoundException;
import blue.endless.stonks.sim.Market;

public class MarketView extends JComponent {
	private static final long serialVersionUID = 1L;
	private static final int HISTORY_SIZE_LIMIT = 60;
	private static final double MAX_MARKET_VALUE = 500.0;
	private static final Color[] GRAPH_COLORS = {
		Color.ORANGE, Color.GREEN, Color.CYAN, Color.MAGENTA, Color.RED
	};
	private static int colorIndex = 0;

	private Market market;
	private ArrayDeque<AssetPriceIndex> priceHistory = new ArrayDeque<>();
	private AssetPriceIndex currentPrices;
	
	private Map<String, Color> indexColors = new HashMap<>();
	
	public MarketView(Market market) {
		this.market = market;
		priceHistory.addLast(market.getClosingPrices());
		
		
		Dimension d = new Dimension(600, 400);
		this.setMinimumSize(d);
		this.setPreferredSize(d);
		this.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
	}
	
	public void supplyPrices(AssetPriceIndex index) {
		if (currentPrices != null) {
			priceHistory.addLast(currentPrices);
			if (priceHistory.size() > HISTORY_SIZE_LIMIT) priceHistory.removeFirst();
		}
		
		currentPrices = index;
		repaint();
	}
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		g.setColor(Color.DARK_GRAY);
		g.fillRect(0, 0, this.getWidth(), this.getHeight());
		
		int barWidth = this.getWidth() / HISTORY_SIZE_LIMIT;
		
		int daysToShow = Math.min(priceHistory.size(), HISTORY_SIZE_LIMIT);
		int xOffset = (HISTORY_SIZE_LIMIT - daysToShow) - 2;
		int offsetIntoQueue = priceHistory.size() - daysToShow;
		
		//Stats
		Set<String> represented = new HashSet<>();
		double maxValue = 0;
		
		int i = 0;
		for(AssetPriceIndex idx : priceHistory) {
			if (i<offsetIntoQueue) continue;
			int x = i * barWidth + (xOffset * barWidth);
			for(String symbol : idx.getIndexedAssets()) {
				represented.add(symbol);
				Color c = indexColors.computeIfAbsent(symbol, (it) -> {
					Color result = GRAPH_COLORS[colorIndex % GRAPH_COLORS.length];
					colorIndex++;
					return result;
				});
				
				
				try {
					double value = idx.getInvestmentValue(symbol);
					maxValue = Math.max(value, maxValue);
					int y = (int) ((1 - (value / MAX_MARKET_VALUE) ) * this.getHeight());
					if (y<0) y=0;
					if (y>=this.getHeight()) y = this.getHeight()-1;
					g.setColor(c);
					g.fillRect(x, y, barWidth, 4);
				} catch (InvestmentNotFoundException ex) {
					//Skip drawing this asset but spam the console
					ex.printStackTrace();
				}
			}
			i++;
		}
		
		int rows = represented.size();
		int y = this.getHeight() - (rows * 20) - 4;
		for(String s : represented) {
			Color c = indexColors.getOrDefault(s, Color.WHITE);
			g.setColor(c);
			try {
				String priceString = String.format("$%8.2f", currentPrices.getInvestmentValue(s));
				g.drawString(s+": "+priceString, 8, y);
			} catch (Exception e) {
				g.drawString(s, 8, y);
			}
			y += 20;
		}
	}
}
