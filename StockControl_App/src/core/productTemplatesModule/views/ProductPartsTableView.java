/**
 * 
 */
package core.productTemplatesModule.views;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import core.productTemplatesModule.controllers.ProductPartsTableController;
import core.productTemplatesModule.models.ProductPartsTableModel;

/**
 * @author Zach
 *
 */
@SuppressWarnings("serial")
public class ProductPartsTableView extends JPanel{
	private ProductPartsTableModel tableModel;
	private JTable table;
	private JPanel panel;
	private JButton addBtn, deleteBtn;
	
	public ProductPartsTableView(ProductPartsTableModel tableModel){
		this.tableModel = tableModel;
		setLayout(new FlowLayout());
		
		JPanel leftPanel = new JPanel();
		GridLayout lpLayout = new GridLayout(1,1);
		lpLayout.setHgap(10);
		leftPanel.setLayout(lpLayout);
		
		table = new JTable(tableModel);
		table.setFillsViewportHeight(true);
		JScrollPane pane = new JScrollPane(table);
		pane.setPreferredSize(new Dimension(320, 300));
		leftPanel.add(pane);
		
		//Hide Columns
		getTable().removeColumn(table.getColumn("PTP ID"));
		getTable().removeColumn(table.getColumn("Product ID"));
		getTable().removeColumn(table.getColumn("Part ID"));
		getTable().removeColumn(table.getColumn("Part Vendor"));
		getTable().removeColumn(table.getColumn("Part Quantity Unit"));
		getTable().removeColumn(table.getColumn("External Part No."));
		
		JPanel rightPanel = new JPanel();
		GridLayout rpLayout = new GridLayout(2,1);
		rpLayout.setVgap(10);
		rightPanel.setLayout(rpLayout);
        
		addBtn = new JButton("Add Part");        
        rightPanel.add(addBtn); 
        addBtn.setActionCommand(ProductPartsTableController.ADD_PRODUCT_PART_COMMAND);
		
		deleteBtn = new JButton("Delete Part Selected");        
        rightPanel.add(deleteBtn); 
        deleteBtn.setActionCommand(ProductPartsTableController.DELETE_PRODUCT_PART_COMMAND);
 
        add(leftPanel, BorderLayout.WEST);
		add(rightPanel, BorderLayout.EAST);
		
		setSize(300, 300);
		setVisible(true);
	}
	
	public JPanel getPanel() {
		return panel;
	}
	
	public JTable getTable(){
		return table;
	}
	
	public ProductPartsTableModel getTableModel(){
		return tableModel;
	}
	
	public void addButtonListener(ActionListener listener) {
		addBtn.addActionListener(listener);
		deleteBtn.addActionListener(listener);
	}
}