package core.mdi;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UIManager.*;

import java.io.File;

import javax.imageio.ImageIO;

import core.inventoryModule.controllers.InvTableController;
import core.inventoryModule.models.InvTableModel;
import core.inventoryModule.views.InvTableView;
import core.partsModule.controllers.PartsTableController;
import core.partsModule.models.PartsTableModel;
import core.partsModule.views.PartsTableView;
import core.productTemplatesModule.controllers.ProductsTableController;
import core.productTemplatesModule.models.ProductsTableModel;
import core.productTemplatesModule.views.ProductsTableView;
import core.settings.controllers.AuthenticatorController;
import core.settings.models.AppPreferences;
import core.settings.models.AuthenticatorModel;
import core.settings.models.Function;
import core.settings.models.Session;
import core.settings.views.AuthenticatorView;

public class MasterFrame extends JFrame {
	private static final long serialVersionUID = 1L;
	private JDesktopPane desktop;
	private PartsTableModel partsTableModel;
	private InvTableModel invTableModel;
	private ProductsTableModel productsTableModel;
	private AuthenticatorModel authenticatorModel;
	private Session session;
	private AppPreferences AP;
	private BufferedImage backgroundImage;
	
	@SuppressWarnings("serial")
	public MasterFrame(String title) {
		super(title);
		setAP(new AppPreferences());
		setPartsTableModel(new PartsTableModel(AP));
		setInvTableModel(new InvTableModel(AP));
		setAuthenticatorModel(new AuthenticatorModel());
		setProductsTableModel(new ProductsTableModel(AP));
		
		try {
			backgroundImage = ImageIO.read(new File("img/cabinetron_logo_transparent.png"));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
		
		try {
		    for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
		        if ("Nimbus".equals(info.getName())) {
		            UIManager.setLookAndFeel(info.getClassName());
		            break;
		        }
		    }
		} catch (Exception e) {
		    // If Nimbus is not available, you can set the GUI to another look and feel.
		}
		
		updateJMenuBar();
		   
		//create the MDI desktop
		// A specialized layered pane to be used with JInternalFrames
        desktop = new JDesktopPane() {
            @Override
            protected void paintComponent(Graphics grphcs) {
            	Dimension frameSize = getContentPane().getSize();
            	
                int dx = ((frameSize.width/2)) - 400;
                int dy = ((frameSize.height/2)) - 225;  
            	
            	super.paintComponent(grphcs);
                grphcs.setColor(new Color(197,239,247));
                grphcs.fillRect(0, 0, getWidth(), getHeight());
                grphcs.drawImage(backgroundImage, dx, dy, null);
            }

            @Override
            public Dimension getPreferredSize() {
                return new Dimension(backgroundImage.getWidth(), backgroundImage.getHeight());
            }
        };
		add(desktop);
		
		Runtime.getRuntime().addShutdownHook(new Thread(){
			public void run(){
				if(MasterFrame.this.getInvTableModel() != null)
					MasterFrame.this.getInvTableModel().exitModule();;
				if(MasterFrame.this.getProductsTableModel() != null)
					MasterFrame.this.getProductsTableModel().exitModule();
				if(MasterFrame.this.getPartsTableModel() != null)
					MasterFrame.this.getPartsTableModel().exitModule();;
			}
		});
		
		AuthenticatorView authView = new AuthenticatorView(this);
		new AuthenticatorController(authView, MasterFrame.this);
	}

	//display a child's message in a dialog centered on MDI frame
	public void displayChildMessage(String msg) {
		JOptionPane.showInternalMessageDialog(this.getContentPane(), msg);
	}
	
	//display a child's yes/no message in a dialog centered on MDI frame
	public boolean displayChildMessageOption(String titleBar, String infoMsg) {
		int dialogButton = JOptionPane.YES_NO_OPTION;
		
		int dialogResult = JOptionPane.showInternalConfirmDialog(this.getContentPane(), infoMsg ,titleBar,dialogButton);
		if(dialogResult == JOptionPane.YES_OPTION){
			return true;
		}
		
		return false;
	}
	
	//display a child's yes/no message in a dialog centered on MDI frame
	public int displayChildMessageOptionCustom(String titleBar, String infoMsg, Object[] options) {
		int dialogResult = JOptionPane.showInternalOptionDialog(this.getContentPane(), infoMsg, titleBar,
				JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE,
				null, options, options[0]);
		
		return dialogResult;
	}
	
	//creates and displays the JFrame
	public static void createAndShowGUI() {
		MasterFrame frame = new MasterFrame("Cabinetron Managment Tool");
		frame.setSize(1024, 768);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);		
		frame.setVisible(true);
	}
	
	public JMenuBar updateJMenuBar(){
		Image resizedCheckImage = null,resizedXImage = null;
		ImageIcon checkIcon = null, xIcon = null;
		
		try {
            Image checkImage = ImageIO.read(new File("img/checkIcon.png"));
            Image xImage = ImageIO.read(new File("img/xIcon.png"));
            
            resizedCheckImage = checkImage.getScaledInstance(20, 20, 0);
            resizedXImage = xImage.getScaledInstance(20, 20, 0);
            
        } catch (Exception ex) {
            ex.printStackTrace();
        }
		
        checkIcon = new ImageIcon(resizedCheckImage);
        xIcon = new ImageIcon(resizedXImage);
		
		//create menu for adding inner frames
		JMenuBar customMenuBar = new JMenuBar();
		JMenu menu = new JMenu("File");
		JMenuItem menuItem;
		
		if(null == session){
			menuItem = new JMenuItem("Quit");
			menuItem.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					MasterFrame.this.dispatchEvent(new WindowEvent(MasterFrame.this, WindowEvent.WINDOW_CLOSING));
				}
			});	
			menu.add(menuItem);
		}
		customMenuBar.add(menu);
		
		String UserNameInfoText = "Not Logged In";
		String UserRoleInfoText = "";
		
		if(null != session){
			menuItem = new JMenuItem("Logout");
			menuItem.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					session = null;			
					invTableModel.refreshTableData("");
				    for(JInternalFrame frame : getDesktop().getAllFrames()){
				    	frame.dispose();
				    }
				    updateJMenuBar();
				    
				    AuthenticatorView authView = new AuthenticatorView(MasterFrame.this);
					new AuthenticatorController(authView, MasterFrame.this);
				}
			});
			menu.add(menuItem);
				
			menu = new JMenu("Modules");
			menuItem = new JMenuItem("Parts");
			menuItem.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					PartsTableView ptView = new PartsTableView(MasterFrame.this);
					new PartsTableController(ptView, MasterFrame.this);
				}
			});
			if(!session.getUserFunctions().contains(new Function("canViewParts"))){ menuItem.setEnabled(false); }
			menu.add(menuItem);
			
			menuItem = new JMenuItem("Inventory");
			menuItem.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					InvTableView itView = new InvTableView(MasterFrame.this);
					new InvTableController(itView, MasterFrame.this);
				}
			});		
			if(!session.getUserFunctions().contains(new Function("canViewInventory"))){ menuItem.setEnabled(false); }
			menu.add(menuItem);
			
			menuItem = new JMenuItem("Product Templates");
			menuItem.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					ProductsTableView ptView = new ProductsTableView(MasterFrame.this);
					new ProductsTableController(ptView, MasterFrame.this);
				}
			});
			if(!session.getUserFunctions().contains(new Function("canViewProductTemplates"))){ menuItem.setEnabled(false); }
			menu.add(menuItem);

			UserNameInfoText = session.getUser().getFullName(); 
			UserRoleInfoText = "("+session.getUserRole().getRoleName()+")"; 
		}
		
		customMenuBar.add(menu);
		
		customMenuBar.add(Box.createHorizontalGlue());
		JMenu UserNameInfo = new JMenu(UserNameInfoText);
		JMenu UserRoleInfo = new JMenu(UserRoleInfoText);
		customMenuBar.add(UserNameInfo);
		customMenuBar.add(UserRoleInfo);
		
		if(null != session){
			menuItem = new JMenuItem("Email: " + session.getUser().getEmail());
			menuItem.setEnabled(false);
			UserNameInfo.add(menuItem);

			for(Function function : session.getAllFunctions()){
				menuItem = new JMenuItem(function.getFunctionName());
				boolean exists = false;
				for(Function subFunction : session.getUserFunctions()){
					if(subFunction.getFunctionID() == function.getFunctionID()){
						exists = true;
					    break;
					}
				}
				
				if(exists) {
					menuItem.setIcon(checkIcon);
				} else{
					menuItem.setIcon(xIcon);
				}
				
				menuItem.setEnabled(false);
				UserRoleInfo.add(menuItem);
				
			}
		}
		
		setJMenuBar(customMenuBar);
		customMenuBar.validate();
		customMenuBar.repaint();
		
		return customMenuBar;
	}

	public PartsTableModel getPartsTableModel() {
		return partsTableModel;
	}

	public void setPartsTableModel(PartsTableModel partsTableModel) {
		this.partsTableModel = partsTableModel;
	}

	public ProductsTableModel getProductsTableModel() {
		return productsTableModel;
	}

	public void setProductsTableModel(ProductsTableModel productsTableModel) {
		this.productsTableModel = productsTableModel;
	}
	
	public InvTableModel getInvTableModel() {
		return invTableModel;
	}

	public void setInvTableModel(InvTableModel invTableModel) {
		this.invTableModel = invTableModel;
	}
	
	public AppPreferences getAP() {
		return AP;
	}

	public void setAP(AppPreferences aP) {
		AP = aP;
	}

	public JDesktopPane getDesktop() {
		return desktop;
	}

	public void setDesktop(JDesktopPane desktop) {
		this.desktop = desktop;
	}
	
	public Session getSession() {
		return session;
	}

	public void setSession(Session session) {
		this.session = session;
	}

	public AuthenticatorModel getAuthenticatorModel() {
		return authenticatorModel;
	}

	public void setAuthenticatorModel(AuthenticatorModel authenticatorModel) {
		this.authenticatorModel = authenticatorModel;
	}
}