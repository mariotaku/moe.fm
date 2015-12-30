package moefou4j.internal.json;

import static moefou4j.internal.util.Moefou4JInternalParseUtil.getInt;
import static moefou4j.internal.util.Moefou4JInternalParseUtil.getLong;
import static moefou4j.internal.util.Moefou4JInternalParseUtil.getRawString;

import java.util.Date;

import moefou4j.Favorite;
import moefou4j.MoefouException;
import moefou4j.http.HttpResponse;

import org.json.JSONObject;

class FavoriteJSONImpl extends MoefouResponseImpl implements Favorite {

	private static final long serialVersionUID = -6674929665808811016L;
	Date date;
	long id;
	long objectId;
	long uid;
	String objectType;
	int type;

	FavoriteJSONImpl() {

	}

	FavoriteJSONImpl(final HttpResponse res) throws MoefouException {
		super(res);
	}

	FavoriteJSONImpl(final JSONObject json) {
		init(json);
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (!(obj instanceof FavoriteJSONImpl)) return false;
		final FavoriteJSONImpl other = (FavoriteJSONImpl) obj;
		if (date == null) {
			if (other.date != null) return false;
		} else if (!date.equals(other.date)) return false;
		if (id != other.id) return false;
		if (objectId != other.objectId) return false;
		if (objectType == null) {
			if (other.objectType != null) return false;
		} else if (!objectType.equals(other.objectType)) return false;
		if (type != other.type) return false;
		if (uid != other.uid) return false;
		return true;
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
	public String getObjectType() {
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (date == null ? 0 : date.hashCode());
		result = prime * result + (int) (id ^ id >>> 32);
		result = prime * result + (int) (objectId ^ objectId >>> 32);
		result = prime * result + (objectType == null ? 0 : objectType.hashCode());
		result = prime * result + type;
		result = prime * result + (int) (uid ^ uid >>> 32);
		return result;
	}

	@Override
	public String toString() {
		return "FavoriteJSONImpl{date=" + date + ", id=" + id + ", objectId=" + objectId + ", uid=" + uid
				+ ", objectType=" + objectType + ", type=" + type + "}";
	}

	void init(final JSONObject json) {

		date = new Date(getLong("fav_date", json) * 1000);
		id = getLong("fav_id", json);
		objectId = getLong("fav_obj_id", json);
		uid = getLong("fav_uid", json);
		objectType = getRawString("fav_obj_type", json);
		type = getInt("fav_type", json);
	}
}
