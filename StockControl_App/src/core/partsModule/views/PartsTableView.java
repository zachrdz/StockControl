package core.partsModule.views;
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
import core.partsModule.controllers.PartsTableController;
import core.partsModule.models.PartsTableModel;


public class PartsTableView extends JInternalFrame {
	private PartsTableModel tableModel;
	@SuppressWarnings("unused")
	private MasterFrame m;
	private JTable table;
	private JButton addPartBtn,deletePartBtn,closeBtn;
	private static final long serialVersionUID = 1L;
	
	public PartsTableView(MasterFrame m) {
		setLayout(new FlowLayout());
		this.tableModel = m.getPartsTableModel();
		this.m = m;
		title = "Parts List";
		
		table = new JTable(tableModel);
		table.setFillsViewportHeight(true);
		JScrollPane pane = new JScrollPane(table);
		pane.setPreferredSize(new Dimension(370, 300));
		this.add(pane);
		
		//Hide Columns
		getTable().removeColumn(table.getColumn("Part ID"));
  	  	getTable().removeColumn(table.getColumn("Part Number"));

  	  	getTable().removeColumn(table.getColumn("Part NumberExt"));
  	  	getTable().removeColumn(table.getColumn("Part Vendor"));

		addPartBtn = new JButton("Add a Part");
		addPartBtn.setActionCommand(PartsTableController.ADD_PART_COMMAND);
		deletePartBtn = new JButton("Delete Selected");
		deletePartBtn.setActionCommand(PartsTableController.DELETE_PART_COMMAND);
		closeBtn = new JButton("Close");
		closeBtn.setActionCommand(PartsTableController.CLOSE_COMMAND);
		
		JPanel rightPanel = new JPanel();
		GridLayout rpLayout = new GridLayout(5,1);
		rpLayout.setVgap(10);
		rightPanel.setLayout(rpLayout);
		
		rightPanel.add(addPartBtn);
		rightPanel.add(deletePartBtn);
		rightPanel.add(new JLabel(""));
		rightPanel.add(new JLabel(""));
		rightPanel.add(closeBtn);

		add(pane, BorderLayout.CENTER);
		add(rightPanel, BorderLayout.EAST);
		
		resizable = true;
        closable = true;
        maximizable = true;
        iconable = true;
        
        setSize(550, 370);
        Dimension desktopSize = m.getDesktop().getSize();
		Dimension jInternalFrameSize = getSize();
		setLocation((desktopSize.width - jInternalFrameSize.width)/2, (desktopSize.height- jInternalFrameSize.height)/2);
		pack();
		
		m.getDesktop().add(this);
		m.getDesktop().getDesktopManager().activateFrame(this);
		setVisible(true); 
	}

	public void addButtonListener(ActionListener listener) {
		addPartBtn.addActionListener(listener);
		deletePartBtn.addActionListener(listener);
		closeBtn.addActionListener(listener);
	}

	public JTable getTable() {
		return table;
	}

	public JInternalFrame getFrame(){
		return this;
	}

	public PartsTableModel getTableModel() {
		return tableModel;
	}

	public JButton getAddPartBtn() {
		return addPartBtn;
	}

	public JButton getDeletePartBtn() {
		return deletePartBtn;
	}
	
}

