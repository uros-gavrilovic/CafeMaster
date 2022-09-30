package gui_package;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Properties;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JSlider;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JColorChooser;

import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.SwingConstants;
import java.awt.Dimension;
import javax.swing.event.ChangeListener;

import backend_package.User;
import backend_package.Configuration;
import backend_package.Product;
import backend_package.Theme;
import lib.Libary;
import lib.PlaceholderTextField;
import lib.themes.DarkTheme;
import lib.themes.LightTheme;

import javax.swing.event.ChangeEvent;
import java.awt.Font;
import java.awt.Graphics2D;

import javax.swing.JList;
import javax.imageio.ImageIO;
import javax.swing.AbstractListModel;
import javax.swing.BorderFactory;
import javax.swing.ListSelectionModel;
import javax.swing.JToggleButton;
import javax.swing.JSeparator;
import java.awt.SystemColor;
import javax.swing.event.ListSelectionListener;

import org.apache.poi.sl.usermodel.Placeholder;

import com.formdev.flatlaf.FlatIntelliJLaf;
import com.formdev.flatlaf.FlatLaf;

import javax.swing.event.ListSelectionEvent;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.List;
import java.awt.RenderingHints;

import javax.swing.JRadioButton;
import javax.swing.JSplitPane;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.JScrollBar;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.Icon;
import javax.swing.ImageIcon;

import java.awt.Color;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Container;
import java.awt.CardLayout;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.colorchooser.AbstractColorChooserPanel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.FlowLayout;
import javax.swing.UIManager;

public class Settings extends JFrame {	
	
	private JPanel contentPane;
	private JPanel panelN;
	private JList list;
	private JPanel panelC;
	private JPanel panelS_general;
	private JPanel panelS_products;
	private JPanel panelS_users;
	private PlaceholderTextField txtNewCategory;
	private PlaceholderTextField  txtProductName;
	private PlaceholderTextField  txtProductPrice;
	private PlaceholderTextField  txtProductExpenses;
	private JComboBox<String> comboBoxProductCategory;
	private JLabel lblError;
	private JList listCategories;
	private JList listProducts;
	private JPanel panel;
	private JButton btnNewButton;
	private JButton btnNewButton_1;
	private JSlider slider = new JSlider();
	private JToggleButton btnTheme = new JToggleButton();
	
	public User activeUser;
	private static Configuration configuration = new Configuration(Libary.loadAllProperties());;
	private Theme theme;
	
	private boolean categorySelected = false;
	private boolean productSelected = false;
	private LinkedList<String> categories = Libary.loadAllCategories();
	private LinkedList<Product> products = Libary.loadAllProducts();
	private LinkedList<Product> productsOfCategory = null;

	// ---------- CUSTOM METODE ---------------------------------------------

	private ImageIcon rescaleImage(File source,int maxHeight, int maxWidth)
	{
	    int newHeight = 0, newWidth = 0;        // Variables for the new height and width
	    int priorHeight = 0, priorWidth = 0;
	    BufferedImage image = null;
	    ImageIcon sizeImage;

	    try {
	            image = ImageIO.read(source);        // get the image
	    } catch (Exception e) {

	            e.printStackTrace();
	            System.out.println("Picture upload attempted & failed");
	    }

	    sizeImage = new ImageIcon(image);

	    if(sizeImage != null)
	    {
	        priorHeight = sizeImage.getIconHeight(); 
	        priorWidth = sizeImage.getIconWidth();
	    }

	    // Calculate the correct new height and width
	    if((float)priorHeight/(float)priorWidth > (float)maxHeight/(float)maxWidth)
	    {
	        newHeight = maxHeight;
	        newWidth = (int)(((float)priorWidth/(float)priorHeight)*(float)newHeight);
	    }
	    else 
	    {
	        newWidth = maxWidth;
	        newHeight = (int)(((float)priorHeight/(float)priorWidth)*(float)newWidth);
	    }


	    // Resize the image

	    // 1. Create a new Buffered Image and Graphic2D object
	    BufferedImage resizedImg = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
	    Graphics2D g2 = resizedImg.createGraphics();

	    // 2. Use the Graphic object to draw a new image to the image in the buffer
	    g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
	    g2.drawImage(image, 0, 0, newWidth, newHeight, null);
	    g2.dispose();

	    // 3. Convert the buffered image into an ImageIcon for return
	    return (new ImageIcon(resizedImg));
	}
	private JButton createButton(Icon icon, String buttonText, boolean isOnLeftSide){
        int nButtonHeight = 60;
        int nGap = 40;

        JButton jButton = new JButton();
        jButton.setIcon(icon);
        jButton.setIconTextGap(nGap);

        if(isOnLeftSide){
            jButton.setHorizontalAlignment(SwingConstants.LEFT);
            jButton.setHorizontalTextPosition(SwingConstants.RIGHT);
            jButton.setText(buttonText);
        }else{
            jButton.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
            jButton.setHorizontalAlignment(SwingConstants.RIGHT);
            jButton.setHorizontalTextPosition(SwingConstants.LEFT);
            jButton.setText("<html><div align=left width=200px>" + buttonText + "</div></html>");
        }

        Dimension jSize = new Dimension(WIDTH, nButtonHeight);
        jButton.setPreferredSize(jSize);
        jButton.setMaximumSize(jSize);
        jButton.setMinimumSize(jSize);
        return jButton;
    }
	private void rotateIcon(JToggleButton button, boolean isOnLeft) {
		if(isOnLeft){
			button.setHorizontalAlignment(SwingConstants.LEFT);
			button.setHorizontalTextPosition(SwingConstants.RIGHT);
			button.setText(button.getText());
        }else{
            //if you want to set icon position to right side of the button
        	button.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        	button.setHorizontalAlignment(SwingConstants.RIGHT);
        	button.setHorizontalTextPosition(SwingConstants.LEFT);
        	button.setText("<html><div align=left width=200px>" + button.getText() + "</div></html>");
        }
	}
    private static String ColorToHex (Color color) {
	    int red = color.getRed();
	    int green = color.getGreen();
	    int blue = color.getBlue();
	    int alpha = color.getAlpha(); 
	
	    String redHex = To00Hex(red);
	    String greenHex = To00Hex(green);
	    String blueHex = To00Hex(blue);
	    String alphaHex = To00Hex(alpha);
	
	    // hexBinary value: RRGGBBAA
	    StringBuilder str = new StringBuilder("#");
	    str.append(redHex);
	    str.append(greenHex);
	    str.append(blueHex);
	    str.append(alphaHex);
	
	    return str.toString();
}
    private static String To00Hex(int value) {
        String hex = "00".concat(Integer.toHexString(value));
        hex=hex.toUpperCase();
        return hex.substring(hex.length()-2, hex.length());
    } 
	private void changeNumOfTables(int numTables) {
		configuration.setNumOfTables(numTables);
	}
	private Border createTitledBorder(String title){
		Font font = new Font("Segoe UI", Font.BOLD, 18);
		Border border = BorderFactory.createTitledBorder(null, title, TitledBorder.LEFT, TitledBorder.DEFAULT_POSITION, font);
		return border;
	}
	private static void trialSettings() {
		configuration.setNumOfTables(5);
		configuration.setTheme("lightTheme"); // accentColor is set in updateSettingsMenu()
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
		products.add(new Product("DORUšAK", "Monte Cristo Šunka", 220.0, 100.0));
		products.add(new Product("DORUšAK", "Monte Cristo Slanina", 260.0, 100.0));
		products.add(new Product("DORUšAK", "Monte Cristo Kulen", 230.0, 100.0));
		products.add(new Product("DORUšAK", "Monte Cristo Suvi Vrat", 250.0, 120.0));
		products.add(new Product("DORUšAK", "Burrito doručak ", 220.0, 100.0));
		products.add(new Product("DORUšAK", "Sendvič suvi vrat", 150.0, 100.0));
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
	private void updateSettingsMenu() {
		// Used to update GUI of Settings, to reflect revert to default settings.
		// Default settings are set to template.
		
		int numOfTables = loadNumberOfTables();
		slider.setValue(numOfTables);
		theme.setActiveTheme(configuration.getTheme());
		theme.setAccentColor("#FF0000");
		
		goToSettings();
	}
	
	private void setUpLabelError(String errorMessage) {
		String tagO = "<html><p>";
		String tagC = "</p></html>";
		
		lblError.setVisible(true);
		lblError.setForeground(Color.RED);
		lblError.setText(tagO + errorMessage + tagC);
		
	}
	private void removeLabelError() {
		lblError.setText("");
		lblError.setVisible(false);
	}
	private void clearNewProductForm(){
		removeLabelError();
		txtProductName.setText("");
		txtProductPrice.setText("");
		txtProductExpenses.setText("");
	}
	private void clearNewCategoryForm() {
		removeLabelError();
		txtNewCategory.setText("");
	}
	
	private boolean performCheckNewProduct() {
		
		if(comboBoxProductCategory.getSelectedIndex() == -1) {
			setUpLabelError("Potrebno je odabrati kategoriju novog proizvoda.");
			return false;
		}
		
		String category = comboBoxProductCategory.getSelectedItem().toString();
		String name = txtProductName.getText();
		Double price = null;
		Double expenses = null;
		
		
		if(Libary.isValidDouble(txtProductPrice.getText())) {
			price = Double.parseDouble(txtProductPrice.getText());
		}
		if(txtProductExpenses.getText().isEmpty()) {
			expenses = 0.0;
		}
		if(Libary.isValidDouble(txtProductExpenses.getText())) {
			expenses = Double.parseDouble(txtProductExpenses.getText());
		} else if (txtProductExpenses.getText().isBlank() || txtProductExpenses.getText().isEmpty()) {
			expenses = 0.0;	
			System.out.println("uspelo");
		} else {
			setUpLabelError("Uneti troškovi proizvoda nisu validni.");
			return false;
		}
		
		if(name == null || name.isEmpty()) {
			setUpLabelError("Uneto ime proizvoda ne sme biti prazno.");
			return false;
		} else if(name.length() < 2) {
			setUpLabelError("Uneto ime proizvoda ne sme imati manje od 2 karaktera.");
			return false;
		} else if(name.contains("-")) {
			setUpLabelError("Uneto ime proizvoda ne sme sadržati karakter \"-\"");
			return false;
		} else if(name.length() > 20) {
			setUpLabelError("Uneto ime proizvoda ne sme imati više od 20 karaktera.");
			return false;
		} else if(price == null) {
			setUpLabelError("Molimo da unesete cenu proizvoda.");
			return false;
		} else if(price < 0) {
			setUpLabelError("Uneta cena proizvoda ne sme biti negativna.");
			return false;
		} else if(price > 999999) {
			setUpLabelError("Uneta cena proizvoda prekoračuje gornju granicu. (0-999999)");
			return false;
		} else if (expenses < 0) {
			setUpLabelError("Uneti troškovi proizvodnje proizvoda ne smeju biti negativni.");
			return false;
		} else if(expenses > 999999) {
			setUpLabelError("Uneti  troškovi proizvodnje proizvoda prekorašuju gornju granicu. (0-999999)");
			return false;
		}
		
		Product newProduct = new Product(category, name, price, expenses);
		if(Libary.checkForProductDuplicates(newProduct)) {
			setUpLabelError("Proizvod već postoji.");
			return false;
		}
		
		return true;
	}
	private boolean performCheckNewCategory() {
		String categoryName = txtNewCategory.getText();
		
		if(categoryName == null || categoryName.isEmpty()) {
			setUpLabelError("Unesite validan naziv kategorije.");
			return false;
		} else if(categoryName.contains("-")){
			setUpLabelError("Naziv kategorije ne sme sadržati karakter \"-\"");
			return false;
		} else if(categoryName.length() > 15) {
			setUpLabelError("Naziv kategorije ne sme sadržati više od 15 karaktera.");
			return false;
		}
		
		if(Libary.checkForCategoryDuplicates(categoryName.toUpperCase())) {
			setUpLabelError("Naziv kategorije već postoji.");
			return false;
		}
		
		return true;
	}
	
	private int loadNumberOfTables() {
		return Libary.loadNumberOfTables();
	}
	private void setUpComboBoxCategories(JComboBox<String> comboBox) {
		comboBox.removeAllItems();
		Iterator<String> it = categories.iterator();
		while(it.hasNext()) {
			comboBox.addItem(it.next());
		}
	}
	
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
					JOptionPane.showMessageDialog(contentPane, "Korisnik koji nema administratorske privilegije nije u mogućnosti da pristupi podešavanjima.", "Nedovoljne privilegije", JOptionPane.WARNING_MESSAGE);
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
	
	// ----------------------------------------------------------------------

	public Settings(User activeUser, Configuration configuration, Theme theme) {
		this.activeUser = activeUser;
		this.configuration = configuration;
		this.theme = theme;
		
		setResizable(false);
		setTitle("CafeMaster - Podešavanja");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 1000, 720);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		contentPane.add(getPanelN(), BorderLayout.NORTH);
		contentPane.add(getPanelC(), BorderLayout.CENTER);
		setLocationRelativeTo(null);
	}
	private JPanel getPanelN() {
		if (panelN == null) {
			panelN = new JPanel();
			panelN.setPreferredSize(new Dimension(30, 40));
			panelN.setFont(new Font("Segoe UI", Font.PLAIN, 10));
			panelN.setLayout(null);
			panelN.add(getList());
		}
		return panelN;
	}
	private JList getList() {
		if (list == null) {
			list = new JList();
			list.setFont(new Font("Segoe UI", Font.PLAIN, 18));
			list.setBounds(0, 10, 976, 30);
			list.setPreferredSize(new Dimension(900, 30));
			list.setLayoutOrientation(JList.VERTICAL_WRAP);
			list.setFocusable(false);
			list.addListSelectionListener(new ListSelectionListener() {
				public void valueChanged(ListSelectionEvent e) {
					switch(getList().getSelectedIndex()) {
					case 0:
						getPanelS_general().setVisible(true);
						getPanelS_products().setVisible(false);
						getPanelS_users().setVisible(false);
						break;
					case 1:
						getPanelS_general().setVisible(false);
						getPanelS_products().setVisible(true);
						getPanelS_users().setVisible(false);
						break;
					case 2:
						getPanelS_general().setVisible(false);
						getPanelS_products().setVisible(false);
						getPanelS_users().setVisible(true);
						break;
					}
				}
			});
			list.setVisibleRowCount(1);
			list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	
			list.setModel(new AbstractListModel() {
				String[] values = new String[] {"OPŠTA PODEŠAVANJA" ,"PODEŠAVANJA PROIZVODA", "PODEŠAVANJA KORISNIČKIH PROFILA"};
				public int getSize() {
					return values.length;
				}
				public Object getElementAt(int index) {
					return values[index];
				}
			});
			list.setSelectedIndex(0);
		}
		return list;
	}
	private JPanel getPanelC() {
		if (panelC == null) {
			panelC = new JPanel();
			panelC.setLayout(new CardLayout(0, 0));
			panelC.add(getPanelS_general(), "name_482836985326400");
			panelC.add(getPanelS_products(), "name_482837012038600");
			panelC.add(getPanelS_users(), "name_482837040455600");
		}
		return panelC;
	}
	private JPanel getPanelS_general() {
		if (panelS_general == null) {
			panelS_general = new JPanel();
			panelS_general.setLayout(null);
			
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setBounds(24, 254, 515, 243);
			panelS_general.add(fileChooser);
			
			JColorChooser colorChooser = new JColorChooser();
			colorChooser.setPreviewPanel(new JPanel());
			colorChooser.setBounds(551, 247, 389, 238);
			
			AbstractColorChooserPanel[] oldPanels = colorChooser.getChooserPanels();
		    for (int i = 0; i < oldPanels.length; i++) {
		      String clsName = oldPanels[i].getClass().getName();
		      if (clsName.equals("javax.swing.colorchooser.ColorChooserPanel")) {
		    	  colorChooser.removeChooserPanel(oldPanels[i]);
		      } else if (clsName.equals("javax.swing.colorchooser.DefaultRGBChooserPanel")) {
		    	  colorChooser.removeChooserPanel(oldPanels[i]);
		      } else if (clsName.equals("javax.swing.colorchooser.DefaultHSBChooserPanel")) {
		    	  colorChooser.removeChooserPanel(oldPanels[i]);
		      }
		    }
		    colorChooser.getSelectionModel().addChangeListener(new ChangeListener() {
		    	public void stateChanged(ChangeEvent e) {
		    		String colorHex = ColorToHex(colorChooser.getColor());
		    		System.out.println("Changing accent color to: " + colorHex + " for theme \"" + theme.getThemeName() + "\".");
		    		theme.setAccentColor(colorHex);
		    		theme.setActiveTheme(theme.getThemeName());
		    		
		    		System.out.println("\n\n- WHILE RUNNING -\nthemeName: " + theme.getThemeName() + "\naccentColor: " + theme.getAccentColor() + "\n\n");
		    		
		    		EventQueue.invokeLater(new Runnable() {
		    			public void run() {
		    					try {
		    						dispose();
		    						Settings frame = new Settings(activeUser, configuration, theme);
		    						frame.setVisible(true);
		    					} catch (Exception e) {
		    						e.printStackTrace();
		    					}
		    			}
		    		});
		    		
		    	}
		    });
		    panelS_general.add(colorChooser);
			
			slider.setValue(loadNumberOfTables());
			slider.addChangeListener(new ChangeListener() {
				public void stateChanged(ChangeEvent e) {
					int numTables = slider.getValue();
                    changeNumOfTables(numTables);
				}
			});
			slider.setSnapToTicks(true);
			slider.setPaintTicks(true);
			slider.setPaintLabels(true);
			slider.setMinorTickSpacing(1);
			slider.setMinimum(1);
			slider.setMaximum(10);
			slider.setMajorTickSpacing(1);
			slider.setFont(new Font("Segoe UI", Font.PLAIN, 15));
			slider.setBounds(193, 32, 281, 56);
			panelS_general.add(slider);
			
			JLabel lblNumOfTables = new JLabel("BROJ STOLOVA");
			lblNumOfTables.setFont(new Font("Segoe UI", Font.BOLD, 20));
			lblNumOfTables.setBounds(24, 32, 180, 27);
			panelS_general.add(lblNumOfTables);
			
			JSeparator separator = new JSeparator();
			separator.setMinimumSize(new Dimension(50, 50));
			separator.setBounds(10, 118, 956, 22);
			panelS_general.add(separator);
			
			JLabel lblTheme = new JLabel("TEMA");
			lblTheme.setFont(new Font("Segoe UI", Font.BOLD, 20));
			lblTheme.setBounds(551, 148, 124, 27);
			panelS_general.add(lblTheme);
			
			JLabel lblDirectory = new JLabel("DIREKTORIJUM IZVEŠTAJA");
			lblDirectory.setFont(new Font("Segoe UI", Font.BOLD, 20));
			lblDirectory.setBounds(24, 210, 502, 27);
			panelS_general.add(lblDirectory);
			
			btnTheme.setHorizontalAlignment(SwingConstants.LEFT);
			btnTheme.setText(" ");
			btnTheme.setIcon(new ImageIcon());
			rotateIcon(btnTheme, false); // Rotates image icon to right.
			switch(theme.getThemeName()) {
				default:
				case "lightTheme":
					btnTheme.setText("Svetla tema");
					btnTheme.setIcon(new ImageIcon("src/lib/img/lightTheme.png"));
					break;
				case "darkTheme":
					btnTheme.setText("Tamna tema");
					btnTheme.setIcon(new ImageIcon("src/lib/img/darkTheme.png"));
					break;
			}
			btnTheme.setFont(new Font("Segoe UI", Font.PLAIN, 18));
			btnTheme.setBounds(659, 146, 281, 33);
			btnTheme.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					Icon iconTheme;
					if(theme.getThemeName().equalsIgnoreCase("lightTheme")) {
						theme.setActiveTheme("darkTheme");
						
					} else {
						theme.setActiveTheme("lightTheme");
					}
					
					theme.getAccentColor();
					goToSettings();
					
				}
			});
			panelS_general.add(btnTheme);
			
			JLabel lblColor = new JLabel("AKCENT BOJA");
			lblColor.setFont(new Font("Segoe UI", Font.BOLD, 20));
			lblColor.setBounds(551, 210, 180, 27);
			panelS_general.add(lblColor);
			
//			Icon iconConfirm = rescaleImage(new File("src/lib/img/confirm.png"), 30, 30);
			Icon iconConfirm = new ImageIcon("src/lib/img/confirm.png");
			JButton btnConfirmDirectory = new JButton("POTVRDI DIREKTORIJUM", iconConfirm);
			btnConfirmDirectory.setFont(new Font("Segoe UI", Font.PLAIN, 18));
			btnConfirmDirectory.setIcon(iconConfirm);
			btnConfirmDirectory.setBounds(24, 507, 502, 45);
			panelS_general.add(btnConfirmDirectory);
			
			Icon iconDefault = new ImageIcon("src/lib/img/default.png");
			JButton btnDefault = new JButton("VRATI NA PODRAZUMEVANO", iconDefault);
			btnDefault.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					int response = JOptionPane.showConfirmDialog(contentPane, "Da li ste sigurni da želite da restartujete podešavanja na podrazumevana?\n"
							+ "Ovo će rezultovati brisanju svih podešavanja i povratku na početnu šemu.", "Potvrda izlaska", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
					
					if(response == JOptionPane.YES_OPTION) {
						trialSettings();
						updateSettingsMenu();
					}
				}
			});
			btnDefault.setFont(new Font("Segoe UI", Font.PLAIN, 18));
			btnDefault.setBounds(551, 507, 389, 45);
			panelS_general.add(btnDefault);
			
			JLabel lblLanguage = new JLabel("JEZIK PROGRAMA");
			lblLanguage.setFont(new Font("Segoe UI", Font.BOLD, 20));
			lblLanguage.setBounds(24, 148, 180, 27);
			panelS_general.add(lblLanguage);
			
			Configuration tempObj = new Configuration(Libary.loadAllProperties()); // Temporary
			JComboBox comboBoxLanguage = new JComboBox();
			comboBoxLanguage.setFont(new Font("Segoe UI", Font.PLAIN, 18));
			comboBoxLanguage.addItem(tempObj.getLanguage());
			comboBoxLanguage.setBounds(211, 146, 314, 33);
			panelS_general.add(comboBoxLanguage);
			
			Icon iconBack = new ImageIcon("src/lib/img/back.png");
			JButton btnBack = new JButton("VRATI SE NAZAD", iconBack);
			btnBack.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					goToMainMenu();
				}
			});
			btnBack.setFont(new Font("Segoe UI", Font.PLAIN, 18));
			btnBack.setBounds(29, 578, 916, 45);
			panelS_general.add(btnBack);
			
			JSeparator separator_1 = new JSeparator();
			separator_1.setMinimumSize(new Dimension(50, 50));
			separator_1.setBounds(24, 562, 916, 22);
			panelS_general.add(separator_1);
		}
		return panelS_general;
	}

	private JPanel getPanelS_products() {
		if (panelS_products == null) {
			panelS_products = new JPanel();
			panelS_products.setLayout(null);
			
			JPanel panelS_productsN = new JPanel();
			panelS_productsN.setBounds(0, 0, 976, 300);
			panelS_productsN.setPreferredSize(new Dimension(0, 300));
			panelS_products.add(panelS_productsN);
			panelS_productsN.setLayout(new BorderLayout(0, 0));
			
			JSplitPane splitPane = new JSplitPane();
			splitPane.setBorder(new LineBorder(Color.GRAY, 4));
			panelS_productsN.add(splitPane);
			
			
			listCategories = new JList();
			listCategories.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			listCategories.setFont(new Font("Segoe UI", Font.PLAIN, 17));
			
			JScrollPane scrollPaneCategories = new JScrollPane();
			scrollPaneCategories.setPreferredSize(new Dimension(300, 2));
			scrollPaneCategories.setViewportView(listCategories);
			listCategories.setLayoutOrientation(JList.VERTICAL);
			
			splitPane.setLeftComponent(scrollPaneCategories);
			refreshCategoriesList();
			listCategories.addListSelectionListener(new ListSelectionListener() {
				public void valueChanged(ListSelectionEvent e) {
					categorySelected = true;
					refreshProductsList();
				}
			});
						
			listProducts = new JList();
			listProducts.addListSelectionListener(new ListSelectionListener() {
				public void valueChanged(ListSelectionEvent e) {
					productSelected = true;
				}
			});
			listProducts.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			listProducts.setFont(new Font("Segoe UI", Font.PLAIN, 17));
			
			JScrollPane scrollPaneProducts = new JScrollPane();
			scrollPaneProducts.setViewportView(listProducts);
			listProducts.setLayoutOrientation(JList.VERTICAL);
			splitPane.setRightComponent(scrollPaneProducts);
			refreshProductsList();
			
			JPanel panelS_productsN_menu = new JPanel();
			panelS_productsN_menu.setPreferredSize(new Dimension(150, 0));
			panelS_productsN_menu.setFont(new Font("Segoe UI", Font.PLAIN, 18));
			panelS_productsN.add(panelS_productsN_menu, BorderLayout.EAST);
			panelS_productsN_menu.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
			
			JLabel lblProducts = new JLabel("PROIZVODI");
			lblProducts.setHorizontalAlignment(SwingConstants.CENTER);
			lblProducts.setFont(new Font("Segoe UI", Font.BOLD, 18));
			panelS_productsN_menu.add(lblProducts);
			
			JButton btnEditProduct = new JButton("IZMENI");
			btnEditProduct.setMaximumSize(new Dimension(69, 23));
			btnEditProduct.setMinimumSize(new Dimension(69, 23));
			btnEditProduct.setPreferredSize(new Dimension(130, 50));
			btnEditProduct.setFont(new Font("Segoe UI", Font.PLAIN, 18));
			panelS_productsN_menu.add(btnEditProduct);
			
			JButton btnDeleteProduct = new JButton("IZBRI\u0160I");
			btnDeleteProduct.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if(productSelected) {
						String selectedProduct = listProducts.getSelectedValue().toString();
						
						Libary.removeProduct(selectedProduct, categories, products);
						Libary.updateInventory(categories, products);
						
						refreshProductsList();
						productSelected = false;
						selectedProduct = "";
					} else {
						JOptionPane.showMessageDialog(null, "Molimo Vas da izaberete proizvod za brisanje.", "Greška!", JOptionPane.WARNING_MESSAGE);
					}
				}
			});
			btnDeleteProduct.setPreferredSize(new Dimension(130, 50));
			btnDeleteProduct.setFont(new Font("Segoe UI", Font.PLAIN, 18));
			panelS_productsN_menu.add(btnDeleteProduct);
			
			JLabel lblBlank = new JLabel("");
			lblBlank.setPreferredSize(new Dimension(120, 20));
			panelS_productsN_menu.add(lblBlank);
			
			JSeparator separator = new JSeparator();
			separator.setPreferredSize(new Dimension(120, 2));
			panelS_productsN_menu.add(separator);
			
			JLabel lblKategorije = new JLabel("KATEGORIJE");
			lblKategorije.setHorizontalAlignment(SwingConstants.CENTER);
			lblKategorije.setFont(new Font("Segoe UI", Font.BOLD, 18));
			panelS_productsN_menu.add(lblKategorije);
			
			JButton btnDeleteCategory = new JButton("IZBRI\u0160I");
			btnDeleteCategory.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if(categorySelected) {
						String selectedCategory = listCategories.getSelectedValue().toString();
						System.out.println(selectedCategory);
						productsOfCategory = Libary.loadAllProductsOfCategory(selectedCategory, categories, products);
						
						if(!productsOfCategory.isEmpty()) {
							int option = JOptionPane.showConfirmDialog(null, "Da li ste sigurni da šelite da obrišete nepraznu kategoriju?\nOvim se brišu svi proizvodi unutar kategorije.", "Brisanje kategorije", JOptionPane.WARNING_MESSAGE);
							if(option != JOptionPane.YES_OPTION) return;
						}
						
						Libary.removeCategory(selectedCategory, categories, products);;
						Libary.updateInventory(categories, products);
						
						refreshCategoriesList();
						refreshProductsList();
						setUpComboBoxCategories(comboBoxProductCategory);
						categorySelected = false;
						productSelected = false;
						
					} else {
						JOptionPane.showMessageDialog(null, "Molimo Vas da izaberete kategoriju za brisanje.", "Greška!", JOptionPane.WARNING_MESSAGE);
					}
				}
			});
			btnDeleteCategory.setPreferredSize(new Dimension(130, 50));
			btnDeleteCategory.setFont(new Font("Segoe UI", Font.PLAIN, 18));
			panelS_productsN_menu.add(btnDeleteCategory);
			
			JPanel panelNewProduct = new JPanel();
			panelNewProduct.setBounds(10, 427, 954, 144);
			panelS_products.add(panelNewProduct);
			Border borderNewProduct = createTitledBorder("DODAVANJE NOVOG PROIZVODA");
			panelNewProduct.setBorder(borderNewProduct);
			panelNewProduct.setLayout(null);
			
			comboBoxProductCategory = new JComboBox();
			comboBoxProductCategory.setBounds(10, 33, 265, 37);
			panelNewProduct.add(comboBoxProductCategory);
			comboBoxProductCategory.setFont(new Font("Segoe UI", Font.PLAIN, 17));
			setUpComboBoxCategories(comboBoxProductCategory);
			
			txtProductName = new PlaceholderTextField ();
			txtProductName.setBounds(285, 33, 327, 39);
			panelNewProduct.add(txtProductName);
			txtProductName.addKeyListener(new KeyAdapter() {
				public void keyPressed(KeyEvent e) {
					removeLabelError();
				}
			});
			txtProductName.setFont(new Font("Segoe UI", Font.PLAIN, 17));
			txtProductName.setColumns(10);
			txtProductName.setPlaceholder("ime proizvoda");
			
			txtProductPrice = new PlaceholderTextField ();
			txtProductPrice.setBounds(622, 33, 156, 37);
			panelNewProduct.add(txtProductPrice);
			txtProductPrice.addKeyListener(new KeyAdapter() {
				public void keyPressed(KeyEvent e) {
					removeLabelError();
				}
			});
			txtProductPrice.setFont(new Font("Segoe UI", Font.PLAIN, 17));
			txtProductPrice.setColumns(10);
			txtProductPrice.setPlaceholder("cena proizvoda");
			
			txtProductExpenses = new PlaceholderTextField ();
			txtProductExpenses.setBounds(788, 33, 156, 37);
			panelNewProduct.add(txtProductExpenses);
			txtProductExpenses.addKeyListener(new KeyAdapter() {
				public void keyPressed(KeyEvent e) {
					removeLabelError();
				}
			});
			txtProductExpenses.setFont(new Font("Segoe UI", Font.PLAIN, 17));
			txtProductExpenses.setColumns(10);
			txtProductExpenses.setPlaceholder("troškovi");
			
			JButton btnSaveProduct = new JButton("SAČUVAJ PROIZVOD");
			btnSaveProduct.setBounds(10, 81, 934, 48);
			panelNewProduct.add(btnSaveProduct);
			btnSaveProduct.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if(performCheckNewProduct() == true) {
						String category = comboBoxProductCategory.getSelectedItem().toString();
						String name = txtProductName.getText();
						Double price = Double.parseDouble(txtProductPrice.getText());
						Double expenses;
						if(txtProductExpenses.getText().isEmpty()) {
							expenses = 0.0;
						} else {
							expenses = Double.parseDouble(txtProductExpenses.getText());
						}
						
						Product newProduct = new Product(category, name, price, expenses);
						products.add(newProduct);
//						Libary.insertProduct(newProduct);
						Libary.updateInventory(categories, products);
						
						refreshProductsList();
						clearNewProductForm();
					}				
				}
			});
			
			Icon iconSave = new ImageIcon("src/lib/img/save.png");
			btnSaveProduct.setFont(new Font("Segoe UI", Font.PLAIN, 18));
			btnSaveProduct.setIcon(iconSave);
			
			
			
			Icon iconBack = new ImageIcon("src/lib/img/back.png");
			JButton btnBack = new JButton("VRATI SE NAZAD", null);
			btnBack.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					goToMainMenu();
				}
			});
			btnBack.setFont(new Font("Segoe UI", Font.PLAIN, 18));
			btnBack.setBounds(20, 578, 933, 45);
			btnBack.setIcon(iconBack);
			panelS_products.add(btnBack);
			
			
			lblError = new JLabel();
			lblError.setHorizontalAlignment(SwingConstants.CENTER);
			lblError.setVisible(false);
			lblError.setBorder(UIManager.getBorder("PopupMenu.border"));
			lblError.setFont(new Font("Segoe UI", Font.ITALIC, 18));
			lblError.setBounds(568, 311, 396, 104);
			panelS_products.add(lblError);
			
			JPanel panelNewCategory = new JPanel();
			panelNewCategory.setBounds(10, 311, 548, 104);
			panelS_products.add(panelNewCategory);
			Border borderNewCategory = createTitledBorder("DODAVANJE NOVE KATEGORIJE");
			panelNewCategory.setBorder(borderNewCategory);
						panelNewCategory.setLayout(null);
						
						txtNewCategory = new PlaceholderTextField ();
						txtNewCategory.setBounds(10, 41, 260, 37);
						txtNewCategory.setPreferredSize(new Dimension(7, 15));
						txtNewCategory.setHorizontalAlignment(SwingConstants.LEFT);
						panelNewCategory.add(txtNewCategory);
						txtNewCategory.addKeyListener(new KeyAdapter() {
							@Override
							public void keyPressed(KeyEvent e) {
								removeLabelError();
							}
						});
						txtNewCategory.setFont(new Font("Segoe UI", Font.PLAIN, 17));
						txtNewCategory.setColumns(10);
						txtNewCategory.setPlaceholder("naziv kategorije");
						JButton btnSaveCategory = new JButton("SAČUVAJ");
						btnSaveCategory.setMinimumSize(new Dimension(69, 53));
						btnSaveCategory.setPreferredSize(new Dimension(130, 50));
						btnSaveCategory.setBounds(280, 41, 258, 37);
						panelNewCategory.add(btnSaveCategory);
						btnSaveCategory.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								if(performCheckNewCategory() == true) {
									String categoryName = txtNewCategory.getText().toUpperCase();
									categories = Libary.insertCategory(categoryName, categories); // categories.add() but done alphabetically
									Libary.updateInventory(categories, products);
									
									refreshCategoriesList();
									clearNewCategoryForm();
									setUpComboBoxCategories(comboBoxProductCategory);
								}
							}
						});
						btnSaveCategory.setFont(new Font("Segoe UI", Font.PLAIN, 18));
						btnSaveCategory.setIcon(iconSave);
			
		}
		return panelS_products;
	}
	
	private void refreshProductsList() {
		DefaultListModel listModel=new DefaultListModel();
		
		if(listCategories.getSelectedIndex() != -1) {
			String selectedCategory = listCategories.getSelectedValue().toString();
			LinkedList<Product> productsOfCategory = Libary.loadAllProductsOfCategory(selectedCategory, categories, products);
			listModel.addAll(productsOfCategory);
		}
		
		listProducts.setModel(listModel);
	}
	private void refreshCategoriesList() {
		DefaultListModel<String> listModel = new DefaultListModel<>();
		listModel.addAll(categories);
		listCategories.setModel(listModel);
	}
	private JPanel getPanelS_users() {
		if (panelS_users == null) {
			panelS_users = new JPanel();
			panelS_users.setLayout(null);
			
			JLabel lblNewLabel = new JLabel("test test TEST");
			lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 17));
			lblNewLabel.setBounds(109, 57, 338, 221);
			panelS_users.add(lblNewLabel);
			
			Border thisBorder = BorderFactory.createTitledBorder("Easy");
			
			panelS_users.add(getPanel());
			getPanel().setBorder(thisBorder);
		}
		return panelS_users;
	}
	private JPanel getPanel() {
		if (panel == null) {
			panel = new JPanel();
			panel.setBounds(227, 305, 490, 261);
			panel.add(getBtnNewButton_1());
			panel.add(getBtnNewButton());
		}
		return panel;
	}
	private JButton getBtnNewButton() {
		if (btnNewButton == null) {
			btnNewButton = new JButton("New button");
		}
		return btnNewButton;
	}
	private JButton getBtnNewButton_1() {
		if (btnNewButton_1 == null) {
			btnNewButton_1 = new JButton("New button");
		}
		return btnNewButton_1;
	}
}
