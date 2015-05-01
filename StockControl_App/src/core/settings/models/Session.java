/**
 * 
 */
package core.settings.models;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author zachary.rodriguez
 *
 */
public class Session implements Serializable{
	private static final long serialVersionUID = 1L;
	private User user;
	private Role userRole;
	private ArrayList<Function> userFunctions;
	private ArrayList<Function> allFunctions;
	
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	
	public Role getUserRole() {
		return userRole;
	}
	
	public void setUserRole(Role userRole) {
		this.userRole = userRole;
	}

	public ArrayList<Function> getUserFunctions() {
		return userFunctions;
	}

	public void setUserFunctions(ArrayList<Function> userFunctions) {
		this.userFunctions = userFunctions;
	}	
	
	public ArrayList<Function> getAllFunctions() {
		return allFunctions;
	}

	public void setAllFunctions(ArrayList<Function> allFunctions) {
		this.allFunctions = allFunctions;
	}
}
