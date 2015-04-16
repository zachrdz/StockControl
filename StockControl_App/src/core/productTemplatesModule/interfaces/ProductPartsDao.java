/**
 * 
 */
package core.productTemplatesModule.interfaces;

import java.sql.ResultSet;
import java.util.ArrayList;

import core.productTemplatesModule.models.ProductPartItem;
import core.productTemplatesModule.models.ProductPartItemExt;

/**
 * @author Zach
 *
 */
public interface ProductPartsDao {
	public ResultSet doRead(int productID);
	public ResultSet doReadExt(int productID);
	public void doDelete(ProductPartItem productPart);
	public void doAdd(ProductPartItem productPart);
	public void doUpdate(ProductPartItem productPart);
	public boolean doCheckDuplicate(ProductPartItem productPart);
	public ArrayList<ProductPartItem> doGetProductPartList(int productID);
	public ArrayList<ProductPartItemExt> doGetProductPartListExt(int productID);
	public void setLastUpdateTS();
	public void doClose();
}
