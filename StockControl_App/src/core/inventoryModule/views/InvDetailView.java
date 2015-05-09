package core.inventoryModule.views;

/**
 * @author hgv265
 *
 */

import javax.swing.*;
import javax.swing.border.TitledBorder;

import core.inventoryModule.controllers.InvDetailController;
import core.inventoryModule.models.InvItemLogObserver;
import core.inventoryModule.models.obj.InvItem;
import core.mdi.models.MasterFrame;
import core.partsModule.models.obj.PartItem;
import core.productTemplatesModule.models.obj.ProductItem;

import java.awt.*;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;


public class InvDetailView extends JInternalFrame{

	private InvItem detailInv;
	private JButton updateBtn, deleteBtn, cancelBtn;
	
	private String iPartID;
	private String iProductID;
	@SuppressWarnings("rawtypes")
	private JComboBox iLocation;
	private JTextField iQuantity;
	private JPanel northPanel;
	private JPanel centerPanel;
	private JPanel southPanel;
	private JScrollPane centerScrollPane;
	private GridLayout gLayoutNorth;
	private GridLayout gLayoutCenter;
	private GridLayout gLayoutSouth;
	private JList<Object> logList;

	private InvItemLogObserver invItemLogObserver;

	private MasterFrame m;
	private static final long serialVersionUID = 1L;

	public InvDetailView(InvItem detailInv, MasterFrame m){
		this.detailInv = detailInv;
		this.m = m;
		run();
	}
	
	public void run(){
		createJFrame("Inventory Detail", 470, 620);

		createJLabel("Part: ", "invPartID", "Arial", 12);
		createJLabel("Product: ", "invProductID", "Arial", 12);
		createJLabel("Location: ", "invLocation", "Arial", 12);
		createJLabel("Quantity: ", "invQuantity", "Arial", 12);
		createJLabel("", "", "Arial", 12);
		createJLabel("", "", "Arial", 12);
		
		createJButton("Delete");
		createJButton("Update");
		createJButton("Cancel");
		
		createJLabel("", "", "Arial", 12);
		createJLabel("", "", "Arial", 12);
		
		displayInvItemLog();
		
		endJFrame();	
		
		initObservers();
	}
	
	public void createJFrame(String name, int width, int height){
		title = name;
		
		gLayoutNorth = new GridLayout(6,2);
    	gLayoutNorth.setHgap(15);
    	northPanel = new JPanel();
    	northPanel.setLayout(gLayoutNorth);
    	
    	gLayoutCenter = new GridLayout(1,1);
    	gLayoutCenter.setHgap(15);
    	centerPanel = new JPanel();
    	centerPanel.setLayout(gLayoutCenter);
    	
    	gLayoutSouth = new GridLayout(1,1);
    	gLayoutSouth.setHgap(15);
    	southPanel = new JPanel();
    	southPanel.setLayout(gLayoutSouth);
    	
    	centerScrollPane = new JScrollPane(centerPanel);
		centerScrollPane.setPreferredSize(new Dimension(600, 100));
	}
	
	public void endJFrame(){
		resizable = true;
        closable = false;
        maximizable = true;
        iconable = true;
        
        getContentPane().add(BorderLayout.NORTH, northPanel);
        getContentPane().add(BorderLayout.CENTER, centerScrollPane);
        getContentPane().add(BorderLayout.SOUTH, southPanel);
        
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
					northPanel.add(label);
					northPanel.add(new JLabel(part.getPartName() + " - " + part.getPartNo()));
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
					northPanel.add(label);
					northPanel.add(new JLabel(product.getProductDesc() + " - " + product.getProductNo()));
				}
				break;	
			case "invLocation" : 
				String[] iLocationOptions = { "Unknown", "Facility 1 Warehouse 1", "Facility 1 Warehouse 2", "Facility 2" };
				iLocation = new JComboBox(iLocationOptions);
				iLocation.setSelectedItem(detailInv.getInvLocation());
				northPanel.add(label);
				northPanel.add(iLocation);
				break;
			case "invQuantity" : 
				iQuantity = new JTextField(Integer.toString(detailInv.getInvQuantity())); 
				northPanel.add(label);
				northPanel.add(iQuantity); 
				break;			
			default : northPanel.add(label); break;
		}	
	}
	
	public void createJButton(String buttonText){
		switch(buttonText){
			case "Update" : updateBtn = new JButton(buttonText); northPanel.add(updateBtn); updateBtn.setActionCommand(InvDetailController.UPDATE_INV_COMMAND); break;
			case "Delete" : deleteBtn = new JButton(buttonText); northPanel.add(deleteBtn); deleteBtn.setActionCommand(InvDetailController.DELETE_INV_COMMAND); break;
			case "Cancel" : cancelBtn = new JButton(buttonText); southPanel.add(cancelBtn); cancelBtn.setActionCommand(InvDetailController.CANCEL_COMMAND); break;
			default : break;
		}
	}
	
	public void displayInvItemLog() {
		setLogList();
		logList.setBorder(new TitledBorder(BorderFactory.createLineBorder(Color.WHITE),"Inventory Item Log"));
		centerPanel.add(logList);
	}
	
	public void asyncReloadInvItemLog() {		
		new Thread(new Runnable() {
		    public void run() {
		    	centerPanel.remove(logList);
		    	setLogList();
		    	logList.setBorder(new TitledBorder(BorderFactory.createLineBorder(Color.WHITE),"Inventory Item Log"));
				InvDetailView.this.repaint();
				InvDetailView.this.revalidate();
				centerPanel.add(logList);
				centerPanel.repaint();
				centerPanel.revalidate();
		    }
		}).start();	
	}
	
	/*
	 * Initializes remote and local observers
	 */
	public void initObservers() {
		// create an observer for this model and register it with the remote EJB
		try {
			invItemLogObserver = new InvItemLogObserver(this);
			m.getInvTableModel().registerLogObserver(invItemLogObserver);
			m.getAllInvItemLogObservers().add(invItemLogObserver);
		} catch (RemoteException e1) {
			e1.printStackTrace();
		}
	}
	
	public void removeObservers() {
		m.getInvTableModel().unregisterLogObserver(invItemLogObserver);
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
		cancelBtn.addActionListener(listener);
	}

	public JButton getUpdateBtn() {
		return updateBtn;
	}

	public JButton getDeleteBtn() {
		return deleteBtn;
	}
	
	public JButton getCancelBtn() {
		return cancelBtn;
	}
	
	@SuppressWarnings("rawtypes")
	public JList getLogList() {
		return logList;
	}

	public void setLogList() {
		logList = new JList<Object>(detailInv.getLog(m.getInvTableModel()).toArray());
	}
	
	public InvItemLogObserver getInvItemLogObserver() {
		return invItemLogObserver;
	}

	public void setInvItemLogObserver(InvItemLogObserver invItemLogObserver) {
		this.invItemLogObserver = invItemLogObserver;
	}
	
}