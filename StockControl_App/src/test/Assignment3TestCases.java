/**
 * 
 */
package test;

import static org.junit.Assert.*;

import org.junit.Test;

import core.inventoryModule.dao.InvGateway;
import core.inventoryModule.models.InvItem;
import core.inventoryModule.models.InvItemExt;
import core.settings.models.AppPreferences;

/**
 * @author hgv265
 *
 */
public class Assignment3TestCases {

	/**
     * Test to see if a inv item is created correctly
     * 
     */
    @SuppressWarnings("unused")
	@Test
    public void testInvCreation() {
        boolean nullError = false;
        InvItem newInv = null;
        try {
        	newInv = new InvItem();
        } catch (NullPointerException e) {
            nullError = true;
        }
        assertFalse("Inv object was not successfully created.", nullError);
    }
    
    /**
     * Test to see if a invExt item is created correctly
     * 
     */
    @SuppressWarnings("unused")
	@Test
    public void testInvExtCreation() {
        boolean nullError = false;
        InvItem newInvExt = null;
        try {
        	newInvExt = new InvItemExt();
        } catch (NullPointerException e) {
            nullError = true;
        }
        assertFalse("InvExt object was not successfully created.", nullError);
    }
    
    /**
     * Test to see if a inv object contains all elements
     * 
     */
    @Test
    public void testInvElements() {
        boolean nullError = false;
        InvItem newInv = null;
        try {
        	newInv = new InvItem(0, 0, "Location", 0);

        	System.out.print("{InvID : " + newInv.getInvID() + ", ");
        	System.out.print("InvPartID : " + newInv.getInvPartID() + ", ");
        	System.out.print("InvLocation : " + newInv.getInvLocation() + ", ");
        	System.out.print("InvQuantity : " + newInv.getInvQuantity() + "}");
        	System.out.println();
        } catch (NullPointerException e) {
            nullError = true;
        }
        assertFalse("Inv object elements were not successfully created.", nullError);
    }
    
    /**
     * Test to see if a invExt object contains all elements
     * 
     */
    @Test
    public void testInvExtElements() {
        boolean nullError = false;
        InvItemExt newInv = null;
        try {
        	newInv = new InvItemExt(0, 0, "Location", 0, "", "", "", "", "","","");

        	System.out.print("{InvID : " + newInv.getInvID() + ", ");
        	System.out.print("InvPartID : " + newInv.getInvPartID() + ", ");
        	System.out.print("InvLocation : " + newInv.getInvLocation() + ", ");
        	System.out.print("InvQuantity : " + newInv.getInvQuantity() + ", ");
        	
        	System.out.print("PartNo : " + newInv.getPartNo() + ", ");
        	System.out.print("PartNoExternal : " + newInv.getPartNoExternal() + ", ");
        	System.out.print("PartName: " + newInv.getPartName() + ", ");
        	System.out.print("PartVendor : " + newInv.getPartVendor() + ", ");
        	System.out.print("PartQuantityUnit : " + newInv.getPartQuantityUnit() + "}");
        	System.out.println();
        } catch (NullPointerException e) {
            nullError = true;
        }
        assertFalse("InvExt object elements were not successfully created.", nullError);
    }
    
    /**
     * Test to see if able to connect to DB
     * 
     */
    @Test
    public void testDBConnection() {
        boolean conError = false;
        try {
        	InvGateway gateway = new InvGateway(new AppPreferences());
        	gateway.doClose();
        } catch (Exception e) {
			// TODO Auto-generated catch block
        	conError = true;
			e.printStackTrace();
		}
        assertFalse("Error connecting to Database", conError);
    }

}
