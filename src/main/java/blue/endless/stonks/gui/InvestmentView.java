package blue.endless.stonks.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import blue.endless.stonks.model.Investment;

public class InvestmentView extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private static final Font MONEY_FONT = Font.decode(Font.MONOSPACED+"-BOLD-14");
	private static final int TEXT_Y = 17;
	
	private final JLabel symbolLabel = fixSize(new JLabel(), 50, 64);
	private final JLabel sharesLabel = fixSize(new JLabel(), 60, 64);
	private final JLabel sharesAtLabel = fixSize(new JLabel("shares @"), 80, 64);
	private final JLabel pricePerShareLabel = fixSize(new JLabel(), 70, 64);
	private final JLabel equalsLabel = fixSize(new JLabel("="), 32, 64);
	private final JLabel totalPriceLabel = fixSize(new JLabel(), 70, 64);
	private final IconActionButton sellButton = new IconActionButton(Assets.SELL_IMAGE, "Sell");
	private Investment investment;
	
	public InvestmentView(Investment investment) {
		this.setInvestment(investment);
		
		this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		this.setBorder(BorderFactory.createEmptyBorder(2, 4, 2, 4));
		
		this.setMinimumSize(new Dimension(500, 32));
		this.setMaximumSize(new Dimension(500, 32));
		this.setPreferredSize(new Dimension(500, 32));
		
		//this.setBackground(new Color(200, 200, 200));
		
		Font mono = Font.decode("Monospaced-PLAIN-12");
		
		this.add(symbolLabel);
		sharesLabel.setFont(mono);
		sharesLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		this.add(sharesLabel);
		sharesAtLabel.setHorizontalAlignment(SwingConstants.CENTER);
		this.add(sharesAtLabel);
		pricePerShareLabel.setFont(mono);
		pricePerShareLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		this.add(pricePerShareLabel);
		equalsLabel.setHorizontalAlignment(SwingConstants.CENTER);
		this.add(equalsLabel);
		totalPriceLabel.setFont(mono);
		totalPriceLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		this.add(totalPriceLabel);
		this.add(Box.createHorizontalGlue());
		this.add(sellButton);
	}

	private <T extends JComponent> T fixSize(T component, int width, int height) {
		Dimension dim = new Dimension(width, height);
		component.setMinimumSize(dim);
		component.setMaximumSize(dim);
		component.setPreferredSize(dim);
		//component.setBorder(BorderFactory.createLineBorder(Color.BLUE));
		
		return component;
	}
	
	public void setInvestment(Investment investment) {
		this.investment = investment;
		
		symbolLabel.setText(investment.getAsset().getSymbol());
		
		double shares = investment.getTotalPurchasePrice() / investment.getPurchasePricePerShare();
		
		sharesLabel.setText(String.format("%8.1f", shares));
		pricePerShareLabel.setText(String.format("$%8.2f", investment.getPurchasePricePerShare()));
		totalPriceLabel.setText(String.format("$%8.2f", investment.getTotalPurchasePrice()));
	}
	
	public Investment getInvestment() {
		return investment;
	}
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
	}
}
