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

package moefou4j.auth;

import moefou4j.MoefouException;
import moefou4j.conf.Configuration;
import moefou4j.conf.ConfigurationContext;
import moefou4j.internal.http.HttpResponse;

/**
 * @author Yusuke Yamamoto - yusuke at mac.com representing unauthorized Request
 *         Token which is passed to the service provider when acquiring the
 *         authorized Access Token
 */
public final class RequestToken extends OAuthToken {
	private final Configuration conf;

	public RequestToken(final Configuration conf, final String token, final String tokenSecret) {
		super(token, tokenSecret);
		this.conf = conf;
	}

	@Deprecated
	public RequestToken(final String token, final String tokenSecret) {
		super(token, tokenSecret);
		conf = ConfigurationContext.getInstance();
	}

	RequestToken(final Configuration conf, final HttpResponse res, final OAuthSupport oauth) throws MoefouException {
		super(res);
		this.conf = conf;
	}

	RequestToken(final Configuration conf, final String token, final String tokenSecret, final OAuthSupport oauth) {
		super(token, tokenSecret);
		this.conf = conf;
	}

	@Deprecated
	RequestToken(final HttpResponse res, final OAuthSupport oauth) throws MoefouException {
		super(res);
		conf = ConfigurationContext.getInstance();
	}

	@Deprecated
	RequestToken(final String token, final String tokenSecret, final OAuthSupport oauth) {
		super(token, tokenSecret);
		conf = ConfigurationContext.getInstance();
	}

	/**
	 * @return authentication URL since Twitter4J 2.0.10
	 */
	public String getAuthenticationURL() {
		return conf.getOAuthAuthenticationURL() + "?oauth_token=" + getToken();
	}

	/**
	 * @return authorization URL since Twitter4J 2.0.0
	 */
	public String getAuthorizationURL() {
		return conf.getOAuthAuthorizationURL() + "?oauth_token=" + getToken();
	}

}
