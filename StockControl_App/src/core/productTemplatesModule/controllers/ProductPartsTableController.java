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

import core.mdi.models.MasterFrame;
import core.productTemplatesModule.models.ProductPartsTableModel;
import core.productTemplatesModule.models.obj.ProductPartItem;
import core.productTemplatesModule.models.obj.ProductPartItemExt;
import core.productTemplatesModule.views.ProductPartsAddView;
import core.productTemplatesModule.views.ProductPartsDetailView;
import core.productTemplatesModule.views.ProductPartsTableView;

/**
 * @author Zach
 *
 */
public class ProductPartsTableController implements ActionListener, ListSelectionListener{
	public static final String ADD_PRODUCT_PART_COMMAND = "ADD_PRODUCT_PART_COMMAND";
	public static final String DELETE_PRODUCT_PART_COMMAND = "DELETE_PRODUCT_PART_COMMAND";
	
	ProductPartsTableView view;
	ProductPartsTableModel tableModel;
	private ProductPartItem productPartSelected;
	private ProductPartItemExt productPartSelectedExt;
	private int rowIndexSelected;
	private MasterFrame m;
	
	public ProductPartsTableController(ProductPartsTableView view, ProductPartsTableModel tableModel, MasterFrame m){
		this.m = m;
		this.view = view;
		this.tableModel = tableModel;
		
		initView();
	}
	
	private void initView() {
		/* Initialize empty row selection */
		productPartSelected = new ProductPartItem();
		productPartSelectedExt = new ProductPartItemExt();
		
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
		case ADD_PRODUCT_PART_COMMAND:
			new ProductPartsAddController(new ProductPartsAddView(m), tableModel, m);
			break;
		case DELETE_PRODUCT_PART_COMMAND:
			if(promptDelete(productPartSelected)){
				tableModel.removeRow(productPartSelected);
				tableModel.refreshTableData();
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
						case 0 : System.out.print("PTP ID : " + value + ", "); productPartSelected.setPtpID((Integer) value); productPartSelectedExt.setPtpID((Integer) value); break;
						case 1 : System.out.print("ProductID : " + value + ", "); productPartSelected.setProductID((Integer) value); productPartSelectedExt.setProductID((Integer) value); break;
						case 2 : System.out.print("PartID : " + value + ", "); productPartSelected.setPartID((Integer) value); productPartSelectedExt.setPartID((Integer) value); break;
						case 3 : productPartSelectedExt.setPartNo((String) value); break;
						case 4 : productPartSelectedExt.setPartNoExternal((String) value); break;
						case 5 : productPartSelectedExt.setPartName((String) value); break;
						case 6 : productPartSelectedExt.setPartVendor((String) value); break;
						case 7 : productPartSelectedExt.setPartQuantityUnit((String) value); break;
						case 8 : System.out.print("Quantity : " + value + ""); productPartSelected.setQuantity((Integer) value); productPartSelectedExt.setQuantity((Integer) value); break;
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
					new ProductPartsDetailController(new ProductPartsDetailView(tableModel.getRow(rowIndexSelected), m), tableModel, m);
				}
			}
		});
	}
	
	public boolean promptDelete(ProductPartItem productPart){
		if(productPart.getProductID() <= 0){
			m.displayChildMessage("No record has been selected for deletion.");
			return false;
		}
		
		String titleBar = "Are you sure?";
		String infoMsg = "Are you sure you would like to delete this product part? This action cannot be undone.";

		return m.displayChildMessageOption(titleBar, infoMsg);
	}
}
