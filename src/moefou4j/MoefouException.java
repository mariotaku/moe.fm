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
import java.util.List;

import moefou4j.http.HttpRequest;
import moefou4j.http.HttpResponse;
import moefou4j.http.HttpResponseCode;

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

	public MoefouException(final String message, final Information information) {
		super(message);
		this.information = information;
	}

	public MoefouException(final String message, final Throwable cause) {
		super(message, cause);
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (!(obj instanceof MoefouException)) return false;
		final MoefouException other = (MoefouException) obj;
		if (information == null) {
			if (other.information != null) return false;
		} else if (!information.equals(other.information)) return false;
		if (nested != other.nested) return false;
		if (request == null) {
			if (other.request != null) return false;
		} else if (!request.equals(other.request)) return false;
		if (response == null) {
			if (other.response != null) return false;
		} else if (!response.equals(other.response)) return false;
		if (statusCode != other.statusCode) return false;
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
		final int prime = 31;
		int result = 1;
		result = prime * result + (information == null ? 0 : information.hashCode());
		result = prime * result + (nested ? 1231 : 1237);
		result = prime * result + (request == null ? 0 : request.hashCode());
		result = prime * result + (response == null ? 0 : response.hashCode());
		result = prime * result + statusCode;
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

	@Override
	public String toString() {
		return "MoefouException{statusCode=" + statusCode + ", response=" + response + ", request=" + request
				+ ", nested=" + nested + ", information=" + information + "}";
	}

	void setNested() {
		nested = true;
	}
}
