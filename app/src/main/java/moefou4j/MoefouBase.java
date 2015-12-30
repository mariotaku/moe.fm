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

import moefou4j.auth.Authorization;
import moefou4j.conf.Configuration;
import moefou4j.http.HttpParameter;
import moefou4j.http.HttpResponse;

/**
 * @author Yusuke Yamamoto - yusuke at mac.com
 * @since Twitter4J 2.2.0
 */
public interface MoefouBase {

	/**
	 * Returns the authorization scheme for this instance.<br>
	 * The returned type will be either of BasicAuthorization,
	 * OAuthAuthorization, or NullAuthorization
	 * 
	 * @return the authorization scheme for this instance
	 */
	Authorization getAuthorization();

	/**
	 * Returns the configuration associated with this instance
	 * 
	 * @return configuration associated with this instance
	 * @since Twitter4J 2.1.8
	 */
	Configuration getConfiguration();

	HttpResponse rawGet(String url, HttpParameter... params) throws MoefouException;

	HttpResponse rawPost(String url, HttpParameter... params) throws MoefouException;

	/**
	 * Shuts down this instance and releases allocated resources.
	 */
	void shutdown();
}
