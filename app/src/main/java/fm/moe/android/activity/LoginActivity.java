/*
 *				Twidere - Twitter client for Android
 * 
 * Copyright (C) 2012 Mariotaku Lee <mariotaku.lee@gmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package fm.moe.android.activity;

import moefou4j.Moefou;
import moefou4j.MoefouException;
import moefou4j.MoefouFactory;
import moefou4j.auth.AccessToken;
import moefou4j.auth.RequestToken;
import moefou4j.conf.ConfigurationBuilder;
import android.annotation.SuppressLint;
import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;
import fm.moe.android.R;

@SuppressLint("SetJavaScriptEnabled")
public class LoginActivity extends BaseActivity implements LoaderCallbacks<RequestToken> {

	private SharedPreferences mPreferences;

	private WebView mWebView;
	private WebSettings mWebSettings;

	private boolean mLoaderInitialized;

	private String mAuthUrl;
	private RequestToken mRequestToken;

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		super.onCreate(savedInstanceState);
		setProgressBarIndeterminateVisibility(false);
		mPreferences = getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
		setContentView(mWebView = new WebView(this));
		mWebView.setWebViewClient(new AuthorizationWebViewClient());
		mWebView.setVerticalScrollBarEnabled(false);
		mWebSettings = mWebView.getSettings();
		mWebSettings.setLoadsImagesAutomatically(true);
		mWebSettings.setJavaScriptEnabled(true);
		mWebSettings.setBlockNetworkImage(false);
		mWebSettings.setSaveFormData(true);
		mWebSettings.setSavePassword(true);
		getRequestToken();
	}

	@Override
	public Loader<RequestToken> onCreateLoader(final int id, final Bundle args) {
		setProgressBarIndeterminateVisibility(true);
		return new RequestTokenLoader(this);
	}

	@Override
	public void onDestroy() {
		getLoaderManager().destroyLoader(0);
		super.onDestroy();
	}

	@Override
	public void onLoaderReset(final Loader<RequestToken> loader) {

	}

	@Override
	public void onLoadFinished(final Loader<RequestToken> loader, final RequestToken data) {
		setProgressBarIndeterminateVisibility(false);
		mRequestToken = data;
		if (data == null) {
			Toast.makeText(this, R.string.error_occurred, Toast.LENGTH_SHORT).show();
			finish();
		}
		mWebView.loadUrl(mAuthUrl = data.getAuthorizationURL());
	}

	@Override
	public boolean onOptionsItemSelected(final MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home: {
				finish();
				return true;
			}
		}
		return super.onOptionsItemSelected(item);
	}

	private void getRequestToken() {
		final LoaderManager lm = getLoaderManager();
		lm.destroyLoader(0);
		if (mLoaderInitialized) {
			lm.restartLoader(0, null, this);
		} else {
			lm.initLoader(0, null, this);
			mLoaderInitialized = true;
		}
	}

	class AuthorizationWebViewClient extends WebViewClient {

		@Override
		public void onPageFinished(final WebView view, final String url) {
			super.onPageFinished(view, url);
			setProgressBarIndeterminateVisibility(false);
		}

		@Override
		public void onPageStarted(final WebView view, final String url, final Bitmap favicon) {
			super.onPageStarted(view, url, favicon);
			setProgressBarIndeterminateVisibility(true);
		}

		@Override
		public void onReceivedError(final WebView view, final int errorCode, final String description,
				final String failingUrl) {
			super.onReceivedError(view, errorCode, description, failingUrl);
			Toast.makeText(LoginActivity.this, R.string.error_occurred, Toast.LENGTH_SHORT).show();
			finish();
		}

		@Override
		public boolean shouldOverrideUrlLoading(final WebView view, final String url) {
			if (mAuthUrl == null) return true;
			final Uri uri = Uri.parse(url);
			if (uri.getHost().equals(Uri.parse(mAuthUrl).getHost()))
				return false;
			else if (url.startsWith(DEFAULT_OAUTH_CALLBACK)) {
				final String oAuth_verifier = uri.getQueryParameter("oauth_verifier");
				if (oAuth_verifier != null && mRequestToken != null) {
					new GetAccessTokenTask(mRequestToken, oAuth_verifier).execute();
				}
				return true;
			}
			final Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
			startActivity(intent);
			finish();
			return true;
		}
	}

	class GetAccessTokenTask extends AsyncTask<Void, Void, AccessToken> {

		private final RequestToken requestToken;
		private final String oauthVerifier;

		public GetAccessTokenTask(final RequestToken requestToken, final String oauthVerifier) {
			this.requestToken = requestToken;
			this.oauthVerifier = oauthVerifier;
		}

		@Override
		protected AccessToken doInBackground(final Void... args) {
			final ConfigurationBuilder cb = new ConfigurationBuilder();
			cb.setOAuthConsumerKey(MOEFOU_CONSUMER_KEY);
			cb.setOAuthConsumerSecret(MOEFOU_CONSUMER_SECRET);
			cb.setGZIPEnabled(true);
			try {
				final Moefou moefou = new MoefouFactory(cb.build()).getInstance();
				return moefou.getOAuthAccessToken(requestToken, oauthVerifier);
			} catch (final MoefouException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(final AccessToken result) {
			super.onPostExecute(result);
			setProgressBarIndeterminateVisibility(false);
			if (result != null) {
				final SharedPreferences.Editor editor = mPreferences.edit();
				editor.putString(PREFERENCE_KEY_ACCESS_TOKEN, result.getToken());
				editor.putString(PREFERENCE_KEY_ACCESS_TOKEN_SECRET, result.getTokenSecret());
				editor.apply();
				startActivity(new Intent(LoginActivity.this, NowPlayingActivity.class));
			} else {
				Toast.makeText(LoginActivity.this, R.string.error_occurred, Toast.LENGTH_SHORT).show();
			}
			finish();
		}

		@Override
		protected void onPreExecute() {
			setProgressBarIndeterminateVisibility(true);
			super.onPreExecute();
		}

	}

	static class RequestTokenLoader extends AsyncTaskLoader<RequestToken> {

		public RequestTokenLoader(final Context context) {
			super(context);
		}

		@Override
		public RequestToken loadInBackground() {
			final ConfigurationBuilder cb = new ConfigurationBuilder();
			cb.setOAuthConsumerKey(MOEFOU_CONSUMER_KEY);
			cb.setOAuthConsumerSecret(MOEFOU_CONSUMER_SECRET);
			cb.setGZIPEnabled(true);
			try {
				final Moefou moefou = new MoefouFactory(cb.build()).getInstance();
				return moefou.getOAuthRequestToken(DEFAULT_OAUTH_CALLBACK);
			} catch (final MoefouException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onStartLoading() {
			forceLoad();
		}

	}
}
