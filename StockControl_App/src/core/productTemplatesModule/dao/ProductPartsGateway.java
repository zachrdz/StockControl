/**
 * 
 */
package core.productTemplatesModule.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import core.productTemplatesModule.interfaces.ProductPartsDao;
import core.productTemplatesModule.models.ProductPartItem;
import core.productTemplatesModule.models.ProductPartItemExt;
import core.settings.models.AppPreferences;

/**
 * @author Zach
 *
 */
public class ProductPartsGateway implements ProductPartsDao{

	private Connection connection;
	private Date lastReadTS;
	
	public ProductPartsGateway(AppPreferences AP){		
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
			System.out.println("DB connection for ProductPartsGateway successful.");
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	public ResultSet doRead(int productID){
		String callString = "{call read_ProductTemplatesParts(?)}";
		ResultSet rs = null;
		
		try {
			CallableStatement callstate = this.connection.prepareCall(callString);
			callstate.setInt(1,productID);
			rs = callstate.executeQuery();
			
			setLastUpdateTS();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return rs;
	}
	
	public ResultSet doReadExt(int productID){
		String callString = "{call read_ProductTemplatesPartsExt(?)}";
		ResultSet rs = null;
		
		try {
			CallableStatement callstate = this.connection.prepareCall(callString);
			callstate.setInt(1,productID);
			rs = callstate.executeQuery();
			
			setLastUpdateTS();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return rs;
	}

	public void doDelete(ProductPartItem productPart){
		String callString = "{call delete_ProductPartByID(?)}";
		
		try {
			CallableStatement callstate = this.connection.prepareCall(callString);
			callstate.setInt(1,productPart.getPtpID());
			callstate.executeUpdate();
			
			System.out.println("Record with ptpID " + productPart.getPtpID() + " deleted from ProductTemplatesParts table.");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void doAdd(ProductPartItem productPart){
		if(!doCheckDuplicate(productPart)){
			String callString = "{call add_ProductPart(?,?,?)}";

			try {
				CallableStatement callstate = this.connection.prepareCall(callString);
				callstate.setInt(1, productPart.getProductID());
				callstate.setInt(2, productPart.getPartID());
				callstate.setInt(3, productPart.getQuantity());
				
				callstate.executeUpdate();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	
	public void doUpdate(ProductPartItem productPart){
		String callString = "{call update_ProductPartByID(?,?,?,?)}";
		
		try {
			CallableStatement callstate = this.connection.prepareCall(callString);
			callstate.setInt(1, productPart.getPtpID());
			callstate.setInt(2, productPart.getProductID());
			callstate.setInt(3, productPart.getPartID());
			callstate.setInt(4, productPart.getQuantity());
			
			callstate.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public boolean doCheckDuplicate(ProductPartItem productPart) {
		String callString = "{call chkDup_ProductPart(?,?)}";
		
		try {
			CallableStatement callstate = this.connection.prepareCall(callString);
			callstate.setInt(1, productPart.getProductID());
			callstate.setInt(2, productPart.getPartID());
			ResultSet rs = callstate.executeQuery();
			
			while(rs.next()){
				//System.out.println("Duplicate Entry Product Part!");
				return true;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		//No duplicates found
		return false;
	}
	
	public ArrayList<ProductPartItem> doGetProductPartList(int productID){
		ResultSet rs = doRead(productID);

		System.out.println("Product Parts List Updated");
		ArrayList<ProductPartItem> productPartList = new ArrayList<ProductPartItem>();
		try {
			while(rs.next()){
				ProductPartItem productPart = new ProductPartItem();
				productPart.setPtpID(rs.getInt("ptpID"));
				productPart.setProductID(rs.getInt("productID"));
				productPart.setPartID(rs.getInt("partID"));
				productPart.setQuantity(rs.getInt("quantity"));
				productPartList.add(productPart);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return productPartList;
	}
	
	public ArrayList<ProductPartItemExt> doGetProductPartListExt(int productID){
		ResultSet rs = doReadExt(productID);

		System.out.println("Product Parts List Updated");
		ArrayList<ProductPartItemExt> productPartList = new ArrayList<ProductPartItemExt>();
		try {
			while(rs.next()){
				ProductPartItemExt productPart = new ProductPartItemExt();
				productPart.setPtpID(rs.getInt("ptpID"));
				productPart.setProductID(rs.getInt("productID"));
				productPart.setPartID(rs.getInt("partID"));
				productPart.setQuantity(rs.getInt("quantity"));
				productPart.setPartNo(rs.getString("partNo"));
				productPart.setPartNoExternal(rs.getString("partNoExternal"));
				productPart.setPartName(rs.getString("partName"));
				productPart.setPartVendor(rs.getString("partVendor"));
				productPart.setPartQuantityUnit(rs.getString("partQuantityUnit"));
				productPartList.add(productPart);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return productPartList;
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
			System.out.println("ProductParts Table synced: " + lastReadTS);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public void doClose(){
		try {
			connection.close();
			System.out.println("DB connection for ProductPartsGateway has been closed.");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
