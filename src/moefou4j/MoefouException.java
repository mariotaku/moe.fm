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

import java.util.List;

import moefou4j.http.HttpRequest;
import moefou4j.http.HttpResponse;
import moefou4j.http.HttpResponseCode;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;

/**
 * An exception class that will be thrown when TwitterAPI calls are failed.<br>
 * In case the Twitter server returned HTTP error code, you can get the HTTP
 * status code using getStatusCode() method.
 * 
 * @author Yusuke Yamamoto - yusuke at mac.com
 */
public class MoefouException extends Exception implements MoefouResponse, HttpResponseCode {
	
	private static final long serialVersionUID = 5876311940770455282L;

	private int statusCode = -1;

	private HttpResponse response;
	private HttpRequest request;
	private final String requestPath = null;

	boolean nested = false;

	private Information information;

	public MoefouException(final Exception cause) {
		this(cause.getMessage(), cause);
		if (cause instanceof MoefouException) {
			((MoefouException) cause).setNested();
		}
	}

	public MoefouException(final String message) {
		this(message, (Throwable) null);
	}

	public MoefouException(final String message, final Exception cause, final int statusCode) {
		this(message, cause);
		this.statusCode = statusCode;
	}

	public MoefouException(final String message, final HttpRequest req, final HttpResponse res) {
		this(message);
		request = req;
		response = res;
		statusCode = res != null ? res.getStatusCode() : -1;
	}

	public MoefouException(final String message, final Throwable cause) {
		super(message, cause);
	}

	public MoefouException(final String message, final Information information) {
		super(message);
		this.information = information;
	}
	
	@Override
	public boolean equals(final Object o) {
		if (this == o) return true;
		if (!(o instanceof MoefouException)) return false;

		final MoefouException that = (MoefouException) o;

		if (nested != that.nested) return false;
		if (statusCode != that.statusCode) return false;
		if (requestPath != null ? !requestPath.equals(that.requestPath) : that.requestPath != null) return false;
		if (response != null ? !response.equals(that.response) : that.response != null) return false;
		if (request != null ? !request.equals(that.request) : that.request != null) return false;

		return true;
	}

	public HttpRequest getHttpRequest() {
		return request;
	}

	public HttpResponse getHttpResponse() {
		return response;
	}

	@Override
	public Information getInformation() {
		return information;
	}

	public String getResponseHeader(final String name) {
		String value = null;
		if (response != null) {
			final List<String> header = response.getResponseHeaderFields().get(name);
			if (header.size() > 0) {
				value = header.get(0);
			}
		}
		return value;
	}

	/**
	 * Returns int value of "Retry-After" response header (Search API) or
	 * seconds_until_reset (REST API). An application that exceeds the rate
	 * limitations of the Search API will receive HTTP 420 response codes to
	 * requests. It is a best practice to watch for this error condition and
	 * honor the Retry-After header that instructs the application when it is
	 * safe to continue. The Retry-After header's value is the number of seconds
	 * your application should wait before submitting another query (for
	 * example: Retry-After: 67).<br>
	 * Check if getStatusCode() == 503 before calling this method to ensure that
	 * you are actually exceeding rate limitation with query apis.<br>
	 * 
	 * @return instructs the application when it is safe to continue in seconds
	 * @see <a href="https://dev.twitter.com/docs/rate-limiting">Rate Limiting |
	 *      Twitter Developers</a>
	 * @since Twitter4J 2.1.0
	 */
	public int getRetryAfter() {
		int retryAfter = -1;
		if (statusCode == ENHANCE_YOUR_CLAIM) {
			try {
				final String retryAfterStr = response.getResponseHeader("Retry-After");
				if (retryAfterStr != null) {
					retryAfter = Integer.valueOf(retryAfterStr);
				}
			} catch (final NumberFormatException ignore) {
			}
		}
		return retryAfter;
	}

	public int getStatusCode() {
		return statusCode;
	}

	@Override
	public int hashCode() {
		int result = statusCode;
		result = 31 * result + (request != null ? request.hashCode() : 0);
		result = 31 * result + (response != null ? response.hashCode() : 0);
		result = 31 * result + (requestPath != null ? requestPath.hashCode() : 0);
		result = 31 * result + (nested ? 1 : 0);
		return result;
	}

	/**
	 * Tests if the exception is caused by network issue
	 * 
	 * @return if the exception is caused by network issue
	 * @since Twitter4J 2.1.2
	 */
	public boolean isCausedByNetworkIssue() {
		return getCause() instanceof IOException;
	}

	void setNested() {
		nested = true;
	}
}
