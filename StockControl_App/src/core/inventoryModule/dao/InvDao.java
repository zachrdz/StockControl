/**
 * 
 */
package core.inventoryModule.dao;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;

import core.inventoryModule.models.obj.InvItem;
import core.inventoryModule.models.obj.InvItemExt;

/**
 * @author Zach
 *
 */
public interface InvDao {
	public ResultSet doRead(String locFilter);
	public ResultSet doReadExt(String locFilter);
	public void doDelete(InvItem inv);
	public void doAdd(InvItem inv);
	public void doUpdate(InvItem inv);
	public boolean doCheckDuplicate(InvItem inv);
	public ArrayList<InvItem> doGetInvList(String locFilter);
	public ArrayList<InvItemExt> doGetInvListExt(String locFilter);
	public void setLastUpdateTS();
	public InvItem doGetInvRec(InvItem inv);
	public Date getInvItemLastUpdateTS(InvItem inv);
	public boolean doPollChanges();
	public void doClose();
}
