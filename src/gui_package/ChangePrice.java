package gui_package;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Dimension;
import java.awt.Font;
import javax.swing.JTextField;
import java.awt.Color;
import javax.swing.JSeparator;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.awt.event.ActionEvent;
import javax.swing.event.CaretListener;

import org.apache.xmlbeans.impl.common.SystemCache;

import gui_package.Inventory.HelperClass;

import javax.swing.event.CaretEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowStateListener;
import java.awt.event.WindowEvent;

public class ChangePrice extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JLabel lblUnesiteNovuCenu;
	private JTextField txtNovaCena;
	private JLabel lblTrenutnaCenaProizvoda;
	private JTextField txtStaraCena;
	private JLabel lblPromenaCene;
	private JTextField txtPromena;
	private JSeparator separator;
	private JLabel lblErrorGornji;
	private JLabel lblErrorDonji;

	private boolean validInput = false;

	public double returnPrice() {
		return Double.parseDouble(txtNovaCena.getText());
	}
	
	public ChangePrice(HelperClass helperClass) {
		addWindowListener(new WindowAdapter() 
		{
		  public void windowClosed(WindowEvent e)
		  {
		    helperClass.newPrice = helperClass.oldPrice;
		    System.out.println("PROZOR ZATOVREN");
		    System.out.println("NOVA CENA: " + helperClass.newPrice);
		  }
		});
		
		setTitle("Promena cene proizvoda");
		setResizable(false);
		setBounds(100, 100, 450, 272);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		contentPanel.add(getLblUnesiteNovuCenu());
		contentPanel.add(getTxtNovaCena());
		contentPanel.add(getLblTrenutnaCenaProizvoda());
		contentPanel.add(getTxtStaraCena());
		contentPanel.add(getLblPromenaCene());
		contentPanel.add(getTxtPromena());
		contentPanel.add(getSeparator());
		txtStaraCena.setText(Double.toString(helperClass.oldPrice));
		contentPanel.add(getLblErrorGornji());
		contentPanel.add(getLblErrorDonji());
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.setFont(new Font("Arial", Font.PLAIN, 15));
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						if(validInput == true) {
							helperClass.newPrice = Double.parseDouble(txtNovaCena.getText());
							returnPrice();
							dispose();
						} else {
							JOptionPane.showMessageDialog(contentPanel, "Molimo unesite ispravnu cenu proizvoda!", "Promena cene", JOptionPane.WARNING_MESSAGE);
						}
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Odbaci");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						helperClass.newPrice = helperClass.oldPrice;
						dispose();
					}
				});
				cancelButton.setFont(new Font("Arial", Font.PLAIN, 15));
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}
	private JLabel getLblUnesiteNovuCenu() {
		if (lblUnesiteNovuCenu == null) {
			lblUnesiteNovuCenu = new JLabel("Unesite novu cenu proizvoda:");
			lblUnesiteNovuCenu.setFont(new Font("Arial", Font.PLAIN, 14));
			lblUnesiteNovuCenu.setMaximumSize(new Dimension(500, 500));
			lblUnesiteNovuCenu.setMinimumSize(new Dimension(150, 200));
			lblUnesiteNovuCenu.setPreferredSize(new Dimension(200, 20));
			lblUnesiteNovuCenu.setBounds(22, 30, 200, 25);
		}
		return lblUnesiteNovuCenu;
	}
	private JTextField getTxtNovaCena() {
		if (txtNovaCena == null) {
			txtNovaCena = new JTextField();
			txtNovaCena.addKeyListener(new KeyAdapter() {
				public void keyReleased(KeyEvent arg0) {
					try {
						txtNovaCena.setBackground(new Color(255,255,255));
						Double newPrice = Double.parseDouble(txtNovaCena.getText());
						
						if(newPrice < 0) {
							lblErrorGornji.setText("<HTML>Uneta cena ne moze biti negativna!</HTML>");
							validInput = false;
							return;
						}
						
						Double oldPrice = Double.parseDouble(txtStaraCena.getText());
						Double change = (newPrice/oldPrice)*100 - 100;
						if(change > 0) {
							txtPromena.setBackground(new Color(207, 253, 197));
						} else if(change < 0) {
							txtPromena.setBackground(new Color(253, 198, 197));
						} else {
							txtPromena.setBackground(new Color(230, 254, 186));
						}
						DecimalFormat df = new DecimalFormat("###.##");
						txtPromena.setText(df.format(change).toString());
						lblErrorGornji.setText("");
						lblErrorDonji.setText("");
						validInput = true;
					} catch (NumberFormatException e1) {
						validInput = false;
						lblErrorGornji.setText("<HTML>Uneta nova cena NIJE validna!</HTML>");
					}
				}
			});
			
			
			txtNovaCena.setBounds(223, 32, 112, 23);
			txtNovaCena.setColumns(10);
		}
		return txtNovaCena;
	}
	private JLabel getLblTrenutnaCenaProizvoda() {
		if (lblTrenutnaCenaProizvoda == null) {
			lblTrenutnaCenaProizvoda = new JLabel("Trenutna cena proizvoda: ");
			lblTrenutnaCenaProizvoda.setFont(new Font("Arial", Font.PLAIN, 14));
			lblTrenutnaCenaProizvoda.setBounds(22, 118, 200, 25);
		}
		return lblTrenutnaCenaProizvoda;
	}
	private JTextField getTxtStaraCena() {
		if (txtStaraCena == null) {
			txtStaraCena = new JTextField();
			txtStaraCena.setEditable(false);
			txtStaraCena.setForeground(Color.BLACK);
			txtStaraCena.setBounds(207, 119, 112, 25);
			txtStaraCena.setColumns(10);
		}
		return txtStaraCena;
	}
	private JLabel getLblPromenaCene() {
		if (lblPromenaCene == null) {
			lblPromenaCene = new JLabel("Promena cene (%):");
			lblPromenaCene.setFont(new Font("Arial", Font.PLAIN, 14));
			lblPromenaCene.setBounds(22, 154, 165, 19);
		}
		return lblPromenaCene;
	}
	private JTextField getTxtPromena() {
		if (txtPromena == null) {
			txtPromena = new JTextField();
			txtPromena.addKeyListener(new KeyAdapter() {
				public void keyReleased(KeyEvent e) {
					try {
						txtPromena.setBackground(new Color(255,255,255));
						Double promena = Double.parseDouble(txtPromena.getText());						
						Double staraCena = Double.parseDouble(txtStaraCena.getText());
						Double novaCena = staraCena + (promena/100) * staraCena;
						
						if(novaCena > staraCena) {
							txtNovaCena.setBackground(new Color(207, 253, 197));
						} else if(novaCena < staraCena) {
							txtNovaCena.setBackground(new Color(253, 198, 197));
							if(novaCena < 0) {
								validInput = false;
								lblErrorDonji.setText("<HTML>Nije moguće toliko sniziti cenu.\nCena je negativna.</HTML>");
								return;
							}
						} else {
							txtNovaCena.setBackground(new Color(230, 254, 186));
						}
						
						
						txtNovaCena.setText(Double.toString(novaCena));
						lblErrorDonji.setText("");
						lblErrorGornji.setText("");
						validInput = true;
					} catch (NumberFormatException e1) {
						validInput = false;
						lblErrorDonji.setText("<HTML>Uneta promena u ceni NIJE validna!</HTML>");
					}
				}
			});
						
			
			txtPromena.setBounds(207, 154, 112, 23);
			txtPromena.setColumns(10);
		}
		return txtPromena;
	}
	private JSeparator getSeparator() {
		if (separator == null) {
			separator = new JSeparator();
			separator.setBounds(10, 87, 424, 9);
		}
		return separator;
	}
	private JLabel getLblErrorGornji() {
		if (lblErrorGornji == null) {
			lblErrorGornji = new JLabel("");
			lblErrorGornji.setForeground(Color.RED);
			lblErrorGornji.setFont(new Font("Arial", Font.BOLD, 12));
			lblErrorGornji.setBounds(345, 11, 89, 54);
		}
		return lblErrorGornji;
	}
	private JLabel getLblErrorDonji() {
		if (lblErrorDonji == null) {
			lblErrorDonji = new JLabel("");
			lblErrorDonji.setForeground(Color.RED);
			lblErrorDonji.setFont(new Font("Arial", Font.BOLD, 12));
			lblErrorDonji.setBounds(329, 118, 105, 54);
		}
		return lblErrorDonji;
	}
}
