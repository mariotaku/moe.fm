package moefou4j;

import java.io.Serializable;
import java.net.URL;

public interface PlaylistItem extends Serializable {

	public String getArtist();

	public Cover getCover();

	public Favorite getFavoriteSub();

	public Favorite getFavoriteWiki();

	/**
	 * @return 媒体文件的大小，单位是KB
	 */
	public int getFileSize();

	public String getFileType();

	/**
	 * @return 媒体的时间，以秒计
	 */
	public int getStreamLength();

	/**
	 * @return 人类可读的媒体时间
	 */
	public String getStreamTime();

	public long getSubId();

	public String getSubTitle();

	public Wiki.Sub.Type getSubType();

	public URL getSubUrl();

	public String getTitle();

	public long getUpId();

	/**
	 * @return 音乐的URL
	 */
	public URL getUrl();

	public long getWikiId();

	public String getWikiTitle();

	public Wiki.Type getWikiType();

	public URL getWikiUrl();

}
