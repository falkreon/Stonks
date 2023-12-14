package blue.endless.stonks.gui;

import java.awt.BorderLayout;
import java.awt.Container;

import javax.swing.JFrame;

import blue.endless.stonks.model.Portfolio;
import blue.endless.stonks.sim.AssetPerformance;

public class StonksPrimaryFrame extends JFrame {
	private static final long serialVersionUID = 1L;
	private final Portfolio portfolio;
	private PortfolioView portfolioView;
	
	public StonksPrimaryFrame(Portfolio portfolio) {
		this.portfolio = portfolio;
		
		this.setSize(1100, 600);
		this.setLayout(new BorderLayout());
		Container root = getContentPane();
		
		
		//Test stuff
		AssetPerformance perf = AssetPerformance.createRandom();
		
		
		//Set up the left-right split panel
		portfolioView = new PortfolioView(portfolio);
		root.add(portfolioView, BorderLayout.WEST);
		FunctionGraph rightPane = new FunctionGraph(perf::getValue);
		root.add(rightPane, BorderLayout.EAST);
		
	}
}
