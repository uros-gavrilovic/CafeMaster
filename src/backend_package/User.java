package backend_package;

import java.util.LinkedList;

public class User{
	private int id;
	private String username;
	private String password;
	private double turnover;
	private boolean isAdmin;
	private LinkedList<Product> orders;
	
	public User(int id, String username, String password, boolean isAdmin, double turnover) {
		super();
		this.id = id;
		this.username = username;
		this.password = password;
		this.turnover = turnover;
		this.isAdmin = isAdmin;
		this.orders = new LinkedList<>();
	}
	public String toString() {
		return id + "-" + username + "-" + password + "-" + isAdmin + "-" + turnover;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		if(id < 0) {
			System.err.println("GRESKA: Unet ID korisnika nije validan! (User/setId())");
			return;
		}
		this.id = id;
	}
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		if(username.isEmpty() || username.length() < 3) {
			System.err.println("GRESKA: Uneto korisnièno ime nije validno! (Users/setUser())");
			return;
		}
		this.username = username;
	}
	
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	public boolean isAdmin() {
		return isAdmin;
	}
	public void setAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}
	
	public double getTurnover() {
		return turnover;
	}
	public void setTurnover(double turnover) {
		this.turnover = turnover;
	}
	
	public LinkedList<Product> getOrders() {
		return orders;
	}
	public void setOrders(LinkedList<Product> orders) {
		if(orders == null) {
			System.err.println("GRESKA: Nije moguce postaviti listu narucenih proizvoda za korisnika na NULL! (User/setOrders())");
			return;
		}
		this.orders = orders;
	}
	
	
	public void addProductToUser(Product product) {
		orders.add(product);
	}
	public void addTurnover(Table table) {
		turnover = turnover + table.getTableBill();
	}
}
