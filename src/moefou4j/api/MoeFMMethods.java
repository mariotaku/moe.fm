package moefou4j.api;

import moefou4j.MoefouException;
import moefou4j.Playlist;
import moefou4j.Paging;

public interface MoeFMMethods {

	public Playlist getPlaylist() throws MoefouException;
	
	public Playlist getPlaylist(Paging Paging) throws MoefouException;
	
	public Playlist getNextPlaylist(Playlist.PlaylistInformation info) throws MoefouException;

}
