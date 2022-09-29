package backend_package;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import lib.Libary;

public class TableOrdersModel extends AbstractTableModel{
	
	String tableHeaders[] = {"KATEGORIJA", "NAZIV", "CENA", "KOLIÈINA"}; 
    private LinkedList<Product> products;

    public TableOrdersModel(LinkedList<Product> products) {
        this.products = compressOrders(products);
    }
    
    @Override 
    public int getColumnCount() { 
        return tableHeaders.length; 
    } 
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
    	
    	Object value = null;
    	Product product = products.get(rowIndex);
    	switch (columnIndex) {
    	case 0:
    		value = product.getCategory();
    		break;
    	case 1:
    		value = product.getName();
    		break;
    	case 2:
    		value = product.getPrice();
    		break;
    	case 3:
    		if(product.getOrderQuantity() <= 1) {
    			value = "";
    		} else {
    			value = "x " + product.getOrderQuantity();
    		}
    		
    		break;
    	default:
    		System.err.println("ERROR: Invalid column index.");
    	}
    	
    	return value;
    	
    }
	@Override
	public int getRowCount() {
		return compressOrders(products).size();
	}
    @Override 
    public String getColumnName(int index) { 
        return tableHeaders[index]; 
    }
    public Product getProductAt(int row) {
        return products.get(row);
    }
	
    @Override
    public void setValueAt(Object value, int row, int col) {
        fireTableCellUpdated(row, col);
    }
    
    public void addRow(Product product) {
    	if(products.contains(product)) {
    		product.setOrderQuantity(product.getOrderQuantity() + 1);
    	} else {
    		product.setOrderQuantity(1);
    		products.add(product);
    	}
    	fireTableStructureChanged();
    }
    public void removeRow(int row)
    {
        products.remove(row);
        fireTableStructureChanged();
        fireTableRowsDeleted(row, row);
    }
    public void clearTable() {
    	int rows = getRowCount();
    	for(int i=0; i<getRowCount(); i++) {
    		removeRow(i);
    	}
    	System.out.println("TABLE DELETED, ROWS REMOVED: " + rows);
    }

    public LinkedList<Product> compressOrders(LinkedList<Product> listToCompress) {
    	// Compresses LinkedList to a LinkedList without any duplicates.
    	// Duplicates are stored as an increment in Product.orderQuantity.
    	
    	LinkedList<Product> resultList = new LinkedList<Product>();
    	Iterator<Product> it = listToCompress.iterator();
		while(it.hasNext()) {
			Product productIterator = it.next();
			if(resultList.contains(productIterator)) {
				int index = resultList.indexOf(productIterator);
				resultList.get(index).setOrderQuantity(resultList.get(index).getOrderQuantity() + 1);
			} else {
				resultList.add(productIterator);
			}
		}
    	return resultList;
    }
}
