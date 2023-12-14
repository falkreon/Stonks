package blue.endless.stonks.gui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JButton;

public class IconActionButton extends JButton implements ActionListener {
	private static final long serialVersionUID = 1L;
	private Runnable onClick = null;

	public IconActionButton(BufferedImage image, String text) {
		super(new ImageIcon(image));
		
		Dimension size = new Dimension(26, 26);
		this.setMinimumSize(size);
		this.setPreferredSize(size);
		this.setMaximumSize(size);
		
		this.setToolTipText(text);
	}
	
	public void onClick(Runnable r) {
		this.onClick = r;
	}

	/*
	 * I'm aware that I could just be using Action here. Unfortunately Actions often need access to local state and
	 * domain logic, 
	 */
	@Override
	public void actionPerformed(ActionEvent arg0) {
		if (onClick != null) onClick.run();
	}
	
	
}
