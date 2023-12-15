package blue.endless.stonks.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import blue.endless.stonks.model.AssetPriceIndex;
import blue.endless.stonks.model.Investment;
import blue.endless.stonks.model.Portfolio;

public class PortfolioView extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private Portfolio portfolio;
	
	private Box contentPane = new Box(BoxLayout.Y_AXIS);
	private JScrollPane scrollPane = new JScrollPane(contentPane);
	
	
	public PortfolioView(Portfolio portfolio) {
		this.portfolio = portfolio;
		
		this.setLayout(new BorderLayout());
		Dimension rigidArea = new Dimension(530, 200);
		this.setMinimumSize(rigidArea);
		this.setPreferredSize(rigidArea);
		this.setMaximumSize(new Dimension(rigidArea.width, Integer.MAX_VALUE));
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		this.add(scrollPane, BorderLayout.CENTER);
		
		
		refresh();
	}
	
	public void setPortfolio(Portfolio p) {
		this.portfolio = p;
		refresh();
	}
	
	public void refresh() {
		contentPane.removeAll();
		if (portfolio != null) {
			for(Investment inv : portfolio) {
				contentPane.add(new InvestmentView(inv));
			}
		}
		
		contentPane.validate();
		this.validate();
		this.repaint(20);
	}

	public void supplyPrices(AssetPriceIndex marketIndex) {
		for(Component comp : contentPane.getComponents()) {
			if (comp instanceof InvestmentView view) {
				view.updatePrice(marketIndex);
			}
		}
		this.validate();
		this.repaint(20);
	}
}
