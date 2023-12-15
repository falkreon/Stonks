package blue.endless.stonks.sim;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import javax.swing.JOptionPane;

import blue.endless.jankson.Jankson;
import blue.endless.jankson.JsonElement;
import blue.endless.jankson.JsonGrammar;
import blue.endless.jankson.api.SyntaxError;
import blue.endless.stonks.model.Asset;
import blue.endless.stonks.model.AssetType;

public class AssetRegistry {
	private static Market market;
	private static transient List<Asset> additional = new ArrayList<>();
	
	public static AssetPerformance getSimulation(String symbol) {
		return market.simulations.get(symbol);
	}
	
	public static Asset getOrCreate(String symbol, AssetType type) {
		Optional<Asset> a = market.assets.stream().filter(it->it.getSymbol().equals(symbol)).findFirst();
		Supplier<Optional<Asset>> b = () -> additional.stream().filter(it->it.getSymbol().equals(symbol)).findFirst();
		
		Asset result = a.or(b).orElseGet(() -> {
			// *cache* it so we don't create multiples within a single portfolio load
			// Note: non-Market assets do not have dividends!
			Asset res = Asset.create(symbol, type);
			additional.add(res);
			return res;
		});
		
		// One last check - if the Asset we've retrieved conflicts with the type of an Asset already created, we've entered
		// a rarely exceptional state and we want to bail out.
		if (result.getCategory() != type) throw new IllegalStateException("Tried to load " + type + symbol + " which conflicts with an existing " + result.getCategory() + " of the same name.");
		
		return result;
	}
	
	public static void clearAdditional() {
		additional.clear();
	}
	
	public static void loadMarket() {
		Path marketFile = Path.of(".", "market.json");
		if (!Files.exists(marketFile)) {
			market = Market.create();
			saveMarket();
		}
		
		try {
			Jankson jankson = Jankson.builder().build();
			JsonElement arr = jankson.loadElement(Files.readString(marketFile));
			Market m = jankson.getMarshaller().marshall(Market.class, arr);
			market = m;
		} catch (SyntaxError | IOException ex) {
			market = Market.create();
			saveMarket();
		}
	}
	
	public static void saveMarket() {
		String saveString = Jankson.builder().build().toJson(market).toJson(JsonGrammar.STRICT);
		Path marketFile = Path.of(".", "market.json");
		try {
			Files.writeString(marketFile, saveString, StandardCharsets.UTF_8);
		} catch (IOException ex) {
			ex.printStackTrace();
			JOptionPane.showMessageDialog(null, "Could not save market data!", "Serious Error", JOptionPane.ERROR_MESSAGE);
			
		}
	}

	public static Market getMarket() {
		return market;
	}
}
