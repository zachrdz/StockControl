/**
 * 
 */
package core.settings.models;

import java.util.ArrayList;
import core.settings.dao.UsersGateway;

/**
 * @author Zach
 *
 */
public class AuthenticatorModel {
	private UsersGateway gateway;
	
	public AuthenticatorModel(){
		gateway = new UsersGateway();
	}
	
	public Session authenticateUser(String email, String password) {
	    Session session = new Session();
		
		try {
	    	User user = gateway.doGetUserByEmail(email);

	        if (user.getPassword() != null && user.getPassword().equals(password)) {
        		Role role = gateway.doGetUserRole(user);
	        	ArrayList<Function> userFunctions = gateway.doGetRoleFunctionList(role);
	        	ArrayList<Function> allFunctions = gateway.doGetFunctionList();
	        	
	        	session.setUser(user);
	        	session.setUserRole(role);
	        	session.setUserFunctions(userFunctions);
	        	session.setAllFunctions(allFunctions);

	    		return session;
	        }
	    } catch (Exception e) {
	    	e.printStackTrace();
	    }
	    
		System.out.println("Invalid User Credentials!");
	    return null;
	}
}
