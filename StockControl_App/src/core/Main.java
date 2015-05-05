package core;

import core.mdi.models.MasterFrame;
import core.session.views.LoginInfoView;

public class Main {
	// main: launch the JFrame on the EDT
	public static void main(String[] args) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				MasterFrame.createAndShowGUI();
				new LoginInfoView();
			}
		});
	}
}
