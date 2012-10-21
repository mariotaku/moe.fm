package moefou4j.api;

import moefou4j.FavoriteResponse;
import moefou4j.MoefouException;
import moefou4j.Sub;

public interface SubMethods {

	/**
	 * 收藏一个子条目。
	 * 
	 * @param objectType 子条目的类型。
	 * @param id 子条目的id。
	 * @param favType 收藏类型号。1是普通收藏，2似乎是添加到“不再播放”的列表中。（是这样的吧……
	 * @throws MoefouException
	 */
	public FavoriteResponse<Sub> addSubFavorite(Sub.Type type, long id, int favType) throws MoefouException;

	/**
	 * 收藏一个子条目，并发布一条动态。
	 * 
	 * @param objectType 子条目的类型。
	 * @param id 子条目的id。
	 * @param favType 收藏类型号。1是普通收藏，2似乎是添加到“不再播放”的列表中。（是这样的吧……
	 * @param status 收藏时提交动态的文字内容。
	 * @throws MoefouException
	 */
	public FavoriteResponse<Sub> addSubFavorite(Sub.Type type, long id, int favType, String status)
			throws MoefouException;

	public FavoriteResponse<Sub> deleteSubFavorite(Sub.Type type, long id) throws MoefouException;

	/**
	 * 取消收藏一个子条目。
	 * 
	 * @param objectType 子条目的类型。
	 * @param id 子条目的id。
	 * @throws MoefouException
	 */
	public Sub showSub(Sub.Type type, long id) throws MoefouException;
}
