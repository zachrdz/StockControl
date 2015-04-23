/**
 * 
 */
package core.productTemplatesModule.views;

/**
 * @author hgv265
 *
 */
import javax.swing.*;

import org.jdesktop.swingx.autocomplete.*;

import core.mdi.MasterFrame;
import core.partsModule.models.PartItem;
import core.productTemplatesModule.controllers.ProductPartsDetailController;
import core.productTemplatesModule.models.ProductPartItem;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class ProductPartsDetailView extends JInternalFrame{

	private ProductPartItem detailProductPart;
	private ArrayList<PartItem> partList;
	private JButton updateBtn, deleteBtn;
	
	@SuppressWarnings("rawtypes")
	private JComboBox pPartID;
	private JTextField pQuantity;
	
	private int selectedPartID = 0;
	private int tmpIndex = 0;
	private MasterFrame m;
	private static final long serialVersionUID = 1L;

	public ProductPartsDetailView(ProductPartItem detailProductPart, MasterFrame m){
		this.m = m;
		this.detailProductPart = detailProductPart;
		this.partList = m.getPartsTableModel().getRows();
		run();
	}
	
	public void run(){
		createJFrame("Product Part Detail", 470, 320);

		createJLabel("Part: ", "productPartID", "Arial", 12);
		createJLabel("Quantity: ", "productPartQuantity", "Arial", 12);
		createJLabel("", "", "Arial", 12);
		createJLabel("", "", "Arial", 12);
		
		createJButton("Delete");
		createJButton("Update");
		
		endJFrame();
		
		selectionListener();
		
	}
	
	public void createJFrame(String name, int width, int height){
		title = name;
		setSize(width, height);
		getContentPane().setSize(width, height);
		
		GridLayout gLayout = new GridLayout(4,2);
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
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void createJLabel(String labelText, String productPartFieldName, String fontType, int fontSize){
		JLabel label = new JLabel(labelText);
		label.setFont(new Font(fontType, Font.TRUETYPE_FONT, fontSize));
		getContentPane().add(label);
		
		switch(productPartFieldName){
			case "productPartID" : 
				String[] pPartIDOptions = new String[partList.size()];
				PartItem p = new PartItem();
				for(int i = 0; i < partList.size(); i++){
					p = partList.get(i);
					if(detailProductPart.getPartID() == p.getPartID()){
						tmpIndex = i;
					}
					pPartIDOptions[i] = p.getPartName() + " - " + p.getPartNo();
				}
				pPartID = new JComboBox(pPartIDOptions);
				pPartID.setSelectedIndex(tmpIndex);
				selectedPartID = partList.get(tmpIndex).getPartID();
				
				// Install auto-completion support. 
				AutoCompleteDecorator.decorate(pPartID);
				getContentPane().add(pPartID);
				break;
			case "productPartQuantity" : 
				pQuantity = new JTextField(Integer.toString(detailProductPart.getQuantity())); 
				getContentPane().add(pQuantity); 
				break;
			default : break;
		}	
	}
	
	public void createJButton(String buttonText){
		switch(buttonText){
			case "Update" : updateBtn = new JButton(buttonText); getContentPane().add(updateBtn); updateBtn.setActionCommand(ProductPartsDetailController.UPDATE_PRODUCT_PART_COMMAND); break;
			case "Delete" : deleteBtn = new JButton(buttonText); getContentPane().add(deleteBtn); deleteBtn.setActionCommand(ProductPartsDetailController.DELETE_PRODUCT_PART_COMMAND); break;
			default : break;
		}
	}

	public ProductPartItem getProductPart(){
		return detailProductPart;
	}
	
	@SuppressWarnings("rawtypes")
	public JComboBox getpPartID(){
		return pPartID;
	}

	public JTextField getpQuantity(){
		return pQuantity;
	}
	
	public int getSelectedPartID(){
		return selectedPartID;
	}
	
	public JInternalFrame getFrame(){
		return this;
	}
	
	public void addButtonListener(ActionListener listener) {
		updateBtn.addActionListener(listener);
		deleteBtn.addActionListener(listener);
	}
	
	public void selectionListener(){
		pPartID.addActionListener (new ActionListener () {
		    public void actionPerformed(ActionEvent e) {
		        selectedPartID = partList.get(pPartID.getSelectedIndex()).getPartID();
		    }
		});
	}

}