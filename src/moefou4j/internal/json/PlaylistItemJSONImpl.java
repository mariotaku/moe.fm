package moefou4j.internal.json;

import static moefou4j.internal.util.Moefou4JInternalParseUtil.getInt;
import static moefou4j.internal.util.Moefou4JInternalParseUtil.getLong;
import static moefou4j.internal.util.Moefou4JInternalParseUtil.getRawString;
import static moefou4j.internal.util.Moefou4JInternalParseUtil.getURLFromString;

import java.net.URL;

import moefou4j.Cover;
import moefou4j.Favorite;
import moefou4j.MoefouException;
import moefou4j.PlaylistItem;
import moefou4j.Sub;
import moefou4j.Wiki;
import moefou4j.Wiki.Type;

import org.json.JSONException;
import org.json.JSONObject;

public class PlaylistItemJSONImpl implements PlaylistItem {

	private static final long serialVersionUID = 3866860449112856833L;
	
	private long upId;
	private long wikiId;
	private String title;
	private URL url;
	private URL wikiUrl;
	private Cover cover;
	private String artist;
	private URL subUrl;
	private String subTitle;
	private Sub.Type subType;
	private long subId;
	private String wikiTitle;
	private Type wikiType;
	private int streamLength;
	private String streamTime;
	private int fileSize;
	private String fileType;

	public PlaylistItemJSONImpl(final JSONObject json) throws MoefouException {

		upId = getLong("up_id", json);
		url = getURLFromString("url", json);
		streamLength = getInt("stream_length", json);
		streamTime = getRawString("stream_time", json);
		fileSize = getInt("file_size", json);
		fileType = getRawString("file_type", json);
		wikiId = getLong("wiki_id", json);
		wikiType = Wiki.Type.fromString(getRawString("wiki_type", json));
		try {
			cover = new CoverJSONImpl(json.getJSONObject("cover"));
		} catch (final JSONException e) {
			throw new MoefouException(e);
		}
		title = getRawString("title", json);
		wikiTitle = getRawString("wiki_title", json);
		wikiUrl = getURLFromString("wiki_url", json);
		subId = getLong("sub_id", json);
		subType = Sub.Type.fromString(getRawString("sub_type", json));
		subTitle = getRawString("sub_title", json);
		subUrl = getURLFromString("sub_url", json);
		artist = getRawString("artist", json);
	}

	PlaylistItemJSONImpl() {

	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (!(obj instanceof PlaylistItemJSONImpl)) return false;
		final PlaylistItemJSONImpl other = (PlaylistItemJSONImpl) obj;
		if (upId != other.upId) return false;
		return true;
	}

	@Override
	public String getArtist() {
		return artist;
	}

	@Override
	public Cover getCover() {
		return cover;
	}

	@Override
	public Favorite getFavoriteSub() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Favorite getFavoriteWiki() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getFileSize() {
		return fileSize;
	}

	@Override
	public String getFileType() {
		return fileType;
	}

	@Override
	public int getStreamLength() {
		return streamLength;
	}

	@Override
	public String getStreamTime() {
		return streamTime;
	}

	@Override
	public long getSubId() {
		return subId;
	}

	@Override
	public String getSubTitle() {
		return subTitle;
	}

	@Override
	public Sub.Type getSubType() {
		return subType;
	}

	@Override
	public URL getSubUrl() {
		return subUrl;
	}

	@Override
	public String getTitle() {
		return title;
	}

	@Override
	public long getUpId() {
		return upId;
	}

	@Override
	public URL getUrl() {
		return url;
	}

	@Override
	public long getWikiId() {
		return wikiId;
	}

	@Override
	public String getWikiTitle() {
		return wikiTitle;
	}

	@Override
	public Type getWikiType() {
		return wikiType;
	}

	@Override
	public URL getWikiUrl() {
		return wikiUrl;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (upId ^ upId >>> 32);
		return result;
	}

	@Override
	public String toString() {
		return "PlaylistItemJSONImpl{upId=" + upId + ", wikiId=" + wikiId + ", title=" + title + ", url=" + url
				+ ", wikiUrl=" + wikiUrl + ", cover=" + cover + ", artist=" + artist + ", subUrl=" + subUrl
				+ ", subTitle=" + subTitle + ", subType=" + subType + ", subId=" + subId + ", wikiTitle=" + wikiTitle
				+ ", wikiType=" + wikiType + ", streamLength=" + streamLength + ", streamTime=" + streamTime
				+ ", fileSize=" + fileSize + ", fileType=" + fileType + "}";
	}

}
