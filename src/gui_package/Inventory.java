package gui_package;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Scanner;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import backend_package.*;
import lib.Libary;
import java.awt.Dimension;
import javax.swing.JSplitPane;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.ActionEvent;
import javax.swing.JSeparator;
import java.awt.Color;
import javax.swing.JLabel;
import java.awt.Font;

public class Inventory extends JFrame {
	private JPanel contentPane;
	private JPanel panelE;
	private JSplitPane splitPane;
	private JPanel panelKategorije;
	private JPanel panelProducti;
	private JButton btnIzbrisiProduct;
	private JButton btnUnazad;
	private JList<String> listKategorije;
	private JList listProducti;
	private JButton btnIzbrisiKategoriju;
	private JSeparator separator;
	private JPanel panelEN;
	private JPanel panelES;
	private JLabel lblProduct;
	private JLabel lblKategorija;
	private JButton btnIzmeniCenu;
	private JLabel lblSpace2;
	private JLabel lblSpace1;
	private JLabel lblSpace3;
	
	private User activeUser;
	
	// --- CUSTOM METODE ---
	void goToSettings() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Settings frame = new Settings(activeUser);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	String izbaciPrazneRedove(String dobijenText) {
		StringBuilder rezultat = new StringBuilder();
		String linija;
		
		Scanner scanner = new Scanner(dobijenText);
		while(scanner.hasNextLine()) {
			linija = scanner.nextLine();
			if(linija.isEmpty() == false) rezultat.append(linija + "\n");
		}
		scanner.close();
		return rezultat.toString();
		
	}
	void izbaciKategoriju(String kategorijaZaBrisanje) {
		LinkedList<String> kategorije = Libary.loadAllCategories();
		
		if(kategorije.contains(kategorijaZaBrisanje) == false) System.err.println("GRESKA: Uneta kategorija za brisanje se ne nalazi u listi kategorija u \"config.txt\"");
		
		// ovaj deo brise proizvode date kategorije
		if(Libary.loadAllProductsOfCategory(kategorijaZaBrisanje).size() > 0) {
			int odgovor = JOptionPane.showConfirmDialog(contentPane, "Da li ste sigurni da zelite da obrisete kategoriju \"" + kategorijaZaBrisanje + "\"?\nData kategorija sadrži proizvode.", "Potvrda o brisanju kategorije", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
			if(odgovor == JOptionPane.YES_OPTION) {
				if(kategorije.remove(kategorijaZaBrisanje) == true) System.out.println("Kategorija \"" + kategorijaZaBrisanje + "\" uspesno obrisana iz liste!");
				LinkedList<Product> listaProducta = Libary.loadAllProducts();
				// stavljam ga u petlju jer ne moze da se brise dok iterator radi, pa zato prestajemo sa iteratorom ovoliko puta, i vracamo se ponovo
				
				for(int i=0; i<Libary.loadAllProductsOfCategory(kategorijaZaBrisanje).size(); i++) {
					Product iteratorniProduct = null; // null ne bi trebao da se pojavi
					Iterator<Product> itP = listaProducta.iterator();
					while(itP.hasNext()) {
						iteratorniProduct = itP.next();
						if(iteratorniProduct.getCategory().matches(kategorijaZaBrisanje)) break;
					}
					listaProducta.remove(iteratorniProduct);
					
				}
				Libary.insertAllProducts(listaProducta);
				
			} else { // negativan odgovor
				return;
			}
		}
		
		// ovaj deo brise samo kategoriju
		try {
			if(kategorije.remove(kategorijaZaBrisanje) == true) System.out.println("Kategorija \"" + kategorijaZaBrisanje + "\" uspesno obrisana iz liste!");
			int brojStolova = Libary.loadNumberOfTables();
			PrintWriter pw = new PrintWriter(new FileWriter(Libary.getConfigPath()), false);
			StringBuilder str = new StringBuilder();
			str.append("brojStolova = " + brojStolova + "\n");
			str.append("kategorije = ");
			
			Iterator<String> it = kategorije.iterator();
			while(it.hasNext()) {
				String pomocni = it.next().toString();
				if(it.hasNext() == false) { // ako je trenutni it poslednji, stavljamo bez zareza
					str.append(pomocni);
				} else {
					str.append(pomocni + ", ");
				}
			}
			pw.print(str.toString());
			pw.close();
			selektovanaKategorija = null;
			System.out.println("Kategorija \"" + kategorijaZaBrisanje + "\" uspesno obrisana iz fajla \"config.txt\"!");
			
		} catch (IOException e) {
			Libary.errorIOException("config.txt", "izbaciKategoriju");
		}
		
	}
	void izbaciProduct(Product proizvodZaBrisanje) {
		LinkedList<Product> listaProducta = Libary.loadAllProducts();
		Iterator<Product> it = listaProducta.iterator();
		
		while(it.hasNext()) {
			Product iteratorniProduct = it.next();
			if(Libary.compareProducts(iteratorniProduct, proizvodZaBrisanje)) {
				listaProducta.remove(iteratorniProduct);
				break;
			}
		}
				
		Libary.insertAllProducts(listaProducta);
		System.out.println("Product \"" + proizvodZaBrisanje + "\" je obrisan iz fajla \"inventory.txt\"!");
		
	}
	
	Product kreirajProductOdStringa(String proizvodUStringu) {
		String[] deloviProducta = proizvodUStringu.split("-");
		Product proizvod = new Product(deloviProducta[0], deloviProducta[1], Double.parseDouble(deloviProducta[2]), Double.parseDouble(deloviProducta[3]));
		return proizvod;
	}
	
	String removeCharAt(String s, int index) {
		char[] array = new char[s.length()];
		for(int i=0; i<array.length; i++) array[i] = s.charAt(i);
		
		array[index] = '`'; // ZAKRPA, URADITI BOLJE!
		
		StringBuilder result = new StringBuilder();
		for(int i=0; i<array.length; i++) {
			if(array[i] == '`') continue;
			result.append(array[i]);
		}
		return result.toString();
	}
	String izbrisiKaraktereOdDatogIndexa(String s, int indexPocetak, int indexKraj) {
		if(indexPocetak > indexKraj) System.err.println("Uneti indexPocetka nije validan za indexKraj!");
		if(indexPocetak == indexKraj) return s.replace(s.charAt(indexPocetak), '\0');
		
		for(int i=indexKraj; i>=indexPocetak; indexKraj--, i--) {
			s = removeCharAt(s, indexKraj);
		}
		return s;
	}
	int pronadjiIndexPocetkaReci(String text, String rec) {
		if(text.contains(rec) == false) System.err.println("Data rec \"" + rec + "\"se ne nalazi u zadatom string-u!");
		
		String pomocna;
		int indexPocetka = Integer.MIN_VALUE;
		for(int i=0; i<text.length(); i++) {
			for(int j=i; j<=text.length(); j++) {
				pomocna = text.substring(i,j);
				if(pomocna.matches(rec)) indexPocetka = i;
			}
		}
		return indexPocetka;
	}
	
	
	// --- POMOCNE KLASE ---
	
	Product selektovanProduct = null;
	String selektovanaKategorija = null;
	
	public class HelperClass{
		double oldPrice = Double.MIN_VALUE;
		double newPrice = Double.MIN_VALUE;
	}
	
	// ---------------------------------------------------
	
//	public static void main(String[] args) {
//		EventQueue.invokeLater(new Runnable() {
//			public void run() {
//				try {
//					PrikazPostavki frame = new PrikazPostavki(activeUser);
//					frame.setVisible(true);
//					
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		});
//	}
	
	// ----------------------------------------------------
	public Inventory(User activeUser) {
		this.activeUser = activeUser;
		
		setPreferredSize(new Dimension(750, 750));
		setResizable(false);
		setTitle("CafeMaster - Inventar");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 845, 482);
		contentPane = new JPanel();
		contentPane.setPreferredSize(new Dimension(750, 750));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		contentPane.add(getPanelE(), BorderLayout.EAST);
		contentPane.add(getSplitPane(), BorderLayout.CENTER);
		
		setLocationRelativeTo(null);
	}

	private JPanel getPanelE() {
		if (panelE == null) {
			panelE = new JPanel();
			panelE.setPreferredSize(new Dimension(175, 250));
			panelE.setLayout(new BorderLayout(0, 0));
			panelE.add(getPanelEN(), BorderLayout.CENTER);
			panelE.add(getPanelES(), BorderLayout.SOUTH);
		}
		return panelE;
	}
	private JSplitPane getSplitPane() {
		if (splitPane == null) {
			splitPane = new JSplitPane();
			splitPane.setPreferredSize(new Dimension(250, 25));
			splitPane.setLeftComponent(getPanelKategorije());
			splitPane.setRightComponent(getPanelProducti());
		}
		return splitPane;
	}
	private JPanel getPanelKategorije() {
		if (panelKategorije == null) {
			panelKategorije = new JPanel();
			panelKategorije.setPreferredSize(new Dimension(250, 250));
			panelKategorije.setLayout(new BorderLayout(0, 0));
			panelKategorije.add(getListKategorije(), BorderLayout.CENTER);
			//repaint();
		}
		return panelKategorije;
	}
	private JPanel getPanelProducti() {
		if (panelProducti == null) {
			panelProducti = new JPanel();
			panelProducti.setPreferredSize(new Dimension(250, 250));
			panelProducti.setLayout(new BorderLayout(0, 0));
			panelProducti.add(getListProducti(), BorderLayout.CENTER);
		}
		return panelProducti;
	}
	
	
	private JButton getBtnIzbrisiProduct() {
		if (btnIzbrisiProduct == null) {
			btnIzbrisiProduct = new JButton("IZBRI\u0160I PROIZVOD");
			btnIzbrisiProduct.setFocusable(false);
			btnIzbrisiProduct.setFont(new Font("Arial", Font.PLAIN, 11));
			btnIzbrisiProduct.setPreferredSize(new Dimension(150, 30));
			btnIzbrisiProduct.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					if(selektovanProduct == null) {
						JOptionPane.showMessageDialog(contentPane, "Molimo odaberite željeni proizvod za brisanje.", "Odabir proizvoda", JOptionPane.INFORMATION_MESSAGE);
					} else {
						int odgovor = JOptionPane.showConfirmDialog(contentPane, "Da li ste sigurni da želite da obrišete proizvod?", "Potvrda o brisanju proizvoda", JOptionPane.YES_NO_OPTION);
						if(odgovor == JOptionPane.YES_OPTION) {
							Product proizvodZaBrisanje = selektovanProduct;
							izbaciProduct(proizvodZaBrisanje);
							selektovanProduct = null;
							
							// refreshujemo listu
							DefaultListModel<Product> dlm = new DefaultListModel<>();
							LinkedList<Product> proizvodiKategorije = Libary.loadAllProductsOfCategory(selektovanaKategorija);
							Iterator<Product> it = proizvodiKategorije.iterator();

							while(it.hasNext()) {
								dlm.addElement(it.next());
							}
							getListProducti().setModel(dlm);
						}
					}
				}
			});
		}
		return btnIzbrisiProduct;
	}
	private JButton getBtnUnazad() {
		if (btnUnazad == null) {
			btnUnazad = new JButton("UNAZAD");
			btnUnazad.setFocusable(false);
			btnUnazad.setFont(new Font("Arial", Font.PLAIN, 11));
			btnUnazad.setPreferredSize(new Dimension(150, 30));
			btnUnazad.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					goToSettings();
					dispose();
				}
			});
		}
		return btnUnazad;
	}
	private JList getListKategorije() {
		if (listKategorije == null) {
			LinkedList<String> kategorije = Libary.loadAllCategories();
			
			listKategorije = new JList();
			listKategorije.setFont(new Font("Tahoma", Font.PLAIN, 13));
			DefaultListModel<String> dlm = new DefaultListModel<>();
			Iterator<String> it = kategorije.iterator();
			while(it.hasNext()) {
				dlm.addElement(it.next());
			}
			listKategorije.setModel(dlm);
			
			// akcija kad se klikne kategorija
			listKategorije.addListSelectionListener(new ListSelectionListener() {
				public void valueChanged(ListSelectionEvent arg0) {
					// kada kliknemo na kategoriju, otvara se nova lista
					if(listKategorije.isSelectionEmpty()==false){
					selektovanProduct = null;
					selektovanaKategorija = listKategorije.getSelectedValue().toString();
					LinkedList<Product> proizvodiKategorije = Libary.loadAllProductsOfCategory(selektovanaKategorija);
					
					listProducti.removeAll();
					DefaultListModel<String> dlm2 = new DefaultListModel<>();
					
					Iterator<Product> it = proizvodiKategorije.iterator();
					while(it.hasNext()) {
						Product temp = it.next();
						dlm2.addElement(temp.toString());
					}
					listProducti.setModel(dlm2);
				}
				}
			});
			
		}
		return listKategorije;
	}
	private JList getListProducti() {
		if (listProducti == null) {
			listProducti = new JList();
			listProducti.setFont(new Font("Tahoma", Font.PLAIN, 13));
			listProducti.addListSelectionListener(new ListSelectionListener() {
				public void valueChanged(ListSelectionEvent arg0) {
					if(((JList)arg0.getSource()).getSelectedValue() != null) {
						selektovanProduct = Libary.stringToProduct(((JList)arg0.getSource()).getSelectedValue().toString());
					}
				}
			});
		}
		return listProducti;
	}
	private JButton getBtnIzbrisiKategoriju() {
		if (btnIzbrisiKategoriju == null) {
			btnIzbrisiKategoriju = new JButton("IZBRI\u0160I KATEGORIJU");
			btnIzbrisiKategoriju.setFocusable(false);
			btnIzbrisiKategoriju.setFont(new Font("Arial", Font.PLAIN, 11));
			btnIzbrisiKategoriju.setPreferredSize(new Dimension(150, 30));
			btnIzbrisiKategoriju.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if(selektovanaKategorija == null) {
						JOptionPane.showMessageDialog(contentPane, "Molimo odaberite kategoriju za brisanje.", "Odabir kategorije za brisanje", JOptionPane.INFORMATION_MESSAGE);
					}else {
						String kategorijaZaBrisanje = listKategorije.getSelectedValue();
						izbaciKategoriju(kategorijaZaBrisanje);
						listKategorije.clearSelection();
						
						// refreshuje se kategorija
						listKategorije.removeAll();
						LinkedList<String> kategorije = Libary.loadAllCategories();
						DefaultListModel<String> dlm = new DefaultListModel<>();
						Iterator<String> it = kategorije.iterator();
						while(it.hasNext()) {
							dlm.addElement(it.next());
						}
						listKategorije.setModel(dlm);
						selektovanaKategorija = null;
						
						//refreshujemo listu
						DefaultListModel<String> dlm2 = new DefaultListModel<>();
						listProducti.setModel(dlm2);
						selektovanProduct = null;
						
					}
				}
				});
		}
		return btnIzbrisiKategoriju;
	}
	private JSeparator getSeparator() {
		if (separator == null) {
			separator = new JSeparator();
			separator.setMinimumSize(new Dimension(120, 100));
			separator.setPreferredSize(new Dimension(165, 2));
			separator.setBackground(Color.GRAY);
			separator.setForeground(Color.GRAY);
		}
		return separator;
	}
	private JPanel getPanelEN() {
		if (panelEN == null) {
			panelEN = new JPanel();
			panelEN.add(getLblProduct());
			panelEN.add(getLblSpace1());
			panelEN.add(getBtnIzbrisiProduct());
			panelEN.add(getBtnIzmeniCenu());
			panelEN.add(getLblSpace2());
			panelEN.add(getSeparator());
			panelEN.add(getLblKategorija());
			panelEN.add(getLblSpace3());
			panelEN.add(getBtnIzbrisiKategoriju());
		}
		return panelEN;
	}
	private JPanel getPanelES() {
		if (panelES == null) {
			panelES = new JPanel();
			panelES.add(getBtnUnazad());
		}
		return panelES;
	}
	private JLabel getLblProduct() {
		if (lblProduct == null) {
			lblProduct = new JLabel("PROIZVOD");
			lblProduct.setFont(new Font("Arial", Font.PLAIN, 11));
		}
		return lblProduct;
	}
	private JLabel getLblKategorija() {
		if (lblKategorija == null) {
			lblKategorija = new JLabel("KATEGORIJA");
			lblKategorija.setFont(new Font("Arial", Font.PLAIN, 11));
		}
		return lblKategorija;
	}
	private JButton getBtnIzmeniCenu() {
		if (btnIzmeniCenu == null) {
			btnIzmeniCenu = new JButton("IZMENI CENU");
			btnIzmeniCenu.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					if(selektovanProduct == null) {
						JOptionPane.showMessageDialog(contentPane, "Izaberite proizvod kojem želite izmeniti cenu.", "Izmena cene", JOptionPane.INFORMATION_MESSAGE);
					} else {
						HelperClass pom = new HelperClass();
						pom.oldPrice = selektovanProduct.getPrice();
							try {								
								ChangePrice dialog = new ChangePrice(pom);
								dialog.setModal(true);
								dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
								dialog.setVisible(true);
							} catch (Exception e) {
								e.printStackTrace();
							}
						
							LinkedList<Product> proizvodi = Libary.loadAllProducts();
							Iterator<Product> it = proizvodi.iterator();
							DefaultListModel<Product> dlm = new DefaultListModel<>();
							
							while(it.hasNext()) {
								Product iteratorniProduct = it.next();
								if(Libary.compareProducts(iteratorniProduct, selektovanProduct)) {
									try {
										if(pom.newPrice == Double.MIN_VALUE) {
											// ukoliko je prozon zatovren na X, vraca MIN (naci bolji nacin)
											iteratorniProduct.setPrice(pom.oldPrice);
										} else {
											iteratorniProduct.setPrice(pom.newPrice);
										}	
									} catch (Exception e) {
										Libary.errorException("getBtnIzmeniCenu");
									}
									break;
								}
						}
							Libary.insertAllProducts(proizvodi);
							
							LinkedList<Product> proizvodiIsteKategorije = Libary.loadAllProductsOfCategory(selektovanaKategorija);
							Iterator<Product> it2 = proizvodiIsteKategorije.iterator();
							DefaultListModel<Product> dlm2 = new DefaultListModel<>();
							while(it2.hasNext()) {
								dlm2.addElement(it2.next());
							}
							getListProducti().setModel(dlm2);	
							getListProducti().setSelectedIndex(-1);
							selektovanProduct = null;
					}
				}
			});
			btnIzmeniCenu.setFocusable(false);
			btnIzmeniCenu.setPreferredSize(new Dimension(150, 30));
			btnIzmeniCenu.setFont(new Font("Arial", Font.PLAIN, 11));
		}
		return btnIzmeniCenu;
	}
	private JLabel getLblSpace2() {
		if (lblSpace2 == null) {
			lblSpace2 = new JLabel("");
			lblSpace2.setPreferredSize(new Dimension(50, 50));
		}
		return lblSpace2;
	}
	private JLabel getLblSpace1() {
		if (lblSpace1 == null) {
			lblSpace1 = new JLabel("");
			lblSpace1.setPreferredSize(new Dimension(150, 10));
		}
		return lblSpace1;
	}
	private JLabel getLblSpace3() {
		if (lblSpace3 == null) {
			lblSpace3 = new JLabel("");
			lblSpace3.setPreferredSize(new Dimension(150, 10));
		}
		return lblSpace3;
	}
}
