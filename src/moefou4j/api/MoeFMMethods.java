package moefou4j.api;

import moefou4j.MoefouException;
import moefou4j.Paging;
import moefou4j.Playlist;

public interface MoeFMMethods {

	public Playlist getNextPlaylist(Playlist.PlaylistInformation info) throws MoefouException;

	public Playlist getPlaylist() throws MoefouException;

	public Playlist getPlaylist(Paging Paging) throws MoefouException;

}
