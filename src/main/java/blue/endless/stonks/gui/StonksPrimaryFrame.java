package blue.endless.stonks.gui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Locale;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.filechooser.FileNameExtensionFilter;

import blue.endless.jankson.Jankson;
import blue.endless.jankson.JsonGrammar;
import blue.endless.jankson.api.DeserializationException;
import blue.endless.jankson.api.SyntaxError;
import blue.endless.stonks.model.Investment;
import blue.endless.stonks.model.Portfolio;
import blue.endless.stonks.sim.AssetRegistry;
import blue.endless.stonks.sim.SimulationThread;

public class StonksPrimaryFrame extends JFrame {
	private static final long serialVersionUID = 1L;
	private Portfolio portfolio;
	private MarketView marketView;
	private PortfolioView portfolioView;
	
	public StonksPrimaryFrame(Portfolio portfolio) {
		this.portfolio = portfolio;
		
		this.setTitle("Stonks");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		this.setSize(1100, 600);
		this.setLayout(new BorderLayout());
		Container root = getContentPane();
		
		//Set up the left-right split panel
		Box box = new Box(BoxLayout.X_AXIS);
		root.add(box, BorderLayout.CENTER);
		portfolioView = new PortfolioView(portfolio);
		box.add(portfolioView);
		
		marketView = new MarketView(AssetRegistry.getMarket());
		box.add(marketView);
		
		//Setup menu
		JMenuBar menuBar = new JMenuBar();
		JMenu fileMenu = new JMenu("File");
		menuBar.add(fileMenu);
		JMenuItem saveItem = new JMenuItem("Save Portfolio", 's');
		saveItem.setAccelerator(KeyStroke.getKeyStroke('S', Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()));
		fileMenu.add(saveItem);
		saveItem.addActionListener((ActionEvent evt) -> savePortfolio());
		JMenuItem openItem = new JMenuItem("Open Portfolio", 'o');
		openItem.setAccelerator(KeyStroke.getKeyStroke('O', Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()));
		fileMenu.add(openItem);
		openItem.addActionListener((ActionEvent evt) -> loadPortfolio());
		
		this.setJMenuBar(menuBar);
		
		//TODO: Spawn a worker thread to advance the day
		SimulationThread simulation = new SimulationThread();
		simulation.attachMarketView(marketView);
		simulation.attachPortfolioView(portfolioView);
		simulation.start();
	}
	
	public void savePortfolio() {
		JFileChooser dialog = new JFileChooser();
		dialog.setCurrentDirectory(new File("."));
		FileNameExtensionFilter filter = new FileNameExtensionFilter("TXT or JSON files", "txt", "json");
		dialog.setFileFilter(filter);
		int dialogResult = dialog.showSaveDialog(this);
		if (dialogResult == JFileChooser.APPROVE_OPTION) {
			File selected = dialog.getSelectedFile();
			if (selected.exists()) {
				int replaceResult = JOptionPane.showConfirmDialog(this, "This file already exists. Replace it?");
				if (replaceResult != JOptionPane.OK_OPTION) return;
			}
			
			if (selected.getName().toLowerCase(Locale.ROOT).endsWith(".txt")) {
				String result = "";
				for(Investment inv : portfolio) {
					result += inv.toSaveText() + "\n";
				}
				
				try {
					Files.writeString(selected.toPath(), result);
				} catch (IOException ex) {
					ex.printStackTrace();
					JOptionPane.showMessageDialog(this, "There was an error saving the file: "+ex.getLocalizedMessage(), "Save Error", JOptionPane.ERROR_MESSAGE);
				}
				
			} else if (selected.getName().toLowerCase(Locale.ROOT).endsWith(".json")) {
				try {
					String result = Jankson.builder().build().toJson(portfolio).toJson(JsonGrammar.STRICT);
					Files.writeString(selected.toPath(), result);
				} catch (IOException ex) {
					ex.printStackTrace();
					JOptionPane.showMessageDialog(this, "There was an error saving the file: "+ex.getLocalizedMessage(), "Save Error", JOptionPane.ERROR_MESSAGE);
				}
			} else {
				JOptionPane.showMessageDialog(this, "Don't know how to save that kind of file", "Didn't Save", JOptionPane.ERROR_MESSAGE);
			}
			
		}
	}
	
	public void loadPortfolio() {
		JFileChooser dialog = new JFileChooser();
		dialog.setCurrentDirectory(new File("."));
		FileNameExtensionFilter filter = new FileNameExtensionFilter("TXT or JSON files", "txt", "json");
		dialog.setFileFilter(filter);
		int dialogResult = dialog.showOpenDialog(this);
		if (dialogResult == JFileChooser.APPROVE_OPTION) {
			File selected = dialog.getSelectedFile();
			if (!selected.exists()) {
				JOptionPane.showMessageDialog(this, "The file \""+selected.getName()+"\" does not exist.", "Doesn't Exist", JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			//Clear the AssetRegistry extras, which is primarily there to debounce Assets in Portfolios with multiple purchases of a custom asset.
			AssetRegistry.clearAdditional();
			
			if (selected.getName().toLowerCase(Locale.ROOT).endsWith(".txt")) {
				try {
					Portfolio loaded = new Portfolio();
					List<String> lines = Files.readAllLines(selected.toPath());
					for(String s : lines) {
						if (s.isBlank()) continue;
						Investment inv = Investment.fromSaveText(s);
						System.out.println("Loading text "+s+" -> "+inv.getAsset().getSymbol());
						loaded.add(inv);
					}
					this.portfolio = loaded;
					portfolioView.setPortfolio(loaded);
				} catch (IOException | DeserializationException ex) {
					ex.printStackTrace();
					JOptionPane.showMessageDialog(this, "There was an error trying to read the file: "+ex.getLocalizedMessage(), "Couldn't Open", JOptionPane.ERROR_MESSAGE);
				}
			} else if (selected.getName().toLowerCase(Locale.ROOT).endsWith(".json")) {
				System.out.println("TODO: Load json file");
				try {
					Portfolio p = Jankson.builder().build().fromJsonCarefully(Files.readString(selected.toPath()), Portfolio.class);
					this.portfolio = p;
					portfolioView.setPortfolio(p);
				} catch (SyntaxError ex) {
					ex.printStackTrace();
					JOptionPane.showMessageDialog(this, ex.getLocalizedMessage(), "Bad JSON", JOptionPane.ERROR_MESSAGE);
					
				} catch (IOException ex) {
					ex.printStackTrace();
					JOptionPane.showMessageDialog(this, "There was an error trying to read the file: "+ex.getLocalizedMessage(), "Couldn't Open", JOptionPane.ERROR_MESSAGE);
					
				} catch (DeserializationException e) {
					JOptionPane.showMessageDialog(this, "Could not read this JSON Portfolio.", "Bad JSON", JOptionPane.ERROR_MESSAGE);
				}
			} else {
				JOptionPane.showMessageDialog(this, "Don't know how to open file \""+selected.getName()+"\"", "Couldn't Open", JOptionPane.ERROR_MESSAGE);
			}
		}
	}
}
