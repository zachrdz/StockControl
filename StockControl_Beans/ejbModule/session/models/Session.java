package session.models;

import java.util.ArrayList;

public class Session{
	private User user;
	private Role userRole;
	private ArrayList<Function> userFunctions;
	
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
}