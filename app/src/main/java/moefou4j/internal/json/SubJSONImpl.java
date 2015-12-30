package moefou4j.internal.json;

import static moefou4j.internal.util.Moefou4JInternalParseUtil.getDouble;
import static moefou4j.internal.util.Moefou4JInternalParseUtil.getInt;
import static moefou4j.internal.util.Moefou4JInternalParseUtil.getLong;
import static moefou4j.internal.util.Moefou4JInternalParseUtil.getRawString;
import static moefou4j.internal.util.Moefou4JInternalParseUtil.getURLFromString;

import java.net.URL;
import java.util.Arrays;
import java.util.Date;

import moefou4j.Favorite;
import moefou4j.Meta;
import moefou4j.MoefouException;
import moefou4j.Sub;
import moefou4j.Wiki;
import moefou4j.http.HttpResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

final class SubJSONImpl extends MoefouResponseImpl implements Sub {

	private static final long serialVersionUID = -5884009905326233853L;

	private long id;
	private String title;
	private String titleEncode;
	private Type type;
	private Meta[] metas;
	private long parent;
	private Date date;
	private Date modified;
	private URL fmUrl;
	private URL url;
	private Favorite userFavorite;
	private long parentWiki;
	private int order;
	private Wiki wiki;
	private String viewTitle;
	private String about;
	private Upload[] uploads;

	SubJSONImpl(final HttpResponse res) throws MoefouException {
		super(res);
		try {
			init(res.asJSONObject().getJSONObject("response").getJSONObject("sub"));
		} catch (final JSONException e) {
			throw new MoefouException(e);
		}
	}

	SubJSONImpl(final JSONObject json) throws MoefouException {
		init(json);
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (!(obj instanceof SubJSONImpl)) return false;
		final SubJSONImpl other = (SubJSONImpl) obj;
		if (about == null) {
			if (other.about != null) return false;
		} else if (!about.equals(other.about)) return false;
		if (date == null) {
			if (other.date != null) return false;
		} else if (!date.equals(other.date)) return false;
		if (fmUrl == null) {
			if (other.fmUrl != null) return false;
		} else if (!fmUrl.equals(other.fmUrl)) return false;
		if (id != other.id) return false;
		if (!Arrays.equals(metas, other.metas)) return false;
		if (modified == null) {
			if (other.modified != null) return false;
		} else if (!modified.equals(other.modified)) return false;
		if (order != other.order) return false;
		if (parent != other.parent) return false;
		if (parentWiki != other.parentWiki) return false;
		if (title == null) {
			if (other.title != null) return false;
		} else if (!title.equals(other.title)) return false;
		if (titleEncode == null) {
			if (other.titleEncode != null) return false;
		} else if (!titleEncode.equals(other.titleEncode)) return false;
		if (type != other.type) return false;
		if (!Arrays.equals(uploads, other.uploads)) return false;
		if (url == null) {
			if (other.url != null) return false;
		} else if (!url.equals(other.url)) return false;
		if (userFavorite == null) {
			if (other.userFavorite != null) return false;
		} else if (!userFavorite.equals(other.userFavorite)) return false;
		if (viewTitle == null) {
			if (other.viewTitle != null) return false;
		} else if (!viewTitle.equals(other.viewTitle)) return false;
		if (wiki == null) {
			if (other.wiki != null) return false;
		} else if (!wiki.equals(other.wiki)) return false;
		return true;
	}

	@Override
	public String getAbout() {
		return about;
	}

	@Override
	public Date getDate() {
		return date;
	}

	@Override
	public URL getFMUrl() {
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
	public int getOrder() {
		return order;
	}

	@Override
	public long getParent() {
		return parent;
	}

	@Override
	public long getParentWiki() {
		return parentWiki;
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
	public Type getType() {
		return type;
	}

	@Override
	public Upload[] getUploads() {
		return uploads;
	}

	@Override
	public URL getUrl() {
		return url;
	}

	@Override
	public Favorite getUserFavorite() {
		return userFavorite;
	}

	@Override
	public String getViewTitle() {
		return viewTitle;
	}

	@Override
	public Wiki getWiki() {
		return wiki;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (about == null ? 0 : about.hashCode());
		result = prime * result + (date == null ? 0 : date.hashCode());
		result = prime * result + (fmUrl == null ? 0 : fmUrl.hashCode());
		result = prime * result + (int) (id ^ id >>> 32);
		result = prime * result + Arrays.hashCode(metas);
		result = prime * result + (modified == null ? 0 : modified.hashCode());
		result = prime * result + order;
		result = prime * result + (int) (parent ^ parent >>> 32);
		result = prime * result + (int) (parentWiki ^ parentWiki >>> 32);
		result = prime * result + (title == null ? 0 : title.hashCode());
		result = prime * result + (titleEncode == null ? 0 : titleEncode.hashCode());
		result = prime * result + (type == null ? 0 : type.hashCode());
		result = prime * result + Arrays.hashCode(uploads);
		result = prime * result + (url == null ? 0 : url.hashCode());
		result = prime * result + (userFavorite == null ? 0 : userFavorite.hashCode());
		result = prime * result + (viewTitle == null ? 0 : viewTitle.hashCode());
		result = prime * result + (wiki == null ? 0 : wiki.hashCode());
		return result;
	}

	@Override
	public String toString() {
		return "SubJSONImpl{id=" + id + ", title=" + title + ", titleEncode=" + titleEncode + ", type=" + type
				+ ", metas=" + Arrays.toString(metas) + ", parent=" + parent + ", date=" + date + ", modified="
				+ modified + ", fmUrl=" + fmUrl + ", url=" + url + ", userFavorite=" + userFavorite + ", parentWiki="
				+ parentWiki + ", order=" + order + ", wiki=" + wiki + ", viewTitle=" + viewTitle + ", about=" + about
				+ ", uploads=" + Arrays.toString(uploads) + "}";
	}

	void init(final JSONObject json) throws MoefouException {
		id = getLong("sub_id", json);
		title = getRawString("sub_title", json);
		titleEncode = getRawString("sub_title_encode", json);
		parent = getLong("sub_parent", json);
		date = new Date(getLong("sub_date", json) * 1000);
		modified = new Date(getLong("sub_modified", json) * 1000);
		type = Type.fromString(getRawString("sub_type", json));
		url = getURLFromString("sub_url", json);
		fmUrl = getURLFromString("sub_fm_url", json);
		viewTitle = getRawString("sub_view_title", json);
		parentWiki = getLong("sub_parent_wiki", json);
		order = getInt("sub_order", json);
		about = getRawString("sub_about", json);
		if (!json.isNull("sub_user_fav")) {
			try {
				userFavorite = new FavoriteJSONImpl(json.getJSONObject("sub_user_fav"));
			} catch (final JSONException jsone) {
				throw new MoefouException(jsone);
			}
		}
		if (!json.isNull("sub_meta")) {
			try {
				metas = MetaJSONImpl.getMetas(json.getJSONArray("sub_meta"));
			} catch (final JSONException jsone) {
				throw new MoefouException(jsone);
			}
		}
		if (!json.isNull("wiki")) {
			try {
				wiki = new WikiJSONImpl(json.getJSONObject("wiki"));
			} catch (final JSONException jsone) {
				throw new MoefouException(jsone);
			}
		}
		if (!json.isNull("sub_upload")) {
			try {
				uploads = UploadJSONImpl.getUploads(json.getJSONArray("sub_upload"));
			} catch (final JSONException jsone) {
				throw new MoefouException(jsone);
			}
		}
	}

	final static class UploadJSONImpl implements Upload {

		private Date date;
		private String type;
		private long uid;
		private String uri;
		private int bitrate;
		private int fileSize;
		private long id;
		private double length;
		private long objectId;
		private Type objectType;
		private int size;
		private String time;

		UploadJSONImpl(final JSONObject json) throws JSONException {
			init(json);
		}

		@Override
		public boolean equals(final Object obj) {
			if (this == obj) return true;
			if (obj == null) return false;
			if (!(obj instanceof UploadJSONImpl)) return false;
			final UploadJSONImpl other = (UploadJSONImpl) obj;
			if (bitrate != other.bitrate) return false;
			if (date == null) {
				if (other.date != null) return false;
			} else if (!date.equals(other.date)) return false;
			if (fileSize != other.fileSize) return false;
			if (id != other.id) return false;
			if (Double.doubleToLongBits(length) != Double.doubleToLongBits(other.length)) return false;
			if (objectId != other.objectId) return false;
			if (objectType != other.objectType) return false;
			if (size != other.size) return false;
			if (time == null) {
				if (other.time != null) return false;
			} else if (!time.equals(other.time)) return false;
			if (type == null) {
				if (other.type != null) return false;
			} else if (!type.equals(other.type)) return false;
			if (uid != other.uid) return false;
			if (uri == null) {
				if (other.uri != null) return false;
			} else if (!uri.equals(other.uri)) return false;
			return true;
		}

		@Override
		public int getBitrate() {
			return bitrate;
		}

		@Override
		public Date getDate() {
			return date;
		}

		@Override
		public int getFileSize() {
			return fileSize;
		}

		@Override
		public long getId() {
			return id;
		}

		@Override
		public double getLength() {
			return length;
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
		public int getSize() {
			return size;
		}

		@Override
		public String getTime() {
			return time;
		}

		@Override
		public String getType() {
			return type;
		}

		@Override
		public long getUid() {
			return uid;
		}

		@Override
		public String getUri() {
			return uri;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + bitrate;
			result = prime * result + (date == null ? 0 : date.hashCode());
			result = prime * result + fileSize;
			result = prime * result + (int) (id ^ id >>> 32);
			long temp;
			temp = Double.doubleToLongBits(length);
			result = prime * result + (int) (temp ^ temp >>> 32);
			result = prime * result + (int) (objectId ^ objectId >>> 32);
			result = prime * result + (objectType == null ? 0 : objectType.hashCode());
			result = prime * result + size;
			result = prime * result + (time == null ? 0 : time.hashCode());
			result = prime * result + (type == null ? 0 : type.hashCode());
			result = prime * result + (int) (uid ^ uid >>> 32);
			result = prime * result + (uri == null ? 0 : uri.hashCode());
			return result;
		}

		@Override
		public String toString() {
			return "UploadJSONImpl{date=" + date + ", type=" + type + ", uid=" + uid + ", uri=" + uri + ", bitrate="
					+ bitrate + ", fileSize=" + fileSize + ", id=" + id + ", length=" + length + ", objectId="
					+ objectId + ", objectType=" + objectType + ", size=" + size + ", time=" + time + "}";
		}

		void init(final JSONObject json) throws JSONException {
			date = new Date(getLong("up_date", json) * 1000);
			type = getRawString("up_type", json);
			uid = getLong("up_uid", json);
			uri = getRawString("up_uri", json);
			id = getLong("up_id", json);
			objectId = getLong("up_obj_id", json);
			objectType = Type.fromString(getRawString("up_obj_type", json));
			size = getInt("up_size", json);
			if (!json.isNull("up_data")) {
				final JSONObject up_data_json = json.getJSONObject("up_data");
				bitrate = getInt("bitrate", up_data_json);
				fileSize = getInt("filesize", up_data_json);
				length = getDouble("length", up_data_json);
				time = getRawString("time", up_data_json);
			}
		}

		static Upload[] getUploads(final JSONArray array) throws JSONException {
			final int length = array.length();
			final Upload[] metas = new Upload[length];
			for (int i = 0; i < length; i++) {
				metas[i] = new UploadJSONImpl(array.getJSONObject(i));
			}
			return metas;
		}
	}
}
