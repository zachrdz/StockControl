package core.partsModule.controllers;
/**
 * 
 */

/**
 * @author hgv265
 *
 */
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import core.mdi.models.MasterFrame;
import core.partsModule.models.PartsTableModel;
import core.partsModule.models.obj.PartItem;
import core.partsModule.views.PartsAddView;
import core.session.models.obj.Function;

public class PartsAddController implements ActionListener{
	public static final String ADD_PART_COMMAND = "ADD_PART_COMMAND";
	public static final String CANCEL_COMMAND = "CANCEL_COMMAND";
	
	private PartsAddView view;
	private PartsTableModel tableModel;
	private MasterFrame m;
    
    public PartsAddController(PartsAddView view, MasterFrame m){
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
			view.getAddBtn().setEnabled(false);
		}
	}
    
    @Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		switch (e.getActionCommand()) {
		case ADD_PART_COMMAND:
			PartItem partAdd = getTextInput();
			boolean dupCheck = tableModel.checkForDuplicate(partAdd);
			boolean dupCheckWarn = tableModel.checkForDuplicateWarn(partAdd);
			boolean accept = false;
			
			if(partAdd.validate(m) && !dupCheck){
				if(dupCheckWarn){
					accept = promptDupWarn(partAdd);
				}
				if(!dupCheckWarn || accept){
					tableModel.addRow(partAdd);
					view.getFrame().dispose();
				}
			}
			if(dupCheck){
				m.displayChildMessage("A duplicate record already exists with this part number, please modify before submitting.");
			}
			break;
		case CANCEL_COMMAND:
			view.getFrame().dispose();
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
    	
    	PartItem newPart = new PartItem();
    	newPart.setPartNo(pNo);
    	newPart.setPartNoExternal(pNoExternal);
    	newPart.setPartName(pName);
    	newPart.setPartVendor(pVendor);
    	newPart.setPartQuantityUnit(pQuantityUnit);
    	
    	return newPart;
    }
    
    public boolean promptDupWarn(PartItem part){
		String titleBar = "Duplicate Name";
		String infoMsg = "Another part alreay contains the name " + part.getPartName() + ". Are you sure you want to continue to add this one?";

		return m.displayChildMessageOption(titleBar, infoMsg);
	}

	
}
