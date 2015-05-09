package core.inventoryModule.models;

import java.rmi.RemoteException;
import javax.rmi.PortableRemoteObject;

/*
 * InvItemLogObserver: client-side implementation of the observer
 * 				when remote EJB executes callback, 
 * 				the observer updates master's status bar
 */
public class InvItemLogObserver implements InvItemLogObserverRemote {
	
	//master is a real consumer of the notification
	private core.mdi.models.MasterFrame master = null;
	
	//inventory detail view is a real consumer of the notification
	private core.inventoryModule.views.InvDetailView invDetailView;
	
	//exportObject turns this class into a little client-side server 
	//so that the remote EJB can call it back
	public InvItemLogObserver(core.mdi.models.MasterFrame mp) throws RemoteException {
		PortableRemoteObject.exportObject(this);
		master = mp;
	}
	
	public InvItemLogObserver(core.inventoryModule.views.InvDetailView idv) throws RemoteException {
		PortableRemoteObject.exportObject(this);
		invDetailView = idv;
	}

	//callback the method that the EJB remotely calls
	@Override
	public void callback(String data) throws RemoteException {
		System.out.println("A change to Inventory Items has been detected, registered observers have been notified.");
		
		// Depending on caller, perform different callback action
		
		if(null != invDetailView){
			invDetailView.asyncReloadInvItemLog();
			System.out.println("Log view for invID:" + invDetailView.getInv().getInvID() + " has been reloaded by observer.");
		}
		
		if(null != master){
			master.getInvTableModel().refreshTableData(master.getInvTableModel().getLocFilter());
		}
	}
	
}