package com.betbox.server.data;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Key;

/* Stand class */

public class Stand {
	public static final String NAME_STAND = "Stand";
	public static final String PROPERTY_STANDYES = "StandYES";
	public static final String PROPERTY_STANDNO = "StandNO";
	public String betContent;
	public String memberID;
	public int numYes;
	public int numNo;

	public static Stand createOrUpdateStand(String betContent, String memberID,
			String standContent) {
		Entity entity = getSingleStand(betContent, standContent);
		Stand stand = new Stand();
		stand.betContent = betContent;
		stand.memberID = memberID;
		/* Check whether there are avaiable stands */
		if (Bet.updateBet(betContent, standContent) == false) {
			return null;
		}
		
		/* Create new stand or update old stand */
		if (entity == null) {
				stand.numYes = 0;
				stand.numNo = 0;
		} else {
			stand.numYes = Integer.getInteger(entity.getProperty(
					PROPERTY_STANDYES).toString());
			stand.numNo = Integer.getInteger(entity.getProperty(
					PROPERTY_STANDNO).toString());
		}

		/* Update the YES and NO stand */
		if (standContent.equals(StandPool.BET_YES)) {
			stand.numYes = stand.numYes + 1;
		} else if (standContent.equals(StandPool.BET_NO)) {
			stand.numNo = stand.numNo + 1;
		} else {
			return null;
		}
		
		saveStand(stand);
		
		return stand;
	}

	public static void saveStand(Stand stand) {
		Entity entity = getSingleStand(stand.betContent, stand.memberID);
		Entity bet = Bet.getSingleBet(stand.betContent);
		if (entity == null) {
			entity = new Entity(NAME_STAND, bet.getKey());
			entity.setProperty(Bet.PROPERTY_CONTENT, stand.betContent);
			entity.setProperty(Member.PROPERTY_MEMBERID, stand.memberID);
			entity.setProperty(PROPERTY_STANDYES, stand.numYes);
			entity.setProperty(PROPERTY_STANDNO, stand.numNo);
		} else {
			entity.setProperty(Bet.PROPERTY_CONTENT, stand.betContent);
			entity.setProperty(Member.PROPERTY_MEMBERID, stand.memberID);
			entity.setProperty(PROPERTY_STANDYES, stand.numYes);
			entity.setProperty(PROPERTY_STANDNO, stand.numNo);
		}
		Util.persistEntity(entity);
	}

	public static Entity getSingleStand(String betContent, String memberID) {
		Key ancestorKey = KeyFactory.createKey(Bet.NAME_BET, betContent);
		Iterable<Entity> entities = Util.listChildren(NAME_STAND, ancestorKey);
		for (Entity e : entities) {
			if (e.getProperty(Member.PROPERTY_MEMBERID).equals(memberID))
				return e;
		}
		return null;
	}

}
