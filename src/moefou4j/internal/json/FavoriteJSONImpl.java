package moefou4j.internal.json;

import static moefou4j.internal.util.Moefou4JInternalParseUtil.getInt;
import static moefou4j.internal.util.Moefou4JInternalParseUtil.getLong;
import static moefou4j.internal.util.Moefou4JInternalParseUtil.getRawString;

import java.util.Date;

import moefou4j.Favorite;
import moefou4j.Wiki.Type;

import org.json.JSONObject;

class FavoriteJSONImpl implements Favorite {

	private static final long serialVersionUID = -6674929665808811016L;
	private Date date;
	private long id;
	private long objectId;
	private long uid;
	private Type objectType;
	private int type;

	FavoriteJSONImpl() {

	}

	FavoriteJSONImpl(final JSONObject json) {
		date = new Date(getLong("fav_date", json) * 1000);
		id = getLong("fav_id", json);
		objectId = getLong("fav_obj_id", json);
		uid = getLong("fav_uid", json);
		objectType = Type.fromString(getRawString("fav_obj_type", json));
		type = getInt("fav_type", json);
	}

	@Override
	public Date getDate() {
		return date;
	}

	@Override
	public long getId() {
		return id;
	}

	@Override
	public long getObjectId() {
		return objectId;
	}

	@Override
	public Type getObjectType() {
		return objectType;
	}

	@Override
	public int getType() {
		return type;
	}

	@Override
	public long getUid() {
		return uid;
	}
}
