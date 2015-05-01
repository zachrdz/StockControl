/**
 * 
 */
package core.settings.dao;

import java.io.Serializable;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import core.settings.models.Function;
import core.settings.models.Role;
import core.settings.models.User;

/**
 * @author zachary.rodriguez
 *
 */
public class UsersGateway implements UsersDao, Serializable {

	private static final long serialVersionUID = 1L;
	private Connection connection;
	
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
			this.connection = DriverManager.getConnection("jdbc:mysql://coderoot.co:3306/CS4743","zjrodr0709", "2487Zach");
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
	public void doAdd(User user, char[] pwd) {
		if(!doCheckDuplicateByEmail(user)){
			String callString = "{call add_User(?,?,?)}";

			try {
				CallableStatement callstate = this.connection.prepareCall(callString);
				callstate.setString(1, user.getFullName());
				callstate.setString(2, user.getEmail());
				callstate.setString(3, String.valueOf(pwd));
				
				callstate.executeUpdate();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	public void doUpdate(User user, char[] pwd) {
		String callString = "{call update_UserByID(?,?,?,?)}";
		String pass = (pwd != null) ? String.valueOf(pwd) : doGetUserLogonByID(user);
		
		try {
			CallableStatement callstate = this.connection.prepareCall(callString);
			callstate.setInt(1, user.getUserID());
			callstate.setString(2, user.getFullName());
			callstate.setString(3, user.getEmail());
			callstate.setString(4, pass);
			
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
	public String doGetUserLogonByID(User user) {
		String callString = "{call get_UserLogonByID(?)}";
		String pwd = null;
		
		try {
			CallableStatement callstate = this.connection.prepareCall(callString);
			callstate.setInt(1, user.getUserID());
			ResultSet rs = callstate.executeQuery();
			
			while(rs.next()){
				pwd = rs.getString("userPassword");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		//No duplicates found
		return pwd;
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
