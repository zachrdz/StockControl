package core.partsModule.models;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.swing.table.AbstractTableModel;

import core.partsModule.dao.PartsGateway;
import core.settings.models.AppPreferences;

public class PartsTableModel extends AbstractTableModel{
	private static final long serialVersionUID = 7097915139152080548L;
	private ArrayList<String> columns = new ArrayList<>();
	private ArrayList<PartItem> rows = new ArrayList<PartItem>();
	private PartsGateway gateway;
	private AppPreferences AP;
	
	public PartsTableModel(AppPreferences AP){
		this.AP = AP;
		
		addColumn("Part ID");
		addColumn("Part Number");
		addColumn("Part NumberExt");
		addColumn("Part Name");
		addColumn("Part Vendor");
		addColumn("Part Quantity Unit");
		loadTableData();
		
		listenForDBChanges(5);
	}
	
	public void loadTableData(){
		gateway = new PartsGateway(AP);
		rows = gateway.doGetPartsList();

		fireTableStructureChanged();
	}
	
	public void refreshTableData(){
		rows = gateway.doGetPartsList();
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
    
	public void addRow(PartItem part) {
		gateway.doAdd(part);
		refreshTableData();
	}
	
	//Remove row from View table and SQL table by partID
	public void removeRow(PartItem part){
		gateway.doDelete(part);
		refreshTableData();
	}

	public void updateRow(PartItem part){
		gateway.doUpdate(part);
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

	public PartItem getRow(int index){
		return rows.get(index);
	}
	
	//Return current ArrayList of parts that populates the tablemodel
	public ArrayList<PartItem> getRows(){
		return rows;
	}

	@Override
	public Object getValueAt(int row, int col) {
		Object data;
		switch(col){
			case 0:
				data = rows.get(row).getPartID();
				break;
			case 1:
				data = rows.get(row).getPartNo(); 
				break;
			case 2:
				data = rows.get(row).getPartNoExternal(); 
				break;
			case 3:
				data = rows.get(row).getPartName(); 
				break;
			case 4:
				data = rows.get(row).getPartVendor(); 
				break;
			case 5:
				data = rows.get(row).getPartQuantityUnit(); 
				break;
			default : 
				data = null;
				break;
		}
		return data;
	}
	
	public boolean checkForDuplicate(PartItem part){
		return gateway.doCheckDuplicateByNo(part);
	}
	
	public boolean checkForDuplicateWarn(PartItem part){
		return gateway.doCheckDuplicateByName(part);
	}
	
	public boolean checkForAssocInvItems(PartItem part){
		return gateway.doCheckInvAssocByPartID(part);
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

