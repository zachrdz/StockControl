/**
 * 
 */
package test;

import static org.junit.Assert.assertFalse;

import org.junit.Test;

import core.productTemplatesModule.dao.ProductPartsGateway;
import core.productTemplatesModule.dao.ProductsGateway;
import core.settings.models.AppPreferences;

/**
 * @author hgv265
 *
 */
public class Assignment6TestCases {
	/**
     * Test to see if able to connect to DB
     * 
     */
    @Test
    public void testDBConnection1() {
        boolean conError = false;
        try {
        	ProductsGateway gateway = new ProductsGateway(new AppPreferences());
        	gateway.doClose();
        } catch (Exception e) {
			// TODO Auto-generated catch block
        	conError = true;
			e.printStackTrace();
		}
        assertFalse("Error connecting to Database", conError);
    }
    
    /**
     * Test to see if able to connect to DB
     * 
     */
    @Test
    public void testDBConnection2() {
        boolean conError = false;
        try {
        	ProductPartsGateway gateway = new ProductPartsGateway(new AppPreferences());
        	gateway.doClose();
        } catch (Exception e) {
			// TODO Auto-generated catch block
        	conError = true;
			e.printStackTrace();
		}
        assertFalse("Error connecting to Database", conError);
    }
}
