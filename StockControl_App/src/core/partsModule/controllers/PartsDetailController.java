package core.partsModule.controllers;
/**
 * @author hgv265
 *
 */
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import core.mdi.MasterFrame;
import core.partsModule.models.PartItem;
import core.partsModule.models.PartsTableModel;
import core.partsModule.views.PartsDetailView;
import core.settings.models.Function;

public class PartsDetailController implements ActionListener{
	public static final String UPDATE_PART_COMMAND = "UPDATE_PART_COMMAND";
	public static final String DELETE_PART_COMMAND = "DELETE_PART_COMMAND";
	
	private PartsDetailView view;
	private PartsTableModel tableModel;
	private MasterFrame m;
	private boolean changedVital;
    
    public PartsDetailController(PartsDetailView view, MasterFrame m){
    	this.m = m;
    	this.view = view;
		this.tableModel = m.getPartsTableModel();
		initView();
    }
    
    public void initView(){        
    	view.addButtonListener(this);
    	setRoleRestrictions();
    }
    
    private void setRoleRestrictions() {
		if (!m.getSession().getUserFunctions().contains(new Function("canAddParts"))) {
			view.getUpdateBtn().setEnabled(false);
		}
		if (!m.getSession().getUserFunctions().contains(new Function("canDeleteParts"))) {
			view.getDeleteBtn().setEnabled(false);
		}
	}
    
    @Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		switch (e.getActionCommand()) {
		case UPDATE_PART_COMMAND:
			PartItem partUpdate = getTextInput();
			boolean dupCheckWarn = tableModel.checkForDuplicateWarn(partUpdate);
			boolean accept = false;
			boolean validate = partUpdate.validate(m);
			
			if(!changedVital && validate){
				if(dupCheckWarn && !partUpdate.getPartName().equals(view.getPart().getPartName())){
					accept = promptDupWarn(partUpdate);
				}
				if(!dupCheckWarn || accept || partUpdate.getPartName().equals(view.getPart().getPartName())){
					tableModel.updateRow(partUpdate);
					view.getFrame().dispose();
				}
			} else if(validate){
				boolean dupCheck = tableModel.checkForDuplicate(partUpdate);
				if(validate && !dupCheck){
					if(dupCheckWarn){
						accept = promptDupWarn(partUpdate);
					}
					if(!dupCheckWarn || accept){
						tableModel.updateRow(partUpdate);
						view.getFrame().dispose();
					}
				}
				if(dupCheck && validate){
					m.displayChildMessage("A duplicate record already exists with this information, please modify before submitting.");
				}
			}
			break;
		case DELETE_PART_COMMAND:
			if(promptDelete(view.getPart())){
				tableModel.removeRow(getTextInput());
				view.getFrame().dispose();
			}
			break;
		default : break;
		}
		
	}
    
    private PartItem getTextInput(){    	
    	String pNo = view.getpNo().getText();
    	String pNoExternal = view.getpNoExternal().getText();
    	String pName = view.getpName().getText();
    	String pVendor = view.getpVendor().getText();
    	String pQuantityUnit = (String) view.getpQuantityUnit().getSelectedItem();
    	
    	PartItem updatedPart = new PartItem();
    	updatedPart.setPartID(view.getPart().getPartID());
    	updatedPart.setPartNo(pNo);
    	updatedPart.setPartNoExternal(pNoExternal);
    	updatedPart.setPartName(pName);
    	updatedPart.setPartVendor(pVendor);
    	updatedPart.setPartQuantityUnit(pQuantityUnit);
    	
    	if(view.getPart().getPartNo().equals(updatedPart.getPartNo())){
    		changedVital = false;
    	} else{
    		changedVital = true;
    	}

    	return updatedPart;
    }
    
    public boolean promptDelete(PartItem part){
    	if(tableModel.checkForAssocInvItems(part)){
			m.displayChildMessage("This record cannot be deleted because there are inventory items associated with it.");
			return false;
		}
    	
    	String titleBar = "Are you sure?";
		String infoMsg = "Are you sure you would like to delete " + part.getPartName() + "? This action cannot be undone.";

		return m.displayChildMessageOption(titleBar, infoMsg);
	}
    
    public boolean promptDupWarn(PartItem part){
		String titleBar = "Duplicate Name";
		String infoMsg = "Another part alreay contains the name " + part.getPartName() + ". Are you sure you want to continue to add this one?";

		return m.displayChildMessageOption(titleBar, infoMsg);
	}

	
}