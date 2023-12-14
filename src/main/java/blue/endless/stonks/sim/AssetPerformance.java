package blue.endless.stonks.sim;

import java.util.ArrayList;
import java.util.Random;

public class AssetPerformance {
	public static record Harmonic(
			/** A number between 0 and 1 representing the offset into the year for the sine wave */
			double phase,
			/** Usually a whole number representing how many times the wave should repeat per year */
			double period,
			double amplitude) {
		
		public double getValue(double yearsSinceEpoch) {
			//2PI per year, plus 2PI per phase
			double theta = 2d * Math.PI * (yearsSinceEpoch * period + phase);
			
			return Math.sin(theta) * amplitude;
		}
	}
	
	private double median = 0d;
	private ArrayList<Harmonic> harmonics = new ArrayList<>();
	
	private AssetPerformance() {}
	
	public double getValue(double yearsSinceEpoch) {
		double result = median;
		for(Harmonic h : harmonics) {
			result += h.getValue(yearsSinceEpoch);
		}
		
		if (result < 0d) result = 0d;
		return result;
	}
	
	public static AssetPerformance createRandom() {
		Random r = new Random();
		AssetPerformance result = new AssetPerformance();
		
		result.median = (r.nextDouble() * 150d) + 200d;
		double amp = r.nextDouble() * 200d;
		result.harmonics.add(new Harmonic(r.nextDouble(), 1d, amp));
		amp /= 2;
		result.harmonics.add(new Harmonic(r.nextDouble(), 3.3d, amp));
		amp /= 2;
		result.harmonics.add(new Harmonic(r.nextDouble(), 5.5d, amp));
		
		return result;
	}
}
