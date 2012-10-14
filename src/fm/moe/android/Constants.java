package fm.moe.android;

public interface Constants {

	public static final String SHARED_PREFERENCES_NAME = "preferences";

	public static final String MOEFOU_CONSUMER_KEY = "9b6292bc2a31180b35bee283e6a4a4ca050780a2e";
	public static final String MOEFOU_CONSUMER_SECRET = "1dcee27336658c6e381d45c32b55aa57";

	public static final String DEFAULT_OAUTH_CALLBACK = "moefou://oauth.callback";

	public static final String PREFERENCE_KEY_THEME = "theme";
	public static final String PREFERENCE_KEY_ACCESS_TOKEN = "access_token";
	public static final String PREFERENCE_KEY_ACCESS_TOKEN_SECRET = "access_token_secret";
	public static final String PREFERENCE_KEY_LAST_ID = "last_id";

	public static final String PACKAGE_NAME = "fm.moe.android";
	public static final String INTENT_ACTION_PREFIX = PACKAGE_NAME + ".";

	public static final String INTENT_KEY_WHAT = "what";
	public static final String INTENT_KEY_EXTRA = "extra";
	public static final String INTENT_KEY_PERCENT = "percent";
	public static final String INTENT_KEY_AUDIO_SESSION_ID = "audio_session_id";

	public static final String BROADCAST_ON_ERROR = INTENT_ACTION_PREFIX + "ON_ERROR";
	public static final String BROADCAST_ON_PREPARED = INTENT_ACTION_PREFIX + "ON_PREPARED";
	public static final String BROADCAST_ON_INFO = INTENT_ACTION_PREFIX + "ON_INFO";
	public static final String BROADCAST_ON_SEEK_COMPLETE = INTENT_ACTION_PREFIX + "ON_SEEK_COMPLETE";
	public static final String BROADCAST_ON_COMPLETION = INTENT_ACTION_PREFIX + "ON_COMPLETION";
	public static final String BROADCAST_ON_BUFFERING_UPDATE = INTENT_ACTION_PREFIX + "ON_BUFFERING_UPDATE";
	public static final String BROADCAST_ON_PAUSE = INTENT_ACTION_PREFIX + "ON_PAUSE";
	public static final String BROADCAST_ON_START = INTENT_ACTION_PREFIX + "ON_START";
	public static final String BROADCAST_ON_STOP = INTENT_ACTION_PREFIX + "ON_STOP";
	public static final String BROADCAST_ON_CURRENT_ITEM_CHANGED = INTENT_ACTION_PREFIX + "ON_CURRENT_ITEM_CHANGED";
}
