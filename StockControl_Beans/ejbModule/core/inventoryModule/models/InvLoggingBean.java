package core.inventoryModule.models;

import java.io.Serializable;
import java.util.ArrayList;

import core.inventoryModule.dao.InvLoggingGateway;
import core.inventoryModule.models.obj.InvItemLogRecord;
import core.inventoryModule.remote.InvLoggingBeanRemote;

import javax.ejb.Singleton;

/**
 * Session Bean implementation class InvModuleBean
 */
@Singleton(name="InvLoggingBean")
public class InvLoggingBean implements InvLoggingBeanRemote,Serializable {
	private static final long serialVersionUID = 1L;
	private InvLoggingGateway gateway;
	
    public InvLoggingBean() {
        gateway = new InvLoggingGateway();
    }
    
    @Override
	public ArrayList<InvItemLogRecord> getInvItemLog(int invID) {
		return gateway.doRead(invID);
	}
    
	@Override
	public void addInvItemLogRecord(InvItemLogRecord invItemLogRec) {
		gateway.doAdd(invItemLogRec);
	}

	

}
