/**
 * 
 */
package core.productTemplatesModule.views;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import core.mdi.models.MasterFrame;
import core.productTemplatesModule.controllers.ProductsAddController;

/**
 * @author Zach
 *
 */
public class ProductsAddView extends JInternalFrame{
	private JButton addBtn, cancelBtn;

	private JTextField pNo;
	private JTextField pDesc;
	@SuppressWarnings("unused")
	private MasterFrame m;
	private static final long serialVersionUID = 1L;

	public ProductsAddView(MasterFrame m){
		this.m = m;
		title = "Add a Product Template";
    	setLayout(new FlowLayout());
    	
    	JPanel topPanel = new JPanel();
		GridLayout tpLayout = new GridLayout(3,2);
		tpLayout.setHgap(10);
		topPanel.setLayout(tpLayout);
		
		JLabel pNoLabel = new JLabel("Product No: ");
        pNoLabel.setFont(new Font("Arial",Font.TRUETYPE_FONT,12));
        topPanel.add(pNoLabel);
        pNo = new JTextField("");
        topPanel.add(pNo);
        
        JLabel pDescLabel = new JLabel("Product Description ");
        pDescLabel.setFont(new Font("Arial",Font.TRUETYPE_FONT,12));
        topPanel.add(pDescLabel);
        pDesc = new JTextField("");
        topPanel.add(pDesc);
        topPanel.add(new JLabel(""));
        
        JPanel bottomPanel = new JPanel();
		GridLayout bpLayout = new GridLayout(1,2);
		bpLayout.setHgap(10);
		bottomPanel.setLayout(bpLayout);
        
        cancelBtn = new JButton("Cancel");        
        bottomPanel.add(cancelBtn); 
        cancelBtn.setActionCommand(ProductsAddController.CANCEL_COMMAND);
 
        addBtn = new JButton("Add Product");        
        bottomPanel.add(addBtn); 
        addBtn.setActionCommand(ProductsAddController.ADD_PRODUCT_COMMAND);
        
        add(topPanel, BorderLayout.NORTH);
		add(bottomPanel, BorderLayout.SOUTH);
        
		resizable = true;
        closable = true;
        maximizable = true;
        iconable = true;
        
        pack();
        setSize(300,160);
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
	
	public JTextField getpDesc() {
		return pDesc;
	}
	
	public void addButtonListener(ActionListener listener) {
		addBtn.addActionListener(listener);
		cancelBtn.addActionListener(listener);
	}
}
