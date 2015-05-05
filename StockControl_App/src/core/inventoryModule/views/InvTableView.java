package core.inventoryModule.views;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import core.inventoryModule.controllers.InvTableController;
import core.inventoryModule.models.InvTableModel;
import core.mdi.models.MasterFrame;


public class InvTableView extends JInternalFrame {
	private InvTableModel tableModel;
	private JTable table;
	@SuppressWarnings("rawtypes")
	private JList locList;
	private JButton addInvBtn,deleteInvBtn;
	@SuppressWarnings("unused")
	private MasterFrame m;
	private static final long serialVersionUID = 1L;
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public InvTableView(MasterFrame m) {
		setLayout(new FlowLayout());
		this.m = m;
		this.tableModel = m.getInvTableModel();

		title = "Inventory List";
		
		table = new JTable(tableModel);
		table.setFillsViewportHeight(true);
		JScrollPane center = new JScrollPane(table);
		center.setPreferredSize(new Dimension(520, 300));
		
		//Hide Columns
		getTable().removeColumn(table.getColumn("Inv ID"));
		getTable().removeColumn(table.getColumn("Part ID"));
		getTable().removeColumn(table.getColumn("Product ID"));

		addInvBtn = new JButton("Add Inventory");
		addInvBtn.setActionCommand(InvTableController.ADD_INV_COMMAND);
		deleteInvBtn = new JButton("Delete Selected");
		deleteInvBtn.setActionCommand(InvTableController.DELETE_INV_COMMAND);
		
		JPanel rightPanel = new JPanel();
		GridLayout rpLayout = new GridLayout(1,3);
		rpLayout.setHgap(10);
		rightPanel.setLayout(rpLayout);
		
		rightPanel.add(addInvBtn);
		rightPanel.add(deleteInvBtn);
		
		JPanel leftPanel = new JPanel();
		GridLayout lpLayout = new GridLayout(4,1);
		lpLayout.setVgap(10);
		leftPanel.setLayout(lpLayout);
		leftPanel.add(new JLabel("Choose Location"));
		
		String	listData[] =
			{
				"All",
				"Facility 1 Warehouse 1",
				"Facility 1 Warehouse 2",
				"Facility 2"
			};
		locList = new JList(listData);
		locList.setSelectedIndex(0);
		
		leftPanel.add(locList);
		leftPanel.add(new JLabel(""));
		leftPanel.add(new JLabel(""));
		
		add(leftPanel, BorderLayout.WEST);
		add(center, BorderLayout.CENTER);
		add(rightPanel, BorderLayout.SOUTH);
		
		resizable = true;
        closable = true;
        maximizable = true;
        iconable = true;
        
        pack();
        
        setSize(750,430);
        Dimension desktopSize = m.getDesktop().getSize();
		Dimension jInternalFrameSize = getSize();
		setLocation((desktopSize.width - jInternalFrameSize.width)/2, (desktopSize.height- jInternalFrameSize.height)/2);
        
        m.getDesktop().add(this);
        m.getDesktop().getDesktopManager().activateFrame(this);
        setVisible(true); 
	}

	public void addButtonListener(ActionListener listener) {
		addInvBtn.addActionListener(listener);
		deleteInvBtn.addActionListener(listener);
	}

	public JTable getTable() {
		return table;
	}
	
	@SuppressWarnings("rawtypes")
	public JList getLocList(){
		return locList;
	}
	
	public JInternalFrame getFrame(){
		return this;
	}

	public InvTableModel getTableModel() {
		return tableModel;
	}

	public JButton getAddInvBtn() {
		return addInvBtn;
	}

	public JButton getDeleteInvBtn() {
		return deleteInvBtn;
	}
	
}
