package fm.moe.android;

import fm.moe.android.model.ParcelablePlaylistItem;

interface IMoefouService {

	boolean addListToQueue(in List<ParcelablePlaylistItem> items);
	boolean addToQueue(in ParcelablePlaylistItem item);
	void clearQueue();
	int getCurrentPosition();
	int getDuration();
	boolean isPlaying();
	boolean isPrepared();
	boolean pause();
	boolean next();
	boolean play(int position);
	boolean start();
	boolean seekTo(int msec);
	void playShuffle();
	List<ParcelablePlaylistItem> getPlaylist();
	int getQueuePosition();
	int getItemIndex(in ParcelablePlaylistItem item);
	ParcelablePlaylistItem findItem(long upId);
	ParcelablePlaylistItem getCurrentItem();
	void quit();

}