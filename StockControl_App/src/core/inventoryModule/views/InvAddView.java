package core.inventoryModule.views;
/**
 * @author hgv265
 *
 */
import javax.swing.*;

import org.jdesktop.swingx.autocomplete.*;

import core.inventoryModule.controllers.InvAddController;
import core.mdi.models.MasterFrame;
import core.partsModule.models.obj.PartItem;
import core.productTemplatesModule.models.obj.ProductItem;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class InvAddView extends JInternalFrame{

	private JButton addBtn, cancelBtn;
	private ArrayList<PartItem> partList;
	private ArrayList<ProductItem> productList;
	
	@SuppressWarnings("rawtypes")
	private JComboBox iPartID;
	@SuppressWarnings("rawtypes")
	private JComboBox iProductID;
	@SuppressWarnings("rawtypes")
	private JComboBox iLocation;
	private JTextField iQuantity;
	@SuppressWarnings("unused")
	private MasterFrame m;
	
	private int selectedPartID = 0;
	private int selectedProductID = 0;
	private PartItem partPlaceHolder;
	private ProductItem productPlaceHolder;
	private static final long serialVersionUID = 1L;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public InvAddView(MasterFrame m){
		this.partList = m.getPartsTableModel().getRows();
		this.productList = m.getProductsTableModel().getRows();
		this.m = m;
		
		title = "Add an Inventory Item";
		setSize(470, 320);
    	
    	GridLayout gLayout = new GridLayout(6,2);
    	gLayout.setHgap(15);
        getContentPane().setLayout(gLayout);
        
        /********************* Part List ******************************/
        JLabel iPartIDLabel =new JLabel("Part: ");
        iPartIDLabel.setFont(new Font("Arial",Font.TRUETYPE_FONT,12));
        getContentPane().add(iPartIDLabel);
        
        String[] iPartIDOptions = new String[partList.size() + 1];
        
        partPlaceHolder = new PartItem();
        partPlaceHolder.setPartName("Select...");
        partPlaceHolder.setPartID(0);
        iPartIDOptions[0] = partPlaceHolder.getPartName();
        
		PartItem p = new PartItem();
		for(int i = 0; i < partList.size(); i++){
			p = partList.get(i);
			iPartIDOptions[i+1] = p.getPartName() + " - " + p.getPartNo();
		}
		iPartID = new JComboBox(iPartIDOptions);
		selectedPartID = 0;
		
		// Install auto-completion support. 
		AutoCompleteDecorator.decorate(iPartID);
		getContentPane().add(iPartID);
		
		/********************* Product List ******************************/
		JLabel iProductIDLabel =new JLabel("Product: ");
        iProductIDLabel.setFont(new Font("Arial",Font.TRUETYPE_FONT,12));
        getContentPane().add(iProductIDLabel);
        
        String[] iProductIDOptions = new String[productList.size() + 1];
        
        productPlaceHolder = new ProductItem();
        productPlaceHolder.setProductDesc("Select...");
        productPlaceHolder.setProductID(0);
        iProductIDOptions[0] = productPlaceHolder.getProductDesc();
        
		ProductItem prod = new ProductItem();
		for(int i = 0; i < productList.size(); i++){
			prod = productList.get(i);
			iProductIDOptions[i+1] = prod.getProductDesc() + " - " + prod.getProductNo();
		}
		iProductID = new JComboBox(iProductIDOptions);
		selectedProductID = 0;
		
		// Install auto-completion support. 
		AutoCompleteDecorator.decorate(iProductID);
		getContentPane().add(iProductID);
		 
		/********************* Location List ******************************/
        JLabel iLocationLabel =new JLabel("Location: ");
        iLocationLabel.setFont(new Font("Arial",Font.TRUETYPE_FONT,12));
        getContentPane().add(iLocationLabel);
        String[] iLocationOptions = { "Unknown", "Facility 1 Warehouse 1", "Facility 1 Warehouse 2", "Facility 2" };
		iLocation = new JComboBox(iLocationOptions);
		iLocation.setSelectedItem("Unknown");
		getContentPane().add(iLocation);
        
        JLabel iQuantityLabel =new JLabel("Quantity: ");
        iQuantityLabel.setFont(new Font("Arial",Font.TRUETYPE_FONT,12));
        getContentPane().add(iQuantityLabel);
        iQuantity = new JTextField("");
        getContentPane().add(iQuantity);
        
        JLabel emptyLabel1 =new JLabel("");
        emptyLabel1.setFont(new Font("Arial",Font.TRUETYPE_FONT,12));
        getContentPane().add(emptyLabel1);

        JLabel emptyLabel2 =new JLabel("");
        emptyLabel2.setFont(new Font("Arial",Font.TRUETYPE_FONT,12));
        getContentPane().add(emptyLabel2);
        
        cancelBtn = new JButton("Cancel");        
        getContentPane().add(cancelBtn); 
        cancelBtn.setActionCommand(InvAddController.CANCEL_COMMAND);
 
        addBtn = new JButton("Add Inventory");        
        getContentPane().add(addBtn); 
        addBtn.setActionCommand(InvAddController.ADD_INV_COMMAND);
        
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
        
        selectionListener();
    }
	
	public JInternalFrame getFrame(){
		return this;
	}

	@SuppressWarnings("rawtypes")
	public JComboBox getiPartID() {
		return iPartID;
	}
	
	@SuppressWarnings("rawtypes")
	public JComboBox getiProductID() {
		return iProductID;
	}
	
	@SuppressWarnings("rawtypes")
	public JComboBox getiLocation() {
		return iLocation;
	}

	public JTextField getiQuantity() {
		return iQuantity;
	}
	
	public int getSelectedPartID(){
		return selectedPartID;
	}
	
	public int getSelectedProductID(){
		return selectedProductID;
	}
	
	public void addButtonListener(ActionListener listener) {
		addBtn.addActionListener(listener);
		cancelBtn.addActionListener(listener);
	}
	
	public void selectionListener(){
		iPartID.addActionListener (new ActionListener () {
		    public void actionPerformed(ActionEvent e) {
		    	if(iPartID.getSelectedIndex() != 0){
			    	if(selectedProductID != 0){
			    		iProductID.setSelectedIndex(0);
			    		selectedProductID = 0;
			    	} 
			        selectedPartID = partList.get(iPartID.getSelectedIndex() - 1).getPartID();
			        System.out.println("Selected Part ID " + selectedPartID);
			    	
		    	} else{
		    		selectedPartID = 0;
		    		System.out.println("Selected Part ID " + selectedPartID);
		    	}
		    }
		});
		
		iProductID.addActionListener (new ActionListener () {
		    public void actionPerformed(ActionEvent e) {
		        if(iProductID.getSelectedIndex() != 0){
			    	if(selectedPartID != 0){
			        	iPartID.setSelectedIndex(0);
			        	selectedPartID = 0;
			        } 
		        	selectedProductID = productList.get(iProductID.getSelectedIndex() - 1).getProductID();
			        System.out.println("Selected Product ID " + selectedProductID);
			        
		        } else{
		        	selectedProductID = 0;
		        	System.out.println("Selected Product ID " + selectedProductID);
		        }
		    }
		});
	}

	public JButton getAddBtn() {
		return addBtn;
	}

}