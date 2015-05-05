/**
 * 
 */
package core.productTemplatesModule.models.obj;

import javax.swing.JOptionPane;

import core.mdi.models.MasterFrame;

/**
 * @author Zach
 *
 */
public class ProductItem {
	private int productID;
    private String productNo;
    private String productDesc;
    
    public ProductItem(){
    	setProductID(0);
    	setProductNo("");
    	setProductDesc("");
    	
    	return;
    }
    
    public ProductItem(int productID, String productNo, String productDesc){
    	setProductID(productID);
    	setProductNo(productNo);
    	setProductDesc(productDesc);
    	
    	return;
    }

	public int getProductID() {
		return productID;
	}

	public void setProductID(int productID) {
		this.productID = productID;
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

	@Override
	public String toString() {
		return "ProductItem [productID=" + productID + ", productNo="
				+ productNo + ", prodDesc=" + productDesc + "]";
	}
	
	public boolean validate(MasterFrame m){	
		String titleBar = "Field Validation";
		
		String productNoRegex = "^[A]{1}[a-zA-Z0-9-!$%^&*()#_+|~=`{}\\[\\]:\";'<>?,.\\/]{1,19}$";
		String infoMsg1 = "[Required Field] The product number must contain only alphanumeric characters or symbols and start with \"A\".\nLength should be between 1-20. Please correct this before submitting.\n\n";
		
		String productDescRegex = "^[a-zA-Z0-9 -!$%^&*()#_+|~=`{}\\[\\]:\";'<>?,.\\/]{1,255}$";
		String infoMsg2 = "[Required Field] The product description must contain only alphanumeric characters, symbols, or spaces.\nLength should be between 1-255. Please correct this before submitting.\n";
	
		StringBuilder finalMsg = new StringBuilder();
		boolean fail = false;
		
		if(!productNo.matches(productNoRegex)){
			finalMsg.append(infoMsg1);
			fail = true;
		}
		
		if(!productDesc.matches(productDescRegex)){
			finalMsg.append(infoMsg2);
			fail = true;
		}
		
		if(fail){
			JOptionPane.showInternalMessageDialog(m.getContentPane(), finalMsg, "Alert: " + titleBar, JOptionPane.INFORMATION_MESSAGE);
			return false;
		}
		
		return true;
	}
}

