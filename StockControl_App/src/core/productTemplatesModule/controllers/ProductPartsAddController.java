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

import core.mdi.MasterFrame;
import core.productTemplatesModule.models.ProductPartItem;
import core.productTemplatesModule.models.ProductPartsTableModel;
import core.productTemplatesModule.views.ProductPartsAddView;

public class ProductPartsAddController implements ActionListener{
	public static final String ADD_PRODUCT_PART_COMMAND = "ADD_PRODUCT_PART_COMMAND";
	public static final String CANCEL_COMMAND = "CANCEL_COMMAND";
	
	private ProductPartsAddView view;
	private ProductPartsTableModel tableModel;
	private MasterFrame m;
    
    public ProductPartsAddController(ProductPartsAddView view, ProductPartsTableModel tableModel, MasterFrame m){
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
		case ADD_PRODUCT_PART_COMMAND:
			ProductPartItem productPartAdd = getTextInput();
			boolean dupCheck = tableModel.checkForDuplicate(productPartAdd);
			
			if(productPartAdd.validate(m) && !dupCheck){
				tableModel.addRow(productPartAdd);
				view.getFrame().dispose();
			}
			if(dupCheck){
				m.displayChildMessage("A duplicate record already exists with this information, please modify before submitting.");
			}
			break;
		case CANCEL_COMMAND:
			view.getFrame().dispose();
			break;
		default : break;
		}
	}
    
    private ProductPartItem getTextInput(){    	
    	String pProductID = Integer.toString(tableModel.getGlobalProductID());
    	String pPartID = Integer.toString(view.getSelectedPartID());
    	String pQuantity = (view.getpQuantity().getText().matches("\\d+") && (Integer.parseInt(view.getpQuantity().getText())) > 0) ? view.getpQuantity().getText() : "-1";
 
    	ProductPartItem newProductPart = new ProductPartItem();
    	newProductPart.setProductID(Integer.parseInt(pProductID));
    	newProductPart.setPartID(Integer.parseInt(pPartID));
    	newProductPart.setQuantity(Integer.parseInt(pQuantity));

    	return newProductPart;
    }

	
}