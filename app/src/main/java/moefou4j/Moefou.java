/*
 * Copyright 2012 Mariotaku Lee <mariotaku.lee@gmail.com>
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

import moefou4j.api.FavoriteMethods;
import moefou4j.api.MoeFMMethods;
import moefou4j.api.SearchMethods;
import moefou4j.api.SubMethods;
import moefou4j.api.UserMethods;
import moefou4j.api.WikiMethods;
import moefou4j.auth.OAuthSupport;

/**
 * @author Mariotaku Lee <mariotaku.lee@gmail.com>
 * @since Moefou4J 0.1
 */
public interface Moefou extends OAuthSupport, MoefouBase, WikiMethods, SubMethods, UserMethods, SearchMethods,
		FavoriteMethods, MoeFMMethods {
}
