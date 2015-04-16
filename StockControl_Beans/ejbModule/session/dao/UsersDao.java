/**
 * 
 */
package session.dao;

import java.sql.ResultSet;
import java.util.ArrayList;

import session.models.Function;
import session.models.Role;
import session.models.User;

/**
 * @author Zach
 *
 */
public interface UsersDao {
	public ResultSet doRead();
	public void doDelete(User user);
	public void doAdd(User user, char[] pwd);
	public void doUpdate(User user, char[] pwd);
	public boolean doCheckDuplicateByEmail(User user);
	public ArrayList<User> doGetUserList();
	public User doGetUserByEmail(String email);
	public Role doGetUserRole(User user);
	public ArrayList<Function> doGetRoleFunctionList(Role role);
	public ArrayList<Function> doGetFunctionList();
	public String doGetUserLogonByID(User user);
	public void doClose();
	
}
