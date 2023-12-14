package blue.endless.stonks.gui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Locale;

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
import blue.endless.stonks.model.Investment;
import blue.endless.stonks.model.Portfolio;
import blue.endless.stonks.sim.AssetPerformance;

public class StonksPrimaryFrame extends JFrame {
	private static final long serialVersionUID = 1L;
	private final Portfolio portfolio;
	private PortfolioView portfolioView;
	
	public StonksPrimaryFrame(Portfolio portfolio) {
		this.portfolio = portfolio;
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		this.setSize(1100, 600);
		this.setLayout(new BorderLayout());
		Container root = getContentPane();
		
		
		//Test stuff
		AssetPerformance perf = AssetPerformance.createRandom();
		
		
		//Set up the left-right split panel
		portfolioView = new PortfolioView(portfolio);
		root.add(portfolioView, BorderLayout.WEST);
		FunctionGraph rightPane = new FunctionGraph(perf::getValue);
		root.add(rightPane, BorderLayout.EAST);
		
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
		
	}
	
	public void savePortfolio() {
		JFileChooser dialog = new JFileChooser();
		dialog.setCurrentDirectory(new File("."));
		FileNameExtensionFilter filter = new FileNameExtensionFilter("TXT or JSON files", "txt", "json");
		dialog.setFileFilter(filter);
		int dialogResult = dialog.showSaveDialog(this);
		if (dialogResult == JFileChooser.APPROVE_OPTION) {
			File selected = dialog.getSelectedFile();
			
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
			
			if (selected.getName().toLowerCase(Locale.ROOT).endsWith(".txt")) {
				System.out.println("TODO: Load text file");
			//} else if (selected.getName().toLowerCase(Locale.ROOT).endsWith(".json")) {
			//	System.out.println("TODO: Load json file");
			} else {
				JOptionPane.showMessageDialog(this, "Don't know how to open file \""+selected.getName()+"\"", "Can't Open That", JOptionPane.ERROR_MESSAGE);
			}
		}
	}
}
