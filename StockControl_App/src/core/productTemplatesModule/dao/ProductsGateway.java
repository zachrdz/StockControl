/**
 * 
 */
package core.productTemplatesModule.dao;

/**
 * @author Zach
 *
 */
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import core.productTemplatesModule.models.ProductItem;
import core.settings.models.AppPreferences;

public class ProductsGateway implements ProductsDao{
	
	private Connection connection;
	private Date lastReadTS;
	private int logRecordCount = 0;
	
	public ProductsGateway(AppPreferences AP){
		//set up the driver
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			System.out.println("Where is your MySQL JDBC Driver?");
			e.printStackTrace();
			return;
		}
		//System.out.println("MySQL JDBC driver registered.");
		
		try {
			this.connection = DriverManager.getConnection(AP.getUrl(),AP.getUsername(), AP.getPassword());
			System.out.println("DB connection for ProductsGateway successful.");
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	public ResultSet doRead(){
		String callString = "{call read_ProductTemplatesModule()}";
		ResultSet rs = null;
		
		try {
			CallableStatement callstate = this.connection.prepareCall(callString);
			rs = callstate.executeQuery();
			
			setLastUpdateTS();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return rs;
	}

	public void doDelete(ProductItem product){
		String callString = "{call delete_ProductByID(?)}";
		
		try {
			CallableStatement callstate = this.connection.prepareCall(callString);
			callstate.setInt(1,product.getProductID());
			callstate.executeUpdate();
			
			System.out.println("Record with productID " + product.getProductID() + " deleted from ProductTemplatesModule table.");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void doAdd(ProductItem product){
		if(!doCheckDuplicateByNo(product)){
			String callString = "{call add_Product(?,?)}";

			try {
				CallableStatement callstate = this.connection.prepareCall(callString);
				callstate.setString(1, product.getProductNo());
				callstate.setString(2, product.getProductDesc());
				
				callstate.executeUpdate();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	
	public void doUpdate(ProductItem product){
		String callString = "{call update_ProductByID(?,?,?)}";
		
		try {
			CallableStatement callstate = this.connection.prepareCall(callString);
			callstate.setInt(1, product.getProductID());
			callstate.setString(2, product.getProductNo());
			callstate.setString(3, product.getProductDesc());
			
			callstate.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public boolean doCheckDuplicateByNo(ProductItem product) {
		String callString = "{call chkDup_ProductByNo(?)}";
		
		try {
			CallableStatement callstate = this.connection.prepareCall(callString);
			callstate.setString(1, product.getProductNo());
			ResultSet rs = callstate.executeQuery();
			
			while(rs.next()){
				//System.out.println("Duplicate Entry Product No!");
				return true;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		//No duplicates found
		return false;
	}

	public ArrayList<ProductItem> doGetProductList(){
		ResultSet rs = doRead();

		System.out.println("Product Templates List Updated");
		ArrayList<ProductItem> productList = new ArrayList<ProductItem>();
		try {
			while(rs.next()){
				ProductItem product = new ProductItem();
				product.setProductID(rs.getInt("productID"));
				product.setProductNo(rs.getString("productNo"));
				product.setProductDesc(rs.getString("productDesc"));
				productList.add(product);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return productList;
	}

	public void setLastUpdateTS(){
		String callString = "{call get_CSTServerTime()}";
		ResultSet timeresult = null;
		
		try {
			CallableStatement callstate = this.connection.prepareCall(callString);
			timeresult = callstate.executeQuery();
			
			String currentTime = null;
			
			while (timeresult.next())
            {
				currentTime = timeresult.getString("ts");
				break;
            }

			lastReadTS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(currentTime);
			System.out.println("Products Table synced: " + lastReadTS);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public boolean doPollChanges(){
		
		String query = "SELECT log_createTimeStamp,log_updateTimeStamp FROM ProductTemplatesModuleLog";
		ResultSet rs = null;
		int tmpLogRecCount = 0;
		
		try {
			PreparedStatement ps = this.connection.prepareStatement(query);
			rs = ps.executeQuery();
			
			while(rs.next()){
				String createTS = rs.getString("log_createTimeStamp");
				String updateTS = rs.getString("log_updateTimeStamp");

				Date cTS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(createTS);
				Date uTS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(updateTS);
				
				if(cTS.after(lastReadTS) || uTS.after(lastReadTS)){
					System.out.println("Changes to table found! Last Update: " + lastReadTS + " createTS: " + cTS + " updateTS: " + uTS);
					return true; //changes found
				}
				tmpLogRecCount++;
			}
			if(logRecordCount == 0){ logRecordCount = tmpLogRecCount; }
			if((logRecordCount != 0) && (logRecordCount != tmpLogRecCount)){
				logRecordCount = tmpLogRecCount; return true; //ChangesFound
			}
		
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return false; //no changes to product templates module table found
		
	}

	public void doClose(){
		try {
			connection.close();
			System.out.println("DB connection for ProductsGateway has been closed.");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
