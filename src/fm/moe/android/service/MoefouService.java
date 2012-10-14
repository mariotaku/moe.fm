package fm.moe.android.service;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import moefou4j.PlaylistItem;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.IBinder;
import android.os.RemoteException;
import fm.moe.android.Constants;
import fm.moe.android.IMediaPlayerService;
import fm.moe.android.IMoefouService;
import fm.moe.android.model.ParcelablePlaylistItem;
import fm.moe.android.util.MediaPlayerStateListener;
import fm.moe.android.util.NoDuplicatesArrayList;
import fm.moe.android.util.SerializationUtil;

public class MoefouService extends Service implements Constants {

	private final ServiceStub mBinder = new ServiceStub(this);

	private IMediaPlayerService mPlayer;
	private int mAudioSessionId;

	private PlaylistManager mPlaylistManager;

	private final MediaPlayerStateListener mMediaPlayerStateListener = new MediaPlayerStateListener() {

		@Override
		public void onBufferingUpdate(final int audio_session_id, final int percent) {

		}

		@Override
		public void onCompletion(final int audio_session_id) {

		}

		@Override
		public void onError(final int audio_session_id, final int what, final int extra) {

		}

		@Override
		public void onInfo(final int audio_session_id, final int what, final int extra) {
		}

		@Override
		public void onPause(final int audio_session_id) {

		}

		@Override
		public void onPrepared(final int audio_session_id) {
			if (audio_session_id == mAudioSessionId) {
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

		}

		@Override
		public void onStop(final int audio_session_id) {

		}
	};

	private final ServiceConnection mMediaPlayerServiceConnection = new ServiceConnection() {

		@Override
		public void onServiceConnected(final ComponentName name, final IBinder service) {
			mPlayer = IMediaPlayerService.Stub.asInterface(service);
			mPlaylistManager.setIMediaPlayerService(mPlayer);
			try {
				//mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
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

	public boolean addListToQueue(final List<ParcelablePlaylistItem> items) {
		return mPlaylistManager.addAll(items);
	}

	public boolean addToQueue(final ParcelablePlaylistItem item) {
		return mPlaylistManager.add(item);
	}

	public void clearQueue() {

	}

	public ParcelablePlaylistItem findItem(final long upId) {
		return mPlaylistManager.findItem(upId);
	}

	public ParcelablePlaylistItem getCurrentItem() {
		return mPlaylistManager.getCurrentItem();
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

	public int getItemIndex(final ParcelablePlaylistItem item) {
		return mPlaylistManager.getItemIndex(item);
	}

	public ArrayList<ParcelablePlaylistItem> getPlaylist() {
		return mPlaylistManager.getPlaylist();
	}

	public int getQueuePosition() {
		return mPlaylistManager.getQueuePosition();
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

	@Override
	public IBinder onBind(final Intent intent) {
		return mBinder;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		bindService(new Intent(this, MediaPlayerService.class), mMediaPlayerServiceConnection, BIND_AUTO_CREATE);
		MediaPlayerStateListener.register(this, mMediaPlayerStateListener);
		mPlaylistManager = new PlaylistManager(this);
	}

	@Override
	public void onDestroy() {
		unregisterReceiver(mMediaPlayerStateListener);
		unbindService(mMediaPlayerServiceConnection);
		mPlaylistManager.savePlaylist();
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
		return mPlaylistManager.play(idx);
	}

	public boolean next() {
		return mPlaylistManager.next();
	}
	
	public void playShuffle() {
		mPlaylistManager.playShuffle();
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

	static class PlaylistManager {
		private final ArrayList<ParcelablePlaylistItem> mPlaylist = new NoDuplicatesArrayList<ParcelablePlaylistItem>();

		private int mPosition;

		private static final String PLAYLIST_SAVED_FILE = "playlist.dat";

		private final Random mRandom = new Random();

		private final Context mContext;
		private IMediaPlayerService mPlayer;

		@SuppressWarnings("unchecked")
		public PlaylistManager(final Context context) {
			mContext = context;
			try {
				final ArrayList<PlaylistItem> list = (ArrayList<PlaylistItem>) SerializationUtil.read(SerializationUtil
						.getSerializationFilePath(mContext, PLAYLIST_SAVED_FILE));
				if (list != null) {
					for (final PlaylistItem item : list) {
						mPlaylist.add(new ParcelablePlaylistItem(item));
					}
				}
			} catch (final ClassCastException e) {
				e.printStackTrace();
			} catch (final IOException e) {
				e.printStackTrace();
			} catch (final ClassNotFoundException e) {
				e.printStackTrace();
			}
			final SharedPreferences prefs = context.getSharedPreferences(SHARED_PREFERENCES_NAME, MODE_PRIVATE);
			jumpToId(prefs.getLong(PREFERENCE_KEY_LAST_ID, -1));
		}

		public void clear() {
			mPlaylist.clear();
			setPosition(-1);
		}

		public ParcelablePlaylistItem findItem(final long upId) {
			for (final ParcelablePlaylistItem item : mPlaylist) {
				if (item.getUpId() == upId) return item;
			}
			return null;
		}

		public ParcelablePlaylistItem get(final int position) {
			return position >= 0 && position < mPlaylist.size() ? mPlaylist.get(position) : null;
		}

		public ParcelablePlaylistItem getCurrentItem() {
			if (mPlaylist.size() == 0) return null;
			final ParcelablePlaylistItem item = get(mPosition);
			if (item != null) return item;
			return mPlaylist.get(0);
		}

		public int getItemIndex(final ParcelablePlaylistItem item) {
			return mPlaylist.indexOf(item);
		}

		@SuppressWarnings("unchecked")
		public ArrayList<ParcelablePlaylistItem> getPlaylist() {
			return (ArrayList<ParcelablePlaylistItem>) mPlaylist.clone();
		}

		public int getQueuePosition() {
			return mPosition;
		}

		public void jumpToId(final long upId) {
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
					break;
				}
			}
			setPosition(0);
		}

		public boolean play(final int idx) {
			final ParcelablePlaylistItem item = get(idx);
			if (item == null) return false;
			setPosition(idx);
			try {
				if (mPlayer.isPlaying()) {
					mPlayer.stop();
					mPlayer.reset();
				}
				mPlayer.setDataSource(String.valueOf(item.getUrl()));
				mPlayer.prepareAsync();
				return true;
			} catch (final RemoteException e) {
				e.printStackTrace();
			}
			return false;
		}
		
		public boolean next() {
			final int size = mPlaylist.size();
			if (size == 0) return false;
			if (mPosition == size - 1) return false;
			return play(mPosition++);
		}

		public void playShuffle() {
			if (mPlayer == null) return;
			final int size = mPlaylist.size();
			if (size == 0) return;
			final int idx = mRandom.nextInt(size);
			play(idx);
		}

		public void savePlaylist() {
			final ArrayList<PlaylistItem> list = new ArrayList<PlaylistItem>();
			for (final ParcelablePlaylistItem item : mPlaylist) {
				list.add(item.getPlaylistItem());
			}
			try {
				SerializationUtil
						.write(list, SerializationUtil.getSerializationFilePath(mContext, PLAYLIST_SAVED_FILE));
			} catch (final IOException e) {
				e.printStackTrace();
			}
		}

		public void setIMediaPlayerService(final IMediaPlayerService player) {
			mPlayer = player;
		}

		boolean add(final ParcelablePlaylistItem item) {
			return mPlaylist.add(item);
		}

		boolean addAll(final Collection<ParcelablePlaylistItem> items) {
			return mPlaylist.addAll(items);
		}

		void setPosition(final int pos) {
			mPosition = pos;
			mContext.sendBroadcast(new Intent(BROADCAST_ON_CURRENT_ITEM_CHANGED));
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
		public ParcelablePlaylistItem findItem(final long upId) {
			return mService.get().findItem(upId);
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
		public int getItemIndex(final ParcelablePlaylistItem item) {
			return mService.get().getItemIndex(item);
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
		public boolean pause() {
			return mService.get().pause();
		}

		@Override
		public boolean play(final int idx) {
			return mService.get().play(idx);
		}

		@Override
		public void playShuffle() {
			mService.get().playShuffle();
		}

		@Override
		public boolean seekTo(final int msec) {
			return mService.get().seekTo(msec);
		}

		@Override
		public boolean start() {
			return mService.get().start();
		}

		@Override
		public boolean next() {
			return mService.get().next();
		}

		@Override
		public void quit() {
			mService.get().stopSelf();
		}

	}
}
