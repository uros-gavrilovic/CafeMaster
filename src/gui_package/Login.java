package gui_package;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import backend_package.Configuration;
import backend_package.Theme;
import backend_package.User;
import lib.Libary;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Login extends JDialog {
	private JPanel contentPane;
	private JPanel panel;
	private JTextField txtUsername;
	private JLabel lblUsername;
	private JLabel lblPassword;
	private JPasswordField txtPassword;
	private JLabel lblIcon1;
	private JButton btnPrijava;
	private JLabel lblIcon3;
	private JLabel lblIcon2;
	
	private LinkedList<User> users;
	private User activeUser = null;
	
	// ---------- CUSTOM METODE ---------------------------------------------
	
	void goToStart() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					App frame = new App(activeUser,  new Configuration(Libary.loadAllProperties()), new Theme());
					frame.setVisible(true);
					dispose();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	private LinkedList<User> loadAllUsers(){
		try {
			LinkedList<User> users = new LinkedList<>();
			BufferedReader br = new BufferedReader(new FileReader(Libary.getUsersPath()));
			String userString;
			String[] userParts;
			User user;
			
			while((userString=br.readLine()) != null) {
				userParts = splitUser(userString);
				user = new User(Integer.parseInt(userParts[0]), userParts[1], userParts[2], Boolean.parseBoolean(userParts[3]), Double.parseDouble(userParts[4]));
				users.add(user);
			}
			
			br.close();
			return users;
		} catch (FileNotFoundException e) {
			Libary.errorFileNotFoundException("users.txt", Libary.getMethodName());
		} catch (IOException e) {
			Libary.errorIOException("users.txt", Libary.getMethodName());
		}
		return null;
	}
	
	private boolean checkValidity(String username, String password) {
		Iterator<User> it = users.iterator();
		User userIterator;
		
		boolean userExists = false;
		while(it.hasNext() == true) {
			userIterator = it.next();
			if(userIterator.getUsername().matches(username) && userIterator.getPassword().matches(password)) {
				userExists = true;
				activeUser = userIterator;
			}
		}
		
		
		if(username.isEmpty()) {			
			lblIcon2.setVisible(true);
			JOptionPane.showMessageDialog(contentPane, "Molimo unesite korisničko ime.", "Nevalidni korisnički parametri", JOptionPane.WARNING_MESSAGE);
			return false;
		} else if(password.isEmpty()) {
			lblIcon3.setVisible(true);
			JOptionPane.showMessageDialog(contentPane, "Molimo unesite lozinku.", "Nevalidni korisnički parametri", JOptionPane.WARNING_MESSAGE);
			return false;
		} else if(password.length() < 3) {
			lblIcon3.setVisible(true);
			JOptionPane.showMessageDialog(contentPane, "Korisnička lozinka mora da sadrži bar 3 karaktera!", "Nevalidni korisnički parametri", JOptionPane.WARNING_MESSAGE);
			return false;
		} else if(username.contains("-")) {
			JOptionPane.showMessageDialog(contentPane, "Korisničko ime ne sme da sadrži karakter '-'!", "Nevalidni korisnički parametri", JOptionPane.WARNING_MESSAGE);
			return false;
		} else if(password.contains("-")) {
			JOptionPane.showMessageDialog(contentPane, "Korisnička lozinka ne sme da sadrži karakter '-'!", "Nevalidni korisnički parametri", JOptionPane.WARNING_MESSAGE);
			return false;
		} else if(userExists == false) {
			JOptionPane.showMessageDialog(contentPane, "Korisničko ime ili lozinka nisu ispravni!", "Nevalidni korisnički parametri", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		
		lblIcon2.setVisible(false);
		lblIcon3.setVisible(false);
		return true;
	}
	private String[] splitUser(String procitanKorisnik) {
		String[] deloviKorisnika = procitanKorisnik.split("-");
		return deloviKorisnika;
	}
	
	public User returnActiveUser() {
		return activeUser;
	}
	// ----------------------------------------------------------------------
	
//	public static void main(String[] args) {
//		EventQueue.invokeLater(new Runnable() {
//			public void run() {
//				try {
//					Login frame = new Login(ulogovanKorisnik);
//					frame.setVisible(true);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		});
//	}

	// ----------------------------------------------------------------------
	
	public Login() {
		users = loadAllUsers();
		
		setTitle("Prijavljivanje");
		setMinimumSize(new Dimension(150, 150));
		setResizable(false);
		setPreferredSize(new Dimension(150, 150));
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 400, 341);
		setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		contentPane.add(getPanel(), BorderLayout.CENTER);
	}

	private JPanel getPanel() {
		if (panel == null) {
			panel = new JPanel();
			panel.setLayout(null);
			panel.add(getTxtUsername());
			panel.add(getLblUsername());
			panel.add(getLblPassword());
			panel.add(getTxtPassword());
			panel.add(getLblIcon1());
			panel.add(getBtnPrijava());
			panel.add(getLblIcon3());
			panel.add(getLblIcon2());
		}
		return panel;
	}
	private JTextField getTxtUsername() {
		if (txtUsername == null) {
			txtUsername = new JTextField();
			txtUsername.setBounds(194, 170, 118, 20);
			txtUsername.setColumns(10);
		}
		return txtUsername;
	}
	private JLabel getLblUsername() {
		if (lblUsername == null) {
			lblUsername = new JLabel("Korisni\u010Dno ime:");
			lblUsername.setFont(new Font("Segoe UI", Font.PLAIN, 19));
			lblUsername.setBounds(43, 163, 141, 29);
		}
		return lblUsername;
	}
	private JLabel getLblPassword() {
		if (lblPassword == null) {
			lblPassword = new JLabel("\u0160ifra:");
			lblPassword.setHorizontalAlignment(SwingConstants.CENTER);
			lblPassword.setFont(new Font("Segoe UI", Font.PLAIN, 19));
			lblPassword.setBounds(43, 196, 126, 29);
		}
		return lblPassword;
	}
	private JPasswordField getTxtPassword() {
		if (txtPassword == null) {
			txtPassword = new JPasswordField();
			txtPassword.addKeyListener(new KeyAdapter() {
				public void keyPressed(KeyEvent e) {
					if (e.getKeyCode()==KeyEvent.VK_ENTER) {
						 users = loadAllUsers();
							lblIcon2.setVisible(false);
							lblIcon3.setVisible(false);
							String username = txtUsername.getText();
							String password = String.valueOf(txtPassword.getPassword());
							
							if(checkValidity(username, password) == true) {
								dispose();
							}
					 }
				}
			});
			txtPassword.setBounds(194, 203, 118, 20);
		}
		return txtPassword;
	}
	private JLabel getLblIcon1() {
		if (lblIcon1 == null) {
			lblIcon1 = new JLabel("");
			lblIcon1.setHorizontalAlignment(SwingConstants.CENTER);
			lblIcon1.setIcon(new ImageIcon(Login.class.getResource("/lib/img/smallCafeMaster.png")));
			lblIcon1.setBounds(10, 11, 364, 146);
		}
		return lblIcon1;
	}
	private JButton getBtnPrijava() {
		if (btnPrijava == null) {
			btnPrijava = new JButton("PRIJAVI ME");
			btnPrijava.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					users = loadAllUsers();
					lblIcon2.setVisible(false);
					lblIcon3.setVisible(false);
					String username = txtUsername.getText();
					String password = String.valueOf(txtPassword.getPassword());
					
					if(checkValidity(username, password) == true) {
						dispose();
					}
					
				}
			});
			btnPrijava.setFocusable(false);
			btnPrijava.setFont(new Font("Segoe UI", Font.ITALIC, 20));
			btnPrijava.setBounds(43, 246, 269, 37);
		}
		return btnPrijava;
	}
	private JLabel getLblIcon3() {
		if (lblIcon3 == null) {
			lblIcon3 = new JLabel("");
			lblIcon3.setVisible(false);
			lblIcon3.setMaximumSize(new Dimension(30, 30));
			lblIcon3.setMinimumSize(new Dimension(30, 3));
			lblIcon3.setPreferredSize(new Dimension(30, 30));
			lblIcon3.setIcon(new ImageIcon(Login.class.getResource("/lib/img/error.png")));
			lblIcon3.setBounds(322, 196, 30, 29);
		}
		return lblIcon3;
	}
	private JLabel getLblIcon2() {
		if (lblIcon2 == null) {
			lblIcon2 = new JLabel("");
			lblIcon2.setIcon(new ImageIcon(Login.class.getResource("/lib/img/error.png")));
			lblIcon2.setVisible(false);
			lblIcon2.setBounds(322, 163, 30, 29);
		}
		return lblIcon2;
	}

}
