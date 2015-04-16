/**
 * 
 */
package core.productTemplatesModule.models;

import javax.swing.JOptionPane;

import core.mdi.MasterFrame;

/**
 * @author Zach
 *
 */
public class ProductPartItem{
	private int ptpID;
	private int productID;
	private int partID;
	private int quantity;
	
	public ProductPartItem(){
		ptpID = 0;
		productID = 0;
		partID = 0;
		quantity = 0;
		
		return;
	}
	
	public ProductPartItem(int ptpID, int productID, int partID, int quantity){
		this.ptpID = ptpID;
		this.productID = productID;
		this.partID = partID;
		this.quantity = quantity;
		
		return;
	}
	
	public int getPtpID(){
		return ptpID;
	}
	
	public void setPtpID(int ptpID){
		this.ptpID = ptpID;
	}
	
	public int getProductID() {
		return productID;
	}

	public void setProductID(int productID) {
		this.productID = productID;
	}

	public int getPartID() {
		return partID;
	}

	public void setPartID(int partID) {
		this.partID = partID;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	@Override
	public String toString() {
		return "ProductPartItem [ptpID=" + ptpID + ", productID=" + productID + ", partID=" + partID
				+ ", quantity=" + quantity + "]";
	}
	
	public boolean validate(MasterFrame m){
		String titleBar = "Field Validation";
		
		String infoMsg1 = "[Required Field] You must select an existing product to associate with this part item.\n";
		String infoMsg2 = "[Required Field] You must select an existing part to associate with this product item.\n";
		String infoMsg3 = "[Required Field] The quantity must contain positive numeric characters only. Please correct this before submitting.\n";

		StringBuilder finalMsg = new StringBuilder();
		boolean fail = false;
		
		if(productID < 0) { //Value is set to -1 when product id is not found to exist
			finalMsg.append(infoMsg1);
			fail = true;
		}
		
		if(partID < 0) { //Value is set to -1 when part id is not found to exist
			finalMsg.append(infoMsg2);
			fail = true;
		}
		
		if(quantity < 0) { //Value is set to -1 when a non-numeric character or negative number is passed in
			finalMsg.append(infoMsg3);
			fail = true;
		}
		
		if(fail){
			JOptionPane.showInternalMessageDialog(m.getContentPane(), finalMsg, "Alert: " + titleBar, JOptionPane.INFORMATION_MESSAGE);
			return false;
		}
		
		return true;
	}
}
