/**
 * 
 */
package core.productTemplatesModule.controllers;
/**
 * @author hgv265
 *
 */
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import core.mdi.models.MasterFrame;
import core.productTemplatesModule.models.ProductPartsTableModel;
import core.productTemplatesModule.models.obj.ProductPartItem;
import core.productTemplatesModule.views.ProductPartsDetailView;

public class ProductPartsDetailController implements ActionListener{
	public static final String UPDATE_PRODUCT_PART_COMMAND = "UPDATE_PRODUCT_PART_COMMAND";
	public static final String DELETE_PRODUCT_PART_COMMAND = "DELETE_PRODUCT_PART_COMMAND";
	
	private ProductPartsDetailView view;
	private ProductPartsTableModel tableModel;
	boolean changedVital;
	private MasterFrame m;
    
    public ProductPartsDetailController(ProductPartsDetailView view, ProductPartsTableModel tableModel, MasterFrame m){
    	this.m = m;
    	this.view = view;
		this.tableModel = tableModel;
		initView();
    }
    
    public void initView(){        
    	view.addButtonListener(this);
    }
    
    @Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		switch (e.getActionCommand()) {
		case UPDATE_PRODUCT_PART_COMMAND:
			ProductPartItem productPartUpdated = getTextInput();
			boolean validate = productPartUpdated.validate(m);
			
			if(validate && !changedVital){
				tableModel.updateRow(productPartUpdated);
				view.getFrame().dispose();
			} else {
				boolean dupCheck = tableModel.checkForDuplicate(productPartUpdated);
				if(validate && !dupCheck){
					tableModel.updateRow(productPartUpdated);
					view.getFrame().dispose();
				}
				if(dupCheck && changedVital){
					m.displayChildMessage("A duplicate record already exists with this Product/Part combination, please modify before submitting.");
				}
			}
			
			break;
		case DELETE_PRODUCT_PART_COMMAND:
			if(promptDelete(view.getProductPart())){
				tableModel.removeRow(getTextInput());
				view.getFrame().dispose();
			}
			break;
		default : break;
		}
		
	}
    
    private ProductPartItem getTextInput(){    	
    	String pPtpID = Integer.toString(view.getProductPart().getPtpID());
    	String pProductID = Integer.toString(view.getProductPart().getProductID());
    	String pPartID = Integer.toString(view.getSelectedPartID());
    	String pQuantity = (view.getpQuantity().getText().matches("\\d+") && (Integer.parseInt(view.getpQuantity().getText())) > 0) ? view.getpQuantity().getText() : "-1";
 
    	ProductPartItem updatedProductPart = new ProductPartItem();
    	updatedProductPart.setPtpID(Integer.parseInt(pPtpID));
    	updatedProductPart.setProductID(Integer.parseInt(pProductID));
    	updatedProductPart.setPartID(Integer.parseInt(pPartID));
    	updatedProductPart.setQuantity(Integer.parseInt(pQuantity));
    	
    	if(view.getProductPart().getPartID() == updatedProductPart.getPartID()){
    		changedVital = false;
    	} else{
    		changedVital = true;
    	}
    	
    	return updatedProductPart;
    }
    
    public boolean promptDelete(ProductPartItem productPart){
		String titleBar = "Are you sure?";
		String infoMsg = "Are you sure you would like to delete this product part? This action cannot be undone.";

		return m.displayChildMessageOption(titleBar, infoMsg);
	}
	
}