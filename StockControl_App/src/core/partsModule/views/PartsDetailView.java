package core.partsModule.views;

/**
 * @author hgv265
 *
 */
import javax.swing.*;

import core.mdi.MasterFrame;
import core.partsModule.controllers.PartsDetailController;
import core.partsModule.models.PartItem;

import java.awt.*;
import java.awt.event.ActionListener;

@SuppressWarnings("serial")
public class PartsDetailView extends JInternalFrame{

	private PartItem detailPart;
	private JButton updateBtn, deleteBtn;
	
	private JTextField pNo;
	private JTextField pNoExternal;
	private JTextField pName;
	private JTextField pVendor;
	@SuppressWarnings("rawtypes")
	private JComboBox pQuantityUnit;
	private MasterFrame m;

	public PartsDetailView(PartItem detailPart, MasterFrame m){
		this.detailPart = detailPart;
		this.m = m;
		run(detailPart);
	}
	
	public void run(PartItem detailPart){
		createJPanel("Part Detail", 250, 250);
		createJLabel("Part No: ", "partNo", "Arial", 12);
		createJLabel("Part No. External: ", "partNoExternal", "Arial", 12);
		createJLabel("Part Name: ", "partName", "Arial", 12);
		createJLabel("Part Vendor: ", "partVendor", "Arial", 12);
		createJLabel("Part Quantity Unit: ", "partQuantityUnit", "Arial", 12);
		createJLabel("","", "Arial", 12);
		createJLabel("","", "Arial", 12);
		
		createJButton("Delete");
		createJButton("Update");
		
		endJFrame();
		
	}
	
	public void createJPanel(String name, int width, int height){
		title = name;
		setSize(width, height);
		this.setSize(width, height);
		
		GridLayout gLayout = new GridLayout(7,2);
    	gLayout.setHgap(15);
		
		this.setLayout(gLayout);
	}
	
	public void endJFrame(){
		resizable = true;
        closable = true;
        maximizable = true;
        iconable = true;
        
        pack();
        Dimension desktopSize = m.getDesktop().getSize();
		Dimension jInternalFrameSize = getSize();
		setLocation((desktopSize.width - jInternalFrameSize.width)/2, (desktopSize.height- jInternalFrameSize.height)/2);
        
        m.getDesktop().add(this);
        m.getDesktop().getDesktopManager().activateFrame(this);
        setVisible(true); 
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void createJLabel(String labelText, String partFieldName, String fontType, int fontSize){
		JLabel label = new JLabel(labelText);
		label.setFont(new Font(fontType, Font.TRUETYPE_FONT, fontSize));
		this.add(label);
		
		switch(partFieldName){
			case "partNo" : 
				pNo = new JTextField(detailPart.getPartNo()); 
				this.add(pNo); 
				break;
			case "partNoExternal" : 
				pNoExternal = new JTextField(detailPart.getPartNoExternal()); 
				this.add(pNoExternal); 
				break;
			case "partName" : 
				pName = new JTextField(detailPart.getPartName()); 
				this.add(pName); 
				break;
			case "partVendor" : 
				pVendor = new JTextField(detailPart.getPartVendor()); 
				this.add(pVendor); 
				break;
			case "partQuantityUnit" : 
				String[] pQUnitOptions = { "Unknown", "Linear Feet", "Pieces" };
				pQuantityUnit = new JComboBox(pQUnitOptions);
				pQuantityUnit.setSelectedItem(detailPart.getPartQuantityUnit());
				this.add(pQuantityUnit);
				break;
			default : break;
		}	
	}
	
	public void createJButton(String buttonText){
		switch(buttonText){
			case "Update" : updateBtn = new JButton(buttonText); this.add(updateBtn); updateBtn.setActionCommand(PartsDetailController.UPDATE_PART_COMMAND); break;
			case "Delete" : deleteBtn = new JButton(buttonText); this.add(deleteBtn); deleteBtn.setActionCommand(PartsDetailController.DELETE_PART_COMMAND); break;
			default : break;
		}
	}

	public PartItem getPart(){
		return detailPart;
	}
	
	public JTextField getpNo(){
		return pNo;
	}
	
	public JTextField getpNoExternal(){
		return pNoExternal;
	}
	
	public JTextField getpName(){
		return pName;
	}
	
	public JTextField getpVendor(){
		return pVendor;
	}
	
	@SuppressWarnings("rawtypes")
	public JComboBox getpQuantityUnit(){
		return pQuantityUnit;
	}
	
	/*
	public JFrame getFrame(){
		return this;
	}
	*/
	public JInternalFrame getFrame(){
		return this;
	}
	
	public void addButtonListener(ActionListener listener) {
		updateBtn.addActionListener(listener);
		deleteBtn.addActionListener(listener);
	}

	public JButton getUpdateBtn() {
		return updateBtn;
	}

	public JButton getDeleteBtn() {
		return deleteBtn;
	}
	
}