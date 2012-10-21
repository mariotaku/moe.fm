package moefou4j.api;

import moefou4j.MoefouException;
import moefou4j.Paging;
import moefou4j.ResponseList;
import moefou4j.Wiki;

public interface SearchMethods {

	/**
	 * 根据条件获取条目列表。需要API Key认证或者OAuth认证。
	 * 
	 * @param type 必填，条目类型，多个类型以数组表示。可使用 {@link Wiki.Type#ANIME} 代替
	 *            {@link Wiki.Type#TV}、{@link Wiki.Type#OVA}、
	 *            {@link Wiki.Type#OAD}、{@link Wiki.Type#MOVIE} 类型。
	 * @param paging
	 * @param keywords 关键词
	 * @return
	 * @throws MoefouException
	 */
	public ResponseList<Wiki> searchWikis(Wiki.Type[] type, Paging paging, String... keywords) throws MoefouException;

}
