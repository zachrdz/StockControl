/**
 * 
 */
package core.productTemplatesModule.models;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

import core.productTemplatesModule.dao.ProductPartsGateway;
import core.productTemplatesModule.models.obj.ProductPartItem;
import core.productTemplatesModule.models.obj.ProductPartItemExt;
import core.settings.models.AppPreferences;

/**
 * @author Zach
 *
 */
@SuppressWarnings("serial")
public class ProductPartsTableModel extends AbstractTableModel{
	private ArrayList<String> columns = new ArrayList<>();
	private ArrayList<ProductPartItemExt> rows = new ArrayList<ProductPartItemExt>();
	private ProductPartsGateway gateway;
	private int globalProductID;
	private AppPreferences AP;
	
	public ProductPartsTableModel(int productID){
		globalProductID = productID;
		AP = new AppPreferences();
		
		addColumn("PTP ID");
		addColumn("Product ID");
		addColumn("Part ID");
		addColumn("Part No.");
		addColumn("External Part No.");
		addColumn("Part Name");
		addColumn("Part Vendor");
		addColumn("Part Quantity Unit");
		addColumn("Quantity");
		loadTableData();
	}
	
	public void loadTableData(){
		gateway = new ProductPartsGateway(AP);
		rows = gateway.doGetProductPartListExt(globalProductID);
		
		fireTableStructureChanged();
	}
	
	public void refreshTableData(){
		rows = gateway.doGetProductPartListExt(globalProductID);
		this.fireTableDataChanged();
	}
	
	public void addColumn(String header) {
		columns.add(header);
		fireTableStructureChanged();
	}
	
	public void removeColumnAt(int index) {
		try{	
			columns.remove(index);
			fireTableStructureChanged();
		} catch(Exception ArrayIndexOutOfBoundsException){
			System.out.println("No column exists at given index!");
		}
	}
    
	public void addRow(ProductPartItem productPart) {
		gateway.doAdd(productPart);
		refreshTableData();
	}
	
	//Remove row from View table and SQL table by productID
	public void removeRow(ProductPartItem productPart){
		gateway.doDelete(productPart);
		refreshTableData();
	}

	public void updateRow(ProductPartItem productPart){
		gateway.doUpdate(productPart);
		refreshTableData();
	}
	
	@Override
	public int getColumnCount() {
		return columns.size();
	}
	
	@Override
	public int getRowCount() {
		return rows.size();
	}
	
	@Override
	public String getColumnName(int column) {
		return columns.get(column);
	}

	public ProductPartItem getRow(int index){
		return rows.get(index);
	}
	
	//Return current ArrayList of product parts that populates the tablemodel
	public ArrayList<ProductPartItemExt> getRows(){
		return rows;
	}

	@Override
	public Object getValueAt(int row, int col) {
		Object data;
		switch(col){
			case 0:
				data = rows.get(row).getPtpID();
				break;
			case 1:
				data = rows.get(row).getProductID(); 
				break;
			case 2:
				data = rows.get(row).getPartID(); 
				break;
			case 3:
				data = rows.get(row).getPartNo(); 
				break;
			case 4:
				data = rows.get(row).getPartNoExternal(); 
				break;
			case 5:
				data = rows.get(row).getPartName(); 
				break;
			case 6:
				data = rows.get(row).getPartVendor(); 
				break;
			case 7:
				data = rows.get(row).getPartQuantityUnit(); 
				break;
			case 8:
				data = rows.get(row).getQuantity(); 
				break;
			default : 
				data = null;
				break;
		}
		return data;
	}
	
	public int getGlobalProductID() {
		return globalProductID;
	}

	public void setGlobalProductID(int globalProductID) {
		this.globalProductID = globalProductID;
	}
	
	public boolean checkForDuplicate(ProductPartItem productPart){
		return gateway.doCheckDuplicate(productPart);
	}
	
	public void exitModule(){
		gateway.doClose();
	}
}