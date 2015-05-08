/**
 * 
 */
package core.inventoryModule.models.obj;

import java.util.ArrayList;

import javax.swing.JOptionPane;

import core.inventoryModule.models.InvTableModel;
import core.mdi.models.MasterFrame;

/**
 * @author Zach
 *
 */
public class InvItem {
	private int invID;
    private int invPartID;
    private int invProductID;
    private String invLocation;
    private int invQuantity;
    
    public InvItem(){
    	setInvID(0);
    	setInvPartID(0);
    	setInvProductID(0);
    	setInvLocation("Unknown");
    	setInvQuantity(0);
    	
    	return;
    }
    
    public InvItem(int invID, int invPartID, String invLocation, int invQuantity){
    	setInvID(invID);
    	setInvPartID(invPartID);
    	setInvLocation(invLocation);
    	setInvQuantity(invQuantity);
    	
    	return;
    }

	public int getInvID() {
		return invID;
	}

	public void setInvID(int invID) {
		this.invID = invID;
	}

	public int getInvPartID() {
		return invPartID;
	}

	public void setInvPartID(int invPartID) {
		this.invPartID = invPartID;
	}

	public String getInvLocation() {
		return invLocation;
	}

	public void setInvLocation(String invLocation) {
		this.invLocation = invLocation;
	}

	public int getInvQuantity() {
		return invQuantity;
	}

	public void setInvQuantity(int invQuantity) {
		this.invQuantity = invQuantity;
	}

	@Override
	public String toString() {
		return "InvItem [invID=" + invID + ", invPartID=" + invPartID
				+ ", invLocation=" + invLocation + ", invQuantity="
				+ invQuantity + "]";
	}
	
	public boolean validate(MasterFrame m){
		String titleBar = "Field Validation";
		
		String infoMsg1 = "[Required Field] You must select an existing part or product to associate with this inventory item.\n";
		
		String infoMsg2 = "[Required Field] The quantity must contain positive numeric characters only. Please correct this before submitting.\n";

		String invLocationRegex = "\\b(?:Facility 1 Warehouse 1|Facility 1 Warehouse 2|Facility 2)\\b";
		String infoMsg3 = "[Required Field] The location is not a valid value. Please correct this before submitting.\n";
		
		String infoMsg4 = "[Required Field] You must only select a part or a product, not both. Please correct this before submitting.\n";
		
		StringBuilder finalMsg = new StringBuilder();
		boolean fail = false;
		
		if(invPartID == 0 && invProductID == 0) { //Value is set to -1 when part id or product id is not found to exist
			finalMsg.append(infoMsg1);
			fail = true;
		}
		
		if(invQuantity < 0) { //Value is set to -1 when a non-numeric character or negative number is passed in
			finalMsg.append(infoMsg2);
			fail = true;
		}
		
		if(!invLocation.matches(invLocationRegex)){
			finalMsg.append(infoMsg3);
			fail = true;
		}
		
		if(invPartID != 0 && invProductID != 0) { //Value is set to -1 when part id or product id is not found to exist
			finalMsg.append(infoMsg4);
			fail = true;
		}
		
		if(fail){
			JOptionPane.showInternalMessageDialog(m.getContentPane(), finalMsg, "Alert: " + titleBar, JOptionPane.INFORMATION_MESSAGE);
			return false;
		}
		
		return true;
	}

	public int getInvProductID() {
		return invProductID;
	}

	public void setInvProductID(int invProductID) {
		this.invProductID = invProductID;
	}
	
	public ArrayList<String> getLog(InvTableModel invTableModel) {
		ArrayList<InvItemLogRecord> log = invTableModel.getInvItemLog(this.invID);
		ArrayList<String> tmp = new ArrayList<String>();
		
		if(null != log){
			for(InvItemLogRecord rec : log){
				tmp.add(rec.toString());
			}
		}
		
		return tmp;
	}
	
}
