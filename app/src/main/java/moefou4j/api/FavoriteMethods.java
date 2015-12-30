package moefou4j.api;

import moefou4j.MoefouException;
import moefou4j.http.HttpResponse;

public interface FavoriteMethods {

	/**
	 * 收藏一个对象。这个对象可以是条目或者子条目。不推荐使用此方法，因为返回的值不确定。建议使用分类更加明确的方法。
	 * 
	 * @param objectType 条目或子条目的类型。
	 * @param id 条目或子条目的id。
	 * @param favType 收藏类型号。1是普通收藏，2似乎是添加到“不再播放”的列表中。（是这样的吧……
	 * @throws MoefouException
	 */
	public HttpResponse addFavorite(String objectType, long id, int favType) throws MoefouException;

	/**
	 * 收藏一个对象，并发布一条动态。这个对象可以是条目或者子条目。不推荐使用此方法，因为返回的值不确定。建议使用分类更加明确的方法。
	 * 
	 * @param objectType 条目或子条目的类型。
	 * @param id 条目或子条目的id。
	 * @param favType 收藏类型号。1是普通收藏，2似乎是添加到“不再播放”的列表中。（是这样的吧……
	 * @param status 收藏时提交动态的文字内容。
	 * @throws MoefouException
	 */
	public HttpResponse addFavorite(String objectType, long id, int favType, String status) throws MoefouException;

	/**
	 * 取消收藏一个对象。这个对象可以是条目或者子条目。不推荐使用此方法，因为返回的值不确定。建议使用分类更加明确的方法。
	 * 
	 * @param objectType 条目或子条目的类型。
	 * @param id 条目或子条目的id。
	 * @throws MoefouException
	 */
	public HttpResponse deleteFavorite(String objectType, long id) throws MoefouException;

}
