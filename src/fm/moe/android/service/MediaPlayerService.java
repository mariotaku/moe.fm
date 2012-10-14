package fm.moe.android.service;

import java.io.IOException;
import java.lang.ref.WeakReference;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnInfoListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.MediaPlayer.OnSeekCompleteListener;
import android.net.Uri;
import android.os.IBinder;
import android.os.RemoteException;
import fm.moe.android.Constants;
import fm.moe.android.IMediaPlayerService;

public class MediaPlayerService extends Service implements Constants, OnBufferingUpdateListener, OnCompletionListener,
		OnErrorListener, OnInfoListener, OnPreparedListener, OnSeekCompleteListener {

	private MediaPlayer mMediaPlayer;

	private final ServiceStub mBinder = new ServiceStub(this);

	private boolean mPlayerPrepared;

	public void attachAuxEffect(final int effectId) {
		if (mMediaPlayer == null) return;
		mMediaPlayer.attachAuxEffect(effectId);
	}

	public int getAudioSessionId() {
		if (mMediaPlayer == null) return -1;
		return mMediaPlayer.getAudioSessionId();
	}

	public int getCurrentPosition() {
		if (mMediaPlayer == null) return -1;
		return mMediaPlayer.getCurrentPosition();
	}

	public int getDuration() {
		if (mMediaPlayer == null) return -1;
		return mMediaPlayer.getDuration();
	}

	public boolean isLooping() {
		if (mMediaPlayer == null) return false;
		return mMediaPlayer.isLooping();
	}

	public boolean isPlaying() {
		if (mMediaPlayer == null) return false;
		return mMediaPlayer.isPlaying();
	}

	public boolean isPrepared() {
		if (mMediaPlayer == null) return false;
		return mPlayerPrepared;
	}

	@Override
	public IBinder onBind(final Intent intent) {
		return mBinder;
	}

	@Override
	public void onBufferingUpdate(final MediaPlayer mp, final int percent) {
		if (mMediaPlayer == null) return;
		final Intent intent = new Intent(BROADCAST_ON_BUFFERING_UPDATE);
		intent.putExtra(INTENT_KEY_PERCENT, percent);
		intent.putExtra(INTENT_KEY_AUDIO_SESSION_ID, getAudioSessionId());
		sendBroadcast(intent);
	}

	@Override
	public void onCompletion(final MediaPlayer mp) {
		if (mMediaPlayer == null) return;
		final Intent intent = new Intent(BROADCAST_ON_COMPLETION);
		intent.putExtra(INTENT_KEY_AUDIO_SESSION_ID, getAudioSessionId());
		intent.putExtra(INTENT_KEY_AUDIO_SESSION_ID, getAudioSessionId());
		sendBroadcast(intent);
	}

	@Override
	public void onCreate() {
		super.onCreate();
		mMediaPlayer = new MediaPlayer();
		mMediaPlayer.setOnBufferingUpdateListener(this);
		mMediaPlayer.setOnCompletionListener(this);
		mMediaPlayer.setOnErrorListener(this);
		mMediaPlayer.setOnInfoListener(this);
		mMediaPlayer.setOnPreparedListener(this);
		mMediaPlayer.setOnSeekCompleteListener(this);
	}

	@Override
	public void onDestroy() {
		release();
		super.onDestroy();
	}

	@Override
	public boolean onError(final MediaPlayer mp, final int what, final int extra) {
		if (mMediaPlayer == null) return false;
		final Intent intent = new Intent(BROADCAST_ON_ERROR);
		intent.putExtra(INTENT_KEY_WHAT, what);
		intent.putExtra(INTENT_KEY_EXTRA, extra);
		intent.putExtra(INTENT_KEY_AUDIO_SESSION_ID, getAudioSessionId());
		sendBroadcast(intent);
		return true;
	}

	@Override
	public boolean onInfo(final MediaPlayer mp, final int what, final int extra) {
		if (mMediaPlayer == null) return false;
		final Intent intent = new Intent(BROADCAST_ON_INFO);
		intent.putExtra(INTENT_KEY_WHAT, what);
		intent.putExtra(INTENT_KEY_EXTRA, extra);
		intent.putExtra(INTENT_KEY_AUDIO_SESSION_ID, getAudioSessionId());
		sendBroadcast(intent);
		return true;
	}

	@Override
	public void onPrepared(final MediaPlayer mp) {
		if (mMediaPlayer == null) return;
		final Intent intent = new Intent(BROADCAST_ON_PREPARED);
		intent.putExtra(INTENT_KEY_AUDIO_SESSION_ID, getAudioSessionId());
		sendBroadcast(intent);
		mPlayerPrepared = true;
	}

	@Override
	public void onSeekComplete(final MediaPlayer mp) {
		if (mMediaPlayer == null) return;
		final Intent intent = new Intent(BROADCAST_ON_SEEK_COMPLETE);
		intent.putExtra(INTENT_KEY_AUDIO_SESSION_ID, getAudioSessionId());
		sendBroadcast(intent);

	}

	public boolean pause() {
		if (mMediaPlayer == null) return false;
		try {
			mMediaPlayer.pause();
			final Intent intent = new Intent(BROADCAST_ON_PAUSE);
			intent.putExtra(INTENT_KEY_AUDIO_SESSION_ID, getAudioSessionId());
			sendBroadcast(intent);
			return true;
		} catch (final IllegalStateException e) {

		}
		return false;
	}

	public boolean prepare() {
		if (mMediaPlayer == null) return false;
		try {
			mMediaPlayer.prepare();
			mPlayerPrepared = true;
			return true;
		} catch (final IllegalStateException e) {
			e.printStackTrace();
		} catch (final IOException e) {
			e.printStackTrace();
		}
		mPlayerPrepared = false;
		return false;
	}

	public boolean prepareAsync() {
		if (mMediaPlayer == null) return false;
		try {
			mMediaPlayer.prepareAsync();
			return true;
		} catch (final IllegalStateException e) {
			e.printStackTrace();
		}
		mPlayerPrepared = false;
		return false;
	}

	public void release() {
		if (mMediaPlayer == null) return;
		mMediaPlayer.release();
		mMediaPlayer = null;
	}

	public void reset() {
		if (mMediaPlayer == null) return;
		mMediaPlayer.reset();
		mPlayerPrepared = false;
	}

	public boolean seekTo(final int msec) {
		if (mMediaPlayer == null) return false;
		try {
			mMediaPlayer.seekTo(msec);
			return true;
		} catch (final IllegalStateException e) {

		}
		return false;
	}

	public boolean setAudioSessionId(final int sessionId) {
		if (mMediaPlayer == null) return false;
		try {
			mMediaPlayer.setAudioSessionId(sessionId);
			return true;
		} catch (final IllegalArgumentException e) {

		} catch (final IllegalStateException e) {

		}
		return false;
	}

	public void setAudioStreamType(final int streamtype) {
		if (mMediaPlayer == null) return;
		mMediaPlayer.setAudioStreamType(streamtype);
	}

	public void setAuxEffectSendLevel(final float level) {
		if (mMediaPlayer == null) return;
		mMediaPlayer.setAuxEffectSendLevel(level);
	}

	public boolean setDataSource(final String path) {
		if (mMediaPlayer == null) return false;
		if (isPlaying()) {
			stop();
		}
		reset();
		try {
			mMediaPlayer.setDataSource(path);
			return true;
		} catch (final IllegalStateException e) {
			e.printStackTrace();
		} catch (final IllegalArgumentException e) {
			e.printStackTrace();
		} catch (final IOException e) {
			e.printStackTrace();
		} catch (final SecurityException e) {
			e.printStackTrace();
		}
		mPlayerPrepared = false;
		return false;
	}

	public void setLooping(final boolean looping) {
		if (mMediaPlayer == null) return;
		mMediaPlayer.setLooping(looping);
	}

	public void setVolume(final float leftVolume, final float rightVolume) {
		if (mMediaPlayer == null) return;
		mMediaPlayer.setVolume(leftVolume, rightVolume);
	}

	public void setWakeMode(final int mode) {
		if (mMediaPlayer == null) return;
		mMediaPlayer.setWakeMode(this, mode);
	}

	public boolean start() {
		if (mMediaPlayer == null) return false;
		try {
			mMediaPlayer.start();
			final Intent intent = new Intent(BROADCAST_ON_START);
			intent.putExtra(INTENT_KEY_AUDIO_SESSION_ID, getAudioSessionId());
			sendBroadcast(intent);
			return true;
		} catch (final IllegalStateException e) {

		}
		return false;
	}

	public boolean stop() {
		if (mMediaPlayer == null) return false;
		try {
			mMediaPlayer.stop();
			final Intent intent = new Intent(BROADCAST_ON_STOP);
			intent.putExtra(INTENT_KEY_AUDIO_SESSION_ID, getAudioSessionId());
			sendBroadcast(intent);
			return true;
		} catch (final IllegalStateException e) {
		}
		return false;
	}

	static final class ServiceStub extends IMediaPlayerService.Stub {

		final WeakReference<MediaPlayerService> mService;

		public ServiceStub(final MediaPlayerService service) {
			mService = new WeakReference<MediaPlayerService>(service);
		}

		@Override
		public void attachAuxEffect(final int effectId) {
			mService.get().attachAuxEffect(effectId);

		}

		@Override
		public int getAudioSessionId() {
			return mService.get().getAudioSessionId();
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
		public boolean isLooping() {
			return mService.get().isLooping();
		}

		@Override
		public boolean isPlaying() {
			return mService.get().isPlaying();
		}

		@Override
		public boolean isPrepared() throws RemoteException {
			return mService.get().isPrepared();
		}

		@Override
		public boolean pause() {
			return mService.get().pause();
		}

		@Override
		public boolean prepare() {
			return mService.get().prepare();
		}

		@Override
		public boolean prepareAsync() {
			return mService.get().prepareAsync();

		}

		@Override
		public void release() {
			mService.get().release();

		}

		@Override
		public void reset() {
			mService.get().reset();

		}

		@Override
		public boolean seekTo(final int msec) {
			return mService.get().seekTo(msec);
		}

		@Override
		public boolean setAudioSessionId(final int sessionId) {
			return mService.get().setAudioSessionId(sessionId);
		}

		@Override
		public void setAudioStreamType(final int streamtype) {
			mService.get().setAudioStreamType(streamtype);

		}

		@Override
		public void setAuxEffectSendLevel(final float level) {
			mService.get().setAuxEffectSendLevel(level);
		}

		@Override
		public boolean setDataSource(final String path) {
			return mService.get().setDataSource(path);

		}

		@Override
		public void setLooping(final boolean looping) {
			mService.get().setLooping(looping);
		}

		@Override
		public void setVolume(final float leftVolume, final float rightVolume) {
			mService.get().setVolume(leftVolume, rightVolume);
		}

		@Override
		public void setWakeMode(final int mode) {
			mService.get().setWakeMode(mode);
		}

		@Override
		public boolean start() {
			return mService.get().start();
		}

		@Override
		public boolean stop() {
			return mService.get().stop();
		}

	}

}
