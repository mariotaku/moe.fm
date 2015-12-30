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

/**
 * A builder that can be used to construct a moefou4j configuration with desired
 * settings. This builder has sensible defaults such that
 * {@code new ConfigurationBuilder().build()} would create a usable
 * configuration. This configuration builder is useful for clients that wish to
 * configure moefou4j in unit tests or from command line flags for example.
 * 
 * @author John Sirois - john.sirois at gmail.com
 */
public final class ConfigurationBuilder {

	private ConfigurationBase configuration = new ConfigurationBase();

	public Configuration build() {
		checkNotBuilt();
		configuration.cacheInstance();
		try {
			return configuration;
		} finally {
			configuration = null;
		}
	}

	public ConfigurationBuilder setClientName(final String clientName) {
		checkNotBuilt();
		configuration.setClientName(clientName);
		return this;
	}

	public ConfigurationBuilder setClientURL(final String clientURL) {
		checkNotBuilt();
		configuration.setClientURL(clientURL);
		return this;
	}

	public ConfigurationBuilder setClientVersion(final String clientVersion) {
		checkNotBuilt();
		configuration.setClientVersion(clientVersion);
		return this;
	}

	public ConfigurationBuilder setDebugEnabled(final boolean debugEnabled) {
		checkNotBuilt();
		configuration.setDebug(debugEnabled);
		return this;
	}

	public ConfigurationBuilder setGZIPEnabled(final boolean gzipEnabled) {
		checkNotBuilt();
		configuration.setGZIPEnabled(gzipEnabled);
		return this;
	}

	public ConfigurationBuilder setHttpConnectionTimeout(final int httpConnectionTimeout) {
		checkNotBuilt();
		configuration.setHttpConnectionTimeout(httpConnectionTimeout);
		return this;
	}

	public ConfigurationBuilder setHttpDefaultMaxPerRoute(final int httpDefaultMaxPerRoute) {
		checkNotBuilt();
		configuration.setHttpDefaultMaxPerRoute(httpDefaultMaxPerRoute);
		return this;
	}

	public ConfigurationBuilder setHttpMaxTotalConnections(final int httpMaxConnections) {
		checkNotBuilt();
		configuration.setHttpMaxTotalConnections(httpMaxConnections);
		return this;
	}

	public ConfigurationBuilder setHttpProxyHost(final String httpProxyHost) {
		checkNotBuilt();
		configuration.setHttpProxyHost(httpProxyHost);
		return this;
	}

	public ConfigurationBuilder setHttpProxyPassword(final String httpProxyPassword) {
		checkNotBuilt();
		configuration.setHttpProxyPassword(httpProxyPassword);
		return this;
	}

	public ConfigurationBuilder setHttpProxyPort(final int httpProxyPort) {
		checkNotBuilt();
		configuration.setHttpProxyPort(httpProxyPort);
		return this;
	}

	public ConfigurationBuilder setHttpProxyUser(final String httpProxyUser) {
		checkNotBuilt();
		configuration.setHttpProxyUser(httpProxyUser);
		return this;
	}

	public ConfigurationBuilder setHttpReadTimeout(final int httpReadTimeout) {
		checkNotBuilt();
		configuration.setHttpReadTimeout(httpReadTimeout);
		return this;
	}

	public ConfigurationBuilder setHttpRetryCount(final int httpRetryCount) {
		checkNotBuilt();
		configuration.setHttpRetryCount(httpRetryCount);
		return this;
	}

	public ConfigurationBuilder setHttpRetryIntervalSeconds(final int httpRetryIntervalSeconds) {
		checkNotBuilt();
		configuration.setHttpRetryIntervalSeconds(httpRetryIntervalSeconds);
		return this;
	}

	public ConfigurationBuilder setIgnoreSSLError(final boolean ignoreSSLError) {
		checkNotBuilt();
		configuration.setIgnoreSSLError(ignoreSSLError);
		return this;
	}

	public ConfigurationBuilder setIncludeEntitiesEnabled(final boolean enabled) {
		checkNotBuilt();
		configuration.setIncludeEntitiesEnbled(enabled);
		return this;
	}

	public ConfigurationBuilder setIncludeRTsEnabled(final boolean enabled) {
		checkNotBuilt();
		configuration.setIncludeRTsEnbled(enabled);
		return this;
	}

	public ConfigurationBuilder setOAuthBaseURL(final String oAuthBaseURL) {
		checkNotBuilt();
		configuration.setOAuthBaseURL(oAuthBaseURL);
		return this;
	}

	public ConfigurationBuilder setOAuthConsumerKey(final String oAuthConsumerKey) {
		checkNotBuilt();
		configuration.setOAuthConsumerKey(oAuthConsumerKey);
		return this;
	}

	public ConfigurationBuilder setOAuthConsumerSecret(final String oAuthConsumerSecret) {
		checkNotBuilt();
		configuration.setOAuthConsumerSecret(oAuthConsumerSecret);
		return this;
	}

	public ConfigurationBuilder setPrettyDebugEnabled(final boolean prettyDebugEnabled) {
		checkNotBuilt();
		configuration.setPrettyDebugEnabled(prettyDebugEnabled);
		return this;
	}

	public ConfigurationBuilder setRestBaseURL(final String restBaseURL) {
		checkNotBuilt();
		configuration.setMoefouBaseURL(restBaseURL);
		return this;
	}

	public ConfigurationBuilder setUserAgent(final String userAgent) {
		checkNotBuilt();
		configuration.setUserAgent(userAgent);
		return this;
	}

	public ConfigurationBuilder setUseSSL(final boolean useSSL) {
		checkNotBuilt();
		configuration.setUseSSL(useSSL);
		return this;
	}

	private void checkNotBuilt() {
		if (configuration == null)
			throw new IllegalStateException("Cannot use this builder any longer, build() has already been called");
	}
}
