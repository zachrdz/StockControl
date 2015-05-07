package core.inventoryModule.models.obj;

import java.io.Serializable;

public class InvItemLogRecord implements Serializable{
	private static final long serialVersionUID = 1L;
	private int invID;
	private String invEntryDate;
	private String invEntryDesc;
	
	public int getInvID() {
		return invID;
	}
	public void setInvID(int invID) {
		this.invID = invID;
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
	@Override
	public String toString() {
		return invEntryDate + " - " + invEntryDesc;
	}
}
