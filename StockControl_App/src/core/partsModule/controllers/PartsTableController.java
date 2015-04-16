package core.partsModule.controllers;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableModel;

import core.mdi.MasterFrame;
import core.partsModule.models.PartItem;
import core.partsModule.models.PartsTableModel;
import core.partsModule.views.PartsAddView;
import core.partsModule.views.PartsDetailView;
import core.partsModule.views.PartsTableView;
import core.settings.models.Function;


public class PartsTableController implements ActionListener, ListSelectionListener {
	public static final String ADD_PART_COMMAND = "ADD_PART_COMMAND";
	public static final String DELETE_PART_COMMAND = "DELETE_PART_COMMAND";
	public static final String CLOSE_COMMAND = "CLOSE_COMMAND";
	
	private PartsTableView view;
	private PartsTableModel tableModel;
	private PartItem partSelected;
	private MasterFrame m;
	private int rowIndexSelected;
	
	public PartsTableController(PartsTableView view, MasterFrame m) {
		this.m = m;
		this.view = view;
		this.tableModel = m.getPartsTableModel();
		initView();
	}
	
	private void initView() {
		/* Initialize empty row selection */
		partSelected = new PartItem();
		
		/* Initialize button listeners */
		view.addButtonListener(this);
		
		/* Initialize Table listener, used for double/single click check */
		listenForClickType();
		
		/* Initialize table record selection listener */
		ListSelectionModel listMod = view.getTable().getSelectionModel();
		listMod.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listMod.addListSelectionListener(this);
		
		setRoleRestrictions();
	}
	
	private void setRoleRestrictions() {
		if (!m.getSession().getUserFunctions().contains(new Function("canAddParts"))) {
			view.getAddPartBtn().setEnabled(false);
		}
		if (!m.getSession().getUserFunctions().contains(new Function("canDeleteParts"))) {
			view.getDeletePartBtn().setEnabled(false);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		switch (e.getActionCommand()) {
		case ADD_PART_COMMAND:
			PartsAddView paView = new PartsAddView(m);
			new PartsAddController(paView, m);
			break;
		case DELETE_PART_COMMAND:
			if(promptDelete(partSelected)){
				tableModel.removeRow(partSelected);
			}
			break;
		case CLOSE_COMMAND:
				view.getFrame().dispose();
				break;
		default : break;
		}
	}
	
	@Override
	public void valueChanged(ListSelectionEvent e) {
		// TODO Auto-generated method stub
		int[] selRows;
		Object value;

		if (!e.getValueIsAdjusting()) {
			selRows = view.getTable().getSelectedRows();

			if (selRows.length > 0) {
				//Index of row selected
				rowIndexSelected = selRows[0]; 
				TableModel tm = view.getTableModel();
				
				System.out.print("Selection {");
				for (int i = 0; i < tableModel.getColumnCount(); i++) {
					// get View table data
					value = tm.getValueAt(rowIndexSelected, i);
					
					switch(i){
						case 0 : System.out.print("PartID : " + value + ", "); partSelected.setPartID((Integer) value); break;
						case 1 : System.out.print("PartNo : " + value + ", "); partSelected.setPartNo((String) value); break;
						case 2 : System.out.print("PartNoExternal : " + value + ", "); partSelected.setPartNoExternal((String) value); break;
						case 3 : System.out.print("PartName : " + value + ", "); partSelected.setPartName((String) value); break;
						case 4 : System.out.print("PartVendor : " + value + ", "); partSelected.setPartVendor((String) value); break;
						case 5 : System.out.print("PartQuantityUnit : " + value + ""); partSelected.setPartQuantityUnit((String) value); break;
						default : break;
					}
				}
				System.out.println("}");
			}
		}
	}
	
	public void listenForClickType(){
		view.getTable().addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					//System.out.println(" double click");
					if (!m.getSession().getUserFunctions().contains(new Function("canAddParts"))) {
						m.displayChildMessage("You are not authorized to edit Parts!");
					} else{
						PartsDetailView pdView = new PartsDetailView(tableModel.getRow(rowIndexSelected), m);
						new PartsDetailController(pdView, m);
					}
				}
			}
		});
	}
	
	public boolean promptDelete(PartItem part){

		if(part.getPartID() <= 0){
			m.displayChildMessage("No record has been selected for deletion.");
			return false;
		}
		
		if(tableModel.checkForAssocInvItems(part)){
			m.displayChildMessage("This record cannot be deleted because there are inventory items associated with it.");
			return false;
		}
		
		String titleBar = "Are you sure?";
		String infoMsg = "Are you sure you would like to delete " + part.getPartName() + "? This action cannot be undone.";
		
		return m.displayChildMessageOption(titleBar, infoMsg);
	}
}

