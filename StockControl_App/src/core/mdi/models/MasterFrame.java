package core.mdi.models;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UIManager.*;

import java.io.File;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Properties;

import javax.imageio.ImageIO;

import core.inventoryModule.models.InvItemLogObserver;
import core.inventoryModule.models.InvTableModel;
import core.partsModule.models.PartsTableModel;
import core.productTemplatesModule.models.ProductsTableModel;
import core.session.controllers.AuthenticatorController;
import core.session.models.obj.Session;
import core.session.models.remote.AuthenticatorRemote;
import core.session.views.AuthenticatorView;
import core.settings.models.AppPreferences;

public class MasterFrame extends JFrame {
	private static final long serialVersionUID = 1L;
	private JDesktopPane desktop;
	private PartsTableModel partsTableModel;
	private InvTableModel invTableModel;
	private ProductsTableModel productsTableModel;
	private AppPreferences AP;
	private BufferedImage backgroundImage;
	@SuppressWarnings("unused")
	private MasterMenuBar masterMenuBar;
	private JMenuItem loginMenuItem;
	private InvItemLogObserver invItemLogObserver;
	private ArrayList<InvItemLogObserver> allInvItemLogObservers;
	
	private AuthenticatorRemote authenticator = null;
	private Session session = null;

	public MasterFrame(String title) {
		super(title);
		setAP(new AppPreferences());
		setPartsTableModel(new PartsTableModel(AP));
		setInvTableModel(new InvTableModel(AP));
		setProductsTableModel(new ProductsTableModel(AP));
		
		initAuth();
		initObservers();

		createBackground();
		createUI();
		
		updateMenuBar();
		createDesktop();
		
		initShutDownHook();
        
		// Display login window when program is launched
		AuthenticatorView authView = new AuthenticatorView(this);
		new AuthenticatorController(authView, MasterFrame.this);
	}

	/*
	 * Creates and displays the JFrame
	 */
	public static void createAndShowGUI() {
		MasterFrame frame = new MasterFrame("Cabinetron Managment Tool");
		frame.setSize(1024, 768);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
	
	/*
	 * Grabs authentication bean, so that it may be used to authenticate users
	 */
	public void initAuth() {
		try {
			Properties props = new Properties();
			props.put("org.omg.CORBA.ORBInitialHost", "localhost");
			props.put("org.omg.CORBA.ORBInitialPort", "3700");

			InitialContext itx = new InitialContext(props);
			authenticator = (AuthenticatorRemote) itx
					.lookup("java:global/StockControl_Beans/Authenticator!core.session.models.remote.AuthenticatorRemote");
		} catch (NamingException e1) {
			e1.printStackTrace();
		}
	}
	
	/*
	 * Initializes remote and local observers
	 */
	public void initObservers() {
		allInvItemLogObservers = new ArrayList<InvItemLogObserver>();
		
		// create an observer for this model and register it with the remote EJB
		try {
			setInvItemLogObserver(new InvItemLogObserver(this));
			allInvItemLogObservers.add(invItemLogObserver);
			getInvTableModel().registerLogObserver(getInvItemLogObserver());
		} catch (RemoteException e1) {
			e1.printStackTrace();
		}
	}
	
	/*
	 * Creates the Background
	 */
	public void createBackground() {
		try {
			backgroundImage = ImageIO.read(new File("img/cabinetron_logo_transparent.png"));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	/*
	 * Creates the UI the application will use and sets it
	 */
	public void createUI() {
		LookAndFeelInfo[] lookNFeels = UIManager.getInstalledLookAndFeels();
		
		try {
			for (LookAndFeelInfo info : lookNFeels) {
				if ("Nimbus".equals(info.getName())) {
					UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (Exception e1) {
			System.out.println("Nimbus Look & Feel not available, setting UI to use default system Look & Feel");
			
			try {
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			} catch (Exception e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			} 
		}
	}
	
	/*
	 * Create the MDI desktop: 
	 * A specialized layered pane to be used with JInternalFrames
	 */
	public void createDesktop() {
		desktop = new JDesktopPane() {
			private static final long serialVersionUID = 1L;

			@Override
			protected void paintComponent(Graphics grphcs) {
				Dimension frameSize = getContentPane().getSize();

				int dx = ((frameSize.width / 2)) - 400;
				int dy = ((frameSize.height / 2)) - 225;

				super.paintComponent(grphcs);
				grphcs.setColor(new Color(197, 239, 247));
				grphcs.fillRect(0, 0, getWidth(), getHeight());
				grphcs.drawImage(backgroundImage, dx, dy, null);
			}

			@Override
			public Dimension getPreferredSize() {
				return new Dimension(backgroundImage.getWidth(),
						backgroundImage.getHeight());
			}
		};
		add(desktop);
	}
	
	/*
	 * Updates the JMenuBar by calling for a new instance 
	 * that will contain an updated state
	 */
	public void updateMenuBar() {
		masterMenuBar = new MasterMenuBar(this);
	}
	
	/*
	 * This sets the operations that will take place when the program exits
	 * It will make sure that the program exits gracefully
	 */
	public void initShutDownHook() {
		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				if (MasterFrame.this.getInvTableModel() != null) {
					MasterFrame.this.getInvTableModel().exitModule();
				}
				if (MasterFrame.this.getProductsTableModel() != null) {
					MasterFrame.this.getProductsTableModel().exitModule();
				}
				if (MasterFrame.this.getPartsTableModel() != null) {
					MasterFrame.this.getPartsTableModel().exitModule();
				}
				
				// Unregister all invItemLogObservers if they are still registered
				for(InvItemLogObserver logObsvr : allInvItemLogObservers){
					MasterFrame.this.getInvTableModel().unregisterLogObserver(logObsvr);
				}
				
				System.out.println("All InvItemLogObservers have been unregistered.");
			}
		});
	}
	
	/*
	 * Display a child's message in a dialog centered on MDI frame
	 */
	public void displayChildMessage(String msg) {
		JOptionPane.showInternalMessageDialog(this.getContentPane(), msg);
	}

	/*
	 *  Display a child's yes/no message in a dialog centered on MDI frame
	 */
	public boolean displayChildMessageOption(String titleBar, String infoMsg) {
		int dialogButton = JOptionPane.YES_NO_OPTION;

		int dialogResult = JOptionPane.showInternalConfirmDialog(
				this.getContentPane(), infoMsg, titleBar, dialogButton);
		if (dialogResult == JOptionPane.YES_OPTION) {
			return true;
		}

		return false;
	}

	/*
	 * Display a child's yes/no message in a dialog centered on MDI frame
	 */
	public int displayChildMessageOptionCustom(String titleBar, String infoMsg,
			Object[] options) {
		int dialogResult = JOptionPane.showInternalOptionDialog(
				this.getContentPane(), infoMsg, titleBar,
				JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null,
				options, options[0]);

		return dialogResult;
	}
	
	/*
	 * All Getters/Setters for MasterFrame
	 */
	
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

	public AuthenticatorRemote getAuthenticator() {
		return authenticator;
	}

	public JMenuItem getLoginMenuItem() {
		return loginMenuItem;
	}

	public void setLoginMenuItem(JMenuItem menuItem) {
		loginMenuItem = menuItem;
	}
	
	public ArrayList<InvItemLogObserver> getAllInvItemLogObservers() {
		return allInvItemLogObservers;
	}

	public void setAllInvItemLogObservers(
			ArrayList<InvItemLogObserver> allInvItemLogObservers) {
		this.allInvItemLogObservers = allInvItemLogObservers;
	}
	
	public InvItemLogObserver getInvItemLogObserver() {
		return invItemLogObserver;
	}

	public void setInvItemLogObserver(InvItemLogObserver invItemLogObserver) {
		this.invItemLogObserver = invItemLogObserver;
	}
}