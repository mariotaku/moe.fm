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

import java.io.Serializable;
import java.util.HashMap;

public interface MoefouResponse extends Serializable {

	public Information getInformation();

	public interface Information extends Serializable {

		/**
		 * 错误信息（应该是这样的吧……
		 */
		public String[] getMessages();

		/**
		 * 请求的参数
		 */
		public HashMap<String, String> getParameters();

		public String getRequest();

		public boolean hasError();
	}
}
