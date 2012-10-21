package moefou4j.api;

import moefou4j.MoefouException;
import moefou4j.Paging;
import moefou4j.Playlist;
import moefou4j.ResponseMessage;

public interface MoeFMMethods {

	public Playlist getNextPlaylist(Playlist.PlaylistInformation info) throws MoefouException;

	/**
	 * 
	 * @return 系统默认的乱序音乐播放列表。
	 * @throws MoefouException
	 */
	public Playlist getPlaylist() throws MoefouException;

	/**
	 * @param paging 分页
	 * @return 系统默认的乱序音乐播放列表。
	 * @throws MoefouException
	 */
	public Playlist getPlaylist(Paging paging) throws MoefouException;
	
	/**
	 * @param paging 分页。
	 * @param radioId 个人电台的ID。
	 * @return 指定 id 的个人电台播放列表。
	 * @throws MoefouException
	 */
	public Playlist getRadioPlaylist(Paging paging, long... radioId) throws MoefouException;

	public ResponseMessage logListened(long objId) throws MoefouException;

}
