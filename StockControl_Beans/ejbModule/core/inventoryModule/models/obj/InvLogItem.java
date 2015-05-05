package core.inventoryModule.models.obj;

import java.io.Serializable;

public class InvLogItem implements Serializable{
	private static final long serialVersionUID = 1L;
	private int invLogID;
	private String invEntryDate;
	private String invEntryDesc;
	
	public int getInvLogID() {
		return invLogID;
	}
	public void setInvLogID(int invLogID) {
		this.invLogID = invLogID;
	}
	public String getInvEntryDate() {
		return invEntryDate;
	}
	public void setInvEntryDate(String invEntryDate) {
		this.invEntryDate = invEntryDate;
	}
	public String getInvEntryDesc() {
		return invEntryDesc;
	}
	public void setInvEntryDesc(String invEntryDesc) {
		this.invEntryDesc = invEntryDesc;
	}
}
