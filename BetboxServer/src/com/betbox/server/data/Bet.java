package com.betbox.server.data;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.betbox.server.Datastore;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;


public class Bet {
	public String content;
	public String creationTime;
	public StandPool pool;
	public String status;
	
	public static final String NAME_BET = "Bet";	
	public static final String PROPERTY_CONTENT = "Content";
	public static final String PROPERTY_TIME = "TimeOfCreation";
	public static final String PROPERTY_POOL = "StandPool";
	public static final String PROPERTY_STATUS = "status";
	
	public static final String STATUS_OPEN = "open";
	public static final String STATUS_CLOSE = "close";	
	
	public static Bet makeBet(String content) {
		Entity entity = getSingleBet(content);
		Bet bet = new Bet();
 		Date date = new Date();
		if (entity == null) {
			bet.content = content;
			bet.creationTime = getTime(date);
			bet.pool = new StandPool();
			bet.status = Bet.STATUS_OPEN;
			saveBet(bet);
		} else {
			bet.content = entity.getProperty(PROPERTY_CONTENT).toString();
			bet.creationTime = entity.getProperty(PROPERTY_TIME).toString();
			bet.pool = new StandPool(entity.getProperty(PROPERTY_POOL).toString());
			bet.status = entity.getProperty(PROPERTY_STATUS).toString();
		}
		return bet;
	}
	
	private static String getTime(Date date) {
		DateFormat df = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss");
		String formattedDate = df.format(System.currentTimeMillis()); 
		return formattedDate;
	}
	
	public static boolean updateBet(String content, String standContent) {
		Bet bet = Bet.makeBet(content);
		if (bet.status == Bet.STATUS_CLOSE || !bet.pool.takeStand(standContent)) {
			return false;
		}
		saveBet(bet);
		/* Broadcase the bet pool update message */
		Datastore.broadcast(bet);
		return true;
	}
	
	public static void saveBet(Bet bet){
		Entity entity = getSingleBet(bet.content);
		if (entity == null) {
        	entity = new Entity(NAME_BET, bet.content);
        	entity.setProperty(PROPERTY_CONTENT, bet.content);
        	entity.setProperty(PROPERTY_TIME, bet.creationTime);
        	entity.setProperty(PROPERTY_POOL, bet.pool.toString());   
        	entity.setProperty(PROPERTY_STATUS, bet.status); 
		} else {
        	entity.setProperty(PROPERTY_CONTENT, bet.content);
        	entity.setProperty(PROPERTY_TIME, bet.creationTime);
        	entity.setProperty(PROPERTY_POOL, bet.pool.toString());
        	entity.setProperty(PROPERTY_STATUS, bet.status);  			
		}
		Util.persistEntity(entity);
	}
	
	public static Iterable<Entity> getAllBets() {
		return Util.listEntities(NAME_BET, null, null);
	}
	
	public static Iterable<Entity> getAllBetsSinceLastUpdate(String time) {
		return Util.listEntities(NAME_BET, PROPERTY_TIME, time);
	}
	
	public static Entity getSingleBet(String content) {
		Key key = KeyFactory.createKey(NAME_BET, content);
		return Util.findEntity(key);
	}
}
