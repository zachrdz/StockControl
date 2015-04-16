package core.inventoryModule.controllers;
/**
 * @author hgv265
 *
 */
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;

import core.inventoryModule.models.InvItem;
import core.inventoryModule.models.InvTableModel;
import core.inventoryModule.views.InvDetailView;
import core.mdi.MasterFrame;
import core.settings.models.Function;

public class InvDetailController implements ActionListener{
	public static final String UPDATE_INV_COMMAND = "UPDATE_INV_COMMAND";
	public static final String DELETE_INV_COMMAND = "DELETE_INV_COMMAND";
	
	private InvDetailView view;
	private InvTableModel tableModel;
	boolean changedVital;
	private Date invItemLastUpdated;
	private MasterFrame m;
    
    public InvDetailController(InvDetailView view, MasterFrame m){
    	this.view = view;
    	this.m = m;
		this.tableModel = m.getInvTableModel();
		invItemLastUpdated = tableModel.getInvItemLastUpdate(view.getInv());
		initView();
    }
    
    public void initView(){        
    	view.addButtonListener(this);
    	setRoleRestrictions();
    }
    
    private void setRoleRestrictions(){
		if(!m.getSession().getUserFunctions().contains(new Function("canAddInventory"))){
			view.getUpdateBtn().setEnabled(false);
		}
		if(!m.getSession().getUserFunctions().contains(new Function("canDeleteInventory"))){
			view.getDeleteBtn().setEnabled(false);
		}
	}
    
    @Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		switch (e.getActionCommand()) {
		case UPDATE_INV_COMMAND:
			InvItem invUpdated = getTextInput();
			
			if(updateVerify(invUpdated)){
				tableModel.updateRow(invUpdated);
				view.getFrame().dispose();
			}
			break;
		case DELETE_INV_COMMAND:
			boolean checkQuanEmpty;
			checkQuanEmpty = (view.getInv().getInvQuantity() > 0) ? false : true;
			if(checkQuanEmpty && promptDelete(view.getInv())){
				tableModel.removeRow(getTextInput());
				view.getFrame().dispose();
			} else{
				promptNotEmpty();
			}
			break;
		default : break;
		}
	}
    
    private InvItem getTextInput(){    	
    	String iPartID = view.getiPartID();
    	String iProductID = view.getiProductID();
    	String iLocation = (String) view.getiLocation().getSelectedItem();
    	String iQuantity = (view.getiQuantity().getText().matches("\\d+") && (Integer.parseInt(view.getiQuantity().getText())) >= 0) ? view.getiQuantity().getText() : "-1";
 
    	InvItem updatedInv = new InvItem();
    	updatedInv.setInvID(view.getInv().getInvID());
    	updatedInv.setInvPartID(Integer.parseInt(iPartID));
    	updatedInv.setInvProductID(Integer.parseInt(iProductID));
    	updatedInv.setInvLocation(iLocation);
    	updatedInv.setInvQuantity(Integer.parseInt(iQuantity));
    	
    	if(view.getInv().getInvLocation().equals(updatedInv.getInvLocation())){
    		changedVital = false;
    	} else{
    		changedVital = true;
    	}
    	
    	System.out.println("partid: " + iPartID + " prodID: " + iProductID);
    	
    	return updatedInv;
    }
    
    public boolean promptDelete(InvItem inv){
		String titleBar = "Are you sure?";
		String infoMsg = "Are you sure you would like to delete " + inv.getInvID() + "? This action cannot be undone.";

		return m.displayChildMessageOption(titleBar, infoMsg);
	}
    
    public void promptNotEmpty(){
    	m.displayChildMessage("Record cannot be deleted because the quantity is not 0.");
    	return;
    }
    
    public boolean updateVerify(InvItem invUpdated){
    	boolean validate = invUpdated.validate(m);
    	Object[] options = { "CANCEL","IGNORE Warning & Submit","Reload with New Data"};
		int choice = 0;
			
    	/*** Handle Part Updates ****/
    	if(invUpdated.getInvPartID() != 0){
    		if(validate && !changedVital){
    			if(invItemLastUpdated.compareTo(tableModel.getInvItemLastUpdate(view.getInv())) != 0){
    				choice = m.displayChildMessageOptionCustom("Warning","This record has been edited by someone else since you've last opened it!\nYou may cancel, ignore and submit your changes, or reload with new data.", options);
    			} else{
    				return true;
    			}
    		} else{
    			boolean dupCheck = tableModel.checkForDuplicate(invUpdated);
    			if(validate && !dupCheck){
    				if(invItemLastUpdated.compareTo(tableModel.getInvItemLastUpdate(view.getInv())) != 0){
    					choice = m.displayChildMessageOptionCustom("Warning","This record has been edited by someone else since you've last opened it!\nYou may cancel and reopen the record, or ignore and submit your changes.", options);
    				} else{
    					return true;
    				}
    			}else if(dupCheck && changedVital){
    				m.displayChildMessage("A duplicate record already exists with this Part/Location or Product/Location combination, please modify before submitting.");
    			}
    		}
    	} 
    	
    	/*** Handle Product Updates ****/
    	else
    	{
    		InvItem invUpdatedMod = new InvItem();
    		invUpdatedMod.setInvID(invUpdated.getInvID());
    		invUpdatedMod.setInvLocation(invUpdated.getInvLocation());
    		invUpdatedMod.setInvPartID(invUpdated.getInvPartID());
    		invUpdatedMod.setInvProductID(invUpdated.getInvProductID());
    		invUpdatedMod.setInvQuantity(invUpdated.getInvQuantity() - view.getInv().getInvQuantity());
    		
    		if(validate && !changedVital){
    			if(invItemLastUpdated.compareTo(tableModel.getInvItemLastUpdate(view.getInv())) != 0){
    				choice = m.displayChildMessageOptionCustom("Warning","This record has been edited by someone else since you've last opened it!\nYou may cancel, ignore and submit your changes, or reload with new data.", options);
    			} else if(!m.getInvTableModel().checkCanAdd(invUpdatedMod)){
    				m.displayChildMessage("Not enough Parts at this location to update this product!");
    			}else{
    				return true;
    			}
    		} else{
    			boolean dupCheck = tableModel.checkForDuplicate(invUpdated);
    			if(validate && !dupCheck){
    				if(invItemLastUpdated.compareTo(tableModel.getInvItemLastUpdate(view.getInv())) != 0){
    					choice = m.displayChildMessageOptionCustom("Warning","This record has been edited by someone else since you've last opened it!\nYou may cancel and reopen the record, or ignore and submit your changes.", options);
    				} else if(!m.getInvTableModel().checkCanAdd(invUpdatedMod)){
        				m.displayChildMessage("Not enough Parts at this location to update this product!");
        			} else{
    					return true;
    				}
    			}else if(dupCheck && changedVital){
    				m.displayChildMessage("A duplicate record already exists with this Part/Location or Product/Location combination, please modify before submitting.");
    			}
    		}
    	}

		switch(choice){
			case 1: return true;
			case 2: view.dispose(); new InvDetailController(new InvDetailView(m.getInvTableModel().getUpdatedInvItem(invUpdated), m), m); break;
			default: break;
		}
		return false;
    }

}