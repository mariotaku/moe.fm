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

import moefou4j.FavoriteResponse;
import moefou4j.MoefouException;
import moefou4j.Playlist;
import moefou4j.ResponseList;
import moefou4j.ResponseMessage;
import moefou4j.Sub;
import moefou4j.User;
import moefou4j.Wiki;
import moefou4j.http.HttpResponse;

/**
 * @author Yusuke Yamamoto - yusuke at mac.com
 * @since Twitter4J 2.2.4
 */
public interface Moefou4JInternalFactory {

	public Playlist createPlayist(HttpResponse res) throws MoefouException;

	public ResponseMessage createResponseMessage(HttpResponse res) throws MoefouException;

	public Sub createSub(HttpResponse res) throws MoefouException;

	public FavoriteResponse<Sub> createSubFavoriteResponse(HttpResponse res) throws MoefouException;

	public User createUser(HttpResponse res) throws MoefouException;

	public Wiki createWiki(HttpResponse res) throws MoefouException;

	public FavoriteResponse<Wiki> createWikiFavoriteResponse(HttpResponse res) throws MoefouException;

	public ResponseList<Wiki> createWikisList(HttpResponse res) throws MoefouException;

}
