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

import java.util.ArrayList;
import java.util.List;

/**
 * @author Yusuke Yamamoto - yusuke at mac.com
 * @since Twitter4J 2.1.4
 */
public class Moefou4JInternalStringUtil {
	private Moefou4JInternalStringUtil() {
		throw new AssertionError();
	}

	public static long[] getLongArrayfromString(final String string, final char token) {
		if (string == null) return new long[0];
		final String[] items_string_array = string.split(String.valueOf(token));
		final ArrayList<Long> items_list = new ArrayList<Long>();
		for (final String id_string : items_string_array) {
			try {
				items_list.add(Long.parseLong(id_string));
			} catch (final NumberFormatException e) {
				// Ignore.
			}
		}
		final int list_size = items_list.size();
		final long[] array = new long[list_size];
		for (int i = 0; i < list_size; i++) {
			array[i] = items_list.get(i);
		}
		return array;
	}

	// for JDK1.4 compatibility

	public static String join(final int[] follows) {
		final StringBuffer buf = new StringBuffer(11 * follows.length);
		for (final int follow : follows) {
			if (0 != buf.length()) {
				buf.append(",");
			}
			buf.append(follow);
		}
		return buf.toString();
	}

	public static String join(final long[] array, final char token) {
		final StringBuilder builder = new StringBuilder();
		for (final long item : array) {
			if (0 != builder.length()) {
				builder.append(token);
			}
			builder.append(item);
		}
		return builder.toString();
	}

	public static String join(final String[] array, final char token) {
		final StringBuilder builder = new StringBuilder();
		for (final String str : array) {
			if (0 != builder.length()) {
				builder.append(token);
			}
			builder.append(str);
		}
		return builder.toString();
	}

	public static String maskString(final String str) {
		final StringBuffer buf = new StringBuffer(str.length());
		for (int i = 0; i < str.length(); i++) {
			buf.append("*");
		}
		return buf.toString();
	}

	public static String replaceLast(final String text, final String regex, final String replacement) {
		if (text == null || regex == null || replacement == null) return text;
		return text.replaceFirst("(?s)" + regex + "(?!.*?" + regex + ")", replacement);
	}

	public static String[] split(final String str, final String separator) {
		String[] returnValue;
		int index = str.indexOf(separator);
		if (index == -1) {
			returnValue = new String[] { str };
		} else {
			final List<String> strList = new ArrayList<String>();
			int oldIndex = 0;
			while (index != -1) {
				final String subStr = str.substring(oldIndex, index);
				strList.add(subStr);
				oldIndex = index + separator.length();
				index = str.indexOf(separator, oldIndex);
			}
			if (oldIndex != str.length()) {
				strList.add(str.substring(oldIndex));
			}
			returnValue = strList.toArray(new String[strList.size()]);
		}

		return returnValue;
	}
}
