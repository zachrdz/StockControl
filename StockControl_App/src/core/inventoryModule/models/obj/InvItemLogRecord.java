package core.inventoryModule.models.obj;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class InvItemLogRecord implements Serializable{
	private static final long serialVersionUID = 1L;
	private int invID;
	private Date invEntryDate;
	private String invEntryDesc;
	
	public int getInvID() {
		return invID;
	}
	public void setInvID(int invID) {
		this.invID = invID;
	}
	public Date getInvEntryDate() {
		return invEntryDate;
	}
	public void setInvEntryDate(Date invEntryDate) {
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
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		return dateFormat.format(invEntryDate) + " - " + invEntryDesc;
	}
}
