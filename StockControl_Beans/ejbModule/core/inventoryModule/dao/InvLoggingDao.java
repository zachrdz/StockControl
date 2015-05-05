package core.inventoryModule.dao;

import java.util.ArrayList;

import core.inventoryModule.models.obj.InvLogItem;

public interface InvLoggingDao {
	public ArrayList<InvLogItem> doRead();
	public InvLogItem doGetLogItemByID(int id);
}
