package fm.moe.android.activity;

import moefou4j.FavoriteResponse;
import moefou4j.Moefou;
import moefou4j.MoefouException;
import moefou4j.Sub;
import moefou4j.api.SubMethods;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.media.AudioManager;
import android.media.audiofx.AudioEffect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.SystemClock;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
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

public class NowPlayingActivity extends BaseActivity implements View.OnClickListener,
		SharedPreferences.OnSharedPreferenceChangeListener, ServiceConnection, RepeatingImageButton.OnRepeatListener {

	private SharedPreferences mPreferences;
	private IMoefouService mService;
	private LazyImageLoader mCoverLoader;

	private ServiceUtils.ServiceToken mToken;

	private ImageView mAlbumCoverView;
	private ImageButton mPlayPauseButton, mFavoriteButton, mTrashButton;
	private RepeatingImageButton mNextFwdButton;
	private TextView mTitleView, mArtistView;
	private ProgressBar mProgress;

	private Handler mHandler;
	private Runnable mTicker;
	private boolean mTickerStopped;

	private Moefou mMoefou;

	private final BroadcastReceiver mReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(final Context context, final Intent intent) {
			final String action = intent.getAction();
			if (BROADCAST_ON_CURRENT_ITEM_CHANGE.equals(action)) {
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
			mPlayPauseButton.setImageResource(R.drawable.btn_play);
			updateNowPlayingInfo();
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
			setPlayPauseButton();
			updateNowPlayingInfo();
			updatePrepareState();
		}

		@Override
		public void onPrepareStateChange(final int audio_session_id) {
			updatePrepareState();

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
			mPlayPauseButton.setImageResource(R.drawable.btn_play);
		}
	};

	private static final int TICKER_DURATION = 1000;

	@Override
	public void onClick(final View v) {
		if (mService == null) return;
		switch (v.getId()) {
			case R.id.play_pause: {
				togglePlayPause();
				break;
			}
			case R.id.next_fwd: {
				try {
					mService.next();
				} catch (final RemoteException e) {
					e.printStackTrace();
				}
				break;
			}
			case R.id.favorite: {
				try {
					new ToggleFavoriteTask(mMoefou, mService, mService.getCurrentItem()).execute();
				} catch (final RemoteException e) {
					e.printStackTrace();
				}
				break;
			}
			case R.id.trash: {
				try {
					new IgnoreTrackTask(mMoefou, mService, mService.getCurrentItem()).execute();
				} catch (final RemoteException e) {
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
		mFavoriteButton = (ImageButton) findViewById(R.id.favorite);
		mTrashButton = (ImageButton) findViewById(R.id.trash);
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
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		super.onCreate(savedInstanceState);
		final Resources res = getResources();
		mCoverLoader = new LazyImageLoader(this, "album_covers", R.drawable.ic_mp_albumart_unknown,
				res.getDimensionPixelSize(R.dimen.album_cover_size),
				res.getDimensionPixelSize(R.dimen.album_cover_size), 3);
		mMoefou = Utils.getMoefouInstance(this);
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
		mFavoriteButton.setOnClickListener(this);
		mTrashButton.setOnClickListener(this);
		mNextFwdButton.setRepeatListener(this, 500);
	}

	@Override
	public boolean onCreateOptionsMenu(final Menu menu) {
		getMenuInflater().inflate(R.menu.now_playing, menu);
		return true;
	}

	@Override
	public void onDestroy() {
		if (mService != null) {
			try {
				if (!mService.isPlaying() && !mService.isPreparing()) {
					mService.quit();
				}
			} catch (final RemoteException e) {
			}
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
			case R.id.equalizer: {
				try {
					final Intent intent = new Intent(AudioEffect.ACTION_DISPLAY_AUDIO_EFFECT_CONTROL_PANEL);
					intent.putExtra(AudioEffect.EXTRA_AUDIO_SESSION, mService.getAudioSessionId());
					startActivityForResult(intent, 0);
				} catch (final RemoteException e) {
					return false;
				} catch (final ActivityNotFoundException e) {
					return false;
				}
				return true;
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
					} catch (final RemoteException e) {
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
		final MenuItem item_equalizer = menu.findItem(R.id.equalizer);
		final MenuItem item_theme;
		switch (theme_res) {
			case R.style.Theme_Gray: {
				item_theme = menu.findItem(R.id.theme_gray);
				break;
			}
			case R.style.Theme_Sandy: {
				item_theme = menu.findItem(R.id.theme_sandy);
				break;
			}
			case R.style.Theme_Woody: {
				item_theme = menu.findItem(R.id.theme_woody);
				break;
			}
			case R.style.Theme_Graphited: {
				item_theme = menu.findItem(R.id.theme_graphited);
				break;
			}
			default: {
				item_theme = menu.findItem(R.id.theme_default);
				break;
			}
		}
		item_theme.setChecked(true);
		final Intent equalizer_intent = new Intent(AudioEffect.ACTION_DISPLAY_AUDIO_EFFECT_CONTROL_PANEL);
		final ResolveInfo info = getPackageManager().resolveActivity(equalizer_intent, 0);
		item_equalizer.setVisible(info != null);
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public void onRepeat(final View v, final long duration, final int repeatcount) {

	}

	@Override
	public void onServiceConnected(final ComponentName name, final IBinder service) {
		mService = IMoefouService.Stub.asInterface(service);
		updateNowPlayingInfo();
		setPlayPauseButton();
		updatePrepareState();
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
		final IntentFilter filter = new IntentFilter(BROADCAST_ON_CURRENT_ITEM_CHANGE);
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
			if (mService.isPreparing()) return;
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
			final ParcelablePlaylistItem item = mService.getCurrentItem();
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
			mFavoriteButton.setImageResource(item.isFavorite() ? R.drawable.btn_heart_activated : R.drawable.btn_heart);
		} catch (final RemoteException e) {
			e.printStackTrace();
		}
	}

	void updatePrepareState() {
		try {
			setProgressBarIndeterminateVisibility(mService.isPreparing());
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

	static class IgnoreTrackTask extends AsyncTask<Void, Void, FavoriteResponse<Sub>> {

		private final ParcelablePlaylistItem item;
		private final SubMethods moefou;
		private final IMoefouService service;

		IgnoreTrackTask(final Moefou moefou, final IMoefouService service, final ParcelablePlaylistItem item) {
			this.item = item;
			this.moefou = moefou;
			this.service = service;
		}

		@Override
		protected FavoriteResponse<Sub> doInBackground(final Void... args) {
			try {
				return moefou.addSubFavorite(Sub.Type.fromString(item.getType()), item.getSubId(), 2);
			} catch (final MoefouException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(final FavoriteResponse<Sub> result) {
			if (result != null && service != null) {
				try {
					service.remove(item);
				} catch (final RemoteException e) {
					e.printStackTrace();
				}
			}
		}
	}

	static class ToggleFavoriteTask extends AsyncTask<Void, Void, FavoriteResponse<Sub>> {

		private final ParcelablePlaylistItem item;
		private final SubMethods moefou;
		private final IMoefouService service;

		ToggleFavoriteTask(final Moefou moefou, final IMoefouService service, final ParcelablePlaylistItem item) {
			this.item = item;
			this.moefou = moefou;
			this.service = service;
		}

		@Override
		protected FavoriteResponse<Sub> doInBackground(final Void... args) {
			try {
				if (!item.isFavorite())
					return moefou.addSubFavorite(Sub.Type.fromString(item.getType()), item.getSubId(), 1);
				else
					return moefou.deleteSubFavorite(Sub.Type.fromString(item.getType()), item.getSubId());
			} catch (final MoefouException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(final FavoriteResponse<Sub> result) {
			if (result != null && service != null) {
				try {
					service.update(new ParcelablePlaylistItem(item, !item.isFavorite()));
				} catch (final RemoteException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
