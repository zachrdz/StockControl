package core.inventoryModule.models;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.swing.table.AbstractTableModel;

import core.inventoryModule.dao.InvGateway;
import core.inventoryModule.models.obj.InvItem;
import core.inventoryModule.models.obj.InvItemExt;
import core.settings.models.AppPreferences;

public class InvTableModel extends AbstractTableModel {
	private static final long serialVersionUID = 3946572900211308486L;
	private ArrayList<String> columns = new ArrayList<String>();
	private ArrayList<InvItemExt> rows = new ArrayList<InvItemExt>();
	private String locFilter;
	private InvGateway gateway;
	private AppPreferences AP;
	
	public InvTableModel(AppPreferences AP){
		this.AP = AP;
		this.locFilter = "";
		addColumn("Inv ID");
		addColumn("Part ID");
		addColumn("Product ID");
		addColumn("Type");
		addColumn("No");
		addColumn("Name");
		addColumn("Location");
		addColumn("Quantity");
		loadTableData();
		
		listenForDBChanges(5);
	}
	
	public void loadTableData(){
		gateway = new InvGateway(AP);
		rows = gateway.doGetInvListExt(locFilter);

		fireTableStructureChanged();
	}
	
	public void refreshTableData(String locFilter){
		rows = gateway.doGetInvListExt(locFilter);
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
    
	public void addRow(InvItem inv) {
		gateway.doAdd(inv);
		refreshTableData(locFilter);
	}
	
	//Remove row from View table and SQL table by invID
	public void removeRow(InvItem inv){
		gateway.doDelete(inv);
		refreshTableData(locFilter);
	}

	public void updateRow(InvItem inv){
		gateway.doUpdate(inv);
		refreshTableData(locFilter);
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

	public InvItem getRow(int index){
		return rows.get(index);
	}
	
	//Return current ArrayList of inventory items that populates the tablemodel
	public ArrayList<InvItemExt> getRows(){
		return rows;
	}
	
	@Override
	public Object getValueAt(int row, int col) {
		Object data;
		switch(col){
			case 0:
				data = rows.get(row).getInvID();
				break;
			case 1:
				data = rows.get(row).getInvPartID(); 
				break;
			case 2:
				data = rows.get(row).getInvProductID(); 
				break;
			case 3:
				data = (rows.get(row).getInvPartID() == 0) ? "Product" :  "Part";
				break;
			case 4:
				data = (rows.get(row).getInvPartID() == 0) ? rows.get(row).getProductNo() : rows.get(row).getPartNo(); 
				break;
			case 5:
				data = (rows.get(row).getInvPartID() == 0) ? rows.get(row).getProductDesc() : rows.get(row).getPartName(); 
				break;
			case 6:
				data = rows.get(row).getInvLocation(); 
				break;
			case 7:
				data = rows.get(row).getInvQuantity(); 
				break;
			default : 
				data = null;
				break;
		}
		return data;
	}
	
	public boolean checkForDuplicate(InvItem inv){
		return gateway.doCheckDuplicate(inv);
	}
	
	public boolean checkCanAdd(InvItem inv){
		return gateway.doCheckAdd(inv);
	}
	
	public void setLocFilter(String locFilter){
		this.locFilter = locFilter;
	}
	
	public String getLocFilter(){
		return locFilter;
	}
	
	public Date getInvItemLastUpdate(InvItem inv){
		return gateway.getInvItemLastUpdateTS(inv);
	}
	
	public InvItem getUpdatedInvItem(InvItem inv){
		return gateway.doGetInvRec(inv);
	}
	
	public void listenForDBChanges(int sec){
		ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor();
		exec.scheduleAtFixedRate(new Runnable() {
		  @Override
		  public void run() {
			  if(gateway.doPollChanges()) refreshTableData(locFilter);
		  }
		}, 0, sec, TimeUnit.SECONDS);
	}
	
	public void exitModule(){
		gateway.doClose();
	}
	
}
