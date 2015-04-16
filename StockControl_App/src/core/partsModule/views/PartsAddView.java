package core.partsModule.views;
/**
 * @author hgv265
 *
 */
import javax.swing.*;

import core.mdi.MasterFrame;
import core.partsModule.controllers.PartsAddController;

import java.awt.*;
import java.awt.event.ActionListener;

@SuppressWarnings("serial")
public class PartsAddView extends JInternalFrame{

	private JButton addBtn, cancelBtn;

	private JTextField pNo;
	private JTextField pNoExternal;
	private JTextField pName;
	private JTextField pVendor;
	@SuppressWarnings("rawtypes")
	private JComboBox pQuantityUnit;
	@SuppressWarnings("unused")
	private MasterFrame m;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public PartsAddView(MasterFrame m){
		this.m = m;
		title = "Add a Part";
		setSize(250, 250);
    	
    	GridLayout gLayout = new GridLayout(7,2);
    	gLayout.setHgap(15);
        this.setLayout(gLayout);
        
        JLabel pNoLabel =new JLabel("Part No: ");
        pNoLabel.setFont(new Font("Arial",Font.TRUETYPE_FONT,12));
        this.add(pNoLabel);
        pNo = new JTextField("");
        this.add(pNo);
        
        JLabel pNoExternalLabel =new JLabel("Part No. Ext: ");
        pNoExternalLabel.setFont(new Font("Arial",Font.TRUETYPE_FONT,12));
        this.add(pNoExternalLabel);
        pNoExternal = new JTextField("");
        this.add(pNoExternal);
        
        JLabel pNameLabel =new JLabel("Part Name: ");
        pNameLabel.setFont(new Font("Arial",Font.TRUETYPE_FONT,12));
        this.add(pNameLabel);
        pName = new JTextField("");
        this.add(pName);
    
        JLabel pVendorLabel =new JLabel("Part Vendor: ");
        pVendorLabel.setFont(new Font("Arial",Font.TRUETYPE_FONT,12));
        this.add(pVendorLabel);
        pVendor = new JTextField("");
        this.add(pVendor);
        
        JLabel pQuantityUnitLabel = new JLabel("Part Quantity Unit: ");
        pQuantityUnitLabel.setFont(new Font("Arial",Font.TRUETYPE_FONT,12));
        this.add(pQuantityUnitLabel);
        String[] pQUnitOptions = { "Unknown", "Linear Feet", "Pieces" };
        pQuantityUnit = new JComboBox(pQUnitOptions);
        pQuantityUnit.setSelectedIndex(0);
        this.add(pQuantityUnit);

        this.add(new JLabel(""));
        this.add(new JLabel(""));
        
        cancelBtn = new JButton("Cancel");        
        this.add(cancelBtn); 
        cancelBtn.setActionCommand(PartsAddController.CANCEL_COMMAND);
 
        addBtn = new JButton("Add Part");        
        this.add(addBtn); 
        addBtn.setActionCommand(PartsAddController.ADD_PART_COMMAND);
        
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

	public JTextField getpNo() {
		return pNo;
	}
	
	public JTextField getpNoExternal() {
		return pNoExternal;
	}

	public JTextField getpName() {
		return pName;
	}

	public JTextField getpVendor() {
		return pVendor;
	}
	
	@SuppressWarnings("rawtypes")
	public JComboBox getpQuantityUnit() {
		return pQuantityUnit;
	}
	
	public void addButtonListener(ActionListener listener) {
		addBtn.addActionListener(listener);
		cancelBtn.addActionListener(listener);
	}

	public JButton getAddBtn() {
		return addBtn;
	}
}