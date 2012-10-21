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

import static moefou4j.internal.util.Moefou4JInternalParseUtil.getInt;

import java.util.ArrayList;

import moefou4j.MoefouException;
import moefou4j.ResponseList;
import moefou4j.http.HttpResponse;
import moefou4j.internal.json.MoefouResponseImpl.InformationImpl;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Yusuke Yamamoto - yusuke at mac.com
 * @since Twitter4J 2.1.3
 */
class ResponseListImpl<T> extends ArrayList<T> implements ResponseList<T> {

	private static final long serialVersionUID = -7789068763212377625L;

	private PageableInformation information;

	ResponseListImpl() {
		super();
	}

	ResponseListImpl(final HttpResponse res) throws MoefouException {
		this(res.asJSONObject());
	}

	ResponseListImpl(final JSONObject json) throws MoefouException {
		super();
		init(json);
	}

	@Override
	public PageableInformation getInformation() {
		return information;
	}

	@Override
	public String toString() {
		return "ResponseListImpl{information=" + information + ", items=" + super.toString() + "}";
	}

	private void init(final JSONObject json) throws MoefouException {
		try {
			final JSONObject response_json = json.getJSONObject("response");
			information = new PageableInformationImpl(response_json);
		} catch (final JSONException e) {
			throw new MoefouException(e);
		}
	}

	static class PageableInformationImpl extends InformationImpl implements PageableInformation {

		private static final long serialVersionUID = 8390363206544179609L;
		int itemCount;
		int page;

		PageableInformationImpl(final JSONObject json) throws MoefouException {
			super(json);
			page = getInt("page", json);
			itemCount = getInt("item_count", json);
		}

		@Override
		public int getItemCount() {
			return itemCount;
		}

		@Override
		public int getPage() {
			return page;
		}

		@Override
		public String toString() {
			return "PlaylistInformationImpl{itemCount=" + itemCount + ", page=" + page + ", parameters=" + parameters
					+ ", request=" + request + "}";
		}

	}
}
