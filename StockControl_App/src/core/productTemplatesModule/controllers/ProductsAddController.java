/**
 * 
 */
package core.productTemplatesModule.controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import core.mdi.models.MasterFrame;
import core.productTemplatesModule.models.ProductsTableModel;
import core.productTemplatesModule.models.obj.ProductItem;
import core.productTemplatesModule.views.ProductsAddView;

/**
 * @author Zach
 *
 */
public class ProductsAddController implements ActionListener{
	public static final String ADD_PRODUCT_COMMAND = "ADD_PRODUCT_COMMAND";
	public static final String CANCEL_COMMAND = "CANCEL_COMMAND";
	
	private ProductsAddView view;
	private ProductsTableModel tableModel;
	private MasterFrame m;
	
	public ProductsAddController(ProductsAddView view, MasterFrame m){
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
		case ADD_PRODUCT_COMMAND:
			ProductItem productAdd = getTextInput();
			boolean dupCheck = tableModel.checkForDuplicate(productAdd);
			
			if(productAdd.validate(m) && !dupCheck){
				tableModel.addRow(productAdd);
				view.getFrame().dispose();
			}
			if(dupCheck){
				m.displayChildMessage("A duplicate record already exists with this product number, please modify before submitting.");
			}
			break;
		case CANCEL_COMMAND:
			view.getFrame().dispose();
			break;
		default : break;
		}
	}
    
    private ProductItem getTextInput(){    	
    	String pNo = view.getpNo().getText();
    	String pDesc = view.getpDesc().getText();
    	
    	ProductItem newProduct = new ProductItem();
    	newProduct.setProductNo(pNo);
    	newProduct.setProductDesc(pDesc);
    	
    	return newProduct;
    }
	
}
