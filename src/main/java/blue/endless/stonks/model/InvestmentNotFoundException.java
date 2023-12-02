package blue.endless.stonks.model;

/**
 * Exception which is thrown when looking up a stock in a portfolio or price index, and the symbol is not present.
 */
public class InvestmentNotFoundException extends Exception {
	private static final long serialVersionUID = 1L;
	
	private final String symbol;
	
	public InvestmentNotFoundException(String symbol) {
		super("Stock '" + symbol + "' not found.");
		this.symbol = symbol;
	}
	
	public String getSymbol() {
		return symbol;
	}
}
