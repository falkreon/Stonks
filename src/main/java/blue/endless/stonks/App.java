package blue.endless.stonks;

import blue.endless.stonks.gui.StonksPrimaryFrame;
import blue.endless.stonks.model.AssetType;
import blue.endless.stonks.model.FractionalAsset;
import blue.endless.stonks.model.FractionalInvestment;
import blue.endless.stonks.model.NonFractionalAsset;
import blue.endless.stonks.model.NonFractionalInvestment;
import blue.endless.stonks.model.Portfolio;

public class App {
	public static void main(String... args) {
		Portfolio example = new Portfolio();
		NonFractionalAsset microsoftAsset = new NonFractionalAsset("MSFT", AssetType.STOCK);
		example.add(new NonFractionalInvestment(microsoftAsset, 368.80d, 20));
		
		NonFractionalAsset appleAsset = new NonFractionalAsset("APPL", AssetType.STOCK);
		example.add(new NonFractionalInvestment(appleAsset, 287.90d, 100));
		
		FractionalAsset vanguardIndex = new FractionalAsset("VSMPX", AssetType.MUTUAL_FUND);
		example.add(new FractionalInvestment(vanguardIndex, 183.20d, 14.6d));
		
		new StonksPrimaryFrame(example).setVisible(true);
	}
}
