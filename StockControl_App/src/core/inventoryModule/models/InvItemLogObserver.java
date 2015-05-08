package core.inventoryModule.models;

import java.rmi.RemoteException;
import javax.rmi.PortableRemoteObject;

/*
 * InvItemLogObserver: client-side implementation of the observer
 * 				when remote EJB executes callback, 
 * 				the observer updates master's status bar
 */
public class InvItemLogObserver implements InvItemLogObserverRemote {
	
	//master is the real consumer of the notification
	private core.mdi.models.MasterFrame master = null;
	
	//exportObject turns this class into a little client-side server 
	//so that the remote EJB can call it back
	public InvItemLogObserver(core.mdi.models.MasterFrame mp) throws RemoteException {
		PortableRemoteObject.exportObject(this);
		master = mp;
	}

	//callback the method that the EJB remotely calls
	@Override
	public void callback(String data) throws RemoteException {
		System.out.print("A global change to Inventory Items has been detected, reloading data.");
		master.getInvTableModel().refreshTableData(master.getInvTableModel().getLocFilter());
	}
	
}