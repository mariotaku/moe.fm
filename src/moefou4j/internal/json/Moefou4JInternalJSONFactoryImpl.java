/*
 * Copyright (C) 2007 Yusuke Yamamoto
 * Copyright (C) 2011 Twitter, Inc.
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

import moefou4j.MoefouException;
import moefou4j.Playlist;
import moefou4j.ResponseList;
import moefou4j.Wiki;
import moefou4j.conf.Configuration;
import moefou4j.internal.http.HttpResponse;

/**
 * @author Yusuke Yamamoto - yusuke at mac.com
 * @since Twitter4J 2.2.4
 */
public class Moefou4JInternalJSONFactoryImpl implements Moefou4JInternalFactory {

	private final Configuration conf;

	public Moefou4JInternalJSONFactoryImpl(final Configuration conf) {
		this.conf = conf;
	}

	@Override
	public Playlist createPlayist(final HttpResponse res, final String json_key) throws MoefouException {
		return PlaylistJSONImpl.createPlayList(res, json_key);
	}

	@Override
	public ResponseList<Wiki> createWikisList(final HttpResponse res, final String wikis_key) throws MoefouException {
		return WikiJSONImpl.createWikisList(res, wikis_key);
	}

}
