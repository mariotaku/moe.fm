package fm.moe.android.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.SystemClock;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import fm.moe.android.IMoefouService;
import fm.moe.android.R;
import fm.moe.android.model.ParcelablePlaylistItem;
import fm.moe.android.service.MoefouService;
import fm.moe.android.util.LazyImageLoader;
import fm.moe.android.util.MediaPlayerStateListener;
import fm.moe.android.util.ServiceUtils;
import fm.moe.android.util.ThemeUtil;
import fm.moe.android.util.Utils;
import fm.moe.android.view.RepeatingImageButton;
import moefou4j.Moefou;
import moefou4j.MoefouException;
import moefou4j.Playlist;
import moefou4j.PlaylistItem;
import android.util.Log;
import moefou4j.internal.http.HttpResponse;
import fm.moe.android.util.JSONFileHelper;

public class NowPlayingActivity extends BaseActivity implements View.OnClickListener, SharedPreferences.OnSharedPreferenceChangeListener,
ServiceConnection, RepeatingImageButton.OnRepeatListener {

	private SharedPreferences mPreferences;
	private IMoefouService mService;
	private LazyImageLoader mCoverLoader;

	private ParcelablePlaylistItem mCurrentPlaylistItem;
	private ServiceUtils.ServiceToken mToken;

	private ImageView mAlbumCoverView;
	private ImageButton mPlayPauseButton;
	private RepeatingImageButton mNextFwdButton;
	private TextView mTitleView, mArtistView;
	private ProgressBar mProgress;

	private Handler mHandler;
	private Runnable mTicker;
	private boolean mTickerStopped;

	private final BroadcastReceiver mReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(final Context context, final Intent intent) {
			final String action = intent.getAction();
			if (BROADCAST_ON_CURRENT_ITEM_CHANGED.equals(action)) {
				updateNowPlayingInfo();
			}
		}

	};

	private final MediaPlayerStateListener mMediaPlayerStateListener = new MediaPlayerStateListener() {

		@Override
		public void onBufferingUpdate(final int audio_session_id, final int percent) {
			mProgress.setSecondaryProgress(percent);
		}

		@Override
		public void onCompletion(final int audio_session_id) {
			mProgress.setProgress(0);
			mProgress.setSecondaryProgress(0);
		}

		@Override
		public void onError(final int audio_session_id, final int what, final int extra) {

		}

		@Override
		public void onInfo(final int audio_session_id, final int what, final int extra) {

		}

		@Override
		public void onPause(final int audio_session_id) {
			setPlayPauseButton();
		}

		@Override
		public void onPrepared(final int audio_session_id) {

		}

		@Override
		public void onSeekComplete(final int audio_session_id) {

		}

		@Override
		public void onStart(final int audio_session_id) {
			setPlayPauseButton();
		}

		@Override
		public void onStop(final int audio_session_id) {
			setPlayPauseButton();
		}
	};

	private static final int TICKER_DURATION = 1000;

	@Override
	public void onClick(final View v) {
		switch (v.getId()) {
			case R.id.play_pause: {
					togglePlayPause();
					break;
				}
			case R.id.next_fwd: {
					if (mService == null) return;
					try {
						mService.next();
					} catch (RemoteException e) {
						e.printStackTrace();
					}
					break;
				}
		}
	}

	@Override
	public void onContentChanged() {
		super.onContentChanged();
		mPlayPauseButton = (ImageButton) findViewById(R.id.play_pause);
		mNextFwdButton = (RepeatingImageButton) findViewById(R.id.next_fwd);
		mAlbumCoverView = (ImageView) findViewById(R.id.album_cover);
		mTitleView = (TextView) findViewById(R.id.title);
		mArtistView = (TextView) findViewById(R.id.artist);
		mProgress = (ProgressBar) findViewById(R.id.player_progress);
	}

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		setTheme(ThemeUtil.getTheme(this));
		mPreferences = getSharedPreferences(SHARED_PREFERENCES_NAME, MODE_PRIVATE);
		super.onCreate(savedInstanceState);
		final Resources res = getResources();
		mCoverLoader = new LazyImageLoader(this, "album_covers", R.drawable.ic_mp_albumart_unknown,
										   res.getDimensionPixelSize(R.dimen.album_cover_size),
										   res.getDimensionPixelSize(R.dimen.album_cover_size), 3);
		setVolumeControlStream(AudioManager.STREAM_MUSIC);
		if (mPreferences.getString(PREFERENCE_KEY_ACCESS_TOKEN, null) == null
			|| mPreferences.getString(PREFERENCE_KEY_ACCESS_TOKEN_SECRET, null) == null) {
			finish();
			startActivity(new Intent(this, LoginActivity.class));
			return;
		}
		mPreferences.registerOnSharedPreferenceChangeListener(this);
		setContentView(R.layout.now_playing);
		mPlayPauseButton.setOnClickListener(this);
		mNextFwdButton.setOnClickListener(this);
		mNextFwdButton.setRepeatListener(this, 500);
	}

	@Override
	public boolean onCreateOptionsMenu(final Menu menu) {
		getMenuInflater().inflate(R.menu.now_playing, menu);
		return true;
	}

	@Override
	public void onDestroy() {
		try {
			if (mService != null && !mService.isPlaying()) {
				mService.quit();
			}
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		super.onDestroy();
	}
	
	@Override
	public boolean onOptionsItemSelected(final MenuItem item) {
		switch (item.getItemId()) {
			case R.id.theme_default: {
					ThemeUtil.setTheme(this, "default");
					break;
				}
			case R.id.theme_gray: {
					ThemeUtil.setTheme(this, "gray");
					break;
				}
			case R.id.theme_sandy: {
					ThemeUtil.setTheme(this, "sandy");
					break;
				}
			case R.id.theme_woody: {
					ThemeUtil.setTheme(this, "woody");
					break;
				}
			case R.id.theme_graphited: {
					ThemeUtil.setTheme(this, "graphited");
					break;
				}
			case R.id.settings: {
					startActivity(new Intent(this, SettingsActivity.class));
					return true;
				}
			case R.id.logout: {
					new LogoutConfirmDialogFragment().show(getFragmentManager(), "logout_confirm");
					return true;
				}
			case R.id.quit: {
					finish();
					if (mService != null) {
						try {
							mService.quit();
						} catch (RemoteException e) {
							e.printStackTrace();
						}
					}
					return true;
				}
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onPrepareOptionsMenu(final Menu menu) {
		final int theme_res = ThemeUtil.getTheme(this);
		final MenuItem theme_item;
		switch (theme_res) {
			case R.style.Theme_Gray: {
					theme_item = menu.findItem(R.id.theme_gray);
					break;
				}
			case R.style.Theme_Sandy: {
					theme_item = menu.findItem(R.id.theme_sandy);
					break;
				}
			case R.style.Theme_Woody: {
					theme_item = menu.findItem(R.id.theme_woody);
					break;
				}
			case R.style.Theme_Graphited: {
					theme_item = menu.findItem(R.id.theme_graphited);
					break;
				}
			default: {
					theme_item = menu.findItem(R.id.theme_default);
					break;
				}
		}
		theme_item.setChecked(true);
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public void onServiceConnected(final ComponentName name, final IBinder service) {
		mService = IMoefouService.Stub.asInterface(service);
		updateNowPlayingInfo();
		setPlayPauseButton();
	}

	@Override
	public void onServiceDisconnected(final ComponentName name) {
		mService = null;
	}

	@Override
	public void onSharedPreferenceChanged(final SharedPreferences preferences, final String key) {
		if (PREFERENCE_KEY_THEME.equals(key)) {
			restartActivity();
		}
	}

	@Override
	protected void onStart() {
		super.onStart();
		mToken = ServiceUtils.bindService(this, new Intent(this, MoefouService.class), this);
		MediaPlayerStateListener.register(this, mMediaPlayerStateListener);
		final IntentFilter filter = new IntentFilter(BROADCAST_ON_CURRENT_ITEM_CHANGED);
		registerReceiver(mReceiver, filter);
		mTickerStopped = false;
		mHandler = new Handler();
		mTicker = new Runnable() {

			@Override
			public void run() {
				if (mTickerStopped) return;
				if (mProgress != null && mService != null) {
					try {
						if (mService.isPlaying()) {
							final int duration = mService.getDuration();
							final int position = mService.getCurrentPosition();
							if (duration > 0) {
								mProgress.setProgress(100 * position / duration);
							}
						}
					} catch (final RemoteException e) {
						e.printStackTrace();
					}
				}
				final long now = SystemClock.uptimeMillis();
				final long next = now + TICKER_DURATION - now % TICKER_DURATION;
				mHandler.postAtTime(mTicker, next);
			}
		};
		mTicker.run();
	}

	@Override
	protected void onStop() {
		mTickerStopped = true;
		ServiceUtils.unbindService(mToken);
		unregisterReceiver(mReceiver);
		unregisterReceiver(mMediaPlayerStateListener);
		final SharedPreferences.Editor editor = mPreferences.edit();
		editor.apply();
		super.onStop();
	}

	private void setPlayPauseButton() {
		if (mService == null) return;
		try {
			if (!mService.isPrepared()) return;
			mPlayPauseButton.setImageResource(mService.isPlaying() ? R.drawable.btn_pause : R.drawable.btn_play);
		} catch (final RemoteException e) {
			e.printStackTrace();
		}
	}

	private void togglePlayPause() {
		if (mService == null) return;
		try {
			if (!mService.isPrepared()) {
				mService.play(mService.getQueuePosition());
			} else {
				if (mService.isPlaying()) {
					mService.pause();
				} else {
					mService.start();
				}
			}
		} catch (final RemoteException e) {
		}
	}

	private void updateNowPlayingInfo() {
		if (mService == null) return;
		try {
			final ParcelablePlaylistItem item = mCurrentPlaylistItem = mService.getCurrentItem();
			mProgress.setProgress(0);
			mProgress.setSecondaryProgress(0);
			if (item == null) {
				mAlbumCoverView.setImageResource(R.drawable.ic_mp_albumart_unknown);
				mTitleView.setText(null);
				mArtistView.setText(null);
				return;
			}
			mCoverLoader.displayImage(item.getCoverUrl(), mAlbumCoverView);
			mTitleView.setText(item.getTitle());
			mArtistView.setText(item.getArtist());
		} catch (final RemoteException e) {
			e.printStackTrace();
		}
	}

	public static class LogoutConfirmDialogFragment extends DialogFragment implements DialogInterface.OnClickListener {

		@Override
		public void onClick(final DialogInterface dialog, final int which) {
			switch (which) {
				case DialogInterface.BUTTON_POSITIVE: {
						final SharedPreferences.Editor editor = getActivity().getSharedPreferences(SHARED_PREFERENCES_NAME,
																								   MODE_PRIVATE).edit();
						editor.putString(PREFERENCE_KEY_ACCESS_TOKEN, null);
						editor.putString(PREFERENCE_KEY_ACCESS_TOKEN_SECRET, null);
						editor.apply();
						getActivity().finish();
						break;
					}
			}

		}

		@Override
		public Dialog onCreateDialog(final Bundle savedInstanceState) {
			final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			builder.setMessage(R.string.logout_confirm);
			builder.setPositiveButton(android.R.string.ok, this);
			builder.setNegativeButton(android.R.string.cancel, null);
			return builder.create();
		}

	}

	@Override
	public void onRepeat(View v, long duration, int repeatcount) {

	}
}
