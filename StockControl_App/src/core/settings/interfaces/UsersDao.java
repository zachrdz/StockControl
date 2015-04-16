/**
 * 
 */
package core.settings.interfaces;

import java.sql.ResultSet;
import java.util.ArrayList;

import core.settings.models.Function;
import core.settings.models.Role;
import core.settings.models.User;

/**
 * @author Zach
 *
 */
public interface UsersDao {
	public ResultSet doRead();
	public void doDelete(User user);
	public void doAdd(User user);
	public void doUpdate(User user);
	public boolean doCheckDuplicateByEmail(User user);
	public ArrayList<User> doGetUserList();
	public User doGetUserByEmail(String email);
	public Role doGetUserRole(User user);
	public ArrayList<Function> doGetRoleFunctionList(Role role);
	public ArrayList<Function> doGetFunctionList();
	public void setLastUpdateTS();
	public boolean doPollChanges();
	public void doClose();
	
}
