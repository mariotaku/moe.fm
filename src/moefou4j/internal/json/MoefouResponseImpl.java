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

package moefou4j.internal.json;

import static moefou4j.internal.util.Moefou4JInternalParseUtil.getBoolean;
import static moefou4j.internal.util.Moefou4JInternalParseUtil.getRawString;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;

import moefou4j.MoefouException;
import moefou4j.MoefouResponse;
import moefou4j.internal.http.HttpResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Super interface of Twitter Response data interfaces which indicates that rate
 * limit status is available.
 * 
 * @author Yusuke Yamamoto - yusuke at mac.com
 * @see moefou4j.DirectMessage
 * @see moefou4j.Status
 * @see moefou4j.User
 */
/* package */abstract class MoefouResponseImpl implements MoefouResponse {

	private static final long serialVersionUID = 8016245009711538262L;

	private Information information;

	public MoefouResponseImpl() {
	}

	public MoefouResponseImpl(final HttpResponse res) throws MoefouException {
		this(res.asJSONObject());
	}
	
	public MoefouResponseImpl(final JSONObject json) throws MoefouException {
		init(json);
	}

	@Override
	public Information getInformation() {
		return information;
	}

	private void init(final JSONObject json) throws MoefouException {
		try {
			information = new InformationImpl(json.getJSONObject("response"));
		} catch (MoefouException e) {} catch (JSONException e) {
			throw new MoefouException(e);
		}
	}

	static class InformationImpl implements Information {

		private static final long serialVersionUID = -606811929444784275L;
		private boolean hasError;
		private HashMap<String, String> parameters;
		private String[] messages;
		private String request;

		public InformationImpl(final JSONObject resp_json) throws MoefouException {
			if (resp_json == null) return;
			try {
				final JSONObject json = resp_json.getJSONObject("information");
				hasError = getBoolean("has_error", json);
				if (!json.isNull("msg")) {
					final JSONArray msg_json = json.getJSONArray("msg");
					final int msg_length = msg_json.length();
					messages = new String[msg_length];
					for (int i = 0; i < msg_length; i++) {
						messages[i] = String.valueOf(msg_json.get(i));
					}
					if (hasError && messages.length > 0) throw new MoefouException(messages[0]);
				}
				if (!json.isNull("parameters")) {
					final JSONObject params_json = json.getJSONObject("parameters");
					parameters = new HashMap<String, String>();
					final Iterator<?> params_it = params_json.keys();
					while (params_it.hasNext()) {
						final String key = String.valueOf(params_it.next());
						parameters.put(key, getRawString(key, json));
					}
				}
				request = getRawString("request", json);
			} catch (final JSONException jsone) {
				throw new MoefouException(jsone);
			}
		}

		@Override
		public String[] getMessages() {
			return messages;
		}

		@Override
		public HashMap<String, String> getParameters() {
			return parameters;
		}

		@Override
		public String getRequest() {
			return request;
		}

		@Override
		public boolean hasError() {
			return hasError;
		}

		@Override
		public String toString() {
			return "InformationImpl{hasError=" + hasError + ", parameters=" + parameters + ", messages="
					+ Arrays.toString(messages) + ", request=" + request + "}";
		}
	}

}
