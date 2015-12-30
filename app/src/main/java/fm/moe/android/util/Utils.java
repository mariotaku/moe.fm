package fm.moe.android.util;

import moefou4j.Moefou;
import moefou4j.MoefouFactory;
import moefou4j.auth.AccessToken;
import moefou4j.conf.ConfigurationBuilder;
import android.content.Context;
import android.content.SharedPreferences;
import fm.moe.android.Constants;

public final class Utils implements Constants {

	private Utils() {
		throw new AssertionError();
	}

	public static Moefou getMoefouInstance(final Context context) {
		if (context == null) return null;
		final SharedPreferences prefs = context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
		final String access_token = prefs.getString(PREFERENCE_KEY_ACCESS_TOKEN, null);
		final String access_token_secret = prefs.getString(PREFERENCE_KEY_ACCESS_TOKEN_SECRET, null);
		if (access_token == null || access_token_secret == null) return null;
		final ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setOAuthConsumerKey(MOEFOU_CONSUMER_KEY);
		cb.setOAuthConsumerSecret(MOEFOU_CONSUMER_SECRET);
		return new MoefouFactory(cb.build()).getInstance(new AccessToken(access_token, access_token_secret));
	}
}
