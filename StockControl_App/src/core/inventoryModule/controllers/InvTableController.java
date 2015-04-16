package core.inventoryModule.controllers;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableModel;

import core.inventoryModule.models.InvItem;
import core.inventoryModule.models.InvTableModel;
import core.inventoryModule.views.InvAddView;
import core.inventoryModule.views.InvDetailView;
import core.inventoryModule.views.InvTableView;
import core.mdi.MasterFrame;
import core.settings.models.Function;


public class InvTableController implements ActionListener, ListSelectionListener {
	public static final String ADD_INV_COMMAND = "ADD_INV_COMMAND";
	public static final String DELETE_INV_COMMAND = "DELETE_INV_COMMAND";
	
	private InvTableView view;
	private InvTableModel tableModel;
	private InvItem invSelected;
	private int rowIndexSelected;
	private MasterFrame m;
	
	public InvTableController(InvTableView view, MasterFrame m) {
		this.view = view;
		this.m = m;
		this.tableModel = m.getInvTableModel();
		initView();
	}
	
	private void initView() {
		/* Initialize empty row selection */
		invSelected = new InvItem();
		
		/* Initialize button listeners */
		view.addButtonListener(this);
		
		/* Initialize Table listener, used for double/single click check */
		listenForClickType();
		
		/* Initialize table record selection listener for table*/
		ListSelectionModel listMod = view.getTable().getSelectionModel();
		listMod.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listMod.addListSelectionListener(this);
		
		/* Initialize table record selection listener for location list*/
		ListSelectionModel listModLoc = view.getLocList().getSelectionModel();
		listModLoc.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listModLoc.addListSelectionListener(this);
		
		setRoleRestrictions();
	}
	
	private void setRoleRestrictions(){
		if(!m.getSession().getUserFunctions().contains(new Function("canAddInventory"))){
			view.getAddInvBtn().setEnabled(false);
		}
		if(!m.getSession().getUserFunctions().contains(new Function("canDeleteInventory"))){
			view.getDeleteInvBtn().setEnabled(false);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		switch (e.getActionCommand()) {
		case ADD_INV_COMMAND:
			new InvAddController(new InvAddView(m), m);
			break;
		case DELETE_INV_COMMAND:
			boolean checkQuanEmpty;
			checkQuanEmpty = (invSelected.getInvQuantity() > 0) ? false : true;
			if(checkQuanEmpty && promptDelete(invSelected)){
				tableModel.removeRow(invSelected);
			} else if(!checkQuanEmpty && invSelected.getInvID() != 0){
				promptNotEmpty();
			} 
			break;
		default : break;
		}
	}
	
	@Override
	public void valueChanged(ListSelectionEvent e) {
		// TODO Auto-generated method stub
		int[] selRows;
		Object value;
		Object locRow;

		if (!e.getValueIsAdjusting()) {
			selRows = view.getTable().getSelectedRows();
			locRow = view.getLocList().getSelectedValue();

			if (selRows.length > 0) {
				//Index of row selected
				rowIndexSelected = selRows[0]; 
				TableModel tm = view.getTableModel();
				
				System.out.print("Selection {");
				for (int i = 0; i < tableModel.getColumnCount(); i++) {
					// get View table data
					value = tm.getValueAt(rowIndexSelected, i);
					
					switch(i){
						case 0 : System.out.print("InvID : " + value + ", "); invSelected.setInvID((Integer) value); break;
						case 1 : System.out.print("PartID : " + value + ", "); invSelected.setInvPartID((Integer) value); break;
						case 2 : System.out.print("ProductID : " + value + ", "); invSelected.setInvProductID((Integer) value); break;
						case 6 : System.out.print("Location : " + value + ", "); invSelected.setInvLocation((String) value); break;
						case 7 : System.out.print("Quantity : " + value + ""); invSelected.setInvQuantity((Integer) value); break;
						default : break;
					}
				}
				System.out.println("}");
			}
			
			if (locRow != null && !locRow.equals(tableModel.getLocFilter())){
				System.out.println("Location Selected: " + locRow);
				if(locRow.equals("All")){
					tableModel.setLocFilter((String) locRow);
					tableModel.refreshTableData(""); 
				} else{
					tableModel.setLocFilter((String) locRow);
					tableModel.refreshTableData((String) locRow); 
				}
				
			}
		}
	}
	
	public void listenForClickType(){
		view.getTable().addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					//System.out.println(" double click");
					if(!m.getSession().getUserFunctions().contains(new Function("canAddInventory"))){
						m.displayChildMessage("You are not authorized to edit Inventory items!");
					} else{
						//if(tableModel.getRow(rowIndexSelected).getInvProductID() > 0){
						//	m.displayChildMessage("Product Items may currently only be ADDED or DELETED, not EDITED.");
						//} else{
							new InvDetailController(new InvDetailView(tableModel.getRow(rowIndexSelected), m), m);
						//}
					}
				}
			}
		});
	}
	
	public boolean promptDelete(InvItem inv){
		if(inv.getInvID() <= 0){
			m.displayChildMessage("No record has been selected for deletion.");
			return false;
		}
		
		String titleBar = "Are you sure?";
		String infoMsg = "Are you sure you would like to delete record ID: " + inv.getInvID() + "? This action cannot be undone.";
		
		return m.displayChildMessageOption(titleBar, infoMsg);
	}
	
	public void promptNotEmpty(){
    	m.displayChildMessage("Record cannot be deleted because the quantity is not 0.");
    	return;
    }
}

