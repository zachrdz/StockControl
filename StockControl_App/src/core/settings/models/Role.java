/**
 * 
 */
package core.settings.models;

import java.io.Serializable;

/**
 * @author zachary.rodriguez
 *
 */
public class Role implements Serializable{
	private static final long serialVersionUID = 1L;
	private int roleID;
	private String roleName;
	private String roleDesc;
	
	public int getRoleID() {
		return roleID;
	}
	
	public void setRoleID(int roleID) {
		this.roleID = roleID;
	}
	
	public String getRoleName() {
		return roleName;
	}
	
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	
	public String getRoleDesc() {
		return roleDesc;
	}
	
	public void setRoleDesc(String roleDesc) {
		this.roleDesc = roleDesc;
	}
	
}
