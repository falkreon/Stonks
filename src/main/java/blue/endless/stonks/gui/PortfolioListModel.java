package blue.endless.stonks.gui;

import javax.swing.AbstractListModel;

import blue.endless.stonks.model.Investment;
import blue.endless.stonks.model.Portfolio;

public class PortfolioListModel extends AbstractListModel<Investment> {
	private static final long serialVersionUID = 1L;
	private final Portfolio portfolio;
	
	public PortfolioListModel(Portfolio delegate) {
		this.portfolio = delegate;
	}
	
	@Override
	public Investment getElementAt(int index) {
		return portfolio.get(index);
	}

	@Override
	public int getSize() {
		return portfolio.size();
	}

}
