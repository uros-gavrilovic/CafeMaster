package backend_package;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

public class TableOrdersModel extends AbstractTableModel{
	String tableHeaders[] = {"KATEGORIJA", "NAZIV", "CENA", "KOLIÈINA"}; 
    private LinkedList<Product> products;

    public TableOrdersModel(LinkedList<Product> products) {
        this.products = products;
    }

    public void addRow(Product product) {
    	products.add(product);
    	fireTableRowsInserted(getRowCount(), getColumnCount());
    }
    
    @Override 
    public int getColumnCount() { 
        return tableHeaders.length; 
    } 
    @Override 
    public String getColumnName(int index) { 
        return tableHeaders[index]; 
    }
    @Override
    public void setValueAt(Object value, int row, int col) {
        fireTableCellUpdated(row, col);
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
                value = product.getQuantity();
                break;
            default:
            	System.err.println("ERROR: Invalid column index.");
        }

        return value;

    }
    public Product getProductAt(int row) {
        return products.get(row);
    }

	@Override
	public int getRowCount() {
		return products.size();
	}
}
