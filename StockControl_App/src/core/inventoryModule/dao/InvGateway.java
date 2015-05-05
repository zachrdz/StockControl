package core.inventoryModule.dao;
/**
 * 
 */


import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import core.inventoryModule.models.obj.InvItem;
import core.inventoryModule.models.obj.InvItemExt;
import core.settings.models.AppPreferences;


public class InvGateway implements InvDao{
	
	private Connection connection;
	private Date lastReadTS;
	private int logRecordCount = 0;
	
	public InvGateway(AppPreferences AP){
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
			this.connection = DriverManager.getConnection(AP.getUrl(),AP.getUsername(), AP.getPassword());
			System.out.println("DB connection for InvGateway successful.");
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	public ResultSet doRead(String locFilter){
		String callString = "{call read_InvModule(?)}";
		ResultSet rs = null;
		
		try {
			CallableStatement callstate = this.connection.prepareCall(callString);
			callstate.setString(1, locFilter);
			rs = callstate.executeQuery();
			
			setLastUpdateTS();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return rs;
	}
	
	public ResultSet doReadExt(String locFilter){
		String callString = "{call read_InvModuleJOIN5(?)}";
		ResultSet rs = null;
		
		try {
			CallableStatement callstate = this.connection.prepareCall(callString);
			callstate.setString(1, locFilter);
			rs = callstate.executeQuery();
			
			setLastUpdateTS();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return rs;
	}
	
	public void doDelete(InvItem inv){
		String callString = "{call delete_Inv(?,?,?,?,?,?)}";

		try {
			CallableStatement callstate = this.connection.prepareCall(callString);
			callstate.setInt(1,inv.getInvID());
			callstate.setInt(2,inv.getInvPartID());
			callstate.setInt(3,inv.getInvProductID());
			callstate.setString(4,inv.getInvLocation());
			callstate.setInt(5,inv.getInvQuantity());
			callstate.setBoolean(6, false);
			callstate.executeUpdate();
			
			System.out.println("Record with invID " + inv.getInvID() + " deleted from InventoryModule table.");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void doAdd(InvItem inv){
		if(!doCheckDuplicate(inv) && doCheckAdd(inv)){
			String callString = "{call add_Inventory5(?,?,?,?)}";

			try {
				CallableStatement callstate = this.connection.prepareCall(callString);
				callstate.setInt(1, inv.getInvPartID());
				callstate.setInt(2, inv.getInvProductID());
				callstate.setString(3, inv.getInvLocation());
				callstate.setInt(4, inv.getInvQuantity());
				
				callstate.executeUpdate();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	
	public void doUpdate(InvItem inv){
		String callString = "{call update_InvByID5(?,?,?,?,?)}";
		System.out.println("Update: " + inv.getInvID()+","+inv.getInvPartID()+","+inv.getInvProductID()+","+inv.getInvQuantity()+","+inv.getInvLocation());
		try {
			CallableStatement callstate = this.connection.prepareCall(callString);
			callstate.setInt(1, inv.getInvID());
			callstate.setInt(2, inv.getInvPartID());
			callstate.setInt(3, inv.getInvProductID());
			callstate.setInt(4, inv.getInvQuantity());
			callstate.setString(5, inv.getInvLocation());
			
			callstate.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public boolean doCheckAdd(InvItem inv){
		String callString = "{call add_InventoryCheck(?,?,?,?,?)}";
		Boolean pass = false;
		try {
			CallableStatement callstate = this.connection.prepareCall(callString);
			callstate.setInt(1, inv.getInvPartID());
			callstate.setInt(2, inv.getInvProductID());
			callstate.setString(3, inv.getInvLocation());
			callstate.setInt(4, inv.getInvQuantity());
			callstate.registerOutParameter(5, Types.BOOLEAN);
			callstate.executeQuery();
			
			pass = callstate.getBoolean(5);
			//System.out.println("Enough Parts: " + enoughParts);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		return pass;
	}
		
	public boolean doCheckDuplicate(InvItem inv){
		String callString = "{call chkDup_InvByPartLoc5(?,?,?)}";
		
		try {
			CallableStatement callstate = this.connection.prepareCall(callString);
			callstate.setInt(1, inv.getInvPartID());
			callstate.setInt(2, inv.getInvProductID());
			callstate.setString(3, inv.getInvLocation());
			ResultSet rs = callstate.executeQuery();
			
			while(rs.next()){
				//System.out.println("Duplicate Entry! Ignorning update.");
				return true;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		//No duplicates found
		return false;
	}
	
	public ArrayList<InvItem> doGetInvList(String locFilter){
		ResultSet rs = doRead(locFilter);
		
		System.out.println("Inventory List Updated");
		ArrayList<InvItem> invList = new ArrayList<InvItem>();
		try {
			while(rs.next()){
				InvItem inv = new InvItem();
				inv.setInvID(rs.getInt("invID"));
				inv.setInvPartID(rs.getInt("invPartID"));
				inv.setInvProductID(rs.getInt("invProductID"));
				inv.setInvQuantity(rs.getInt("invQuantity"));
				inv.setInvLocation(rs.getString("invLocation"));
				invList.add(inv);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return invList;
	}
	
	public InvItem doGetInvRec(InvItem inv){
		String callString = "{call get_InvModuleRec(?)}";
		ResultSet rs = null;
		InvItem updatedInv = new InvItem();
		
		try {
			CallableStatement callstate = this.connection.prepareCall(callString);
			callstate.setInt(1, inv.getInvID());
			rs = callstate.executeQuery();
			
			while(rs.next()){
				updatedInv.setInvID(rs.getInt("invID"));
				updatedInv.setInvPartID(rs.getInt("invPartID"));
				updatedInv.setInvProductID(rs.getInt("invProductID"));
				updatedInv.setInvQuantity(rs.getInt("invQuantity"));
				updatedInv.setInvLocation(rs.getString("invLocation"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return updatedInv;
	}
	
	public ArrayList<InvItemExt> doGetInvListExt(String locFilter){
		ResultSet rs = doReadExt(locFilter);
		
		System.out.println("Inventory List Updated");
		ArrayList<InvItemExt> invListExt = new ArrayList<InvItemExt>();
		try {
			while(rs.next()){
				InvItemExt invExt = new InvItemExt();
				invExt.setInvID(rs.getInt("invID"));
				invExt.setInvPartID(rs.getInt("invPartID"));
				invExt.setInvProductID(rs.getInt("invProductID"));
				invExt.setInvQuantity(rs.getInt("invQuantity"));
				invExt.setInvLocation(rs.getString("invLocation"));
				
				invExt.setPartName(rs.getString("partName"));
				invExt.setPartNo(rs.getString("partNo"));
				invExt.setPartNoExternal(rs.getString("partNoExternal"));
				invExt.setPartVendor(rs.getString("partVendor"));
				invExt.setPartQuantityUnit(rs.getString("partQuantityUnit"));

				invExt.setProductNo(rs.getString("productNo"));
				invExt.setProductDesc(rs.getString("productDesc"));
				
				invListExt.add(invExt);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return invListExt;
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
			System.out.println("Inventory Table synced: " + lastReadTS);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public Date getInvItemLastUpdateTS(InvItem inv){
		String callString = "{call get_InvItemLastUpdateTS(?)}";
		ResultSet rs = null;
		Date TimeStamp = null;
		try {
			TimeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("0000-00-00 00:00:00");
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		try {
			CallableStatement callstate = this.connection.prepareCall(callString);
			callstate.setInt(1, inv.getInvID());
			rs = callstate.executeQuery();
            
			while (rs.next())
            {
				TimeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(rs.getString("log_updateTimeStamp"));
				break;
            }
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return TimeStamp;
	}
	
	public boolean doPollChanges(){
		String query = "SELECT log_createTimeStamp,log_updateTimeStamp FROM InventoryModuleLog";
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
		
		return false; //no changes to inventory module table found
		
	}
	
	public void doClose(){
		try {
			connection.close();
			System.out.println("DB connection for InvGateway has been closed.");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}