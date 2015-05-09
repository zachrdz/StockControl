package core.inventoryModule.dao;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Tuple;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Set;

import javax.annotation.PreDestroy;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;

import core.inventoryModule.dao.remote.InvItemLogGatewayRemote;
import core.inventoryModule.models.InvItemLogObserverRemote;
import core.inventoryModule.models.obj.InvItemLogRecord;

@Singleton(name = "InvItemLogGateway")
public class InvItemLogGateway implements InvItemLogGatewayRemote, Serializable {
	private static final long serialVersionUID = 1L;
	private Jedis jedis;
	private ArrayList<InvItemLogRecord> InvItemLog = null;
	private ArrayList<InvItemLogObserverRemote> observers;

	public InvItemLogGateway() {
		String connectionResponse = null;

		// Connect to my Redis server
		jedis = new Jedis("45.55.142.106");
		connectionResponse = jedis.auth("Vi2F~RB,*VzuZ4]i^,&7jeua");

		jedis.select(0);
		if (connectionResponse.equals("OK")) {
			System.out.println("EJB Redis DB connection for InvItemLogGateway successful.");
		} else {
			System.out.println("EJB Redis DB connection for InvItemLogGateway failed.");
		}

		observers = new ArrayList<InvItemLogObserverRemote>();
	}

	/**
	 * Get the entire log record for a specific inventory item
	 */
	@Override
	public ArrayList<InvItemLogRecord> doRead(int invID) {
		// Reset list to empty
		InvItemLog = new ArrayList<InvItemLogRecord>();
		
		Set<Tuple> logItems = jedis.zrevrangeWithScores("invLogItem:"+invID, 0, -1);
		
		logItems.toArray();
		
		// Place holders for iteration
		Date date = null;
		String desc = null;
		InvItemLogRecord tmpLogRec = new InvItemLogRecord();
		
		for(Tuple value : logItems){
			date = new Date((long) value.getScore());
			
			desc = value.getElement();
			tmpLogRec.setInvID(invID);
			tmpLogRec.setInvEntryDate(date);
			tmpLogRec.setInvEntryDesc(desc);
			
			InvItemLog.add(tmpLogRec);
			
			// Reset place holders before next iteration
			date = null;
			desc = null;
			tmpLogRec = new InvItemLogRecord();
		}

		return InvItemLog;
	}

	/**
	 * Add a log record to an inventory item
	 */
	@Override
	public void doAdd(InvItemLogRecord invItemLogRec) {
		jedis.zadd("invLogItem:" + invItemLogRec.getInvID(), 
				invItemLogRec.getInvEntryDate().getTime(), 
				invItemLogRec.getInvEntryDesc()
				);

		notifyObservers("UPDATE");
	}

	/**
	 * Close database connection once bean is no longer being used Clients do
	 * not have remote access to this method
	 */
	@PreDestroy
	public void doClose() {
		// TODO Auto-generated method stub
		jedis.close();
		System.out.println("EJB Redis DB connection for InvItemLogGateway has been closed");
	}

	/**
	 * Notify registered observers that a change has been made and they need to
	 * update their views
	 * 
	 * @param msg
	 */
	private void notifyObservers(String msg) {
		try {
			for (int i = 0; i < observers.size(); i++) {
				((InvItemLogObserverRemote) observers.get(i)).callback(msg);
			}
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	@Lock(LockType.WRITE)
	public void registerObserver(InvItemLogObserverRemote o) {
		observers.add(o);
	}

	@Lock(LockType.WRITE)
	public void unregisterObserver(InvItemLogObserverRemote o) {
		observers.remove(o);
	}

}
