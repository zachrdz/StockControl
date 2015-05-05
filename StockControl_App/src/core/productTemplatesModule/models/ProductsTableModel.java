/**
 * 
 */
package core.productTemplatesModule.models;

import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.swing.table.AbstractTableModel;

import core.productTemplatesModule.dao.ProductsGateway;
import core.productTemplatesModule.models.obj.ProductItem;
import core.settings.models.AppPreferences;

/**
 * @author Zach
 *
 */
@SuppressWarnings("serial")
public class ProductsTableModel extends AbstractTableModel{
	private ArrayList<String> columns = new ArrayList<>();
	private ArrayList<ProductItem> rows = new ArrayList<ProductItem>();
	private ProductsGateway gateway;
	private AppPreferences AP;
	
	public ProductsTableModel(AppPreferences AP){
		this.AP = AP;
		
		addColumn("Product ID");
		addColumn("Product No.");
		addColumn("Product Description");
		loadTableData();
		
		listenForDBChanges(5);
	}
	
	public void loadTableData(){
		gateway = new ProductsGateway(AP);
		rows = gateway.doGetProductList();

		fireTableStructureChanged();
	}
	
	public void refreshTableData(){
		rows = gateway.doGetProductList();
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
    
	public void addRow(ProductItem product) {
		gateway.doAdd(product);
		refreshTableData();
	}
	
	//Remove row from View table and SQL table by productID
	public void removeRow(ProductItem product){
		gateway.doDelete(product);
		refreshTableData();
	}

	public void updateRow(ProductItem product){
		gateway.doUpdate(product);
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

	public ProductItem getRow(int index){
		return rows.get(index);
	}
	
	//Return current ArrayList of product that populates the tablemodel
	public ArrayList<ProductItem> getRows(){
		return rows;
	}

	@Override
	public Object getValueAt(int row, int col) {
		Object data;
		switch(col){
			case 0:
				data = rows.get(row).getProductID();
				break;
			case 1:
				data = rows.get(row).getProductNo(); 
				break;
			case 2:
				data = rows.get(row).getProductDesc(); 
				break;
			default : 
				data = null;
				break;
		}
		return data;
	}
	
	public boolean checkForDuplicate(ProductItem product){
		return gateway.doCheckDuplicateByNo(product);
	}
	
	public void listenForDBChanges(int sec){
		ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor();
		exec.scheduleAtFixedRate(new Runnable() {
		  @Override
		  public void run() {
			  if(gateway.doPollChanges()) refreshTableData();
		  }
		}, 0, sec, TimeUnit.SECONDS);
	}
	
	public void exitModule(){
		gateway.doClose();
	}

}
