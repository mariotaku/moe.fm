package fm.moe.android;

import fm.moe.android.model.ParcelablePlaylistItem;

interface IMoefouService {

	boolean play(int position);
	boolean start();
	boolean pause();
	boolean next();
	boolean prev();
	boolean isPlaying();
	boolean isPreparing();
	boolean isPrepared();
	boolean seekTo(int msec);
	boolean addAll(in List<ParcelablePlaylistItem> items);
	boolean add(in ParcelablePlaylistItem item);
	boolean update(in ParcelablePlaylistItem item);
	boolean remove(in ParcelablePlaylistItem item);
	void clear();
	int getCurrentPosition();
	int getDuration();
	void shuffle();
	List<ParcelablePlaylistItem> getPlaylist();
	int getQueuePosition();
	ParcelablePlaylistItem getCurrentItem();
	int getAudioSessionId();
	void quit();

}
