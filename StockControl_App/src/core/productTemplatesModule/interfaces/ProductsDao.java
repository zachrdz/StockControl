/**
 * 
 */
package core.productTemplatesModule.interfaces;

import java.sql.ResultSet;
import java.util.ArrayList;

import core.productTemplatesModule.models.ProductItem;

/**
 * @author Zach
 *
 */
public interface ProductsDao {
	public ResultSet doRead();
	public void doDelete(ProductItem product);
	public void doAdd(ProductItem product);
	public void doUpdate(ProductItem product);
	public boolean doCheckDuplicateByNo(ProductItem product);
	public ArrayList<ProductItem> doGetProductList();
	public void setLastUpdateTS();
	public boolean doPollChanges();
	public void doClose();
}
