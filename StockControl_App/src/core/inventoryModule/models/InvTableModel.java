package core.inventoryModule.models;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.swing.table.AbstractTableModel;

import core.inventoryModule.dao.InvGateway;
import core.inventoryModule.dao.remote.InvItemLogGatewayRemote;
import core.inventoryModule.models.obj.InvItem;
import core.inventoryModule.models.obj.InvItemExt;
import core.inventoryModule.models.obj.InvItemLogRecord;
import core.settings.models.AppPreferences;

public class InvTableModel extends AbstractTableModel {
	private static final long serialVersionUID = 3946572900211308486L;
	private ArrayList<String> columns = new ArrayList<String>();
	private ArrayList<InvItemExt> rows = new ArrayList<InvItemExt>();
	private String locFilter;
	private InvGateway invGateway;
	private InvItemLogGatewayRemote logGateway;
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
		
		initLog();
	}
	
	public void loadTableData(){
		invGateway = new InvGateway(AP);
		rows = invGateway.doGetInvListExt(locFilter);

		fireTableStructureChanged();
	}
	
	public void refreshTableData(String locFilter){
		rows = invGateway.doGetInvListExt(locFilter);
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
		invGateway.doAdd(inv);
		refreshTableData(locFilter);
	}
	
	//Remove row from View table and SQL table by invID
	public void removeRow(InvItem inv){
		invGateway.doDelete(inv);
		refreshTableData(locFilter);
	}

	public void updateRow(InvItem inv){
		invGateway.doUpdate(inv);
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
		return invGateway.doCheckDuplicate(inv);
	}
	
	public boolean checkCanAdd(InvItem inv){
		return invGateway.doCheckAdd(inv);
	}
	
	public void setLocFilter(String locFilter){
		this.locFilter = locFilter;
	}
	
	public String getLocFilter(){
		return locFilter;
	}
	
	public Date getInvItemLastUpdate(InvItem inv){
		return invGateway.getInvItemLastUpdateTS(inv);
	}
	
	public InvItem getUpdatedInvItem(InvItem inv){
		return invGateway.doGetInvRec(inv);
	}

	public void exitModule(){
		invGateway.doClose();
	}
	
	private void initLog() {
		try {
			Properties props = new Properties();
			props.put("org.omg.CORBA.ORBInitialHost", "localhost");
			props.put("org.omg.CORBA.ORBInitialPort", "3700");

			InitialContext itx = new InitialContext(props);
			logGateway =  (InvItemLogGatewayRemote) itx
					.lookup("java:global/StockControl_Beans/InvItemLogGateway!core.inventoryModule.dao.remote.InvItemLogGatewayRemote");
			
		} catch (NamingException e1) {
			e1.printStackTrace();
		}
	}
	
	public InvItemLogGatewayRemote getLogGateway() {
		return logGateway;
	}
	
	public void registerLogObserver(InvItemLogObserver logObsvr){
		logGateway.registerObserver(logObsvr);
	}
	
	public void unregisterLogObserver(InvItemLogObserver logObsvr) {
		logGateway.unregisterObserver(logObsvr);
	}
	
	public ArrayList<InvItemLogRecord> getInvItemLog(int invID) {
		return logGateway.doRead(invID);
	}
    
	public void addInvItemLogRecord(InvItemLogRecord invItemLogRec) {
		logGateway.doAdd(invItemLogRec);
	}
	
}
