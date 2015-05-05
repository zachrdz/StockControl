/**
 * 
 */
package core.productTemplatesModule.controllers;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableModel;

import core.inventoryModule.models.obj.InvItem;
import core.mdi.models.MasterFrame;
import core.productTemplatesModule.models.ProductPartsTableModel;
import core.productTemplatesModule.models.ProductsTableModel;
import core.productTemplatesModule.models.obj.ProductItem;
import core.productTemplatesModule.views.ProductPartsTableView;
import core.productTemplatesModule.views.ProductsAddView;
import core.productTemplatesModule.views.ProductsDetailView;
import core.productTemplatesModule.views.ProductsTableView;


public class ProductsTableController implements ActionListener, ListSelectionListener {
	public static final String ADD_PRODUCT_COMMAND = "ADD_PRODUCT_COMMAND";
	public static final String DELETE_PRODUCT_COMMAND = "DELETE_PRODUCT_COMMAND";
	public static final String CLOSE_COMMAND = "CLOSE_COMMAND";
	
	private ProductsTableView view;
	private ProductsTableModel tableModel;
	private ProductPartsTableModel pptm;
	private ProductItem productSelected;
	private int rowIndexSelected;
	private MasterFrame m;
	
	public ProductsTableController(ProductsTableView view, MasterFrame m) {
		this.view = view;
		this.m = m;
		this.tableModel = m.getProductsTableModel();
		initView();
	}
	
	private void initView() {
		/* Initialize empty row selection */
		productSelected = new ProductItem();
		
		/* Initialize button listeners */
		view.addButtonListener(this);
		
		/* Initialize Table listener, used for double/single click check */
		listenForClickType();
		
		/* Initialize table record selection listener */
		ListSelectionModel listMod = view.getTable().getSelectionModel();
		listMod.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listMod.addListSelectionListener(this);

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		switch (e.getActionCommand()) {
		case ADD_PRODUCT_COMMAND:
			new ProductsAddController(new ProductsAddView(m), m);
			break;
		case DELETE_PRODUCT_COMMAND:
			Boolean noAssoc = true;
			for(InvItem invItem : m.getInvTableModel().getRows()){
				if(invItem.getInvProductID() == tableModel.getRow(rowIndexSelected).getProductID()){
					noAssoc = false;
					m.displayChildMessage("Cannot delete this Product Template because it is associated with and Inventory Item");
					break;
				}
			}
			if(noAssoc && promptDelete(productSelected)){
				tableModel.removeRow(productSelected);
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
						case 0 : System.out.print("ProductID : " + value + ", "); productSelected.setProductID((Integer) value); break;
						case 1 : System.out.print("ProductNo : " + value + ", "); productSelected.setProductNo((String) value); break;
						case 2 : System.out.print("ProductDesc : " + value + ", "); productSelected.setProductDesc((String) value); break;
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
					pptm = new ProductPartsTableModel(tableModel.getRow(rowIndexSelected).getProductID());
					ProductPartsTableView pptv = new ProductPartsTableView(pptm);
					new ProductPartsTableController(pptv, pptm, m);
					

					new ProductsDetailController(new ProductsDetailView(tableModel.getRow(rowIndexSelected), pptv, m), m);
				}
			}
		});
	}
	
	public boolean promptDelete(ProductItem product){
		if(product.getProductID() <= 0){
			m.displayChildMessage("No record has been selected for deletion.");
			return false;
		}
		
		String titleBar = "Are you sure?";
		String infoMsg = "Are you sure you would like to delete " + product.getProductNo() + "? This action cannot be undone.";

		return m.displayChildMessageOption(titleBar, infoMsg);
		
	}
}
