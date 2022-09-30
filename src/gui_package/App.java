package gui_package;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.EventQueue;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.LinkedList;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import backend_package.Report;
import backend_package.User;
import backend_package.Configuration;
import backend_package.Product;
import backend_package.ProductButton;
import backend_package.Table;
import backend_package.TableButton;
import backend_package.TableOrdersModel;
import backend_package.Theme;
import lib.Libary;
import javax.swing.JTextField;
import javax.swing.JOptionPane;
import javax.swing.JLabel;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Panel;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ActionEvent;
import java.awt.SystemColor;
import javax.swing.JTabbedPane;
import java.awt.FlowLayout;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import java.awt.Component;
import javax.swing.UIManager;
import javax.swing.JTable;

public class App extends JFrame {

	private JPanel contentPane;
	private JPanel panelW;
	private JPanel panelE;
	private JPanel panelC;
	private JLabel lblOdabirStola;
	private JPanel panelWS;
	private JButton btnPlus;
	private JButton btnMinus;
	private JTabbedPane tabbedPane;
	private JScrollPane scrollPane;
	private JPanel panel;
	private JPanel panelCS;
	private JLabel lblRaunStola;
	private JTextField textField;
	private JButton btnNaplatiSto;
	private JPanel panelWN;
	private JLabel lnlSadrzajNarudzbine;
	private JPanel panelES;
	private JScrollPane scrollPaneWC;
	private JMenuBar menuBar;
	private JMenu mnItemReport;
	private JMenuItem mnItemCreateReport;
	private JMenuItem mnItemChangesSaved;
	private JMenuItem mnItemOpenFolder;
	private JMenuItem mnItemSettings;
	private JMenuItem mnItemBack;
	private JMenuItem mnItemCreateLogs;
	private JPanel panelN;
	private JPanel panelNW;
	private JPanel panelNE;
	private JLabel lblUser;
	private JLabel lblUsername;
	private JButton btnOdjava;
	private JLabel lblPazar;
	private JLabel lblPazarUsera;
	private JButton btnVratiPazar;
	private JTable table;
	
	private User activeUser;
	private static Configuration configuration;
	private static Theme theme;
	private boolean reportCreated;
	private boolean logsCreated;
	private boolean changesSaved = true;
	private Table selectedTable = null;
	private Product selectedProduct = null;
	private Table tables[] =  new Table[Libary.loadNumberOfTables()];

	private LinkedList<Product> productInventory = Libary.loadAllProducts(); // used for creating report of all products
	private LinkedList<Product> listOfOrderedProducts = new LinkedList<>(); // used for creating logs
	
	// ---------- CUSTOM METHODS ---------------------------------------------
	
	void goToMainMenu() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainMenu frame = new MainMenu(activeUser);
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
					JOptionPane.showMessageDialog(contentPane, "User koji nema administratorske privilegije nije u moguænosti da pristupi podešavanjima.", "Nedovoljne privilegije", JOptionPane.WARNING_MESSAGE);
				} else {
					try {
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
					User prethodniUser = activeUser;
					
					Login frame = new Login();
					frame.setModal(true);
					activeUser = frame.returnActiveUser();
					System.out.println("sad je modalno");
					frame.setVisible(true);
					System.out.println("sad je ulogovan " + activeUser);
					if(activeUser == null) activeUser = prethodniUser;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	boolean isNumber(char c) {
		if(c >= 48 && c <= 57) return true;
		return false;
	}
	int extractNumberFromString(String s) {
		// SREDITI
		int indexStart = Integer.MIN_VALUE, indexFinish = Integer.MIN_VALUE;
		boolean numberFound = false;
		
		for(int i=0; i<s.length(); i++) {
			if(isNumber(s.charAt(i)) == true) {
				if(numberFound == true) {
					indexFinish = i;
					if(i == s.length()) return Integer.parseInt(s.substring(indexStart, indexFinish+1));
					continue;
				} else {
					numberFound = true;
					indexStart = i;
					indexFinish = i;
					continue;
				}
			}
			if(isNumber(s.charAt(i)) == false && numberFound == true) return Integer.parseInt(s.substring(indexStart,indexFinish+1));
			
		}
		if(numberFound == true) {
			return Integer.parseInt(s.substring(indexStart, indexFinish+1));
		} else {
			return Integer.MIN_VALUE; // String s doesn't contain any integers.
		}
		
	}
	
	void showTableBill(Table table) {
		getTextField().setText(Double.toString(table.getTableBill()));
	}

	void displayAllOrdersForThisTable(Table selectedTable) {
		clearOrdersTable();
		LinkedList<Product> products = selectedTable.getOrders();
		Iterator<Product> it = products.iterator();
		
		while(it.hasNext()) {
			Product productIterator = it.next();
			displayNewOrder(selectedTable, productIterator);
		}
	}
	void clearOrdersTable() {
		TableOrdersModel tableModel = (TableOrdersModel) table.getModel();
		tableModel.clearTable();
	}
	void displayNewOrder(Table selectedTable, Product newProduct) {
		TableOrdersModel tableModel = (TableOrdersModel) table.getModel();
		tableModel.addRow(newProduct);
	}
	
	int countOccurencesOfProductInList(Product product, LinkedList<Product> list) {
		return Collections.frequency(list, product);
	}
	
	void incrementProduct(Product product) {		
		Iterator<Product> it = productInventory.iterator();
		
		while(it.hasNext()) {
			Product productIterator = it.next();
			if(productIterator.equals(product)) {
				productIterator.setQuantity(productIterator.getQuantity() + 1);
				return;
			}
		}
	}
	void decrementProduct(Product proizvod) {
		Iterator<Product> it = productInventory.iterator();
		
		while(it.hasNext()) {
			Product productIterator = it.next();
			if(productIterator.toString().matches(proizvod.toString())) {
				int count = productIterator.getQuantity();
				productIterator.setQuantity(count - 1);
				return;
			}
		}
	}
	int countProductQuantity(Product product) {
		Iterator<Product> it = productInventory.iterator();
		while(it.hasNext()) {
			Product productIterator = it.next();
			if(product.equals(productIterator)) {
				return productIterator.getQuantity();
			}
		}
		return 0;
	}
	boolean doesReportExist() {
		String reportPath = Libary.getReportsPath() + "\\" + Libary.createReportName();
		File reportFile = new File(reportPath);
		return reportFile.exists();
	}
	
	private static void selectColor(JRadioButton selectedButton) {
		if(theme.getThemeName().contains("light")) {
			selectedButton.setBackground(Libary.lighter(UIManager.getColor("TextArea.selectionBackground"), (float)0.7));
			selectedButton.setFont(new Font("Segoe UI", Font.BOLD, 25));
		} else {
			selectedButton.setBackground(Libary.darker(UIManager.getColor("TextArea.selectionBackground"), (float)0.7));
			selectedButton.setFont(new Font("Segoe UI", Font.BOLD, 25));
		}
		selectedButton.setFont(new Font("Segoe UI", Font.BOLD, 25));
	}
	private static void unselectColor(JRadioButton selectedButton) {
		selectedButton.setBackground(UIManager.getColor("RadioButton.interiorBackground"));
		selectedButton.setFont(new Font("Segoe UI", Font.PLAIN, 25));
	}
	private static void indebtedColor(JRadioButton selectedButton) {
		selectedButton.setBackground(UIManager.getColor("RadioButton.shadow"));
	}

	// ----------------------------------------------------------------------	
	// ------------------- FOR TESTING ONLY --------------------------------
//	public static void main(String[] args) {
//		EventQueue.invokeLater(new Runnable() {
//			public void run() {
//				try {
//					User activeUser = new User(0, "admin", "admin", true, 100.0);
//					Configuration configuration = new Configuration(Libary.loadAllProperties());
//					Theme theme = new Theme();
//					
//					FlatLaf.setGlobalExtraDefaults( Collections.singletonMap( "@accentColor", theme.getAccentColor()) );
//					FlatIntelliJLaf.setup();
//					
//					System.out.println("- ON START -\nthemeName: " + theme.getThemeName() + "\naccentColor: " + theme.getAccentColor() + "\n\n");
//					
//					theme.setActiveTheme(theme.getThemeName());
//					
//					App frame = new App(activeUser, null, null);
//					frame.setVisible(true);					
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		});
//	}
	// ----------------------------------------------------------------------	
	
	public App(User activeUser, Configuration configuration, Theme theme) throws Exception {
		this.activeUser = activeUser;
		this.configuration = configuration;
		this.theme = theme; // Has to be passed in order to go directly from App to Settings. TODO: make new constructor

		for(int i=0;i<Libary.loadNumberOfTables(); i++) {
			tables[i] = new Table();
		}
		
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent arg0) {
				if(changesSaved == false) {
					int response = JOptionPane.showConfirmDialog(contentPane, "Da li ste sigurni da želite da izaðete? Sve unete izmene neæe biti saèuvane u izveštaj.", "Potvrda izlaska", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
					if(response == JOptionPane.YES_OPTION) System.exit(0);
				} else if(reportCreated == false) {
					int response = JOptionPane.showConfirmDialog(contentPane, "Da li ste sigurni da želite da izaðete? Sve unete izmene neæe biti saèuvane u izveštaj.", "Potvrda izlaska", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
					if(response == JOptionPane.YES_OPTION) System.exit(0);
				} else {
					System.exit(0);
				}
			}
		});
		setTitle("CafeMaster");
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setBounds(100, 100, 1163, 750);
		setJMenuBar(getMenuBar_1());
		contentPane = new JPanel();
		//contentPane.(SystemColor.controlHighlight);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout());
		setContentPane(contentPane);
		contentPane.add(getPanelW(), BorderLayout.WEST);
		contentPane.add(getPanelE(), BorderLayout.EAST);
		contentPane.add(getPanelC(), BorderLayout.CENTER);
		contentPane.add(getPanelN(), BorderLayout.NORTH);
		
		setLocationRelativeTo(null);
		
		// ---
		for(int i=0; i<tables.length; i++)
			try {
				tables[i] = new Table(); // inicijalizacija stolova, globalna promenljiva
			} catch (Exception e) {
				System.err.println("GRESKA: Nije moguce inicijalizovati stolove! (" + Libary.getMethodName() + ")");
			}
		
		reportCreated = doesReportExist();
	}

	private JPanel getPanelW() {
		if (panelW == null) {
			panelW = new JPanel();
			
			Border titledBorder = BorderFactory.createTitledBorder("SADRŽAJ NARUDŽBINE");
			panelW.setBorder(titledBorder);
			((TitledBorder) titledBorder).setTitleJustification(TitledBorder.CENTER);
			((javax.swing.border.TitledBorder) panelW.getBorder()).setTitleFont(new Font("Segoe UI", Font.BOLD, 14));
			panelW.repaint();
			
			panelW.setLayout(new BorderLayout(0, 0));
			panelW.add(getPanelWS(), BorderLayout.SOUTH);
			panelW.add(getPanelWN(), BorderLayout.NORTH);
			panelW.add(getScrollPane_1_1(), BorderLayout.CENTER);
		}
		return panelW;
	}
	private GridLayout getLayout(int br) {
		GridLayout gl_panelEC;
		gl_panelEC=new GridLayout(br/2,2);
		return gl_panelEC;
	}
	private JPanel getPanelE() {
		if (panelE == null) {
			panelE = new JPanel();
			panelE.setPreferredSize(new Dimension(200, 10));
			panelE.setLayout(new BorderLayout());
			Panel panelEN=new Panel();
			panelE.add(panelEN,BorderLayout.NORTH);
			panelEN.add(getLblOdabirStola());
			
			int numberOfTables = Libary.loadNumberOfTables();
			
			Panel panelEC=new Panel(getLayout(numberOfTables));
			panelEC.setLayout(new GridLayout(0, 1, 0, 0));
			panelE.add(panelEC,BorderLayout.CENTER); 
			panelE.add(getPanelES(), BorderLayout.SOUTH);
			TableButton adder;
			ButtonGroup rbGroup = new ButtonGroup();
			
			for(int i=0; i<numberOfTables; i++) {
				adder = new TableButton(tables[i], "STO # " + Integer.toString(i+1));
				adder.setFont(new Font("Segoe UI", Font.PLAIN, 25));
				adder.setFocusable(true);
				adder.setAlignmentX(SwingConstants.CENTER);
				rbGroup.add(adder);
				panelEC.add(adder);
				adder.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						TableButton selectedButton = (TableButton) e.getSource();
						selectedTable = selectedButton.getLinkedTable();
						
						clearOrdersTable();
						displayAllOrdersForThisTable(selectedTable);
						showTableBill(selectedTable);
						selectColor(selectedButton);
					}
				});
				adder.addFocusListener(new FocusListener() {
					public void focusLost(FocusEvent e) {
						JRadioButton selectedButton = (JRadioButton) e.getSource();
	
						unselectColor(selectedButton);
						if(selectedTable != null && selectedTable.getTableBill() > 0) { // De-selected table needs to pay
							indebtedColor(selectedButton);
						}
					}
					public void focusGained(FocusEvent e) {
						if(selectedTable != null) {
							JRadioButton selectedButton = (JRadioButton) e.getSource();
							selectColor(selectedButton);
						}
					}
				});
			}
		}
		return panelE;
	}
	
	private JPanel getPanelC() {
		if (panelC == null) {
			panelC = new JPanel();
			//panelC.(SystemColor.controlHighlight);
			panelC.setLayout(new BorderLayout(0, 0));
			panelC.add(getTabbedPane(), BorderLayout.CENTER);
			panelC.add(getPanelCS(), BorderLayout.SOUTH);
		}
		return panelC;
	}
	private JLabel getLblOdabirStola() {
		if (lblOdabirStola == null) {
			lblOdabirStola = new JLabel("ODABIR STOLA");
			lblOdabirStola.setFont(new Font("Segoe UI", Font.BOLD, 17));
		}
		return lblOdabirStola;
	}
	private JPanel getPanelWS() {
		if (panelWS == null) {
			panelWS = new JPanel();
			//panelWS.(SystemColor.controlHighlight);
			panelWS.add(getBtnPlus());
			panelWS.add(getBtnMinus());
		}
		return panelWS;
	}
	private JButton getBtnPlus() {
		if (btnPlus == null) {
			
			btnPlus = new JButton("+");
			btnPlus.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					if(selectedProduct == null) {
						JOptionPane.showMessageDialog(contentPane, "Nije moguæe dodati proizvod.\nMolimo izaberite proizvod koji želite dodati.", "Dodavanje proizvoda", JOptionPane.INFORMATION_MESSAGE);
					} else {
						
					}
				}
			});
			btnPlus.setFont(new Font("Segoe UI", Font.BOLD, 25));
			btnPlus.setPreferredSize(new Dimension(125, 30));
			btnPlus.setFocusable(false);
		}
		return btnPlus;
	}
	private JButton getBtnMinus() {
		if (btnMinus == null) {
			btnMinus = new JButton("-");
			btnMinus.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					if(selectedProduct == null) {
						JOptionPane.showMessageDialog(contentPane, "Nije moguæe oduzeti proizvod.\nMolimo izaberite proizvod koji želite oduzeti.", "Oduzimanje proizvoda", JOptionPane.INFORMATION_MESSAGE);
					} else {
						Product toRemove = Libary.findLastOccurenceOfProduct(selectedProduct, selectedTable.getOrders());
						
						if(selectedTable.getOrders().remove(toRemove)) {
							System.out.println("Product \"" + toRemove + "\" is removed from the table #" + (selectedTable.getId() + 1) + "!");
						}
						
						decrementProduct(selectedProduct);
						activeUser.getOrders().remove(toRemove);
						showTableBill(selectedTable);
						clearOrdersTable();
						displayAllOrdersForThisTable(selectedTable);
						table.getSelectionModel().clearSelection();
						selectedProduct = null;
					}
				}

				
			});
			btnMinus.setFont(new Font("Segoe UI", Font.BOLD, 30));
			btnMinus.setPreferredSize(new Dimension(125, 30));
			btnMinus.setFocusable(false);
		}
		return btnMinus;
	}
	
	private JTabbedPane getTabbedPane() {
		if (tabbedPane == null) {
			tabbedPane = new JTabbedPane(JTabbedPane.TOP);
			tabbedPane.setFocusable(false);
			tabbedPane.setPreferredSize(new Dimension(500, 490));
			LinkedList<String> kategorije = Libary.loadAllCategories();
			LinkedList<Product> productsOfCategory = new LinkedList<>();
			Iterator<String> itK = kategorije.iterator();

			int numberOfCategories = Libary.loadAllCategories().size();
			JScrollPane adderSP;
			JPanel adderP;
			ProductButton adderB;
									
			for(int i=0; i<numberOfCategories; i++) {
				adderP = new JPanel();
				adderP.setLayout(new GridLayout(0,1));
				
				adderSP = new JScrollPane(adderP, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
				adderSP.setViewportView(adderP);
				String currentCategory = itK.next();
				tabbedPane.addTab(currentCategory, null, adderSP);
				productsOfCategory = Libary.loadAllProductsOfCategory(currentCategory, Libary.loadAllCategories(), Libary.loadAllProducts());
				Iterator<Product> itP = productsOfCategory.iterator();
				
				
				for(int j=0; j<productsOfCategory.size(); j++) {
					adderB = new ProductButton(itP.next());
					adderB.setMaximumSize(new Dimension(200, 200));
					adderB.setPreferredSize(new Dimension(100,100));
					adderB.setFont(new Font("Segoe UI", Font.PLAIN, 20));
					adderB.setFocusable(false);
					adderP.add(adderB);
			
					adderB.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							ProductButton selectedButton = (ProductButton) e.getSource();
							Product selectedProduct = selectedButton.getLinkedProduct();
		
							if(selectedTable == null) {
								JOptionPane.showMessageDialog(contentPane, "Molimo odaberite sto za koji želite da dodate proizvod!","Odabir stola", JOptionPane.INFORMATION_MESSAGE);
							} else {
								changesSaved = false;
								
								incrementProduct(selectedProduct);
								
								selectedProduct.setOrderTime(new GregorianCalendar());
								selectedProduct.setUserId(activeUser.getId());
								activeUser.addProductToUser(selectedProduct);
								listOfOrderedProducts.add(selectedProduct); // used to list all products for logs
								
								selectedTable.addProductToTable(selectedProduct);
								displayNewOrder(selectedTable, selectedProduct);
								showTableBill(selectedTable);
								
								selectedProduct = null;
							}
						}
					});
				}
				
			}
			
		}
		return tabbedPane;
	}
	private JScrollPane getScrollPane_1() {
		if (scrollPane == null) {
			scrollPane = new JScrollPane();
			scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
			scrollPane.setViewportView(getPanel());
		}
		return scrollPane;
	}
	private JPanel getPanel() {
		if (panel == null) {
			panel = new JPanel();
			panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		}
		return panel;
	}
	private JPanel getPanelCS() {
		if (panelCS == null) {
			panelCS = new JPanel();
			//panelCS.(SystemColor.controlHighlight);
			panelCS.setFocusable(false);
			panelCS.setPreferredSize(new Dimension(10, 40));
			panelCS.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
			panelCS.add(getLblRaunStola());
			panelCS.add(getTextField());
			panelCS.add(getBtnNaplatiSto());
			panelCS.add(getBtnVratiPazar());
		}
		return panelCS;
	}
	private JLabel getLblRaunStola() {
		if (lblRaunStola == null) {
			lblRaunStola = new JLabel("RA\u010CUN STOLA");
			lblRaunStola.setFont(new Font("Segoe UI", Font.BOLD, 14));
		}
		return lblRaunStola;
	}
	private JTextField getTextField() {
		if (textField == null) {
			textField = new JTextField();
			textField.setHorizontalAlignment(SwingConstants.CENTER);
			textField.setFocusable(false);
			textField.setMaximumSize(new Dimension(50,25));
			textField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
			textField.setEditable(false);
			textField.setPreferredSize(new Dimension(50, 25));
			textField.setColumns(20);
		}
		return textField;
	}
	private JButton getBtnNaplatiSto() {
		if (btnNaplatiSto == null) {
			btnNaplatiSto = new JButton("NAPLATI RA\u010CUN");
			btnNaplatiSto.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if(selectedTable == null) {
						JOptionPane.showMessageDialog(contentPane, "Molimo izaberite sto za koji želite da naplatite raèun", "Naplata raèuna", JOptionPane.INFORMATION_MESSAGE);
						return;
					}
					if(selectedTable.getTableBill() == 0.0) {
						JOptionPane.showMessageDialog(contentPane, "Trenutni sto nema potraživanja i nije moguæe naplatiti raèun.", "Naplata raèuna", JOptionPane.INFORMATION_MESSAGE);
						return;
					}
					int response = JOptionPane.showConfirmDialog(contentPane, "Da li ste sigurni da želite da naplatite potraživanja za dati sto?", "Naplata dugovanja stola", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
					if(response == JOptionPane.YES_OPTION) {
						activeUser.addTurnover(selectedTable);
						lblPazarUsera.setText(Double.toString(activeUser.getTurnover()) + " RSD");
						
						selectedTable.removeAllProductsFromTable();
						displayAllOrdersForThisTable(selectedTable);
						selectedTable.setTableBill(0);
						textField.setText("0");						
					}
				}
			});
			btnNaplatiSto.setFocusable(false);
			btnNaplatiSto.setPreferredSize(new Dimension(150, 25));
			btnNaplatiSto.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		}
		return btnNaplatiSto;
	}
	private JPanel getPanelWN() {
		if (panelWN == null) {
			panelWN = new JPanel();
			//panelWN.(SystemColor.controlHighlight);
			//panelWN.add(getLnlSadrzajNarudzbine());
		}
		return panelWN;
	}
	private JLabel getLnlSadrzajNarudzbine() {
		if (lnlSadrzajNarudzbine == null) {
			lnlSadrzajNarudzbine = new JLabel("SADRŽDAJ NARUDŽDBINE");
			lnlSadrzajNarudzbine.setFont(new Font("Segoe UI", Font.BOLD, 13));
		}
		return lnlSadrzajNarudzbine;
	}
	private JPanel getPanelES() {
		if (panelES == null) {
			panelES = new JPanel();
			//panelES.(SystemColor.controlHighlight);
			panelES.setPreferredSize(new Dimension(40, 40));
		}
		return panelES;
	}
	private JScrollPane getScrollPane_1_1() {
		if (scrollPaneWC == null) {
			scrollPaneWC = new JScrollPane();
			//scrollPaneWC.(SystemColor.controlHighlight);
			scrollPaneWC.setViewportView(getTable());
		}
		return scrollPaneWC;
	}
	private JMenuBar getMenuBar_1() {
		if (menuBar == null) {
			menuBar = new JMenuBar();
			menuBar.add(getMnItemReport());
			menuBar.add(getMnItemSettings());
			menuBar.add(getMnItemBack());
		}
		return menuBar;
	}
	private JMenu getMnItemReport() {
		if (mnItemReport == null) {
			mnItemReport = new JMenu("IZVEŠTAJ");
			mnItemReport.setBackground(Color.BLUE);
			mnItemReport.setMaximumSize(new Dimension(80, 32767));
			mnItemReport.setFont(new Font("Segoe UI", Font.PLAIN, 12));
			mnItemReport.add(getMnItemCreateReport());
			mnItemReport.add(getMnItemCreateLogs());
			mnItemReport.add(getMnItemOpenFolder());
			mnItemReport.add(getMnItemChangesSaved());
		}
		return mnItemReport;
	}
		
	private JMenuItem getMnItemCreateReport() {
		if (mnItemCreateReport == null) {
			mnItemCreateReport = new JMenuItem("Kreiraj izveštaj");
			mnItemCreateReport.setToolTipText("Kreira dnevni izveštaj koji prikazuje finansijsku analizu i kolièinu svakog proizvoda koji je naruèen.");
			mnItemCreateReport.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					
					File excelFile = new File (Libary.getReportsPath() + "\\" + Libary.createReportName());
					if(excelFile.exists() && Report.doesReportExist(excelFile)) {
						int response = JOptionPane.showConfirmDialog(contentPane, "Izveštaj za današnji dan je veæ kreiran. Da li želite da ga prepišete?","Kreiranje izveštaja", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
						if(response == JOptionPane.NO_OPTION || response == JOptionPane.CLOSED_OPTION) return;
					}
					
					Report report = new Report(productInventory);
					report.createReport();
					changesSaved = true;
					reportCreated = true;
				}
			});
		}
		return mnItemCreateReport;
	}
	private JMenuItem getMnItemChangesSaved() {
		if (mnItemChangesSaved == null) {
			mnItemChangesSaved = new JMenuItem("Proveri da li je izveštaj kreiran");
			mnItemChangesSaved.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					File report = new File(Libary.getReportsPath() + "\\" + Libary.createReportName());
					
					if(report.exists() == true) {
						if(changesSaved == false) {
							JOptionPane.showMessageDialog(contentPane, "Izveštaj je napravljen, ali nove promene nisu saèuvane.", "Uvid o postojanju izveštaja", JOptionPane.INFORMATION_MESSAGE);
						} else {
							JOptionPane.showMessageDialog(contentPane, "Izveštaj je napravljen i kompletno saèuvan.", "Uvid o postojanju izveštaja", JOptionPane.INFORMATION_MESSAGE);
						}
					} else {
						JOptionPane.showMessageDialog(contentPane, "Izveštaj nije kreiran.", "Uvid o postojanju izveštaja", JOptionPane.INFORMATION_MESSAGE);
					}
				}
			});
		}
		return mnItemChangesSaved;
	}
	private JMenuItem getMnItemOpenFolder() {
		if (mnItemOpenFolder == null) {
			mnItemOpenFolder = new JMenuItem("Otvori folder sa izveštajima");
			mnItemOpenFolder.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					try {
						Desktop.getDesktop().open(new File(Libary.getReportsPath()));
					} catch (IOException e1) {
						System.err.println("GRESKA: Nije moguce prikazati folder \"CafeMaster\\reports\"!");
					}
				}
			});
		}
		return mnItemOpenFolder;
	}
	private JMenuItem getMnItemSettings() {
		if (mnItemSettings == null) {
			mnItemSettings = new JMenuItem("PODEŠAVANJA");
			mnItemSettings.setForeground(UIManager.getColor("menuText"));
			mnItemSettings.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {	
					if(changesSaved == false) {
						int response = JOptionPane.showConfirmDialog(contentPane, "Da li ste sigurni da želite da izaðete? Sve unete izmene neæe biti saèuvane u izveštaju.", "Potvrda izlaska", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
						if(response == JOptionPane.YES_OPTION) {
							goToSettings();
							if(activeUser.isAdmin()==true) dispose();
						}
					} else {
						if(activeUser.isAdmin()==true) dispose();
						goToSettings();
					}
				}
			});
			//mntmPodeavanja.(SystemColor.controlHighlight);
			mnItemSettings.setMaximumSize(new Dimension(120, 32767));
			mnItemSettings.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		}
		return mnItemSettings;
	}
	private JMenuItem getMnItemBack() {
		if (mnItemBack == null) {
			mnItemBack = new JMenuItem("UNAZAD");
			mnItemBack.setForeground(UIManager.getColor("menuText"));
			//mntmUnazad.(SystemColor.controlHighlight);
			mnItemBack.setMaximumSize(new Dimension(100, 32767));
			mnItemBack.setFont(new Font("Segoe UI", Font.PLAIN, 12));
			mnItemBack.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {		
					if(changesSaved == false) {
						int response = JOptionPane.showConfirmDialog(contentPane, "Da li ste sigurni da želite da izaðete? Sve unete izmene neæe biti saèuvane.", "Potvrda izlaska", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
						if(response == JOptionPane.YES_OPTION) {
							goToMainMenu();
							dispose();
						}
					} else if(reportCreated == false) {
						int response = JOptionPane.showConfirmDialog(contentPane, "Da li ste sigurni da želite da izaðete? Izveštaj nije napravljen.", "Potvrda izlaska", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
						if(response == JOptionPane.YES_OPTION) {
							goToMainMenu();
							dispose();
						}
					} else {
						dispose();
						goToMainMenu();
					}
				}
			});
		}
		return mnItemBack;
	}
	private JMenuItem getMnItemCreateLogs() {
		if (mnItemCreateLogs == null) {
			mnItemCreateLogs = new JMenuItem("Kreiraj evidenciju");
			mnItemCreateLogs.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					
					File excelFile = new File (Libary.getReportsPath() + "\\" + Libary.createReportName());
					if(excelFile.exists() && Report.doLogsExist(excelFile)) {
						int response = JOptionPane.showConfirmDialog(contentPane, "Evidencija za današnji dan je veæ kreirana. Da li želite da je prepišete?","Kreiranje evidencije", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
						if(response == JOptionPane.NO_OPTION || response == JOptionPane.CLOSED_OPTION) return;						
					}
					
					Report report = new Report(listOfOrderedProducts);
					report.createLogs();

				}
			});
			mnItemCreateLogs.setToolTipText("Kreira evidenciju koja pruža uvid o naruèenim proizvodima kroz vreme.");
		}
		return mnItemCreateLogs;
	}
	private JPanel getPanelN() {
		if (panelN == null) {
			panelN = new JPanel();
			panelN.setBorder(new LineBorder(SystemColor.scrollbar));
			panelN.setPreferredSize(new Dimension(10, 50));
			panelN.setAlignmentX(Component.RIGHT_ALIGNMENT);
			//panelN.(SystemColor.controlHighlight);
			panelN.setLayout(new BorderLayout(0, 0));
			panelN.add(getPanelNW(), BorderLayout.WEST);
			panelN.add(getPanelNE(), BorderLayout.EAST);
		}
		return panelN;
	}
	private JPanel getPanelNW() {
		if (panelNW == null) {
			panelNW = new JPanel();
			panelNW.setPreferredSize(new Dimension(265, 10));
			panelNW.setFont(new Font("Tahoma", Font.PLAIN, 17));
			panelNW.setToolTipText("Ime korisnika na èijem profilu se obavlja poslovna aktivnost.");
			//panelNW.(SystemColor.controlHighlight);
			panelNW.setLayout(new GridLayout(2,2));
			panelNW.add(getLblUser());
			panelNW.add(getLblUsername());
			panelNW.add(getLblPazar());
			panelNW.add(getLblPazarUsera());
		}
		return panelNW;
	}
	private JPanel getPanelNE() {
		if (panelNE == null) {
			panelNE = new JPanel();
			panelNE.setPreferredSize(new Dimension(200, 10));
			panelNE.setLayout(new BorderLayout(0, 0));
			panelNE.add(getBtnOdjava());
		}
		return panelNE;
	}
	private JLabel getLblUser() {
		if (lblUser == null) {
			lblUser = new JLabel("KORISNIK:");
			lblUser.setHorizontalAlignment(SwingConstants.LEFT);
			lblUser.setFocusable(false);
			lblUser.setFont(new Font("Segoe UI", Font.PLAIN, 17));
			//lblUser.(SystemColor.controlHighlight);
		}
		return lblUser;
	}
	private JLabel getLblUsername() {
		if (lblUsername == null) {
			lblUsername = new JLabel(activeUser.getUsername());
			lblUsername.setToolTipText("");
			lblUsername.setPreferredSize(new Dimension(178, 20));
			lblUsername.setAlignmentX(Component.RIGHT_ALIGNMENT);
			lblUsername.setFocusable(false);
			lblUsername.setHorizontalTextPosition(SwingConstants.CENTER);
			lblUsername.setHorizontalAlignment(SwingConstants.CENTER);
			//lblUsername.(SystemColor.controlHighlight);
			lblUsername.setFont(new Font("Segoe UI Black", Font.BOLD, 15));
		}
		return lblUsername;
	}
	private JButton getBtnOdjava() {
		if (btnOdjava == null) {
			btnOdjava = new JButton("ODJAVI KORISNIKA");
			btnOdjava.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					int response = JOptionPane.showConfirmDialog(contentPane, "Da li ste sigurni da želite da se odjavite?", "Potvrda o odjavi sa profila", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
					if(response == JOptionPane.YES_OPTION) {
						goToLogin(); 	// NASTAVI, ZASTO NIJE MODALNO?
						System.out.println("sad je ulogovan " + activeUser.getUsername());
						lblUsername.setText(activeUser.getUsername());
						lblPazarUsera.setText(Double.toString(activeUser.getTurnover()) + " RSD");
						System.out.println("ulogovan je sad: " +activeUser.getUsername());
					}
				}
			});
			btnOdjava.setFocusable(false);
			btnOdjava.setPreferredSize(new Dimension(127, 30));
			btnOdjava.setFont(new Font("Segoe UI", Font.PLAIN, 15));
		}
		return btnOdjava;
	}
	private JLabel getLblPazar() {
		if (lblPazar == null) {
			lblPazar = new JLabel("PAZAR:");
			lblPazar.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		}
		return lblPazar;
	}
	private JLabel getLblPazarUsera() {
		if (lblPazarUsera == null) {
			lblPazarUsera = new JLabel("");
			lblPazarUsera.setHorizontalAlignment(SwingConstants.CENTER);
			lblPazarUsera.setText(Double.toString(activeUser.getTurnover()) + " RSD");
		}
		return lblPazarUsera;
	}
	private JButton getBtnVratiPazar() {
		if (btnVratiPazar == null) {
			btnVratiPazar = new JButton("VRATI PAZAR");
			btnVratiPazar.setFocusable(false);
			btnVratiPazar.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		}
		return btnVratiPazar;
	}
	private JTable getTable() {
		if (table == null) {
			TableOrdersModel tableModel = new TableOrdersModel(new LinkedList<Product>());
			table = new JTable(tableModel) {
				public boolean editCellAt(int row, int column, java.util.EventObject e) {
		            return false;
		         }
			};
			table.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
		        public void valueChanged(ListSelectionEvent event) {
		        	if(table.getSelectedRow() != -1) {
		        		selectedProduct = tableModel.getProductAt(table.getSelectedRow());
		        	} else {
		        		selectedProduct = null;
		        	}
		        }
		    });
			table.setFont(new Font("Segoe UI", Font.PLAIN, 18));
		}
		return table;
	}
}