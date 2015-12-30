package moefou4j.api;

import moefou4j.FavoriteResponse;
import moefou4j.MoefouException;
import moefou4j.Paging;
import moefou4j.ResponseList;
import moefou4j.Wiki;

/**
 * 对应条目API
 * 
 * @author mariotaku
 * 
 */
public interface WikiMethods {

	public FavoriteResponse<Wiki> addWikiFavorite(Wiki.Type type, long id, int favType) throws MoefouException;

	public FavoriteResponse<Wiki> addWikiFavorite(Wiki.Type type, long id, int favType, String status)
			throws MoefouException;

	public FavoriteResponse<Wiki> deleteWikiFavorite(Wiki.Type type, long id) throws MoefouException;

	/**
	 * 根据条件获取条目列表。需要API Key认证或者OAuth认证。
	 * 
	 * @param type 必填，条目类型，多个类型以数组表示。可使用 {@link Wiki.Type#ANIME} 代替
	 *            {@link Wiki.Type#TV}、{@link Wiki.Type#OVA}、
	 *            {@link Wiki.Type#OAD}、{@link Wiki.Type#MOVIE} 类型。
	 * @param paging
	 * @param tags 条目标签
	 * @return
	 * @throws MoefouException
	 */
	public ResponseList<Wiki> getWikis(Wiki.Type[] type, Paging paging, String... tags) throws MoefouException;

	public Wiki showWiki(Wiki.Type type, long id) throws MoefouException;

	public Wiki showWiki(Wiki.Type type, String name) throws MoefouException;
}
