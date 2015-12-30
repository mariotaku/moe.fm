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

package moefou4j.http;

import java.util.Arrays;
import java.util.Map;

import moefou4j.auth.Authorization;

/**
 * HTTP Request parameter object
 * 
 * @author Yusuke Yamamoto - yusuke at mac.com
 */
public final class HttpRequest {

	private final RequestMethod method;

	private final String url;

	private final HttpParameter[] parameters;

	private final Authorization authorization;

	private final Map<String, String> requestHeaders;

	private static final HttpParameter[] NULL_PARAMETERS = new HttpParameter[0];

	/**
	 * @param method Specifies the HTTP method
	 * @param url the request to request
	 * @param parameters parameters
	 * @param authorization Authentication implementation. Currently
	 *            BasicAuthentication, OAuthAuthentication and
	 *            NullAuthentication are supported.
	 * @param requestHeaders
	 */
	public HttpRequest(final RequestMethod method, final String url, final HttpParameter[] parameters,
			final Authorization authorization, final Map<String, String> requestHeaders) {
		this.method = method;
		if (method != RequestMethod.POST && parameters != null && parameters.length != 0) {
			final String param_string = HttpParameter.encodeParameters(parameters);
			this.url = url + "?" + param_string;
			this.parameters = NULL_PARAMETERS;
		} else {
			this.url = url;
			this.parameters = parameters;
		}
		this.authorization = authorization;
		this.requestHeaders = requestHeaders;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (!(obj instanceof HttpRequest)) return false;
		final HttpRequest other = (HttpRequest) obj;
		if (authorization == null) {
			if (other.authorization != null) return false;
		} else if (!authorization.equals(other.authorization)) return false;
		if (method == null) {
			if (other.method != null) return false;
		} else if (!method.equals(other.method)) return false;
		if (!Arrays.equals(parameters, other.parameters)) return false;
		if (requestHeaders == null) {
			if (other.requestHeaders != null) return false;
		} else if (!requestHeaders.equals(other.requestHeaders)) return false;
		if (url == null) {
			if (other.url != null) return false;
		} else if (!url.equals(other.url)) return false;
		return true;
	}

	public Authorization getAuthorization() {
		return authorization;
	}

	public RequestMethod getMethod() {
		return method;
	}

	public HttpParameter[] getParameters() {
		return parameters;
	}

	public Map<String, String> getRequestHeaders() {
		return requestHeaders;
	}

	public String getURL() {
		return url;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (authorization == null ? 0 : authorization.hashCode());
		result = prime * result + (method == null ? 0 : method.hashCode());
		result = prime * result + Arrays.hashCode(parameters);
		result = prime * result + (requestHeaders == null ? 0 : requestHeaders.hashCode());
		result = prime * result + (url == null ? 0 : url.hashCode());
		return result;
	}

	@Override
	public String toString() {
		return "HttpRequest{method=" + method + ", url=" + url + ", parameters=" + Arrays.toString(parameters)
				+ ", authorization=" + authorization + ", requestHeaders=" + requestHeaders + "}";
	}

}
