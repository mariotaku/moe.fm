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

import moefou4j.auth.AuthorizationConfiguration;
import moefou4j.http.HttpClientConfiguration;
import moefou4j.http.HttpClientWrapperConfiguration;

/**
 * @author Yusuke Yamamoto - yusuke at mac.com
 */
public interface Configuration extends HttpClientConfiguration, HttpClientWrapperConfiguration,
		AuthorizationConfiguration {

	String getClientName();

	String getClientURL();

	String getClientVersion();

	String getMoeFMBaseURL();

	String getMoefouBaseURL();

	String getOAuthAccessTokenURL();

	String getOAuthAuthenticationURL();

	String getOAuthAuthorizationURL();

	String getOAuthBaseURL();

	String getOAuthRequestTokenURL();

	String getUserAgent();

	boolean isDebugEnabled();

	boolean isProxyConfigured();

	boolean isSSLEnabled();

}
