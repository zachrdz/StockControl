/**
 * 
 */
package core.partsModule.interfaces;

import java.sql.ResultSet;
import java.util.ArrayList;

import core.partsModule.models.PartItem;

/**
 * @author Zach
 *
 */
public interface PartsDao {
	public ResultSet doRead();
	public void doDelete(PartItem part);
	public void doAdd(PartItem part);
	public void doUpdate(PartItem part);
	public boolean doCheckDuplicateByNo(PartItem part);
	public boolean doCheckDuplicateByName(PartItem part);
	public boolean doCheckInvAssocByPartID(PartItem part);
	public ArrayList<PartItem> doGetPartsList();
	public void setLastUpdateTS();
	public boolean doPollChanges();
	public void doClose();
}
