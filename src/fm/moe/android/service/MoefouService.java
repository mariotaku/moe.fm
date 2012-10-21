package fm.moe.android.service;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.Collection;
import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.Random;

import moefou4j.Moefou;
import moefou4j.Playlist;
import moefou4j.PlaylistItem;
import moefou4j.ResponseMessage;

import org.json.JSONException;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.os.AsyncTask;
import android.os.IBinder;
import android.os.RemoteException;
import fm.moe.android.Constants;
import fm.moe.android.IMediaPlayerService;
import fm.moe.android.IMoefouService;
import fm.moe.android.R;
import fm.moe.android.activity.NowPlayingActivity;
import fm.moe.android.model.ParcelablePlaylistItem;
import fm.moe.android.util.JSONFileHelper;
import fm.moe.android.util.LazyImageLoader;
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
			mNotificationManager.cancel(NOTIFICATION_ID_NOW_PLAYING);
			if (audio_session_id == mAudioSessionId) {
				mManager.next(true);
				mManager.logListened(mManager.getCurrentItem());
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
			
		}

		@Override
		public void onPrepareStateChange(final int audio_session_id) {

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

	private LazyImageLoader mCoverLoader;

	private static final String JSON_FILENAME_CURRENT_ITEM = "current_item.json";

	public boolean add(final ParcelablePlaylistItem item) {
		return mManager.add(item);
	}

	public boolean addAll(final List<ParcelablePlaylistItem> items) {
		return mManager.addAll(items);
	}

	public void clear() {
		mManager.clear();
	}

	public int getAudioSessionId() {
		if (mPlayer == null) return -1;
		try {
			return mPlayer.getAudioSessionId();
		} catch (final RemoteException e) {
			e.printStackTrace();
		}
		return -1;
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

	public boolean isPreparing() {
		if (mPlayer == null) return false;
		try {
			return mPlayer.isPreparing();
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
		final Resources res = getResources();
		mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		mCoverLoader = new LazyImageLoader(this, "album_covers", R.drawable.ic_mp_albumart_unknown,
				res.getDimensionPixelSize(R.dimen.album_cover_size),
				res.getDimensionPixelSize(R.dimen.album_cover_size), 3);
	}

	@Override
	public void onDestroy() {
		unregisterReceiver(mMediaPlayerStateListener);
		unbindService(mMediaPlayerServiceConnection);
		mManager.saveStates();
		mNotificationManager.cancelAll();
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

	public boolean remove(final ParcelablePlaylistItem item) {
		return mManager.remove(item);
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
		return shuffle(false);
	}

	public boolean shuffle(final boolean play_now) {
		return mManager.shuffle(play_now);
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

	public boolean update(final ParcelablePlaylistItem item) {
		return mManager.update(item);
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
		final String cover_path = mCoverLoader.getCachedImagePath(item.getCoverUrl());
		if (cover_path == null) {
			builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_mp_albumart_unknown));
		} else {
			builder.setLargeIcon(BitmapFactory.decodeFile(cover_path));
		}
		final Intent intent = new Intent(this, NowPlayingActivity.class);
		final PendingIntent content_intent = PendingIntent.getActivity(this, 0, intent, 0);
		builder.setContentIntent(content_intent);
		mNotificationManager.notify(NOTIFICATION_ID_NOW_PLAYING, builder.getNotification());
	}

	static class PlaylistManager {

		private static final String JSON_FILENAME_PLAYLIST = "playlist.json";
		private static final String JSON_FILENAME_HISTORY = "history.json";

		private final Random mRandom = new Random();

		private final List<ParcelablePlaylistItem> mPlaylist = Collections
				.synchronizedList(new NoDuplicatesArrayList<ParcelablePlaylistItem>());
		private final List<ParcelablePlaylistItem> mHistory = Collections
				.synchronizedList(new NoDuplicatesArrayList<ParcelablePlaylistItem>(100));

		private final Context mContext;
		private final SiteShuffler mShuffler;

		private IMediaPlayerService mPlayer;

		private int mPosition;
		private int mPlayMode = PLAY_MODE_SITE_SHUFFLE;

		PlaylistManager(final Context context) {
			mContext = context;
			mShuffler = new SiteShuffler(context, mHistory, this);
			try {
				mPlaylist.addAll(ParcelablePlaylistItem.createListFromFile(context, JSON_FILENAME_PLAYLIST));
				mHistory.addAll(ParcelablePlaylistItem.createListFromFile(context, JSON_FILENAME_HISTORY));
			} catch (final JSONException e) {
				e.printStackTrace();
			} catch (final IOException e) {
				e.printStackTrace();
			}
			final SharedPreferences prefs = context.getSharedPreferences(SHARED_PREFERENCES_NAME, MODE_PRIVATE);
			jumpToId(prefs.getLong(PREFERENCE_KEY_LAST_ID, -1));
		}

		public boolean remove(final ParcelablePlaylistItem item) {
			boolean ret = false;
			ret |= mPlaylist.remove(item);
			ret |= mHistory.add(item);
			if (mShuffler.remove(item)) {
				ret |= true;
				next(isPlaying());
			}
			return ret;
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
				return mShuffler.getCurrentItem();
			if (mPlaylist.size() == 0) return null;
			return position >= 0 && position < mPlaylist.size() ? mPlaylist.get(position) : null;
		}

		ParcelablePlaylistItem getCurrentItem() {
			final ParcelablePlaylistItem item = get(mPosition);
			if (item != null) return item;
			return mPlaylist.size() > 0 ? mPlaylist.get(0) : null;
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

		boolean load(final int idx, final boolean play_now) {
			try {
				if (mPlayer.isPlaying()) {
					mPlayer.stop();
					mPlayer.reset();
				}
				final ParcelablePlaylistItem item = get(idx);
				if (item == null) return false;
				return mPlayer.open(String.valueOf(item.getUrl()), play_now);
			} catch (final RemoteException e) {
				e.printStackTrace();
			}
			return false;
		}

		void logListened(final ParcelablePlaylistItem item) {
			new LogListenedTask(mContext, item).execute();
		}

		boolean next(final boolean play_now) {
			switch (mPlayMode) {
				case PLAY_MODE_SITE_SHUFFLE: {
					return mShuffler.next(play_now);
				}
			}
			final int size = mPlaylist.size();
			if (size == 0) return false;
			if (mPosition == size - 1) return false;
			return select(mPosition + 1, play_now);
		}

		boolean play(final int idx) {
			return load(idx, true);
		}

		boolean prev(final boolean play_now) {
			switch (mPlayMode) {
				case PLAY_MODE_SITE_SHUFFLE: {
					return mShuffler.prev(play_now);
				}
			}
			final int size = mPlaylist.size();
			if (size == 0) return false;
			if (mPosition == 0) return false;
			return select(mPosition + 1, play_now);
		}

		void saveStates() {
			try {
				ParcelablePlaylistItem.saveListToFile(mContext, JSON_FILENAME_PLAYLIST, mPlaylist);
				ParcelablePlaylistItem.saveListToFile(mContext, JSON_FILENAME_HISTORY, mHistory);
				mShuffler.saveQueue();
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

		boolean select(final int idx, final boolean play_now) {
			final ParcelablePlaylistItem item = get(idx);
			if (item == null) return false;
			setPosition(idx);
			return load(idx, play_now);
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
			mContext.sendBroadcast(new Intent(BROADCAST_ON_CURRENT_ITEM_CHANGE));
		}

		boolean shuffle(final boolean play_now) {
			switch (mPlayMode) {
				case PLAY_MODE_SITE_SHUFFLE: {
					return mShuffler.shuffle(false);
				}
			}
			if (mPlayer == null) return false;
			final int size = mPlaylist.size();
			if (size == 0) return false;
			final int idx = mRandom.nextInt(size);
			return select(idx, play_now);
		}

		boolean update(final ParcelablePlaylistItem item) {
			boolean ret = false;
			if (mPlaylist.remove(item)) {
				if (!ret) {
					ret = mPlaylist.add(item);
				}
			}
			if (mHistory.remove(item)) {
				if (!ret) {
					ret = mHistory.add(item);
				}
			}
			if (mShuffler.update(item)) {
				ret = true;
			}
			if (ret) {
				mContext.sendBroadcast(new Intent(BROADCAST_ON_CURRENT_ITEM_CHANGE));
			}
			return ret;
		}

		static final class LogListenedTask extends AsyncTask<Void, Void, ResponseMessage> {

			private final ParcelablePlaylistItem item;
			private final Moefou moefou;

			LogListenedTask(final Context context, final ParcelablePlaylistItem item) {
				this.item = item;
				moefou = Utils.getMoefouInstance(context);
			}

			@Override
			protected ResponseMessage doInBackground(final Void... args) {
				if (moefou == null || item == null) return null;
				try {
					return moefou.logListened(item.getSubId());
				} catch (final Exception e) {
					return null;
				}
			}

			@Override
			protected void onPostExecute(final ResponseMessage result) {
				if (result == null) return;
			}
		}

	}

	static final class ServiceStub extends IMoefouService.Stub {

		private final WeakReference<MoefouService> mService;

		ServiceStub(final MoefouService service) {
			mService = new WeakReference<MoefouService>(service);
		}

		@Override
		public boolean add(final ParcelablePlaylistItem item) {
			return mService.get().add(item);
		}

		@Override
		public boolean addAll(final List<ParcelablePlaylistItem> items) {
			return mService.get().addAll(items);
		}

		@Override
		public void clear() {
			mService.get().clear();
		}

		@Override
		public int getAudioSessionId() throws RemoteException {
			return mService.get().getAudioSessionId();
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
		public boolean isPreparing() {
			return mService.get().isPreparing();
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
		public boolean remove(final ParcelablePlaylistItem item) throws RemoteException {
			return mService.get().remove(item);
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

		@Override
		public boolean update(final ParcelablePlaylistItem item) {
			return mService.get().update(item);
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

		private final List<ParcelablePlaylistItem> mHistory;

		private final List<ParcelablePlaylistItem> mQueue = Collections
				.synchronizedList(new NoDuplicatesArrayList<ParcelablePlaylistItem>());
		private int position = -1;

		private boolean is_playing_history;
		private ParcelablePlaylistItem mCurrent;

		private GetPlaylistTask task;

		private final PlaylistManager manager;

		SiteShuffler(final Context context, final List<ParcelablePlaylistItem> history, final PlaylistManager manager) {
			this.context = context;
			mHistory = history;
			this.manager = manager;
			try {
				mQueue.addAll(ParcelablePlaylistItem.createListFromFile(context, JSON_FILENAME_SITE_SHUFFLE_QUEUE));
				mCurrent = new ParcelablePlaylistItem(JSONFileHelper.read(JSONFileHelper.getFilePath(context,
						JSON_FILENAME_CURRENT_ITEM)));
			} catch (final JSONException e) {
				e.printStackTrace();
			} catch (final IOException e) {
				e.printStackTrace();
			}
			checkQueue();
		}

		void checkQueue() {
			if (mQueue.size() == 0) {
				getPlaylist();
			}
		}

		ParcelablePlaylistItem getCurrentItem() {
			if (mCurrent == null) {
				shuffle(false);
			}
			return mCurrent;
		}

		void getPlaylist() {
			if (task != null) {
				task.cancel(true);
			}
			task = new GetPlaylistTask(context, mQueue);
			task.execute();
		}

		boolean hasNext() {
			return is_playing_history || mQueue.size() > 0;
		}

		boolean next(final boolean play_now) {
			checkQueue();
			if (position == -1 || position >= mHistory.size() - 1) {
				// 没有在播放之前的曲目或者已经到了历史的最后一条，所以直接从还没播放的列表里随机挑一首
				position = -1;
				is_playing_history = false;
				return shuffle(play_now);
			}
			position++;
			return select(mHistory.get(position), play_now);
		}

		boolean prev(final boolean play_now) {
			checkQueue();
			if (position == 0 || mHistory.size() == 0) // 已经到了播放历史的第一个或者历史是空的
				return false;
			if (position >= mHistory.size() || position < 0) {
				position = mHistory.size() - 1;
			}
			is_playing_history = true;
			position--;
			return select(mHistory.get(position), play_now);
		}

		boolean remove(final ParcelablePlaylistItem item) {
			if (item == null) return false;
			boolean ret = false;
			ret |= mQueue.remove(item);
			if (item.equals(mCurrent)) {
				mCurrent = null;
				ret |= true;
			}
			return ret;
		}

		void saveQueue() throws JSONException, IOException {
			ParcelablePlaylistItem.saveListToFile(context, JSON_FILENAME_SITE_SHUFFLE_QUEUE, mQueue);
		}

		boolean select(final ParcelablePlaylistItem item, final boolean play_now) {
			mCurrent = item;
			if (item == null) return false;
			context.sendBroadcast(new Intent(BROADCAST_ON_CURRENT_ITEM_CHANGE));
			return manager.isPlaying() ? manager.play(-1) : manager.load(-1, play_now);
		}

		boolean shuffle(final boolean play_now) {
			checkQueue();
			if (mQueue.size() == 0) return false;
			if (mCurrent != null) {
				mHistory.add(mCurrent);
			}
			final int random_idx = random.nextInt(mQueue.size());
			final ParcelablePlaylistItem item = mQueue.get(random_idx);
			mQueue.remove(item);
			if (mQueue.size() < 4) {
				getPlaylist();
			}
			return select(item, play_now);
		}

		boolean update(final ParcelablePlaylistItem item) {
			if (item == null) return false;
			boolean ret = false;
			if (mQueue.remove(item)) {
				ret |= mQueue.add(item);
			}
			if (item.equals(item)) {
				mCurrent = item;
				ret |= true;
			}
			return ret;
		}

		static final class GetPlaylistTask extends AsyncTask<Void, Void, Playlist> {

			private final Context context;
			private final List<ParcelablePlaylistItem> list;
			private final Moefou moefou;

			GetPlaylistTask(final Context context, final List<ParcelablePlaylistItem> list) {
				this.context = context;
				this.list = list;
				moefou = Utils.getMoefouInstance(context);
			}

			@Override
			protected Playlist doInBackground(final Void... args) {
				if (moefou == null) return null;
				try {
					return moefou.getPlaylist();
				} catch (final Exception e) {
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
				context.sendBroadcast(new Intent(BROADCAST_ON_CURRENT_ITEM_CHANGE));
			}
		}
	}
}
