package fm.moe.android.model;

import java.net.URL;

import moefou4j.Cover;
import moefou4j.Favorite;
import moefou4j.PlaylistItem;
import moefou4j.Sub;
import moefou4j.Wiki;
import android.os.Parcel;
import android.os.Parcelable;

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

	final PlaylistItem item;

	public ParcelablePlaylistItem(final PlaylistItem item) {
		this.item = item;
	}

	private ParcelablePlaylistItem(final Parcel in) {
		item = (PlaylistItem) in.readSerializable();
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
		if (item == null) {
			if (other.item != null) return false;
		} else if (!item.equals(other.item)) return false;
		return true;
	}

	public String getArtist() {
		return item.getArtist();
	}

	public Cover getCover() {
		return item.getCover();
	}

	public Favorite getFavoriteSub() {
		return item.getFavoriteSub();

	}

	public Favorite getFavoriteWiki() {
		return item.getFavoriteWiki();

	}

	/**
	 * @return 媒体文件的大小，单位是KB
	 */
	public int getFileSize() {
		return item.getFileSize();
	}

	public String getFileType() {
		return item.getFileType();
	}

	public PlaylistItem getPlaylistItem() {
		return item;
	}

	/**
	 * @return 媒体的时间，以秒计
	 */
	public int getStreamLength() {
		return item.getStreamLength();
	}

	/**
	 * @return 人类可读的媒体时间
	 */
	public String getStreamTime() {
		return item.getStreamTime();
	}

	public long getSubId() {
		return item.getSubId();
	}

	public String getSubTitle() {
		return item.getSubTitle();
	}

	public Sub.Type getSubType() {
		return item.getSubType();
	}

	public URL getSubUrl() {
		return item.getSubUrl();
	}

	public String getTitle() {
		return item.getTitle();
	}

	public long getUpId() {
		return item.getUpId();
	}

	/**
	 * @return 音乐的URL
	 */
	public URL getUrl() {
		return item.getUrl();
	}

	public long getWikiId() {
		return item.getWikiId();
	}

	public String getWikiTitle() {
		return item.getWikiTitle();
	}

	public Wiki.Type getWikiType() {
		return item.getWikiType();
	}

	public URL getWikiUrl() {
		return item.getWikiUrl();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (item == null ? 0 : item.hashCode());
		return result;
	}

	@Override
	public String toString() {
		return item.toString();
	}

	@Override
	public void writeToParcel(final Parcel out, final int flags) {
		out.writeSerializable(item);
	}

}
