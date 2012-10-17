package fm.moe.android.model;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import moefou4j.Cover;
import moefou4j.PlaylistItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import fm.moe.android.util.JSONFileHelper;
import fm.moe.android.util.NoDuplicatesArrayList;

public final class ParcelablePlaylistItem implements Parcelable {

	public static final Parcelable.Creator<ParcelablePlaylistItem> CREATOR = new Parcelable.Creator<ParcelablePlaylistItem>() {
		@Override
		public ParcelablePlaylistItem createFromParcel(final Parcel in) {
			return new ParcelablePlaylistItem(in);
		}

		@Override
		public ParcelablePlaylistItem[] newArray(final int size) {
			return new ParcelablePlaylistItem[size];
		}
	};

	private final String artist;

	private final URL coverUrl;

	private final String title;

	private final long upId, subId;

	private final URL url;

	public ParcelablePlaylistItem(final JSONObject json) throws JSONException {
		upId = Long.valueOf(toString(json.get("upId")));
		url = parseUrl(toString(json.get("url")));
		title = toString(json.get("title"));
		artist = toString(json.get("artist"));
		coverUrl = parseUrl(toString(json.get("coverUrl")));
		subId = Long.valueOf(toString(json.get("subId")));
	}

	public ParcelablePlaylistItem(final PlaylistItem item) {
		upId = item.getUpId();
		url = item.getUrl();
		title = item.getTitle();
		artist = item.getArtist();
		final Cover cover = item != null ? item.getCover() : null;
		coverUrl = cover != null ? cover.getSquare() : null;
		subId = item.getSubId();
	}

	private ParcelablePlaylistItem(final Parcel in) {
		upId = in.readLong();
		url = parseUrl(in.readString());
		title = in.readString();
		artist = in.readString();
		coverUrl = parseUrl(in.readString());
		subId = in.readLong();
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (!(obj instanceof ParcelablePlaylistItem)) return false;
		final ParcelablePlaylistItem other = (ParcelablePlaylistItem) obj;
		if (upId != other.upId) return false;
		return true;
	}

	public String getArtist() {
		return artist;
	}

	public URL getCoverUrl() {
		return coverUrl;
	}
	
	public long getSubId() {
		return subId;
	}

	public String getTitle() {
		return title;
	}

	public long getUpId() {
		return upId;
	}

	/**
	 * @return 音乐的URL
	 */
	public URL getUrl() {
		return url;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (upId ^ upId >>> 32);
		return result;
	}

	public JSONObject toJSONObject() throws JSONException {
		final JSONObject json = new JSONObject();
		json.put("upId", upId);
		json.put("url", toString(url));
		json.put("title", title);
		json.put("artist", artist);
		json.put("coverUrl", coverUrl);
		json.put("subId", subId);
		return json;
	}

	@Override
	public String toString() {
		return "ParcelablePlaylistItem{artist=" + artist + ", coverUrl=" + coverUrl + ", title=" + title + ", upId="
				+ upId + ", url=" + url + "}";
	}

	@Override
	public void writeToParcel(final Parcel out, final int flags) {
		out.writeLong(upId);
		out.writeString(toString(url));
		out.writeString(title);
		out.writeString(artist);
		out.writeString(toString(coverUrl));
		out.writeLong(subId);
	}

	public static List<ParcelablePlaylistItem> createListFromFile(final Context context, final String filename)
			throws JSONException, IOException {
		final List<ParcelablePlaylistItem> list = new NoDuplicatesArrayList<ParcelablePlaylistItem>();
		final JSONObject json = JSONFileHelper.read(JSONFileHelper.getFilePath(context, filename));
		if (json != null) {
			final JSONArray array = json.getJSONArray("playlist");
			final int length = array.length();
			for (int i = 0; i < length; i++) {
				list.add(new ParcelablePlaylistItem(array.getJSONObject(i)));
			}
		}
		return list;
	}

	public static void saveListToFile(final Context context, final String filename,
			final List<ParcelablePlaylistItem> list) throws JSONException, IOException {
		final JSONObject json = new JSONObject();
		final JSONArray array = new JSONArray();
		for (final ParcelablePlaylistItem item : list) {
			array.put(item.toJSONObject());
		}
		json.put("playlist", array);
		JSONFileHelper.write(json, JSONFileHelper.getFilePath(context, filename));
	}

	private static URL parseUrl(final String url) {
		if (url == null) return null;
		try {
			return new URL(url);
		} catch (final MalformedURLException e) {
			return null;
		}
	}

	private static String toString(final Object object) {
		return object != null ? object.toString() : null;
	}
}
