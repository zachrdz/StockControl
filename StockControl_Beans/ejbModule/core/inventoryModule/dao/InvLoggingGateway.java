package core.inventoryModule.dao;

import redis.clients.jedis.Jedis;
import java.util.ArrayList;

import core.inventoryModule.models.obj.InvLogItem;

public class InvLoggingGateway implements InvLoggingDao{
	private Jedis jedis;
	
	public InvLoggingGateway(){
		//Connect to redis server
		jedis = new Jedis("");
		jedis.auth("");
		jedis.select(0);
	}
	
	@Override
	public ArrayList<InvLogItem> doRead() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public InvLogItem doGetLogItemByID(int id) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
