package backend_package;
import javax.swing.JButton;

public class ProductButton extends JButton{

	private static final long serialVersionUID = 1L;
	private Product linkedProduct;

	public ProductButton(Product linkedProduct) {
		this.setText(linkedProduct.getName());
		this.linkedProduct = linkedProduct;
	}

	public String toString() {
		return linkedProduct.toString();
	}


	public Product getLinkedProduct() {
		return linkedProduct;
	}
	public void setLinkedProduct(Product linkedProduct) {
		this.linkedProduct = linkedProduct;
	}

}
