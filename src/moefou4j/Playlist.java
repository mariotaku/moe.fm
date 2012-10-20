package moefou4j;

public interface Playlist extends ResponseList<PlaylistItem> {

	@Override
	public PlaylistInformation getInformation();

	public interface PlaylistInformation extends PageableInformation {

		public String getNextUrl();

		public boolean isTarget();

		public boolean mayHaveNext();
	}
}
