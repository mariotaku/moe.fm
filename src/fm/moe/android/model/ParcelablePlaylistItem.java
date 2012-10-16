package fm.moe.android.model;

import android.os.Parcel;
import android.os.Parcelable;
import java.net.MalformedURLException;
import java.net.URL;
import moefou4j.Cover;
import moefou4j.PlaylistItem;
import org.json.JSONObject;
import org.json.JSONException;

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

	private final long upId;

	private final URL url;

	public ParcelablePlaylistItem(final PlaylistItem item) {
		upId = item.getUpId();
		url = item.getUrl();
		title = item.getTitle();
		artist = item.getArtist();
		final Cover cover = item != null ? item.getCover() : null;
		coverUrl = cover != null ? cover.getSquare() : null;
	}

	private ParcelablePlaylistItem(final Parcel in) {
		upId = in.readLong();
		url = parseUrl(in.readString());
		title = in.readString();
		artist = in.readString();
		coverUrl = parseUrl(in.readString());
	}

	private static URL parseUrl(String url) {
		if (url == null) return null;
		try {
			return new URL(url);
		} catch (MalformedURLException e) {
			return null;
		}
	}
	
	private static String toString(Object object) {
		return object != null ? object.toString() : null;
	}
	
	@Override
	public int describeContents() {
		return 0;
	}


	public String getArtist() {
		return artist;
	}

	public URL getCoverUrl() {
		return coverUrl;
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
	public void writeToParcel(final Parcel out, final int flags) {
		out.writeLong(upId);
		out.writeString(toString(url));
		out.writeString(title);
		out.writeString(artist);
		out.writeString(toString(coverUrl));
	}

	public JSONObject toJSONObject() throws JSONException {
		final JSONObject json = new JSONObject();		
			json.put("upId", upId);
			json.put("url", toString(url));
			json.put("title", title);
			json.put("artist", artist);
			json.put("coverUrl", coverUrl);
		
		return json;
	}
	
	public ParcelablePlaylistItem(JSONObject json) throws JSONException {
		upId = Long.valueOf(toString(json.get("upId")));
		url = parseUrl(toString(json.get("url")));
		title = toString(json.get("title"));
		artist = toString(json.get("artist"));
		coverUrl = parseUrl(toString(json.get("coverUrl")));
	}
}
