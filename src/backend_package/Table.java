package backend_package;

import java.util.Iterator;
import java.util.LinkedList;

public class Table {
	private int id;
	private double tableBill;
	private LinkedList<Product> orders;
	
	public Table() throws Exception {
		this.tableBill = 0;
		this.orders = new LinkedList<>();
	}
	
	
	
	@Override
	public String toString() {
		return "Table [id=" + id + ", tableBill=" + tableBill + ", orders=" + orders + "]";
	}



	public double getTableBill() {
		double sum = 0;
		LinkedList<Product> orders = getOrders();
		Iterator<Product> it = orders.iterator();
		
		while(it.hasNext()) {
			Product iteratorProduct = it.next();
			sum = sum + iteratorProduct.getPrice();
		}
		return sum;
	}
	public void setTableBill(double money){
		if(money < 0) {
			System.err.println("GRESKA: Unet racun stola ne sme biti negativan! (Table/setTableBill())");
			return;
		}
		this.tableBill = money;
	}
	
	public LinkedList<Product> getOrders() {
		return orders;
	}
	public void setOrders(LinkedList<Product> orders) {
		this.orders = orders;
	}

	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void addProductToTable(Product newProduct) {
		orders.add(newProduct);
	}
	public void removeProductFromTable(Product product) {
		Iterator<Product> it = orders.iterator();
		boolean found = false;
		
		while(it.hasNext()) {
			if(it.next().equals(product)) found = true;
		}
		
		if(found == false) {
			System.err.println("Dati proizvod se ne nalazi u listi narucenih proizvoda datog stola!");
		} else {
			orders.remove(product);
			System.out.println("Proizvod " + product + "je uspesno izbacen iz liste narucenih proizvoda stola.");
		}
	}
	public void removeAllProductsFromTable() {
		orders.removeAll(orders);
	}
}
