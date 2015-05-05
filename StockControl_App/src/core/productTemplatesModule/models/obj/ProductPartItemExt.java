/**
 * 
 */
package core.productTemplatesModule.models.obj;


/**
 * @author Zach
 *
 */
public class ProductPartItemExt extends ProductPartItem{
	private String partName;
	private String partNo;
	private String partNoExternal;
	private String partVendor;
	private String partQuantityUnit;
	
	public ProductPartItemExt(){
    	setProductID(0);
    	setPartID(0);
    	setQuantity(0);
    	
    	setPartName(null);
    	setPartNo(null);
    	setPartNoExternal(null);
    	setPartVendor(null);
    	setPartQuantityUnit("Unknown");
    	
    	return;
    }
    
    public ProductPartItemExt(int productID, int partID, int quantity, String partName, String partNo, String partNoExternal, String partVendor, String partQuantityUnit){
    	setProductID(productID);
    	setPartID(partID);
    	setQuantity(quantity);
    	
    	setPartName(partName);
    	setPartNo(partNo);
    	setPartNoExternal(partNoExternal);
    	setPartVendor(partVendor);
    	setPartQuantityUnit(partQuantityUnit);
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
}
