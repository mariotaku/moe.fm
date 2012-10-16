package moefou4j;

import java.net.URL;

public interface Playlist extends ResponseList<PlaylistItem> {

	public PlaylistInformation getInformation();
	
	public interface PlaylistInformation extends Information {
		
		public int getPage();
		
		public int getItemCount();
		
		public boolean isTarget();
		
		public boolean mayHaveNext();
		
		public String getNextUrl();
	}
}
