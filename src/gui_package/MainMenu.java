package gui_package;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatIntelliJLaf;
import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.FlatLightLaf;

import backend_package.User;
import backend_package.Configuration;
import backend_package.Product;
import backend_package.Theme;
import lib.Libary;
import lib.themes.DarkTheme;
import lib.themes.LightTheme;

import javax.swing.JButton;
import java.awt.Dimension;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.ImageIcon;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.LinkedList;
import java.util.ListIterator;
import java.awt.event.ActionEvent;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class MainMenu extends JFrame {

	private JPanel contentPane;
	private JPanel panelC;
	private JButton btnStart;
	private JButton btnSettings;
	private JButton btnExit;
	private JLabel label;

//	private static User activeUser = null;
	private static User activeUser = new User(0, "admin", "admin", true, 100.0);
	private static Configuration configuration;
	private static Theme theme;
	
	// ---------- CUSTOM METHODS ---------------------------------------------
	
	void goToStart() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					App frame = new App(activeUser, configuration, theme);
					frame.setVisible(true);
					dispose();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	void goToSettings() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				if(activeUser.isAdmin() == false) {
					JOptionPane.showMessageDialog(contentPane, "User koji nema administratorske privilegije nije u mogućnosti da pristupi podešavanjima.", "Nedovoljne privilegije", JOptionPane.WARNING_MESSAGE);
				} else {
					try {
						dispose();
						Settings frame = new Settings(activeUser, configuration, theme);
						frame.setVisible(true);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		});
	}
	void goToLogin() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Login frame = new Login();
					frame.setModal(true);
					frame.setVisible(true);
					activeUser = frame.returnActiveUser();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public static void createRequiredFiles() {
		File mainDirectory = new File(Libary.getDirectoryPath());
		File lib = new File(Libary.getLibPath());
		File reports = new File(Libary.getReportsPath());
		File config = new File(Libary.getConfigPath());
		File inventory = new File(Libary.getInventoryPath());
		File users = new File(Libary.getUsersPath());
		
		if(!mainDirectory.exists()) {
			if(mainDirectory.mkdir() == true) System.out.println("Directory \"CafeMaster\\\" is created!");
			else {
				JOptionPane.showMessageDialog(null, "Directory \"CafeMaster\" cannot be created!", "Error!", JOptionPane.ERROR_MESSAGE);
				System.exit(0);
			}
		}
		
		if(!lib.exists()) {
			if(lib.mkdir() == true) {
				System.out.println("Directory \"CafeMaster\\lib\" is created!");
				
				File themesFile = new File(lib + "\\themes");
				if(!themesFile.exists()) {
					if(themesFile.mkdir() == true) {
						System.out.println("Directory \"CafeMaster\\lib\\themes\" is created!");
						Theme.createDefaultThemes();	
					}
					else {
						JOptionPane.showMessageDialog(null, "Directory \"CafeMaster\\lib\\themes\" cannot be created!", "Error!", JOptionPane.ERROR_MESSAGE);
						System.exit(0);
					}
				}
				
			} else {
				JOptionPane.showMessageDialog(null, "Directory \"CafeMaster\\lib\" cannot be created!!", "Error!", JOptionPane.ERROR_MESSAGE);
				System.exit(0);
			}	
		}
		
		if(!reports.exists()) {
			if(reports.mkdir() == true) System.out.println("Directory \"CafeMaster\\reports\" is created!");
			else {
				JOptionPane.showMessageDialog(null, "Directory \"CafeMaster\\reports\" cannot be created!", "Error", JOptionPane.ERROR_MESSAGE);
				System.exit(0);
			}
		}
		
		if(!config.exists()) {
			try {
				if(config.createNewFile() == true) System.out.println("File \"CafeMaster\\config.properties\" is created!");
				Configuration.createConfig();
			} catch (HeadlessException | IOException e) {
				JOptionPane.showMessageDialog(null, "File \"CafeMaster\\config.properties\" cannot be created!", "Error!", JOptionPane.ERROR_MESSAGE);
				System.exit(0);
			}
		}
		
		if(!inventory.exists()) {
			try {
				if(Libary.createEmptyInventory() == true) System.out.println("File \"CafeMaster\\inventory.xlsx\" is created!");
			} catch (HeadlessException e) {
				JOptionPane.showMessageDialog(null, "File \"CafeMaster\\inventory.txt\" cannot be created!", "Error!", JOptionPane.ERROR_MESSAGE);
				System.exit(0);
			}
		}
		
		if(!users.exists()) {
			try {
				if(users.createNewFile() == true) System.out.println("File \"CafeMaster\\users.txt\" is created!");
				
				PrintWriter pw = new PrintWriter(new File(Libary.getUsersPath()));
				//int id, String username, String password, boolean isAdmin, double pazar
				pw.println("0-admin-admin-true-0");
				pw.close();
			} catch (HeadlessException | IOException e) {
				JOptionPane.showMessageDialog(null, "File \"CafeMaster\\users.txt\" cannot be created!", "Error!", JOptionPane.ERROR_MESSAGE);
				System.exit(0);
			}
		}
	}	
	private static void trialSettings() {
		configuration.setNumOfTables(5);
		configuration.setTheme("lightTheme");
		configuration.setLanguage("Srpski");
		
		LinkedList<String> categories = new LinkedList<>();
		categories.add("BURGER");
		categories.add("BURRITO");
		categories.add("TACOS");
		categories.add("KRILCA");
		categories.add("QUESADILLA");
		categories.add("WEDGES");
		categories.add("DORUČAK");
		categories.add("DESERT");
		categories.add("PIVO");
		categories.add("SOSEVI");
		configuration.setCategories(categories);
		
		LinkedList<Product> products = new LinkedList<>();
		products.add(new Product("BURGER", "Black Burger Classic", 350.0, 100.0));
		products.add(new Product("BURGER", "BBG Black Burger", 320.0, 100.0));
		products.add(new Product("BURGER", "Bacon Burger", 320.0, 100.0));
		products.add(new Product("BURRITO", "Burrito Classic", 340.0, 80.0));
		products.add(new Product("BURRITO", "Chilli con Carne", 310.0, 80.0));
		products.add(new Product("BURRITO", "Chicken Burrito", 300.0, 80.0));
		products.add(new Product("TACOS", "Classic Tacos", 280.0, 90.0));
		products.add(new Product("TACOS", "Amigos Tacos", 290.0, 90.0));
		products.add(new Product("TACOS", "BBQ Tacos", 280.0, 90.0));
		products.add(new Product("TACOS", "Con Carne Tacos", 300.0, 90.0));
		products.add(new Product("KRILCA", "Classic", 320.0, 100.0));
		products.add(new Product("KRILCA", "Sweet Chilli Wings", 330.0, 100.0));
		products.add(new Product("KRILCA", "Garlic Wings", 310.0, 100.0));
		products.add(new Product("KRILCA", "Hot Wings", 320.0, 100.0));
		products.add(new Product("QUESADILLA", "Classic", 270.0, 50.0));
		products.add(new Product("QUESADILLA", "Con Carne", 290.0, 80.0));
		products.add(new Product("QUESADILLA", "Vegan", 250.0, 50.0));
		products.add(new Product("WEDGES", "Garlic Wedges", 170.0, 50.0));
		products.add(new Product("WEDGES", "BBQ Wedges", 180.0, 50.0));
		products.add(new Product("WEDGES", "Amigos Wedges", 190.0, 50.0));
		products.add(new Product("DORUČAK", "Monte Cristo Šunka", 220.0, 100.0));
		products.add(new Product("DORUČAK", "Monte Cristo Slanina", 260.0, 100.0));
		products.add(new Product("DORUČAK", "Monte Cristo Kulen", 230.0, 100.0));
		products.add(new Product("DORUČAK", "Monte Cristo Suvi Vrat", 250.0, 120.0));
		products.add(new Product("DORUČAK", "Burrito doručak ", 220.0, 100.0));
		products.add(new Product("DORUČAK", "Sendvič suvi vrat", 150.0, 100.0));
		products.add(new Product("DESERT", "Fried Oreos", 220.0, 130.0));
		products.add(new Product("PIVO", "Svetionik 0,33l flaša ", 220.0, 120.0));
		products.add(new Product("PIVO", "Svetionik 0,33l limenka ", 190.0, 100.0));
		products.add(new Product("PIVO", "Hoptopod 0,33l flaša ", 220.0, 120.0));
		products.add(new Product("PIVO", "Hoptopod 0,33l limenka ", 190.0, 100.0));
		products.add(new Product("SOSEVI", "Gvakamola sos", 70.0, 30.0));
		products.add(new Product("SOSEVI", "BBQ sos", 50.0, 10.0));
		products.add(new Product("SOSEVI", "Tzaciki sos", 0.0, 10.0));
		products.add(new Product("SOSEVI", "Avokado sos", 40.0, 30.0));
		products.add(new Product("SOSEVI", "Fresh sos", 0.0, 10.0));
		products.add(new Product("SOSEVI", "Slaninica sos", 0.0, 10.0));
		products.add(new Product("SOSEVI", "Burger sos", 0.0, 10.0));
		products.add(new Product("SOSEVI", "Kečap i majonez", 0.0, 10.0));
		Libary.insertAllProducts(products);
	}
	private static boolean checkParameters() {
		// returns false if user hasn't made any changes to config file
		// used for initializing trialSettings
		
		configuration = new Configuration(Libary.loadAllProperties());
		return !configuration.getCategories().get(0).isEmpty();
	}

	// -----------------------------------------------------------------------
	
	public static void main(String[] args) throws InterruptedException {
		createRequiredFiles();
		configuration = new Configuration(Libary.loadAllProperties());
		theme = new Theme();
		FlatLaf.setGlobalExtraDefaults( Collections.singletonMap( "@accentColor", theme.getAccentColor()) );
		FlatIntelliJLaf.setup();
		
		System.out.println("- ON START -\nthemeName: " + theme.getThemeName() + "\naccentColor: " + theme.getAccentColor() + "\n\n");
		
		theme.setActiveTheme(theme.getThemeName());

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainMenu frame = new MainMenu(activeUser);
					frame.setVisible(true);
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	// ----------------------------------------------------------------------
	
	public MainMenu(User ulogovanUser2) {
		addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode()==KeyEvent.VK_ENTER) {
					if(activeUser == null) {
						goToLogin();
					} else {
						goToStart();
					}
				}
			}
		});
		setResizable(false);
		setTitle("CafeMaster");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 546, 430);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		contentPane.add(getPanelC(), BorderLayout.CENTER);
		setLocationRelativeTo(null);
	}
	private JPanel getPanelC() {
		if (panelC == null) {
			panelC = new JPanel();
			panelC.setPreferredSize(new Dimension(1000, 10));
			GridBagLayout gbl_panelC = new GridBagLayout();
			gbl_panelC.columnWidths = new int[]{530, 0};
			gbl_panelC.rowHeights = new int[]{60, 60, 60, 60, 60};
			gbl_panelC.columnWeights = new double[]{0.0, Double.MIN_VALUE};
			gbl_panelC.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
			panelC.setLayout(gbl_panelC);
			GridBagConstraints gbc_label = new GridBagConstraints();
			gbc_label.fill = GridBagConstraints.VERTICAL;
			gbc_label.insets = new Insets(0, 0, 5, 0);
			gbc_label.gridx = 0;
			gbc_label.gridy = 0;
			panelC.add(getLabel(), gbc_label);
			GridBagConstraints gbc_btnStart = new GridBagConstraints();
			gbc_btnStart.insets = new Insets(5, 0, 5, 0);
			gbc_btnStart.gridx = 0;
			gbc_btnStart.gridy = 1;
			panelC.add(getBtnStart(), gbc_btnStart);
			GridBagConstraints gbc_btnSettings = new GridBagConstraints();
			gbc_btnSettings.fill = GridBagConstraints.VERTICAL;
			gbc_btnSettings.insets = new Insets(5, 0, 5, 0);
			gbc_btnSettings.gridx = 0;
			gbc_btnSettings.gridy = 2;
			panelC.add(getBtnSettings(), gbc_btnSettings);
			GridBagConstraints gbc_btnExit = new GridBagConstraints();
			gbc_btnExit.fill = GridBagConstraints.VERTICAL;
			gbc_btnExit.insets = new Insets(5, 0, 5, 0);
			gbc_btnExit.gridx = 0;
			gbc_btnExit.gridy = 3;
			panelC.add(getBtnExit(), gbc_btnExit);
		}
		return panelC;
	}
	private JButton getBtnStart() {
		if (btnStart == null) {
			btnStart = new JButton("PO\u010CETAK PROGRAMA");
			btnStart.setMinimumSize(new Dimension(200, 45));
			btnStart.setMaximumSize(new Dimension(500, 500));
			btnStart.setFont(new Font("Segoe UI", Font.BOLD, 14));
			btnStart.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if(activeUser == null) {
						goToLogin();
					} else {
						if(!checkParameters()) {
							int odgovor = JOptionPane.showConfirmDialog(contentPane, "Pre početka programa preporučujemo da kreirate parametre Vašeg restorana u podešavanjima.\nDa li želite da unesemo probni šablon parametara?", "Podešavanja parametara", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
							if(odgovor == JOptionPane.YES_OPTION) trialSettings();
							if(odgovor == JOptionPane.CLOSED_OPTION) return;
						}
						goToStart();
					}
				}
			});
			btnStart.setPreferredSize(new Dimension(300, 550));
			btnStart.setFocusable(false);
		}
		return btnStart;
	}
	private JButton getBtnSettings() {
		if (btnSettings == null) {
			btnSettings = new JButton("PODE\u0160AVANJA");
			btnSettings.setMaximumSize(new Dimension(500, 500));
			btnSettings.setMinimumSize(new Dimension(200, 45));
			btnSettings.setFont(new Font("Segoe UI", Font.BOLD, 14));
			btnSettings.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					if(activeUser != null) {
						goToSettings();
					} else {
						goToLogin();
					}
					
				}
			});
			btnSettings.setPreferredSize(new Dimension(300, 550));
			btnSettings.setFocusable(false);
		}
		return btnSettings;
	}
	private JButton getBtnExit() {
		if (btnExit == null) {
			btnExit = new JButton("IZA\u0110I IZ PROGRAMA");
			btnExit.setPreferredSize(new Dimension(300, 500));
			btnExit.setMaximumSize(new Dimension(500, 500));
			btnExit.setMinimumSize(new Dimension(200, 45));
			btnExit.setFont(new Font("Segoe UI", Font.BOLD, 14));
			btnExit.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					System.exit(0);
				}
			});
			btnExit.setFocusable(false);
		}
		return btnExit;
	}
	private JLabel getLabel() {
		if (label == null) {
			label = new JLabel("");
			label.setIcon(new ImageIcon(MainMenu.class.getResource("/lib/img/CafeMaster.png")));
		}
		return label;
	}

}
