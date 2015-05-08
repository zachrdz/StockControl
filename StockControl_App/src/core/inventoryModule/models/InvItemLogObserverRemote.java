package core.inventoryModule.models;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface InvItemLogObserverRemote extends Remote{
	public void callback(String data) throws RemoteException; 
}
