package session;

import java.util.ArrayList;

import javax.ejb.Remote;

@Remote
public interface SessionBeanRemote {
	public User getUser();
	public void setUser(User user);
	
	public Role getUserRole();
	public void setUserRole(Role userRole);
	
	public ArrayList<Function> getUserFunctions();
	public void setUserFunctions(ArrayList<Function> userFunctions);
}
