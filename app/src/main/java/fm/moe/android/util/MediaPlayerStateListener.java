package fm.moe.android.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import fm.moe.android.Constants;

public abstract class MediaPlayerStateListener extends BroadcastReceiver implements Constants {

	public abstract void onBufferingUpdate(final int audio_session_id, final int percent);

	public abstract void onCompletion(final int audio_session_id);

	public abstract void onError(final int audio_session_id, final int what, final int extra);

	public abstract void onInfo(final int audio_session_id, final int what, final int extra);

	public abstract void onPause(final int audio_session_id);

	public abstract void onPrepared(final int audio_session_id);

	public abstract void onPrepareStateChange(final int audio_session_id);

	@Override
	public final void onReceive(final Context context, final Intent intent) {
		final String action = intent.getAction();
		final int audio_session_id = intent.getIntExtra(INTENT_KEY_AUDIO_SESSION_ID, 0);
		if (BROADCAST_ON_BUFFERING_UPDATE.equals(action)) {
			onBufferingUpdate(audio_session_id, intent.getIntExtra(INTENT_KEY_PERCENT, 0));
		} else if (BROADCAST_ON_COMPLETION.equals(action)) {
			onCompletion(audio_session_id);
		} else if (BROADCAST_ON_ERROR.equals(action)) {
			onError(audio_session_id, intent.getIntExtra(INTENT_KEY_WHAT, 0), intent.getIntExtra(INTENT_KEY_EXTRA, 0));
		} else if (BROADCAST_ON_INFO.equals(action)) {
			onInfo(audio_session_id, intent.getIntExtra(INTENT_KEY_WHAT, 0), intent.getIntExtra(INTENT_KEY_EXTRA, 0));
		} else if (BROADCAST_ON_PREPARED.equals(action)) {
			onPrepared(audio_session_id);
		} else if (BROADCAST_ON_SEEK_COMPLETE.equals(action)) {
			onSeekComplete(audio_session_id);
		} else if (BROADCAST_ON_START.equals(action)) {
			onStart(audio_session_id);
		} else if (BROADCAST_ON_STOP.equals(action)) {
			onStop(audio_session_id);
		} else if (BROADCAST_ON_PAUSE.equals(action)) {
			onPause(audio_session_id);
		} else if (BROADCAST_ON_PREPARE_STATE_CHANGE.equals(action)) {
			onPrepareStateChange(audio_session_id);
		}
	}

	public abstract void onSeekComplete(final int audio_session_id);

	public abstract void onStart(final int audio_session_id);

	public abstract void onStop(final int audio_session_id);

	public static void register(final Context context, final MediaPlayerStateListener listener) {
		if (context == null || listener == null) return;
		final IntentFilter filter = new IntentFilter();
		filter.addAction(BROADCAST_ON_COMPLETION);
		filter.addAction(BROADCAST_ON_BUFFERING_UPDATE);
		filter.addAction(BROADCAST_ON_ERROR);
		filter.addAction(BROADCAST_ON_INFO);
		filter.addAction(BROADCAST_ON_PREPARED);
		filter.addAction(BROADCAST_ON_SEEK_COMPLETE);
		filter.addAction(BROADCAST_ON_START);
		filter.addAction(BROADCAST_ON_STOP);
		filter.addAction(BROADCAST_ON_PAUSE);
		filter.addAction(BROADCAST_ON_PREPARE_STATE_CHANGE);
		context.registerReceiver(listener, filter);
	}
}
