package core.inventoryModule.controllers;
/**
 * 
 */

/**
 * @author hgv265
 *
 */
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import core.inventoryModule.models.InvTableModel;
import core.inventoryModule.models.obj.InvItem;
import core.inventoryModule.views.InvAddView;
import core.mdi.models.MasterFrame;
import core.session.models.obj.Function;

public class InvAddController implements ActionListener{
	public static final String ADD_INV_COMMAND = "ADD_INV_COMMAND";
	public static final String CANCEL_COMMAND = "CANCEL_COMMAND";
	
	private InvAddView view;
	private InvTableModel tableModel;
	private MasterFrame m;
    
    public InvAddController(InvAddView view, MasterFrame m){
    	this.view = view;
    	this.m = m;
		this.tableModel = m.getInvTableModel();
		initView();
    }
    
    public void initView(){        
    	view.addButtonListener(this);
        setRoleRestrictions();
    }
    
    private void setRoleRestrictions(){
		if(!m.getSession().getUserFunctions().contains(new Function("canAddInventory"))){
			view.getAddBtn().setEnabled(false);
		}
	}
    
    @Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		switch (e.getActionCommand()) {
		case ADD_INV_COMMAND:
			InvItem invAdd = getTextInput();
			boolean dupCheck = tableModel.checkForDuplicate(invAdd);
			boolean canAddCheck = tableModel.checkCanAdd(invAdd);
			
			if(invAdd.validate(m) && !dupCheck && canAddCheck){
				tableModel.addRow(invAdd);
				view.getFrame().dispose();
			}
			if(dupCheck){
				m.displayChildMessage("A duplicate record already exists with this information, please modify before submitting.");
			}
			if(!canAddCheck){
				m.displayChildMessage("This Product can NOT be added. There is not enough parts at " + invAdd.getInvLocation() + " to fulfill the request.");
			}
			break;
		case CANCEL_COMMAND:
			view.getFrame().dispose();
			break;
		default : break;
		}
	}
    
    private InvItem getTextInput(){    	
    	String iPartID = Integer.toString(view.getSelectedPartID());
    	String iProductID = Integer.toString(view.getSelectedProductID());
    	String iLocation = (String) view.getiLocation().getSelectedItem();
    	String iQuantity = (view.getiQuantity().getText().matches("\\d+") && (Integer.parseInt(view.getiQuantity().getText())) > 0) ? view.getiQuantity().getText() : "-1";
 
    	InvItem newInv = new InvItem();
    	newInv.setInvPartID(Integer.parseInt(iPartID));
    	newInv.setInvProductID(Integer.parseInt(iProductID));
    	newInv.setInvLocation(iLocation);
    	newInv.setInvQuantity(Integer.parseInt(iQuantity));

    	return newInv;
    }

	
}