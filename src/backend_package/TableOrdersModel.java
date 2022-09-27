package backend_package;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

public class TableOrdersModel extends AbstractTableModel {

    private List<Product> products;

    public TableOrdersModel(List<Product> products) {
        this.products = new ArrayList<Product>(products);
    }

    @Override
    public int getRowCount() {
        return products.size();
    }

    @Override
    public int getColumnCount() {
        return 4;
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

}
