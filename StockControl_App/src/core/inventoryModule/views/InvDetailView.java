package core.inventoryModule.views;

/**
 * @author hgv265
 *
 */
import javax.swing.*;

import core.inventoryModule.controllers.InvDetailController;
import core.inventoryModule.models.InvItem;
import core.mdi.MasterFrame;
import core.partsModule.models.PartItem;
import core.productTemplatesModule.models.ProductItem;

import java.awt.*;
import java.awt.event.ActionListener;


public class InvDetailView extends JInternalFrame{

	private InvItem detailInv;
	private JButton updateBtn, deleteBtn;
	
	private String iPartID;
	private String iProductID;
	@SuppressWarnings("rawtypes")
	private JComboBox iLocation;
	private JTextField iQuantity;
	private MasterFrame m;
	private static final long serialVersionUID = 1L;

	public InvDetailView(InvItem detailInv, MasterFrame m){
		this.detailInv = detailInv;
		this.m = m;
		run();
	}
	
	public void run(){
		createJFrame("Inventory Detail", 470, 320);

		createJLabel("Part: ", "invPartID", "Arial", 12);
		createJLabel("Product: ", "invProductID", "Arial", 12);
		createJLabel("Location: ", "invLocation", "Arial", 12);
		createJLabel("Quantity: ", "invQuantity", "Arial", 12);
		createJLabel("", "", "Arial", 12);
		createJLabel("", "", "Arial", 12);
		
		createJButton("Delete");
		createJButton("Update");
		
		endJFrame();	
	}
	
	public void createJFrame(String name, int width, int height){
		title = name;
		setSize(width, height);
		getContentPane().setSize(width, height);
		
		GridLayout gLayout = new GridLayout(5,2);
    	gLayout.setHgap(15);
		
		getContentPane().setLayout(gLayout);
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
	public void createJLabel(String labelText, String invFieldName, String fontType, int fontSize){
		JLabel label = new JLabel(labelText);
		label.setFont(new Font(fontType, Font.TRUETYPE_FONT, fontSize));
		
		switch(invFieldName){
			case "invPartID" : 
				iPartID = Integer.toString(detailInv.getInvPartID());
				PartItem part = null;
				for(PartItem prt : m.getPartsTableModel().getRows()){
					if(prt.getPartID() == Integer.parseInt(iPartID)){
						part = prt; 
						break;
					}
				}
				if(detailInv.getInvPartID() != 0){
					getContentPane().add(label);
					getContentPane().add(new JLabel(part.getPartName() + " - " + part.getPartNo()));
				}
				break;				
			case "invProductID" :	
				iProductID = Integer.toString(detailInv.getInvProductID());
				ProductItem product = null;
				for(ProductItem prod : m.getProductsTableModel().getRows()){
					if(prod.getProductID() == Integer.parseInt(iProductID)){
						product = prod; 
						break;
					}
				}
				if(detailInv.getInvProductID() != 0){
					getContentPane().add(label);
					getContentPane().add(new JLabel(product.getProductDesc() + " - " + product.getProductNo()));
				}
				break;	
			case "invLocation" : 
				String[] iLocationOptions = { "Unknown", "Facility 1 Warehouse 1", "Facility 1 Warehouse 2", "Facility 2" };
				iLocation = new JComboBox(iLocationOptions);
				iLocation.setSelectedItem(detailInv.getInvLocation());
				getContentPane().add(label);
				getContentPane().add(iLocation);
				break;
			case "invQuantity" : 
				iQuantity = new JTextField(Integer.toString(detailInv.getInvQuantity())); 
				getContentPane().add(label);
				getContentPane().add(iQuantity); 
				break;
			default : getContentPane().add(label); break;
		}	
	}
	
	public void createJButton(String buttonText){
		switch(buttonText){
			case "Update" : updateBtn = new JButton(buttonText); getContentPane().add(updateBtn); updateBtn.setActionCommand(InvDetailController.UPDATE_INV_COMMAND); break;
			case "Delete" : deleteBtn = new JButton(buttonText); getContentPane().add(deleteBtn); deleteBtn.setActionCommand(InvDetailController.DELETE_INV_COMMAND); break;
			default : break;
		}
	}

	public InvItem getInv(){
		return detailInv;
	}
	
	public void setInv(InvItem inv){
		detailInv = inv;
	}
	
	public String getiPartID(){
		return iPartID;
	}
	
	public String getiProductID(){
		return iProductID;
	}
	
	@SuppressWarnings("rawtypes")
	public JComboBox getiLocation(){
		return iLocation;
	}
	
	public JTextField getiQuantity(){
		return iQuantity;
	}
	
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