package core;

import core.settings.views.LoginInfoView;
import core.mdi.MasterFrame;

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
