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

package moefou4j.conf;

import java.io.ObjectStreamException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import moefou4j.MoefouConstants;
import moefou4j.Version;

/**
 * Configuration base class with default settings.
 * 
 * @author Yusuke Yamamoto - yusuke at mac.com
 */
class ConfigurationBase implements MoefouConstants, Configuration {

	static final boolean DEFAULT_USE_SSL = true;

	private boolean debug;
	private String userAgent;
	private boolean useSSL;
	private boolean ignoreSSLError;
	private boolean prettyDebug;
	private boolean gzipEnabled;
	private String httpProxyHost;
	private String httpProxyUser;
	private String httpProxyPassword;
	private int httpProxyPort;
	private int httpConnectionTimeout;
	private int httpReadTimeout;

	private int httpRetryCount;
	private int httpRetryIntervalSeconds;
	private int maxTotalConnections;
	private int defaultMaxPerRoute;
	private String oAuthConsumerKey;
	private String oAuthConsumerSecret;

	private String oAuthRequestTokenURL;
	private String oAuthAuthorizationURL;
	private String oAuthAccessTokenURL;
	private String oAuthAuthenticationURL;

	private String oAuthBaseURL;
	private String moefouBaseURL;
	private String moeFMBaseURL;

	private boolean includeRTsEnabled;

	private boolean includeEntitiesEnabled;

	// hidden portion
	private String clientVersion;
	private String clientURL;
	private String clientName;

	// method for HttpRequestFactoryConfiguration
	Map<String, String> requestHeaders;

	private static final List<ConfigurationBase> instances = new ArrayList<ConfigurationBase>();

	protected ConfigurationBase() {
		setDebug(false);
		setUseSSL(false);
		setPrettyDebugEnabled(false);
		setGZIPEnabled(true);
		setHttpProxyHost(null);
		setHttpProxyUser(null);
		setHttpProxyPassword(null);
		setHttpProxyPort(-1);
		setHttpConnectionTimeout(20000);
		setHttpReadTimeout(120000);
		setHttpRetryCount(0);
		setHttpRetryIntervalSeconds(5);
		setHttpMaxTotalConnections(20);
		setHttpDefaultMaxPerRoute(2);
		setOAuthConsumerKey(null);
		setOAuthConsumerSecret(null);
		setClientName("Twitter4J");
		setClientVersion(Version.getVersion());
		setClientURL("http://moefou4j.org/en/moefou4j-" + Version.getVersion() + ".xml");
		setUserAgent("moefou4j http://moefou4j.org/ /" + Version.getVersion());

		setIncludeRTsEnbled(true);

		setIncludeEntitiesEnbled(true);

		setOAuthBaseURL(DEFAULT_OAUTH_BASE_URL);
		setMoefouBaseURL(DEFAULT_MOEFOU_BASE_URL);
		setMoeFMBaseURL(DEFAULT_MOEFM_BASE_URL);
		setIncludeRTsEnbled(true);
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (!(obj instanceof ConfigurationBase)) return false;
		final ConfigurationBase other = (ConfigurationBase) obj;
		if (clientName == null) {
			if (other.clientName != null) return false;
		} else if (!clientName.equals(other.clientName)) return false;
		if (clientURL == null) {
			if (other.clientURL != null) return false;
		} else if (!clientURL.equals(other.clientURL)) return false;
		if (clientVersion == null) {
			if (other.clientVersion != null) return false;
		} else if (!clientVersion.equals(other.clientVersion)) return false;
		if (debug != other.debug) return false;
		if (defaultMaxPerRoute != other.defaultMaxPerRoute) return false;
		if (gzipEnabled != other.gzipEnabled) return false;
		if (httpConnectionTimeout != other.httpConnectionTimeout) return false;
		if (httpProxyHost == null) {
			if (other.httpProxyHost != null) return false;
		} else if (!httpProxyHost.equals(other.httpProxyHost)) return false;
		if (httpProxyPassword == null) {
			if (other.httpProxyPassword != null) return false;
		} else if (!httpProxyPassword.equals(other.httpProxyPassword)) return false;
		if (httpProxyPort != other.httpProxyPort) return false;
		if (httpProxyUser == null) {
			if (other.httpProxyUser != null) return false;
		} else if (!httpProxyUser.equals(other.httpProxyUser)) return false;
		if (httpReadTimeout != other.httpReadTimeout) return false;
		if (httpRetryCount != other.httpRetryCount) return false;
		if (httpRetryIntervalSeconds != other.httpRetryIntervalSeconds) return false;
		if (ignoreSSLError != other.ignoreSSLError) return false;
		if (includeEntitiesEnabled != other.includeEntitiesEnabled) return false;
		if (includeRTsEnabled != other.includeRTsEnabled) return false;
		if (maxTotalConnections != other.maxTotalConnections) return false;
		if (moeFMBaseURL == null) {
			if (other.moeFMBaseURL != null) return false;
		} else if (!moeFMBaseURL.equals(other.moeFMBaseURL)) return false;
		if (moefouBaseURL == null) {
			if (other.moefouBaseURL != null) return false;
		} else if (!moefouBaseURL.equals(other.moefouBaseURL)) return false;
		if (oAuthAccessTokenURL == null) {
			if (other.oAuthAccessTokenURL != null) return false;
		} else if (!oAuthAccessTokenURL.equals(other.oAuthAccessTokenURL)) return false;
		if (oAuthAuthenticationURL == null) {
			if (other.oAuthAuthenticationURL != null) return false;
		} else if (!oAuthAuthenticationURL.equals(other.oAuthAuthenticationURL)) return false;
		if (oAuthAuthorizationURL == null) {
			if (other.oAuthAuthorizationURL != null) return false;
		} else if (!oAuthAuthorizationURL.equals(other.oAuthAuthorizationURL)) return false;
		if (oAuthBaseURL == null) {
			if (other.oAuthBaseURL != null) return false;
		} else if (!oAuthBaseURL.equals(other.oAuthBaseURL)) return false;
		if (oAuthConsumerKey == null) {
			if (other.oAuthConsumerKey != null) return false;
		} else if (!oAuthConsumerKey.equals(other.oAuthConsumerKey)) return false;
		if (oAuthConsumerSecret == null) {
			if (other.oAuthConsumerSecret != null) return false;
		} else if (!oAuthConsumerSecret.equals(other.oAuthConsumerSecret)) return false;
		if (oAuthRequestTokenURL == null) {
			if (other.oAuthRequestTokenURL != null) return false;
		} else if (!oAuthRequestTokenURL.equals(other.oAuthRequestTokenURL)) return false;
		if (prettyDebug != other.prettyDebug) return false;
		if (requestHeaders == null) {
			if (other.requestHeaders != null) return false;
		} else if (!requestHeaders.equals(other.requestHeaders)) return false;
		if (useSSL != other.useSSL) return false;
		if (userAgent == null) {
			if (other.userAgent != null) return false;
		} else if (!userAgent.equals(other.userAgent)) return false;
		return true;
	}

	@Override
	public final String getClientName() {
		return clientName;
	}

	@Override
	public final String getClientURL() {
		return clientURL;
	}

	@Override
	public final String getClientVersion() {
		return clientVersion;
	}

	@Override
	public final int getHttpConnectionTimeout() {
		return httpConnectionTimeout;
	}

	@Override
	public final int getHttpDefaultMaxPerRoute() {
		return defaultMaxPerRoute;
	}

	@Override
	public final int getHttpMaxTotalConnections() {
		return maxTotalConnections;
	}

	@Override
	public final String getHttpProxyHost() {
		return httpProxyHost;
	}

	@Override
	public final String getHttpProxyPassword() {
		return httpProxyPassword;
	}

	@Override
	public final int getHttpProxyPort() {
		return httpProxyPort;
	}

	@Override
	public final String getHttpProxyUser() {
		return httpProxyUser;
	}

	@Override
	public final int getHttpReadTimeout() {
		return httpReadTimeout;
	}

	@Override
	public final int getHttpRetryCount() {
		return httpRetryCount;
	}

	@Override
	public final int getHttpRetryIntervalSeconds() {
		return httpRetryIntervalSeconds;
	}

	@Override
	public String getMoeFMBaseURL() {
		return moeFMBaseURL;
	}

	@Override
	public String getMoefouBaseURL() {
		return moefouBaseURL;
	}

	@Override
	public String getOAuthAccessTokenURL() {
		return oAuthAccessTokenURL;
	}

	@Override
	public String getOAuthAuthenticationURL() {
		return oAuthAuthenticationURL;
	}

	@Override
	public String getOAuthAuthorizationURL() {
		return oAuthAuthorizationURL;
	}

	@Override
	public String getOAuthBaseURL() {
		return oAuthBaseURL;
	}

	@Override
	public final String getOAuthConsumerKey() {
		return oAuthConsumerKey;
	}

	@Override
	public final String getOAuthConsumerSecret() {
		return oAuthConsumerSecret;
	}

	@Override
	public String getOAuthRequestTokenURL() {
		return oAuthRequestTokenURL;
	}

	@Override
	public Map<String, String> getRequestHeaders() {
		return requestHeaders;
	}

	@Override
	public final String getUserAgent() {
		return userAgent;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (clientName == null ? 0 : clientName.hashCode());
		result = prime * result + (clientURL == null ? 0 : clientURL.hashCode());
		result = prime * result + (clientVersion == null ? 0 : clientVersion.hashCode());
		result = prime * result + (debug ? 1231 : 1237);
		result = prime * result + defaultMaxPerRoute;
		result = prime * result + (gzipEnabled ? 1231 : 1237);
		result = prime * result + httpConnectionTimeout;
		result = prime * result + (httpProxyHost == null ? 0 : httpProxyHost.hashCode());
		result = prime * result + (httpProxyPassword == null ? 0 : httpProxyPassword.hashCode());
		result = prime * result + httpProxyPort;
		result = prime * result + (httpProxyUser == null ? 0 : httpProxyUser.hashCode());
		result = prime * result + httpReadTimeout;
		result = prime * result + httpRetryCount;
		result = prime * result + httpRetryIntervalSeconds;
		result = prime * result + (ignoreSSLError ? 1231 : 1237);
		result = prime * result + (includeEntitiesEnabled ? 1231 : 1237);
		result = prime * result + (includeRTsEnabled ? 1231 : 1237);
		result = prime * result + maxTotalConnections;
		result = prime * result + (moeFMBaseURL == null ? 0 : moeFMBaseURL.hashCode());
		result = prime * result + (moefouBaseURL == null ? 0 : moefouBaseURL.hashCode());
		result = prime * result + (oAuthAccessTokenURL == null ? 0 : oAuthAccessTokenURL.hashCode());
		result = prime * result + (oAuthAuthenticationURL == null ? 0 : oAuthAuthenticationURL.hashCode());
		result = prime * result + (oAuthAuthorizationURL == null ? 0 : oAuthAuthorizationURL.hashCode());
		result = prime * result + (oAuthBaseURL == null ? 0 : oAuthBaseURL.hashCode());
		result = prime * result + (oAuthConsumerKey == null ? 0 : oAuthConsumerKey.hashCode());
		result = prime * result + (oAuthConsumerSecret == null ? 0 : oAuthConsumerSecret.hashCode());
		result = prime * result + (oAuthRequestTokenURL == null ? 0 : oAuthRequestTokenURL.hashCode());
		result = prime * result + (prettyDebug ? 1231 : 1237);
		result = prime * result + (requestHeaders == null ? 0 : requestHeaders.hashCode());
		result = prime * result + (useSSL ? 1231 : 1237);
		result = prime * result + (userAgent == null ? 0 : userAgent.hashCode());
		return result;
	}

	@Override
	public final boolean isDebugEnabled() {
		return debug;
	}

	@Override
	public boolean isGZIPEnabled() {
		return gzipEnabled;
	}

	@Override
	public boolean isPrettyDebugEnabled() {
		return prettyDebug;
	}

	@Override
	public boolean isProxyConfigured() {
		return (getHttpProxyHost() != null || "".equals(getHttpProxyHost())) && getHttpProxyPort() > 0;
	}

	@Override
	public boolean isSSLEnabled() {
		return getMoefouBaseURL() != null && getMoefouBaseURL().startsWith("https://");
	}

	@Override
	public final boolean isSSLErrorIgnored() {
		return ignoreSSLError;
	}

	@Override
	public String toString() {
		return "ConfigurationBase{debug=" + debug + ", userAgent=" + userAgent + ", useSSL=" + useSSL
				+ ", ignoreSSLError=" + ignoreSSLError + ", prettyDebug=" + prettyDebug + ", gzipEnabled="
				+ gzipEnabled + ", httpProxyHost=" + httpProxyHost + ", httpProxyUser=" + httpProxyUser
				+ ", httpProxyPassword=" + httpProxyPassword + ", httpProxyPort=" + httpProxyPort
				+ ", httpConnectionTimeout=" + httpConnectionTimeout + ", httpReadTimeout=" + httpReadTimeout
				+ ", httpRetryCount=" + httpRetryCount + ", httpRetryIntervalSeconds=" + httpRetryIntervalSeconds
				+ ", maxTotalConnections=" + maxTotalConnections + ", defaultMaxPerRoute=" + defaultMaxPerRoute
				+ ", oAuthConsumerKey=" + oAuthConsumerKey + ", oAuthConsumerSecret=" + oAuthConsumerSecret
				+ ", oAuthRequestTokenURL=" + oAuthRequestTokenURL + ", oAuthAuthorizationURL=" + oAuthAuthorizationURL
				+ ", oAuthAccessTokenURL=" + oAuthAccessTokenURL + ", oAuthAuthenticationURL=" + oAuthAuthenticationURL
				+ ", oAuthBaseURL=" + oAuthBaseURL + ", moefouBaseURL=" + moefouBaseURL + ", moeFMBaseURL="
				+ moeFMBaseURL + ", includeRTsEnabled=" + includeRTsEnabled + ", includeEntitiesEnabled="
				+ includeEntitiesEnabled + ", clientVersion=" + clientVersion + ", clientURL=" + clientURL
				+ ", clientName=" + clientName + ", requestHeaders=" + requestHeaders + "}";
	}

	protected void cacheInstance() {
		cacheInstance(this);
	}

	// assures equality after deserializedation
	protected Object readResolve() throws ObjectStreamException {
		return getInstance(this);
	}

	protected final void setClientName(final String clientName) {
		this.clientName = clientName;
		initRequestHeaders();
	}

	protected final void setClientURL(final String clientURL) {
		this.clientURL = clientURL;
		initRequestHeaders();
	}

	protected final void setClientVersion(final String clientVersion) {
		this.clientVersion = clientVersion;
		initRequestHeaders();
	}

	protected final void setDebug(final boolean debug) {
		this.debug = debug;
	}

	protected final void setGZIPEnabled(final boolean gzipEnabled) {
		this.gzipEnabled = gzipEnabled;
		initRequestHeaders();
	}

	protected final void setHttpConnectionTimeout(final int connectionTimeout) {
		httpConnectionTimeout = connectionTimeout;
	}

	protected final void setHttpDefaultMaxPerRoute(final int defaultMaxPerRoute) {
		this.defaultMaxPerRoute = defaultMaxPerRoute;
	}

	protected final void setHttpMaxTotalConnections(final int maxTotalConnections) {
		this.maxTotalConnections = maxTotalConnections;
	}

	protected final void setHttpProxyHost(final String proxyHost) {
		httpProxyHost = proxyHost;
		initRequestHeaders();
	}

	protected final void setHttpProxyPassword(final String proxyPassword) {
		httpProxyPassword = proxyPassword;
	}

	protected final void setHttpProxyPort(final int proxyPort) {
		httpProxyPort = proxyPort;
		initRequestHeaders();
	}

	protected final void setHttpProxyUser(final String proxyUser) {
		httpProxyUser = proxyUser;
	}

	protected final void setHttpReadTimeout(final int readTimeout) {
		httpReadTimeout = readTimeout;
	}

	protected final void setHttpRetryCount(final int retryCount) {
		httpRetryCount = retryCount;
	}

	protected final void setHttpRetryIntervalSeconds(final int retryIntervalSeconds) {
		httpRetryIntervalSeconds = retryIntervalSeconds;
	}

	protected final void setIgnoreSSLError(final boolean ignoreSSLError) {
		this.ignoreSSLError = ignoreSSLError;
		initRequestHeaders();
	}

	protected final void setIncludeEntitiesEnbled(final boolean enabled) {
		includeEntitiesEnabled = enabled;
	}

	protected final void setIncludeRTsEnbled(final boolean enabled) {
		includeRTsEnabled = enabled;
	}

	protected final void setMoeFMBaseURL(String moeFMBaseURL) {
		if (isNullOrEmpty(moeFMBaseURL)) {
			moeFMBaseURL = DEFAULT_MOEFM_BASE_URL;
		}
		this.moeFMBaseURL = moeFMBaseURL;
		fixMoeFMBaseURL();
	}

	protected final void setMoefouBaseURL(String moefouBaseURL) {
		if (isNullOrEmpty(moefouBaseURL)) {
			moefouBaseURL = DEFAULT_MOEFOU_BASE_URL;
		}
		this.moefouBaseURL = fixURLSlash(moefouBaseURL);
		fixMoefouBaseURL();
	}

	protected final void setOAuthBaseURL(String oAuthBaseURL) {
		if (isNullOrEmpty(oAuthBaseURL)) {
			oAuthBaseURL = DEFAULT_OAUTH_BASE_URL;
		}
		this.oAuthBaseURL = fixURLSlash(oAuthBaseURL);

		oAuthAccessTokenURL = oAuthBaseURL + PATH_SEGMENT_ACCESS_TOKEN;
		oAuthAuthenticationURL = oAuthBaseURL + PATH_SEGMENT_AUTHENTICATION;
		oAuthAuthorizationURL = oAuthBaseURL + PATH_SEGMENT_AUTHORIZATION;
		oAuthRequestTokenURL = oAuthBaseURL + PATH_SEGMENT_REQUEST_TOKEN;

		fixOAuthBaseURL();
	}

	protected final void setOAuthConsumerKey(final String oAuthConsumerKey) {
		this.oAuthConsumerKey = oAuthConsumerKey;
		fixMoefouBaseURL();
	}

	protected final void setOAuthConsumerSecret(final String oAuthConsumerSecret) {
		this.oAuthConsumerSecret = oAuthConsumerSecret;
		fixMoefouBaseURL();
	}

	protected final void setPrettyDebugEnabled(final boolean prettyDebug) {
		this.prettyDebug = prettyDebug;
	}

	protected final void setUserAgent(final String userAgent) {
		this.userAgent = userAgent;
		initRequestHeaders();
	}

	protected final void setUseSSL(final boolean useSSL) {
		this.useSSL = useSSL;
		fixMoefouBaseURL();
	}

	private void fixMoeFMBaseURL() {
		if (DEFAULT_MOEFM_BASE_URL.equals(fixURL(DEFAULT_USE_SSL, moeFMBaseURL))) {
			moeFMBaseURL = fixURL(useSSL, moeFMBaseURL);
		}
		initRequestHeaders();
	}

	private void fixMoefouBaseURL() {
		if (DEFAULT_MOEFOU_BASE_URL.equals(fixURL(DEFAULT_USE_SSL, moefouBaseURL))) {
			moefouBaseURL = fixURL(useSSL, moefouBaseURL);
		}
		initRequestHeaders();
	}

	private void fixOAuthBaseURL() {
		if (DEFAULT_OAUTH_BASE_URL.equals(fixURL(DEFAULT_USE_SSL, oAuthBaseURL))) {
			oAuthBaseURL = fixURL(useSSL, oAuthBaseURL);
		}
		if (oAuthBaseURL != null
				&& (oAuthBaseURL + PATH_SEGMENT_ACCESS_TOKEN).equals(fixURL(DEFAULT_USE_SSL, oAuthAccessTokenURL))) {
			oAuthAccessTokenURL = fixURL(useSSL, oAuthAccessTokenURL);
		}
		if (oAuthBaseURL != null
				&& (oAuthBaseURL + PATH_SEGMENT_AUTHENTICATION).equals(fixURL(DEFAULT_USE_SSL, oAuthAuthenticationURL))) {
			oAuthAuthenticationURL = fixURL(useSSL, oAuthAuthenticationURL);
		}
		if (oAuthBaseURL != null
				&& (oAuthBaseURL + PATH_SEGMENT_AUTHORIZATION).equals(fixURL(DEFAULT_USE_SSL, oAuthAuthorizationURL))) {
			oAuthAuthorizationURL = fixURL(useSSL, oAuthAuthorizationURL);
		}
		if (oAuthBaseURL != null
				&& (oAuthBaseURL + PATH_SEGMENT_REQUEST_TOKEN).equals(fixURL(DEFAULT_USE_SSL, oAuthRequestTokenURL))) {
			oAuthRequestTokenURL = fixURL(useSSL, oAuthRequestTokenURL);
		}
		initRequestHeaders();
	}

	final void initRequestHeaders() {
		requestHeaders = new HashMap<String, String>();
		requestHeaders.put("X-Twitter-Client-Version", getClientVersion());
		requestHeaders.put("X-Twitter-Client-URL", getClientURL());
		requestHeaders.put("X-Twitter-Client", getClientName());

		requestHeaders.put("User-Agent", getUserAgent());
		if (gzipEnabled) {
			requestHeaders.put("Accept-Encoding", "gzip");
		}
		// I found this may cause "Socket is closed" error in Android, so I
		// changed it to "keep-alive".
		if (!isNullOrEmpty(httpProxyHost) && httpProxyPort > 0) {
			requestHeaders.put("Connection", "keep-alive");
		} else {
			requestHeaders.put("Connection", "close");
		}
	}

	private static void cacheInstance(final ConfigurationBase conf) {
		if (!instances.contains(conf)) {
			instances.add(conf);
		}
	}

	private static ConfigurationBase getInstance(final ConfigurationBase configurationBase) {
		int index;
		if ((index = instances.indexOf(configurationBase)) == -1) {
			instances.add(configurationBase);
			return configurationBase;
		} else
			return instances.get(index);
	}

	static String fixURL(final boolean useSSL, String url) {
		if (null == url) return null;
		if (!url.startsWith("http://") || !url.startsWith("https://")) {
			url = "https://" + url;
		}
		final int index = url.indexOf("://");
		if (-1 == index) throw new IllegalArgumentException("url should contain '://'");
		final String hostAndLater = url.substring(index + 3);
		if (useSSL)
			return "https://" + hostAndLater;
		else
			return "http://" + hostAndLater;
	}

	static String fixURLSlash(final String urlOrig) {
		if (urlOrig == null) return null;
		if (!urlOrig.endsWith("/")) return urlOrig + "/";
		return urlOrig;
	}

	static boolean isNullOrEmpty(final String string) {
		if (string == null) return true;
		if (string.length() == 0) return true;
		return false;
	}

}
