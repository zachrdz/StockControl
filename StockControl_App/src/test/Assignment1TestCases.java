/**
 * 
 */
package test;

import static org.junit.Assert.*;

import org.junit.Test;

import core.partsModule.dao.PartsGateway;
import core.partsModule.models.obj.PartItem;
import core.settings.models.AppPreferences;

/**
 * @author hgv265
 *
 */
public class Assignment1TestCases {

	/**
     * Test to see if a part is created correctly
     * 
     */
    @SuppressWarnings("unused")
	@Test
    public void testPartCreation() {
        boolean nullError = false;
        PartItem newPart = null;
        try {
        	newPart = new PartItem();
        } catch (NullPointerException e) {
            nullError = true;
        }
        assertFalse("Part object was not successfully created.", nullError);
    }
    
    /**
     * Test to see if a part object contains all elements
     * 
     */
    @Test
    public void testPartElements() {
        boolean nullError = false;
        PartItem newPart = null;
        try {
        	newPart = new PartItem(5, "Test Part Number", "Test Part Ext Number", "Test Part Name", "Test Part Vendor", "Unknown");

        	System.out.print("{PartID : " + newPart.getPartID() + ", ");
        	System.out.print("PartNo : " + newPart.getPartNo() + ", ");
        	System.out.print("PartNoExt : " + newPart.getPartNoExternal() + ", ");
        	System.out.print("PartName : " + newPart.getPartName() + ", ");
        	System.out.print("PartVendor : " + newPart.getPartVendor() + ", ");
        	System.out.print("PartQuantityUnit : " + newPart.getPartQuantityUnit() + ", ");
        	System.out.println();
        } catch (NullPointerException e) {
            nullError = true;
        }
        assertFalse("Part object elements were not successfully created.", nullError);
    }
    
    /**
     * Test to see if able to connect to DB
     * 
     */
    @Test
    public void testDBConnection() {
        boolean conError = false;
        try {
        	PartsGateway gateway = new PartsGateway(new AppPreferences());
        	gateway.doClose();
        } catch (Exception e) {
			// TODO Auto-generated catch block
        	conError = true;
			e.printStackTrace();
		}
        assertFalse("Error connecting to Database", conError);
    }

}
