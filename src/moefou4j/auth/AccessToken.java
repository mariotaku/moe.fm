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
import moefou4j.http.HttpResponse;

/**
 * Representing authorized Access Token which is passed to the service provider
 * in order to access protected resources.<br>
 * the token and token secret can be stored into some persistent stores such as
 * file system or RDBMS for the further accesses.
 * 
 * @author Yusuke Yamamoto - yusuke at mac.com
 */
public class AccessToken extends OAuthToken {

	public AccessToken(final String token, final String tokenSecret) {
		super(token, tokenSecret);
	}

	AccessToken(final HttpResponse res) throws MoefouException {
		this(res.asString());
	}

	AccessToken(final String str) {
		super(str);
	}
}
