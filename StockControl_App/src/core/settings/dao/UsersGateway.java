/**
 * 
 */
package core.settings.dao;

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

import core.settings.interfaces.UsersDao;
import core.settings.models.AppPreferences;
import core.settings.models.Function;
import core.settings.models.Role;
import core.settings.models.User;

/**
 * @author zachary.rodriguez
 *
 */
public class UsersGateway implements UsersDao {
	
	private Connection connection;
	private Date lastReadTS;
	private int logRecordCount = 0;
	private AppPreferences AP = new AppPreferences();
	
	public UsersGateway(){
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
			System.out.println("DB connection for UsersGateway successful.");
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	@Override
	public ResultSet doRead() {
		String callString = "{call read_Users()}";
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

	@Override
	public void doDelete(User user) {
		String callString = "{call delete_UserByID(?)}";
		
		try {
			CallableStatement callstate = this.connection.prepareCall(callString);
			callstate.setInt(1,user.getUserID());
			callstate.executeUpdate();
			
			System.out.println("Record with userID " + user.getUserID() + " deleted from Users table.");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void doAdd(User user) {
		if(!doCheckDuplicateByEmail(user)){
			String callString = "{call add_User(?,?,?)}";

			try {
				CallableStatement callstate = this.connection.prepareCall(callString);
				callstate.setString(1, user.getFullName());
				callstate.setString(2, user.getEmail());
				callstate.setString(3, user.getPassword());
				
				callstate.executeUpdate();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	public void doUpdate(User user) {
		String callString = "{call update_UserByID(?,?,?)}";
		
		try {
			CallableStatement callstate = this.connection.prepareCall(callString);
			callstate.setInt(1, user.getUserID());
			callstate.setString(2, user.getFullName());
			callstate.setString(3, user.getEmail());
			callstate.setString(4, user.getPassword());
			
			callstate.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public boolean doCheckDuplicateByEmail(User user) {
		String callString = "{call chkDup_UserByEmail(?)}";
		
		try {
			CallableStatement callstate = this.connection.prepareCall(callString);
			callstate.setString(1, user.getEmail());
			ResultSet rs = callstate.executeQuery();
			
			while(rs.next()){
				//System.out.println("Duplicate Entry Product No!");
				return true;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		//No duplicates found
		return false;
	}

	@Override
	public ArrayList<User> doGetUserList() {
		ResultSet rs = doRead();

		System.out.println("User List Updated");
		ArrayList<User> userList = new ArrayList<User>();
		try {
			while(rs.next()){
				User user = new User();
				user.setUserID(rs.getInt("userID"));
				user.setFullName(rs.getString("userFullName"));
				user.setEmail(rs.getString("userEmail"));
				userList.add(user);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return userList;
	}
	
	@Override
	public User doGetUserByEmail(String email) {
		String callString = "{call get_UserByEmail(?)}";
		User user = new User();
		
		try {
			CallableStatement callstate = this.connection.prepareCall(callString);
			callstate.setString(1, email);
			ResultSet rs = callstate.executeQuery();
			
			while(rs.next()){
				user.setUserID(rs.getInt("userID"));
				user.setFullName(rs.getString("userFullName"));
				user.setEmail(rs.getString("userEmail"));
				user.setPassword(rs.getString("userPassword"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		//No duplicates found
		return user;
	}

	@Override
	public Role doGetUserRole(User user) {
		String callString = "{call get_UserRole(?)}";
		Role userRole = new Role();
		
		try {
			CallableStatement callstate = this.connection.prepareCall(callString);
			callstate.setInt(1, user.getUserID());
			ResultSet rs = callstate.executeQuery();
			
			while(rs.next()){
				userRole.setRoleID(rs.getInt("roleID"));
				userRole.setRoleName(rs.getString("roleName"));
				userRole.setRoleDesc(rs.getString("roleDesc"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		//No duplicates found
		return userRole;
	}

	@Override
	public ArrayList<Function> doGetRoleFunctionList(Role role) {
		String callString = "{call get_RoleFunctions(?)}";
		ArrayList<Function> functionList = new ArrayList<Function>();
		
		try {
			CallableStatement callstate = this.connection.prepareCall(callString);
			callstate.setInt(1, role.getRoleID());
			ResultSet rs = callstate.executeQuery();
			
			while(rs.next()){
				Function function = new Function();
				function.setFunctionID(rs.getInt("functionID"));
				function.setFunctionName(rs.getString("functionName"));
				function.setFunctionDesc(rs.getString("functionDesc"));
				functionList.add(function);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return functionList;
	}
	
	@Override
	public ArrayList<Function> doGetFunctionList() {
		String callString = "{call get_Functions()}";
		ArrayList<Function> functionList = new ArrayList<Function>();
		
		try {
			CallableStatement callstate = this.connection.prepareCall(callString);
			ResultSet rs = callstate.executeQuery();
			
			while(rs.next()){
				Function function = new Function();
				function.setFunctionID(rs.getInt("functionID"));
				function.setFunctionName(rs.getString("functionName"));
				function.setFunctionDesc(rs.getString("functionDesc"));
				functionList.add(function);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return functionList;
	}
	
	@Override
	public void setLastUpdateTS() {
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
			System.out.println("Users Table synced: " + lastReadTS);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Override
	public boolean doPollChanges() {
		String query = "SELECT log_createTimeStamp,log_updateTimeStamp FROM UsersLog";
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
		
		return false; //no changes to users table found
	}

	@Override
	public void doClose() {
		try {
			connection.close();
			System.out.println("DB connection for UsersGateway has been closed.");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
