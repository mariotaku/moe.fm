package fm.moe.android.service;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.Collection;
import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.Random;

import moefou4j.Moefou;
import moefou4j.MoefouException;
import moefou4j.Playlist;
import moefou4j.PlaylistItem;

import org.json.JSONException;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.AsyncTask;
import android.os.IBinder;
import android.os.RemoteException;
import fm.moe.android.Constants;
import fm.moe.android.IMediaPlayerService;
import fm.moe.android.IMoefouService;
import fm.moe.android.R;
import fm.moe.android.model.ParcelablePlaylistItem;
import fm.moe.android.util.JSONFileHelper;
import fm.moe.android.util.MediaPlayerStateListener;
import fm.moe.android.util.NoDuplicatesArrayList;
import fm.moe.android.util.Utils;

public class MoefouService extends Service implements Constants {

	private final ServiceStub mBinder = new ServiceStub(this);

	private int mAudioSessionId;

	private IMediaPlayerService mPlayer;

	private PlaylistManager mManager;
	private NotificationManager mNotificationManager;

	public static final int PLAY_MODE_SITE_SHUFFLE = 0;

	private final MediaPlayerStateListener mMediaPlayerStateListener = new MediaPlayerStateListener() {

		@Override
		public void onBufferingUpdate(final int audio_session_id, final int percent) {

		}

		@Override
		public void onCompletion(final int audio_session_id) {
			System.out.println("onCompletion");
			mNotificationManager.cancel(NOTIFICATION_ID_NOW_PLAYING);
			if (audio_session_id == mAudioSessionId) {
				mManager.next(true);
			}
		}

		@Override
		public void onError(final int audio_session_id, final int what, final int extra) {

		}

		@Override
		public void onInfo(final int audio_session_id, final int what, final int extra) {
		}

		@Override
		public void onPause(final int audio_session_id) {
			mNotificationManager.cancel(NOTIFICATION_ID_NOW_PLAYING);
		}

		@Override
		public void onPrepared(final int audio_session_id) {
			System.out.println("onPrepared");
			if (audio_session_id == mAudioSessionId) {
				showNowPlayingNotification();
				try {
					mPlayer.start();
				} catch (final RemoteException e) {
					e.printStackTrace();
				}
			}
		}

		@Override
		public void onSeekComplete(final int audio_session_id) {

		}

		@Override
		public void onStart(final int audio_session_id) {
			showNowPlayingNotification();
		}

		@Override
		public void onStop(final int audio_session_id) {
			mNotificationManager.cancel(NOTIFICATION_ID_NOW_PLAYING);
		}
	};

	private final ServiceConnection mMediaPlayerServiceConnection = new ServiceConnection() {

		@Override
		public void onServiceConnected(final ComponentName name, final IBinder service) {
			mPlayer = IMediaPlayerService.Stub.asInterface(service);
			mManager.setIMediaPlayerService(mPlayer);
			try {
				mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
				mAudioSessionId = mPlayer.getAudioSessionId();
			} catch (final RemoteException e) {
				mAudioSessionId = -1;
			}
		}

		@Override
		public void onServiceDisconnected(final ComponentName name) {
			mPlayer = null;
		}

	};

	private static final String JSON_FILENAME_CURRENT_ITEM = "current_item.json";

	public boolean addListToQueue(final List<ParcelablePlaylistItem> items) {
		return mManager.addAll(items);
	}

	public boolean addToQueue(final ParcelablePlaylistItem item) {
		return mManager.add(item);
	}

	public void clearQueue() {
		mManager.clear();
	}


	public ParcelablePlaylistItem getCurrentItem() {
		return mManager.getCurrentItem();
	}

	public int getCurrentPosition() {
		if (mPlayer == null) return -1;
		try {
			return mPlayer.getCurrentPosition();
		} catch (final RemoteException e) {
			e.printStackTrace();
		}
		return -1;
	}

	public int getDuration() {
		if (mPlayer == null) return -1;
		try {
			return mPlayer.getDuration();
		} catch (final RemoteException e) {
			e.printStackTrace();
		}
		return -1;
	}

	public List<ParcelablePlaylistItem> getPlaylist() {
		return mManager.getPlaylist();
	}

	public int getQueuePosition() {
		return mManager.getQueuePosition();
	}

	public boolean isPlaying() {
		if (mPlayer == null) return false;
		try {
			return mPlayer.isPlaying();
		} catch (final RemoteException e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean isPrepared() {
		if (mPlayer == null) return false;
		try {
			return mPlayer.isPrepared();
		} catch (final RemoteException e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean next() {
		return mManager.next(false);
	}

	@Override
	public IBinder onBind(final Intent intent) {
		return mBinder;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		bindService(new Intent(this, MediaPlayerService.class), mMediaPlayerServiceConnection, BIND_AUTO_CREATE);
		MediaPlayerStateListener.register(this, mMediaPlayerStateListener);
		mManager = new PlaylistManager(this);
		mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
	}

	@Override
	public void onDestroy() {
		unregisterReceiver(mMediaPlayerStateListener);
		unbindService(mMediaPlayerServiceConnection);
		mManager.saveStates();
		super.onDestroy();
	}

	public boolean pause() {
		if (mPlayer == null) return false;
		try {
			return mPlayer.pause();
		} catch (final RemoteException e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean play(final int idx) {
		return mManager.play(idx);
	}

	public boolean prev() {
		return mManager.prev(false);
	}

	public boolean seekTo(final int msec) {
		if (mPlayer == null) return false;
		try {
			return mPlayer.seekTo(msec);
		} catch (final RemoteException e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean shuffle() {
		return mManager.shuffle();
	}

	public boolean start() {
		if (mPlayer == null) return false;
		try {
			if (mPlayer.isPlaying()) return false;
			return mPlayer.start();
		} catch (final RemoteException e) {
			e.printStackTrace();
		}
		return false;
	}

	private void showNowPlayingNotification() {
		if (mManager == null) return;
		final ParcelablePlaylistItem item = mManager.getCurrentItem();
		if (item == null) return;
		final Notification.Builder builder = new Notification.Builder(this);
		builder.setOngoing(true);
		builder.setContentTitle(item.getTitle());
		builder.setContentText(item.getArtist());
		builder.setSmallIcon(R.drawable.ic_stat_now_playing);
		mNotificationManager.notify(NOTIFICATION_ID_NOW_PLAYING, builder.getNotification());
	}

	static class PlaylistManager {

		private static final String JSON_FILENAME_PLAYLIST = "playlist.json";
		private static final String JSON_FILENAME_HISTORY = "history.json";

		private final List<ParcelablePlaylistItem> mPlaylist = Collections
				.synchronizedList(new NoDuplicatesArrayList<ParcelablePlaylistItem>());
		private final List<ParcelablePlaylistItem> mHistory = Collections
				.synchronizedList(new NoDuplicatesArrayList<ParcelablePlaylistItem>(100));

		private int mPosition;
		private int mPlayMode = PLAY_MODE_SITE_SHUFFLE;

		private final Random mRandom = new Random();

		private final Context mContext;

		private IMediaPlayerService mPlayer;
		private final SiteShuffler mSiteShuffler;

		PlaylistManager(final Context context) {
			mContext = context;
			mSiteShuffler = new SiteShuffler(context, mHistory, this);
			try {
				mPlaylist.addAll(ParcelablePlaylistItem.createListFromFile(mContext, JSON_FILENAME_PLAYLIST));
				mHistory.addAll(ParcelablePlaylistItem.createListFromFile(mContext, JSON_FILENAME_HISTORY));
			} catch (final JSONException e) {
				e.printStackTrace();
			} catch (final IOException e) {
				e.printStackTrace();
			}
			final SharedPreferences prefs = context.getSharedPreferences(SHARED_PREFERENCES_NAME, MODE_PRIVATE);
			jumpToId(prefs.getLong(PREFERENCE_KEY_LAST_ID, -1));
		}

		boolean add(final ParcelablePlaylistItem item) {
			return mPlaylist.add(item);
		}

		boolean addAll(final Collection<ParcelablePlaylistItem> items) {
			return mPlaylist.addAll(items);
		}

		void clear() {
			mPlaylist.clear();
			setPosition(-1);
		}

		ParcelablePlaylistItem findItem(final long upId) {
			for (final ParcelablePlaylistItem item : mPlaylist) {
				if (item.getUpId() == upId) return item;
			}
			return null;
		}

		ParcelablePlaylistItem get(final int position) {
			if (mPlayMode == PLAY_MODE_SITE_SHUFFLE) // 随机播放电台中的曲目，队列里没有曲目。
				return mSiteShuffler.getCurrentItem();
			if (mPlaylist.size() == 0) return null;
			return position >= 0 && position < mPlaylist.size() ? mPlaylist.get(position) : null;
		}

		ParcelablePlaylistItem getCurrentItem() {
			final ParcelablePlaylistItem item = get(mPosition);
			if (item != null) return item;
			return mPlaylist.get(0);
		}

		int getItemIndex(final ParcelablePlaylistItem item) {
			return mPlaylist.indexOf(item);
		}

		List<ParcelablePlaylistItem> getPlaylist() {
			return mPlaylist;
		}

		int getQueuePosition() {
			switch (mPlayMode) {
				case PLAY_MODE_SITE_SHUFFLE: {
					return -1;
				}
			}
			return mPosition;
		}

		boolean isPlaying() {
			if (mPlayer == null) return false;
			try {
				return mPlayer.isPlaying();
			} catch (final RemoteException e) {
				e.printStackTrace();
			}
			return false;
		}

		void jumpToId(final long upId) {
			final int size = mPlaylist.size();
			if (size == 0) {
				setPosition(-1);
				return;
			}
			if (upId <= 0) {
				setPosition(0);
				return;
			}
			for (int i = 0; i < size; i++) {
				if (mPlaylist.get(i).getUpId() == upId) {
					setPosition(i);
					return;
				}
			}
			setPosition(0);
		}

		boolean load(final int idx) {
			try {
				if (mPlayer.isPlaying()) {
					mPlayer.stop();
					mPlayer.reset();
				}
				final ParcelablePlaylistItem item = get(idx);
				if (item == null) return false;
				return mPlayer.setDataSource(String.valueOf(item.getUrl()));
			} catch (final RemoteException e) {
				e.printStackTrace();
			}
			return false;
		}

		boolean next(final boolean play_now) {
			switch (mPlayMode) {
				case PLAY_MODE_SITE_SHUFFLE: {
					return mSiteShuffler.next(play_now);
				}
			}
			final int size = mPlaylist.size();
			if (size == 0) return false;
			if (mPosition == size - 1) return false;
			final boolean ret = select(mPosition + 1);
			return play_now ? prepare() : ret;
		}

		boolean play(final int idx) {
			return load(idx) && prepare();
		}

		boolean prepare() {
			try {
				return mPlayer.prepareAsync();
			} catch (final RemoteException e) {
				e.printStackTrace();
			}
			return false;
		}

		boolean prev(final boolean play_now) {
			switch (mPlayMode) {
				case PLAY_MODE_SITE_SHUFFLE: {
					return mSiteShuffler.prev(play_now);
				}
			}
			final int size = mPlaylist.size();
			if (size == 0) return false;
			if (mPosition == 0) return false;
			final boolean ret = select(mPosition + 1);
			return play_now ? prepare() : ret;
		}

		void saveStates() {
			try {
				ParcelablePlaylistItem.saveListToFile(mContext, JSON_FILENAME_PLAYLIST, mPlaylist);
				ParcelablePlaylistItem.saveListToFile(mContext, JSON_FILENAME_HISTORY, mHistory);
				mSiteShuffler.saveQueue();
				final ParcelablePlaylistItem item = getCurrentItem();
				if (item != null) {
					JSONFileHelper.write(item.toJSONObject(),
							JSONFileHelper.getFilePath(mContext, JSON_FILENAME_CURRENT_ITEM));
				}
			} catch (final JSONException e) {
				e.printStackTrace();
			} catch (final IOException e) {
				e.printStackTrace();
			}
		}

		boolean select(final int idx) {
			final ParcelablePlaylistItem item = get(idx);
			if (item == null) return false;
			setPosition(idx);
			return isPlaying() ? play(idx) : load(idx);
		}

		void setIMediaPlayerService(final IMediaPlayerService player) {
			mPlayer = player;
		}

		void setPlayMode(final int mode) {
			switch (mode) {
				case PLAY_MODE_SITE_SHUFFLE: {
					mPlayMode = PLAY_MODE_SITE_SHUFFLE;
					break;
				}
				default: {
					break;
				}
			}
		}

		void setPosition(final int pos) {
			mPosition = pos;
			mContext.sendBroadcast(new Intent(BROADCAST_ON_CURRENT_ITEM_CHANGED));
		}

		boolean shuffle() {
			switch (mPlayMode) {
				case PLAY_MODE_SITE_SHUFFLE: {
					return mSiteShuffler.shuffle(false);
				}
			}
			if (mPlayer == null) return false;
			final int size = mPlaylist.size();
			if (size == 0) return false;
			final int idx = mRandom.nextInt(size);
			return select(idx);
		}
	}

	static final class ServiceStub extends IMoefouService.Stub {

		private final WeakReference<MoefouService> mService;

		ServiceStub(final MoefouService service) {
			mService = new WeakReference<MoefouService>(service);
		}

		@Override
		public boolean addListToQueue(final List<ParcelablePlaylistItem> items) {
			return mService.get().addListToQueue(items);
		}

		@Override
		public boolean addToQueue(final ParcelablePlaylistItem item) {
			return mService.get().addToQueue(item);
		}

		@Override
		public void clearQueue() {
			mService.get().clearQueue();
		}

		@Override
		public ParcelablePlaylistItem getCurrentItem() {
			return mService.get().getCurrentItem();
		}

		@Override
		public int getCurrentPosition() {
			return mService.get().getCurrentPosition();
		}

		@Override
		public int getDuration() {
			return mService.get().getDuration();
		}

		@Override
		public List<ParcelablePlaylistItem> getPlaylist() {
			return mService.get().getPlaylist();
		}

		@Override
		public int getQueuePosition() {
			return mService.get().getQueuePosition();
		}

		@Override
		public boolean isPlaying() {
			return mService.get().isPlaying();
		}

		@Override
		public boolean isPrepared() {
			return mService.get().isPrepared();
		}

		@Override
		public boolean next() {
			return mService.get().next();
		}

		@Override
		public boolean pause() {
			return mService.get().pause();
		}

		@Override
		public boolean play(final int idx) {
			return mService.get().play(idx);
		}

		@Override
		public boolean prev() {
			return mService.get().prev();
		}

		@Override
		public void quit() {
			mService.get().stopSelf();
		}

		@Override
		public boolean seekTo(final int msec) {
			return mService.get().seekTo(msec);
		}

		@Override
		public void shuffle() {
			mService.get().shuffle();
		}

		@Override
		public boolean start() {
			return mService.get().start();
		}

	}

	/**
	 * 用于随机播放萌否电台曲目的类
	 * 
	 * @author mariotaku
	 * 
	 */
	static final class SiteShuffler {

		private static final String JSON_FILENAME_SITE_SHUFFLE_QUEUE = "site_shuffle_queue.json";

		private final Context context;

		private final Random random = new Random();

		private final List<ParcelablePlaylistItem> history;

		private final List<ParcelablePlaylistItem> queue = Collections
				.synchronizedList(new NoDuplicatesArrayList<ParcelablePlaylistItem>());
		private int mPosition = -1;

		private boolean is_playing_history;
		private ParcelablePlaylistItem current;

		private GetPlaylistTask task;

		private final PlaylistManager manager;

		SiteShuffler(final Context context, final List<ParcelablePlaylistItem> history, final PlaylistManager manager) {
			this.context = context;
			this.history = history;
			this.manager = manager;
			try {
				queue.addAll(ParcelablePlaylistItem.createListFromFile(context, JSON_FILENAME_SITE_SHUFFLE_QUEUE));
				current = new ParcelablePlaylistItem(JSONFileHelper.read(JSONFileHelper.getFilePath(context,
						JSON_FILENAME_CURRENT_ITEM)));
			} catch (final JSONException e) {
				e.printStackTrace();
			} catch (final IOException e) {
				e.printStackTrace();
			}
			checkQueue();
		}

		void checkQueue() {
			if (queue.size() == 0) {
				getPlaylist();
			}
		}

		ParcelablePlaylistItem getCurrentItem() {
			return current;
		}

		void getPlaylist() {
			if (task != null) {
				task.cancel(true);
			}
			task = new GetPlaylistTask(context, queue);
			task.execute();
		}

		boolean hasNext() {
			return is_playing_history || queue.size() > 0;
		}

		boolean next(final boolean play_now) {
			checkQueue();
			if (mPosition == -1 || mPosition >= history.size() - 1) {
				// 没有在播放之前的曲目或者已经到了历史的最后一条，所以直接从还没播放的列表里随机挑一首
				mPosition = -1;
				is_playing_history = false;
				return shuffle(play_now);
			}
			mPosition++;
			final boolean ret = select(history.get(mPosition));
			return play_now ? manager.prepare() : ret;
		}

		boolean prev(final boolean play_now) {
			checkQueue();
			if (mPosition == 0 || history.size() == 0) // 已经到了播放历史的第一个或者历史是空的
				return false;
			if (mPosition >= history.size() || mPosition < 0) {
				mPosition = history.size() - 1;
			}
			is_playing_history = true;
			mPosition--;
			final boolean ret = select(history.get(mPosition));
			return play_now ? manager.prepare() : ret;
		}

		void saveQueue() throws JSONException, IOException {
			ParcelablePlaylistItem.saveListToFile(context, JSON_FILENAME_SITE_SHUFFLE_QUEUE, queue);
		}

		boolean select(final ParcelablePlaylistItem item) {
			current = item;
			if (item == null) return false;
			context.sendBroadcast(new Intent(BROADCAST_ON_CURRENT_ITEM_CHANGED));
			return manager.isPlaying() ? manager.play(-1) : manager.load(-1);
		}

		boolean shuffle(final boolean play_now) {
			checkQueue();
			if (queue.size() == 0) return false;
			if (current != null) {
				history.add(current);
			}
			final int random_idx = random.nextInt(queue.size());
			final ParcelablePlaylistItem item = queue.get(random_idx);
			queue.remove(item);
			if (queue.size() < 4) {
				getPlaylist();
			}
			final boolean ret = select(item);
			return play_now ? manager.prepare() : ret;
		}

		static final class GetPlaylistTask extends AsyncTask<Void, Void, Playlist> {

			private final Context context;
			private final List<ParcelablePlaylistItem> list;

			GetPlaylistTask(final Context context, final List<ParcelablePlaylistItem> list) {
				this.context = context;
				this.list = list;
			}

			@Override
			protected Playlist doInBackground(final Void... args) {
				final Moefou moefou = Utils.getMoefouInstance(context);
				if (moefou == null) return null;
				try {
					return moefou.getPlaylist();
				} catch (final MoefouException e) {
					return null;
				}
			}

			@Override
			protected void onPostExecute(final Playlist result) {
				if (result == null) return;
				try {
					for (final PlaylistItem item : result) {
						list.add(new ParcelablePlaylistItem(item));
					}
				} catch (final ConcurrentModificationException e) {
					e.printStackTrace();
				}
				context.sendBroadcast(new Intent(BROADCAST_ON_CURRENT_ITEM_CHANGED));
			}
		}
	}
}
