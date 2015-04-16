/**
 * 
 */
package core.productTemplatesModule.controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import core.inventoryModule.models.InvItem;
import core.mdi.MasterFrame;
import core.productTemplatesModule.models.ProductItem;
import core.productTemplatesModule.models.ProductsTableModel;
import core.productTemplatesModule.views.ProductsDetailView;

/**
 * @author Zach
 *
 */
public class ProductsDetailController implements ActionListener{
	public static final String UPDATE_PRODUCT_COMMAND = "UPDATE_PRODUCT_COMMAND";
	public static final String DELETE_PRODUCT_COMMAND = "DELETE_PRODUCT_COMMAND";
	
	private ProductsDetailView view;
	private ProductsTableModel tableModel;
	private boolean changedVital;
	private MasterFrame m;
    
    public ProductsDetailController(ProductsDetailView view, MasterFrame m){
    	this.view = view;
    	this.m = m;
		this.tableModel = m.getProductsTableModel();
		initView();
    }
    
    public void initView(){        
    	view.addButtonListener(this);
    }
    
    @Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		switch (e.getActionCommand()) {
		case UPDATE_PRODUCT_COMMAND:
			ProductItem productUpdate = getTextInput();
			boolean validate = productUpdate.validate(m);
			
			if(!changedVital && validate){
				tableModel.updateRow(productUpdate);
				view.getFrame().dispose();
			} else if(validate){
				boolean dupCheck = tableModel.checkForDuplicate(productUpdate);
				if(!dupCheck){
					tableModel.updateRow(productUpdate);
					view.getFrame().dispose();
				}else {
					m.displayChildMessage("A duplicate record already exists with this information, please modify before submitting.");
				}
			}
			break;
		case DELETE_PRODUCT_COMMAND:
			Boolean noAssoc = true;
			for(InvItem invItem : m.getInvTableModel().getRows()){
				if(invItem.getInvProductID() == getTextInput().getProductID()){
					noAssoc = false;
					m.displayChildMessage("Cannot delete this Product Template because it is associated with and Inventory Item");
				}
			}
			if(noAssoc && promptDelete(view.getProduct())){
				tableModel.removeRow(getTextInput());
				view.getFrame().dispose();
			}
			break;
		default : break;
		}	
	}
    
    private ProductItem getTextInput(){    	
    	String pNo = view.getpNo().getText();
    	String pDesc = view.getpDesc().getText();
   
    	ProductItem updatedProduct = new ProductItem();
    	updatedProduct.setProductID(view.getProduct().getProductID());
    	updatedProduct.setProductNo(pNo);
    	updatedProduct.setProductDesc(pDesc);
    	
    	if(view.getProduct().getProductNo().equals(updatedProduct.getProductNo())){
    		changedVital = false;
    	} else{
    		changedVital = true;
    	}

    	return updatedProduct;
    }
    
    public boolean promptDelete(ProductItem product){
    	String titleBar = "Are you sure?";
		String infoMsg = "Are you sure you would like to delete " + product.getProductNo() + "? This action cannot be undone.";

		return m.displayChildMessageOption(titleBar, infoMsg);
	}
}
