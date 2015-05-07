package core.inventoryModule.remote;

import java.util.ArrayList;

import javax.ejb.Remote;

import core.inventoryModule.models.obj.InvItemLogRecord;

@Remote
public interface InvLoggingBeanRemote {
	public void addInvItemLogRecord(InvItemLogRecord invItemLogRec);
	public ArrayList<InvItemLogRecord> getInvItemLog(int invID);
}
