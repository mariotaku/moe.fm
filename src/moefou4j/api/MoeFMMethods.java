package moefou4j.api;

import moefou4j.MoefouException;
import moefou4j.PlaylistItem;
import moefou4j.ResponseList;

public interface MoeFMMethods {

	public ResponseList<PlaylistItem> getPlaylist() throws MoefouException;

}
