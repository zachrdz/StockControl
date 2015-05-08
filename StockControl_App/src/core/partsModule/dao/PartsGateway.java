package core.partsModule.dao;
/**
 * 
 */


import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import core.partsModule.models.obj.PartItem;
import core.settings.models.AppPreferences;


public class PartsGateway implements PartsDao{
	
	private Connection connection;
	private Date lastReadTS;
	private int logRecordCount = 0;
	
	public static final String MYSQL_AUTO_RECONNECT = "autoReconnect";
	public static final String MYSQL_MAX_RECONNECTS = "maxReconnects";
	
	public PartsGateway(AppPreferences AP){
		//set up the driver
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			System.out.println("Where is your MySQL JDBC Driver?");
			e.printStackTrace();
			return;
		}
		//System.out.println("MySQL JDBC driver registered.");
		
		try {
			java.util.Properties connProperties = new java.util.Properties();
			connProperties.put("user", AP.getUsername());
			connProperties.put("password", AP.getPassword());
			
			connProperties.put(MYSQL_AUTO_RECONNECT, "true");
			connProperties.put(MYSQL_MAX_RECONNECTS, "10");
			
			this.connection = DriverManager.getConnection(AP.getUrl(), connProperties);
			
			System.out.println("DB connection for PartsGateway successful.");
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	public ResultSet doRead(){
		String callString = "{call read_PartsModule()}";
		ResultSet rs = null;
		
		try {
			CallableStatement callstate = this.connection.prepareCall(callString);
			rs = callstate.executeQuery();
			
			setLastUpdateTS();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return rs;
	}
	
	public void doDelete(PartItem part){
		String callString = "{call delete_PartByID(?)}";
		
		try {
			CallableStatement callstate = this.connection.prepareCall(callString);
			callstate.setInt(1,part.getPartID());
			callstate.executeUpdate();
			
			System.out.println("Record with partID " + part.getPartID() + " deleted from PartsModule table.");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void doAdd(PartItem part){
		if(!doCheckDuplicateByNo(part)){
			String callString = "{call add_Part(?,?,?,?,?)}";

			try {
				CallableStatement callstate = this.connection.prepareCall(callString);
				callstate.setString(1, part.getPartNo());
				callstate.setString(2, part.getPartNoExternal());
				callstate.setString(3, part.getPartName());
				callstate.setString(4, part.getPartVendor());
				callstate.setString(5, part.getPartQuantityUnit());
				
				callstate.executeUpdate();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	
	public void doUpdate(PartItem part){
		String callString = "{call update_PartByID(?,?,?,?,?,?)}";
		
		try {
			CallableStatement callstate = this.connection.prepareCall(callString);
			callstate.setInt(1, part.getPartID());
			callstate.setString(2, part.getPartNo());
			callstate.setString(3, part.getPartNoExternal());
			callstate.setString(4, part.getPartName());
			callstate.setString(5, part.getPartVendor());
			callstate.setString(6, part.getPartQuantityUnit());
			
			callstate.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public boolean doCheckDuplicateByNo(PartItem part){
		String callString = "{call chkDup_PartByNo(?)}";
		
		try {
			CallableStatement callstate = this.connection.prepareCall(callString);
			callstate.setString(1, part.getPartNo());
			ResultSet rs = callstate.executeQuery();
			
			while(rs.next()){
				//System.out.println("Duplicate Entry Part No!");
				return true;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		//No duplicates found
		return false;
	}
	
	public boolean doCheckDuplicateByName(PartItem part){
		String callString = "{call chkDup_PartByName(?)}";
		
		try {
			CallableStatement callstate = this.connection.prepareCall(callString);
			callstate.setString(1, part.getPartName());
			ResultSet rs = callstate.executeQuery();
			
			while(rs.next()){
				//System.out.println("Duplicate Entry Part Name!");
				return true;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		//No duplicates found
		return false;
	}
	
	public boolean doCheckInvAssocByPartID(PartItem part){
		String callString = "{call chkAssocInv_PartByID(?)}";
		
		try {
			CallableStatement callstate = this.connection.prepareCall(callString);
			callstate.setInt(1, part.getPartID());
			ResultSet rs = callstate.executeQuery();
			
			while(rs.next()){
				//System.out.println("Cannot delete, inventory records are currently associated with this part.");
				return true;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		//No duplicates found
		return false;
	}
	
	public ArrayList<PartItem> doGetPartsList(){
		ResultSet rs = doRead();

		System.out.println("Parts List Updated");
		ArrayList<PartItem> partsList = new ArrayList<PartItem>();
		try {
			while(rs.next()){
				PartItem part = new PartItem();
				part.setPartID(rs.getInt("partID"));
				part.setPartNo(rs.getString("partNo"));
				part.setPartNoExternal(rs.getString("partNoExternal"));
				part.setPartName(rs.getString("partName"));
				part.setPartVendor(rs.getString("partVendor"));
				part.setPartQuantityUnit(rs.getString("partQuantityUnit"));
				partsList.add(part);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return partsList;
	}
	
	public void setLastUpdateTS(){
		String callString = "{call get_CSTServerTime()}";
		ResultSet timeresult = null;
		
		try {
			CallableStatement callstate = this.connection.prepareCall(callString);
			timeresult = callstate.executeQuery();
			
			String currentTime = null;
			
			while (timeresult.next())
            {
				currentTime = timeresult.getString("ts");
				break;
            }

			lastReadTS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(currentTime);
			System.out.println("Parts Table synced: " + lastReadTS);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public boolean doPollChanges(){
		
		String query = "SELECT log_createTimeStamp,log_updateTimeStamp FROM PartsModuleLog";
		ResultSet rs = null;
		int tmpLogRecCount = 0;
		
		try {
			PreparedStatement ps = this.connection.prepareStatement(query);
			rs = ps.executeQuery();
			
			while(rs.next()){
				String createTS = rs.getString("log_createTimeStamp");
				String updateTS = rs.getString("log_updateTimeStamp");

				Date cTS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(createTS);
				Date uTS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(updateTS);
				
				if(cTS.after(lastReadTS) || uTS.after(lastReadTS)){
					System.out.println("Changes to table found! Last Update: " + lastReadTS + " createTS: " + cTS + " updateTS: " + uTS);
					return true; //changes found
				}
				tmpLogRecCount++;
			}
			if(logRecordCount == 0){ logRecordCount = tmpLogRecCount; }
			if((logRecordCount != 0) && (logRecordCount != tmpLogRecCount)){
				logRecordCount = tmpLogRecCount; return true; //ChangesFound
			}
		
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return false; //no changes to parts module table found
		
	}
	
	public void doClose(){
		try {
			connection.close();
			System.out.println("DB connection for PartsGateway has been closed.");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
