package core.inventoryModule.dao;

import java.util.ArrayList;

import core.inventoryModule.models.obj.InvItemLogRecord;

public interface InvLoggingDao {
	public ArrayList<InvItemLogRecord> doRead(int invID);
	public void doAdd(InvItemLogRecord invItemLogRec);
}
