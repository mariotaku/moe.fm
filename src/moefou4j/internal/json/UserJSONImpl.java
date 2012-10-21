package moefou4j.internal.json;

import static moefou4j.internal.util.Moefou4JInternalParseUtil.getInt;
import static moefou4j.internal.util.Moefou4JInternalParseUtil.getLong;
import static moefou4j.internal.util.Moefou4JInternalParseUtil.getRawString;
import static moefou4j.internal.util.Moefou4JInternalParseUtil.getURLFromString;
import static moefou4j.internal.util.Moefou4JInternalStringUtil.getLongArrayfromString;

import java.net.URL;
import java.util.Arrays;
import java.util.Date;

import moefou4j.MoefouException;
import moefou4j.User;
import moefou4j.http.HttpResponse;

import org.json.JSONException;
import org.json.JSONObject;

class UserJSONImpl extends MoefouResponseImpl implements User {

	private static final long serialVersionUID = -7945728399815509654L;

	long uid;
	String name;
	String nickname;
	Date registered;
	Date lastActivity;
	URL url;
	URL fmUrl;
	Avatar avatar;
	long[] groups;
	long[] follower;
	long[] following;
	int msg;
	String about;

	UserJSONImpl() {

	}

	UserJSONImpl(final HttpResponse res) throws MoefouException {
		super(res);
		try {
			init(res.asJSONObject().getJSONObject("response").getJSONObject("user"));
		} catch (final JSONException e) {
			throw new MoefouException(e);
		}
	}

	UserJSONImpl(final JSONObject json) throws MoefouException {
		init(json);
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (!(obj instanceof UserJSONImpl)) return false;
		final UserJSONImpl other = (UserJSONImpl) obj;
		if (about == null) {
			if (other.about != null) return false;
		} else if (!about.equals(other.about)) return false;
		if (avatar == null) {
			if (other.avatar != null) return false;
		} else if (!avatar.equals(other.avatar)) return false;
		if (fmUrl == null) {
			if (other.fmUrl != null) return false;
		} else if (!fmUrl.equals(other.fmUrl)) return false;
		if (!Arrays.equals(follower, other.follower)) return false;
		if (!Arrays.equals(following, other.following)) return false;
		if (!Arrays.equals(groups, other.groups)) return false;
		if (lastActivity == null) {
			if (other.lastActivity != null) return false;
		} else if (!lastActivity.equals(other.lastActivity)) return false;
		if (msg != other.msg) return false;
		if (name == null) {
			if (other.name != null) return false;
		} else if (!name.equals(other.name)) return false;
		if (nickname == null) {
			if (other.nickname != null) return false;
		} else if (!nickname.equals(other.nickname)) return false;
		if (registered == null) {
			if (other.registered != null) return false;
		} else if (!registered.equals(other.registered)) return false;
		if (uid != other.uid) return false;
		if (url == null) {
			if (other.url != null) return false;
		} else if (!url.equals(other.url)) return false;
		return true;
	}

	@Override
	public String getAbout() {
		return about;
	}

	@Override
	public Avatar getAvatar() {
		return avatar;
	}

	@Override
	public URL getFMUrl() {
		return fmUrl;
	}

	@Override
	public long[] getFollower() {
		return follower;
	}

	@Override
	public long[] getFollowing() {
		return following;
	}

	@Override
	public long[] getGroups() {
		return groups;
	}

	@Override
	public Date getLastActivity() {
		return lastActivity;
	}

	@Override
	public int getMsg() {
		return msg;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getNickname() {
		return nickname;
	}

	@Override
	public Date getRegistered() {
		return registered;
	}

	@Override
	public long getUid() {
		return uid;
	}

	@Override
	public URL getUrl() {
		return url;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (about == null ? 0 : about.hashCode());
		result = prime * result + (avatar == null ? 0 : avatar.hashCode());
		result = prime * result + (fmUrl == null ? 0 : fmUrl.hashCode());
		result = prime * result + Arrays.hashCode(follower);
		result = prime * result + Arrays.hashCode(following);
		result = prime * result + Arrays.hashCode(groups);
		result = prime * result + (lastActivity == null ? 0 : lastActivity.hashCode());
		result = prime * result + msg;
		result = prime * result + (name == null ? 0 : name.hashCode());
		result = prime * result + (nickname == null ? 0 : nickname.hashCode());
		result = prime * result + (registered == null ? 0 : registered.hashCode());
		result = prime * result + (int) (uid ^ uid >>> 32);
		result = prime * result + (url == null ? 0 : url.hashCode());
		return result;
	}

	@Override
	public String toString() {
		return "UserJSONImpl{uid=" + uid + ", name=" + name + ", nickname=" + nickname + ", registered=" + registered
				+ ", lastActivity=" + lastActivity + ", url=" + url + ", fmUrl=" + fmUrl + ", avatar=" + avatar
				+ ", groups=" + Arrays.toString(groups) + ", follower=" + Arrays.toString(follower) + ", following="
				+ Arrays.toString(following) + ", msg=" + msg + ", about=" + about + "}";
	}

	void init(final JSONObject json) throws MoefouException {
		uid = getLong("user_uid", json);
		name = getRawString("user_name", json);
		nickname = getRawString("user_nickname", json);
		registered = new Date(getLong("user_registered", json) * 1000);
		lastActivity = new Date(getLong("user_lastactivity", json) * 1000);
		url = getURLFromString("wiki_url", json);
		fmUrl = getURLFromString("wiki_fm_url", json);
		if (json.isNull("user_avatar")) {
			try {
				avatar = new AvatarJSONImpl(json.getJSONObject("user_avatar"));
			} catch (final JSONException e) {
				throw new MoefouException(e);
			}
		}
		groups = getLongArrayfromString(getRawString("user_groups", json), ',');
		follower = getLongArrayfromString(getRawString("user_follower", json), ',');
		following = getLongArrayfromString(getRawString("user_following", json), ',');
		msg = getInt("msg", json);
		about = getRawString("about", json);
	}

	static class AvatarJSONImpl implements Avatar {
		private static final long serialVersionUID = -3163558735837192477L;

		URL small;
		URL medium;
		URL large;

		AvatarJSONImpl() {
		}

		AvatarJSONImpl(final JSONObject json) {
			init(json);
		}

		@Override
		public boolean equals(final Object obj) {
			if (this == obj) return true;
			if (obj == null) return false;
			if (!(obj instanceof AvatarJSONImpl)) return false;
			final AvatarJSONImpl other = (AvatarJSONImpl) obj;
			if (large == null) {
				if (other.large != null) return false;
			} else if (!large.equals(other.large)) return false;
			if (medium == null) {
				if (other.medium != null) return false;
			} else if (!medium.equals(other.medium)) return false;
			if (small == null) {
				if (other.small != null) return false;
			} else if (!small.equals(other.small)) return false;
			return true;
		}

		@Override
		public URL getLarge() {
			return large;
		}

		@Override
		public URL getMedium() {
			return medium;
		}

		@Override
		public URL getSmall() {
			return small;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + (large == null ? 0 : large.hashCode());
			result = prime * result + (medium == null ? 0 : medium.hashCode());
			result = prime * result + (small == null ? 0 : small.hashCode());
			return result;
		}

		@Override
		public String toString() {
			return "AvatarJSONImpl{small=" + small + ", medium=" + medium + ", large=" + large + "}";
		}

		void init(final JSONObject json) {
			small = getURLFromString("small", json);
			medium = getURLFromString("medium", json);
			large = getURLFromString("large", json);
		}

	}
}
