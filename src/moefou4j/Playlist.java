package moefou4j;

public interface Playlist extends ResponseList<PlaylistItem> {

	@Override
	public PlaylistInformation getInformation();

	public interface PlaylistInformation extends Information {

		public int getItemCount();

		public String getNextUrl();

		public int getPage();

		public boolean isTarget();

		public boolean mayHaveNext();
	}
}
