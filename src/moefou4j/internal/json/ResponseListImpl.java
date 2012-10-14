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

import java.util.ArrayList;

import moefou4j.MoefouException;
import moefou4j.ResponseList;
import moefou4j.internal.http.HttpResponse;
import moefou4j.internal.json.MoefouResponseImpl.InformationImpl;

/**
 * @author Yusuke Yamamoto - yusuke at mac.com
 * @since Twitter4J 2.1.3
 */
class ResponseListImpl<T> extends ArrayList<T> implements ResponseList<T> {
	private static final long serialVersionUID = -7789068763212377625L;

	private Information information;

	ResponseListImpl(final HttpResponse res) throws MoefouException {
		super();
		init(res);
	}

	@Override
	public Information getInformation() {
		return information;
	}

	@Override
	public String toString() {
		return "ResponseListImpl{information=" + information + ", items=" + super.toString() + "}";
	}

	private void init(final HttpResponse res) throws MoefouException {
		information = new InformationImpl(res);
	}
}
