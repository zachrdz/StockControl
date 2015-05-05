/**
 * 
 */
package core.session.models;

import java.io.Serializable;
import java.util.ArrayList;

import javax.ejb.Stateless;

import core.session.dao.UsersGateway;
import core.session.models.obj.Function;
import core.session.models.obj.Role;
import core.session.models.obj.Session;
import core.session.models.obj.User;
import core.session.remote.AuthenticationBeanRemote;

/**
 * @author Zach
 *
 */

@Stateless(name="AuthenticationBean")
public class AuthenticationBean implements AuthenticationBeanRemote, Serializable{
	private static final long serialVersionUID = 1L;
	private UsersGateway gateway;
	
	public AuthenticationBean(){
		gateway = new UsersGateway();
	}
	
	public Session authenticateUser(String email, String password) {
	    Session session = new Session();
		
		try {
	    	User user = gateway.doGetUserByEmail(email);

	        if (null != user && password.equals(gateway.doGetUserLogonByID(user))) {
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
