package backend_package;

import java.util.GregorianCalendar;
import java.util.Objects;

public class Product {
	private String category;
	private String name;
	private double price;
	private double expenses;
	private int totalQuantity;
	private GregorianCalendar orderTime;
	private int userId;
	private int orderQuantity; // used to show quantity in JTable order
		
	public Product(String category, String name, double price, double expenses) {
		this.category = category;
		this.name = name;
		this.price = price;
		this.expenses = expenses;
		this.totalQuantity = 0;
		this.orderTime = null;
		this.userId = Integer.MIN_VALUE;
		orderQuantity = 1;
	}
	
	public String toString() {
		return name;
	}
	public String extendedToString() {
		return category + "-" + name + "-" + price + "-" + expenses; 
	}
	
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof Product)) {
			return false;
		}
		Product other = (Product) obj;
		return Objects.equals(category, other.category)
			//	&& Double.doubleToLongBits(expenses) == Double.doubleToLongBits(other.expenses)
				&& Objects.equals(name, other.name)
				&& Double.doubleToLongBits(price) == Double.doubleToLongBits(other.price);
	}

	public String getCategory() {
		return category;
	}
	public void setCategory(String category) throws Exception {
		if(category == null || category.isEmpty()) {
			System.err.println("GRESKA: Uneta kategorija \"" + category + "\" nije validna! (Product/setCategory())");
			return;
		}
		this.category = category;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) throws Exception {
		if(name == null || name.isEmpty()) {
			System.err.println("GRESKA: Uneto ime \"" + name + "\"nije validno! (Product/setName())");
			return;
		}
		this.name = name;
	}
	
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) throws Exception {
		if(price < 0) {
			System.err.println("GRESKA: Uneta cena nije validna! (Product/setPrice())");
			return;
		}
		this.price = price;
	}
	
	public double getExpenses() {
		return expenses;
	}
	public void setExpenses(double expenses) {
		if(expenses < 0) {
			System.err.println("GRESKA: Uneti troskovi nisu validni! (Product/setExpenses())");
		}
		this.expenses = expenses;
	}

	public int getQuantity() {
		return totalQuantity;
	}
	public void setQuantity(int quantity) {
		if(quantity < 0) {
			System.err.println("GRESKA: Uneti kvantitet proizvoda nije validan! (Product/setQuantity())");
			return;
		}
		this.totalQuantity = quantity;
	}

	public GregorianCalendar getOrderTime() {
		return orderTime;
	}
	public void setOrderTime(GregorianCalendar orderTime) {
		this.orderTime = orderTime;
	}

	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		if(userId < 0) {
			System.err.println("GRESKA: Uneti ID korisnika nije validan! (Product/setUserId())");
			return;
		}
		this.userId = userId;
	}

	public int getOrderQuantity() {
		return orderQuantity;
	}
	public void setOrderQuantity(int orderQuantity) {
		this.orderQuantity = orderQuantity;
	}
}
