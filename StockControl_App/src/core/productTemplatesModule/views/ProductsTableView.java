package core.productTemplatesModule.views;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import core.mdi.MasterFrame;
import core.productTemplatesModule.controllers.ProductsTableController;
import core.productTemplatesModule.models.ProductsTableModel;


@SuppressWarnings("serial")
public class ProductsTableView extends JInternalFrame {
	private ProductsTableModel tableModel;
	private JTable table;
	private JButton addProductBtn,deleteProductBtn,closeBtn;
	@SuppressWarnings("unused")
	private MasterFrame m;
	
	public ProductsTableView(MasterFrame m) {
		setLayout(new FlowLayout());
		this.m = m;
		this.tableModel = m.getProductsTableModel();

		title = "Product Templates List";
		
		table = new JTable(tableModel);
		table.setFillsViewportHeight(true);
		JScrollPane pane = new JScrollPane(table);
		pane.setPreferredSize(new Dimension(320, 300));
		getContentPane().add(pane);
		
		//Hide Columns
		getTable().removeColumn(table.getColumn("Product ID"));
		
		//Set column width
		table.getColumn("Product No.").setPreferredWidth(1);

		addProductBtn = new JButton("Add a Product Template");
		addProductBtn.setActionCommand(ProductsTableController.ADD_PRODUCT_COMMAND);
		deleteProductBtn = new JButton("Delete Selected");
		deleteProductBtn.setActionCommand(ProductsTableController.DELETE_PRODUCT_COMMAND);
		closeBtn = new JButton("Close");
		closeBtn.setActionCommand(ProductsTableController.CLOSE_COMMAND);
		
		JPanel rightPanel = new JPanel();
		GridLayout rpLayout = new GridLayout(5,1);
		rpLayout.setVgap(10);
		rightPanel.setLayout(rpLayout);
		
		rightPanel.add(addProductBtn);
		rightPanel.add(deleteProductBtn);
		rightPanel.add(new JLabel(""));
		rightPanel.add(new JLabel(""));
		rightPanel.add(closeBtn);

		add(pane, BorderLayout.CENTER);
		add(rightPanel, BorderLayout.EAST);
		
		resizable = true;
        closable = true;
        maximizable = true;
        iconable = true;
        
        pack();
        setSize(520,370);
        Dimension desktopSize = m.getDesktop().getSize();
		Dimension jInternalFrameSize = getSize();
		setLocation((desktopSize.width - jInternalFrameSize.width)/2, (desktopSize.height- jInternalFrameSize.height)/2);
        
        m.getDesktop().add(this);
        m.getDesktop().getDesktopManager().activateFrame(this);
        setVisible(true); 
	}

	public void addButtonListener(ActionListener listener) {
		addProductBtn.addActionListener(listener);
		deleteProductBtn.addActionListener(listener);
		closeBtn.addActionListener(listener);
	}

	public JTable getTable() {
		return table;
	}
	
	public JInternalFrame getFrame(){
		return this;
	}

	public ProductsTableModel getTableModel() {
		return tableModel;
	}
	
}
