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

import core.mdi.MasterFrame;
import core.productTemplatesModule.controllers.ProductsDetailController;
import core.productTemplatesModule.models.ProductItem;

/**
 * @author Zach
 *
 */
public class ProductsDetailView extends JInternalFrame{
	private ProductItem detailProduct;
	private JButton updateBtn, deleteBtn;
	@SuppressWarnings("unused")
	private MasterFrame m;
	
	private JTextField pNo;
	private JTextField pDesc;
	private static final long serialVersionUID = 1L;
	
	public ProductsDetailView(ProductItem detailProduct, ProductPartsTableView productPartsTableView, MasterFrame m){
		this.detailProduct = detailProduct;
		this.m = m;
		
		title = "Edit Product Template";
    	setLayout(new FlowLayout());
    	
    	JPanel topPanel = new JPanel();
		GridLayout tpLayout = new GridLayout(2,4);
		topPanel.setLayout(tpLayout);
		
		JLabel pNoLabel = new JLabel("Product No: ");
        pNoLabel.setFont(new Font("Arial",Font.TRUETYPE_FONT,12));
        topPanel.add(pNoLabel);
        JLabel pDescLabel = new JLabel("Product Description: ");
        pDescLabel.setFont(new Font("Arial",Font.TRUETYPE_FONT,12));
        topPanel.add(pDescLabel);
        
        pNo = new JTextField(detailProduct.getProductNo());
        topPanel.add(pNo);
        pDesc = new JTextField(detailProduct.getProductDesc());
        topPanel.add(pDesc);
        
        JPanel centerPanel = new JPanel();
		GridLayout cpLayout = new GridLayout(1,2);
		cpLayout.setHgap(10);
		centerPanel.setLayout(cpLayout);
        
        JPanel bottomPanel = new JPanel();
		GridLayout bpLayout = new GridLayout(1,2);
		bpLayout.setHgap(10);
		bottomPanel.setLayout(bpLayout);
        
		deleteBtn = new JButton("Delete Product");        
        bottomPanel.add(deleteBtn); 
        deleteBtn.setActionCommand(ProductsDetailController.DELETE_PRODUCT_COMMAND);
		
        updateBtn = new JButton("Update Product");        
        bottomPanel.add(updateBtn); 
        updateBtn.setActionCommand(ProductsDetailController.UPDATE_PRODUCT_COMMAND); 
        
        add(topPanel, BorderLayout.NORTH);
		add(productPartsTableView, BorderLayout.CENTER);
		add(bottomPanel, BorderLayout.SOUTH);
        
		resizable = true;
        closable = true;
        maximizable = true;
        iconable = true;
        
        pack();
        setSize(500,450);
        Dimension desktopSize = m.getDesktop().getSize();
		Dimension jInternalFrameSize = getSize();
		setLocation((desktopSize.width - jInternalFrameSize.width)/2, (desktopSize.height- jInternalFrameSize.height)/2);
        
        m.getDesktop().add(this);
        m.getDesktop().getDesktopManager().activateFrame(this);
        setVisible(true); 
    }
	
	public ProductItem getProduct(){
		return detailProduct;
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
		updateBtn.addActionListener(listener);
		deleteBtn.addActionListener(listener);
	}

}
