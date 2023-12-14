package blue.endless.stonks.gui;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

public class Assets {
	public static BufferedImage ERROR_IMAGE = createErrorImage(26);
	public static BufferedImage BUY_IMAGE = getImage("buy");
	public static BufferedImage SELL_IMAGE = getImage("sell");
	
	private static BufferedImage createErrorImage(int size) {
		BufferedImage result = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
		for(int y=0; y<size; y++) {
			int yi = (y > size / 2) ? 0 : 1;
			for(int x=0; x<size; x++) {
				
				int xi = (x > size / 2) ? 0 : 1;
				
				int pixel = xi ^ yi;  // checkers
				pixel *= 0xFF00FF;    // if pixel is 1, transform it into magic pink; otherwise leave it 0 (black).
				pixel |= 0xFF_000000; // Make it opaque
				
				result.setRGB(x, y, pixel);
			}
		}
		return result;
	}
	
	private static BufferedImage getImage(String id) {
		try (InputStream in = Assets.class.getClassLoader().getResourceAsStream(id+".png")) {
			return ImageIO.read(in);
		} catch (IOException e) {
			e.printStackTrace();
			
			return ERROR_IMAGE;
		}
	}
}
