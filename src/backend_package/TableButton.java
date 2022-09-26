package backend_package;

import javax.swing.JRadioButton;

public class TableButton extends JRadioButton{
	private Table linkedTable;

	public TableButton(Table linkedTable, String caption) {
		super();
		this.linkedTable = linkedTable;
		this.setText(caption);
	}
	
	
	public String toString() {
		return linkedTable.toString();
	}


	public Table getLinkedTable() {
		return linkedTable;
	}public void setLinkedTable(Table linkedTable) {
		this.linkedTable = linkedTable;
	}
	
}
