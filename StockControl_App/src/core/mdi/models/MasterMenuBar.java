package core.mdi.models;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import core.inventoryModule.controllers.InvTableController;
import core.inventoryModule.views.InvTableView;
import core.partsModule.controllers.PartsTableController;
import core.partsModule.views.PartsTableView;
import core.productTemplatesModule.controllers.ProductsTableController;
import core.productTemplatesModule.views.ProductsTableView;
import core.session.controllers.AuthenticatorController;
import core.session.models.obj.Function;
import core.session.views.AuthenticatorView;

public class MasterMenuBar {
	private MasterFrame m;

	public MasterMenuBar(MasterFrame m) {
		this.m = m;
		createMenuBar();
	}

	public void createMenuBar() {
		Image resizedCheckImage = null, resizedXImage = null;
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

		// create menu for adding inner frames
		JMenuBar customMenuBar = new JMenuBar();
		JMenu menu = new JMenu("File");
		JMenuItem menuItem;

		if (null == m.getSession()) {
			m.getLoginMenuItem();
			m.setLoginMenuItem(new JMenuItem("Login"));
			m.getLoginMenuItem().addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					AuthenticatorView authView = new AuthenticatorView(m);
					new AuthenticatorController(authView, m);
					m.getLoginMenuItem().setVisible(false);
				}
			});
			m.getLoginMenuItem().setVisible(false);
			menu.add(m.getLoginMenuItem());

			menuItem = new JMenuItem("Quit");
			menuItem.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					m.dispatchEvent(new WindowEvent(m,
							WindowEvent.WINDOW_CLOSING));
				}
			});
			menu.add(menuItem);
		}
		customMenuBar.add(menu);

		String UserNameInfoText = "Not Logged In";
		String UserRoleInfoText = "";

		if (null != m.getSession()) {
			menuItem = new JMenuItem("Logout");
			menuItem.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					m.setSession(null);
					m.getInvTableModel().refreshTableData("");
					for (JInternalFrame frame : m.getDesktop().getAllFrames()) {
						frame.dispose();
					}
					
					createMenuBar();

					AuthenticatorView authView = new AuthenticatorView(m);
					new AuthenticatorController(authView, m);
				}
			});
			menu.add(menuItem);

			menu = new JMenu("Modules");
			menuItem = new JMenuItem("Parts");
			menuItem.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					PartsTableView ptView = new PartsTableView(m);
					new PartsTableController(ptView, m);
				}
			});
			if (!m.getSession().getUserFunctions()
					.contains(new Function("canViewParts"))) {
				menuItem.setEnabled(false);
			}
			menu.add(menuItem);

			menuItem = new JMenuItem("Inventory");
			menuItem.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					InvTableView itView = new InvTableView(m);
					new InvTableController(itView, m);
				}
			});
			if (!m.getSession().getUserFunctions()
					.contains(new Function("canViewInventory"))) {
				menuItem.setEnabled(false);
			}
			menu.add(menuItem);

			menuItem = new JMenuItem("Product Templates");
			menuItem.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					ProductsTableView ptView = new ProductsTableView(m);
					new ProductsTableController(ptView, m);
				}
			});
			if (!m.getSession().getUserFunctions()
					.contains(new Function("canViewProductTemplates"))) {
				menuItem.setEnabled(false);
			}
			menu.add(menuItem);

			UserNameInfoText = m.getSession().getUser().getFullName();
			UserRoleInfoText = "(" + m.getSession().getUserRole().getRoleName()
					+ ")";
		}

		customMenuBar.add(menu);

		customMenuBar.add(Box.createHorizontalGlue());
		JMenu UserNameInfo = new JMenu(UserNameInfoText);
		JMenu UserRoleInfo = new JMenu(UserRoleInfoText);
		customMenuBar.add(UserNameInfo);
		customMenuBar.add(UserRoleInfo);

		if (null != m.getSession()) {
			menuItem = new JMenuItem("Email: "
					+ m.getSession().getUser().getEmail());
			// menuItem.setEnabled(false);
			UserNameInfo.add(menuItem);

			for (Function function : m.getSession().getAllFunctions()) {
				menuItem = new JMenuItem(function.getFunctionName());
				boolean exists = false;
				for (Function subFunction : m.getSession().getUserFunctions()) {
					if (subFunction.getFunctionID() == function.getFunctionID()) {
						exists = true;
						break;
					}
				}

				if (exists) {
					menuItem.setIcon(checkIcon);
				} else {
					menuItem.setIcon(xIcon);
				}

				// menuItem.setEnabled(false);
				UserRoleInfo.add(menuItem);

			}
		}

		m.setJMenuBar(customMenuBar);
		customMenuBar.validate();
		customMenuBar.repaint();
	}
}
