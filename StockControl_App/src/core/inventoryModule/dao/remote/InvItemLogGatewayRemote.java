package core.inventoryModule.dao.remote;

import java.util.ArrayList;

import javax.ejb.Remote;

import core.inventoryModule.models.InvItemLogObserverRemote;
import core.inventoryModule.models.obj.InvItemLogRecord;

@Remote
public interface InvItemLogGatewayRemote {
	public ArrayList<InvItemLogRecord> doRead(int invID);
	public void doAdd(InvItemLogRecord invItemLogRec);
	public void registerObserver(InvItemLogObserverRemote o);
	public void unregisterObserver(InvItemLogObserverRemote o);
}
