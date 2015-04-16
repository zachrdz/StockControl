/**
 * 
 */
package session;

/**
 * @author zachary.rodriguez
 *
 */
public class User {
	private int userID;
	private String fullName;
	private String email;
	
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
}
