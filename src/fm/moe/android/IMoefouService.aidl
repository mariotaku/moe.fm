package fm.moe.android;

import fm.moe.android.model.ParcelablePlaylistItem;

interface IMoefouService {

	boolean play(int position);
	boolean start();
	boolean pause();
	boolean next();
	boolean prev();
	boolean isPlaying();
	boolean isPrepared();
	boolean seekTo(int msec);
	boolean addListToQueue(in List<ParcelablePlaylistItem> items);
	boolean addToQueue(in ParcelablePlaylistItem item);
	void clearQueue();
	int getCurrentPosition();
	int getDuration();
	void shuffle();
	List<ParcelablePlaylistItem> getPlaylist();
	int getQueuePosition();
	ParcelablePlaylistItem getCurrentItem();
	void quit();

}
