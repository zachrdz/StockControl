package core.partsModule.models;
/**
 * 
 */
import javax.swing.JOptionPane;

import core.mdi.MasterFrame;

/**
 * @author hgv265
 *
 */
public class PartItem {

    private int partID;
    private String partNo;
    private String partNoExternal;
    private String partName;
    private String partVendor;
    private String partQuantityUnit;
    
    public PartItem(){
    	setPartID(0);
    	setPartNo(null);
    	setPartNoExternal(null);
    	setPartName(null);
    	setPartVendor(null);
    	setPartQuantityUnit("Unkown");
    	
    	return;
    }
    
    public PartItem(int partID, String partNo, String partNoExternal, String partName, String partVendor, String partQuantityUnit){
    	setPartID(partID);
    	setPartNo(partNo);
    	setPartNoExternal(partNoExternal);
    	setPartName(partName);
    	setPartVendor(partVendor);
    	setPartQuantityUnit(partQuantityUnit);
    	
    	return;
    }
    
	/**
	 * @return the partNo
	 */
	public String getPartNo() {
		return partNo;
	}

	/**
	 * @param partNo the partNo to set
	 */
	public void setPartNo(String partNo) {
		this.partNo = partNo;
	}
	
	/**
	 * @return the partNoExternal
	 */
	public String getPartNoExternal() {
		return partNoExternal;
	}

	/**
	 * @param partNoExternal the partNoExternal to set
	 */
	public void setPartNoExternal(String partNoExternal) {
		this.partNoExternal = partNoExternal;
	}

	/**
	 * @return the partName
	 */
	public String getPartName() {
		return partName;
	}

	/**
	 * @param partName the partName to set
	 */
	public void setPartName(String partName) {
		this.partName = partName;
	}

	/**
	 * @return the partVendor
	 */
	public String getPartVendor() {
		return partVendor;
	}

	/**
	 * @param partVendor the partVendor to set
	 */
	public void setPartVendor(String partVendor) {
		this.partVendor = partVendor;
	}
	

	/**
	 * @return the partID
	 */
	public int getPartID() {
		return partID;
	}

	/**
	 * @param partID the partID to set
	 */
	public void setPartID(int partID) {
		this.partID = partID;
	}
	
	public boolean validate(MasterFrame m){
		String titleBar = "Field Validation";
		
		String partNoRegex = "^[P]{1}[a-zA-Z0-9-!$%^&*()#_+|~=`{}\\[\\]:\";'<>?,.\\/]{1,19}$";
		String infoMsg1 = "[Required Field] The part number must start with \"P\" & contain only alphanumeric characters or symbols\nLength should be between 1-20. Please correct this before submitting.\n\n";
		
		String partNameRegex = "^[a-zA-Z0-9-!$%^&*()#_+|~=`{}\\[\\]:\";'<>?,.\\/]{1,255}$";
		String infoMsg2 = "[Required Field] The part name must contain only alphanumeric characters or symbols.\nLength should be between 1-255. Please correct this before submitting.\n\n";
		
		String partVendorRegex = "^[a-zA-Z0-9-!$%^&*()#_+|~=`{}\\[\\]:\";'<>?,.\\/]{0,255}$";
		String infoMsg3 = "[Optional Field] The part vendor must contain only alphanumeric characters or symbols.\nLength should be between 0-255. Please correct this before submitting.\n\n";
				
		String partQuantityUnitRegex = "\\b(?:Pieces|Linear Feet)\\b";
		String infoMsg4 = "[Required Field] The unit of quantity is not a valid value. Please correct this before submitting.\n\n";
		
		String partNoExternalRegex = "^[a-zA-Z0-9-!$%^&*()#_+|~=`{}\\[\\]:\";'<>?,.\\/]{0,50}$";
		String infoMsg5 = "[Optional Field] The external part number must contain only alphanumeric characters or symbols.\nLength should be between 0-50. Please correct this before submitting.\n";
	
		StringBuilder finalMsg = new StringBuilder();
		boolean fail = false;
		
		if(!partNo.matches(partNoRegex)){
			finalMsg.append(infoMsg1);
			fail = true;
		}
		
		if(!partName.matches(partNameRegex)){
			finalMsg.append(infoMsg2);
			fail = true;
		}
		
		if(!partVendor.matches(partVendorRegex)){
			finalMsg.append(infoMsg3);
			fail = true;
		}
		
		if(!partQuantityUnit.matches(partQuantityUnitRegex)){
			finalMsg.append(infoMsg4);
			fail = true;
		}
		
		if(!partNoExternal.matches(partNoExternalRegex)){
			finalMsg.append(infoMsg5);
		}
		
		if(fail){
			JOptionPane.showInternalMessageDialog(m.getContentPane(), finalMsg, "Alert: " + titleBar, JOptionPane.INFORMATION_MESSAGE);
			return false;
		}
		
		return true;
	}
	
	public String getPartQuantityUnit() {
		return partQuantityUnit;
	}

	public void setPartQuantityUnit(String partQuantityUnit) {
		this.partQuantityUnit = partQuantityUnit;
	}

	@Override
	public String toString() {
		return "PartItem [partID=" + partID + ", partNo=" + partNo
				+ ", partNoExternal=" + partNoExternal + ", partName="
				+ partName + ", partVendor=" + partVendor
				+ ", partQuantityUnit=" + partQuantityUnit + "]";
	}

	
}