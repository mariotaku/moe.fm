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

package moefou4j.internal.util;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import moefou4j.MoefouException;
import moefou4j.http.HTMLEntity;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * A tiny parse utility class.
 * 
 * @author Yusuke Yamamoto - yusuke at mac.com
 */
public final class Moefou4JInternalParseUtil {
	private static ThreadLocal<Map<String, SimpleDateFormat>> formatMap = new ThreadLocal<Map<String, SimpleDateFormat>>() {
		@Override
		protected Map<String, SimpleDateFormat> initialValue() {
			return new HashMap<String, SimpleDateFormat>();
		}
	};

	private Moefou4JInternalParseUtil() {
		// should never be instantiated
		throw new AssertionError();
	}

	public static boolean getBoolean(final String name, final JSONObject json) {
		final String str = getRawString(name, json);
		if (null == str || "null".equals(str)) return false;
		return Boolean.valueOf(str);
	}

	public static Date getDate(final String name, final JSONObject json) throws MoefouException {
		return getDate(name, json, "EEE MMM d HH:mm:ss z yyyy");
	}

	public static Date getDate(final String name, final JSONObject json, final String format) throws MoefouException {
		final String dateStr = getUnescapedString(name, json);
		if ("null".equals(dateStr) || null == dateStr)
			return null;
		else
			return getDate(dateStr, format);
	}

	public static Date getDate(final String name, final String format) throws MoefouException {
		SimpleDateFormat sdf = formatMap.get().get(format);
		if (null == sdf) {
			sdf = new SimpleDateFormat(format, Locale.ENGLISH);
			sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
			formatMap.get().put(format, sdf);
		}
		try {
			return sdf.parse(name);
		} catch (final ParseException pe) {
			throw new MoefouException("Unexpected date format(" + name + ") returned from twitter.com", pe);
		}
	}

	public static double getDouble(final String name, final JSONObject json) {
		final String str2 = getRawString(name, json);
		if (null == str2 || "".equals(str2) || "null".equals(str2))
			return -1;
		else
			return Double.valueOf(str2);
	}

	public static int getInt(final String str) {
		if (null == str || "".equals(str) || "null".equals(str))
			return -1;
		else {
			try {
				return Integer.valueOf(str);
			} catch (final NumberFormatException nfe) {
				// workaround for the API side issue
				// http://moefou4j.org/jira/browse/TFJ-484
				return -1;
			}
		}
	}

	public static int getInt(final String name, final JSONObject json) {
		return getInt(getRawString(name, json));
	}

	public static long getLong(String str) {
		if (null == str || "".equals(str) || "null".equals(str))
			return -1;
		else {
			// some count over 100 will be expressed as "100+"
			if (str.endsWith("+")) {
				str = str.substring(0, str.length() - 1);
				try {
					return Integer.valueOf(str) + 1;
				} catch (final NumberFormatException nfe) {
					// workaround for the API side issue
					// http://moefou4j.org/jira/browse/TFJ-484
					return -1;
				}
			}
			try {
				return Long.valueOf(str);
			} catch (final NumberFormatException nfe) {
				// workaround for the API side issue
				// http://moefou4j.org/jira/browse/TFJ-484
				return -1;
			}
		}
	}

	public static long getLong(final String name, final JSONObject json) {
		return getLong(getRawString(name, json));
	}

	public static String getRawString(final String name, final JSONObject json) {
		try {
			return parseString(json.get(name));
		} catch (final JSONException jsone) {
			return null;
		}
	}

	public static String getUnescapedString(final String str, final JSONObject json) {
		return HTMLEntity.unescape(getRawString(str, json));
	}

	public static String getURLDecodedString(final String name, final JSONObject json) {
		String returnValue = getRawString(name, json);
		if (returnValue != null) {
			try {
				returnValue = URLDecoder.decode(returnValue, "UTF-8");
			} catch (final UnsupportedEncodingException ignore) {
			}
		}
		return returnValue;
	}

	public static URL getURLFromString(final String name, final JSONObject json) {
		final String url = getRawString(name, json);
		try {
			return new URL(url);
		} catch (final MalformedURLException e) {
			// System.err.println("Invalid URL " + url);
		}
		return null;
	}

	public static Date parseTrendsDate(final String asOfStr) throws MoefouException {
		Date parsed;
		switch (asOfStr.length()) {
			case 10:
				parsed = new Date(Long.parseLong(asOfStr) * 1000);
				break;
			case 20:
				parsed = getDate(asOfStr, "yyyy-MM-dd'T'HH:mm:ss'Z'");
				break;
			default:
				parsed = getDate(asOfStr, "EEE, d MMM yyyy HH:mm:ss z");
		}
		return parsed;
	}

	private static String parseString(final Object object) {
		return object != null ? object.toString() : null;
	}
}
