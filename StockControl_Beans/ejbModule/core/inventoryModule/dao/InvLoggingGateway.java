package core.inventoryModule.dao;

import redis.clients.jedis.Jedis;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import core.inventoryModule.models.obj.InvItemLogRecord;

public class InvLoggingGateway implements InvLoggingDao, Serializable{
	private static final long serialVersionUID = 1L;
	private Jedis jedis;
	private ArrayList<InvItemLogRecord> InvItemLog = null;
	
	public InvLoggingGateway(){
		String connectionResponse = null;
		
		//Connect to Redis server
		jedis = new Jedis("45.55.142.106");
		connectionResponse = jedis.auth("Vi2F~RB,*VzuZ4']i^,&7jeua");
		
		jedis.select(0);
		System.out.println(connectionResponse);
	}
	
	/**
	 * Get the entire log record for a specific inventory item
	 */
	@Override
	public ArrayList<InvItemLogRecord> doRead(int invID) {
		// Reset list to empty
		InvItemLog = null;
		
		InvItemLogRecord tmp;
		
		//Loop through the keys, create invLogItem objects and store them in an ArrayList
		//Grab all hash keys that match this pattern
		for(String key : jedis.keys("invLogItem:"+ invID +"*")){
			tmp = new InvItemLogRecord();
			tmp.setInvEntryDate(jedis.hget(key, "entryDate"));
			tmp.setInvEntryDesc(jedis.hget(key, "entryDesc"));
			
			InvItemLog.add(tmp);
		}
		
		return InvItemLog;
	}
	
	/**
	 * Add a log record to an inventory item
	 */
	@Override
	public void doAdd(InvItemLogRecord invItemLogRec) {
		String invLogItemCount = jedis.get("invLogItemCount");
		String key = "invLogItem:" + invItemLogRec.getInvID() + ":" + invLogItemCount;
		
		Map<String, String> map = new HashMap<String, String>();
		
		map.put("entryDate", invItemLogRec.getInvEntryDate());
		map.put("entryDesc", invItemLogRec.getInvEntryDesc());
		
		jedis.hmset(key, map);
		jedis.incr("invLogItemCount");
	}

	
}
