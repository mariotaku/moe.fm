package moefou4j.internal.json;

import static moefou4j.internal.util.Moefou4JInternalParseUtil.getInt;
import static moefou4j.internal.util.Moefou4JInternalParseUtil.getLong;
import static moefou4j.internal.util.Moefou4JInternalParseUtil.getRawString;
import static moefou4j.internal.util.Moefou4JInternalParseUtil.getURLFromString;

import java.net.URL;
import java.util.Arrays;
import java.util.Date;

import moefou4j.Cover;
import moefou4j.Favorite;
import moefou4j.MoefouException;
import moefou4j.ResponseList;
import moefou4j.Wiki;
import moefou4j.http.HttpResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

final class WikiJSONImpl extends MoefouResponseImpl implements Wiki {

	private static final long serialVersionUID = 7565441300902354863L;
	private long id;
	private String title;
	private String titleEncode;
	private String name;
	private Type[] types;
	private Meta[] metas;
	private long parent;
	private Date date;
	private Date modified;
	private long modifiedUser;
	private URL fmUrl;
	private URL url;
	private Cover cover;
	private Favorite userFavorite;

	public WikiJSONImpl(final HttpResponse res) throws MoefouException {
		super(res);
		try {
			init(res.asJSONObject().getJSONObject("response").getJSONObject("wiki"));
		} catch (JSONException e) {
			throw new MoefouException(e);
		}
	}
	
	public WikiJSONImpl(final JSONObject json) throws MoefouException {
		init(json);
	}

	WikiJSONImpl() {

	}
	
	private void init(final JSONObject json) throws MoefouException {
		id = getLong("wiki_id", json);
		title = getRawString("wiki_title", json);
		titleEncode = getRawString("wiki_title_encode", json);
		name = getRawString("wiki_name", json);
		parent = getLong("wiki_parent", json);
		date = new Date(getLong("wiki_date", json) * 1000);
		modified = new Date(getLong("wiki_modified", json) * 1000);
		modifiedUser = getLong("wiki_modified_user", json);
		types = Type.fromCommaSepratedString(getRawString("wiki_type", json));
		url = getURLFromString("wiki_url", json);
		fmUrl = getURLFromString("wiki_fm_url", json);
		if (!json.isNull("cover")) {
			try {
				cover = new CoverJSONImpl(json.getJSONObject("cover"));
			} catch (final JSONException jsone) {
				throw new MoefouException(jsone);
			}
		}
		if (!json.isNull("wiki_user_fav")) {
			try {
				userFavorite = new FavoriteJSONImpl(json.getJSONObject("wiki_user_fav"));
			} catch (final JSONException jsone) {
				throw new MoefouException(jsone);
			}
		}
		if (!json.isNull("wiki_meta")) {
			try {
				metas = MetaJSONImpl.getMetas(json.getJSONArray("wiki_meta"));
			} catch (JSONException jsone) {
				throw new MoefouException(jsone);
			}
		}
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (!(obj instanceof WikiJSONImpl)) return false;
		final WikiJSONImpl other = (WikiJSONImpl) obj;
		if (cover == null) {
			if (other.cover != null) return false;
		} else if (!cover.equals(other.cover)) return false;
		if (date == null) {
			if (other.date != null) return false;
		} else if (!date.equals(other.date)) return false;
		if (fmUrl == null) {
			if (other.fmUrl != null) return false;
		} else if (!fmUrl.equals(other.fmUrl)) return false;
		if (id != other.id) return false;
		if (modified == null) {
			if (other.modified != null) return false;
		} else if (!modified.equals(other.modified)) return false;
		if (modifiedUser != other.modifiedUser) return false;
		if (name == null) {
			if (other.name != null) return false;
		} else if (!name.equals(other.name)) return false;
		if (parent != other.parent) return false;
		if (title == null) {
			if (other.title != null) return false;
		} else if (!title.equals(other.title)) return false;
		if (titleEncode == null) {
			if (other.titleEncode != null) return false;
		} else if (!titleEncode.equals(other.titleEncode)) return false;
		if (!Arrays.equals(types, other.types)) return false;
		if (url == null) {
			if (other.url != null) return false;
		} else if (!url.equals(other.url)) return false;
		if (userFavorite == null) {
			if (other.userFavorite != null) return false;
		} else if (!userFavorite.equals(other.userFavorite)) return false;
		return true;
	}

	@Override
	public Cover getCover() {
		return cover;
	}

	@Override
	public Date getDate() {
		return date;
	}

	@Override
	public URL getFMURL() {
		return fmUrl;
	}

	@Override
	public long getId() {
		return id;
	}

	@Override
	public Meta[] getMetas() {
		return metas;
	}

	@Override
	public Date getModified() {
		return modified;
	}

	@Override
	public long getModifiedUser() {
		return modifiedUser;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public long getParent() {
		return parent;
	}

	@Override
	public String getTitle() {
		return title;
	}

	@Override
	public String getTitleEncode() {
		return titleEncode;
	}

	@Override
	public Type[] getTypes() {
		return types;
	}

	@Override
	public URL getURL() {
		return url;
	}

	@Override
	public Favorite getUserFavorite() {
		return userFavorite;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (cover == null ? 0 : cover.hashCode());
		result = prime * result + (date == null ? 0 : date.hashCode());
		result = prime * result + (fmUrl == null ? 0 : fmUrl.hashCode());
		result = prime * result + (int) (id ^ id >>> 32);
		result = prime * result + (modified == null ? 0 : modified.hashCode());
		result = prime * result + (int) (modifiedUser ^ modifiedUser >>> 32);
		result = prime * result + (name == null ? 0 : name.hashCode());
		result = prime * result + (int) (parent ^ parent >>> 32);
		result = prime * result + (title == null ? 0 : title.hashCode());
		result = prime * result + (titleEncode == null ? 0 : titleEncode.hashCode());
		result = prime * result + Arrays.hashCode(types);
		result = prime * result + (url == null ? 0 : url.hashCode());
		result = prime * result + (userFavorite == null ? 0 : userFavorite.hashCode());
		return result;
	}

	@Override
	public String toString() {
		return "WikiJSONImpl{id=" + id + ", title=" + title + ", titleEncode=" + titleEncode + ", name=" + name
				+ ", types=" + Arrays.toString(types) + ", parent=" + parent + ", date=" + date + ", modified="
				+ modified + ", modifiedUser=" + modifiedUser + ", fmUrl=" + fmUrl + ", url=" + url + ", cover="
				+ cover + ", userFavorite=" + userFavorite + ", metas=" + Arrays.toString(metas) + "}";
	}

	public static ResponseList<Wiki> createWikisList(final HttpResponse res)
			throws MoefouException {
		try {
			final JSONObject json = res.asJSONObject();
			final ResponseList<Wiki> list = new ResponseListImpl<Wiki>(json);
			final JSONArray wikis_json = json.getJSONObject("response").getJSONArray("wikis");
			if (wikis_json == null) throw new MoefouException("Unknown response value!");
			final int length = wikis_json.length();
			for (int i = 0; i < length; i++) {
				list.add(new WikiJSONImpl(wikis_json.getJSONObject(i)));
			}
			return list;
		} catch (final JSONException e) {
			throw new MoefouException(e);
		}
	}

	final static class MetaJSONImpl implements Meta {

		private int type;
		private String value;
		private String key;

		MetaJSONImpl(final JSONObject json) {
			key = getRawString("meta_key", json);
			value = getRawString("meta_value", json);
			type = getInt("meta_type", json);
		}

		@Override
		public int getType() {
			return type;
		}

		@Override
		public String getValue() {
			return value;
		}

		@Override
		public String getKey() {
			return key;
		}

		@Override
		public String toString() {
			return "MetaJSONImpl{key=" + key + ", value=" + value + ", type=" + type + "}";
		}

		static Meta[] getMetas(final JSONArray array) throws JSONException {
			final int length = array.length();
			final Meta[] metas = new Meta[length];
			for (int i = 0; i < length; i++) {
				metas[i] = new MetaJSONImpl(array.getJSONObject(i));
			}
			return metas;
		}
	}
}
