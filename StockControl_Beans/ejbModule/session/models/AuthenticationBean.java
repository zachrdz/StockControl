/**
 * 
 */
package session.models;

import java.util.ArrayList;

import session.dao.UsersGateway;

import javax.ejb.Stateless;

/**
 * @author Zach
 *
 */

@Stateless(name="AuthenticationBean")
public class AuthenticationBean {
	private UsersGateway gateway;
	
	public AuthenticationBean(){
		gateway = new UsersGateway();
	}
	
	public Session authenticateUser(String email, String password) {
	    Session session = new Session();
		
		try {
	    	User user = gateway.doGetUserByEmail(email);
	    	
	        if (null != user && password == gateway.doGetUserLogonByID(user)) {
        		Role role = gateway.doGetUserRole(user);
	        	ArrayList<Function> userFunctions = gateway.doGetRoleFunctionList(role);
	        	
	        	session.setUser(user);
	        	session.setUserRole(role);
	        	session.setUserFunctions(userFunctions);

	    		return session;
	        }
	    } catch (Exception e) {
	    	e.printStackTrace();
	    }
	    
		System.out.println("Invalid User Credentials!");
	    return null;
	}
}
