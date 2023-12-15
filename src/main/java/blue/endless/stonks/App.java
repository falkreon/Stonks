package blue.endless.stonks;

import blue.endless.stonks.gui.StonksPrimaryFrame;
import blue.endless.stonks.model.Asset;
import blue.endless.stonks.model.AssetType;
import blue.endless.stonks.model.FractionalAsset;
import blue.endless.stonks.model.FractionalInvestment;
import blue.endless.stonks.model.NonFractionalInvestment;
import blue.endless.stonks.model.Portfolio;
import blue.endless.stonks.sim.AssetRegistry;

public class App {
	public static void main(String... args) {
		AssetRegistry.loadMarket();
		
		Portfolio example = new Portfolio();
		Asset msftAsset = AssetRegistry.getOrCreate("MSFT", AssetType.STOCK);
		example.add(new NonFractionalInvestment(msftAsset, 368.80d, 20));
		
		Asset applAsset = AssetRegistry.getOrCreate("APPL", AssetType.STOCK);
		example.add(new NonFractionalInvestment(applAsset, 287.90d, 100));
		
		Asset vanguardIndex = AssetRegistry.getOrCreate("VSMPX", AssetType.MUTUAL_FUND);
		example.add(new FractionalInvestment((FractionalAsset) vanguardIndex, 183.20d, 14.6d));
		
		new StonksPrimaryFrame(example).setVisible(true);
	}
}
