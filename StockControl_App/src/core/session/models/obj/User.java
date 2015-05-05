/**
 * 
 */
package core.session.models.obj;

import java.io.Serializable;

/**
 * @author zachary.rodriguez
 *
 */
public class User implements Serializable{
	private static final long serialVersionUID = 1L;
	private int userID;
	private String fullName;
	private String email;
	private String password;
	
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}

	public int getUserID() {
		return userID;
	}
	public void setUserID(int userID) {
		this.userID = userID;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
}
