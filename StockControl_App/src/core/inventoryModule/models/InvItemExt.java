/**
 * 
 */
package core.inventoryModule.models;



/**
 * @author hgv265
 *
 */
public class InvItemExt extends InvItem{
	private String partName;
	private String partNo;
	private String partNoExternal;
	private String partVendor;
	private String partQuantityUnit;
	private String productNo;
	private String productDesc;
		
	public InvItemExt(){
    	setInvID(0);
    	setInvPartID(0);
    	setInvLocation("Unknown");
    	setInvQuantity(0);
    	
    	setPartName(null);
    	setPartNo(null);
    	setPartNoExternal(null);
    	setPartVendor(null);
    	setPartQuantityUnit("Unknown");
    	
    	setProductNo(null);
    	setProductDesc(null);
    	
    	return;
    }
    
    public InvItemExt(int invID, int invPartID, String invLocation, int invQuantity, String partName, String partNo, String partNoExternal, String partVendor, String partQuantityUnit, String productNo, String productDesc){
    	setInvID(invID);
    	setInvPartID(invPartID);
    	setInvLocation(invLocation);
    	setInvQuantity(invQuantity);
    	
    	setPartName(partName);
    	setPartNo(partNo);
    	setPartNoExternal(partNoExternal);
    	setPartVendor(partVendor);
    	setPartQuantityUnit(partQuantityUnit);
    	
    	setProductNo(productNo);
    	setProductDesc(productDesc);
    	
    	return;
    }
	
	public String getPartName() {
		return partName;
	}
	public void setPartName(String partName) {
		this.partName = partName;
	}
	public String getPartNo() {
		return partNo;
	}
	public void setPartNo(String partNo) {
		this.partNo = partNo;
	}
	public String getPartNoExternal() {
		return partNoExternal;
	}
	public void setPartNoExternal(String partNoExternal) {
		this.partNoExternal = partNoExternal;
	}
	public String getPartVendor() {
		return partVendor;
	}
	public void setPartVendor(String partVendor) {
		this.partVendor = partVendor;
	}
	public String getPartQuantityUnit() {
		return partQuantityUnit;
	}
	public void setPartQuantityUnit(String partQuantityUnit) {
		this.partQuantityUnit = partQuantityUnit;
	}

	public String getProductNo() {
		return productNo;
	}

	public void setProductNo(String productNo) {
		this.productNo = productNo;
	}

	public String getProductDesc() {
		return productDesc;
	}

	public void setProductDesc(String productDesc) {
		this.productDesc = productDesc;
	}
	
}
