package com.betbox.server.data;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Key;

public class Member {
	public static final String NAME_MEMBER = "Member";
	public static final String PROPERTY_MEMBERID = "MemberID";
	public String memberID;

	public static Member makeMember(String memberID) {
		Entity entity = getSingleMember(memberID);
		Member member = new Member();
		if (entity == null) {
			member.memberID = memberID;
			saveMember(member);
		} else {
			member.memberID = entity.getProperty(PROPERTY_MEMBERID).toString();
		}
		return member;
	}
	  public static void saveMember(Member member) {
	    Entity entity = getSingleMember(member.memberID);
	    if (entity == null) {
	      entity = new Entity(NAME_MEMBER, member.memberID);
	      entity.setProperty(PROPERTY_MEMBERID, member.memberID);
	    } else {
	      if (member.memberID != null && !"".equals(member.memberID)) {
	        entity.setProperty(PROPERTY_MEMBERID, member.memberID);
	      }
	    }
	    Util.persistEntity(entity);
	  }

	  public static Entity getSingleMember(String memberID) {
	    Key key = KeyFactory.createKey("member", memberID);
	    return Util.findEntity(key);
	  }

}
