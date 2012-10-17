/*
 * Copyright 2007 Yusuke Yamamoto
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package moefou4j;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import moefou4j.auth.AccessToken;
import moefou4j.auth.Authorization;
import moefou4j.auth.OAuthAuthorization;
import moefou4j.auth.OAuthSupport;
import moefou4j.auth.RequestToken;
import moefou4j.conf.Configuration;
import moefou4j.http.HttpClientWrapper;
import moefou4j.http.HttpResponseEvent;
import moefou4j.http.HttpResponseListener;
import moefou4j.internal.json.Moefou4JInternalFactory;
import moefou4j.internal.json.Moefou4JInternalJSONFactoryImpl;

/**
 * Base class of Moefou supports OAuth.
 * 
 * @author Yusuke Yamamoto - yusuke at mac.com
 */
abstract class MoefouBaseImpl implements MoefouBase, OAuthSupport, HttpResponseListener {
	protected Configuration conf;

	protected transient HttpClientWrapper http;

	protected Moefou4JInternalFactory factory;

	protected Authorization auth;

	/* package */MoefouBaseImpl(final Configuration conf, final Authorization auth) {
		this.conf = conf;
		this.auth = auth;
		init();
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o) return true;
		if (!(o instanceof MoefouBaseImpl)) return false;

		final MoefouBaseImpl that = (MoefouBaseImpl) o;

		if (auth != null ? !auth.equals(that.auth) : that.auth != null) return false;
		if (!conf.equals(that.conf)) return false;
		if (http != null ? !http.equals(that.http) : that.http != null) return false;

		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final Authorization getAuthorization() {
		return auth;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Configuration getConfiguration() {
		return conf;
	}

	@Override
	public synchronized AccessToken getOAuthAccessToken() throws MoefouException {
		return getOAuth().getOAuthAccessToken();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @throws IllegalStateException when AccessToken has already been retrieved
	 *             or set
	 */
	@Override
	public synchronized AccessToken getOAuthAccessToken(final RequestToken requestToken) throws MoefouException {
		return getOAuth().getOAuthAccessToken(requestToken);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @throws IllegalStateException when AccessToken has already been retrieved
	 *             or set
	 */
	@Override
	public synchronized AccessToken getOAuthAccessToken(final RequestToken requestToken, final String oauthVerifier)
			throws MoefouException {
		return getOAuth().getOAuthAccessToken(requestToken, oauthVerifier);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @throws IllegalStateException when AccessToken has already been retrieved
	 *             or set
	 */
	@Override
	public synchronized AccessToken getOAuthAccessToken(final String oauthVerifier) throws MoefouException {
		final AccessToken oauthAccessToken = getOAuth().getOAuthAccessToken(oauthVerifier);
		return oauthAccessToken;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public RequestToken getOAuthRequestToken() throws MoefouException {
		return getOAuthRequestToken(null);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public RequestToken getOAuthRequestToken(final String callbackUrl) throws MoefouException {
		return getOAuth().getOAuthRequestToken(callbackUrl);
	}

	@Override
	public void httpResponseReceived(final HttpResponseEvent event) {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized void setOAuthAccessToken(final AccessToken accessToken) {
		getOAuth().setOAuthAccessToken(accessToken);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized void setOAuthConsumer(final String consumerKey, final String consumerSecret) {
		if (null == consumerKey) throw new NullPointerException("consumer key is null");
		if (null == consumerSecret) throw new NullPointerException("consumer secret is null");
		if (auth == null) {
			final OAuthAuthorization oauth = new OAuthAuthorization(conf);
			oauth.setOAuthConsumer(consumerKey, consumerSecret);
			auth = oauth;
		} else if (auth instanceof OAuthAuthorization)
			throw new IllegalStateException("consumer key/secret pair already set.");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void shutdown() {
		if (http != null) {
			http.shutdown();
		}
	}

	protected final void ensureAuthorizationEnabled() {
		if (!auth.isEnabled()) throw new IllegalStateException("Authentication credentials are missing.");
	}

	protected final void ensureOAuthEnabled() {
		if (!(auth instanceof OAuthAuthorization))
			throw new IllegalStateException("OAuth required. Authentication credentials are missing.");
	}

	protected void setFactory() {
		factory = new Moefou4JInternalJSONFactoryImpl(conf);
	}

	private OAuthSupport getOAuth() {
		if (!(auth instanceof OAuthSupport)) {
			init();
			if (!(auth instanceof OAuthSupport))
				throw new IllegalStateException("OAuth consumer key/secret combination not supplied");
		}
		return (OAuthSupport) auth;
	}

	private void init() {
		if (auth == null) {
			auth = new OAuthAuthorization(conf);
		}
		http = new HttpClientWrapper(conf);
		http.setHttpResponseListener(this);
		setFactory();
	}

	private void readObject(final ObjectInputStream stream) throws IOException, ClassNotFoundException {
		conf = (Configuration) stream.readObject();
		auth = (Authorization) stream.readObject();
		http = new HttpClientWrapper(conf);
		http.setHttpResponseListener(this);
		setFactory();
	}

	private void writeObject(final ObjectOutputStream out) throws IOException {
		out.writeObject(conf);
		out.writeObject(auth);
	}
}
