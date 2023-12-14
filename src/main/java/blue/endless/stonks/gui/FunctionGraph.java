package blue.endless.stonks.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.function.DoubleUnaryOperator;

import javax.swing.JComponent;

public class FunctionGraph extends JComponent {
	private static final long serialVersionUID = 1L;
	private DoubleUnaryOperator function;
	private double[] cachedValues = new double[365];
	private double maxValue = 0d;
	
	public FunctionGraph(DoubleUnaryOperator op) {
		this.function = op;
		//calc(0);
		/*
		for(int i=0; i<365; i++) {
			cachedValues[i] = op.applyAsDouble(i/360d);
			System.out.println("op: " + cachedValues[i]);
			maxValue = Math.max(maxValue, cachedValues[i]);
		}*/
		
		this.setMinimumSize(new Dimension(500, 500));
		this.setPreferredSize(new Dimension(600, 500));
		this.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
	}
	
	private void calc(double offset) {
		maxValue = 0d;
		for(int i=0; i<365; i++) {
			cachedValues[i] = function.applyAsDouble(offset + i/360d);
			maxValue = Math.max(maxValue, cachedValues[i]);
		}
	}
	
	private void drawCalc(Graphics g) {
		double sliceWidth = this.getWidth() / (double) cachedValues.length;
		
		int lastX = 0;
		int lastY = 0;
		
		for(int i=0; i<cachedValues.length; i++) {
			int x = (int) (sliceWidth * i);
			
			double percentUp = cachedValues[i] / maxValue;
			if (percentUp > 1d) percentUp = 1d;
			
			int y = (int) ((1 - percentUp) * this.getHeight());
			
			if (i>0) {
				g.drawLine(lastX, lastY, x, y);
			}
			lastX = x;
			lastY = y;
		}
	}
	
	@Override
	public void paint(Graphics g) {
		//super.paint(g);
		g.setColor(Color.GRAY);
		g.fillRect(0, 0, this.getWidth(), this.getHeight());
		
		g.setColor(Color.BLUE);
		calc(0);
		drawCalc(g);
		
		g.setColor(Color.GREEN);
		calc(365);
		drawCalc(g);
		
		g.setColor(Color.DARK_GRAY);
		calc(365 * 2);
		drawCalc(g);
		
		g.setColor(Color.RED);
		calc(365 * 3);
		drawCalc(g);
		
		/*
		int sliceWidth = this.getWidth() / cachedValues.length;
		
		int lastX = 0;
		int lastY = 0;
		
		int x = 0;
		for(int i=0; i<cachedValues.length; i++) {
			double percentUp = cachedValues[i] / maxValue;
			if (percentUp > 1d) percentUp = 1d;
			
			int y = (int) ((1 - percentUp) * this.getHeight());
			
			if (i>0) {
				g.drawLine(lastX, lastY, x, y);
			}
			lastX = x;
			lastY = y;
			
			x += sliceWidth;
		}*/
	}
}
