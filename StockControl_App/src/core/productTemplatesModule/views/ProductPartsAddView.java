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

import core.mdi.models.MasterFrame;
import core.partsModule.models.obj.PartItem;
import core.productTemplatesModule.controllers.ProductPartsAddController;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class ProductPartsAddView extends JInternalFrame{

	private JButton addBtn, cancelBtn;
	private ArrayList<PartItem> partList;
	
	@SuppressWarnings("rawtypes")
	private JComboBox pPartID;
	private JTextField pQuantity;
	
	private int selectedPartID = 0;
	@SuppressWarnings("unused")
	private MasterFrame m;
	private static final long serialVersionUID = 1L;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public ProductPartsAddView(MasterFrame m){
		this.partList = m.getPartsTableModel().getRows();
		
		title = "Add a Part to the Product Template";
    	
    	GridLayout gLayout = new GridLayout(4,2);
    	gLayout.setHgap(15);
        getContentPane().setLayout(gLayout);
        
        JLabel pPartIDLabel =new JLabel("Part: ");
        pPartIDLabel.setFont(new Font("Arial",Font.TRUETYPE_FONT,12));
        getContentPane().add(pPartIDLabel);
        //iPartID = new JTextField("");
        //getContentPane().add(iPartID);
        
        String[] pPartIDOptions = new String[partList.size()];
		PartItem p = new PartItem();
		for(int i = 0; i < partList.size(); i++){
			p = partList.get(i);
			pPartIDOptions[i] = p.getPartName() + " - " + p.getPartNo();
		}
		pPartID = new JComboBox(pPartIDOptions);
		selectedPartID = partList.get(pPartID.getSelectedIndex()).getPartID();
		
		// Install auto-completion support. 
		AutoCompleteDecorator.decorate(pPartID);
		getContentPane().add(pPartID);
        
        JLabel pQuantityLabel =new JLabel("Quantity: ");
        pQuantityLabel.setFont(new Font("Arial",Font.TRUETYPE_FONT,12));
        getContentPane().add(pQuantityLabel);
        pQuantity = new JTextField("");
        getContentPane().add(pQuantity);
        
        JLabel emptyLabel1 =new JLabel("");
        emptyLabel1.setFont(new Font("Arial",Font.TRUETYPE_FONT,12));
        getContentPane().add(emptyLabel1);

        JLabel emptyLabel2 =new JLabel("");
        emptyLabel2.setFont(new Font("Arial",Font.TRUETYPE_FONT,12));
        getContentPane().add(emptyLabel2);
        
        cancelBtn = new JButton("Cancel");        
        getContentPane().add(cancelBtn); 
        cancelBtn.setActionCommand(ProductPartsAddController.CANCEL_COMMAND);
 
        addBtn = new JButton("Add Part");        
        getContentPane().add(addBtn); 
        addBtn.setActionCommand(ProductPartsAddController.ADD_PRODUCT_PART_COMMAND);
        
        selectionListener();
        
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
	
	public JInternalFrame getFrame(){
		return this;
	}

	@SuppressWarnings("rawtypes")
	public JComboBox getpPartID() {
		return pPartID;
	}

	public JTextField getpQuantity() {
		return pQuantity;
	}
	
	public int getSelectedPartID(){
		return selectedPartID;
	}
	
	public void addButtonListener(ActionListener listener) {
		addBtn.addActionListener(listener);
		cancelBtn.addActionListener(listener);
	}
	
	public void selectionListener(){
		pPartID.addActionListener (new ActionListener () {
		    public void actionPerformed(ActionEvent e) {
		        selectedPartID = partList.get(pPartID.getSelectedIndex()).getPartID();
		    }
		});
	}
}